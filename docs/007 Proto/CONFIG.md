## A program által használt állapotkonfigurációs fájl formátuma

---

```text
@config 

CARCOUNT 4
PLAYER player_1 BUS
PLAYER player_2 CLEANER
PLAYER player_3 CLEANER

FLAGS <randomoff> <testmode>  <- DETERMINISZTIKUS MŰKÖDÉS

SEED <random> 39   <- DETERMINISZTIKUS MŰKÖDÉS
SEED <map> 5       <- KELL? HOGYAN GENERÁLJUK A MAPET?

@begin 

@newround ID 1
player_1
pick lane_5
@endround

@newround ID 2
player_2
pick lane_6 clean
@endround

@newround ID 3
player_3
swap dragon
refill dragon
pick lane_7 clean
@endround


@end
```

## @config

> Itt adhatóak meg a játék beállításai, amikkel az első kör indul

## @begin - @newround - @endround - @end
 
> Ide kerülnek az egyes körök forgatókönyvei