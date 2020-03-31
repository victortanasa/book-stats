package service;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.stream.Collectors.toList;

import model.Statistic;
import utils.PrinterUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvService {

    private static final String CANNOT_COMBINE_DIFFERENT_KEYS_MESSAGE = "Cannot combine statistics with different keys! First key: %s | Second key: %s";
    private static final String COULD_NOT_LOAD_STATISTICS_MESSAGE = "Could not load statistics from file, cannot compute. Exception was: %s";
    private static final String COULD_NOT_WRITE_STATISTIC_MESSAGE = "Could not write statistic to file %s. Exception was: %s";

    private static final String RESOURCES_LOCATION = "src/main/resources/";
    private static final String CSV_LOCATION = RESOURCES_LOCATION + "csv/";

    private static final String CSV_FILE_NAME_FORMAT = "%s.csv";
    private static final String STATISTIC_FORMAT = "%s,%s";

    private static final String NEWLINE = "\n";
    private static final String COMMA = ",";

    //TODO: double formatting
    public void singleAxisStatisticToCsv(final Statistic statistic, final Map<String, ?> map) {
        final List<String> csvLines = getCsvLines(map, statistic.getResultLimit());

        addLabels(csvLines, statistic.getKeyLabel(), statistic.getValueLabel());

        writeStatistic(statistic.getFileName(), csvLines);
    }

    public void dualAxisStatisticToCsv(final Statistic firstStatistic, final Statistic secondStatistic,
                                       final Set<String> includedKeys) {
        validateKeys(firstStatistic, secondStatistic);

        final Map<String, ?> firstMap = getStatisticFromFile(String.format(CSV_FILE_NAME_FORMAT, firstStatistic.getFileName()));
        final Map<String, ?> secondMap = getStatisticFromFile(String.format(CSV_FILE_NAME_FORMAT, secondStatistic.getFileName()));

        final Map<String, String> resultMap = newLinkedHashMap();

        Stream.of(secondMap, firstMap)
                .flatMap(m -> m.entrySet().stream())
                .filter(entry -> includedKeys.contains(entry.getKey()))
                .forEach(entry -> mergeValues(resultMap, entry));

        final List<String> csv = getCsvLines(resultMap, includedKeys.size());

        addLabels(csv, firstStatistic.getKeyLabel(), secondStatistic.getValueLabel(), firstStatistic.getValueLabel());

        writeStatistic(firstStatistic.getFileName() + secondStatistic.getFileName(), csv);
    }

    private static void mergeValues(final Map<String, String> resultMap, final Map.Entry<String, ?> entry) {
        final Object value = resultMap.get(entry.getKey());
        if (Objects.isNull(value)) {
            resultMap.put(entry.getKey(), entry.getValue().toString());
        } else {
            resultMap.put(entry.getKey(), String.format(STATISTIC_FORMAT, value, entry.getValue()));
        }
    }

    private static List<String> getCsvLines(final Map<String, ?> map, final int limit) {
        return map.entrySet().stream()
                .map(entry -> String.format(STATISTIC_FORMAT, entry.getKey(), entry.getValue()))
                .limit(limit)
                .collect(toList());
    }

    private static void addLabels(final List<String> csv, final String... labels) {
        csv.add(0, String.join(COMMA, labels));
    }

    private static Map<String, ?> getStatisticFromFile(final String fileName) {
        try {
            final String fileContent = readFileContent(fileName);
            return Stream.of(fileContent.split(NEWLINE))
                    .map(entry -> entry.split(COMMA))
                    .collect(Collectors.toMap(split -> split[0], split -> split[1],
                            (firstEntry, secondEntry) -> firstEntry,
                            LinkedHashMap::new));
        } catch (final Exception e) {
            throw new IllegalStateException(String.format(COULD_NOT_LOAD_STATISTICS_MESSAGE, e));
        }
    }

    private static String readFileContent(final String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(CSV_LOCATION + fileName)));
    }

    private void writeStatistic(final String fileName, final List<String> csvLines) {
        try {
            final Path path = Paths.get(CSV_LOCATION + String.format(CSV_FILE_NAME_FORMAT, fileName));
            Files.write(path, csvLines, CREATE, TRUNCATE_EXISTING);
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_WRITE_STATISTIC_MESSAGE, fileName, e));
        }
    }

    private void validateKeys(final Statistic firstStatistic, final Statistic secondStatistic) {
        if (!firstStatistic.getKeyLabel().equals(secondStatistic.getKeyLabel())) {
            throw new IllegalArgumentException(String.format(CANNOT_COMBINE_DIFFERENT_KEYS_MESSAGE,
                    firstStatistic.getKeyLabel(), secondStatistic.getKeyLabel()));
        }
    }

}