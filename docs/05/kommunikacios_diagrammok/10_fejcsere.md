```mermaid
graph LR
    SK((:Skeleton))
    SP[<u>sp:SnowPlow</u>]
    SW[<u>sw:Sweeper</u>]
    IB[<u>ib:IceBreaker</u>]

    SK -- "1: << create >>" --> SP
    SK -- "2: << create >>" --> SW
    SK -- "3: << create >>" --> IB
    SK -- "4: pushEntity(sp,'sp'), 5: pushEntity(sw,'sw'), 6: pushEntity(ib,'ib')" --> SK

    SK -- "7: addAttachment(sw)" --> SP
    SK -- "8: addAttachment(ib)" --> SP
    SK -- "9: changeAttachment(sw)" --> SP
```
