# 8.3 Tesztelést támogató programok tervei

## Áttekintés

A tesztelési infrastruktúra tisztán Java-alapú, külön segédprogram nélkül működik, és Windows-on is teljes mértékben futtatható. A tesztek a program saját parancsnyelve alapján íródnak, és I/O átirányítással etethetők be a programnak.

---

## Egy teszt felépítése

Minden teszt két fájlból áll, a `tests/` könyvtárban, névkonvenció szerint:

| Fájl | Tartalom |
|---|---|
| `<teszt_neve>_input.txt` | Bemeneti szkript a program parancsnyelve szerint |
| `<teszt_neve>_expected.txt` | Elvárt snapshot kimenet |
| `<teszt_neve>_actual.txt` | Tényleges kimenet (futtatáskor generálódik) |

### Bemeneti fájl felépítése

A bemeneti fájl a parancsnyelv formátumát követi. Minden tesztnél kötelező a `randomoff <seed>` parancs a determinisztikusság biztosításához, majd a játék parancsai, végül egy `snapshot` hívás, amit `exit` zár le.

```
randomoff 39
carcount 4
addplayer bus player_1
addplayer cleaner player_2
start

pick lane_5
pick lane_6 -clean
snapshot tests\test_01_actual.txt
exit
```

### Elvárt kimeneti fájl felépítése

Az elvárt kimeneti fájl a snapshot által leírt formátumú. Manuálisan, referencia-futtatás alapján készül: a fejlesztő egy helyesnek ítélt futás eredményét tárolja el elvárt kimenetként.

---

## Tesztek futtatásának támogatása – TestRunner

A tesztek futtatásához egy `TestRunner` Java osztály készül. Stdin-ről olvassa a felhasználó választását, majd az adott tesztet alprogramként indítja el. A `snapshot` parancs a bemeneti szkriptben megadott útvonalra írja az actual fájlt, a TestRunner ezt hasonlítja össze az elvárttal.

### Menü

```
TestRunner indul, listázza a tests\ könyvtár *_input.txt fájljait:

  [0] Összes teszt futtatása
  [1] test_01
  [2] test_02
  ...

Választás (stdin): >
```

### TestRunner – pszeudókód

```
osztály TestRunner:

    metódus main():
        tesztek ← listFiles("tests\", végződés: "_input.txt")
        kiír menü(tesztek)
        választás ← stdin következő sora

        ha választás == "0":
            minden tesztre: futtat(teszt)
        különben:
            futtat(tesztek[választás])

    metódus futtat(testName):
        Main osztály elindítása alprogramként
            stdin ← tests\testName_input.txt
        megvárja a futás végét
        // a snapshot parancs a bemeneti szkriptben megadott útvonalra írja az actual fájlt

        eltérések ← összehasonlít(
            tests\testName_expected.txt,
            tests\testName_actual.txt
        )

        ha eltérések üres:
            kiír "PASS: " + testName
        különben:
            kiír "FAIL: " + testName
            minden eltérésre: kiír sor száma, elvárt, tényleges

    metódus összehasonlít(expected, actual):
        soronként olvassa be mindkét fájlt
        visszaadja azokat a sorokat ahol eltérés van
```

### Futtatás

```
java -cp bin TestRunner
```

---

## Teszt oráklum

A **teszt oráklum** az elvárt kimeneti fájlok összessége (`*_expected.txt`). Ezeket manuálisan, egyszer kell elkészíteni:

1. A fejlesztő lefuttatja a tesztet a helyesnek ítélt verzióval.
2. A generált snapshot fájlt átmásolja és `*_expected.txt`-ként menti el.
3. A tartalmat szükség esetén manuálisan ellenőrzi/kiigazítja.

A teszt pontosan akkor PASS, ha a TestRunner `összehasonlít` metódusa nem talál eltérést. Eltérés esetén kiírja az érintett sorokat és a különbséget.


---

## Tesztelési mód

Az állapot kiírása a `snapshot <path>` paranccsal történik, amit a fejlesztő explicit módon helyez el a bemeneti szkriptben, ezzel pontosan meghatározható, hogy mikor és hova íródjon ki az aktuális állapot.

---

## Játék parancsnyelven való futtathatósága

A játék teljes mértékben irányítható a parancsnyelven keresztül, interaktív módban is. A `CommandLineInterpreter` a standard bementről (`System.in`) olvassa a sorokat, így a játék közvetlenül játszható kézzel begépelt parancsokkal is, nemcsak automatizált tesztfájlokból:

```
java -cp bin proto.CLIProto
```

Ez a tulajdonság biztosítja, hogy a tesztelési nyelv egyben a játék vezérlési nyelve is, nincs különálló tesztelési szintaxis.


