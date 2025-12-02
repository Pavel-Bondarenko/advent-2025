import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class Day01 {

    public static void main(String[] args) {
        new Day01().run();
    }

    public void run() {
        try {
            List<String> rotations = readLinesFromInputFile("day_01_input.txt");
            System.out.printf("\nStop on zero %s times", calculateZeroStops(50, rotations));
            System.out.printf("\nRotate on zero %s times", calculateZeroRotates(50, rotations));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private int calculateZeroStops(
            int initialPosition,
            List<String> rotations
    ) {
        AtomicInteger zeroCounter = new AtomicInteger(0);
        rotations
                .stream()
                .map(e -> e.replace("L", "-"))
                .map(e -> e.replace("R", ""))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .reduce(initialPosition, (a, b) -> {
                    int result = a + b;
                    if (result % 100 == 0) {
                        zeroCounter.incrementAndGet();
                    }
                    return result;
                });
        return zeroCounter.get();
    }

    private int calculateZeroRotates(
            int initialPosition,
            List<String> rotations
    ) {
        AtomicInteger zeroCounter = new AtomicInteger(0);
        rotations
                .stream()
                .flatMapToInt(e -> e.charAt(0) == 'L'
                        ? IntStream.generate(() -> -1).limit(Integer.parseInt(e.substring(1)))
                        : IntStream.generate(() -> 1).limit(Integer.parseInt(e.substring(1))))
                .reduce(initialPosition, (a, b) -> {
                    int result = a + b;
                    if (result % 100 == 0) {
                        zeroCounter.incrementAndGet();
                    }
                    return result;
                });
        return zeroCounter.get();
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
