# JTinyCsvParser

JTinyCsvParser is a high-performance CSV parsing library for Java 21. Optimized for Virtual Threads and built entirely around the Java Stream API, this documentation explains the usage, configuration, and extensibility of the library through practical examples.

## Table of Contents

-   [1\. Setup](#1-setup "null")
-   [2\. Quick Start](#2-quick-start "null")
    -   [2.1 The Model](#21-the-model "null")
    -   [2.2 The Mapping](#22-the-mapping "null")
    -   [2.3 Handling Quotes](#23-handling-quotes "null")
-   [3\. Configuring and Running the Parser](#3-configuring-and-running-the-parser "null")
    -   [3.1 Setup the Parser](#31-setup-the-parser "null")
    -   [3.2 Execute the Parsing](#32-execute-the-parsing "null")
-   [4\. Core Concept: Record and Line Tracking](#4-core-concept-record-and-line-tracking "null")
    -   [4.1 LineNumber vs. RecordIndex](#41-linenumber-vs-recordindex "null")
    -   [4.2 Reasoning for the Distinction](#42-reasoning-for-the-distinction "null")
-   [5\. Result Handling: Success, Error, and Comment](#5-result-handling-success-error-and-comment "null")
-   [6\. Dynamic Mapping (Map<String, Object>)`](#6-dynamic-mapping-mapstring-object "null")
    -   [6.1 Inline Configuration](#61-inline-configuration "null")
    -   [6.2 Explicit Converters & Custom Types](#62-explicit-converters--custom-types "null")
    -   [6.3 Fallback Behavior](#63-fallback-behavior "null")
-   [7\. Advanced Usage: Custom Column Logic](#7-advanced-usage-custom-column-logic "null")
-   [8\. Type Definitions (CsvTypes)](#8-type-definitions-csvtypes "null")
    -   [8.1 Configuring Existing Types](#81-configuring-existing-types "null")
    -   [8.2 Writing a Custom Type](#82-writing-a-custom-type "null")

## 1\. Setup

To include JTinyCsvParser in your project, add the dependency to your build tool.

**Maven:**

```
<dependency>
    <groupId>de.bytefish</groupId>
    <artifactId>jtinycsvparser</artifactId>
    <version>2.0.0</version>
</dependency>

```

**Gradle:**

```
implementation 'de.bytefish:jtinycsvparser:3.0.0'

```

## 2. Quick Start

To parse a CSV file, you need a target model and a mapping definition.

### 2.1 The Model

The target of your parsing operation should be a class with a parameterless constructor (or a factory method) and appropriate setters.

```java
public class Person {
    private int id;
    private String name;

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }

    // Getters...
}
```

### 2.2 The Mapping

Create a `FluentMapping<T>` and define the relationship between CSV columns and model properties. JTinyCsvParser uses highly optimized **Type Tokens** (`CsvTypes`) to ensure compile-time safety and avoid autoboxing for primitive types.

**Option A: Mapping by Header Name (Recommended)**

This approach is flexible as it doesn't depend on the order of columns in the CSV file. The parser automatically resolves the names to indices.

```java
FluentMapping<Person> mapping = new FluentMapping<>(Person::new)
    // Uses primitive int-setter to avoid autoboxing
    .map(CsvTypes.INTEGER.primitive("ID", Person::setId))
    // Uses boxed setter (allows nulls for empty fields)
    .map(CsvTypes.STRING.boxed("Full Name", Person::setName));

```

**Option B: Mapping by Index**

Use this for files without headers or for maximum performance, as it bypasses header resolution.

```java
FluentMapping<Person> mapping = new FluentMapping<>(Person::new)
    // 0-based index: ID is column 0, Name is column 1
    .map(CsvTypes.INTEGER.primitive(0, Person::setId))
    .map(CsvTypes.STRING.boxed(1, Person::setName));

```

### 2.3 Handling Quotes

JTinyCsvParser automatically handles fields wrapped in quotes. This is essential when your data or your header names contain the delimiter character or line breaks.

```java
// Example CSV: "ID";"Full Name"
// The parser strips the quotes automatically.
// You map using the clean name:
.map(CsvTypes.STRING.boxed("Full Name", Person::setName));
```

Quoted fields can contain the delimiter (e.g., `"Doe, John"`) or even escaped quotes (e.g., `"The ""Great"" Gatsby"`), which the parser resolves before passing the value to the mapping.

## 3. Configuring and Running the Parser

The `CsvParser` is the central engine. It is stateless and can be reused for multiple parsing operations.

### 3.1 Setup the Parser

First, you combine your `CsvOptions` and your `FluentMapping` to create the parser instance.

```java
// 1. Define the technical format
CsvOptions options = new CsvOptions(
    ';',    // Delimiter
    '"',    // QuoteChar
    '"',    // EscapeChar
    '#',    // CommentCharacter
    true,   // SkipHeader
    StandardCharsets.UTF_8 // Encoding
);

// 2. Create the parser (Stateless and reusable)
CsvParser<Person> parser = new CsvParser<>(options, mapping);

```

### 3.2 Execute the Parsing

JTinyCsvParser provides full support for the Java `Stream` API. Crucially, the parsing process uses deferred execution (lazy loading) and is highly optimized for **Virtual Threads**. The file is read and parsed one record at a time as you consume the stream.

Always use a **try-with-resources** block when parsing from a `Path` to ensure the underlying file handles are closed properly.

```java
// The actual parsing happens lazily as the stream is consumed.
try (Stream<CsvMappingResult<Person>> stream = parser.stream(Path.of("data.csv"))) {
    stream.forEach(result -> {
        // Every result encapsulates Success, Error, or Comment states.
        if (result instanceof CsvMappingResult.Success<Person> success) {
            Person person = success.entity();
            System.out.println("Parsed: " + person.getName());
        }
    });
}

```

## 4. Core Concept: Record and Line Tracking

JTinyCsvParser distinguishes between two types of indices. This distinction is necessary because CSV files often deviate from a simple "one line equals one record" structure.

### 4.1 LineNumber vs. RecordIndex

-   `LineNumber`: Refers to the physical line in the source file (1-based).
-   `RecordIndex`: Refers to the logical data entity (0-based).

### 4.2 Reasoning for the Distinction

-   **Quoted Newlines**: If a CSV field contains a newline (e.g., a description field), a single logical record spans multiple physical lines. In this case, the `LineNumber` will point to the start of the record, but the next record's `LineNumber` will jump several lines ahead.
-   **Comments**: If `CommentCharacter` is set, comment lines occupy a physical `LineNumber` but do not increment the `RecordIndex`.
-   **Header**: The header row consumes a `LineNumber` but is not counted as a data `RecordIndex`.

**Usage Tip**: Always use `LineNumber` when reporting errors to users, as it corresponds directly to what they see in a text editor!

## 5. Result Handling: Success, Error, and Comment

The `CsvMappingResult<T>` is a **Java 21 Sealed Interface**, capturing every possible state of a row. You can use modern Pattern Matching (`switch`) to ensure all states are handled safely and expressively.

```java
try (Stream<CsvMappingResult<Person>> stream = parser.stream(Path.of("data.csv"))) {
    stream.forEach(result -> {
        switch (result) {
            case CsvMappingResult.Success<Person> s ->
                System.out.printf("[Record %d] Imported: %s%n", s.recordIndex(), s.entity().getName());

            case CsvMappingResult.Error<Person> e ->
                System.err.printf("[Line %d] Error: %s%n", e.lineNumber(), e.error().message());

            case CsvMappingResult.Comment<Person> c ->
                System.out.printf("[Line %d] Meta-Info: %s%n", c.lineNumber(), c.comment());
        }
    });
}

```

## 6. Dynamic Mapping (Map<String, Object>)

When the CSV schema is only known at runtime, or you want to avoid creating dedicated POJOs for simple scripts, you can parse rows directly into dynamic structures (`Map<String, Object>`).

### 6.1 Inline Configuration

Use the static factory methods on the `CsvParser` class. The schema is configured inline using a `Consumer<CsvSchema>`.

```java
CsvOptions options = new CsvOptions(';', '"', '"', null, false, StandardCharsets.UTF_8);

// Create the parser and configure the schema in one go
var parser = CsvParser.createMapParser(options, schema -> {
    schema.add("Id", CsvTypes.INTEGER);
    schema.add("Price", CsvTypes.DOUBLE);
});

try (Stream<CsvMappingResult<Map<String, Object>>> stream = parser.stream(Path.of("products.csv"))) {
    stream.forEach(result -> {
        if (result instanceof CsvMappingResult.Success<Map<String, Object>> s) {
            Map<String, Object> row = s.entity();
            System.out.printf("Item %s costs %s%n", row.get("Id"), row.get("Price"));
        }
    });
}
```

### 6.2 Explicit Converters & Custom Types

If you need a dynamic schema but with specialized parsing logic (e.g., specific date patterns), you can construct converters via `CsvTypes` inline:

```java
var parser = CsvParser.createMapParser(options, schema -> {
    schema.add("Id", CsvTypes.INTEGER);
    schema.add("BirthDate", CsvTypes.localDate("yyyy-MM-dd")); // Explicit formatting
});

```

### 6.3 Fallback Behavior

Any column present in the CSV header that is not mapped in your `CsvSchema` will automatically be parsed as a raw `String`. This prevents data loss while maintaining strict typing for the columns you specifically configure.

## 7. Advanced Usage: Custom Column Logic

If you need one-off parsing logic specifically bound to a column without formally creating a new reusable `CsvType`, you can pass a `BiConsumer` lambda directly.

```java
FluentMapping<Person> mapping = new FluentMapping<>(Person::new)
    .map("Complex_Col", (entity, rawValue) -> {
        // Complex custom logic here
        if (rawValue != null && rawValue.startsWith("A-")) {
            entity.setCategory(rawValue.substring(2));
        }
    });

```

## 8. Type Definitions (CsvTypes)

### 8.1 Configuring Existing Types

JTinyCsvParser ships with out-of-the-box tokens for common Java types (`STRING`, `INTEGER`, `DOUBLE`, `BOOLEAN`, `BIG_DECIMAL`, `UUID`, `LOCAL_DATE`, etc.). Date and Time classes provide helper methods for custom patterns:

```java
CsvType<LocalDate> customDate = CsvTypes.localDate("dd/MM/yyyy");

```

### 8.2 Writing a Custom Type

To create a reusable `CsvType` without implementing an interface from scratch, use the `CsvTypes.custom()` factory method with a mapping lambda.

```java
// Create a reusable token for Enums or specific formats
CsvType<MyStatus> STATUS_TYPE = CsvTypes.custom(MyStatus::valueOf);

// Use it across your mappings like any primitive
mapping.map(STATUS_TYPE.boxed("status_column", Person::setStatus));
```
