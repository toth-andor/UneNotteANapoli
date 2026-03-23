```mermaid
graph LR
    SK((:Skeleton))
    CL[<u>cl:Cleaner</u>]
    SP[<u>sp:SnowPlow</u>]
    SV[<u>sv:SaltVomitter</u>]
    OL[<u>lane:OutdoorLane</u>]
    DS[<u>dry:DryState</u>]

    SK -- "1: << create >>" --> CL
    SK -- "2: << create >>" --> SP
    SK -- "3: << create >>" --> SV
    SK -- "4: << create >>" --> OL
    SK -- "5: << create >>" --> DS

    SK -- "6: setPlow(sp)" --> CL
    SK -- "7: addAttachment(sv)" --> SP
    SK -- "8: changeAttachment(sv)" --> SP
    SK -- "9: setState(dry)" --> OL
```
