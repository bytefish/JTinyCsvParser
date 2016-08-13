.. _userguide_tokenizer:

Tokenizer
=========

Motivation
~~~~~~~~~~

There is no possible standard how CSV files are described. There is no schema, so every file you get 
may look completely different. This rules out one single strategy to tokenize a given line in your CSV 
data. 

Imagine a situation, where a column delimiter is also present in the column data like this:

::

    FirstNameLastName;BirthDate
    "Philipp,Wagner",1986/05/12
    ""Max,Mustermann",2014/01/01


A simple :csharp:`string.Split` with a comma as column delimiter will lead to wrong data, so the line 
needs to be split differently. And this is exactely where a :code:`Tokenizer` fits in.

So a :code:`Tokenizer` is used to split a given line of your CSV data into the column data.

Available Tokenizers
~~~~~~~~~~~~~~~~~~~~

StringSplitTokenizer
""""""""""""""""""""

The :java:`StringSplitTokenizer` splits a line at a given column delimiter.

::

    Philipp,Wagner,1986/05/12
    
Is tokenized into the following values:

* :code:`Philipp`
* :code:`Wagner`
* :code:`1986/05/12`

RFC4180Tokenizer
""""""""""""""""

The `RFC4180`_ proposes a specification for the CSV format, which is commonly accepted. You can use 
the :java:`RFC4180Tokenizer` to parse a CSV file in a `RFC4180`_-compliant format.

Example
^^^^^^^

Imagine a RFC4180-compliant CSV file with Person Names should be parsed. Each Person has a :code:`Name`, 
:code:`Age` and :code:`Description`. The :code:`Name` and :code:`Description` may contain the column 
delimiter and also double quotes.  

::

    Name, Age, Description
	"Michael, Chester", 24, "Also goes by ""Mike"", among friends that is"
	"Robert, Willliamson", , "All-around nice guy who always says hi"

The following example shows how to use the :code:`RFC4180Tokenizer` for the given example data.
	
.. code-block:: java

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
		
    }
	
RegularExpressionTokenizer
""""""""""""""""""""""""""

The :java:`RegularExpressionTokenizer` is an **abstract base class**, that uses a regular expression 
to match a given line. So if you need to match a line with a regular expression, you have to implement 
this base class.

The :java:`QuotedStringTokenizer` is a good example to start with.

QuotedStringTokenizer
"""""""""""""""""""""
 
The :java:`QuotedStringTokenizer` is an implementation of a :java:`RegularExpressionTokenizer`. It uses 
a (rather complicated) regular expression to leave data in a double quotes (:code:`""`) untouched. I highly 
recommend to use the `RFC4180Tokenizer`_ for working with complex CSV data. 

.. _RFC4180: http://tools.ietf.org/html/rfc4180
.. _TinyCsvParser: https://github.com/bytefish/TinyCsvParser
.. _NUnit: http://www.nunit.org
.. MIT License: https://opensource.org/licenses/MIT