// Copyright (c) Philipp Wagner. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license information.

package de.bytefish.jtinycsvparser;

import com.sun.deploy.perf.PerfRollup;
import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import de.bytefish.jtinycsvparser.mapping.CsvMapping;
import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;
import de.bytefish.jtinycsvparser.mapping.CsvMappingTest;
import junit.framework.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

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

            Map(0, String.class, Person::setFirstName);
            Map(1, String.class, Person::setLastName);
            Map(2, LocalDate.class, Person::setBirthDate);
        }
    }

    @Test
    public void testParse() throws Exception {
        CsvParserOptions options = new CsvParserOptions(false, ",");
        PersonMapping mapping = new PersonMapping(() -> new Person());

        CsvParser<Person> parser = new CsvParser<>(options, mapping);

        ArrayList<String> csvData = new ArrayList<>();

        // Simulate CSV Data:
        csvData.add("Philipp,Wagner,1986-05-12");
        csvData.add("Max,Musterman,2000-01-07");

        List<CsvMappingResult<Person>> result =  parser.Parse(csvData)
                .collect(Collectors.toList()); // turn it into a List!

        Assert.assertNotNull(result);

        Assert.assertEquals(2, result.size());

        Assert.assertEquals("Philipp", result.get(0).getResult().firstName);
        Assert.assertEquals("Wagner", result.get(0).getResult().lastName);
        Assert.assertEquals(1986, result.get(0).getResult().getBirthDate().getYear());
        Assert.assertEquals(5, result.get(0).getResult().getBirthDate().getMonthValue());
        Assert.assertEquals(12, result.get(0).getResult().getBirthDate().getDayOfMonth());


        Assert.assertEquals("Max", result.get(1).getResult().firstName);
        Assert.assertEquals("Musterman", result.get(1).getResult().lastName);
        Assert.assertEquals(2000, result.get(1).getResult().getBirthDate().getYear());
        Assert.assertEquals(1, result.get(1).getResult().getBirthDate().getMonthValue());
        Assert.assertEquals(7, result.get(1).getResult().getBirthDate().getDayOfMonth());
    }
}