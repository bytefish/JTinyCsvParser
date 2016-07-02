// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.rfc4180;

import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;

public class PeekableReader implements AutoCloseable {

    private Reader reader;

    private PeekableReader(Reader reader) {
        this.reader = reader;
    }

    public int read() {
        return internalRead();
    }

    public String readTo(char readTo)
    {
        StringBuilder buffer = new StringBuilder();
        while(peek() != -1 && peek() != readTo)
        {
            buffer.append((char) read());
        }
        return buffer.toString();
    }

    public int peek() {
        return internalPeek();
    }

    private int internalRead() {
        try {
            return reader.read();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    private int internalPeek() {
        try {
            reader.mark(1);
            final int c = reader.read();
            reader.reset();
            return c;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        if(reader != null) {
            reader.close();
        }
    }

    public static PeekableReader createFromString(String value) {
        return new PeekableReader(new StringReader(value));
    }
}
