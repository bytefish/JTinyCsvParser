// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.rfc4180;

import de.bytefish.jtinycsvparser.utils.StringUtils;

import java.io.PushbackReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Reader {

    private final Options options;

    public Reader(Options options) {
        this.options = options;
    }

    public List<Token> readTokens(PeekableReader reader) {
        List<Token> tokens = new ArrayList<>();

        while (true)
        {
            Token token = nextToken(reader);

            tokens.add(token);

            if (token.getType() == TokenType.EndOfRecord)
            {
                break;
            }
        }

        return tokens;
    }

    private Token nextToken(PeekableReader reader) {
        skip(reader);

        String result;

        int c = reader.peek();

        if (c == options.getDelimiterCharacter()) {
            reader.read();

            return new Token(TokenType.Token);
        } else {
            if (isQuoteCharacter(c)) {
                result = readQuoted(reader);

                skip(reader);

                if (isEndOfStream(reader.peek())) {
                    return new Token(TokenType.EndOfRecord, result);
                }

                if (isDelimiter(reader.peek())) {
                    reader.read();
                }

                return new Token(TokenType.Token, result);
            }

            if (isEndOfStream(c)) {
                return new Token(TokenType.EndOfRecord);
            } else {
                result = reader.readTo(options.getDelimiterCharacter()).trim();

                skip(reader);

                if (isEndOfStream(reader.peek())) {
                    return new Token(TokenType.EndOfRecord, result);
                }

                if (isDelimiter(reader.peek())) {
                    reader.read();
                }

                return new Token(TokenType.Token, result);
            }
        }
    }

    private String readQuoted(PeekableReader reader) {
        reader.read();

        String result = reader.readTo(options.getQuoteCharacter());

        reader.read();

        if (reader.peek() != options.getQuoteCharacter()) {
            return result;
        }

        StringBuilder buffer = new StringBuilder(result);
        do {
            buffer.append((char) reader.read());
            buffer.append(reader.readTo(options.getQuoteCharacter()));

            reader.read();
        } while (reader.peek() == options.getQuoteCharacter());

        return buffer.toString();
    }

    private void skip(PeekableReader reader)
    {
        while (isWhiteSpace(reader.peek()))
        {
            reader.read();
        }
    }

    private boolean isQuoteCharacter(final int c) {
        return c == options.getQuoteCharacter();
    }

    private boolean isEndOfStream(final int c)
    {
        return c == -1;
    }

    private boolean isDelimiter(final int c)
    {
        return c == options.getDelimiterCharacter();
    }

    private boolean isWhiteSpace(final int c)
    {
        return c == ' ' || c == '\t';
    }

}
