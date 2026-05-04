# A Skeleton osztály felhasználói felülete és működése

Ez a dokumentum a `Skeleton` osztály felhasználói felületét, működését, illetve a tesztesetekhez tartozó konzolos log-kimenetek pontos formátumát írja le a korábbi szekvencia-diagramok alapján.

## I. Felhasználói felület

A Skeleton osztály egy interaktív, konzolos menün keresztül teszi lehetővé a tesztesetek kiválasztását és futtatását. Amikor a program elindul, kilistázza a választható teszteseteket, a felhasználó pedig egy sorszám megadásával választhatja ki a futtatni kívánt esetet:

```text
--- Skeleton Teszt Menü ---
1. Havazás
2. Hatásos általános takarítás: Hóeltakarítás söpréssel
3. Takarítás és ennek hatására egyenleg változása
4. Busz megfordulása és pontszerzés
5. Hókotró fej vásárlása
6. Hatástalan takarítás: Hóeltakarítás jégtörővel
7. Két autó ütközik jeges úton
8. Hókotró fej újratöltése
9. Busz/Autó interakció hóval borított úton
10. Hókotró fej csere
11. Autó interakció száraz úton
12. Hókotró interakció sószóró fejjel
13. Takarítás fogyóanyaggal: Hóeltakarítás sószóró fejjel
14. Autó interakció sószórt úton
15. Söprőfej hatása szomszédos sávok esetén
16. Hókotró utat próbál takarítani kifogyott Dragon vagy SaltVomitter fejjel
17. Jeges út takarítása IceBreaker fejjel
0. Kilépés
---------------------------
Válassz egy tesztesetet: _
```

## II. A Skeleton osztály belső működése

A Skeleton osztály felelős a tesztesetek futtatásáért (**Arrange** és **Act** fázisok) és az események naplózásáért.

**Objektumok nyilvántartása:**
Mivel hivatkozni kell az aktorokra a naplózás során, a Skeleton egy `HashMap`-ben tárolja a példányokat a nevük / azonosítójuk alapján (referencia-név párosítás). Konstruktorhívások során a példányok beregisztrálásra kerülnek.

**Főbb metódusok a működéshez és naplózáshoz:**
*   `printCall()`: Kiírja a felhasználói felületre a függvényhívást, a hívási lánc mélységének megfelelő indentálással (behúzással).
*   `printReturn()`: Kiírja a felhasználói felületre a visszatérést.
*   `askQuestion()`: Interaktál a felhasználóval és bekéri a válaszokat.
*   `pushEntity()`: Beregisztrálja a példányt egy konstruktor hívás után.
*   `popEntity()`: Kiregisztrálja a példányt (pl. törlés esetén).
*   `testCase1()`, `testCase2()`, stb...: Minden tesztesethez tartozik egy külön metódus, ami futtatja az adott eset **Arrange** és **Act** fázisát.

## III. Kimenet formátuma

A loggolás egyértelmű és konzisztens indentálással történik:
*   `-> [aktor/példány neve]:[Osztály].[metódus](paraméterek)` : Metódus hívás
*   `<- [opcionális visszatérési érték]` : Visszatérés
*   `? [kérdés] (opciók): [válasz]` : Kérdés a felhasználónak

A behúzás nagysága jelzi a hívás mélységét (függvényhívások és visszatérések egymásba ágyazása).

---

## IV. Tesztesetek Dialógusai (Kimenetek)

A következőkben a menüben kiválasztható tesztesetek elvárt kimenetei találhatók, amelyek pontosan lekövetik a rendszer szekvencia diagramjainak hívásait.

### 1. Havazás
A Controller (`sys`) elindítja a havazást az úton (`road`).
```text
[Teszt: Havazás]
-> road:Road.snowFall()
  -> lane:OutdoorLane.snowFall(amount)
    -> state:DryState.handleSnow(lane, amount)
      -> snowy:SnowyState.<<create>>
      <-
    <- snowy
    -> lane:OutdoorLane.setState(snowy)
    <-
  <-
<-
```

### 2. Hatásos általános takarítás: Hóeltakarítás söpréssel
A Cleaner (`c`) hókotróval takarít.
```text
[Teszt: Hatásos általános takarítás: Hóeltakarítás söpréssel]
-> p:SnowPlow.interactWithLane(lane)
  -> s:Sweeper.cleanLane(lane, currentTimestamp)
    -> lane:OutdoorLane.cleanWithHead(s)
      -> state:SnowyState.handleCleaning(lane, s)
        -> dry:DryState.<<create>>
        <-
      <- dry
      -> lane:OutdoorLane.setState(dry)
      <-
    <-
  <- true
<-
```

### 3. Takarítás és ennek hatására egyenleg változása
A hókotró (Sweeper-rel) takarít, majd jutalmat kap a sikeres feladatért.
```text
[Teszt: Takarítás és ennek hatására egyenleg változása]
-> p:SnowPlow.interactWithLane(lane)
  -> s:Sweeper.cleanLane(lane, currentTimestamp)
    -> lane:OutdoorLane.cleanWithHead(s)
    <-
  <- true
  -> c:Cleaner.addIncome(reward)
  <-
<-
```

### 4. Busz megfordulása és pontszerzés
A Controller (`sys`) utasítja a buszt a megfordulásra.
```text
[Teszt: Busz megfordulása és pontszerzés]
-> b:Bus.turnAround()
  -> b:Bus.addIncome(reward)
  <-
<-
```

### 5. Hókotró fej vásárlása
A Cleaner hókotró fejet vásárol.
```text
[Teszt: Hókotró fej vásárlása]
-> p:SnowPlow.buyAttachment(newS)
  -> newS:Sweeper.getPrice()
  <- price
  -> c:Cleaner.addIncome(-price)
  <-
<- true
```

### 6. Hatástalan takarítás: Hóeltakarítás jégtörővel
Havas utat (`SnowyState`) jégtörővel takarít.
```text
[Teszt: Hatástalan takarítás: Hóeltakarítás jégtörővel]
-> p:SnowPlow.interactWithLane(lane)
  -> ib:IceBreaker.cleanLane(lane, currentTimestamp)
    -> lane:OutdoorLane.cleanWithHead(ib)
      -> state:SnowyState.handleCleaning(lane, ib)
      <- state
    <-
  <- false
<-
```

### 7. Két autó ütközik jeges úton
A második autó rálép a jeges sávra, ahol a `handleTraffic` ütközést okoz.
```text
[Teszt: Két autó ütközik jeges úton]
-> lane:OutdoorLane.pushVehicle(c2, currentTimestamp)
  -> state:IcyState.handleTraffic(lane, c2)
    -> c1:Car.setImmobilized()
    <-
    -> c2:Car.setImmobilized()
    <-
    -> crashed:CrashedState.<<create>>
    <-
  <- crashed
  -> lane:OutdoorLane.setState(crashed)
  <-
<- false
```

### 8. Hókotró fej újratöltése
A Cleaner újratölti az aktív fogyóeszközös fejet (SaltVomitter/Dragon).
```text
[Teszt: Hókotró fej újratöltése]
-> p:SnowPlow.refillAttachment()
  -> c:Cleaner.getScore()
  <- currentBalance
  -> sv:SaltVomitter.refill(currentBalance)
  <- actualCost
  -> c:Cleaner.addIncome(-actualCost)
  <-
<-
```

### 9. Busz/Autó interakció hóval borított úton
Jármű `SnowyState` úton halad át. A letaposás hatására jegesedés (IcyState) történhet. (Az `icingCounter >= threshold` ág szerint naplózva).
```text
[Teszt: Busz/Autó interakció hóval borított úton]
-> lane:OutdoorLane.interactWithLane(v)
  -> state:SnowyState.handleTraffic(lane, v)
    -> icy:IcyState.<<create>>
    <-
  <- icy
  -> lane:OutdoorLane.setState(icy)
  <-
<-
```

### 10. Hókotró fej csere
A hókotró átváltja a meglévő fejet egy másik meglévő fejre.
```text
[Teszt: Hókotró fej csere]
-> p:SnowPlow.changeAttachment(iceBreaker)
<- true
```

### 11. Autó interakció száraz úton
Autó sikeresen halad át DryState úton.
```text
[Teszt: Autó interakció száraz úton]
-> lane:OutdoorLane.interactWithLane(v)
  -> state:DryState.handleTraffic(lane, v)
  <- state
<-
```

### 12. Hókotró interakció sószóró fejjel
Sószóró (`SaltVomitter`) fejet használ száraz (`DryState`) úton.
```text
[Teszt: Hókotró interakció sószóró fejjel]
-> p:SnowPlow.interactWithLane(lane)
  -> sv:SaltVomitter.cleanLane(lane, currentTimestamp)
    -> lane:OutdoorLane.cleanWithHead(sv)
      -> state:DryState.handleCleaning(lane, sv)
        -> salted:SaltedState.<<create>>
        <-
      <- salted
      -> lane:OutdoorLane.setState(salted)
      <-
    <-
  <- true
<-
```

### 13. Takarítás fogyóanyaggal: Hóeltakarítás sószóró fejjel
Sószóró (`SaltVomitter`) fejet használ havas (`SnowyState`) úton.
```text
[Teszt: Takarítás fogyóanyaggal: Hóeltakarítás sószóró fejjel]
-> p:SnowPlow.interactWithLane(lane)
  -> sv:SaltVomitter.cleanLane(lane, currentTimestamp)
    -> lane:OutdoorLane.cleanWithHead(sv)
      -> state:SnowyState.handleCleaning(lane, sv)
        -> salted:SaltedState.<<create>>
        <-
      <- salted
      -> lane:OutdoorLane.setState(salted)
      <-
    <-
  <- true
<-
```

### 14. Autó interakció sószórt úton
Autó sikeresen halad át `SaltedState` úton.
```text
[Teszt: Autó interakció sószórt úton]
-> lane:OutdoorLane.interactWithLane(v)
  -> state:SaltedState.handleTraffic(lane, v)
  <- state
<-
```

### 15. Söprőfej hatása szomszédos sávok esetén
A Sweeper a havat átdobja a szomszéd sávra, miközben az első sávot (lane1) takarítja.
```text
[Teszt: Söprőfej hatása szomszédos sávok esetén]
-> p:SnowPlow.interactWithLane(lane1)
  -> s:Sweeper.cleanLane(lane1, currentTimestamp)
    -> lane1:OutdoorLane.cleanWithHead(s)
      -> road:Road.snowFall(amount)
        -> lane2:OutdoorLane.snowFall(amount)
        <-
      <-
    <-
  <- true
<-
```

### 16. Hókotró utat próbál takarítani kifogyott Dragon vagy SaltVomitter fejjel
Az üres fej visszautasítja a műveletet (`false`).
```text
[Teszt: Hókotró utat próbál takarítani kifogyott Dragon vagy SaltVomitter fejjel]
-> p:SnowPlow.interactWithLane(lane)
  -> sv:SaltVomitter.cleanLane(lane, currentTimestamp)
  <- false
<-
```

### 17. Jeges út takarítása IceBreaker fejjel
Az IceBreaker feltöri a jeget, ezáltal `IcyState` -> `SnowyState` átmenetet produkálva.
```text
[Teszt: Jeges út takarítása IceBreaker fejjel]
-> p:SnowPlow.interactWithLane(lane)
  -> ib:IceBreaker.cleanLane(lane, currentTimestamp)
    -> lane:OutdoorLane.cleanWithHead(ib)
      -> state:IcyState.handleCleaning(lane, ib)
        -> snowy:SnowyState.<<create>>
        <-
      <- snowy
      -> lane:OutdoorLane.setState(snowy)
      <-
    <-
  <- true
<-
```