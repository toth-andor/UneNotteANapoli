## A szkeleton modell valóságos use-case-ei

### Use-case 1: Havazás és hóeltakarítás söpréssel

xaélskdjf 

| Use-case neve	| Havazás és hóeltakarítás söpréssel |
|--- | ---|
| Rövid leírás		| Egy OutdoorLane-en havazik, majd egy hókotró söprőfejjel letakarítja a havat. |
| Aktorok		| OutdoorLane, DryState, SnowyState, SnowPlow, Sweeper |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Havazik a sávon, állapota SnowyState-re változik. **3.** A SnowPlow söprőfejjel letakarítja a havat, a sáv állapota DryState-re változik. |

### Use-case 2: Jeges úton történő baleset

| Use-case neve	| Jeges úton történő baleset |
|--- | ---|
| Rövid leírás		| Két autó ütközik egy jeges úton, a sáv állapota CrashedState-re változik. |
| Aktorok		| OutdoorLane, IcyState, CrashedState, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t IcyState-ben. **2.** Két Car ütközik a sávon, a sáv állapota CrashedState-re változik. |


### Use-case 3: Hóeltakarítás sószóróval

| Use-case neve	| Hóeltakarítás sószóróval |
|--- | ---|
| Rövid leírás		| Egy OutdoorLane-en havazik, majd egy hókotró sószóró fejjel letakarítja a havat. |
| Aktorok		| OutdoorLane, DryState, SnowyState, SnowPlow, SaltVomitter |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Havazik a sávon, állapota SnowyState-re változik. **3.** A SnowPlow sószóró fejjel letakarítja a havat, a sáv állapota SaltedState-re változik. |

### Use-case 4: Busz megfordulása és pontszerzés

| Use-case neve	| Busz megfordulása és pontszerzés |
|--- | ---|
| Rövid leírás		| Egy busz megfordul, és pontot szerez. |
| Aktorok		| Bus |
| Forgatókönyv | **1.** Létrehozunk egy Bus-t úgy hogy a `currentLane` megegyezzen az egyik végállomással. **2.** A busz megfordul, és pontot szerez. |

### Use-case 5: Hókotró fej vásárlása

| Use-case neve	| Hókotró fej vásárlása |
|--- | ---|
| Rövid leírás		| Egy hókotró új fejet vásárol, és az egyenlege csökken. |
| Aktorok		| SnowPlow, Sweeper |
| Forgatókönyv | **1.** Létrehozunk egy SnowPlow-t. **2.** A SnowPlow vásárol egy Sweeper fejet, és az egyenlege csökken. |

### Use-case 6: Hóeltakarítás jégtörővel

| Use-case neve	| Hóeltakarítás jégtörővel |
|--- | ---|
| Rövid leírás		| Egy OutdoorLane-en havazik, majd egy hókotró jégtörő fejjel letakarítja a havat. |
| Aktorok		| OutdoorLane, DryState, SnowyState, SnowPlow, IceBreaker |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Havazik a sávon, állapota SnowyState-re változik. **3.** A SnowPlow jégtörő fejjel letakarítja a havat, a sáv állapota változatlanul SnoweyState marad |

### Use-case 7: Két autó ütközik jeges úton

| Use-case neve	| Autó interakció jeges úton |
|--- | ---|
| Rövid leírás		| Két autó ütközik egy jeges úton. |
| Aktorok		| OutdoorLane, IcyState, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t IcyState-ben. **2.** Létrehozunk 2 Car-t **3.** A két Car-t egymás után rátesszük az útra. **4.** A két autó egymásnak ütközik. |

### Use-case 8: Hókotró fej újratöltése

| Use-case neve	| Hókotró fej újratöltése |
|--- | ---|
| Rövid leírás		| Egy hókotró újratölti a fejét, és az egyenlege csökken. |
| Aktorok		| Cleaner, Dragon/SaltVomitter, SnowPlow, Sweeper |
| Forgatókönyv | **1.** Létrehozunk egy Cleaner-t és hozzá egy SnowPlow-t. **2.** A SnowPlow-t felszereljük egy Dragon/SaltVomitter fejjel. **3.** A SnowPlow újratölti a Dragon/SaltVomitter fejet, és a Cleaner egyenlege csökken. |

### Use-case 9: Busz/Autó interakció hóval borított úton

| Use-case neve	| Busz/Autó interakció hóval borított úton |
|--- | ---|
| Rövid leírás		| Egy busz/autó interaktál egy hóval borított úton, és letapossa a havat. |
| Aktorok		| OutdoorLane, SnowyState, IcyState, Bus, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnowyState-ben és egy buszt/autót. **2.** a busz/autó interaktál a sávval ahányszor a jéggé taposáshoz kell, és letapossa a havat **3.** A sáv IcyState-be kerül |

### Use-case 10: Hókotró fej csere

| Use-case neve	| Hókotró fej csere |
|--- | ---|
| Rövid leírás		| Egy hókotró cseréli a fejét. |
| Aktorok		| SnowPlow, Sweeper, IceBreaker |
| Forgatókönyv | **1.** Létrehozunk egy SnowPlow-t úgy hogy birtokoljon Sweeper és IceBreaker fejet. **2.** A SnowPlow cseréli a Sweeper fejét IceBreaker fejre. |

### Use-case 11: Hóeltakarítás sárkányfejjel

| Use-case neve	| Hóeltakarítás sárkányfejjel |
|--- | ---|
| Rövid leírás		| Egy OutdoorLane-en havazik, majd egy hókotró sárkányfejjel letakarítja a havat. |
| Aktorok		| OutdoorLane, DryState, SnowyState, SnowPlow, Dragon |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Havazik a sávon, állapota SnowyState-re változik. **3.** A SnowPlow sárkányfejjel letakarítja a havat, a sáv állapota DryState-re változik. |

### Use-case 12: Hóeltakarítás hányófejjel

| Use-case neve	| Hóeltakarítás hányófejjel |
|--- | ---|
| Rövid leírás		| Egy OutdoorLane-en havazik, majd egy hókotró hányófejjel letakarítja a havat. |
| Aktorok		| OutdoorLane, DryState, SnowyState, SnowPlow, VomitingHead |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Havazik a sávon, állapota SnowyState-re változik. **3.** A SnowPlow hányófejjel letakarítja a havat, a sáv állapota DryState-re változik. |

### Use-case 13: Autó interakció száraz úton

| Use-case neve	| Autó interakció száraz úton |
|--- | ---|
| Rövid leírás		| Egy autó interaktál egy száraz úton, és nem csúszik meg. |
| Aktorok		| OutdoorLane, DryState, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Egy Car interaktál a sávon, és nem csúszik meg. |


### Use-case 14: Hókotró interakció sószóró fejjel

| Use-case neve	| Hókotró interakció sószóró fejjel |
|--- | ---|
| Rövid leírás		| Egy hókotró sószóró fejjel interaktál egy sávon. |
| Aktorok		| OutdoorLane, DryState, SnowPlow, SaltVomitter |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** A SnowPlow sószóró fejjel interaktál a sávon. **3.** A sáv SaltedState-be kerül. |

### Use-case 15: Hóeltakarítás sószóró fejjel

| Use-case neve	| Hóeltakarítás sószóró fejjel |
|--- | ---|
| Rövid leírás		| Egy OutdoorLane-en havazik, majd egy hókotró sószóró fejjel letakarítja a havat. |
| Aktorok		| OutdoorLane, DryState, SnowyState, SnowPlow, SaltVomitter |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Havazik a sávon, állapota SnowyState-re változik. **3.** A SnowPlow sószóró fejjel letakarítja a havat, a sáv állapota SaltedState-re változik. |

### Use-case 16: Hóeltakarítás hányófejjel

| Use-case neve	| Hóeltakarítás hányófejjel |
|--- | ---|
| Rövid leírás		| Egy OutdoorLane-en havazik, majd egy hókotró hányófejjel letakarítja a havat. |
| Aktorok		| OutdoorLane, DryState, SnowyState, SnowPlow, VomitingHead |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t DryState-ben. **2.** Havazik a sávon, állapota SnowyState-re változik. **3.** A SnowPlow hányófejjel letakarítja a havat, a sáv állapota DryState-re változik. |

### Use-case 17: Autó interakció sószórt úton

| Use-case neve	| Autó interakció sószórt úton |
|--- | ---|
| Rövid leírás		| Egy autó interaktál egy sószórt úton, és nem csúszik meg. |
| Aktorok		| OutdoorLane, SaltedState, Car |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SaltedState-ben. **2.** Egy Car interaktál a sávon, és nem csúszik meg. |

### Use-case 18: Busz interakció sószórt úton

| Use-case neve	| Busz interakció sószórt úton |
|--- | ---|
| Rövid leírás		| Egy busz interaktál egy sószórt úton, és nem csúszik meg. |
| Aktorok		| OutdoorLane, SaltedState, Bus |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SaltedState-ben. **2.** Egy Bus interaktál a sávon, és nem csúszik meg. |

### Use-case 19: Hókotró söprőfejjel takarít havas utat

| Use-case neve	| Hókotró interakció hányófejjel |
|--- | ---|
| Rövid leírás		| Hókotró söprőfejjel takarít havas utat. |
| Aktorok		| OutdoorLane, DryState, SnoweyState, SnowPlow, Sweeper, Road |
| Forgatókönyv | **1.** Létrehozunk két OutdoorLane-t  egyiket SnoweyState-ben a másikat DryState és hozzáadjuk őket egy Road-hoz. **2.** A SnowPlow Sweeper fejjel interaktál a SnowyState-ben lévő sávval. **3.** Az interaktált sáv DryState-be, a szomszéd sáv SnoweyState-be kerül |

### Use-case 19: Hókotró utat próbál takarítani kifogyott Dragon vagy SaltVomitter fejjel

| Use-case neve	| Hókotró interakció hányófejjel |
|--- | ---|
| Rövid leírás		| Hókotró utat próbál takarítani kifogyott Dragon vagy SaltVomitter fejjel, sikeretelenül |
| Aktorok		| OutdoorLane, SnoweyState, SnowPlow, Dragon, SaltVomitter |
| Forgatókönyv | **1.** Létrehozunk egy OutdoorLane-t SnoweyState-ben, egy SnowPlow-t üres Dragon vagy SaltVomitter fejjel **2.** A SnowPlow az aktív fejjel interaktál a SnowyState-ben lévő sávval. **3.** A sáv továbbra is SnoweyState-ben marad |
