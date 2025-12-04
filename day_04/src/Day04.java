import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Day04 {

    static void main() {
        new Day04().run();
    }

    public void run() {
        try {
            List<String> diagram = readLinesFromInputFile("day_04_input.txt");
            int diagramWidth = diagram.getFirst().length();
            List<Character> diagramChars = diagram
                    .stream()
                    .flatMap(l -> Arrays.stream(l.split("")))
                    .map(s -> s.charAt(0))
                    .toList();
            IO.println(String.format(
                    "\nTotal access rolls = %s",
                    calculateNumberOfAccessedRolls(diagramChars, diagramWidth)));
            IO.println(String.format(
                    "\nTotal access rolls with repeats = %s",
                    calculateNumberOfAccessedRollsWithRepeats(diagramChars, diagramWidth)));
        } catch (Exception e) {
            IO.println(e.getMessage());
        }
    }

    private int calculateNumberOfAccessedRolls(
            List<Character> diagramChars,
            int diagramWidth
    ) {
        return findAccessedIndexes(diagramChars, diagramWidth).size();
    }

    private int calculateNumberOfAccessedRollsWithRepeats(
            List<Character> diagramChars,
            int diagramWidth
    ) {
        List<Character> diagramCharsCopy = new ArrayList<>(diagramChars);
        List<Integer> accessedIndexes = findAccessedIndexes(diagramCharsCopy, diagramWidth);

        if (accessedIndexes.isEmpty()) {
            return 0;
        }

        accessedIndexes.forEach(i -> diagramCharsCopy.set(i, '.'));

        return accessedIndexes.size() + calculateNumberOfAccessedRollsWithRepeats(diagramCharsCopy, diagramWidth);
    }

    private List<Integer> findAccessedIndexes(
            List<Character> diagramChars,
            int diagramWidth
    ) {
        return IntStream
                .range(0, diagramChars.size())
                .filter(i -> diagramChars.get(i) == '@')
                .filter(i -> isAccessedRoll(diagramChars, diagramWidth, i))
                .boxed()
                .toList();
    }

    private boolean isAccessedRoll(
            List<Character> diagramChars,
            int diagramWidth,
            int index
    ) {
        boolean notLeft = index % diagramWidth != 0;
        boolean notRight = index % diagramWidth != diagramWidth - 1;
        boolean notTop = index >= diagramWidth;
        boolean notBottom = index < diagramChars.size() - diagramWidth;

        return Stream
                .of(
                        notLeft ? diagramChars.get(index - 1) : '.',
                        notRight ? diagramChars.get(index + 1) : '.',
                        notTop ? diagramChars.get(index - diagramWidth) : '.',
                        notBottom ? diagramChars.get(index + diagramWidth) : '.',
                        notLeft && notTop ? diagramChars.get(index - diagramWidth - 1) : '.',
                        notRight && notTop ? diagramChars.get(index - diagramWidth + 1) : '.',
                        notLeft && notBottom ? diagramChars.get(index + diagramWidth - 1) : '.',
                        notRight && notBottom ? diagramChars.get(index + diagramWidth + 1) : '.')
                .filter(c -> c.equals('@'))
                .count() < 4;
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
