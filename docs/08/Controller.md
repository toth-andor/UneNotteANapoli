# Controller csomag

A Controller csomag felelős a játékmenet irányításáért, a felhasználói parancsok értelmezéséért, a körök kezeléséért és a rendszeresemények (havazás, NPC mozgás) koordinálásáért. Elkülöníti a modell logikáját a megjelenítéstől és a bemenetkezeléstől.

## 1. Osztályok és felelősségek

### Controller
A rendszer központi osztálya, amely **állapotgépként** működik (State pattern). 
- **Felelősség:**
    - Az `IController` interfész megvalósítása a prototípus/UI felé.
    - A beérkező `Message` objektumok fogadása és továbbítása az aktuális `GameState`-nek.
    - A `MapModel`, `TurnManager`, `NPCHandler` és `WeatherManager` példányok koordinálása.
    - A mentés és betöltés (`save`/`load`) folyamatának irányítása.

### GameState (Abstract State)
Az állapotgép állapotainak ősosztálya. Meghatározza a parancsok kezelésének alapértelmezett viselkedését. Minden állapot implementálja a rá vonatkozó `Message` típusok feldolgozását.
- **Konkrét állapotok:**
    - `SetupState`: A játék inicializálása (konfiguráció betöltése, játékosok felvétele).
    - `PlayerPhaseState`: Várakozás az aktuális játékos parancsára.
    - `SystemPhaseState`: Automatizált események (havazás, NPC-k) végrehajtása.
    - `GameOverState`: A játék vége, eredményhirdetés.

### Message (Algebrai Adattípus)
A View és a Controller közötti kommunikációs egység. Minden üzenet egy konkrét parancsot és annak adatait reprezentálja.

**Struktúra (Java sealed interface/records koncepció):**

- **Setup Üzenetek (SetupState):**
    - `LoadConfig(String path)`: Konfigurációs fájl elérési útja.
    - `AddPlayer(String role, String name)`: Játékos szerepköre (BusDriver/Cleaner) és neve.
    - `RmPlayer(String name)`: Eltávolítandó játékos neve.
    - `SetCarCount(int count)`: NPC autók száma a térképen.
    - `StartGame()`: Átlépés a játékfázisba.

- **Játék Üzenetek (PlayerPhaseState):**
    - `PickLane(String vehicleId, String laneId, boolean clean)`: Melyik járművel melyik sávba lép a játékos, végez-e takarítást.
    - `SwapAttachment(String plowId, String attrType)`: Melyik hókotrón milyen fejre vált.
    - `RefillAttachment(String plowId)`: Hókotró fejének újratöltése.
    - `BuyItem(String plowId, String type)`: Új fej vagy extra hókotró vásárlása.
13%(68,130)
- **Rendszer Üzenetek (System/Teszt):**
    - `SetRandom(boolean enabled, Long seed)`: Randomizáció ki/be kapcsolása és seed beállítása (`randomoff`/`randomon`).
    - `SaveState(String path)`: Jelenlegi állapot mentése (`save`).
    - `RequestSnapshot(String path)`: Teszt snapshot készítése (`snapshot`).

- **Általános Üzenetek:**
    - `ExitGame()`: Kilépés a programból.
    - `RequestHelp(String topic)`: Súgó kérése (`help`).

### Player
A játékost reprezentáló osztály, amely összeköti a felhasználót a modellbeli járművekkel.
- **Típusai és korlátozások:**
    - **BusDriver**: Pontosan egy `Bus` példányt irányít.
    - **Cleaner**: Egy vagy több `SnowPlow` példányt irányít.
- **Felelősség:**
    - Nyilvántartja a játékos nevét, pontszámát és az általa irányított járműveket.
    - Kezeli a játékoshoz tartozó erőforrásokat (pl. a Cleaner esetében a hókotrók közös költségvetését).

### MapModel
A játéktér (úthálózat) összefoglaló modellje.
- **Felelősség:**
    - Tárolja a kereszteződéseket (`Junction`) és az utakat (`Road`).
    - Biztosítja a térkép gráf-szerkezetének elérését az útvonalkereséshez.

### TurnManager
A körökre osztott játékmenet motorja, amely a játékosok sorrendjéért felel.
- **Felelősség:**
    - Nyilvántartja az aktív `Player` listát és az aktuális játékos indexét.
    - Ha egy játékos minden járművével végzett az adott körben (pl. lépett az összes hókotrójával), átadja a soron következőt.

### NPCHandler
A gépi vezérlésű autók (`Car`) mozgásáért felelős komponens.

### WeatherManager
A környezeti hatások, elsősorban a havazás időzítéséért és mértékéért felel.

### RandomManager
A véletlenszám-generálás absztrakciója (`IRandom`), amely támogatja a determinisztikus tesztelést.

## 2. Interfészek

### IController (UI -> Controller)
- `void receive(Message msg)`: Az egységes belépési pont a parancsok számára. A vezérlő az aktuális `GameState`-nek delegálja a feldolgozást.

### IStateQuery (UI -> Controller/Model)
A megjelenítő réteg ezen keresztül kérdezi le a pillanatnyi állapotot (DTO-kon keresztül).

## 3. Fontosabb algoritmusok

### Állapotátmenetek
- **Setup** -> `StartGame` üzenet -> **PlayerPhase** (első játékos)
- **PlayerPhase** -> (utolsó játékos utolsó járműve végzett) -> **SystemPhase**
- **SystemPhase** -> (események lefutottak) -> **PlayerPhase** (új kör, első játékos)
- Bármely fázis -> (kilépési feltétel) -> **GameOver**

### Útvonalkeresés (Shortest Path)
Szélességi keresés (BFS) a `Junction` gráfban, figyelembe véve a sávok járhatóságát.

## 4. Adatáramlás és láthatóság
A View egy `Message` objektumot küld a Controllernek. A Controller az aktuális `GameState`-nek továbbítja az üzenetet. Az állapot végrehajtja a logikát a Modell-en (a `Player` objektumokon keresztül), majd a vezérlő válaszol a View-nak vagy frissíti a lekérdezhető DTO-kat.
