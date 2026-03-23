```mermaid
graph LR
    SK((:Skeleton))
    CL[<u>cl:Cleaner</u>]
    SP[<u>sp:SnowPlow</u>]
    SW[<u>sw:Sweeper</u>]
    OL[<u>lane:OutdoorLane</u>]
    SN[<u>snowy:SnowyState</u>]

    SK -- "1: << create >>" --> CL
    SK -- "2: pushEntity(cl, 'cl')" --> SK
    SK -- "3: << create >>" --> SP
    SK -- "4: pushEntity(sp, 'sp')" --> SK
    SK -- "5: << create >>" --> SW
    SK -- "6: pushEntity(sw, 'sw')" --> SK
    SK -- "7: << create >>" --> OL
    SK -- "8: pushEntity(lane, 'lane')" --> SK
    SK -- "9: << create >>" --> SN
    SK -- "10: pushEntity(snowy, 'snowy')" --> SK

    SK -- "11: setPlow(sp)" --> CL
    SK -- "12: addAttachment(sw)" --> SP
    SK -- "13: changeAttachment(sw)" --> SP
    SK -- "14: setState(snowy)" --> OL
```
