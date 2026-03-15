### 3.3.1 Attachment

**Felelősség**
Az összes hókotró-fej közös ősét reprezentálja. Tárolja a fej alapárát, és alapértelmezett
implementációt biztosít a takarítási és újratöltési műveletekhez. Olyan fejek esetén,
amelyek nem igényelnek fogyóanyagot, ezek az alapimplementációk elegendőek.

**Ősosztályok**
—

**Interfészek**
IAttachment

**Attribútumok**
- `price: int` — a fej vételára

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Meghívja `l` `cleanWithHead`
  metódusát, amely a `LaneState` logikája alapján dönti el, hogy a takarítás
  elvégezhető-e (pl. feltöretlen jeget nem lehet söprőfejjel eltávolítani). Az
  alapimplementáció mindig `true`-val tér vissza. Fogyóanyagot igénylő
  leszármazottaknál felül van definiálva.
- `int getPrice()`: Visszaadja a fej vételárát.
- `int refill(int budget)`: Az alapimplementáció változatlanul visszaadja a `budget`
  értékét, mivel nem fogyóanyagos fejeket nem kell feltölteni. Fogyóanyagos
  leszármazottaknál felül van definiálva.

---

### 3.3.2 Bus

**Felelősség**
Egy buszvezető által irányított buszt reprezentál. Két végállomás között közlekedik, és
minden sikeres forduló után bevételt szerez. A megtett fordulók száma alapján pontszámot
tart nyilván.

**Ősosztályok**
Vehicle → Commuter

**Interfészek**
IControllable, IRouteHandler, IScoreOwner

**Attribútumok**
- `balance: int` — az eddig megszerzett bevétel

**Metódusok**
- `void interactWithLane(Lane l)`: Meghívja `l` `interactWithLane(this)` metódusát,
  amely a `LaneState` `handleTraffic` logikáján keresztül kezeli a letaposást és a
  jégesedést.
- `void addIncome(int amount)`: Növeli a `balance` értékét a megadott összeggel,
  amelyet a `turnAround` hív meg sikeres forduló teljesítésekor.
- `int getScore()`: Visszaadja az aktuális pontszámot, amely a megszerzett bevétel
  alapján számítódik.
- `void turnAround()`: Felüldefiniálja a `Commuter` `turnAround` metódusát. Megcseréli
  `destination1` és `destination2` értékeit, majd meghívja az `addIncome`-ot a forduló
  után járó bevétellel.

---

### 3.3.3 Car

**Felelősség**
Egy gépi vezérlésű személyautót reprezentál, amely a legrövidebb járható úton közlekedik
két célpont között. Hozzájárul a sávok letaposásához és ezáltal a jégesedéshez, de nem
termel bevételt és nem tart nyilván pontszámot.

**Ősosztályok**
Vehicle → Commuter

**Interfészek**
IControllable, IRouteHandler

**Metódusok**
- `void interactWithLane(Lane l)`: Meghívja `l` `handleTraffic` metódusát, amely a
  `LaneState` logikája alapján kezeli a letaposást és a jégesedést.
---

### 3.3.4 Cleaner

**Felelősség**
Egy takarító játékost reprezentál. Kezeli a játékoshoz tartozó egy vagy több hókotró
működését, nyilvántartja a rendelkezésre álló pénzösszeget, amelyből fejek vásárlása,
fogyóanyag-utántöltés és új hókotró vásárlása finanszírozható. Pontszámát a megtisztított
útszakaszok után kapott bevétel adja.

**Ősosztályok**
—

**Interfészek**
IScoreOwner

**Attribútumok**
- `balance: int` — a takarító rendelkezésére álló pénzösszeg

**Metódusok**
- `void addIncome(int amount)`: Növeli a `balance` értékét a megadott összeggel,
  amelyet megtisztított útszakasz után kap a takarító.
- `int getScore()`: Visszaadja az aktuális pontszámot, amely a megszerzett bevétel
  alapján számítódik.

**Asszociációk**
- `controls (1 → 1..*)`: Egy `Cleaner` egy vagy több `SnowPlow`-t irányít. A `Cleaner`
  birtokolja a hókotrókat, és az ő egyenlegéből finanszírozódnak a vásárlások.

---

### 3.3.5 Commuter

**Felelősség**
A `Bus` és `Car` osztályok közös viselkedését foglalja össze. Tárolja a két célpontot
és gondoskodik a célpontváltásról végállomásokon. Maga az osztály nem példányosítható.

**Ősosztályok**
Vehicle

**Interfészek**
IControllable, IRouteHandler

**Metódusok**
- `void turnAround()`: Ha a jármű az egyik végállomásra érkezett, megcseréli
  `destination1` és `destination2` értékeit. A `Bus` felüldefiniálja ezt a metódust,
  hogy a célpontváltás mellett a forduló után járó bevételt is elszámolja.

**Asszociációk**
- `destination1 (1 → 1)`: Az egyik végállomást jelölő `Road`.
- `destination2 (1 → 1)`: A másik végállomást jelölő `Road`.

---

### 3.3.6 Dragon

**Felelősség**
Egy sárkányfej típusú hókotró-fejet reprezentál, amely biokerozin elégetésével azonnal
eltávolítja a havat és a jeget a sávról. Fogyóanyagot igényel, és ha az elfogy,
hatástalanná válik, amíg nem töltik fel.

**Ősosztályok**
Attachment

**Interfészek**
IAttachment

**Attribútumok**
- `priceOfFuel: int` — egy egységnyi biokerozin-utántöltés ára

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Csak akkor hívja meg `l` `cleanWithHead`
  metódusát, ha van elegendő fogyóanyag. Ha nincs, `false`-szal tér vissza és takarítás
  nem történik.
- `int refill(int budget)`: Ha `budget >= priceOfFuel`, levonja a feltöltés árát,
  feltölti a fejet, majd visszaadja a maradék budgetet. Ha nincs elegendő fedezet,
  változatlanul visszaadja a `budget` értékét.

---

### 3.3.7 IAttachment

**Felelősség**
Az összes hókotró-fej egységes interfésze. Biztosítja, hogy a `SnowPlow` és más
komponensek típustól függetlenül kezelhessék a fejeket.

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Meghívja `l` `cleanWithHead` metódusát,
  ha a fej működőképes. A tényleges takarítási logikát a `LaneState` végzi a
  `cleanWithHead`-en belül. Visszatérési értéke jelzi, hogy a fej működőképes volt-e.
- `int getPrice()`: Visszaadja a fej vételárát.
- `int refill(int budget)`: Megpróbálja feltölteni a fejet a megadott keretből.
  Visszatérési értéke a felhasználás utáni maradék budget. Ha `ret < budget`, a
  feltöltés sikeres volt.

---

### 3.3.8 IControllable

**Felelősség**
Az összes irányítható jármű egységes interfésze. Lehetővé teszi, hogy a controller
típustól függetlenül kezelhesse a járműveket.

**Metódusok**
- `bool gotoLane(Lane l, int timestamp)`: Megpróbálja befogadtatni a járművet `l`
  sávval a `pushVehicle` meghívásán keresztül. Visszatérési értéke jelzi, hogy
  sikerült-e a sávra lépés. Sikeres esetben meghívja az `interactWithLane`-t.
- `void crash(int timestamp)`: Beállítja a `timeOutStart` értékét az ütközés
  pillanatára, ezzel mozgásképtelenné teszi a járművet egy meghatározott időre.
  A `SnowPlow` ezt üresre implementálja, mivel az ütközés nem akadályozza a
  hókotró munkáját.
- `void interactWithLane(Lane l)`: Meghívja `l` `interactWithLane(this)` metódusát,
  amelyen keresztül a `LaneState` elvégzi a tényleges kölcsönhatás logikáját.

---

### 3.3.9 IRouteHandler

**Felelősség**
A két végállomás között közlekedő járművek célpontkezelésének interfésze.

**Metódusok**
- `void turnAround()`: Végállomás elérésekor megcseréli a kiindulási és célpontot.
  A `Bus` felüldefiniálja, hogy a célpontváltás mellett a forduló után járó bevételt
  is elszámolja.

---

### 3.3.10 IScoreOwner

**Felelősség**
Azon játékos entitások egységes interfésze, amelyekhez pontszám tartozik. Lehetővé
teszi, hogy a rendszer típustól függetlenül kérdezhesse le a pontszámokat.

**Metódusok**
- `int getScore()`: Visszaadja az adott entitás aktuális pontszámát.

---

### 3.3.11 ISnowPlow

**Felelősség**
A hókotró-specifikus műveletek interfésze. Lehetővé teszi, hogy a controller a
konkrét implementációtól függetlenül kezelhesse a fejcseréket és a vásárlásokat.

**Metódusok**
- `bool buyAttachment(Attachment a)`: Megvásárolja az `a` fejet, ha a `Cleaner`
  egyenlegén elegendő fedezet áll rendelkezésre. Visszatérési értéke jelzi, hogy a
  vásárlás sikerült-e.
- `bool changeAttachment(Attachment a)`: Az `a` fejre vált, ha az szerepel az
  `owned_tools` listában. Visszatérési értéke jelzi, hogy a váltás sikerült-e.
- `void refillAttachment()`: Az aktív fej fogyóanyagát tölti fel, ha a `Cleaner`
  egyenlege fedezi a feltöltés árát.

---

### 3.3.12 IceBreaker

**Felelősség**
Egy jégtörőfej típusú hókotró-fejet reprezentál, amely feltöri a jeget, de nem
távolítja el. A feltört jég hóvá alakul, amelynek eltávolításához további takarítási
műveletre van szükség egy söprő- vagy hányófejjel. Fogyóanyagot nem igényel.

**Ősosztályok**
Attachment

**Interfészek**
IAttachment

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Meghívja `l` `cleanWithHead`
  metódusát, amely a `LaneState` logikája alapján a jeget hóvá alakítja, de nem
  távolítja el. Mindig `true`-val tér vissza, mivel fogyóanyagot nem igényel.

---

### 3.3.13 SaltVomitter

**Felelősség**
Egy sószóró típusú hókotró-fejet reprezentál, amely sót juttat az útra, ezzel idővel
felolvasztja a havat és a jeget, valamint meggátolja az újabb lerakódást. Fogyóanyagot
igényel, és ha az elfogy, hatástalanná válik, amíg nem töltik fel.

**Ősosztályok**
Attachment

**Interfészek**
IAttachment

**Attribútumok**
- `priceOfFuel: int` — egy egységnyi só-utántöltés ára

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Csak akkor hívja meg `l` `cleanWithHead`
  metódusát, ha van elegendő só. Ha nincs, `false`-szal tér vissza és takarítás nem
  történik.
- `int refill(int budget)`: Ha `budget >= priceOfFuel`, levonja a feltöltés árát,
  feltölti a fejet, majd visszaadja a maradék budgetet. Ha nincs elegendő fedezet,
  változatlanul visszaadja a `budget` értékét.

---

### 3.3.14 SnowPlow

**Felelősség**
Egy takarító által irányított hókotróját reprezentál. Tárolja a megvásárolt fejeket,
nyilvántartja az aktív fejet, és elvégzi a sávok takarítását. Egy `Cleaner` játékoshoz
tartozik, akinek egyenlegéből finanszírozza a vásárlásokat és utántöltéseket.

**Ősosztályok**
Vehicle

**Interfészek**
IControllable, ISnowPlow

**Metódusok**
- `bool changeAttachment(Attachment a)`: Az `a` fejre vált, ha az szerepel az
  `owned_tools` listában. Visszatérési értéke jelzi, hogy a váltás sikerült-e.
- `bool buyAttachment(Attachment a)`: Megvásárolja az `a` fejet, ha a tulajdonos
  `Cleaner` egyenlegén elegendő fedezet áll rendelkezésre, és hozzáadja az
  `owned_tools` listához. Visszatérési értéke jelzi, hogy a vásárlás sikerült-e.
- `void refillAttachment()`: Az aktív fej `refill` metódusát hívja meg a `Cleaner`
  aktuális egyenlegével, majd frissíti az egyenleget a visszatérési értékkel.
- `void interactWithLane(Lane l)`: Meghívja az aktív fej `cleanLane` metódusát,
  ezzel kezdeményezi a sáv takarítását, amelynek tényleges logikáját a `LaneState`
  végzi a `cleanWithHead`-en belül.
- `void crash(int timestamp)`: Üres implementáció — a hókotró ütközés esetén is
  folytatja a munkát.

**Asszociációk**
- `owner (1 → 1)`: A `SnowPlow`-t irányító `Cleaner`, akinek egyenlegéből a
  vásárlások és utántöltések finanszírozódnak.
- `owned_tools (1 → 1..*)`: A `SnowPlow` tulajdonában lévő, megvásárolt fejek
  gyűjteménye.
- `active_tool (1 → 1)`: Az éppen aktív, takarításhoz használt fej; az
  `owned_tools` egyik eleme.

---

### 3.3.15 Sweeper

**Felelősség**
Egy söprőfej típusú hókotró-fejet reprezentál, amely a havat és a feltört jeget
közvetlenül a hókotró melletti jobb oldali sávra tolja. Ha nem létezik jobb oldali sáv,
vagy a takarítás hídon történik, a hó eltűnik. Feltöretlen jeget nem képes eltávolítani,
fogyóanyagot nem igényel.

**Ősosztályok**
Attachment

**Interfészek**
IAttachment

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Meghívja `l` `cleanWithHead` metódusát.
  A `LaneState` logikája alapján, ha létezik jobb oldali szomszédos sáv és nem hídon
  történik a takarítás, a havat oda tolja; egyébként eltűnik. Feltöretlen jeget nem
  távolít el. Mindig `true`-val tér vissza, mivel fogyóanyagot nem igényel.

---

### 3.3.16 Vehicle

**Felelősség**
Az összes játékbeli jármű közös ősét reprezentálja. Tárolja az aktuális sávot és az
esetleges mozgásképtelenség kezdetét. Maga az osztály nem példányosítható.

**Ősosztályok**
—

**Interfészek**
IControllable

**Attribútumok**
- `currentLane: Lane` — az a sáv, amelyen a jármű jelenleg tartózkodik
- `timeOutStart: int` — az ütközés bekövetkezésének időpillanata; ebből számítható,
  hogy mikor válik ismét mozgásképessé a jármű

**Metódusok**
- `bool gotoLane(Lane l, int timestamp)`: Megpróbálja befogadtatni a járművet `l`
  sávval a `pushVehicle` meghívásán keresztül. Sikeres esetben frissíti a
  `currentLane` értékét és meghívja az `interactWithLane`-t.
- `void crash(int timestamp)`: Beállítja a `timeOutStart` értékét az ütközés
  pillanatára. A `SnowPlow` felüldefiniálja üres implementációval.
- `void interactWithLane(Lane l)`: Leszármazottakban felül van definiálva a konkrét
  kölcsönhatás megvalósításához.

**Asszociációk**
- `currentLane (1 ↔ 1)`: A jármű és az aktuális sáv kölcsönös asszociációja; a `Lane`
  `pushVehicle` metódusa tartja karban.

---

### 3.3.17 VomitingHead

**Felelősség**
Egy hányófej típusú hókotró-fejet reprezentál, amely a havat és a feltört jeget
messzebbre szórja, így az nem rakódik le egyik szomszédos sávra sem — a szél elfújja.
Feltöretlen jeget nem képes eltávolítani, fogyóanyagot nem igényel.

**Ősosztályok**
Attachment

**Interfészek**
IAttachment

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Meghívja `l` `cleanWithHead` metódusát.
  A `LaneState` logikája alapján az eltávolított hó és feltört jég egyik sávra sem
  kerül át, hanem eltűnik. Feltöretlen jeget nem távolít el. Mindig `true`-val tér
  vissza, mivel fogyóanyagot nem igényel.
  