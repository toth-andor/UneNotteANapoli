## A szkeleton modell valóságos use-case-ei

### Use-case 1: Havazás

| Use-case neve	| Havazás |
|--- | ---|
| Rövid leírás		| Egy OutdoorLane-en havazik, a sáv állapota SnowyState-re változik. |
| Aktorok		| OutdoorLane, DryState, SnowyState |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Havazik a sávon, állapota SnowyState-re változik. |

### Use-case 2: Hóeltakarítás söpréssel

| Use-case neve	| Hóeltakarítás söpréssel |
|--- | ---|
| Rövid leírás		| Egy hókotró söprőfejjel letakarítja a havat egy OutdoorLane-en. |
| Aktorok		| Cleaner, OutdoorLane, SnowyState, DryState, SnowPlow, Sweeper |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben. **2.** A SnowPlow söprőfejjel letakarítja a havat, a sáv állapota DryState-re változik. |

### Use-case 3: Jeges úton történő baleset

| Use-case neve	| Jeges úton történő baleset |
|--- | ---|
| Rövid leírás		| Két autó ütközik egy jeges úton, a sáv állapota CrashedState-re változik. |
| Aktorok		| OutdoorLane, IcyState, CrashedState, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t IcyState-ben. **2.** Két Car ütközik a sávon, a sáv állapota CrashedState-re változik. |

### Use-case 4: Hóeltakarítás sószóróval

| Use-case neve	| Hóeltakarítás sószóróval |
|--- | ---|
| Rövid leírás		| Egy hókotró sószóró fejjel letakarítja a havat egy OutdoorLane-en. |
| Aktorok		| Cleaner, OutdoorLane, SnowyState, SaltedState, SnowPlow, SaltVomitter |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben. **2.** A SnowPlow sószóró fejjel letakarítja a havat, a sáv állapota SaltedState-re változik. |

### Use-case 5: Busz megfordulása és pontszerzés

| Use-case neve	| Busz megfordulása és pontszerzés |
|--- | ---|
| Rövid leírás		| Egy busz megfordul, és pontot szerez. |
| Aktorok		| Bus |
| Forgatókönyv | **1.** Létrehozunk egy Bus-t úgy, hogy a `currentLane` megegyezzen az egyik végállomással. **2.** A busz megfordul, és pontot szerez. |

### Use-case 6: Hókotró fej vásárlása

| Use-case neve	| Hókotró fej vásárlása |
|--- | ---|
| Rövid leírás		| Egy hókotró új fejet vásárol, és az egyenlege csökken. |
| Aktorok		| Cleaner, SnowPlow, Sweeper |
| Forgatókönyv | **1.** Létrehozunk egy SnowPlow-t. **2.** A SnowPlow vásárol egy Sweeper fejet, és az egyenlege csökken. |

### Use-case 7: Hóeltakarítás jégtörővel

| Use-case neve	| Hóeltakarítás jégtörővel |
|--- | ---|
| Rövid leírás		| Egy hókotró jégtörő fejjel letakarítja a havat egy OutdoorLane-en. |
| Aktorok		| Cleaner, OutdoorLane, SnowyState, SnowPlow, IceBreaker |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben. **2.** A SnowPlow jégtörő fejjel letakarítja a havat, a sáv állapota változatlanul SnowyState marad. |

### Use-case 8: Két autó ütközik jeges úton

| Use-case neve	| Két autó ütközik jeges úton |
|--- | ---|
| Rövid leírás		| Két autó ütközik egy jeges úton. |
| Aktorok		| OutdoorLane, IcyState, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t IcyState-ben. **2.** Létrehozunk 2 Car-t. **3.** A két Car-t egymás után rátesszük az útra. **4.** A két autó egymásnak ütközik. |

### Use-case 9: Hókotró fej újratöltése

| Use-case neve	| Hókotró fej újratöltése |
|--- | ---|
| Rövid leírás		| Egy hókotró újratölti a fejét, és az egyenlege csökken. |
| Aktorok		| Cleaner, Dragon/SaltVomitter, SnowPlow, Sweeper |
| Forgatókönyv | **1.** Létrehozunk egy Cleaner-t és hozzá egy SnowPlow-t. **2.** A SnowPlow-t felszereljük egy Dragon/SaltVomitter fejjel. **3.** A SnowPlow újratölti a Dragon/SaltVomitter fejet, és a Cleaner egyenlege csökken. |

### Use-case 10: Busz/Autó interakció hóval borított úton

| Use-case neve	| Busz/Autó interakció hóval borított úton |
|--- | ---|
| Rövid leírás		| Egy busz/autó interaktál egy hóval borított úton, és letapossa a havat. |
| Aktorok		| OutdoorLane, SnowyState, IcyState, Bus, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben és egy buszt/autót. **2.** A busz/autó interaktál a sávval ahányszor a jéggé taposáshoz kell, és letapossa a havat. **3.** A sáv IcyState-be kerül. |

### Use-case 11: Hókotró fej csere

| Use-case neve	| Hókotró fej csere |
|--- | ---|
| Rövid leírás		| Egy hókotró cseréli a fejét. |
| Aktorok		| Cleaner, SnowPlow, Sweeper, IceBreaker |
| Forgatókönyv | **1.** Létrehozunk egy SnowPlow-t úgy, hogy birtokoljon Sweeper és IceBreaker fejet. **2.** A SnowPlow cseréli a Sweeper fejét IceBreaker fejre. |

### Use-case 12: Hóeltakarítás sárkányfejjel

| Use-case neve	| Hóeltakarítás sárkányfejjel |
|--- | ---|
| Rövid leírás		| Egy hókotró sárkányfejjel letakarítja a havat egy OutdoorLane-en. |
| Aktorok		| Cleaner, OutdoorLane, SnowyState, DryState, SnowPlow, Dragon |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben. **2.** A SnowPlow sárkányfejjel letakarítja a havat, a sáv állapota DryState-re változik. |

### Use-case 13: Hóeltakarítás hányófejjel

| Use-case neve	| Hóeltakarítás hányófejjel |
|--- | ---|
| Rövid leírás		| Egy hókotró hányófejjel letakarítja a havat egy OutdoorLane-en. |
| Aktorok		| Cleaner, OutdoorLane, SnowyState, DryState, SnowPlow, VomitingHead |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben. **2.** A SnowPlow hányófejjel letakarítja a havat, a sáv állapota DryState-re változik. |

### Use-case 14: Autó interakció száraz úton

| Use-case neve	| Autó interakció száraz úton |
|--- | ---|
| Rövid leírás		| Egy autó interaktál egy száraz úton, és nem csúszik meg. |
| Aktorok		| OutdoorLane, DryState, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Egy Car interaktál a sávon, és nem csúszik meg. |

### Use-case 15: Hókotró interakció sószóró fejjel

| Use-case neve	| Hókotró interakció sószóró fejjel |
|--- | ---|
| Rövid leírás		| Egy hókotró sószóró fejjel interaktál egy sávon. |
| Aktorok		| Cleaner, OutdoorLane, DryState, SaltedState, SnowPlow, SaltVomitter |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** A SnowPlow sószóró fejjel interaktál a sávon. **3.** A sáv SaltedState-be kerül. |

### Use-case 16: Hóeltakarítás sószóró fejjel

| Use-case neve	| Hóeltakarítás sószóró fejjel |
|--- | ---|
| Rövid leírás		| Egy hókotró sószóró fejjel letakarítja a havat egy OutdoorLane-en. |
| Aktorok		| Cleaner, OutdoorLane, SnowyState, SaltedState, SnowPlow, SaltVomitter |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben. **2.** A SnowPlow sószóró fejjel letakarítja a havat, a sáv állapota SaltedState-re változik. |

### Use-case 17: Hóeltakarítás hányófejjel

| Use-case neve	| Hóeltakarítás hányófejjel |
|--- | ---|
| Rövid leírás		| Egy hókotró hányófejjel letakarítja a havat egy OutdoorLane-en. |
| Aktorok		| Cleaner, OutdoorLane, SnowyState, DryState, SnowPlow, VomitingHead |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben. **2.** A SnowPlow hányófejjel letakarítja a havat, a sáv állapota DryState-re változik. |

### Use-case 18: Autó interakció sószórt úton

| Use-case neve	| Autó interakció sószórt úton |
|--- | ---|
| Rövid leírás		| Egy autó interaktál egy sószórt úton, és nem csúszik meg. |
| Aktorok		| OutdoorLane, SaltedState, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SaltedState-ben. **2.** Egy Car interaktál a sávon, és nem csúszik meg. |

### Use-case 19: Busz interakció sószórt úton

| Use-case neve	| Busz interakció sószórt úton |
|--- | ---|
| Rövid leírás		| Egy busz interaktál egy sószórt úton, és nem csúszik meg. |
| Aktorok		| OutdoorLane, SaltedState, Bus |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SaltedState-ben. **2.** Egy Bus interaktál a sávon, és nem csúszik meg. |

### Use-case 20: Hókotró söprőfejjel takarít havas utat

| Use-case neve	| Hókotró söprőfejjel takarít havas utat |
|--- | ---|
| Rövid leírás		| Hókotró söprőfejjel takarít havas utat. |
| Aktorok		| Cleaner, OutdoorLane, DryState, SnowyState, SnowPlow, Sweeper, Road |
| Forgatókönyv | **1.** Létrehozunk két OutdoorLane-t, egyiket SnowyState-ben, a másikat DryState-ben, és hozzáadjuk őket egy Road-hoz. **2.** A SnowPlow Sweeper fejjel interaktál a SnowyState-ben lévő sávval. **3.** Az interaktált sáv DryState-be, a szomszéd sáv SnowyState-be kerül. |

### Use-case 21: Hókotró utat próbál takarítani kifogyott Dragon vagy SaltVomitter fejjel

| Use-case neve	| Hókotró utat próbál takarítani kifogyott Dragon vagy SaltVomitter fejjel |
|--- | ---|
| Rövid leírás		| Hókotró utat próbál takarítani kifogyott Dragon vagy SaltVomitter fejjel, sikeretelenül. |
| Aktorok		| Cleaner, OutdoorLane, SnowyState, SnowPlow, Dragon, SaltVomitter |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben, egy SnowPlow-t üres Dragon vagy SaltVomitter fejjel. **2.** A SnowPlow az aktív fejjel interaktál a SnowyState-ben lévő sávval. **3.** A sáv továbbra is SnowyState-ben marad. |

### Use-case 22: Jeges út takarítása IceBreaker fejjel

| Use-case neve	| Jeges út takarítása IceBreaker fejjel |
|--- | ---|
| Rövid leírás		| Hókotró utat takarít IceBreaker fejjel. |
| Aktorok		| Cleaner, OutdoorLane, IcyState, SnowyState, SnowPlow, IceBreaker |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t IcyState-ben és egy SnowPlow-t IceBreaker fejjel. **2.** A SnowPlow az aktív fejjel interaktál a IcyState-ben lévő sávval. **3.** A sáv SnowyState állapotba kerül. |

### Use-case 23: Hókotró hányófejjel takarít havas utat

| Use-case neve	| Hókotró hányófejjel takarít havas utat |
|--- | ---|
| Rövid leírás		| Hókotró hányófejjel takarít havas utat. |
| Aktorok		| Cleaner, OutdoorLane, DryState, SnowyState, SnowPlow, VomittingHead |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben és egy SnowPlow-t VomittingHead fejjel. **2.** A SnowPlow az aktív fejjel interaktál a SnowyState-ben lévő sávval. **3.** A sáv DryState-be kerül. |
