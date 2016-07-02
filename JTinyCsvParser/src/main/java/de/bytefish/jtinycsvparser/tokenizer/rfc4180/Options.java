// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.rfc4180;

public class Options {

    private char quoteCharacter;
    private char escapeCharacter;
    private char delimiterCharacter;

    public Options(char quoteCharacter, char escapeCharacter, char delimiterCharacter) {
        this.quoteCharacter = quoteCharacter;
        this.escapeCharacter = escapeCharacter;
        this.delimiterCharacter = delimiterCharacter;
    }

    @Override
    public String toString() {
        return String.format("Options (QuoteCharacter = %s, EscapeCharacter = %s, DelimiterCharacter = %s)",
                quoteCharacter, escapeCharacter, delimiterCharacter);
    }

    public char getQuoteCharacter() {
        return quoteCharacter;
    }

    public char getEscapeCharacter() {
        return escapeCharacter;
    }

    public char getDelimiterCharacter() {
        return delimiterCharacter;
    }
}
