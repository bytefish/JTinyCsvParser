package de.bytefish.jtinycsvparser.core;

import java.nio.charset.Charset;

public record CsvOptions(char delimiter, char quoteChar, char escapeChar, Character commentCharacter, boolean skipHeader, Charset encoding) {}
