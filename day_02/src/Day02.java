import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

public class Day02 {

    public static void main(String[] args) {
        new Day02().run();
    }

    public void run() {
        try {
            List<String> ranges = readLinesFromInputFile("day_02_input.txt");
            System.out.printf("\nRepeated ids sum = %s", calculateRepeatedIdSum(ranges));
            System.out.printf("\nRepeated at least twice ids sum = %s", calculateRepeatedAtLeastTwiceIdSum(ranges));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private long calculateRepeatedIdSum(
            List<String> ranges
    ) {
        return ranges
                .stream()
                .flatMap(line -> Arrays.stream(line.split(",")))
                .flatMap(range -> {
                    String[] borders = range.split("-");
                    long startInclude = Long.parseLong(borders[0]);
                    long endExclude = Long.parseLong(borders[1]) + 1;
                    return LongStream
                            .range(startInclude, endExclude)
                            .mapToObj(String::valueOf);
                })
                .filter(id -> id.length() % 2 == 0)
                .filter(id -> id.substring(0, id.length() / 2).equals(id.substring(id.length() / 2)))
                .mapToLong(Long::parseLong)
                .sum();
    }

    private long calculateRepeatedAtLeastTwiceIdSum(
            List<String> ranges
    ) {
        return ranges
                .stream()
                .flatMap(line -> Arrays.stream(line.split(",")))
                .flatMap(range -> {
                    String[] borders = range.split("-");
                    long startInclude = Long.parseLong(borders[0]);
                    long endExclude = Long.parseLong(borders[1]) + 1;
                    return LongStream
                            .range(startInclude, endExclude)
                            .mapToObj(String::valueOf);
                })
                .filter(this::digitsRepeatedAtLeastTwice)
                .mapToLong(Long::parseLong)
                .sum();
    }

    private boolean digitsRepeatedAtLeastTwice(String id) {
        return IntStream
                .range(1, id.length() / 2 + 1)
                .filter(n -> id.length() % n == 0)
                .anyMatch(n -> id.replace(id.substring(0, n), "").isEmpty());
    }

    private List<String> readLinesFromInputFile(
            String filename
    ) {
        try (InputStream inputStream = Thread
                .currentThread()
                .getContextClassLoader()
                .getResourceAsStream(filename);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            return bufferedReader.readAllLines();
        } catch (Exception e) {
            throw new RuntimeException(String.format("Can not read a file: %s", e.getMessage()), e);
        }
    }
}
