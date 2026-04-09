## A snapshot formátuma

---


```text

[SNAPSHOT OF ROUND 1]

@global
ROUND_ID 1
CURRENT_PLAYER player_2 ROLE cleaner
NEXT_PLAYER player_3 ROLE cleaner

@players
PLAYER <player> <role> <score>      <- EZEK MAJD CSAK A HELP TEST-BE MENNEK, IDE NEM KELL

PLAYER player_1 ROLE bus SCORE 120
PLAYER player_2 ROLE cleaner SCORE 80
PLAYER player_3 ROLE cleaner SCORE 70

@positions
POSITION <player> <road> <lane> <state>

POSITION player_1 ROAD road_4 LANE lane_12 STATE dry
POSITION player_2 ROAD road_5 LANE lane_14 STATE snowy
POSITION player_3 ROAD road_6 LANE lane_18 STATE dry

@lanes
LANE <lane> <road> <type> <state> <available>

LANE lane_1 ROAD road_5 TYPE outdoor STATE snowy AVAILABLE true
LANE lane_2 ROAD road_5 TYPE outdoor STATE crashed AVAILABLE false
LANE lane_3 ROAD road_6 TYPE tunnel STATE dry AVAILABLE true
LANE lane_4 ROAD road_9 TYPE outdoor STATE snowy AVAILABLE false

@attachments
ATTACHMENT <type> <owner> <fuel>

ATTACHMENT dragon OWNER player_2 FUEL true
ATTACHMENT sweeper OWNER player_2 FUEL none
ATTACHMENT salt OWNER player_3 FUEL false
ATTACHMENT icebreaker OWNER player_3 FUEL none

@cars
CAR <id> <road> <lane> <ismobile>

CAR car_1 ROAD road_2 LANE lane_3 ISMOBILE true
CAR car_2 ROAD road_4 LANE lane_6 ISMOBILE true
CAR car_3 ROAD road_1 LANE lane_9 ISMOBILE false
CAR car_4 ROAD road_1 LANE lane_9 ISMOBILE false

[END SNAPSHOT]

[SNAPSHOT OF ROUND 2]

@global
ROUND_ID 2
CURRENT_PLAYER player_3 ROLE cleaner
NEXT_PLAYER player_1 ROLE bus

@players
PLAYER <player> <role> <score>

PLAYER player_1 ROLE bus SCORE 120
PLAYER player_2 ROLE cleaner SCORE 90
PLAYER player_3 ROLE cleaner SCORE 70

@positions
POSITION <player> <road> <lane> <state>

POSITION player_1 ROAD road_7 LANE lane_11 STATE dry
POSITION player_2 ROAD road_4 LANE lane_16 STATE snowy
POSITION player_3 ROAD road_9 LANE lane_10 STATE snowy

@lanes
LANE <lane> <road> <type> <state> <available>

LANE lane_1 ROAD road_5 TYPE outdoor STATE snowy AVAILABLE true
LANE lane_2 ROAD road_5 TYPE outdoor STATE crashed AVAILABLE false
LANE lane_3 ROAD road_6 TYPE tunnel STATE dry AVAILABLE true
LANE lane_4 ROAD road_9 TYPE outdoor STATE snowy AVAILABLE false

@attachments
ATTACHMENT <type> <owner> <fuel>

ATTACHMENT dragon OWNER player_2 FUEL false
ATTACHMENT sweeper OWNER player_2 FUEL none
ATTACHMENT salt OWNER player_3 FUEL true
ATTACHMENT icebreaker OWNER player_3 FUEL none

@cars
CAR <id> <road> <lane> <ismobile>

CAR car_1 ROAD road_2 LANE lane_3 ISMOBILE true
CAR car_2 ROAD road_4 LANE lane_6 ISMOBILE true
CAR car_3 ROAD road_1 LANE lane_9 ISMOBILE true
CAR car_4 ROAD road_9 LANE lane_1 ISMOBILE true

[END SNAPSHOT]

```
---
### Szerkezet: [ ]
* `[SNAPSHOT OF ROUND X]`: az adott kör állapotáról készült snapshot log eleje.
* `[END OF SNAPSHOT]`: az adott kör állapotáról készült snapshot log vége.

### Blokkok: @

* `@global`: általános beállítások (kör, jelenlegi és következő játékosok).
* `@players`: minden a kezdéskor regisztrált játékos.
* `@positions`: minden a kezdéskor regisztrált játékos pozíciója.
* `@lanes`: a térképen szereplő sávok és ezek állapotai.
* `@attachments`: a takarítók által birtokolt fejek és üzemanyag szintjeik (ahol releváns, vagy "none").
* `@cars`: a térképen közlekedő autók listája.

---

> Összehasonlítás tesztelésnél szerintem a built-in diff(bash)/fc(win)-vel a legegyszerűbb.