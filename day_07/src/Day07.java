import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Day07 {

    static void main() {
        new Day07().run();
    }

    public void run() {
        try {
            List<String> input = readLinesFromInputFile("day_07_input.txt");
            IO.println(String.format(
                    "\nNumber of beam splits = %s",
                    calculateBeamSplits(input)));
            IO.println(String.format(
                    "\nNumber of different timelines = %s",
                    calculateDifferentTimelines(input)));
        } catch (Exception e) {
            IO.println(e.getMessage());
        }
    }

    private int calculateBeamSplits(
            List<String> input
    ) {
        AtomicInteger splits = new AtomicInteger();
        Set<Integer> beamPositions = new HashSet<>();
        beamPositions.add(input.getFirst().indexOf("S"));

        input
                .stream()
                .skip(1)
                .forEach(l -> new ArrayList<>(beamPositions)
                        .forEach(i -> {
                            if (l.charAt(i) == '^') {
                                splits.getAndIncrement();
                                beamPositions.remove(i);
                                beamPositions.addAll(List.of(i - 1, i + 1));
                            }
                        }));

        return splits.get();
    }

    private long calculateDifferentTimelines(
            List<String> input
    ) {
        Map<Integer, Long> timelinePositionAndNumber = new HashMap<>();
        timelinePositionAndNumber.put(input.getFirst().indexOf("S"), 1L);

        input
                .stream()
                .skip(1)
                .forEach(l -> new HashMap<>(timelinePositionAndNumber)
                        .forEach((key, value) -> {
                            if (l.charAt(key) == '^') {
                                timelinePositionAndNumber.merge(
                                        key - 1,
                                        value,
                                        Math::addExact
                                );
                                timelinePositionAndNumber.merge(
                                        key + 1,
                                        value,
                                        Math::addExact
                                );
                                timelinePositionAndNumber.remove(key);
                            }
                        }));

        return timelinePositionAndNumber
                .values()
                .stream()
                .mapToLong(Number::longValue)
                .sum();
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
