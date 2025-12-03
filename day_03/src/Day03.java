import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class Day03 {

    static void main(String[] args) {
        new Day03().run();
    }

    public void run() {
        try {
            List<String> batteries = readLinesFromInputFile("day_03_input.txt");
            System.out.printf(
                    "\nTotal 2 digit output joltage = %s",
                    calculateMaxOutputJoltage(batteries, 2));
            System.out.printf(
                    "\nTotal 12 digit output joltage = %s",
                    calculateMaxOutputJoltage(batteries, 12));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private long calculateMaxOutputJoltage(
            List<String> batteries,
            int numberOfDigitsInBattery
    ) {
        return batteries
                .stream()
                .map(j -> Arrays.stream(j.split("")).map(Long::valueOf).toList())
                .mapToLong(b -> calculateBatteryMaxJoltage(b, numberOfDigitsInBattery))
                .sum();
    }

    private long calculateBatteryMaxJoltage(
            List<Long> joltages,
            int numberOfDigits
    ) {
        long maxJoltage;

        if (numberOfDigits <= 1) {
            maxJoltage = joltages
                    .stream()
                    .mapToLong(Long::longValue)
                    .max()
                    .orElseThrow();
        } else {
            long firstDigit = joltages
                    .stream()
                    .limit(joltages.size() - numberOfDigits + 1)
                    .mapToLong(Long::longValue)
                    .max()
                    .orElseThrow();
            long lastNumber = calculateBatteryMaxJoltage(
                    joltages.subList(joltages.indexOf(firstDigit) + 1, joltages.size()),
                    numberOfDigits - 1);
            maxJoltage = firstDigit * Math.powExact(10L, numberOfDigits - 1) + lastNumber;
        }

        return maxJoltage;
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
