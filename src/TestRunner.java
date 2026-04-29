import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Tesztek automatikus futtatását és eredményeinek kiértékelését végző segédosztály.
 *
 * <p>Minden teszt három fájlból áll a {@code tests/} könyvtárban:
 * <ul>
 *   <li>{@code <teszt_neve>_input.txt} – bemeneti szkript a játék parancsnyelve szerint</li>
 *   <li>{@code <teszt_neve>_expected.txt} – kézzel elkészített elvárt snapshot kimenet</li>
 *   <li>{@code <teszt_neve>_actual.txt} – a játék által generált tényleges kimenet</li>
 * </ul>
 *
 * <p>Futtatás:
 * <pre>
 *   java -cp game.jar TestRunner test_01       // egy adott teszt
 *   java -cp game.jar TestRunner --all         // összes teszt
 * </pre>
 */
public class TestRunner {

    /**
     * Belépési pont. {@code --all} argumentummal az összes tesztet futtatja,
     * egyébként az argumentumként megadott nevű tesztet.
     *
     * @param args parancssori argumentumok: {@code --all} vagy {@code <teszt_neve>}
     */
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();
        if (args.length == 0) {
            System.out.println("Használat: java -cp game.jar TestRunner <teszt_neve>");
            System.out.println("           java -cp game.jar TestRunner --all");
            return;
        }
        if (args[0].equals("--all")) {
            runner.runAllTests();
        } else {
            runner.runTest(args[0]);
        }
    }

    /**
     * Lefuttat egy tesztet: elindítja a játékot alprogramként, az input fájlt
     * átirányítja a standard bemenetre, majd összehasonlítja a generált kimenetet
     * az elvárttal.
     *
     * @param testName a teszt neve (fájlnév-előtag, pl. {@code test_01})
     * @return {@code true}, ha a teszt átment; {@code false}, ha nem
     */
    private boolean runTest(String testName) {
        String inputPath    = "tests/" + testName + "_input.txt";
        String expectedPath = "tests/" + testName + "_expected.txt";
        String actualPath   = "tests/" + testName + "_actual.txt";

        if (!new File(inputPath).exists()) {
            System.out.println("FAIL: " + testName + " (input file not found: " + inputPath + ")");
            return false;
        }
        if (!new File(expectedPath).exists()) {
            System.out.println("FAIL: " + testName + " (expected file not found: " + expectedPath + ")");
            return false;
        }

        try {
            ProcessBuilder pb = new ProcessBuilder("java", "-jar", "game.jar")
                    .redirectInput(new File(inputPath))
                    .redirectOutput(new File(actualPath))
                    .redirectError(ProcessBuilder.Redirect.DISCARD);
            pb.directory(new File("."));
            Process process = pb.start();
            process.waitFor();
        } catch (IOException e) {
            System.out.println("FAIL: " + testName + " (process error: " + e.getMessage() + ")");
            return false;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("FAIL: " + testName + " (process error: " + e.getMessage() + ")");
            return false;
        }

        if (filesAreEqual(expectedPath, actualPath)) {
            System.out.println("PASS: " + testName);
            return true;
        } else {
            System.out.println("FAIL: " + testName);
            printDiff(testName, expectedPath, actualPath);
            return false;
        }
    }

    /**
     * Megkeresi és névsorban futtatja az összes tesztet a {@code tests/} könyvtárban,
     * majd összesített eredményt ír ki.
     */
    private void runAllTests() {
        File testsDir = new File("tests");
        if (!testsDir.exists() || !testsDir.isDirectory()) {
            System.out.println("Hiba: a 'tests/' könyvtár nem található.");
            return;
        }

        List<String> testNames = new ArrayList<>();
        File[] files = testsDir.listFiles((dir, name) -> name.endsWith("_input.txt"));
        if (files != null) {
            for (File f : files) {
                String name = f.getName();
                testNames.add(name.substring(0, name.length() - "_input.txt".length()));
            }
            testNames.sort(String::compareTo);
        }

        if (testNames.isEmpty()) {
            System.out.println("Nem található teszt a 'tests/' könyvtárban.");
            return;
        }

        int pass = 0, fail = 0;
        for (String testName : testNames) {
            if (runTest(testName)) pass++;
            else fail++;
        }

        System.out.println("---");
        System.out.println("Eredmény: " + pass + " PASS, " + fail + " FAIL");
    }

    /**
     * Beolvassa a megadott fájl sorait. A {@link Files#readAllLines} CRLF és LF
     * sortöréseket egyaránt kezel, így Windows és Unix között nincs hamis eltérés.
     *
     * @param path a fájl elérési útja
     * @return a fájl sorai listaként
     * @throws IOException ha a fájl nem olvasható
     */
    private List<String> linesOf(String path) throws IOException {
        return Files.readAllLines(Paths.get(path));
    }

    /**
     * Soronként összehasonlítja az elvárt és a tényleges kimeneti fájlt.
     *
     * @param expectedPath az elvárt kimenet fájljának elérési útja
     * @param actualPath   a tényleges kimenet fájljának elérési útja
     * @return {@code true}, ha minden sor egyezik; {@code false}, ha bármely sor különbözik
     *         vagy a fájlok nem olvashatók
     */
    private boolean filesAreEqual(String expectedPath, String actualPath) {
        try {
            List<String> expectedLines = linesOf(expectedPath);
            List<String> actualLines   = linesOf(actualPath);

            if (expectedLines.size() != actualLines.size()) return false;

            for (int i = 0; i < expectedLines.size(); i++) {
                if (!expectedLines.get(i).equals(actualLines.get(i))) return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Kiírja az elvárt és tényleges kimenet közötti eltérő sorokat, sorszámmal együtt.
     * Ha valamelyik fájlból hiányzik egy sor, {@code "<hiányzó sor>"} jelöléssel szerepel.
     *
     * @param testName     a teszt neve (csak a kimeneti fejléchez)
     * @param expectedPath az elvárt kimenet fájljának elérési útja
     * @param actualPath   a tényleges kimenet fájljának elérési útja
     */
    private void printDiff(String testName, String expectedPath, String actualPath) {
        try {
            List<String> expectedLines = linesOf(expectedPath);
            List<String> actualLines   = linesOf(actualPath);
            int maxLines = Math.max(expectedLines.size(), actualLines.size());

            for (int i = 0; i < maxLines; i++) {
                String exp = i < expectedLines.size() ? expectedLines.get(i) : "<hiányzó sor>";
                String act = i < actualLines.size()   ? actualLines.get(i)   : "<hiányzó sor>";
                if (!exp.equals(act)) {
                    System.out.println("  Sor " + (i + 1) + ":");
                    System.out.println("    ELVÁRT:    " + exp);
                    System.out.println("    TÉNYLEGES: " + act);
                }
            }
        } catch (IOException e) {
            System.out.println("  (diff olvasási hiba: " + e.getMessage() + ")");
        }
    }
}
