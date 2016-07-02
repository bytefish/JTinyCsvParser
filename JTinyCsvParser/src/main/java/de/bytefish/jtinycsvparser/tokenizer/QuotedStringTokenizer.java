// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer;

import de.bytefish.jtinycsvparser.tokenizer.ITokenizer;
import de.bytefish.jtinycsvparser.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuotedStringTokenizer implements ITokenizer {

    private String columnDelimiter;
    private boolean trimValues;
    private Pattern pattern;

    public QuotedStringTokenizer(String columnDelimiter, boolean trimValues) {
        this.columnDelimiter = columnDelimiter;
        this.trimValues = trimValues;
        this.pattern = BuildPattern();
    }

    private Pattern BuildPattern() {
        return Pattern.compile(BuildRegExp());
    }

    private String BuildRegExp() {
        return String.format("((?<=\\\")[^\\\"]*(?=\\\"(%1$s|$)+)|(?<=%1$s|^)[^%1$s\\\"]*(?=%1$s|$))", columnDelimiter);
    }

    private String[] tokenizeWithRegExp(String input) {

        Matcher m = pattern.matcher(input);

        List<String> tokens = new ArrayList<>();

        while(m.find()) {
            tokens.add(m.group(1));
        }

        return tokens.toArray(new String[tokens.size()]);
    }

    @Override
    public String[] tokenize(String input) {

        String[] result = tokenizeWithRegExp(input);
        if(trimValues) {
            result = StringUtils.trimAllElements(result);
        }
        return result;
    }

    @Override
    public String toString() {
        return "QuotedStringTokenizer{" +
                "columnDelimiter='" + columnDelimiter + '\'' +
                ", pattern=" + pattern +
                '}';
    }
}
