package de.bytefish.jtinycsvparser;


import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import de.bytefish.jtinycsvparser.mapping.CsvMapping;
import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;
import de.bytefish.jtinycsvparser.typeconverter.LocalDateConverter;
import de.bytefish.jtinycsvparser.utils.MeasurementUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Spliterator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Ignore("Integration Test to evaluate the Performance")
public class IntegrationTest {

    public class LocalWeatherData
    {
        private String WBAN;

        private LocalDate Date;

        private String SkyCondition;

        public String getWBAN() {
            return WBAN;
        }

        public void setWBAN(String WBAN) {
            this.WBAN = WBAN;
        }

        public LocalDate getDate() {
            return Date;
        }

        public void setDate(LocalDate date) {
            Date = date;
        }

        public String getSkyCondition() {
            return SkyCondition;
        }

        public void setSkyCondition(String skyCondition) {
            SkyCondition = skyCondition;
        }
    }

    public class LocalWeatherDataMapper extends CsvMapping<LocalWeatherData>
    {
        public LocalWeatherDataMapper(IObjectCreator creator)
        {
            super(creator);

            MapProperty(0, String.class, LocalWeatherData::setWBAN);
            MapProperty(1, LocalDate.class, LocalWeatherData::setDate, new LocalDateConverter(DateTimeFormatter.ofPattern("yyyyMMdd")));
            MapProperty(4, String.class, LocalWeatherData::setSkyCondition);
        }
    }

    @Test
    public void testReadFromFile_SequentialRead() {

        MeasurementUtils.MeasureElapsedTime("LocalWeatherData_SequentialRead", () -> {

            // Read the file. Make sure to wrap it in a try, so the file handle gets disposed properly:
            try(Stream<String> stream = Files.lines(FileSystems.getDefault().getPath("C:\\Users\\philipp\\Downloads\\csv", "201503hourly.txt"), StandardCharsets.UTF_8)) {

                List<String> result = stream
                        .collect(Collectors.toList()); // turn it into a List!

                // Make sure we got the correct amount of lines in the file:
                Assert.assertEquals(4496263, result.size());

            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void testReadFromFile_LocalWeatherData_Sequential() {

        // Not in parallel:
        CsvParserOptions options = new CsvParserOptions(true, ",", false);
        // The Mapping to employ:
        LocalWeatherDataMapper mapping = new LocalWeatherDataMapper(() -> new LocalWeatherData());
        // Construct the parser:
        CsvParser<LocalWeatherData> parser = new CsvParser<>(options, mapping);
        // Measure the Time using the MeasurementUtils:
        MeasurementUtils.MeasureElapsedTime("LocalWeatherData_Sequential_Parse", () -> {

            // Read the file. Make sure to wrap it in a try, so the file handle gets disposed properly:
            try(Stream<CsvMappingResult<LocalWeatherData>> stream = parser.ReadFromFile(FileSystems.getDefault().getPath("C:\\Users\\philipp\\Downloads\\csv", "201503hourly.txt"), StandardCharsets.UTF_8)) {

                    List<CsvMappingResult<LocalWeatherData>> result = stream
                            .filter(e -> e.isValid())
                            .collect(Collectors.toList()); // turn it into a List!

                Assert.assertEquals(4496262, result.size());
            }
        });
    }

    @Test
    public void testReadFromFile_LocalWeatherData_Parallel() {

        // Not in parallel:
        CsvParserOptions options = new CsvParserOptions(true, ",", true);
        // The Mapping to employ:
        LocalWeatherDataMapper mapping = new LocalWeatherDataMapper(() -> new LocalWeatherData());
        // Construct the parser:
        CsvParser<LocalWeatherData> parser = new CsvParser<>(options, mapping);
        // Measure the Time using the MeasurementUtils:
        MeasurementUtils.MeasureElapsedTime("LocalWeatherData_Parallel_Parse", () -> {

            // Read the file. Make sure to wrap it in a try, so the file handle gets disposed properly:
            try(Stream<CsvMappingResult<LocalWeatherData>> stream = parser.ReadFromFile(FileSystems.getDefault().getPath("C:\\Users\\philipp\\Downloads\\csv", "201503hourly.txt"), StandardCharsets.UTF_8)) {

                List<CsvMappingResult<LocalWeatherData>> result = stream
                        .filter(e -> e.isValid())
                        .collect(Collectors.toList()); // turn it into a List!

                Assert.assertEquals(4496262, result.size());
            }
        });

    }
}
