```mermaid
graph LR
    SK((:Skeleton))
    CL[<u>cl:Cleaner</u>]
    SP[<u>sp:SnowPlow</u>]

    SK -- "1: << create >>" --> CL
    SK -- "2: << create >>" --> SP
    SK -- "3: pushEntity(cl, 'cl'), 4: pushEntity(sp, 'sp')" --> SK
    
    SK -- "5: setPlow(sp)" --> CL
```
