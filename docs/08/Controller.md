# Controller csomag

A Controller csomag felelős a játékmenet irányításáért, a felhasználói parancsok értelmezéséért, a körök kezeléséért és a rendszeresemények koordinálásáért. A vezérlő egy **állapotgép**, amely szigorú szabályok szerint kezeli a játék fázisait és a játékosok interakcióit.

## 1. Controller osztály
- *Attribútumok*
  - `MapModel mapModel`: A játéktérkép állapotát fogja össze: Sávok, Utak, Kereszteződések állapota.
  - `ArrayList<Player> players`: A játékosok nyílvántartása.
  - `ArrayList<Car> npcs`: A nem játékos karakterek nyílvántartása, akik minden kör elején átlépnek a számukra következő sávra.
  - `GameState gameState`: A játék állapotának állapotgépét valósítja meg, amely mint hogy éppen melyik játékos jön, és ha takarító, akkor melyik hókotróval léphet.

- *Függvények*
  - `void handleEven(Message msg)`: A View vagy Proto felöl érkező események kezelése, és a gameState-et átlépteti a következő állapotba, amennyiben szükséges.
  - `void loadConfig(String c)`: Betölti a paraméterül kapott konfigot. Sorról sorra olvassa a konfigurációt és a megfelelő `Message`-ek hívásával beállítja a megfelelő állapotot.
  - Getterek, hogy a View és a Ptoro le tudják kérdezni a játék teljes állapotát.

## 2. GameState (Állapotgép megvalósítás)
- *Függvények*
  - `GameState handleEvent(Message msg)`: Virtuális függvény melyet a leszármazott állapotok valósítanak meg. A paraméterül kapott `msg` hatására elvégzi a szükséges mellékhatásokat, és visszaadja az új játék állapotot.

A `GameState` absztrakt osztály határozza meg az egyes fázisok viselkedését. A Controller az aktuális állapotnak delegálja az érkező üzenetek feldolgozását.

### SetupState
A játék kezdete előtti fázis, ahol a világ felépítése zajlik.
- **Felelősség:** Játékosok regisztrálása, a térkép gráfstruktúrájának módosítása (csomópontok és utak lehelyezése).
- **Üzenetek:** `AddPlayer`, `AddJunctions`, `AddRoad`.
- **Átmenet:** A `StartGame` üzenet hatására a rendszer kiválasztja az első játékost, és annak típusától függően `BusActionState` vagy `AwaitingPurchaseState` állapotba vált.

### AwaitingPurchaseState (Csak Cleaner esetén)
A takarító játékos körének első fázisa, ahol bővítheti a járműparkját.
- **Felelősség:** Új hókotrók (`SnowPlow`) vásárlása a játékos közös költségvetéséből. Egy fázisban több vásárlás is történhet.
- **Üzenetek:** `BuySnowPlow`, `FinishPurchase`.
- **Átmenet:** A `FinishPurchase` üzenet hatására a vezérlő kiválasztja a játékos első hókotróját, és `SnowPlowActionState`-be vált.

### SnowPlowActionState (Csak Cleaner esetén)
Egy konkrét hókotró lépéséért felelős állapot.
- **Logika:** Kezeli az adott hókotróra vonatkozó előkészítő akciókat és a mozgást:
    1. Opcionális tartozék vásárlás (`BuyAttachment`) - az új fej az aktuális gép készletébe kerül.
    2. Opcionális fejcsere (`SwapAttachment`).
    3. Opcionális újratöltés (`RefillAttachment`).
    4. Kötelező mozgás (`PickLane`).
- **Fontos:** Bármely fázisban érkező `PickLane` üzenet azonnal végrehajtja a mozgást és lezárja az adott jármű körét.
- **Átmenet:** A mozgás után:
    - Ha van a játékosnak következő hókotrója: Az állapotgép újraindul az új járművel (vissza az előkészítő fázisokhoz).
    - Ha nincs több hókotró: Átvált a következő játékosra.

### BusActionState (Csak BusDriver esetén)
A buszvezető köréért felelős állapot.
- **Felelősség:** A busz mozgásának kezelése.
- **Üzenetek:** `PickLane`.
- **Átmenet:** A mozgás után a vezérlő a következő játékosra vált. Ha az utolsó játékos is végzett, `SystemPhaseState`-be lép.

### SystemPhaseState
A kör végén lefutó automatizált fázis.
- **Felelősség:** Környezeti hatások (havazás), NPC autók mozgatása (BFS útvonalkereséssel), és a modell belső időzítéseinek frissítése.
- **Átmenet:** A műveletek végeztével a vezérlő elindítja az új kört (Round) az első játékossal.

## 3. Message ADT (Algebrai Adattípus)

Az összes View felől érkező bemenetet egy egységes `Message` típus reprezentálja, amely Java-ban egy **sealed interface**-ként, leszármazottai pedig **record**-okként valósulnak meg. Ez biztosítja a típusbiztos és kimerítő mintaillesztést (exhaustive pattern matching).

### A Message struktúrája

- **SetupMessages (SetupState):**
    - `AddPlayer(String role, String name)`: Új játékos hozzáadása.
    - `AddJunctions(int number)`: Meghatározott számú csomópont hozzáadása a térképhez.
    - `AddRoad(String junction1_id, String junction2_id)`: Új út lehelyezése két létező csomópont között.
    - `StartGame()`: Átlépés a játékfázisba.

- **PurchaseMessages (AwaitingPurchaseState):**
    - `BuySnowPlow()`: Új hókotró vásárlása a játékos keretéből.
    - `FinishPurchase()`: A globális vásárlási fázis lezárása és továbblépés az egyéni járművek kezeléséhez.

- **ActionMessages (Jármű-fázisok):**
    - `BuyAttachment(String type)`: Új fej vásárlása a **soron lévő** hókotróhoz.
    - `SwapAttachment(String attrType)`: Hókotró aktív fejének cseréje.
    - `RefillAttachment()`: Hókotró aktív fejének újratöltése.
    - `PickLane(String laneId, boolean clean)`: Kötelező mozgás és takarítás. **Ez az akció minden esetben lezárja az aktuális jármű körét.**
    - `SkipAction()`: Az aktuális döntési pont (vásárlás/csere/töltés) átugrása.

- **SystemMessages:**
    - `SaveGame(String path)`: Játékállapot mentése.
    - `LoadGame(String path)`: Mentett állapot betöltése.
    - `ExitGame()`: Kilépés.
    - `RequestHelp(String topic)`: Súgó megjelenítése.

## 4. TurnManager és Iteráció

A `TurnManager` két szinten követi a haladást:
1. **Játékos szint:** Ki a soron következő emberi résztvevő.
2. **Jármű szint:** A jelenlegi játékos melyik járművét irányítja éppen.
A kör akkor ér véget, ha az összes regisztrált `Player` összes `Vehicle` példánya végrehajtotta a `PickLane` parancsot, majd lefutott a rendszerfázis.
