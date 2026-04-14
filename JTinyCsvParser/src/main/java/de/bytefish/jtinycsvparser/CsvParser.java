package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.core.*;
import de.bytefish.jtinycsvparser.core.CsvFieldRange;
import de.bytefish.jtinycsvparser.mappings.CsvSchemaMapping;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * A high-performance CSV parser for Java 21, optimized for use with
 * Virtual Threads.
 * The API is fully designed around Java Streams.
 */
public class CsvParser<TEntity> {

    private final CsvOptions options;
    private final ICsvMapping<TEntity> mapping;
    private final Charset charset;

    private enum ParseState { NORMAL, IN_QUOTED_FIELD, AFTER_QUOTE }

    public CsvParser(CsvOptions options, ICsvMapping<TEntity> mapping) {
        this.options = options;
        this.mapping = mapping;
        this.charset = options.encoding() != null ? options.encoding() : StandardCharsets.UTF_8;
    }
    // --- Streaming API ---

    public Stream<CsvMappingResult<TEntity>> stream(String csvContent) {
        InputStream is = new ByteArrayInputStream(csvContent.getBytes(charset));
        return stream(is);
    }

    public Stream<CsvMappingResult<TEntity>> stream(Path path) throws IOException {
        InputStream is = Files.newInputStream(path);
        return stream(is).onClose(() -> {
            try { is.close(); } catch (IOException e) { throw new UncheckedIOException(e); }
        });
    }

    public Stream<CsvMappingResult<TEntity>> stream(InputStream is) {
        Iterator<CsvMappingResult<TEntity>> iter = new Iterator<>() {
            private final List<CsvMappingResult<TEntity>> buffer = new LinkedList<>();
            private final BufferedReader reader = new BufferedReader(new InputStreamReader(is, charset));
            private final StringBuilder recordBuilder = new StringBuilder();
            private CsvFieldRange[] currentRanges = new CsvFieldRange[64];
            private int recordIndex = 0;
            private int lineNumber = 1;
            private boolean isInitialized = false;
            private CsvHeaderResolution headerResolution = null;
            private boolean eof = false;

            @Override
            public boolean hasNext() {
                while (buffer.isEmpty() && !eof) {
                    try {
                        LogicalRecord record = readLogicalRecord(reader, recordBuilder);
                        if (record == null) { eof = true; break; }
                        if (record.isMalformed) {
                            buffer.add(new CsvMappingResult.Error<>(
                                    new CsvMappingError(recordIndex++, lineNumber, 0, "Unclosed quote"),
                                    recordIndex - 1, lineNumber));
                            lineNumber += record.linesConsumed;
                            continue;
                        }
                        ProcessingStep<TEntity> step = processLine(record.content, currentRanges, recordIndex, lineNumber, record.linesConsumed, isInitialized, headerResolution);
                        if (step.result() != null) buffer.add(step.result());
                        headerResolution = step.headerResolution();
                        isInitialized = step.initialized();
                        recordIndex = step.nextRecordIndex();
                        lineNumber = step.nextLineNumber();
                        currentRanges = step.currentRangeBuffer();
                    } catch (IOException e) { throw new UncheckedIOException(e); }
                }
                return !buffer.isEmpty();
            }

            @Override
            public CsvMappingResult<TEntity> next() {
                if (!hasNext()) throw new NoSuchElementException();
                return buffer.remove(0);
            }
        };
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iter, Spliterator.ORDERED), false);
    }

    private record ProcessingStep<T>(CsvMappingResult<T> result, int nextRecordIndex, int nextLineNumber, boolean initialized, CsvHeaderResolution headerResolution, CsvFieldRange[] currentRangeBuffer) {}

    private ProcessingStep<TEntity> processLine(String line, CsvFieldRange[] rangeBuffer, int recordIdx, int lineNo, int consumed, boolean initialized, CsvHeaderResolution headerResolution) {
        if (isComment(line)) return new ProcessingStep<>(new CsvMappingResult.Comment<>(line, -1, lineNo), recordIdx, lineNo + consumed, initialized, headerResolution, rangeBuffer);
        CsvFieldRange[] currentRanges = rangeBuffer;
        boolean currentInitialized = initialized;
        CsvHeaderResolution currentResolution = headerResolution;
        int currentRecordIdx = recordIdx;

        if (!currentInitialized) {
            currentInitialized = true;
            if (mapping instanceof IHeaderBinder binder && binder.needsHeaderResolution()) {
                SplitResult split = splitLine(line, currentRanges);
                currentRanges = split.ranges();
                CsvRow headerRow = new CsvRow(line, Arrays.copyOf(currentRanges, split.count()), options, currentRecordIdx, lineNo);
                currentResolution = binder.bindHeaders(headerRow);
                return new ProcessingStep<>(null, currentRecordIdx, lineNo + consumed, currentInitialized, currentResolution, currentRanges);
            }
            if (options.skipHeader()) return new ProcessingStep<>(null, currentRecordIdx, lineNo + consumed, currentInitialized, currentResolution, currentRanges);
        }

        SplitResult split = splitLine(line, currentRanges);
        currentRanges = split.ranges();
        CsvRow row = new CsvRow(line, Arrays.copyOf(currentRanges, split.count()), options, currentRecordIdx++, lineNo);
        try {
            return new ProcessingStep<>(mapping.map(row, currentResolution), currentRecordIdx, lineNo + consumed, currentInitialized, currentResolution, currentRanges);
        } catch (Exception ex) {
            return new ProcessingStep<>(new CsvMappingResult.Error<>(new CsvMappingError(currentRecordIdx - 1, lineNo, -1, ex.getMessage()), currentRecordIdx - 1, lineNo), currentRecordIdx, lineNo + consumed, currentInitialized, currentResolution, currentRanges);
        }
    }

    private record SplitResult(CsvFieldRange[] ranges, int count) {}

    private SplitResult splitLine(String line, CsvFieldRange[] ranges) {
        int rangeCount = 0; int fieldStart = 0;
        ParseState state = ParseState.NORMAL;
        boolean needsUnescape = false;
        CsvFieldRange[] currentRanges = ranges;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (rangeCount >= currentRanges.length) currentRanges = Arrays.copyOf(currentRanges, currentRanges.length * 2);
            switch (state) {
                case NORMAL -> {
                    if (c == options.quoteChar()) { state = ParseState.IN_QUOTED_FIELD; fieldStart = i; }
                    else if (c == options.delimiter()) {
                        currentRanges[rangeCount++] = new CsvFieldRange(fieldStart, i - fieldStart, false, false);
                        fieldStart = i + 1;
                    }
                }
                case IN_QUOTED_FIELD -> {
                    if (c == options.escapeChar() && i + 1 < line.length() && line.charAt(i + 1) == options.quoteChar()) { needsUnescape = true; i++; }
                    else if (c == options.quoteChar()) state = ParseState.AFTER_QUOTE;
                }
                case AFTER_QUOTE -> {
                    if (c == options.delimiter()) {
                        currentRanges[rangeCount++] = new CsvFieldRange(fieldStart, i - fieldStart, true, needsUnescape);
                        fieldStart = i + 1; state = ParseState.NORMAL; needsUnescape = false;
                    }
                }
            }
        }
        if (rangeCount >= currentRanges.length) currentRanges = Arrays.copyOf(currentRanges, currentRanges.length + 1);
        currentRanges[rangeCount++] = new CsvFieldRange(fieldStart, line.length() - fieldStart, state != ParseState.NORMAL, needsUnescape);
        return new SplitResult(currentRanges, rangeCount);
    }

    private record LogicalRecord(String content, int linesConsumed, boolean isMalformed) {}

    private LogicalRecord readLogicalRecord(BufferedReader reader, StringBuilder sb) throws IOException {
        sb.setLength(0); int lines = 0; boolean inQuotes = false; String line;
        while ((line = reader.readLine()) != null) {
            lines++;
            if (sb.length() > 0) sb.append('\n');
            sb.append(line);
            for (int i = 0; i < line.length(); i++) if (line.charAt(i) == options.quoteChar()) inQuotes = !inQuotes;
            if (!inQuotes) return new LogicalRecord(sb.toString(), lines, false);
        }
        return sb.length() > 0 ? new LogicalRecord(sb.toString(), lines, inQuotes) : null;
    }

    private boolean isComment(String line) {
        if (options.commentCharacter() == null) return false;
        String trimmed = line.stripLeading();
        return !trimmed.isEmpty() && trimmed.charAt(0) == options.commentCharacter();
    }
    /**
     * Creates a parser that maps rows to Map<String, Object> using the provided options and schema.
     */
    public static CsvParser<Map<String, Object>> createMapParser(CsvOptions options, CsvSchema schema) {
        return new CsvParser<>(options, new CsvSchemaMapping(schema));
    }

    /**
     * Creates a parser that maps rows to Map<String, Object> using a fluent schema configuration.
     */
    public static CsvParser<Map<String, Object>> createMapParser(CsvOptions options, Consumer<CsvSchema> configureSchema) {
        CsvSchema schema = new CsvSchema();
        configureSchema.accept(schema);
        return new CsvParser<>(options, new CsvSchemaMapping(schema));
    }

    /**
     * Creates a parser that maps rows to Map<String, Object> with default string-only mapping.
     */
    public static CsvParser<Map<String, Object>> createMapParser(CsvOptions options) {
        return new CsvParser<>(options, new CsvSchemaMapping());
    }

}