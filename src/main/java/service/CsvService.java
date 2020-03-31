package service;

import static com.google.common.collect.Maps.newLinkedHashMap;
import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static java.util.stream.Collectors.toList;

import model.Statistic;
import utils.PrinterUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

public class CsvService {

    private static final String COULD_NOT_WRITE_STATISTIC_MESSAGE = "Could not write statistic to file %s. Exception was: %s";

    private static final String RESOURCES_LOCATION = "src/main/resources/";
    private static final String CSV_LOCATION = RESOURCES_LOCATION + "csv/";

    private static final String CSV_FILE_NAME_FORMAT = "%s.csv";
    private static final String STATISTIC_FORMAT = "%s,%s";

    public void singleAxisStatisticToCsv(final Statistic statistic, final Map<String, ? extends Comparable<?>> map, final int resultLimit) {
        final List<String> csv = getCsvLines(map, resultLimit);

        addLabels(csv, statistic.getStatisticKeyName(), statistic.getStatisticValueName());

        writeStatistic(statistic.getFileName(), csv);
    }

    //TODO: just better - load from file
    public void dualAxisStatisticToCsv(final Statistic firstStatistic, final Statistic secondStatistic,
                                           final Map<String, ?> firstMap, final Map<String, ?> secondMap,
                                           final Set<String> includedKeys) {
        final Map<String, String> resultMap = newLinkedHashMap();

        Stream.of(secondMap, firstMap)
                .flatMap(m -> m.entrySet().stream())
                .filter(entry -> includedKeys.contains(entry.getKey()))
                .forEach(entry -> mergeValues(resultMap, entry));

        final List<String> csv = getCsvLines(resultMap, includedKeys.size());

        addLabels(csv, firstStatistic.getStatisticKeyName(), secondStatistic.getStatisticValueName(), firstStatistic.getStatisticValueName());

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
        csv.add(0, String.join(",", labels));
    }

    private void writeStatistic(final String fileName, final List<String> csvLines) {
        try {
            final Path path = Paths.get(CSV_LOCATION + String.format(CSV_FILE_NAME_FORMAT, fileName));
            Files.write(path, csvLines, CREATE, TRUNCATE_EXISTING);
        } catch (final Exception e) {
            PrinterUtils.printSimple(String.format(COULD_NOT_WRITE_STATISTIC_MESSAGE, fileName, e));
        }
    }

}