# 8.3 Tesztelést támogató programok tervei

## Áttekintés

A tesztelési infrastruktúra tisztán Java-alapú, külön segédprogram nélkül működik, és Windows-on is teljes mértékben futtatható. A tesztek a program saját parancsnyelve alapján íródnak (lásd CONFIG.md), és I/O átirányítással etethetők be a programnak.

---

## Egy teszt felépítése

Minden teszt két fájlból áll, a `tests/` könyvtárban, névkonvenció szerint:

| Fájl | Tartalom |
|---|---|
| `<teszt_neve>_input.txt` | Bemeneti szkript a program parancsnyelve szerint |
| `<teszt_neve>_expected.txt` | Elvárt snapshot kimenet |
| `<teszt_neve>_actual.txt` | Tényleges kimenet (futtatáskor generálódik) |

### Bemeneti fájl felépítése

A bemeneti fájl a CONFIG.md formátumát követi. Minden tesztnél kötelező a `randomoff <seed>` parancs a determinisztikusság biztosításához, majd a játék parancsai, végül egy `snapshot` hívás, amit `exit` zár le.

```
randomoff 39
carcount 4
addplayer bus player_1
addplayer cleaner player_2
start

pick lane_5
pick lane_6 -clean
snapshot tests/test_01_actual.txt
exit
```

### Elvárt kimeneti fájl felépítése

Az elvárt kimeneti fájl a SNAPSHOT.md-ben leírt formátumú snapshot. Manuálisan, referencia-futtatás alapján készül: a fejlesztő egy helyesnek ítélt futás eredményét tárolja el elvárt kimenetként.

---

## Egy teszt lefuttatása

A program standard bemenetről olvassa a parancsokat (`System.in`, a `CommandLineInterpreter` már ezt valósítja meg). Egy teszt lefuttatása Windows parancssori átirányítással:

```
java -jar game.jar < tests\test_01_input.txt
```

A `snapshot` parancs a bemeneti fájlban megadott elérési útra írja ki az aktuális állapotot. Az összehasonlítás ezután a generált és az elvárt fájl között történik.

---

## Tesztek futtatásának támogatása – TestRunner

A tesztek futtatásához egy `TestRunner` Java osztály készül. Ez a program:
- elindítja a játékot alprogramként (`ProcessBuilder`),
- a megadott bemeneti fájlt átirányítja a játék standard bemenetére,
- megvárja a futás végét,
- összehasonlítja a generált kimenetet az elvárttal,
- kiírja az eredményt.

### TestRunner – pszeudókód

```
osztály TestRunner:

    metódus runTest(testName: String) -> boolean:
        inputPath  ← "tests/" + testName + "_input.txt"
        expectedPath ← "tests/" + testName + "_expected.txt"
        actualPath ← "tests/" + testName + "_actual.txt"

        process ← ProcessBuilder("java", "-jar", "game.jar")
                        .redirectInput(inputPath)
                        .redirectOutput(actualPath)
                        .start()
        process.waitFor()

        ha filesAreEqual(expectedPath, actualPath):
            kiír "PASS: " + testName
            visszaad igaz
        különben:
            kiír "FAIL: " + testName
            printDiff(testName, expectedPath, actualPath)
            visszaad hamis

    metódus runAllTests() -> void:
        tests ← listFiles("tests/", végződés: "_input.txt")
        pass ← 0, fail ← 0

        minden testFile-ra tests-ben:
            testName ← testFile neve "_input.txt" nélkül
            ha runTest(testName): pass++
            különben: fail++

        kiír "---"
        kiír "Eredmény: " + pass + " PASS, " + fail + " FAIL"

    metódus linesOf(path: String) -> Lista<String>:
        visszaad Files.readAllLines(path)   // soronként olvassa be, CRLF és LF egyaránt kezeli

    metódus filesAreEqual(expectedPath: String, actualPath: String) -> boolean:
        expectedLines ← linesOf(expectedPath)
        actualLines   ← linesOf(actualPath)

        ha expectedLines.mérete != actualLines.mérete:
            visszaad hamis

        minden i-re 0-tól expectedLines.mérete-1-ig:
            ha expectedLines[i] != actualLines[i]:
                visszaad hamis

        visszaad igaz

    metódus printDiff(testName: String, expectedPath: String, actualPath: String) -> void:
        expectedLines ← linesOf(expectedPath)
        actualLines   ← linesOf(actualPath)
        maxSor ← max(expectedLines.mérete, actualLines.mérete)

        minden i-re 0-tól maxSor-1-ig:
            exp ← ha i < expectedLines.mérete: expectedLines[i], különben "<hiányzó sor>"
            act ← ha i < actualLines.mérete:   actualLines[i],   különben "<hiányzó sor>"
            ha exp != act:
                kiír "  Sor " + (i+1) + ":"
                kiír "    ELVÁRT:   " + exp
                kiír "    TÉNYLEGES: " + act
```

### TestRunner futtatása

Egy adott teszt futtatása:
```
java -cp game.jar TestRunner test_01
```

Az összes teszt futtatása:
```
java -cp game.jar TestRunner --all
```

---

## Teszt oráklum

A **teszt oráklum** az elvárt kimeneti fájlok összessége (`*_expected.txt`). Ezeket manuálisan, egyszer kell elkészíteni:

1. A fejlesztő lefuttatja a tesztet a helyesnek ítélt verzióval.
2. A generált snapshot fájlt átnevezi `*_expected.txt`-re.
3. A tartalmat szükség esetén manuálisan ellenőrzi/kiigazítja.

Az összehasonlítás **soronkénti szövegszintű egyezést** jelent. A `Files.readAllLines()` soronként olvassa be a fájlokat, és egységesen kezeli a Windows (CRLF) és Unix (LF) sortöréseket – így nem okoz hamis FAIL-t az, ha az elvárt fájlt Windows-on szerkesztik, a generált kimenet viszont LF-et használ. A teszt pontosan akkor PASS, ha minden sor egyezik.

Ha a snapshot formátum megváltozik (pl. új `@blokk` kerül bele), az érintett `*_expected.txt` fájlokat frissíteni kell, és a változást dokumentálni kell.

---

## Játék parancsnyelven való futtathatósága

A játék teljes mértékben irányítható a parancsnyelven keresztül, interaktív módban is. A `CommandLineInterpreter` a standard bementről (`System.in`) olvassa a sorokat, így a játék közvetlenül játszható kézzel begépelt parancsokkal is, nemcsak automatizált tesztfájlokból:

```
java -jar game.jar
```

Ez a tulajdonság biztosítja, hogy a tesztelési nyelv egyben a játék vezérlési nyelve is – nincs különálló tesztelési szintaxis.

