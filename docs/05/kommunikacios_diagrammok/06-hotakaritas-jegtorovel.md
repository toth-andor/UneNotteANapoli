```mermaid
graph LR
    SK((:Skeleton))
    CL[<u>cl:Cleaner</u>]
    SP[<u>sp:SnowPlow</u>]
    IB[<u>ib:IceBreaker</u>]
    OL[<u>lane:OutdoorLane</u>]
    SN[<u>snowy:SnowyState</u>]

    SK -- "1: << create >>" --> CL
    SK -- "2: << create >>" --> SP
    SK -- "3: << create >>" --> IB
    SK -- "4: << create >>" --> OL
    SK -- "5: << create >>" --> SN

    SK -- "6: setPlow(sp)" --> CL
    SK -- "7: addAttachment(ib)" --> SP
    SK -- "8: changeAttachment(ib)" --> SP
    SK -- "9: setState(snowy)" --> OL
```
