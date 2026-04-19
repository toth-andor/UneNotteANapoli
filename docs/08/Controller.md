# Controller csomag

A Controller csomag felelős a játékmenet irányításáért, a felhasználói parancsok értelmezéséért, a körök kezeléséért és a rendszeresemények koordinálásáért. A vezérlő egy állapotgép, amely szigorú szabályok szerint kezeli a játék fázisait és a játékosok interakcióit.

## 1. Osztályok dokumentációja

### Controller
#### Felelősség
A rendszer központi osztálya, amely a State tervezési mintát alkalmazva állapotgépként működik. Kontextusként tárolja a játék aktuális állapotát, és az `IController` interfész megvalósítójaként az érkező üzeneteket továbbítja a jelenlegi `GameState` példánynak. Koordinálja a modell, a turn manager és a rendszerfázisért felelős komponensek munkáját.
#### Ősosztályok
Object → Controller
#### Interfészek
IController
#### Attribútumok
- `mapModel`: A játéktérkép (gráf) és a modell állapotának összefogója: - MapModel
- `players`: A regisztrált játékosok listája: - ArrayList<Player>
- `gameState`: Az állapotgép aktuális állapota: - GameState
- `turnManager`: A körök és a soron lévő egységek kezelője: - TurnManager
- `npcHandler`: Az NPC autók mozgásáért felelős segédkomponens: - NPCHandler
- `weatherManager`: A havazás szimulációjáért felelős segédkomponens: - WeatherManager
#### Metódusok
- `void receive(Message msg)`: A külső felületekről (View/Proto) érkező üzenetek fogadása és továbbítása a `gameState.process(msg)` hívással: + (Public) gameState firssítése a visszaadott GameState-re
- `void loadConfig(String cfg)`: Soronként feldolgozza a kapott konfigot, és minden sorhoz hív egy `reciev(msg)`-t a megfelelő `Message`-dszel.

---

### GameState (Abstract)
#### Felelősség
Az állapotgép állapotainak közös absztrakt ősosztálya. Definiálja az üzenetek feldolgozásának keretrendszerét. Az alapértelmezett implementáció minden üzenetre hibát vagy elutasítást ad vissza, a konkrét állapotok csak a számukra releváns üzeneteket definiálják felül.
#### Ősosztályok
Object → GameState
#### Interfészek
Nincs.
#### Attribútumok
- `controller`: Visszamutató referencia a kontextusra az állapotátmenetekhez: # Controller
#### Metódusok
- `void process(Message msg)`: Absztrakt metódus az üzenetek feldolgozására: + (Abstract)

---

### SetupState
#### Felelősség
A játék kezdete előtti fázis, ahol a világ felépítése zajlik. Ebben az állapotban adhatóak hozzá a játékosok és építhető fel a térkép (junction-ök és utak lehelyezése).
#### Ősosztályok
Object → GameState → SetupState
#### Interfészek
Nincs.
#### Attribútumok
Nincs saját attribútum.
#### Metódusok
- `void process(Message msg)`: Felüldefiniálja az AddPlayer, AddJunctions, AddRoad és StartGame üzenetek kezelését. A StartGame hatására átlépteti a vezérlőt az első játékos típusától függően `AwaitingPurchaseState` vagy `BusActionState` állapotba: + (Public)

---

### AwaitingPurchaseState
#### Felelősség
A takarító (Cleaner) játékos körének kezdő fázisa. Ebben az állapotban a játékos tetszőleges számú új hókotrót vásárolhat a közös keretéből, mielőtt elkezdené a meglévő járművei mozgatását.
#### Ősosztályok
Object → GameState → AwaitingPurchaseState
#### Interfészek
Nincs.
#### Attribútumok
- `activePlayer`: A soron lévő takarító játékos: - Cleaner
#### Metódusok
- `void process(Message msg)`: Kezeli a BuySnowPlow és FinishPurchase üzeneteket. A FinishPurchase hatására kiválasztja a játékos első hókotróját és SnowPlowActionState-be vált: + (Public)

---

### SnowPlowActionState
#### Felelősség
Egy konkrét hókotró lépéséért felelős állapot. Biztosítja az opcionális akciók sorrendiségét (BuyAttachment → SwapAttachment → RefillAttachment → PickLane). A folyamat bármely pontján érkező PickLane azonnal lezárja az adott gép körét.
#### Ősosztályok
Object → GameState → SnowPlowActionState
#### Interfészek
Nincs.
#### Attribútumok
- `currentPlow`: Az éppen irányított hókotró: - SnowPlow
- `phase`: A belső döntési al-fázis (BUY/SWAP/REFILL/MOVE): - ActionPhase
#### Metódusok
- `void process(Message msg)`: Kezeli a BuyAttachment, SwapAttachment, RefillAttachment, SkipAction és PickLane üzeneteket. PickLane után ellenőrzi, van-e következő gép, és ha nincs, lépteti a játékost a következő fázisba (következő játékos vagy rendszerfázis): + (Public)

---

### BusActionState
#### Felelősség
A buszvezető köréért felelős állapot. Mivel a busz nem rendelkezik cserélhető fejekkel, ez az állapot csak a mozgást (sávválasztást) várja el a játékostól.
#### Ősosztályok
Object → GameState → BusActionState
#### Interfészek
Nincs.
#### Attribútumok
- `currentBus`: Az éppen irányított busz: - Bus
#### Metódusok
- `void process(Message msg)`: Kizárólag a PickLane üzenetet kezeli, majd átadja a kört a következő egységnek (következő játékos vagy rendszerfázis): + (Public)

---

### SystemPhaseState
#### Felelősség
A kör végén lefutó automatizált fázis. Végrehajtja a havazást a WeatherManager segítségével, mozgatja az összes NPC autót az NPCHandleren keresztül, és frissíti a sávok állapotát (pl. só lejárata).
#### Ősosztályok
Object → GameState → SystemPhaseState
#### Interfészek
Nincs.
#### Attribútumok
Nincs saját attribútum.
#### Metódusok
- `void process(Message msg)`: Nem fogad külső üzenetet, a belépéskor automatikusan lefut a rendszerlogika, majd új kört indít az első játékossal: + (Public)

## 2. Állapotátmenetek

| Forrás Állapot | Esemény / Üzenet | Cél Állapot | Feltétel / Megjegyzés |
| :--- | :--- | :--- | :--- |
| **SetupState** | `StartGame` | **AwaitingPurchaseState** | Ha az első játékos Cleaner típusú. |
| **SetupState** | `StartGame` | **BusActionState** | Ha az első játékos BusDriver típusú. |
| **AwaitingPurchaseState** | `FinishPurchase` | **SnowPlowActionState** | A játékos első hókotrójának irányítása kezdődik. |
| **SnowPlowActionState** | `PickLane` | **SnowPlowActionState** | Ha a játékosnak van még következő (nem lépett) hókotrója. |
| **SnowPlowActionState** | `PickLane` | **AwaitingPurchaseState** | Ha nincs több gép, és a következő játékos Cleaner. |
| **SnowPlowActionState** | `PickLane` | **BusActionState** | Ha nincs több gép, és a következő játékos BusDriver. |
| **SnowPlowActionState** | `PickLane` | **SystemPhaseState** | Ha nincs több gép, és az utolsó játékos végzett. |
| **BusActionState** | `PickLane` | **AwaitingPurchaseState** | Ha a következő játékos Cleaner. |
| **BusActionState** | `PickLane` | **BusActionState** | Ha a következő játékos BusDriver. |
| **BusActionState** | `PickLane` | **SystemPhaseState** | Ha az utolsó játékos végzett. |
| **SystemPhaseState** | (Automatikus) | **AwaitingPurchaseState** | Rendszerlogika lefutása után, ha az első játékos Cleaner. |
| **SystemPhaseState** | (Automatikus) | **BusActionState** | Rendszerlogika lefutása után, ha az első játékos BusDriver. |

## 3. Segédosztályok és típusok

### Player (Abstract)
#### Felelősség
A játékost reprezentáló osztály, amely tárolja a nevet, az aktuális pontszámot/egyenleget és az irányított járművek listáját. 
#### Ősosztályok
Object → Player
#### Interfészek
Nincs.
#### Attribútumok
- `name`: A játékos egyedi azonosítója: - String
- `score`: A játékos pontszáma vagy elkölthető egyenlege: # int
- `vehicles`: Az irányított járművek listája: # ArrayList<Vehicle>
#### Metódusok
- `int getScore()`: Pontszám lekérése: + (Public)
- `void addScore(int val)`: Pontszám módosítása: + (Public)
- `List<Vehicle> getVehicles()`: Járművek listázása: + (Public)

---

### Message (Algebraic Data Type)
Az összes bemeneti parancsot összefogó típus. Java record-ok segítségével valósul meg a típusbiztonság érdekében.

#### Setup üzenetek
- `AddPlayer(String role, String name)`: Játékos regisztrálása.
- `AddJunctions(int number)`: Csomópontok hozzáadása.
- `AddRoad(String j1, String j2)`: Út hozzáadása.
- `StartGame()`: Játék indítása.

#### Játékos és jármű akciók
- `BuySnowPlow()`: Új hókotró vásárlása (AwaitingPurchaseState-ben).
- `FinishPurchase()`: Vásárlási fázis vége.
- `BuyAttachment(String type)`: Új fej vétele a soron lévő géphez.
- `SwapAttachment(String type)`: Fejcsere.
- `RefillAttachment()`: Töltés.
- `PickLane(String laneId, boolean clean)`: Mozgás (körzáró akció).
- `SkipAction()`: Opcionális fázis átugrása.

---

### TurnManager
#### Felelősség
A játékosok és azokon belüli járművek sorrendiségének, valamint a körök (Round) haladásának precíz nyilvántartása.
#### Ősosztályok
Object → TurnManager
#### Attribútumok
- `currentPlayerIndex`: Soron lévő játékos indexe: - int
- `currentVehicleIndex`: Soron lévő jármű indexe a játékoson belül: - int
- `roundNumber`: Az aktuális kör száma: - int
#### Metódusok
- `Player getCurrentPlayer()`: Soron lévő játékos lekérése: + (Public)
- `Vehicle getCurrentVehicle()`: Soron lévő jármű lekérése: + (Public)
- `void nextStep()`: A következő járműre vagy játékosra léptet: + (Public)
