package de.bytefish.jtinycsvparser;


import de.bytefish.jtinycsvparser.builder.IObjectCreator;
import de.bytefish.jtinycsvparser.mapping.CsvMapping;
import de.bytefish.jtinycsvparser.mapping.CsvMappingResult;
import de.bytefish.jtinycsvparser.typeconverter.LocalDateConverter;
import de.bytefish.jtinycsvparser.utils.MeasurementUtils;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

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

        try {

            List<String> result = Files.lines(FileSystems.getDefault().getPath("C:\\Users\\philipp\\Downloads\\csv", "201503hourly.txt"), StandardCharsets.UTF_8)
                    .collect(Collectors.toList()); // turn it into a List!


            Instant end = Instant.now();

            System.out.println(Duration.between(start, end));
        } catch(Exception e) {

        }
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
        MeasurementUtils.MeasureElapsedTime("LocalWeatherData_Sequential", () -> {
            // Read the file:
            List<CsvMappingResult<LocalWeatherData>> result =  parser.ReadFromFile(FileSystems.getDefault().getPath("C:\\Users\\philipp\\Downloads\\csv", "201503hourly.txt"), StandardCharsets.UTF_8)
                    .filter(e -> e.isValid())
                    .collect(Collectors.toList()); // turn it into a List!

            // Make sure we got the correct amount of lines in the file:
            Assert.assertEquals(4496262, result.size());
        });
    }
}
