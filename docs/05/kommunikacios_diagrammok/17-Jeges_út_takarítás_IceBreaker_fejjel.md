```mermaid
graph LR
    C((:Cleaner))
    SP[<u>:SnowPlow</u>]
    IB[<u>:IceBreaker</u>]
    OL[<u>:OutdoorLane</u>]
    IS[<u>:IcyState</u>]
    SN[<u>:SnowyState</u>]

    C -- "1: takarít()" --> SP
    SP -- "1.1: használ()" --> IB
    SP -- "1.2: jeget_tör()" --> OL
    OL -. "1.2.1: <<megszüntet>>" .-> IS
    OL -- "1.2.2: <<létrehoz>>" --> SN
    OL -- "1.2.3: setState(SnowyState)" --> OL
```
