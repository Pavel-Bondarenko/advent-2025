import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Day05 {

    record Range(
            long start,
            long end
    ) {
        public Range(String range) {
            String[] borders = range.split("-");
            this(Long.parseLong(borders[0]), Long.parseLong(borders[1]));
        }

        public boolean contains(long value) {
            return value >= start && value <= end;
        }

        public long size() {
            return end - start + 1;
        }
    }

    static void main() {
        new Day05().run();
    }

    public void run() {
        try {
            List<String> input = readLinesFromInputFile("day_05_input.txt");
            List<Range> freshIngredientIdRanges = input
                    .stream()
                    .takeWhile(l -> !l.isBlank())
                    .map(Range::new)
                    .toList();
            List<Long> databaseIngredientIds = input
                    .stream()
                    .dropWhile(l -> !l.isBlank())
                    .skip(1)
                    .map(Long::valueOf)
                    .toList();
            IO.println(String.format(
                    "\nNumber of fresh ingredients = %s",
                    calculateNumberOfFreshIngredients(freshIngredientIdRanges, databaseIngredientIds)));
            IO.println(String.format(
                    "\nNumber of unique fresh ingredient ids = %s",
                    calculateNumberOfUniqueFreshIngredientIds(freshIngredientIdRanges)));
        } catch (Exception e) {
            IO.println(e.getMessage());
        }
    }

    private long calculateNumberOfFreshIngredients(
            List<Range> freshIngredientIdRanges,
            List<Long> databaseIngredientIds
    ) {
        return databaseIngredientIds
                .stream()
                .filter(id -> freshIngredientIdRanges.stream().anyMatch(r -> r.contains(id)))
                .count();
    }

    private long calculateNumberOfUniqueFreshIngredientIds(
            List<Range> freshIngredientIdRanges
    ) {
        return distinctRanges(freshIngredientIdRanges)
                .stream()
                .mapToLong(Range::size)
                .sum();
    }

    private List<Range> distinctRanges(
            List<Range> ranges
    ) {
        List<Range> distinct = new ArrayList<>();
        ranges.forEach(r -> {
            Range unique = r;
            Iterator<Range> iterator = distinct.iterator();

            while (iterator.hasNext()) {
                Range next = iterator.next();

                if (r.start <= next.end && r.end >= next.start) {
                    unique = new Range(Math.min(unique.start, next.start), Math.max(unique.end, next.end));
                    iterator.remove();
                }
            }
            distinct.add(unique);
        });

        return distinct;
    }

    private List<String> readLinesFromInputFile(
            String filename
    ) {
        try {
            Path filePath = Path.of(Thread.currentThread().getContextClassLoader().getResource(filename).toURI());
            return Files.readAllLines(filePath, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Can not read a file: %s", e.getMessage()), e);
        }
    }
}
