import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day06 {

    record Equation(
            List<Long> numbers,
            BinaryOperator<Long> operation
    ) {
        public long calculate() {
            return numbers
                    .stream()
                    .reduce(operation)
                    .orElseThrow();
        }
    }

    static void main() {
        new Day06().run();
    }

    public void run() {
        try {
            List<String> input = readLinesFromInputFile("day_06_input.txt");
            List<Equation> firstEquations = parseFirstEquations(input);
            List<Equation> secondEquations = parseSecondEquations(input);
            IO.println(String.format(
                    "\nSum of equations = %s",
                    calculateEquationsAndSumAll(firstEquations)));
            IO.println(String.format(
                    "\nSum of equations = %s",
                    calculateEquationsAndSumAll(secondEquations)));
        } catch (Exception e) {
            IO.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private List<Equation> parseFirstEquations(
            List<String> input
    ) {
        List<List<String>> splitInput = input
                .stream()
                .map(l -> Arrays
                        .stream(l.split(" "))
                        .filter(e -> !e.isBlank())
                        .toList())
                .toList();
        return IntStream
                .range(0, splitInput.getFirst().size())
                .mapToObj(i -> parseFirstEquation(i, splitInput))
                .toList();
    }

    private Equation parseFirstEquation(
            int index,
            List<List<String>> splitInput
    ) {
        List<Long> numbers = splitInput
                .stream()
                .map(l -> l.get(index))
                .takeWhile(e -> e.matches("\\d+"))
                .map(Long::valueOf)
                .toList();
        BinaryOperator<Long> operation = splitInput.getLast().get(index).equals("+")
                ? Math::addExact
                : Math::multiplyExact;
        return new Equation(numbers, operation);
    }

    private List<Equation> parseSecondEquations(
            List<String> input
    ) {
        int maxLength = input
                .stream()
                .mapToInt(String::length)
                .max()
                .orElseThrow();
        String rotatedInput = IntStream
                .range(0, maxLength)
                .boxed()
                .sorted(Comparator.reverseOrder())
                .map(i -> input
                        .stream()
                        .map(s -> s.length() > i
                                ? String.valueOf(s.charAt(i))
                                : "")
                        .filter(s -> !s.isBlank())
                        .collect(Collectors.joining()))
                .filter(s -> !s.isBlank())
                .collect(Collectors.joining(" "));
        return Arrays
                .stream(rotatedInput.split("(?<=[+*])"))
                .map(String::trim)
                .map(this::parseSecondEquation)
                .toList();
    }

    private Equation parseSecondEquation(
            String equation
    ) {
        String sign = equation.substring(equation.length() - 1);
        String numbersLine = equation.substring(0, equation.length() - 1);
        List<Long> numbers = Arrays
                .stream(numbersLine.split(" "))
                .map(Long::valueOf)
                .toList();
        BinaryOperator<Long> operation = sign.equals("+")
                ? Math::addExact
                : Math::multiplyExact;
        return new Equation(numbers, operation);
    }

    private long calculateEquationsAndSumAll(
            List<Equation> equations
    ) {
        return equations
                .stream()
                .mapToLong(Equation::calculate)
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
