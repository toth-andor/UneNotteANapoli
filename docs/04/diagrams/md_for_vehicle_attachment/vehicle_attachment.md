### 3.3.1 Attachment

**Felelősség**
Az összes hókotró-fej közös ősét reprezentálja. Tárolja a fej alapárát, és alapértelmezett
implementációt biztosít a takarítási és újratöltési műveletekhez. Olyan fejek esetén,
amelyek nem igényelnek fogyóanyagot, ezek az alapimplementációk elegendőek.

**Ősosztályok**
Attachment (gyökér)

**Interfészek**
IAttachment

**Attribútumok**
- `price: int` — a fej vételára

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Elvégzi a sáv takarítását az `l` sáv
  `cleanWithAttachment` metódusán keresztül. Az alapimplementáció mindig elvégzi a
  takarítást és `true`-val tér vissza. Fogyóanyagot igénylő leszármazottaknál felül van
  definiálva.
- `int getPrice()`: Visszaadja a fej vételárát.
- `int refill(int budget)`: Az alapimplementáció változatlanul visszaadja a `budget`
  értékét, mivel nem fogyóanyagos fejeket nem kell feltölteni. Fogyóanyagos leszármazottaknál
  felül van definiálva.

---

### 3.3.2 Bus

**Felelősség**
Egy buszvezető által irányított buszt reprezentál. Két végállomás között közlekedik, és
minden sikeres forduló után bevételt szerez. A megtett fordulók száma alapján pontszámot
tart nyilván.

**Ősosztályok**
Vehicle → Commuter → Bus

**Interfészek**
IControllable, IRouteHandler, IScoreOwner

**Attribútumok**
- `balance: int` — az eddig megszerzett bevétel

**Metódusok**
- `void interactWithLane(Lane l)`: A busz és az adott sáv kölcsönhatását valósítja meg:
  letapossa a havat, ami hozzájárul a jégréteg kialakulásához.
- `void addIncome(int amount)`: Növeli a `balance` értékét a megadott összeggel,
  amelyet sikeres forduló teljesítésekor hív meg a `turnAround`.
- `int getScore()`: Visszaadja az aktuális pontszámot.

---

### 3.3.3 Car

**Felelősség**
Egy gépi vezérlésű személyautót reprezentál, amely a legrövidebb járható úton közlekedik
két célpont között. Hozzájárul a sávok letaposásához és ezáltal a jégesedéshez, de nem
termel bevételt és nem tart nyilván pontszámot.

**Ősosztályok**
Vehicle → Commuter → Car

**Interfészek**
IControllable, IRouteHandler

**Metódusok**
- `void interactWithLane(Lane l)`: A személyautó és az adott sáv kölcsönhatását
  valósítja meg: letapossa a havat, növelve a jégesedési számlálót.

---

### 3.3.4 Cleaner

**Felelősség**
Egy takarító játékost reprezentál. Kezeli a játékoshoz tartozó egy vagy több hókotró
működését, nyilvántartja a rendelkezésre álló pénzösszeget, amelyből fejek vásárlása és
fogyóanyag-utántöltés finanszírozható. Pontszámát a megtisztított útszakaszok után kapott
bevétel adja.

**Ősosztályok**
Cleaner (gyökér)

**Interfészek**
IScoreOwner

**Attribútumok**
- `balance: int` — a takarító rendelkezésére álló pénzösszeg

**Metódusok**
- `void addIncome(int amount)`: Növeli a `balance` értékét a megadott összeggel,
  amelyet megtisztított útszakasz után kap a takarító.
- `int getScore()`: Visszaadja az aktuális pontszámot, amely a megszerzett bevétel
  alapján számítódik.

---

### 3.3.5 Commuter

**Felelősség**
A `Bus` és `Car` osztályok közös viselkedését foglalja össze. Tárolja a két célpontot
(`destination1`, `destination2`) és gondoskodik a célpontváltásról végállomásokon. Maga
az osztály nem példányosítható.

**Ősosztályok**
Vehicle → Commuter

**Interfészek**
IControllable, IRouteHandler

**Metódusok**
- `void turnAround()`: Ha a jármű az egyik végállomásra érkezett, megcseréli
  `destination1` és `destination2` értékeit, majd leszármazott osztályban szükség esetén
  elindítja a fizetési logikát.

---

### 3.3.6 Dragon

**Felelősség**
Egy sárkányfej típusú hókotró-fejet reprezentál, amely biokerozin elégetésével azonnal
eltávolítja a havat és a jeget a sávról. Fogyóanyagot igényel, és ha az elfogy,
hatástalanná válik, amíg nem töltik fel.

**Ősosztályok**
Attachment → Dragon

**Interfészek**
IAttachment

**Attribútumok**
- `priceOfFuel: int` — egy egységnyi biokerozin-utántöltés ára

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Csak akkor hívja meg `l`
  `cleanWithAttachment` metódusát, ha van elegendő fogyóanyag. Ha nincs, `false`-szal
  tér vissza és takarítás nem történik.
- `int refill(int budget)`: Ha `budget >= priceOfFuel`, levonja a feltöltés árát és
  feltölti a fejet, majd visszaadja a maradék budgetet. Ha nincs elegendő fedezet,
  változatlanul visszaadja a `budget` értékét.

---

### 3.3.7 IAttachment

**Felelősség**
Az összes hókotró-fej egységes interfésze. Biztosítja, hogy a `SnowPlow` és más
komponensek típustól függetlenül kezelhessék a fejeket.

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Elvégzi a takarítást az adott sávon, ha a fej
  működőképes. Visszatérési értéke jelzi, hogy a takarítás megtörtént-e.
- `int getPrice()`: Visszaadja a fej vételárát.
- `int refill(int budget)`: Megpróbálja feltölteni a fejet a megadott keretből.
  Visszatérési értéke a felhasználás utáni maradék budget. Ha `ret < budget`, a feltöltés
  sikeres volt.

---

### 3.3.8 IControllable

**Felelősség**
Az összes irányítható jármű egységes interfésze. Lehetővé teszi, hogy a controller
típustól függetlenül kezelhesse a járműveket.

**Metódusok**
- `bool gotoLane(Lane l, int timestamp)`: Megpróbálja befogadtatni a járművet `l`
  sávval az `acceptVehicle` meghívásán keresztül. Visszatérési értéke jelzi, hogy
  sikerült-e a sávra lépés. Sikeres esetben meghívja az `interactWithLane`-t.
- `void crash(int timestamp)`: Beállítja a `timeOutStart` értékét az ütközés
  pillanatára, ezzel mozgásképtelenné teszi a járművet egy meghatározott időre.
- `void interactWithLane(Lane l)`: A jármű és a sáv közötti kölcsönhatás logikáját
  valósítja meg (pl. hóletaposás, jégesedés).

---

### 3.3.9 IRouteHandler

**Felelősség**
A két végállomás között közlekedő járművek célpontkezelésének interfésze.

**Metódusok**
- `void turnAround()`: Végállomás elérésekor megcseréli a kiindulási és célpontot.
  `Bus` esetén egyúttal elindítja a bevételszámítást.

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

### 3.3.12 SaltVomitter

**Felelősség**
Egy sószóró típusú hókotró-fejet reprezentál, amely sót juttat az útra, ezzel idővel
felolvasztja a havat és a jeget, valamint meggátolja az újabb lerakódást. Fogyóanyagot
igényel, és ha az elfogy, hatástalanná válik, amíg nem töltik fel.

**Ősosztályok**
Attachment → SaltVomitter

**Interfészek**
IAttachment

**Attribútumok**
- `priceOfFuel: int` — egy egységnyi só-utántöltés ára

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Csak akkor hívja meg `l`
  `cleanWithAttachment` metódusát, ha van elegendő só. Ha nincs, `false`-szal tér vissza
  és takarítás nem történik.
- `int refill(int budget)`: Ha `budget >= priceOfFuel`, levonja a feltöltés árát és
  feltölti a fejet, majd visszaadja a maradék budgetet. Ha nincs elegendő fedezet,
  változatlanul visszaadja a `budget` értékét.

---

### 3.3.13 SnowPlow

**Felelősség**
Egy takarító által irányított hókotróját reprezentál. Tárolja a megvásárolt fejeket,
nyilvántartja az aktív fejet, és elvégzi a sávok takarítását. Egy `Cleaner` játékoshoz
tartozik, akinek egyenlegéből finanszírozza a vásárlásokat és utántöltéseket.

**Ősosztályok**
Vehicle → SnowPlow

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
  ezzel elvégzi a sáv takarítását.
- `void crash(int timestamp)`: Beállítja a `timeOutStart` értékét. A hókotró
  esetében ez üres implementáció is lehet, ha az ütközés nem akadályozza a
  hókotró munkáját.

---

### 3.3.14 Vehicle

**Felelősség**
Az összes játékbeli jármű közös ősét reprezentálja. Tárolja az aktuális sávot és az
esetleges mozgásképtelenség kezdetét. Alapértelmezett implementációt biztosít a
körlogikához és a mozgásképtelenség kezeléséhez. Maga az osztály nem példányosítható.

**Ősosztályok**
Vehicle (gyökér)

**Interfészek**
IControllable

**Attribútumok**
- `currentLane: Lane` — az a sáv, amelyen a jármű jelenleg tartózkodik
- `timeOutStart: int` — az ütközés bekövetkezésének időpillanata; ebből
  számítható, hogy mikor válik ismét mozgásképessé a jármű

**Metódusok**
- `void nextTurn()`: Elvégzi a jármű körönkénti lépéseit: ellenőrzi a
  mozgásképességet, majd szükség esetén előre halad.
- `void interactWithLane(Lane l)`: A jármű és a sáv kölcsönhatásának alaplogikája.
  Leszármazottakban felül van definiálva a konkrét hatás megvalósításához.
- `void setImmobilized()`: Mozgásképtelenné teszi a járművet, beállítva a
  `timeOutStart` értékét az aktuális időpillanatra.

  ### 3.3.15 Sweeper

**Felelősség**
Egy söprőfej típusú hókotró-fejet reprezentál, amely a havat és a feltört jeget közvetlenül
a hókotró melletti jobb oldali sávra tolja. Ha nem létezik jobb oldali sáv, vagy a takarítás
hídon történik, a hó eltűnik (a szél elfújja). Nem képes feltöretlen jeget eltávolítani,
fogyóanyagot nem igényel.

**Ősosztályok**
Attachment → Sweeper

**Interfészek**
IAttachment

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Meghívja `l` `cleanWithAttachment`
  metódusát. Ha létezik a hókotróhoz képest jobbra lévő szomszédos sáv és nem hídon
  történik a takarítás, a havat oda tolja. Egyébként a hó eltűnik. Feltöretlen jeget nem
  távolít el. Mindig `true`-val tér vissza, mivel fogyóanyagot nem igényel.

---

### 3.3.16 VomitingHead

**Felelősség**
Egy hányófej típusú hókotró-fejet reprezentál, amely a havat és a feltört jeget messzebbre
szórja, így az nem rakódik le egyik szomszédos sávra sem — a szél elfújja. Feltöretlen
jeget nem képes eltávolítani, fogyóanyagot nem igényel.

**Ősosztályok**
Attachment → VomitingHead

**Interfészek**
IAttachment

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Meghívja `l` `cleanWithAttachment`
  metódusát. Az eltávolított hó és feltört jég egyik sávra sem kerül át, hanem
  eltűnik. Feltöretlen jeget nem távolít el. Mindig `true`-val tér vissza, mivel
  fogyóanyagot nem igényel.

---

### 3.3.17 IceBreaker

**Felelősség**
Egy jégtörőfej típusú hókotró-fejet reprezentál, amely feltöri a jeget, de nem távolítja
el. A feltört jég eltávolításához további takarítási műveletre van szükség egy söprő- vagy
hányófejjel. Fogyóanyagot nem igényel.

**Ősosztályok**
Attachment → IceBreaker

**Interfészek**
IAttachment

**Metódusok**
- `bool cleanLane(Lane l, int timestamp)`: Meghívja `l` `cleanWithAttachment`
  metódusát, amely feltöri a jeget, de nem távolítja el a sávról. Mindig `true`-val
  tér vissza, mivel fogyóanyagot nem igényel.