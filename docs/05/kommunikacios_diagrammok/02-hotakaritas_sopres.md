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
    SK -- "6: pushEntity(cl,'cl'), 7: pushEntity(sp,'sp'), 8: pushEntity(sw,'sw'), 9: pushEntity(lane,'lane'), 10: pushEntity(snowy,'snowy')" --> SK

    SK -- "11: setPlow(sp)" --> CL
    SK -- "12: addAttachment(sw)" --> SP
    SK -- "13: changeAttachment(sw)" --> SP
    SK -- "14: setState(snowy)" --> OL
```
