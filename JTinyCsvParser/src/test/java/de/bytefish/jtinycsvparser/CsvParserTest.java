package de.bytefish.jtinycsvparser;

import de.bytefish.jtinycsvparser.core.CsvMappingResult;
import de.bytefish.jtinycsvparser.core.CsvOptions;
import de.bytefish.jtinycsvparser.core.CsvType;
import de.bytefish.jtinycsvparser.core.CsvTypes;
import de.bytefish.jtinycsvparser.mappings.FluentMapping;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CsvParserTest {

    private final CsvOptions defaultOptions = new CsvOptions(',', '"', '\\', '#', true, StandardCharsets.UTF_8);

    @Test
    @DisplayName("Should parse CSV into a Map using a fluent schema configuration")
    void testMapMappingWithSchema() {
        // Arrange
        String csv = """
                id,name,active
                1,"John Doe",true
                2,Jane Smith,false
                """;

        CsvParser<Map<String, Object>> parser = CsvParser.createMapParser(defaultOptions, schema -> {
            schema.add("id", CsvTypes.INTEGER);
            schema.add("active", CsvTypes.BOOLEAN);
        });

        // Act
        var results = parser.stream(csv).toList();

        // Assert
        assertEquals(2, results.size());

        if (results.get(0) instanceof CsvMappingResult.Success<Map<String, Object>> success) {
            Map<String, Object> data = success.entity();
            assertEquals(1, data.get("id"));
            assertEquals("John Doe", data.get("name"));
            assertEquals(true, data.get("active"));
        } else {
            fail("Expected success result");
        }
    }

    @Test
    @DisplayName("Should parse CSV into a POJO using FluentMapping with primitive types")
    void testFluentMappingWithPrimitives() {
        // Arrange
        record User(int id, String name, double score) {
            // Internal mutable state for the fluent setter demonstration
            static class MutableUser {
                int id; String name; double score;
                void setId(int id) { this.id = id; }
                void setName(String name) { this.name = name; }
                void setScore(double score) { this.score = score; }
                User toUser() { return new User(id, name, score); }
            }
        }

        String csv = """
                ID,Name,Score
                101,Alice,88.5
                """;

        FluentMapping<User.MutableUser> mapping = new FluentMapping<>(User.MutableUser::new)
                .map(CsvTypes.INTEGER.primitive("ID", User.MutableUser::setId))
                .map(CsvTypes.STRING.boxed("Name", User.MutableUser::setName))
                .map(CsvTypes.DOUBLE.primitive("Score", User.MutableUser::setScore));

        CsvParser<User.MutableUser> parser = new CsvParser<>(defaultOptions, mapping);

        // Act
        var results = parser.stream(csv).toList();

        // Assert
        assertInstanceOf(CsvMappingResult.Success.class, results.get(0));
        User user = ((CsvMappingResult.Success<User.MutableUser>) results.get(0)).entity().toUser();

        assertEquals(101, user.id());
        assertEquals("Alice", user.name());
        assertEquals(88.5, user.score());
    }

    @Test
    @DisplayName("Should parse CSV into a POJO using FluentMapping with index-based columns")
    void testFluentMappingWithIndices() {
        // Arrange
        record User(int id, String name, double score) {
            static class MutableUser {
                int id; String name; double score;
                void setId(int id) { this.id = id; }
                void setName(String name) { this.name = name; }
                void setScore(double score) { this.score = score; }
                User toUser() { return new User(id, name, score); }
            }
        }

        // CSV data completely without a header row
        String csv = """
                101,Alice,88.5
                102,Bob,92.0
                """;

        // Since there is no header, we configure the options to not skip the first line
        CsvOptions optionsWithoutHeader = new CsvOptions(',', '"', '\\', '#', false, StandardCharsets.UTF_8);

        // Mapping directly via column index (0-based)
        FluentMapping<User.MutableUser> mapping = new FluentMapping<>(User.MutableUser::new)
                .map(CsvTypes.INTEGER.primitive(0, User.MutableUser::setId))
                .map(CsvTypes.STRING.boxed(1, User.MutableUser::setName))
                .map(CsvTypes.DOUBLE.primitive(2, User.MutableUser::setScore));

        CsvParser<User.MutableUser> parser = new CsvParser<>(optionsWithoutHeader, mapping);

        // Act
        var results = parser.stream(csv).toList();

        // Assert
        assertEquals(2, results.size());

        // Assert first record
        assertTrue(results.get(0) instanceof CsvMappingResult.Success);
        User user1 = ((CsvMappingResult.Success<User.MutableUser>) results.get(0)).entity().toUser();
        assertEquals(101, user1.id());
        assertEquals("Alice", user1.name());
        assertEquals(88.5, user1.score());

        // Assert second record
        assertTrue(results.get(1) instanceof CsvMappingResult.Success);
        User user2 = ((CsvMappingResult.Success<User.MutableUser>) results.get(1)).entity().toUser();
        assertEquals(102, user2.id());
        assertEquals("Bob", user2.name());
        assertEquals(92.0, user2.score());
    }

    @Test
    @DisplayName("Should handle malformed CSV rows and unclosed quotes")
    void testErrorHandling() {
        // Arrange
        String csv = """
                id,name
                1,"Malformed Name
                2,Valid Name
                """;

        CsvParser<Map<String, Object>> parser = CsvParser.createMapParser(defaultOptions);

        // Act
        var results = parser.stream(csv).toList();

        // Assert
        assertEquals(1, results.size());

        // Row 1 should be an error
        assertTrue(results.get(0) instanceof CsvMappingResult.Error, "First row should be an error due to unclosed quote");
        CsvMappingResult.Error<?> error = (CsvMappingResult.Error<?>) results.get(0);
        assertEquals("Unclosed quote", error.error().message());
    }

    @Test
    @DisplayName("Should respect comment characters and skip headers")
    void testCommentsAndHeaders() {
        // Arrange
        String csv = """
                # This is a global comment
                id,val
                # This is a local comment
                1,A
                """;

        CsvParser<Map<String, Object>> parser = CsvParser.createMapParser(defaultOptions);

        // Act
        var results = parser.stream(csv).toList();

        // Assert
        // Result should contain: 2 Comments and 1 Success (Header is skipped internally)
        long successCount = results.stream().filter(r -> r instanceof CsvMappingResult.Success).count();
        long commentCount = results.stream().filter(r -> r instanceof CsvMappingResult.Comment).count();

        assertEquals(1, successCount);
        assertEquals(2, commentCount);
    }

    @Test
    @DisplayName("Should handle custom types and type tokens")
    void testCustomCsvType() {
        // Arrange
        String csv = "code\nERR_01";

        CsvType<Integer> errorCodeType = CsvTypes.custom(val ->
                Integer.parseInt(val.substring(val.indexOf('_') + 1))
        );

        CsvParser<Map<String, Object>> parser = CsvParser.createMapParser(defaultOptions, schema -> {
            schema.add("code", errorCodeType);
        });

        // Act
        var results = parser.stream(csv).toList();

        // Assert
        Map<String, Object> data = ((CsvMappingResult.Success<Map<String, Object>>) results.get(0)).entity();
        assertEquals(1, data.get("code"));
    }
}