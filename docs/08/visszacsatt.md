# Visszajelzések
## Tesztelési nyelv
- Determinisztikusság - jó hogy van!, Űgyik vagyunk
- Idő legyen abstrakt, nálunk alapból az
- Snapshotban legyen minden infó benne, egymásba csúszott autók pl.
## Teszt tervek
- Inkébb a spec oldaláról közelítsük meg a teszteket, mint a parancsot oldaláról.
  - Sok vásárlós teszt, kevés mozdóg teszt - Nemtom ez nálunk milyen


# Kövi hétre - Részletes tervek
- Cntrollert elkezdeni elkészíteni
  - hogy jönnek egymás után a játékosok

- Elkezdeni kitalálni, hogy a controller és a modell között pontosan, hogyan áramlik az információ
  - Ez vonatokzik a majdani view layer-re is

- Eddigi osztálydiagrammot minél részletesebben le kell írni
  - Legrövidebb utat pontosan milyen módszerrel keresünk legrövidebb utat
  - Egy több kapcsolatok pontosan milyen java típusúak

- Fel kell venni a controller osztályait, ki irányítja a köröket, pénz vagy pont számítása, vagy ami kell

- Ha szükségünk van véletlen értékre, akkor arra vegyünk fel egy interfészt.
  - A modellben ettől függjünk, a controller pedig majd eldönti milyen implementációt rak mögé
  
- Proto egy külön modul ami a model interfészeit használja. (solidban, most jön az I és a D)
- A view felé lévő interfészeket is meg kell, nézni, hogy a proto ki tudja írni a dolgokat
- Nálunk ezek nem külön interfészek, nemtom ez probléma-e
  - De mégis, Kell külön Controller Interfész megy View Interfész, az egyik megmont, a másik kérdez.
- Bonyolult kódhoz írunk pszeudó-kódot, amúgy elég annyit mondani, hogy BFS vagy Dijstral

- Tesztek megvalósítása a mi teszt nyelvünkön.
- Azt mondja dokumentáljuk ha változtatunk a teszt nyelven (megint doksi csere lesz)


- Tesztelést támogató programok terve
  - Teszt orákulum
  
  
- Legyen funkció az összes teszt futtatására
- Legyen egy-egy specifikus teszt futtatására lehetőség
- Játszahót legyen a játék a parancsnyelven.




- Objektumok részletes tervei
- tesztek részletes tervei
- tesztet támogató rész
