// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser.tokenizer.rfc4180;

import de.bytefish.jtinycsvparser.tokenizer.ITokenizer;
import org.junit.Assert;
import org.junit.Test;

public class RFC4180TokenizerTest {

    @Test
    public void Rfc4180_QuotedString_Double_Quoted_Data_Test()
    {
        // Use a " as Quote Character, a \\ as Escape Character and a , as Delimiter.
        Options options = new Options('"', '\\', ',');

        // Initialize the Rfc4180 Tokenizer:
        ITokenizer tokenizer = new RFC4180Tokenizer(options);

        // Initialize a String with Double Quoted Data:
        String line = "\"Michael, Chester\",24,\"Also goes by \"\"Mike\"\", among friends that is\"";

        // Split the Line into its Tokens:
        String[] tokens = tokenizer.tokenize(line);

        // And make sure the Quotes are retained:
        Assert.assertNotNull(tokens);

        Assert.assertEquals(3, tokens.length);

        Assert.assertEquals("Michael, Chester", tokens[0]);
        Assert.assertEquals("24", tokens[1]);
        Assert.assertEquals("Also goes by \"Mike\", among friends that is", tokens[2]);
    }

    @Test
    public void Rfc4180_Issue3_Empty_Column_Test()
    {
        // Use a " as Quote Character, a \\ as Escape Character and a , as Delimiter.
        Options options = new Options('"', '\\', ',');

        // Initialize the Rfc4180 Tokenizer:
        ITokenizer tokenizer = new RFC4180Tokenizer(options);

        // Initialize a String with Double Quoted Data:
        String line = "\"Robert, Willliamson\", , \"All-around nice guy who always says hi\"";

        // Split the Line into its Tokens:
        String[] tokens = tokenizer.tokenize(line);

        // And make sure the Quotes are retained:
        Assert.assertNotNull(tokens);

        Assert.assertEquals(3, tokens.length);

        Assert.assertEquals("Robert, Willliamson", tokens[0]);
        Assert.assertEquals("", tokens[1]);
        Assert.assertEquals("All-around nice guy who always says hi", tokens[2]);
    }

    @Test
    public void Rfc4180_Issue3_Empty_First_Column_Test()
    {
        // Use a " as Quote Character, a \\ as Escape Character and a , as Delimiter.
        Options options = new Options('"', '\\', ',');

        // Initialize the Rfc4180 Tokenizer:
        ITokenizer tokenizer = new RFC4180Tokenizer(options);

        // Initialize a String with Double Quoted Data:
        String line = " , 24 ,\"Great Guy\"";
        // Split the Line into its Tokens:
        String[] tokens = tokenizer.tokenize(line);

        // And make sure the Quotes are retained:
        Assert.assertNotNull(tokens);

        Assert.assertEquals(3, tokens.length);

        Assert.assertEquals("", tokens[0]);
        Assert.assertEquals("24", tokens[1]);
        Assert.assertEquals("Great Guy", tokens[2]);
    }

    @Test
    public void Rfc4180_Issue3_Empty_Last_Columns_Test()
    {
        // Use a " as Quote Character, a \\ as Escape Character and a , as Delimiter.
        Options options = new Options('"', '\\', ',');

        // Initialize the Rfc4180 Tokenizer:
        ITokenizer tokenizer = new RFC4180Tokenizer(options);

        // Initialize a String with Double Quoted Data:
        String line = "\"Robert, Willliamson\",27,";
        // Split the Line into its Tokens:
        String[] tokens = tokenizer.tokenize(line);

        // And make sure the Quotes are retained:
        Assert.assertNotNull(tokens);

        Assert.assertEquals(3, tokens.length);

        Assert.assertEquals("Robert, Willliamson", tokens[0]);
        Assert.assertEquals("27", tokens[1]);
        Assert.assertEquals("", tokens[2]);
    }

    @Test
    public void All_Empty_Last_Columns_Test()
    {
        // Use a " as Quote Character, a \\ as Escape Character and a , as Delimiter.
        Options options = new Options('"', '\\', ',');

        // Initialize the Rfc4180 Tokenizer:
        ITokenizer tokenizer = new RFC4180Tokenizer(options);

        // Initialize a String with Double Quoted Data:
        String line = ",,";

        // Split the Line into its Tokens:
        String[] tokens = tokenizer.tokenize(line);

        // And make sure the Quotes are retained:
        Assert.assertNotNull(tokens);

        Assert.assertEquals(3, tokens.length);

        Assert.assertEquals("", tokens[0]);
        Assert.assertEquals("", tokens[1]);
        Assert.assertEquals("", tokens[2]);
    }

    @Test
    public void All_Empty_Last_Column_Not_Empty_Test()
    {
        // Use a " as Quote Character, a \\ as Escape Character and a , as Delimiter.
        Options options = new Options('"', '\\', ',');

        // Initialize the Rfc4180 Tokenizer:
        ITokenizer tokenizer = new RFC4180Tokenizer(options);

        // Initialize a String with Double Quoted Data:
        String line = ",,a";
        // Split the Line into its Tokens:
        String[] tokens = tokenizer.tokenize(line);

        // And make sure the Quotes are retained:
        Assert.assertNotNull(tokens);

        Assert.assertEquals(3, tokens.length);

        Assert.assertEquals("", tokens[0]);
        Assert.assertEquals("", tokens[1]);
        Assert.assertEquals("a", tokens[2]);
    }
}