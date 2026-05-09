# A felület működési elve

A `controller`-t `Message`-eken keresztül értesíti a `view` a felhasználó által végrehajtott akciókról. A `controller`
A `model`-en végrehajtja a megfelelő módosításokat és a `model` az observer minta segítségével értesíti a `view`-t a az a megváltozott objektumokról. Tehát a `view` tervezésénél a `push` alapjú megközelítést választottuk. A legtöbb `model`-beli osztályhoz tartozik a `view`-nak egy megfelelő osztálya, amely implementálja az `Observer` interfészt. A `model`-ben lévő osztályok számon tartják a `view`-ban nekik megfelelő objektumot és a az `Observer` interfészen keresztül értesítik változás esetén, így az újra tudja rajzolni a képernyőn az objektumot.


# A felület osztály-struktúrája

A grafikus felület megvalósítása a Java **Swing** keretrendszerére épül, követve az **Observer** tervezési mintát. A megjelenítés alapelve, hogy minden játékelem (járművek, sávok, csomópontok) **négyszögként** jelenik meg, ahol az elemek mérete és színe hordozza a vizuális információt.

A felhasználói interakciókat Swing eseménykezelők figyelik, amelyek a fizikai bemenetből (kattintás, gombnyomás) állítják elő a `Controller` számára érthető `Message` objektumokat.

### Interfészek és osztályok részletes leírása

#### `Observer` (interfész)
A modell változásait figyelő objektumok közös interfésze. Ez biztosítja a kapcsolatot a logikai és a megjelenítési réteg között.
- **Metódusok:**
    - `update()`: A modell hívja meg állapotváltozáskor. Hatására a nézet frissíti a belső adatait (koordináták, színek) és kezdeményezi a grafikus felület újrarajzolását (`repaint()`).

#### `View` (absztrakt osztály)
Minden grafikus elem közös őse, amely az `Observer` interfészt valósítja meg. Összefogja a téglalap alapú kirajzoláshoz szükséges geometriai és stílus adatokat.
- **Attribútumok:**
    - `#x, #y`: A négyszög bal felső sarkának koordinátái a képernyőn.
    - `#width, #height`: A négyszög méretei pixelben.
    - `#color`: A négyszög kitöltési színe.
- **Metódusok:**
    - `draw(Graphics g)`: Absztrakt metódus. A konkrét nézetek ebben hívják meg a `g.fillRect()` metódust a saját értékeikkel.
    - `update()`: Absztrakt metódus az adatok modellből történő szinkronizálására.

#### `JunctionView` (konkrét osztály)
A csomópontok megjelenítéséért felelős. Mivel a csomópontok rögzített pontok, ez az osztály tárolja a fix képernyő-koordinátákat, amelyekhez az utak és sávok igazodnak.
- **Metódusok:**
    - `draw(Graphics g)`: Kirajzolja a csomópontot jelképező négyszöget.
    - `update()`: Frissíti a színt a csomópont foglaltsága vagy állapota alapján.

#### `LaneView` (konkrét osztály)
A sávok megjelenítéséért felelős. A pozícióját és méretét a két végpontját jelentő `JunctionView`-k koordinátáiból számítja ki.
- **Metódusok:**
    - `draw(Graphics g)`: Kirajzolja a sávot reprezentáló négyszöget az út mentén.
    - `update()`: Lekérdezi a sáv aktuális logikai állapotát (száraz, jeges, stb.) és ennek megfelelő színt állít be.

#### `VehicleView` (konkrét osztály)
A járművek dinamikus megjelenítéséért felelős.
- **Metódusok:**
    - `draw(Graphics g)`: Kirajzolja a járművet jelképező (a sávnál kisebb) négyszöget.
    - `update()`: Lekéri a jármű aktuális sávját, és annak pozíciója alapján frissíti a saját `x, y` koordinátáit.

#### `CleanerView` (konkrét osztály)
A takarító játékos státuszának (egyenleg, felszerelés) grafikus megjelenítése.
- **Metódusok:**
    - `draw(Graphics g)`: Megjeleníti az adatokat egy információs panelen (négyszögben).
    - `update()`: Frissíti a szöveges információkat a játékos modell-objektumából.

#### `GamePanel` (osztály, `JPanel` leszármazott)
A játék grafikus vászna, amely tárolja és rétegezetten kirajzolja a nézeteket.
- **Metódusok:**
    - `paintComponent(Graphics g)`: Felüldefiniált Swing metódus. Meghívja minden regisztrált `View` objektum `draw()` metódusát.
    - `addView(View v)`: Új megjelenítendő elem regisztrálása.

#### `GameWindow` (osztály, `JFrame` leszármazott)
A főablak, amely összefogja a felületet és generálja az üzeneteket.
- **Metódusok:**
    - `initUI()`: Létrehozza a `GamePanel`-t és a vezérlőgombokat.
    - `dispatchMessage(Message msg)`: Továbbítja a generált üzenetet a `Controller`-nek.
    - **Event Handlers**: A gombok (`ActionListener`) és a térkép (`MouseListener`) eseményei itt futnak le. Például egy sávra kattintáskor a handler azonosítja a sávot, létrehoz egy `PickLane` üzenetet, és meghívja a `dispatchMessage`-t.

### Modellbeli változások

Az `Observer` minta megvalósításához a modell osztályainak képessé kell válniuk a megfigyelők regisztrálására és értesítésére. Ehhez bevezetésre kerül egy `Observable` interfész, amelyet a változást produkáló modell-elemek valósítanak meg.

#### `Observable` (interfész)
A megfigyelhető objektumok közös interfésze.
- **Metódusok:**
    - `addObserver(Observer o)`: Új nézet regisztrálása az adott modell-elemhez.
    - `notifyObservers()`: Végigiterál a regisztrált nézeteken és meghívja azok `update()` metódusát.

#### Érintett modell osztályok

Az alábbi osztályoknak kell implementálniuk az `Observable` interfészt, vagy tartalmazniuk az értesítési logikát:

1.  **`Vehicle` (absztrakt osztály)**:
    - **Változás**: Értesítést küld, ha a jármű pozíciója megváltozik (`gotoLane`), ha ütközik (`crash`), vagy ha az állapota (pl. időzítő) módosul.
2.  **`Lane` (absztrakt osztály)**:
    - **Változás**: Értesítést küld, ha a sávban lévő hó mennyisége változik, vagy ha a logikai állapota (pl. `SnowyState` -> `IcyState`) átfordul.
3.  **`Junction` (osztály)**:
    - **Változás**: Bár logikailag ritkán változik, tárolnia kell a hozzárendelt nézetet. A nézet rögzíti a csomópont képernyő-koordinátáit, amelyek a teljes térkép elrendezésének alapjául szolgálnak.
4.  **`Cleaner` (osztály)**:
    - **Változás**: Értesítést küld minden olyan esetben, amikor a játékos egyenlege (`balance`) módosul (vásárlás, takarítási jutalom).

### Osztálydiagram


A felület osztály-struktúráját szemléltető diagram az alábbi fájlban található:
[ui_class_diagram.puml](diagrams/ui_class_diagram.puml)





