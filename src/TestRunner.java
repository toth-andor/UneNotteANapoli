import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Tesztek automatikus futtatását és eredményeinek kiértékelését végző segédosztály.
 *
 * <p>Minden teszt három fájlból áll a {@code tests/} könyvtárban:
 * <ul>
 *   <li>{@code <teszt_neve>_input.txt} – bemeneti szkript a játék parancsnyelve szerint</li>
 *   <li>{@code <teszt_neve>_expected.txt} – kézzel elkészített elvárt snapshot kimenet</li>
 *   <li>{@code <teszt_neve>_actual.txt} – a snapshot parancs által generált tényleges kimenet</li>
 * </ul>
 *
 * <p>Futtatás:
 * <pre>
 *   java -cp bin TestRunner
 * </pre>
 */
public class TestRunner {

    /**
     * Belépési pont. Listázza a teszteket, majd stdin-ről olvassa a választást.
     * 0 → összes teszt futtatása, egyébként az adott sorszámú teszt.
     */
    public static void main(String[] args) {
        TestRunner runner = new TestRunner();

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

        System.out.println("  [0] Összes teszt futtatása");
        for (int i = 0; i < testNames.size(); i++) {
            System.out.println("  [" + (i + 1) + "] " + testNames.get(i));
        }
        System.out.print("Választás (stdin): > ");

        String choice;
        try (Scanner scanner = new Scanner(System.in)) {
            choice = scanner.nextLine().trim();
        }

        if (choice.equals("0")) {
            int pass = 0, fail = 0;
            for (String testName : testNames) {
                if (runner.runTest(testName)) pass++;
                else fail++;
            }
            System.out.println("---");
            System.out.println("Eredmény: " + pass + " PASS, " + fail + " FAIL");
        } else {
            int idx;
            try {
                idx = Integer.parseInt(choice) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Érvénytelen választás: " + choice);
                return;
            }
            if (idx < 0 || idx >= testNames.size()) {
                System.out.println("Érvénytelen választás: " + choice);
                return;
            }
            runner.runTest(testNames.get(idx));
        }
    }

    /**
     * Lefuttat egy tesztet: elindítja a játékot alprogramként az input fájllal
     * a standard bemeneten, majd összehasonlítja a snapshot által generált actual
     * fájlt az elvárttal.
     *
     * <p>A stdout nincs átirányítva – az actual fájlt a bemeneti szkript
     * {@code snapshot} parancsa írja ki a szkriptben megadott útvonalra.
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
            ProcessBuilder pb = new ProcessBuilder("java", "-cp", "out/production/UneNotteANapoli22", "proto.CLIProto")
                    .redirectInput(new File(inputPath))
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

        List<String[]> diffs = compare(expectedPath, actualPath);

        if (diffs.isEmpty()) {
            System.out.println("PASS: " + testName);
            return true;
        } else {
            System.out.println("FAIL: " + testName);
            for (String[] diff : diffs) {
                System.out.println("  Sor " + diff[0] + ":");
                System.out.println("    ELVÁRT:    " + diff[1]);
                System.out.println("    TÉNYLEGES: " + diff[2]);
            }
            return false;
        }
    }

    /**
     * Soronként összehasonlítja az elvárt és a tényleges kimeneti fájlt.
     *
     * @param expectedPath az elvárt kimenet fájljának elérési útja
     * @param actualPath   a tényleges kimenet fájljának elérési útja
     * @return az eltérő sorok listája; minden elem {@code [sorszám, elvárt, tényleges]}
     */
    private List<String[]> compare(String expectedPath, String actualPath) {
        List<String[]> diffs = new ArrayList<>();
        try {
            List<String> expectedLines = Files.readAllLines(Paths.get(expectedPath));
            List<String> actualLines   = Files.readAllLines(Paths.get(actualPath));
            int maxLines = Math.max(expectedLines.size(), actualLines.size());

            for (int i = 0; i < maxLines; i++) {
                String exp = i < expectedLines.size() ? expectedLines.get(i) : "<hiányzó sor>";
                String act = i < actualLines.size()   ? actualLines.get(i)   : "<hiányzó sor>";
                if (!exp.equals(act)) {
                    diffs.add(new String[]{String.valueOf(i + 1), exp, act});
                }
            }
        } catch (IOException e) {
            diffs.add(new String[]{"?", "(olvasási hiba: " + e.getMessage() + ")", ""});
        }
        return diffs;
    }
}
