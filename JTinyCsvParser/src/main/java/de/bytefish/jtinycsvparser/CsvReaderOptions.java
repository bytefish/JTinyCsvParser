package de.bytefish.jtinycsvparser;

public class CsvReaderOptions {

    public String newLine;

    public CsvReaderOptions(String newLine) {
        this.newLine = newLine;
    }

    public String getNewLine() {
        return newLine;
    }

    @Override
    public String toString() {
        return "CsvReaderOptions{" +
                "newLine='" + newLine + '\'' +
                '}';
    }
}
