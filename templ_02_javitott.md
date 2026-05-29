# 2. Követelmény, projekt, funkcionalitás — Una notte a Napoli

> **Megjegyzés:** Ez az átdolgozott változat az eredeti (2026-03-01) dokumentum javított verziója.
> A módosítások az implementált kódbázissal való inkonzisztenciákat szüntetik meg;
> a változtatott vagy új szakaszok _[JAVÍTVA]_ illetve _[ÚJ]_ jelöléssel vannak ellátva.

---

## 2.1 Bevezetés

### 2.1.1 Cél

A játék egy téli város, **Zúzmaraváros** közlekedésének fenntartásáról szól, ahol a folyamatos
havazás miatt az utak állapota dinamikusan romlik. A cél az utak járhatóságának biztosítása,
miközben különböző szereplők eltérő érdekek mentén működnek.

A rendszer egy **többszereplős, stratégiai szimuláció**, ahol a hó, a jég, a forgalom és az
erőforrás-gazdálkodás kölcsönhatásban állnak.

### 2.1.2 Szakterület

Szórakoztatóipar / játék fejlesztés

### 2.1.3 Definíciók, rövidítések

**Város**
A játék teljes működési területe, amely magában foglalja az úthálózatot, a járműveket, az
időjárási viszonyokat és a gazdasági folyamatokat.

**Útszakasz**
Két csatlakozási pont között elhelyezkedő útrész, amely egy vagy több sávból állhat.

**Sáv**
Az útszakasz önálló, külön kezelhető része, amelyen a járművek közlekednek. Minden sáv
saját állapottal rendelkezik.

**Kereszteződés**
Olyan csatlakozási pont, ahol kettő vagy több útszakasz találkozik, függetlenül attól, hogy
azonos vagy eltérő szinten helyezkednek el.

**Alagút** _[JAVÍTVA]_
Különszinten elhelyezkedő, fedett útszakasz. Az alagútsávokra nem hull hó, nem
jegesednek el, és takarítás sem szükséges rajtuk. _(Az eredeti dokumentumban „Híd / Alagút"
közösen szerepelt; a megvalósítás csak alagútsávot kezel — az alagút mint fedett
infrastruktúra váltja ki mindkét különszintes sávtípust. A nyílt hídszakaszok sima
kültéri sávként (OutdoorLane) modellezhetők.)_

**Sávállapot** _[ÚJ]_
A sáv pillanatnyi minőségét leíró diszkrét érték. A lehetséges állapotok:

| Állapot | Leírás |
|---|---|
| **Száraz** | Tiszta, járható út, hó és jég nincs. |
| **Havas** | Hó borítja a sávot; 5 jármű áthaladása után jeges állapotba válthat. |
| **Jeges** | A letaposott hó megfagyott; az áthaladó járművek balesetet szenvedhetnek. |
| **Sózott** | Sószóró kezelte a sávot; hó nem rakódik le, újabb hóesés nem okoz jegesedést. Lejárat után szárazba vált. |
| **Balesetes** | Ütközés blokkolta a sávot; belépés tiltott. `IMMOBILE_TIME` elteltével jeges állapotba vált vissza. |

**Hóvastagság**
A sávon felhalmozódott hó mennyisége (egységekben mérve). Ha eléri az 5-ös küszöbértéket,
a sáv havas állapotba kerül; ha a 20-as küszöbértéket, a sáv járhatatlanná válik (hókotrók számára kivéve).

**Letaposottság** _[JAVÍTVA]_
A havas sávon áthaladó járművek kumulált száma. Ha 5 jármű halad át a havas sávon,
jeges állapotba vált. _(Az eredeti dokumentum ezt folyamatos mennyiségként írta le;
az implementáció átmeneti számlálóval kezeli.)_

**Jégréteg**
A havas sáv letaposottság-küszöb elérése után kialakult jeges állapot, amely balesetveszélyt jelent.

**Járhatóság**
Az útszakasz azon állapota, amely meghatározza, hogy a járművek biztonságosan tudnak-e
rajta közlekedni. Balesetes sávra nem lehet belépni; 20-as hómennyiség felett hókotrón kívül
más jármű nem tud áthaladni.

**Forduló**
A busz által megtett egy irányú teljes út a két végállomás valamelyikéhez való megérkezéssel
lezárva. _(Egy oda- és visszaút összesen két fordulónak számít.)_

**Végállomás**
A buszjárat egyik kijelölt indulási vagy érkezési útszakasza.

**Ütközés**
Két jármű nem szándékos találkozása jeges sávon, amely az érintett sáv balesetes állapotba
kerülését és mindkét jármű `IMMOBILE_TIME` időre szóló mozgásképtelenségét okozza.

**Mozgásképtelenségi idő (IMMOBILE_TIME)**
Az az időtartam (3 kör), amely alatt egy ütközött jármű nem képes továbbhaladni.

**Hókotró**
Olyan jármű, amely az útszakaszok megtisztítására szolgál. Alapértelmezett feje a söprő fej.
Ütközés esetén is folytatja munkáját (immobilizálás nem vonatkozik rá).

**Fej**
A hókotró elejére szerelhető eszköz, amely meghatározza a tisztítás módját.
Egy hókotrónak egyszerre egyféle aktív feje lehet, de a megvásárolt fejek közül bármikor cserélhető.

**Söprő fej**
Olyan eszköz (ár: 1), amely a havat és a feltört jeget a kisebb sávindexű, azonos irányú
szomszéd sávra tolja. Ha nincs ilyen szomszéd sáv (pl. csak egysávos út), vagy a sáv
nincs hídhoz kapcsolt kültéri sávként meghatározva ahol a szél elfúj, a hó eltűnik.
Feltöretlen jeget nem képes eltávolítani. Fogyóanyagot nem igényel.
_Ez a hókotró alapértelmezett kezdeti feje._

**Hányó fej** _[JAVÍTVA]_
Olyan eszköz (ár: 0), amely a havat és a feltört jeget messzebbre szórja;
az eltávolított anyag egyetlen sávra sem kerül — eltűnik. Feltöretlen jeget nem képes
eltávolítani. Fogyóanyagot nem igényel. _(Az eredeti dokumentum emelkedő árat
specifikált söprő → hányó sorrendben; a megvalósításban a hányó fej ára 0, a söprőé 1.
Az ár-lista: söprő fej 1, hányó fej 0, jégtörő fej 3, sószóró fej 5, sárkány fej 10.)_

**Jégtörő fej**
Olyan eszköz (ár: 3), amely a jeget feltöri, és azt havas állapotba alakítja, de nem távolítja
el. A feltört jeget ezután söprő vagy hányó fejjel kell eltávolítani. Fogyóanyagot nem igényel.

**Sószóró fej**
Olyan eszköz (ár: 5), amely sót juttat az útra, amely idővel elolvasztja a havat és a jeget,
és megakadályozza az újabb hólerakódást. Fogyóanyagot (só) igényel.
Ha a só elfogy, a fej hatástalanná válik, amíg az utánpótlás meg nem történik.

**Sárkány fej**
Olyan eszköz (ár: 10), amely biokerozin elégetésével azonnal, egyetlen művelettel eltávolít
minden havat és jeget a sávról (DryState-be vált), beleértve a balesetes sávot is.
Fogyóanyagot (biokerozin) igényel. Ha a biokerozin elfogy, a fej hatástalanná válik.
_Kivétel: zúzalékkal kezelt sávra nem hat._

**Fogyóanyag** _[JAVÍTVA]_
Olyan felhasználható anyag, amely a sószóró és sárkány fejek működéséhez szükséges,
és a működés során elfogy. Típusai:
- **Só** — a sószóró fej anyaga
- **Biokerozin** — a sárkány fej anyaga

**Bevétel**
Minden sikeres takarítási művelet (ahol az aktív fej ténylegesen megváltoztatja a sáv
állapotát) után a takarítónak járó 2 egységnyi ellenérték, amely fejlesztésre fordítható.

**Fejlesztés**
Új fejek, fogyóanyag-utántöltés, vagy új hókotró vásárlása a hatékonyabb működés érdekében.

**Buszvezető**
Olyan résztvevő, aki buszt irányít, és célja minél több forduló teljesítése.

**Takarító**
Olyan résztvevő, aki hókotrókat irányít, eszközöket kezel és gazdálkodási döntéseket hoz.

**Játékidő**
Az a meghatározott időtartam, amely alatt a résztvevők céljaikat elérni igyekeznek.

---

### 2.1.4 Hivatkozások

1. https://gemini.google.com/share/552a53c3d787
2. https://www.iit.bme.hu/file/11582/feladat

### 2.1.5 Összefoglalás

A következőkben részletesen bemutatjuk a fejlesztendő szoftver képességeit, célközönségét és
szerkezeti felépítését, biztosítva ezzel a projekt alapvető összefüggéseinek átláthatóságát.
Ezt követően alaposabban megvizsgáljuk a rendszer specifikus elvárásait, valamint a funkcionális
esethasználatokat. Zárásként ismertetjük a kivitelezés tervezett szakaszait, a fejlesztési
környezet sajátosságait, továbbá rögzítjük az iratban előforduló speciális szakkifejezések
pontos értelmezését.

---

## 2.2 Áttekintés

### 2.2.1 Általános áttekintés

A kialakítandó rendszer egy többszereplős, városi működést modellező játék, amelyben a
résztvevők buszvezetőként vagy takarítóként vesznek részt. A cél az egységes működés
biztosítása, amelyben a város közlekedése és a hóeltakarítás, folyamatosan változó
körülmények között, egyszerre zajlik.

#### A RENDSZER FŐ RÉSZEI

**Az úthálózat**
Feladata az utak, sávok, kereszteződések (melyekben akár négynél több út is összefuthat),
alagutak nyilvántartása, valamint a járművek számára a legrövidebb járható út meghatározása.
Szerepet játszik az utak állapotának kezelésében, frissítésében.

_Megjegyzés: Az utak nem helyrajzi pontokhoz kötve, hanem egymáshoz kapcsolódó
szakaszok rendszerében kerülnek meghatározásra._

**Az időjárást és utak állapotát kezelő rendszer**
Feladata a folyamatos havazás modellezése, a letaposott hó jéggé alakulásának kezelése,
a só hatására bekövetkező olvadás és a hókotró fejek hatásának modellezése.
Meghatározza, hogy egy adott útszakasz mennyire járható. A sávok állapotait állapotgép
(State Pattern) kezeli; a lehetséges állapotok: Száraz, Havas, Jeges, Sózott, Balesetes.

**A hókotrókat kezelő rendszer**
Feladata a hókotrók mozgatásának kezelése, a különböző fejek cseréjének biztosítása,
illetve a fejek üzemeltetéséhez szükséges fogyóanyagok mennyiségének nyilvántartása.
A rendszer feladata továbbá az új eszközök és hókotrók beszerzésének lehetővé tétele.

**A buszokat kezelő rendszer**
Feladata a buszjáratok két végállomás közötti közlekedésének kezelése,
valamint a megtett fordulók számolása.

**A felhasználói kapcsolatok**

A. Buszvezetők, akik járműveikkel meghatározott megállók között közlekednek
B. Takarítók, akik hókotrókat vezetnek; feladatuk az utak járhatóságának biztosítása

A résztvevők a számukra biztosított kezelőfelületen keresztül:

- Irányítják járműveiket: a jármű magától megy előre, kereszteződésnél megáll és ekkor
  az éppen soron lévő játékos egérrel rákattint a követendő útra
- A játékosok egymás után körökben kerülnek sorra, ahol minden játékos számára egy
  adott időkeret áll rendelkezésre
- Adott számú körönként (a folyamatos havazás miatt) takarítás nélkül járhatatlanná
  válik az út a buszok számára

**A gazdasági modell**
Feladata a bevételek és kiadások nyilvántartása, az eszközök árának meghatározása.
A hókotrók számára felületet biztosít a fogyóanyagok és eszközök beszerzésére,
illetve feladata ezek költségének követése.

**Az alrendszerek kapcsolatai**

- Az úthálózat tájékoztatja a közlekedési rendszert az utak állapotáról
- Az időjárásért felelős rendszer módosítja az utak állapotát
- A hókotrókat kezelő rendszer szintén módosítja az utak állapotát
- A közlekedési rendszer hat az utak állapotára a letaposásból eredő jegesedés miatt
- A gazdasági rendszer a megtisztított útszakaszok szerint elszámolást végez

**Adattárolási elvárások**
A rendszernek meg kell őriznie a játékosok eredményeit, a gazdasági állapotot, a város és
utak aktuális helyzetét, a megtett fordulók számát és a birtokolt eszközöket.
Ennek célja a játék folytathatósága, illetve az eredmények visszakereshetősége.

---

### 2.2.2 Funkciók

A kialakítandó rendszer célja Zúzmaraváros téli működésének több szereplő által alakított
modellezése. A játék során a résztvevők buszvezetőként vagy takarítóként tevékenykednek,
miközben a folyamatos havazás, a forgalom és a gazdasági döntések egymásra hatva alakítják
a város állapotát.

#### 1. A város és az úthálózat működése

A város egymáshoz kapcsolódó útszakaszok rendszeréből áll. Az utak lehetnek egy- vagy
többsávosak, kereszteződhetnek egy szintben, illetve alagutak révén különszinteken is
találkozhatnak. Egy kereszteződésben több út is összefuthat.

Minden kültéri sáv (OutdoorLane) önálló állapottal rendelkezik (Száraz, Havas, Jeges,
Sózott, Balesetes), és számon tartja a rajta felhalmozódott hó mennyiségét (hóvastagság).
Az alagútsávok nem érintkeznek az időjárással — rajtuk sem hó nem gyűlik, sem jég nem keletkezik.

A havazás folyamatosan növeli a hó mennyiségét a kültéri sávokon.
Ha a hóvastagság eléri az 5-ös küszöbértéket, a sáv havas állapotba kerül;
ha eléri a 20-as küszöbértéket, hókotrón kívül más jármű számára járhatatlanná válik.
Ha egy havas sávon 5 jármű halad át, a letaposott hó jéggé alakul. A jeges útfelületen
a járművek megcsúszhatnak és összeütközhetnek, ami az adott sáv balesetes állapotba
kerülését okozza. A balesetes sáv `IMMOBILE_TIME` (3 kör) elteltével automatikusan jeges
állapotba vált vissza.

#### 2. Járművek és közlekedés

A városban személyautók, buszok és hókotrók közlekednek. A személyautók a lakóhely és a
munkahely között mozognak, és minden esetben a legrövidebb járható útvonalat választják.
Ha egy sáv járhatatlanná válik, másik sávra térhetnek át, amennyiben az elérhető és megfelelő
állapotú.

A buszok meghatározott járatokon közlekednek két végállomás között. Céljuk, hogy a játék
ideje alatt minél több fordulót teljesítsenek. Egy sikeres forduló akkor számít, ha a busz
eljut az egyik végállomásra (egy irányú út). Ha egy busz összeütközik más járművel,
`IMMOBILE_TIME` ideig mozgásképtelenné válik.

#### 3. Hókotrók és fejek _[JAVÍTVA]_

A takarítók hókotrókat irányítanak; egy takarítónak több hókotrója is lehet.
Egy hókotró egyszerre egyféle fejjel működhet, de a megvásárolt fejek szabadon cserélhetők.
Minden hókotró alapértelmezetten **söprő fejjel** indul.

A választható fejek és viselkedésük:

A **söprő fej** (ár: 1) a havat és a feltört jeget a kisebb sávindexű, azonos menetirányú
szomszéd sávra tolja. Ha nincs ilyen szomszéd, a hó eltűnik. Feltöretlen jeget nem képes
eltávolítani. Fogyóanyagot nem igényel.

A **hányó fej** (ár: 0) a havat és a feltört jeget messzebbre szórja, egyik sávra sem kerül
az eltávolított anyag — eltűnik. Feltöretlen jeget nem képes eltávolítani. Fogyóanyagot nem igényel.

> **[JAVÍTVA]** Az eredeti dokumentum a fejek árát emelkedő sorrendben adta meg (söprő →
> hányó → …). A megvalósításban a hányó fej ára **0** (söprő fej ára **1**), így az első két
> fej sorrendje az ár szempontjából megfordul — a hányó fej olcsóbban (ingyenesen) szerezhető be.
> A teljes ársorrend: hányó (0) < söprő (1) < jégtörő (3) < sószóró (5) = sárkány (10).
> A soszóró és sárkány fej azonos áron is megvásárolható, de a sárkány fej hatékonyabb.

A **jégtörő fej** (ár: 3) feltöri a jeget (jeges állapot → havas állapot), de nem távolítja el.
A feltört jegett ezt követően söprő vagy hányó fejjel lehet eltávolítani.

A **sószóró fej** (ár: 5) sót juttat az útra, amely idővel elolvasztja a havat és a jeget,
és megakadályozza az újabb lerakódást. Lejárt hatású sózott sáv száraz állapotba vált.
Só fogyóanyagot igényel; ha elfogy, hatástalanná válik.

A **sárkány fej** (ár: 10) biokerozin elégetésével azonnal eltávolít minden havat, jeget,
és balesetes állapotot is, a sávot száraz állapotba hozza. Biokerozin fogyóanyagot igényel;
ha elfogy, hatástalanná válik.

A sószóró és sárkány fej működéséhez fogyóanyag szükséges. Ha a só vagy biokerozin
elfogy, az adott fej hatástalanná válik, amíg az utánpótlás meg nem történik.

#### 4. Gazdálkodás és fejlődés

A hókotrók minden sikeres takarítási művelet után **2 egység** bevételt termelnek
(csak akkor, ha a fej ténylegesen megváltoztatta a sáv állapotát).
A bevétel felhasználható:

- új fejek beszerzésére,
- fogyóanyag vásárlására,
- új hókotró megvásárlására (ár: 100 egység).

A fejek árai: hányó (0), söprő (1), jégtörő (3), sószóró (5), sárkány (10).
A legdrágább befektetés az új hókotró — a takarítóknak mérlegelniük kell, hogy mikor
ruháznak be fejlettebb fejre, és mikor fordítják erőforrásaikat további jármű vásárlására.

#### 5. Kölcsönhatások

A közlekedés és a hóeltakarítás folyamatosan hat egymásra. A járművek letapossák a havat
(5 áthaladás után jegesedés), ami balesetekhez vezet. A hókotrók javítják a járhatóságot,
de tevékenységük gazdasági döntésektől függ. A buszok teljesítménye a takarítók
hatékonyságától is függ.

A rendszer biztosítja, hogy minden változás azonnal érvényesüljön a város egészében,
és minden résztvevő ugyanazon állapot alapján hozhassa meg döntéseit.

#### 6. Játékosok szerepe és céljai

A buszvezetők célja minél több sikeres forduló teljesítése a játékidő alatt.
A takarítók célja a lehető legtöbb útszakasz megtisztítása és a város közlekedésének
fenntartása. A játék akkor tekinthető eredményesnek, ha a város közlekedése a résztvevők
együttes munkájának hatására működőképes marad.

---

### 2.2.3 Felhasználók

A rendszer használói olyan személyek, akik egy közös, folyamatosan változó városi
környezetben vállalnak szerepet. A résztvevők eltérő célokkal és felelősségi körrel
rendelkeznek, de tevékenységük egymásra hat. A játék során két alapvető szerep
különböztethető meg: buszvezetők és takarítók.

#### 1. Buszvezetők

**Jellemzőik:**
- Meghatározott útvonalon közlekedő jármű irányításáért felelnek.
- Folyamatosan figyelik az utak járhatóságát és a forgalmi helyzetet.
- Teljesítményük mérhető a megtett fordulók számával.

**Tulajdonságaik:**
- Érdekeltek abban, hogy az utak minél jobb állapotban legyenek.
- Az ütközések negatívan befolyásolják a teljesítményüket.
- Céljuk a folyamatos operáció.

#### 2. Takarítók

**Jellemzőik:**
- Hókotrókat irányítanak és azok működését szervezik.
- Eszközöket választanak, fejeket cserélnek, beszerzésekről döntenek az általuk
  megszerzett erőforrások és utak állapota alapján.

**Tulajdonságaik:**
- Szerepkörük előrelátó gondolkodást igényel; mérlegelik a költségeket és a várható hasznot.
- Mivel számos eszköz áll rendelkezésükre, alkalmazkodhatnak az időjárási és forgalmi körülményekhez.
- Egyszerre több jármű működését is átlátják.
- Céljuk minél több útszakasz megtisztítása és a város közlekedésének fenntartása.

---

### 2.2.4 Korlátozások

Az elkészült programnak minden helyzetben megbízhatóan és a tervezett logikának
megfelelően kell működnie. Alapvető elvárás, hogy a szoftver stabil maradjon, és hiba nélkül
teljesítse a felhasználók kéréseit.

### 2.2.5 Feltételezések, kapcsolatok

A kurzus hivatalos felülete rögzíti a benyújtási feltételek többségét, valamint meghatározza a
jelen dokumentáció szerkezeti felépítését és kötelező elemeit. A projektfeladat leírása pontosan
körvonalazza a fejlesztés elsődleges célkitűzéseit és a szoftverrel szemben támasztott
alapvető funkcionális elvárásokat.

---

## 2.3 Követelmények

### 2.3.1 Funkcionális követelmények

| Azonosító | Leírás | Ellenőrzés | Prioritás | Forrás | Use-case | Megjegyzés |
|---|---|---|---|---|---|---|
| REQ-01 | A rendszernek kezelnie kell az egymáshoz kapcsolódó útszakaszok és sávok állapotát (hó, jég, járhatóság). | Több sávos útszakaszon különböző hóállapotok kialakítása és megfigyelése. | MUST | Feladatleírás | Útszakasz állapotának figyelése | |
| REQ-02 | A havazásnak folyamatosan növelnie kell a hó mennyiségét az utakon. | Idő múlásával növekvő hóvastagság mérése. | MUST | Feladatleírás | Időjárás változás kezelése | |
| REQ-03 | Többsávos úton a jármű átválthat másik sávba, ha az járható. | Elakadt sáv mellett tiszta sáv használata. | SHOULD | Feladatleírás | Sávváltás | |
| REQ-04 | Jeges útfelületen a járművek megcsúszhatnak és ütközhetnek. | Jeges szakaszon baleset előidézése. | MUST | Feladatleírás | Ütközés kezelése | |
| REQ-05 | A hókotró egyszerre csak egy fejjel működhet. | Fejcsere végrehajtása és hatás ellenőrzése. | MUST | Feladatleírás | Fejcsere | |
| REQ-06 | A jégtörő fej feltöri a jeget (jeges → havas), de nem távolítja el. | Jég feltörése után havas állapot megfigyelése. | MUST | Feladatleírás | Jégtörés | |
| REQ-07 | A sószóró fej idővel elolvasztja a havat és jeget; megakadályozza az újabb hólerakódást. | Sózás után időbeli állapotváltozás mérése. | MUST | Feladatleírás | Sózás | |
| REQ-08 | A sárkány fej azonnal eltávolítja a havat, jeget, és a balesetes állapotot is. | Használat után azonnali száraz sáv. | MUST | Feladatleírás | Hőhatás alkalmazása | [JAVÍTVA] Balesetes sávra is hat. |
| REQ-09 | A fogyóanyag elfogyása esetén az adott fej hatástalanná válik. | Fogyóanyag kimerítése és hatás megszűnésének ellenőrzése. | MUST | Feladatleírás | Fogyóanyag kezelés | |
| REQ-10 | A sikeres takarítási művelet után 2 egység bevétel jár. | Tényleges állapotváltozást okozó tisztítás után bevétel növekedésének mérése. | MUST | Feladatleírás | Útszakasz tisztítása | [JAVÍTVA] Bevétel összege: 2 egység/művelet. |
| REQ-11 | A bevételből új fejek, fogyóanyag és hókotró vásárolható. | Vásárlási folyamat végrehajtása. | MUST | Feladatleírás | Eszközbeszerzés | |
| REQ-12 | A buszok fordulóit számolni kell; forduló = egy irányú, végállomásig megtett út. | Több oda-visszaút teljesítése és számlálás ellenőrzése. | MUST | Feladatleírás | Forduló teljesítése | [JAVÍTVA] Forduló = egy irányú út. |
| REQ-13 | Ütközés esetén a busz IMMOBILE_TIME (3 kör) ideig nem mozoghat. | Baleset után mozgásképtelenségi idő mérése. | MUST | Feladatleírás | Buszbaleset | |
| REQ-14 | Több buszvezető és takarító egy időben részt vehet a játékban. | Egyidejű többszereplős működés. | MUST | Feladatleírás | Többszereplős játék | |
| REQ-15 | A söprő és hányó fejek eltávolítják a havat és a feltört jeget a sávról. | A fejek alkalmazása, majd az út állapotának megfigyelése. | MUST | Feladatleírás | Takarítás | |
| REQ-16 | A söprő fej a havat a kisebb indexű, azonos irányú szomszéd sávba tolja, ha az létezik. | Söprő alkalmazása feltéve, hogy létezik a szomszéd sáv. | MUST | Feladatleírás | Takarítás | [JAVÍTVA] „jobbra lévő sáv" → kisebb indexű azonos irányú sáv. |
| REQ-17 | A söprő fej által eltakarított hó eltűnik, ha nincs szomszéd sáv. | Söprő alkalmazása, ha nem létezik szomszéd sáv. | MUST | Feladatleírás | Takarítás | |
| REQ-18 | A hányó fej által eltakarított hó eltűnik. | A hányó fej alkalmazása. | MUST | Feladatleírás | Takarítás | |
| REQ-19 | A sószóró fej idővel elolvasztja a havat/jeget, illetve meggátolja a további hólerakódást. | A sószóró fej alkalmazása, út állapotának megfigyelése. | MUST | Feladatleírás | Takarítás | |
| REQ-20 | A sárkány fej azonnal elolvasztja a havat/jeget. | A sárkány fej alkalmazása, út állapotának megfigyelése. | MUST | Feladatleírás | Takarítás | |
| REQ-21 | Ütközés csak jeges sávon következhet be. | Jeges sávon ütközés előidézése; havas/száraz sávon nincs ütközés. | MUST | Implementáció | Ütközés kezelése | [ÚJ] |
| REQ-22 | Ütközés után az érintett sáv balesetes állapotba kerül; IMMOBILE_TIME után jeges állapotba vált vissza. | Balesetes sáv megfigyelése, automatikus visszaváltás ellenőrzése. | MUST | Implementáció | Buszbaleset | [ÚJ] |
| REQ-23 | Hókotró ütközés esetén is folytatja munkáját (nem immobilizálódik). | Ütközés után hókotró takarítási képességének ellenőrzése. | MUST | Implementáció | Fejcsere | [ÚJ] |
| REQ-24 | A hókotró alapértelmezett kezdeti feje a söprő fej. | Új hókotró létrehozásakor söprő fejjel indul. | MUST | Implementáció | Eszközbeszerzés | [ÚJ] |

---

### 2.3.2 Erőforrásokkal kapcsolatos követelmények

| Azonosító | Leírás | Ellenőrzés | Prioritás | Forrás | Megjegyzés |
|---|---|---|---|---|---|
| POW-01 | A program Java nyelven fog elkészülni. | Csapattagok ellenőrzik (futtatják). | MUST | Csapat | |
| POW-02 | Az elkészült program fordítható lesz a kari felhőben ("Windows 10 20H2 - JDK-Eclipse-WSU" sablon). | Tesztfuttatás beadás előtt. | MUST | Csapat | |
| POW-03 | Futtatáshoz az ajánlott rendszer: Windows 10. | Tesztfuttatás beadás előtt. | SHALL | Csapat | Futtatható más platformokon is, azonban ezt garantálni nem tudjuk. |
| POW-04 | A futtatáshoz szükséges: billentyűzet és egér. | Futtatás előtt ellenőrizendő. | MUST | Csapat | |
| POW-05 | Jogok fájlok olvasásához/módosításához. | Futtatás előtt ellenőrizendő. | MUST | Csapat | Mentéshez és betöltéshez. |

---

### 2.3.3 Átadással kapcsolatos követelmények

| Azonosító | Leírás | Ellenőrzés | Prioritás | Forrás |
|---|---|---|---|---|
| DEL-01 | A dokumentációt digitális PDF formátumban, a megadott határidőig fel kell tölteni a Hercules rendszerbe. | A beküldő a feltöltést üzenetben erősíti meg. | MUST | Tárgylap |
| DEL-02 | A dokumentumok nyomtatott példányait az I-épület földszintjén kell leadni a hét első munkanapján. | A leadás tényét a leadó személy üzenetben erősíti meg. | MUST | Tárgylap |
| DEL-03 | A papíralapú dokumentációt átlátszó műanyag mappába rendezve kell benyújtani. | A beküldő személyesen ellenőrzi a csomag teljességét. | MUST | Tárgylap |
| DEL-04 | A dokumentumok fedlapjának szigorúan követnie kell a letölthető hivatalos sablont. | A beküldő ellenőrzi, hogy minden szükséges elem szerepel-e. | MUST | Tárgylap |
| DEL-05 | A leadott dokumentációnak tartalmaznia kell a megfelelő projektvezetési naplót is. | A beküldő feladata a teljes körű felülvizsgálat. | MUST | Tárgylap |
| DEL-06 | A szoftveres állományokat digitálisan, ZIP tömörítéssel kell feltölteni a Herculesbe a határidő lejárta előtt. | A beküldő a sikeres feltöltésről mentést csatol a naplóhoz. | MUST | Tárgylap |
| DEL-07 | Egy-egy fejlesztési szakaszban legfeljebb három alkalommal van lehetőség az anyagok feltöltésére. | A benyújtás feltétele a teljes csapat jóváhagyása. | MUST | Tárgylap |
| DEL-08 | A forráskódnak a kari felhő környezetében fordíthatónak kell lennie. | Beküldés előtt kötelező tesztfuttatás a megadott környezetben. | MUST | Tárgylap |
| DEL-09 | A forráskód mellé kötelezően csatolni kell a telepítési útmutatót és a használati segédletet is. | A beküldő ellenőrzi, hogy minden kötelező melléklet szerepel-e. | MUST | Tárgylap |

---

### 2.3.4 Egyéb nem funkcionális követelmények

| Azonosító | Leírás | Ellenőrzés | Prioritás | Forrás |
|---|---|---|---|---|
| NFR-001 | **Telepítésmentes indítás.** A szoftvernek közvetlenül futtathatónak kell lennie a gazda operációs rendszeren, telepítési folyamat nélkül. | Futtatás telepítés nélkül. | MUST | Csapat |
| NFR-002 | **Rendszerintegritás.** A program nem hajthat végre módosításokat a futtató operációs rendszeren vagy a tárolt fájlokon a játékmenethez szükséges mentések kivételével. | Rendszerfájlok vizsgálata. | MUST | Csapat |
| NFR-003 | **Hálózati izoláció.** A program nem kezdeményezhet semmilyen hálózati kommunikációt. | Hálózati forgalom monitorozása. | MUST | Csapat |
| NFR-004 | **Utasításkövetés.** A programnak stabilan és pontosan kell végrehajtania a felhasználók által kiadott összes utasítást. | Futtatási tesztek, felhasználói interakciók. | MUST | Csapat |
| NFR-005 | **Stabilitás.** A programnak stabilan, összeomlás és kritikus hiba nélkül kell futnia. | Hosszú távú futtatási teszt. | MUST | Csapat |
| NFR-006 | **Karbantarthatóság.** A szoftver forráskódja legyen áttekinthető, moduláris és megfelelően dokumentált a későbbi fejlesztések megkönnyítése érdekében. | Kódvizsgálat (code review). | SHOULD | Csapat |

---

## 2.4 Lényeges use-case-ek

### 2.4.1 Use-case leírások

| Use-case neve | Utak takarítása |
|---|---|
| **Rövid leírás** | A játékos megtisztítja a havas utakat a járművével. |
| **Aktorok** | Takarító |
| **Forgatókönyv** | 1. A takarító elkezdi az útszakasz tisztítását. 2. Szükség esetén az alfolyamatok (fej csere, vásárlás, karbantartás) aktiválódhatnak. |

| Use-case neve | Takarító fej csere |
|---|---|
| **Rövid leírás** | A meglévő takarítófej cseréje a járművön. |
| **Aktorok** | Nincs közvetlen (Az _Utak takarítása_ kiterjesztése) |
| **Forgatókönyv** | 1. Az utak takarítása során a takarítófej nem megfelelő a feladathoz. 2. A játékos lecseréli a fejet egy másikra a már megvásároltak közül. |

| Use-case neve | Hókotró fej vásárlás |
|---|---|
| **Rövid leírás** | Új hókotró fej beszerzése a hatékonyabb munkavégzéshez. |
| **Aktorok** | Nincs közvetlen (Az _Utak takarítása_ kiterjesztése) |
| **Forgatókönyv** | 1. Az utak takarítása közben a játékos úgy dönt, új eszközt vesz. 2. Megvásárolja az új hókotró fejet. |

| Use-case neve | Karbantartás |
|---|---|
| **Rövid leírás** | Só vagy biokerozin tartály feltöltése. |
| **Aktorok** | Nincs közvetlen (Az _Utak takarítása_ kiterjesztése) |
| **Forgatókönyv** | 1. Az utak karbantartása közben kifogy a biokerozin vagy a só. 2. A játékos elvégzi a szükséges utántöltéseket. |

| Use-case neve | Só feltöltés |
|---|---|
| **Rövid leírás** | A jármű sókészletének újratöltése. |
| **Aktorok** | Nincs közvetlen (A _Karbantartás_ kiterjesztése) |
| **Forgatókönyv** | 1. Az utak takarítása során kiderül, hogy fogyóban van a só. 2. A játékos feltölti a járművet sóval. |

| Use-case neve | Biokerozin feltöltés |
|---|---|
| **Rövid leírás** | A jármű biokerozin készletének újratöltése. |
| **Aktorok** | Nincs közvetlen (A _Karbantartás_ kiterjesztése) |
| **Forgatókönyv** | 1. Az utak takarítása során a biokerozin szintje csökken. 2. A játékos feltölti a gépet a szükséges anyaggal. |

| Use-case neve | Utasok szállítása |
|---|---|
| **Rövid leírás** | A buszvezető utasokat juttat el a végállomásra; minden megérkezés egy fordulót jelent. |
| **Aktorok** | Buszvezető |
| **Forgatókönyv** | 1. A buszvezető a két végállomás között közlekedik. 2. Minden végállomás elérése egy fordulónak számít és bevételt eredményez. |

| Use-case neve | Havazás |
|---|---|
| **Rövid leírás** | Folyamatosan történik; körönként adott mennyiségű hó esik a kültéri sávokra. |
| **Aktorok** | Körök előrehaladása |
| **Forgatókönyv** | 1. A körök múlásával esik a hó a pályán. 2. Ez automatikusan maga után vonja a hóréteg növekedését a sávokon. |

| Use-case neve | Hóréteg kialakulása az utakon |
|---|---|
| **Rövid leírás** | A hó felhalmozódik a pálya kültéri útjain. |
| **Aktorok** | Nincs közvetlen (A _Havazás_ beágyazása — include) |
| **Forgatókönyv** | 1. A havazás következtében a hómennyiség növekszik. 2. Ha eléri az 5-ös küszöbértéket, havas állapot alakul ki. |

| Use-case neve | Autók közlekednek |
|---|---|
| **Rövid leírás** | A gépi irányítású járművek forgalmának szimulációja. |
| **Aktorok** | Útvonal meghatározó |
| **Forgatókönyv** | 1. A rendszer legenerálja a forgalmat. 2. Az autók haladnak a kijelölt útvonalakon. |

| Use-case neve | Kijegesedik az út |
|---|---|
| **Rövid leírás** | Az útfelület csúszóssá, jegessé válik a forgalom és a hó miatt. |
| **Aktorok** | Nincs közvetlen (Az _Autók közlekednek_ kiterjesztése) |
| **Forgatókönyv** | 1. Az autók közlekedése hatására a havas sávon 5 jármű után jegesedés alakul ki. |

| Use-case neve | Ütközés |
|---|---|
| **Rövid leírás** | Ha egy autó vagy busz egy másik járművel jeges sávon közlekedik, valamekkora eséllyel egymásnak ütköznek. |
| **Aktorok** | Randomizátor |
| **Forgatókönyv** | 1. A randomizátor egy baleseti eseményt vált ki az autók közlekedése közben jeges sávon. 2. A baleset következtében az érintett sáv balesetes állapotba kerül; az érintett járművek IMMOBILE_TIME ideig elakadnak. |

| Use-case neve | Autók egymásba csúszása |
|---|---|
| **Rövid leírás** | Két vagy több személyautó egymásnak ütközik. |
| **Aktorok** | Nincs közvetlen (Az _Ütközés_ kiterjesztése) |
| **Forgatókönyv** | 1. Két autó egy jeges útszakaszon közlekedik és a Randomizátor összeütközteti őket. 2. A csúszós úton személyautók csúsznak egymásba. |

| Use-case neve | Autók buszba csúszása |
|---|---|
| **Rövid leírás** | Egy vagy több személyautó nekiütközik egy busznak. |
| **Aktorok** | Nincs közvetlen (Az _Ütközés_ kiterjesztése) |
| **Forgatókönyv** | 1. Egy autó és egy busz egy jeges útszakaszon közlekedik és a Randomizátor összeütközteti őket. 2. Egy megcsúszó autó egy busznak ütközik. |

| Use-case neve | Buszok egymásba csúszása |
|---|---|
| **Rövid leírás** | Két vagy több busz balesetet szenved a csúszós úton. |
| **Aktorok** | Buszvezető |
| **Forgatókönyv** | 1. Két buszvezető két buszt egy jeges útszakaszra irányít és a Randomizátor összeütközteti őket. 2. A buszok egymásnak ütköznek. |

| Use-case neve | Eredmények mentése |
|---|---|
| **Rövid leírás** | A buszok által megtett fordulókból és a hókotrók által gyűjtött pénzből számított pontszám elmentése. |
| **Aktorok** | Eredmény nyilvántartó rendszer |
| **Forgatókönyv** | 1. A játékosok úgy döntenek, abbahagyják a játékot. 2. A rendszer naplózza az elért eredményeket és elmenti azokat. |

---

## 2.4.2 Use-case diagram

_(Az eredeti dokumentumban szereplő UML use-case diagram változatlanul érvényes;
az ábrán szereplő `<<extend>>` és `<<include>>` kapcsolatok az alábbi módosítással
pontosítandók: az „Ütközés" use-case előfeltétele a jeges sáv megléte.)_

---

## 2.5 Szótár

_(Lásd 2.1.3 Definíciók, rövidítések — az összes kulcsfogalom ott szerepel.)_

---

## 2.6 Projekt terv

A csapatmunkához a következő kooperációt segítő szolgáltatásokat vesszük igénybe:

- **GitHub**: Verziókezelő platform forráskód tárolására és együttműködésre.
- **Discord**: Valós idejű hang-, videó- és szöveges kommunikációs platform.
- **Google Docs**: Online szövegszerkesztő dokumentumok létrehozására és közös szerkesztésére.
- **Google Drive**: Felhő alapú tárhely szolgáltatás fájlok tárolására, megosztására és szinkronizálására.

| Fázis / Esemény | Határidő | Felelősök |
|---|---|---|
| Követelmény, projekt, funkcionalitás (Konzultáció) | 2026. márc. 2. | Kiss Vince, Koncz Benjámin, Göröcs-Szinetár Milán, Tóth Andor, Vincze Botond |
| Analízis modell (I. változat) (Konzultáció) | 2026. márc. 9. | Kiss Vince, Koncz Benjámin, Göröcs-Szinetár Milán, Tóth Andor, Vincze Botond |
| Analízis modell (II. változat) (Konzultáció) | 2026. márc. 16. | Kiss Vince, Koncz Benjámin, Göröcs-Szinetár Milán, Tóth Andor, Vincze Botond |
| Szkeleton tervezése (Konzultáció) | 2026. márc. 23. | Kiss Vince, Koncz Benjámin, Göröcs-Szinetár Milán, Tóth Andor, Vincze Botond |
| Szkeleton elkészítése (Konzultáció) | 2026. márc. 30. | Kiss Vince, Koncz Benjámin, Göröcs-Szinetár Milán, Tóth Andor, Vincze Botond |
| Szkeleton bemutatása | 2026. ápr. 1. | Kiss Vince, Koncz Benjámin, Göröcs-Szinetár Milán, Tóth Andor, Vincze Botond |
| Tavaszi szünet | 2026. ápr. 8. | — |
| Prototípus koncepciója (Konzultáció) | 2026. ápr. 13. | Kiss Vince, Koncz Benjámin, Göröcs-Szinetár Milán, Tóth Andor, Vincze Botond |
| Részletes tervek (Konzultáció) | 2026. ápr. 20. | Kiss Vince, Koncz Benjámin, Göröcs-Szinetár Milán, Tóth Andor, Vincze Botond |
| Prototípus elkészítése (Konzultáció) | 2026. máj. 4. | Kiss Vince, Koncz Benjámin, Göröcs-Szinetár Milán, Tóth Andor, Vincze Botond |
| Prototípus bemutatása / Grafikus változat tervei (Konzultáció) | 2026. máj. 11. | Kiss Vince, Koncz Benjámin, Szinetár Milán, Tóth Andor, Vincze Botond |
| Grafikus változat elkészítése (Konzultáció) / Egyesített dokumentáció (elektronikus beadás) | 2026. máj. 27. | Kiss Vince, Koncz Benjámin, Szinetár Milán, Tóth Andor, Vincze Botond |
| Grafikus verzió bemutatása | 2026. máj. 29. | Kiss Vince, Koncz Benjámin, Szinetár Milán, Tóth Andor, Vincze Botond |

---

## 2.7 Napló

| Kezdet | Időtartam | Résztvevők | Leírás |
|---|---|---|---|
| 2026.02.26. 19:00 | 2,5 óra | Kiss, Tóth, Koncz, Szinetár, Vincze | Kezdő értekezlet, feladatok megbeszélése, mindenki gondolkodik a következő értekezletig. |
| 2026.02.28. 18:00 | 2 óra | Kiss | Funkcionális követelmények készítése. |
| 2026.03.01. 7:00 | 2 óra | Szinetár | Funkciók/áttekintés/definíciók/felhasználók leírásának kidolgozása. |
| 2026.03.01. 10:00 | 2 óra | Kiss, Tóth, Koncz, Szinetár, Vincze | UML diagram készítése, a dokumentum további részeinek kiosztása. |
| 2026.03.01. 19:00 | 1 óra | Tóth | Use-case-ek táblázatos leírása. |
| 2026.03.01. 19:00 | 1,5 óra | Vincze | Követelmények megfogalmazása, szöveg megformázása. |
| 2026.03.01. 20:00 | 20 perc | Tóth | Projekt terv leírása. |
| 2026.03.01. 20:20 | 20 perc | Tóth | Apróbb formázások a dokumentumban. |

---

## Változtatások összefoglalója (eredeti → átdolgozott)

| # | Szakasz | Változás típusa | Leírás |
|---|---|---|---|
| 1 | 2.1.3 Definíciók | **[JAVÍTVA]** | „Híd / Alagút" → „Alagút": csak alagútsávot kezel az implementáció; hídszakaszok kültéri sávként modellezendők. |
| 2 | 2.1.3 Definíciók | **[ÚJ]** | Sávállapot táblázat hozzáadva: Száraz, Havas, Jeges, Sózott, Balesetes. |
| 3 | 2.1.3 Definíciók | **[JAVÍTVA]** | Letaposottság: folyamatos mennyiség helyett áthaladó járművek számlálója (küszöb: 5). |
| 4 | 2.1.3 Definíciók | **[JAVÍTVA]** | Forduló: egy irányú út (nem oda-vissza kerek forduló). |
| 5 | 2.1.3 Definíciók | **[JAVÍTVA]** | Hányó fej ára 0, söprő fej ára 1 — az eredeti emelkedő sorrend az első két fejnél megfordul. Ár-lista pontosítva. |
| 6 | 2.1.3 Definíciók | **[JAVÍTVA]** | Sárkány fej: balesetes sávot is képes megtisztítani (DryState-be vált). |
| 7 | 2.1.3 Definíciók | **[ÚJ]** | Hókotró: alapértelmezett feje a söprő fej; ütközéstől nem immobilizálódik. |
| 8 | 2.2.2 Funkciók | **[JAVÍTVA]** | Sávállapot-modell expliciten leírva (állapotgép); hóvastagság és járhatósági küszöbértékek megadva. |
| 9 | 2.2.2 Funkciók | **[JAVÍTVA]** | Söprő fej iránya: „jobbra" → „kisebb indexű, azonos irányú szomszéd sáv". |
| 10 | 2.2.2 Funkciók | **[JAVÍTVA]** | Bevétel összege: 2 egység per sikeres takarítási művelet. |
| 11 | 2.3.1 Követelmények | **[JAVÍTVA]** | REQ-08: sárkány fej balesetes sávra is hat. |
| 12 | 2.3.1 Követelmények | **[JAVÍTVA]** | REQ-10: bevétel összege expliciten 2 egység. |
| 13 | 2.3.1 Követelmények | **[JAVÍTVA]** | REQ-12: forduló = egy irányú út. |
| 14 | 2.3.1 Követelmények | **[JAVÍTVA]** | REQ-16: söprő fej iránya pontosítva. |
| 15 | 2.3.1 Követelmények | **[ÚJ]** | REQ-21: ütközés csak jeges sávon következhet be. |
| 16 | 2.3.1 Követelmények | **[ÚJ]** | REQ-22: balesetes sáv IMMOBILE_TIME után jeges állapotba vált vissza. |
| 17 | 2.3.1 Követelmények | **[ÚJ]** | REQ-23: hókotró ütközés esetén folytatja munkáját. |
| 18 | 2.3.1 Követelmények | **[ÚJ]** | REQ-24: hókotró alapértelmezett feje söprő fej. |
