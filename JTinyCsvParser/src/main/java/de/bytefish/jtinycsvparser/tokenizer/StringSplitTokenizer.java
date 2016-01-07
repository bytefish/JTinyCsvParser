package de.bytefish.jtinycsvparser.tokenizer;

/**
 * Created by philipp on 1/6/2016.
 */
public class StringSplitTokenizer implements ITokenizer {

    private String columnDelimiter;
    private boolean trimLine;

    public StringSplitTokenizer(String columnDelimiter, boolean trimLine) {
        this.columnDelimiter = columnDelimiter;
        this.trimLine = trimLine;
    }

    @Override
    public String[] tokenize(String input) {
        if(trimLine) {
            return input.trim().split(columnDelimiter);
        }
        return input.split(columnDelimiter);
    }
}
