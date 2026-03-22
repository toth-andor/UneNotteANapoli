```mermaid
graph LR
    C((:Cleaner))
    SP[<u>:SnowPlow</u>]
    SV[<u>:SaltVomitter</u>]
    OL[<u>:OutdoorLane</u>]
    DS[<u>:DryState</u>]
    SS[<u>:SaltedState</u>]

    C -- "1: sóz()" --> SP
    SP -- "1.1: használ()" --> SV
    SP -- "1.2: interaktál()" --> OL
    OL -. "1.2.1: <<megszüntet>>" .-> DS
    OL -- "1.2.2: <<létrehoz>>" --> SS
    OL -- "1.2.3: setState(SaltedState)" --> OL
```
