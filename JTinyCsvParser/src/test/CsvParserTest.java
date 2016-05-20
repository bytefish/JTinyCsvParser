// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import de.bytefish.jtinycsvparser.mapping.CsvMapping;
import de.bytefish.jtinycsvparser.mapping.CsvMappingError;
import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;

import de.bytefish.jtinycsvparser.utils.JUnitUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvParserTest {

    class Person {

        private String firstName;
        private String lastName;
        private LocalDate BirthDate;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public LocalDate getBirthDate() {
            return BirthDate;
        }

        public void setBirthDate(LocalDate birthDate) {
            BirthDate = birthDate;
        }
    }

    class PersonMapping extends CsvMapping<Person> {

        public PersonMapping(IObjectCreator creator) {
            super(creator);

            mapProperty(0, String.class, Person::setFirstName);
            mapProperty(1, String.class, Person::setLastName);
            mapProperty(2, LocalDate.class, Person::setBirthDate);
        }
    }

    @Test
    public void testParse() {
        CsvParserOptions options = new CsvParserOptions(false, ",");
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        ArrayList<String> csvData = new ArrayList<>();

        // Simulate CSV Data:
        csvData.add("Philipp,Wagner,1986-05-12");
        csvData.add(""); // An empty line... Should be skipped.
        csvData.add("Max,Musterman,2000-01-07");

        List<CsvMappingResult<Person>> result =  parser.parse(csvData)
                .collect(Collectors.toList()); // turn it into a List!

        Assert.assertNotNull(result);

        Assert.assertEquals(2, result.size());

        // Get the first person:
        Person person0 = result.get(0).getResult();

        Assert.assertEquals("Philipp", person0.firstName);
        Assert.assertEquals("Wagner", person0.lastName);
        Assert.assertEquals(1986, person0.getBirthDate().getYear());
        Assert.assertEquals(5, person0.getBirthDate().getMonthValue());
        Assert.assertEquals(12, person0.getBirthDate().getDayOfMonth());

        // Get the second person:
        Person person1 = result.get(1).getResult();

        Assert.assertEquals("Max", person1.firstName);
        Assert.assertEquals("Musterman", person1.lastName);
        Assert.assertEquals(2000, person1.getBirthDate().getYear());
        Assert.assertEquals(1, person1.getBirthDate().getMonthValue());
        Assert.assertEquals(7, person1.getBirthDate().getDayOfMonth());
    }

    @Test
    public void testParse_Parallel() {
        CsvParserOptions options = new CsvParserOptions(false, ",", true);
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        ArrayList<String> csvData = new ArrayList<>();

        // Simulate CSV Data:
        csvData.add("Philipp,Wagner,1986-05-12");
        csvData.add(""); // An empty line... Should be skipped.
        csvData.add("Max,Musterman,2000-01-07");

        List<CsvMappingResult<Person>> result =  parser.parse(csvData)
                .collect(Collectors.toList()); // turn it into a List!

        Assert.assertNotNull(result);

        Assert.assertEquals(2, result.size());

        // Get the first person:
        Person person0 = result.get(0).getResult();

        Assert.assertEquals("Philipp", person0.firstName);
        Assert.assertEquals("Wagner", person0.lastName);
        Assert.assertEquals(1986, person0.getBirthDate().getYear());
        Assert.assertEquals(5, person0.getBirthDate().getMonthValue());
        Assert.assertEquals(12, person0.getBirthDate().getDayOfMonth());

        // Get the second person:
        Person person1 = result.get(1).getResult();

        Assert.assertEquals("Max", person1.firstName);
        Assert.assertEquals("Musterman", person1.lastName);
        Assert.assertEquals(2000, person1.getBirthDate().getYear());
        Assert.assertEquals(1, person1.getBirthDate().getMonthValue());
        Assert.assertEquals(7, person1.getBirthDate().getDayOfMonth());
    }


    @Test
    public void testParse_ReadFromString() throws Exception {
        CsvParserOptions options = new CsvParserOptions(false, ",");
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        String lineSeparator = System.getProperty("line.separator");
        String csvData = "Philipp,Wagner,1986-05-12" + lineSeparator + "Max,Musterman,2000-01-07";

        List<CsvMappingResult<Person>> result =  parser.readFromString(csvData, new CsvReaderOptions(lineSeparator))
                .collect(Collectors.toList()); // turn it into a List!

        Assert.assertNotNull(result);

        Assert.assertEquals(2, result.size());

        // Get the first person:
        Person person0 = result.get(0).getResult();

        Assert.assertEquals("Philipp", person0.firstName);
        Assert.assertEquals("Wagner", person0.lastName);
        Assert.assertEquals(1986, person0.getBirthDate().getYear());
        Assert.assertEquals(5, person0.getBirthDate().getMonthValue());
        Assert.assertEquals(12, person0.getBirthDate().getDayOfMonth());

        // Get the second person:
        Person person1 = result.get(1).getResult();

        Assert.assertEquals("Max", person1.firstName);
        Assert.assertEquals("Musterman", person1.lastName);
        Assert.assertEquals(2000, person1.getBirthDate().getYear());
        Assert.assertEquals(1, person1.getBirthDate().getMonthValue());
        Assert.assertEquals(7, person1.getBirthDate().getDayOfMonth());
    }

    @Test
    public void testParse_WithError() {
        CsvParserOptions options = new CsvParserOptions(false, ",");
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        ArrayList<String> csvData = new ArrayList<>();

        // Simulate CSV Data:
        csvData.add("Philipp,Wagner,1986-05-12");
        csvData.add(""); // An empty line... Should be skipped.
        csvData.add("Max,Musterman,2000-");

        List<CsvMappingResult<Person>> result =  parser.parse(csvData)
                .collect(Collectors.toList()); // turn it into a List!

        Assert.assertNotNull(result);

        Assert.assertEquals(2, result.size());

        // Get the first person:
        Assert.assertEquals(true, result.get(0).isValid());

        // No Error!
        Assert.assertEquals(null, result.get(0).getError());

        // Check Person Data for Valid data:
        Person person0 = result.get(0).getResult();

        Assert.assertEquals("Philipp", person0.firstName);
        Assert.assertEquals("Wagner", person0.lastName);
        Assert.assertEquals(1986, person0.getBirthDate().getYear());
        Assert.assertEquals(5, person0.getBirthDate().getMonthValue());
        Assert.assertEquals(12, person0.getBirthDate().getDayOfMonth());

        // The second result is invalid:
        Assert.assertEquals(false, result.get(1).isValid());

        // So the Result must be null:
        Assert.assertEquals(null, result.get(1).getResult());

        CsvMappingError error = result.get(1).getError();

        Assert.assertEquals("2000-", error.getValue());
        Assert.assertEquals(2, error.getIndex());
        Assert.assertEquals( "Conversion failed", error.getMessage());
    }

    @Test
    public void testParse_toString() {
        CsvParserOptions options = new CsvParserOptions(false, ",");
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        JUnitUtils.assertDoesNotThrow(() -> parser.toString());
    }

    @Test
    public void testParse_readFromFile() {
        CsvParserOptions options = new CsvParserOptions(false, ",");
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        // Csv Data to parse (written to file...):
        String lineSeparator = System.getProperty("line.separator");
        String csvData = "Philipp,Wagner,1986-05-12" + lineSeparator + "Max,Musterman,2000-01-07";

        try {
            // Create a temporaryFile
            File temporaryFile = File.createTempFile("readFromFile", "csv");

            temporaryFile.deleteOnExit();

            // Write to file...
            BufferedWriter writer = new BufferedWriter(new FileWriter(temporaryFile));
            writer.write(csvData);
            writer.close();

            List<CsvMappingResult<Person>> result = new ArrayList<>();

            try(Stream<CsvMappingResult<Person>> stream = parser.readFromFile(temporaryFile.toPath(), StandardCharsets.UTF_8))
            {
                result = stream.collect(Collectors.toList()); // turn it into a List!
            }

            Assert.assertNotNull(result);

            Assert.assertEquals(2, result.size());

            // Get the first person:
            Person person0 = result.get(0).getResult();

            Assert.assertEquals("Philipp", person0.firstName);
            Assert.assertEquals("Wagner", person0.lastName);
            Assert.assertEquals(1986, person0.getBirthDate().getYear());
            Assert.assertEquals(5, person0.getBirthDate().getMonthValue());
            Assert.assertEquals(12, person0.getBirthDate().getDayOfMonth());

            // Get the second person:
            Person person1 = result.get(1).getResult();

            Assert.assertEquals("Max", person1.firstName);
            Assert.assertEquals("Musterman", person1.lastName);
            Assert.assertEquals(2000, person1.getBirthDate().getYear());
            Assert.assertEquals(1, person1.getBirthDate().getMonthValue());
            Assert.assertEquals(7, person1.getBirthDate().getDayOfMonth());

        } catch(Exception e) {
            Assert.assertEquals(true, false);
        }

        JUnitUtils.assertDoesNotThrow(() -> parser.toString());
    }


    @Test
    public void testParse_readFromFile_PathNotFound_Throws() {
        CsvParserOptions options = new CsvParserOptions(false, ",");
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        // Csv Data to parse (written to file...):
        String lineSeparator = System.getProperty("line.separator");
        String csvData = "Philipp,Wagner,1986-05-12" + lineSeparator + "Max,Musterman,2000-01-07";

        try {
            List<CsvMappingResult<Person>> result = new ArrayList<>();

            JUnitUtils.assertThrows(() ->
            {
                try (Stream<CsvMappingResult<Person>> stream = parser.readFromFile(FileSystems.getDefault().getPath("aaaa", "aaaa.txt"), StandardCharsets.UTF_8)) {
                    stream.collect(Collectors.toList()); // turn it into a List!
                }
            });

        } catch (Exception e) {
            // Should never get here!
            Assert.assertEquals(true, false);
        }
    }

}