```mermaid
graph LR
    V((:Car))
    OL[<u>:OutdoorLane</u>]
    DS[<u>:DryState</u>]

    V -- "1: interaktál()" --> OL
    OL -- "1.1: állapot_lekérdez()" --> DS
    OL -- "1.2: nem_csúszik()" --> V
```
