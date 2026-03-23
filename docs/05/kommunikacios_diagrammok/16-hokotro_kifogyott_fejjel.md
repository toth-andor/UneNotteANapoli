```mermaid
graph LR
    SK((:Skeleton))
    CL[<u>cl:Cleaner</u>]
    SP[<u>sp:SnowPlow</u>]
    DR[<u>dr:Dragon</u>]
    OL[<u>lane:OutdoorLane</u>]
    SN[<u>snowy:SnowyState</u>]

    SK -- "1: << create >>" --> CL
    SK -- "2: << create >>" --> SP
    SK -- "3: << create >>" --> DR
    SK -- "4: << create >>" --> OL
    SK -- "5: << create >>" --> SN

    SK -- "6: pushEntity(cl,'cl'), 7: pushEntity(sp,'sp'), 8: pushEntity(dr,'dr'), 9: pushEntity(lane,'lane'), 10: pushEntity(snowy,'snowy')" --> SK

    SK -- "11: setPlow(sp)" --> CL
    SK -- "12: addAttachment(dr)" --> SP
    SK -- "13: changeAttachment(dr)" --> SP
    SK -- "14: setState(snowy)" --> OL
```
