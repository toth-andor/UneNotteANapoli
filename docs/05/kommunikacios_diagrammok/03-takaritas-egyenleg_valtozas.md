```mermaid
graph LR
    SK((:Skeleton))
    CL[<u>cl:Cleaner</u>]
    SP[<u>sp:SnowPlow</u>]
    SW[<u>sw:Sweeper</u>]
    OL[<u>lane:OutdoorLane</u>]
    SN[<u>snowy:SnowyState</u>]

    SK -- "1: << create >>" --> CL
    SK -- "2: << create >>" --> SP
    SK -- "3: << create >>" --> SW
    SK -- "4: << create >>" --> OL
    SK -- "5: << create >>" --> SN

    SK -- "6: setPlow(sp)" --> CL
    SK -- "7: addAttachment(sw)" --> SP
    SK -- "8: changeAttachment(sw)" --> SP
    SK -- "9: setState(snowy)" --> OL
```
