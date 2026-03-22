```mermaid
graph LR
    SK((:Skeleton))
    CL[<u>cl:Cleaner</u>]
    SP[<u>sp:SnowPlow</u>]
    DR[<u>dr:Dragon</u>]

    SK -- "1: << create >>" --> CL
    SK -- "2: << create >>" --> SP
    SK -- "3: << create >>" --> DR

    SK -- "4: setPlow(sp)" --> CL
    SK -- "5: addAttachment(dr)" --> SP
    SK -- "6: changeAttachment(dr)" --> SP
```
