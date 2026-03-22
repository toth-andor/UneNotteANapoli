```mermaid
graph LR
    SK((:Skeleton))
    OL[<u>lane:OutdoorLane</u>]
    IS[<u>icy:IcyState</u>]
    C1[<u>car1:Car</u>]
    C2[<u>car2:Car</u>]

    SK -- "1: << create >>" --> OL
    SK -- "2: << create >>" --> IS
    SK -- "3: << create >>" --> C1
    SK -- "4: << create >>" --> C2

    SK -- "5: setState(icy)" --> OL
    SK -- "6: addVehicle(car1)" --> OL
    SK -- "7: addVehicle(car2)" --> OL
```
