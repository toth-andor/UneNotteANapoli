## A prototípus felhasználói felülete

---

### I. Konfigurálás

#### A program indítása után a következő felületen lehet beállítani a játék kezdeti állapotát

```text
-----------------------------------------------------------------
          Játék inicializálása [Felhasználói mód] [Rand]
-----------------------------------------------------------------

? =========================== DOCS ============================ ?
| Útmutató a program működéséhez: help                          |
? ------------------------------------------------------------- ?
| Játék vezérlésének leírása: help game                         |
? ------------------------------------------------------------- ?
| Konfigurációs útmutató: help conf                             |
| Külső konfigurációs fájl elvárt formátuma: help conf format   |
? ------------------------------------------------------------- ?
| Tesztelési útmutató: help test                                |
? ------------------------------------------------------------- ?

# ================= TESZTELÉS [RANDOMIZÁCIÓ] =================== #
| A randomizáció kikapcsolása: randomoff <SEED>                  |
| A randomizáció bekapcsolása: randomon                          |
# -------------------------------------------------------------- #

# ========================= VEZÉRLÉS ========================== #
| Játék indítása: start                                         |
# ------------------------------------------------------------- #
| Kilépés: exit                                                 |
# ------------------------------------------------------------- #

* ======================== BEÁLLÍTÁSOK ======================== *
| Konfiguráció betöltése: load <src filepath>                   |
* ------------------------------------------------------------- *
| Konfiguráció törlése: clear                                   |
* ------------------------------------------------------------- *
| Tesztelési mód engedélyezése: mode test                       |  <- EZ CSAK FELHASZNÁLÓI MÓDBAN ÉRHETŐ EL
| Tesztelési mód kikapcsolása: mode user                        |  <- EZ CSAK TESZTELÉSI MÓDBAN ÉRHETŐ EL
* ------------------------------------------------------------- *

============================= AUTÓK =============================
----------------------------
Autók száma: nincs megadva
----------------------------
* ------------------------------------------------------------- *
| Konfigurálása: carcount <value: non-negative integer>         |
* ------------------------------------------------------------- *

* ========================= JÁTÉKOSOK ========================= *
| Játékos felvétele: addplayer <role: bus/cleaner> [name]       |
| Játékos eltávolítása:  rmplayer <name>                        |
* ------------------------------------------------------------- *

----------------------------
Aktív játékosok
----------------------------
# player_1 [BUS]
# player_2 [CLEANER]
----------------------------

> 
```

---

### Konfigurálás: DOCS
Itt lesznek elérhetőek a működéssel kapcsolatos dokumentációk. (basically egy `man` page)

* `help`: általános sugó. 
* `help game`: játék irányítása.
* `help conf`: hogyan kell használni a fenti interfacet.
* `help conf format`: milyen formátumú a betölthető konfig file.
* `help test`: sugó a tesztelés menetéről, működéséről

> Parancsok kimenete: megnyílik az adott sugó ablak (ezt még össze kell rakni mert be fognak szólni ha csak ennyi írunk).

### Konfigurálás: TESZTELÉS [RANDOMIZÁCIÓ]

* `randomoff <SEED>`: kikapcsolja a randomizációt, a SEED értékével megadható, hogy a randomizált elemek milyen állandó értékkel működjenek.
* `randomon`: visszakapcsolja a randomizációt.

> Parancsok kimenete: a randomizáció sikeres ki/be kapcsolását üzenet nyugtázza.

### Konfigurálás: VEZÉRLÉS
Alapvető vezérlési parancsok
* `start`: a megadott konfigurációval elindul a játék.
* `exit`: leállítás.

> Parancsok kimenete: `exit` hatására leáll a program, `start` hatására ha helyes a konfiguráció (pl.: van regisztrált játékos) elindul a játék első köre. Helytelen konfiguráció esetén ezt üzenet jelzi.

### Konfigurálás: BEÁLLÍTÁSOK
* `load <src filepath>`: manuális konfig helyett külső fileból történő inicializálás.
* `clear`: alaphelyzetbe állítja a konfiguációs ablakot.
* `mode test`: engedélyezi a tesztelési módot, bekapcsol a játék automatikus fájlba loggolása a tesztelhetőség biztosításához.
* `mode user`: kikapcsolja a tesztelési módot.

> Parancsok kimenete: a sikeres `betöltést` üzenet naplózza. Ekkor beálllításra kerülnek a fájlban specifikált paraméterek és lefut a forgatókönyv. Érvénytelen konfigurációs fájl esetén hibaüzenet keletkezik. 

### Konfigurálás: AUTÓK
* `carcount <value: non-negative integer>`: a térképen közlekedő autók számának beállítása.

> Parancsok kimenete: az `autók` számának konfigurációját sikeres üzenet nyugtázza, vagy hibaüzenet érvénytelen érték esetén.  

### Konfigurálás: JÁTÉKOSOK
* `addplayer <role: bus/cleaner> [name]`: játékos hozzáadása.
* `rmplayer <name>`: játékos eltávolítása.

> Parancsok kimenete: játékos `hozzáadása` esetén a sikeres felvételt üzenet igazolja vissza és a játékos megjelenik az `aktív játékosok listában`. Játékos `eltávolításakor` a sikeres műveletet üzenet jelzi. Érvénytelen bemenet esetén mind felvételkor (pl.: érvénytelen role), mind eltávolításkor (pl.: érvénytelen név) hibaüzenet keletkezik.

`Note: ` játékos felvételekor a név opcionális, ha üres akkor automatikusan generál (pl.: player_1, player_2).

---

### II. Játék menete

#### Konfigurálás után a megadott beállításokkal `start` hatására elindul a játék.

```text
------------------------------------------------
   Játék folyamatban [Felhasználói mód] [Rand]
------------------------------------------------
                Jelenlegi kör: 1
------------------------------------------------

? ================= DOCS ================== ?
| Játék vezérlésének leírása: help game     |
? ----------------------------------------- ?
| Tesztelési útmutató: help test            |
? ----------------------------------------- ?

================= JÁTÉKOSOK ================
Jelenlegi játékos: player_1 [BUS]
Score: 120
--------------------------------------------
Következő játékos: player_2 [CLEANER]
Score: 95

================== TÉRKÉP ==================
Jelenlegi út: road_4
Jelenlegi sáv: lane_12  [DRY ✓]
--------------------------------------------
Célállomás: junction_21                         <- EZ CSAK AKKOR ÉRHETŐ EL HA A JÁTÉKOS BUSZ (random?)
--------------------------------------------
Következő kereszteződés: junction_3
-----------------------
Innen elérhető: 
--------------
# road_5
	 #lane_14  [OL] [SNOWY ✓]   <- OL: OutdoorLane/TL: TunnelLane, ✓: járható a sáv/x: járhatatlan a sáv
	 #lane_15  [OL] [SNOWY x]
	 #lane_16  [OL] [ICY x]
	 #lane_17  [TL] [DRY ✓]
# road_6
	 #lane_18  [TL] [DRY ✓]
	 #lane_19  [OL] [SNOWY x]
	 #lane_20  [OL] [ICY x]
	 #lane_21  [TL] [DRY ✓]
# road_7
	 #lane_22  [OL] [SNOWY ✓]
	 #lane_23  [OL] [CRASHED x]
	 #lane_24  [OL] [ICY x]
	 #lane_25  [TL] [DRY ✓]

* ============================= VEZÉRLÉS ============================ *
| Sáv választása: pick <lane>                                         |
| Sáv választása takarítással: pick <lane> -clean                     |   <- EZ CSAK AKKOR ÉRHETŐ EL HA A JÁTÉKOS TAKARÍTÓ
* ------------------------------------------------------------------- *
| Fej cseréje: swap <attachment>                                      |   <- EZ CSAK AKKOR ÉRHETŐ EL HA A JÁTÉKOS TAKARÍTÓ
| Fej/Hókotró vásárlása: buy <attachment/snowplow>                    |   <- EZ CSAK AKKOR ÉRHETŐ EL HA A JÁTÉKOS TAKARÍTÓ
| Fej újratöltése: refill <attachment>                                |   <- EZ CSAK AKKOR ÉRHETŐ EL HA A JÁTÉKOS TAKARÍTÓ
* ------------------------------------------------------------------- *

* =========================== KONFIGURÁCIÓ ========================== *
| Aktuális állapotkonfiguráció fájlba mentése: save <dst filepath>    |
| Új állapotkonfiguráció betöltése fájlból: load <src filepath>       |
* ------------------------------------------------------------------- *

* ======================== TESZTELÉS [LOG] ========================== *
| Akutális állapot loggolása fájlba: snapshot <dst filepath>          |
| Aktuális állapot loggolása konzolra: state                          |
* ------------------------------------------------------------------- *
| Kilépés: exit                                                       |
* ------------------------------------------------------------------- *

player_1 [BUS] > 
```
---


### Játék menete: VEZÉRLÉS

* `pick <lane>`: választás az adott kereszteződésből elérhető sávok közül.
* `pick <lane> -clean`: takarító játékos esetén elérhető, végighaladás a sávon takarítással.
* `swap <attachment>`: takarító játékos esetén elérhető, váltás a meglévő fejek között.
* `buy <attachment/snowplow>`: takarító játékos esetén elérhető, új fej vagy hókotró vásárlása.
* `refill <attachment>`: takarító játékos esetén elérhető, fogyóanyagot használó fej újratöltése.
* `save <dst filepath>`: a jelenlegi állapot mentése fájlba, formátuma a program által használt konfigurációs formátum.
* `load <src filepath>`: funkcionalitása megyegyezik az __inicializációs__ felületen található __load__ parancséval. Törli a jelenlegi állapotot és betölti a fájl tartalmát.

> Parancsok kimenete: ebben az esetben is üzenet nyugtázza a sikeres műveleteket. Amennyiben választott sáv járhatatlan, a takarítónak nincs elég pontja a vásárláshoz vagy a hivatkozott fájlok hibásak, hibaüzenet keletkezik.
---

## Szálkezelés (ide ki kéne találni valami értelmeset)

A prototípusban az események szekvenciálisan kerülnek végrehajtásra,
így a működés determinisztikus és tesztelhető.

Az időalapú működés (pl. hóesés, mozgás) körökre van bontva.

---

(Parsert írni vicces lesz xd)
