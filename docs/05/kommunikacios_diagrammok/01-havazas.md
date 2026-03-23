```mermaid 
graph LR
    SK((:Skeleton))
    OL[<u>lane:OutdoorLane</u>]
    DS[<u>dry:DryState</u>]

    SK -- "1: << create >>" --> OL
    SK -- "2: pushEntity(lane, 'lane'), pushEntity(dry, 'dry')" --> SK
    SK -- "3: << create >>" --> DS
    SK -- "5: setState(dry)" --> OL
```
