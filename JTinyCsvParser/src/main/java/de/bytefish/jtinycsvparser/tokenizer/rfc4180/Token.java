// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.rfc4180;

import de.bytefish.jtinycsvparser.utils.StringUtils;

public class Token {

    private final TokenType type;
    private final String content;

    public Token(TokenType type) {
        this(type, StringUtils.EmptyString);
    }

    public Token(TokenType type, String content) {
        this.type = type;
        this.content = content;
    }

    public TokenType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
