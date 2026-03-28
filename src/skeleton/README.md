## Példa

### 1. Havazás
A Controller (`sys`) elindítja a havazást az úton (`road`).


```text
[Teszt: Havazás]

-> road: Road.snowFall(10)
  -> lane: OutdoorLane.snowFall(10)
    -> dry: DryState.handleSnow(lane: OutdoorLane, 10)
    <- <<create>> snowy: SnowyState
    -> lane: OutdoorLane.setState(snowy: SnowyState)
    <-
  <-
<-

```

### A tesztesethez tartozó UseCaseImplementation (ez egy kicsit eltér attól amit néztünk, de lejjeb látjátok, hogy miért)

```java
  public static void UC1() {
    System.out.println("[Teszt: Havazás]\n");

    // Init
    Junction junction1 = new Junction(); Skeleton.pushEntity("junction1", junction1);
    Junction junction2 = new Junction(); Skeleton.pushEntity("junction2", junction2);

    Road road = new Road(junction1, junction2); Skeleton.pushEntity("road", road);

    OutdoorLane lane = new OutdoorLane(new DryState()); Skeleton.pushEntity("lane", lane);

    Skeleton.pushEntity("dry" ,lane.getCurrentState());

    road.addLane(lane);

    // Call trigger
    road.snowFall(10);
}
``` 

### Loggolás beállítása

### 1. Road osztály - snowFall

```java
public void snowFall(int amount) {

    if (Skeleton.ENABLE_LOGGING) {
        Skeleton.CallChainLogger.printCall(this, "snowFall(" + amount + ")");
    }

    for (Lane lane : lanes) {
        lane.snowFall(amount);
    }

    if (Skeleton.ENABLE_LOGGING) {
        Skeleton.CallChainLogger.printReturn(null);
    }
}
```

### 2. OutdoorLane osztály - snowFall

```java
@Override
public void snowFall(int snow) {

    if (Skeleton.ENABLE_LOGGING) {
        Skeleton.CallChainLogger.printCall(this, "snowFall(" + snow + ")");
    }

    // Eredetileg ez volt: currentState = currentState.handleSnow(this, snow);
    // Cserélni kell setState hívásra, hogy megjelenlen a naplózásban:
    setState(currentState.handleSnow(this, snow));

    if(Skeleton.ENABLE_LOGGING) {
        Skeleton.CallChainLogger.printReturn(null);
    }
}
```

### 3. DryState osztály - handleSnow

```java
public LaneState handleSnow(OutdoorLane lane, int amount) {

        SnowyState snowy = new SnowyState();

        if (Skeleton.ENABLE_LOGGING) {
            Skeleton.pushEntity("snowy", snowy);
            Skeleton.CallChainLogger.printCall(this, "handleSnow(" + Skeleton.getEntityByRef(lane) + ", " + amount +")");
            Skeleton.CallChainLogger.printReturn("<<create>> " + Skeleton.getEntityByRef(snowy));
        }

        return snowy;

    }
```

### 4. OutdoorLane osztály - setState

```java
public void setState(LaneState s) {
        if(Skeleton.ENABLE_LOGGING) {
            Skeleton.CallChainLogger.printCall(this, "setState(" + Skeleton.getEntityByRef(s) + ")");
            Skeleton.CallChainLogger.printReturn(null);
        }
        this.currentState = s;
    }
```

---

### `Note`

### Az alapján, ahogy a hívásban beszéltük, így nézett volna ki a teszteset:

```java
import skeleton.Skeleton;
import states.DryState;

public static void UC1() {
    System.out.println("[Teszt: Havazás]\n");

    // Init
    Junction junction1 = new Junction();
    Skeleton.pushEntity("junction1", junction1);
    Junction junction2 = new Junction();
    Skeleton.pushEntity("junction2", junction2);

    Road road = new Road(junction1, junction2);
    Skeleton.pushEntity("road", road);

    OutdoorLane lane = new OutdoorLane();
    Skeleton.pushEntity("lane", lane);

    LaneState dry = new DryState();
    Skeleton.pushEntity("dry", dry);
    
    lane.setState(dry);
    road.addLane(lane);

    // Call trigger
    road.snowFall(10);

}
``` 

### Viszont ezzel az a baj, hogy mivel a setState loggolja magát:
```java
    public void setState(LaneState s) {
    if(Skeleton.ENABLE_LOGGING) {
        Skeleton.CallChainLogger.printCall(this, "setState(" + Skeleton.getEntityByRef(s) + ")");
        Skeleton.CallChainLogger.printReturn(null);
    }
    this.currentState = s;
}
```
### Ezért az inicializáció során hívott  `lane.setState(dry)` is megjelenik a naplózásban:


```text
[Teszt: Havazás]

-> lane: OutdoorLane.setState(dry: DryState)
<-
-> road: Road.snowFall(10)
  -> lane: OutdoorLane.snowFall(10)
    -> dry: DryState.handleSnow(lane: OutdoorLane, 10)
    <- <<create>> snowy: SnowyState
    -> lane: OutdoorLane.setState(snowy: SnowyState)
    <-
  <-
<-

```
### Ez viszont nekünk nem kell, úgyhogy kicsit átvariáltam az `OutdoorLane` osztályt: 
* Kapott egy `defaultState` attribútumot, amit a konstruktorában be lehet állítani
* Kapott egy gettert ami visszaadja a `currentState`-et


```java
public class OutdoorLane extends Lane {

    private LaneState defaultState;

    /**
     * A sáv aktuális állapota (pl. száraz, havas, jeges, sózott, balesetes).
     */
    private LaneState currentState;

    public LaneState getCurrentState() {
        return currentState;
    }

    public OutdoorLane(LaneState _defaultState) {
        super();
        this.defaultState = _defaultState;
        this.currentState = _defaultState;
    }
}
```
És innentől az inicializálásnál nem kell `setState`, hanem csak megadjuk a konstruktorában a tesztesetnél aktuális kezdőállapotot, majd lekérjük a getterrel a `currentState`
-et, és hozzáadjuk a Map-hez.

`Note: ` így már a `currentState`-nek nincs alapértelmezett `DryState` értéke, (nem minden esetben ez a kiindulópont).

Ha ez nektek is rendben van, akkor kérlek majd dobjátok bele a végleges verzióba.
