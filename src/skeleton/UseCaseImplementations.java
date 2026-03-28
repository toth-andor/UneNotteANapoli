package skeleton;

import Vehicle.Bus;
import attachments.IceBreaker;
import Vehicle.Cleaner;
import Vehicle.SnowPlow;
import attachments.Sweeper;
import map.Junction;
import map.OutdoorLane;
import map.Road;
import states.DryState;
import states.LaneState;
import states.SnowyState;

public class UseCaseImplementations {

    // Use Cases

    public static void UC1() {
        System.out.println("[Teszt: Havazás]\n");

        // Init
        Junction junction1 = new Junction(); Skeleton.pushEntity("junction1", junction1);
        Junction junction2 = new Junction(); Skeleton.pushEntity("junction2", junction2);

        Road road = new Road(junction1, junction2); Skeleton.pushEntity("road", road);

        OutdoorLane lane = new OutdoorLane(new DryState()); Skeleton.pushEntity("lane", lane);

        Skeleton.pushEntity("dry" ,lane.getCurrentState());

        road.addLane(lane);

        // Call trigger
        road.snowFall(10);
    }

    public static void UC2() {
        System.out.println("[Teszt: Hatásos általános takarítás: Hóeltakarítás söpréssel]\n");

        // Init
        Cleaner cleaner = new Cleaner(0); Skeleton.pushEntity("cleaner", cleaner);
        Sweeper sweeper = new Sweeper(); Skeleton.pushEntity("sweeper", sweeper);
        SnowPlow snowplow = new SnowPlow(cleaner, null); Skeleton.pushEntity("snowplow", snowplow);
        snowplow.getOwnedTools().add(sweeper);
        snowplow.changeAttachment(sweeper);

        Junction junction1 = new Junction(); Skeleton.pushEntity("junction1", junction1);
        Junction junction2 = new Junction(); Skeleton.pushEntity("junction2", junction2);
        Road road = new Road(junction1, junction2); Skeleton.pushEntity("road", road);

        OutdoorLane lane = new OutdoorLane(new SnowyState()); Skeleton.pushEntity("lane", lane);
        Skeleton.pushEntity("snowy", lane.getCurrentState());
        road.addLane(lane);

        // Call trigger
        snowplow.interactWithLane(lane, 0);
    }

    public static void UC3() {
        // TODO implement UC3
        System.out.println("Running UC3!");

        System.out.println("[Teszt: Hóeltakarítás és egyenleg változása]\n");

        // Init
        OutdoorLane lane = new OutdoorLane(new SnowyState()); Skeleton.pushEntity("lane", lane);

        Skeleton.pushEntity("snowy" ,lane.getCurrentState());
    }

    public static void UC4() {
        System.out.println("[Teszt: Busz megfordulása és pontszerzés]\n");

        // Init
        Junction junction1 = new Junction(); Skeleton.pushEntity("junction1", junction1);
        Junction junction2 = new Junction(); Skeleton.pushEntity("junction2", junction2);
        Road road1 = new Road(junction1, junction2); Skeleton.pushEntity("road1", road1);

        Junction junction3 = new Junction(); Skeleton.pushEntity("junction3", junction3);
        Junction junction4 = new Junction(); Skeleton.pushEntity("junction4", junction4);
        Road road2 = new Road(junction3, junction4); Skeleton.pushEntity("road2", road2);

        OutdoorLane lane = new OutdoorLane(new DryState()); Skeleton.pushEntity("lane", lane);
        road1.addLane(lane);

        Bus bus = new Bus(road1, road2, lane, 0); Skeleton.pushEntity("bus", bus);

        // Call trigger
        bus.turnAround();
    }

    public static void UC5() {
        // TODO implement UC5

        System.out.println("[Teszt: Fej vásárlása és egyenlegleg csökkenése]\n");

        // Init
        Cleaner cleaner = new Cleaner(0); Skeleton.pushEntity("cleaner", cleaner);
        SnowPlow snowplow = new SnowPlow(cleaner, null); Skeleton.pushEntity("snowplow", snowplow);

        // Kérdés a felhasználóhoz: egyenleg
        int availableBalance = Integer.parseInt(Skeleton.CallChainLogger.askQuestion("Mennyi a játékos egyenlege?", null));

        // Egyenleg beállítása
        cleaner.setScore(availableBalance);

        // TODO
        // Itt a fej árát bele kéne hard code-olni a fejek konstruktorába ahelyett, hogy paraméterként kapja
        snowplow.buyAttachment(new Sweeper());

    }

    public static void UC6() {
        System.out.println("[Teszt: Hatástalan takarítás: Hóeltakarítás jégtörővel]\n");

        // Init
        Cleaner cleaner = new Cleaner(0); Skeleton.pushEntity("cleaner", cleaner);
        IceBreaker iceBreaker = new IceBreaker(); Skeleton.pushEntity("ib", iceBreaker);
        SnowPlow snowplow = new SnowPlow(cleaner, null); Skeleton.pushEntity("p", snowplow);
        snowplow.getOwnedTools().add(iceBreaker);
        snowplow.changeAttachment(iceBreaker);

        Junction junction1 = new Junction(); Skeleton.pushEntity("junction1", junction1);
        Junction junction2 = new Junction(); Skeleton.pushEntity("junction2", junction2);
        Road road = new Road(junction1, junction2); Skeleton.pushEntity("road", road);

        OutdoorLane lane = new OutdoorLane(new SnowyState()); Skeleton.pushEntity("lane", lane);
        Skeleton.pushEntity("state", lane.getCurrentState());
        road.addLane(lane);

        // Call trigger
        snowplow.interactWithLane(lane, 0);
    }

    public static void UC7() {
        // TODO implement UC7
        System.out.println("Running UC7!");
    }

    public static void UC8() {
        // TODO implement UC8
        System.out.println("Running UC8!");
    }

    public static void UC9() {
        // TODO implement UC9
        System.out.println("Running UC9!");
    }

    public static void UC10() {
        // TODO implement UC10
        System.out.println("Running UC10!");
    }

    public static void UC11() {
        // TODO implement UC11
        System.out.println("Running UC11!");
    }

    public static void UC12() {
        // TODO implement UC12
        System.out.println("Running UC12!");
    }

    public static void UC13() {
        // TODO implement UC13
        System.out.println("Running UC13!");
    }

    public static void UC14() {
        // TODO implement UC14
        System.out.println("Running UC14!");
    }

    public static void UC15() {
        // TODO implement UC15
        System.out.println("Running UC15!");
    }

    public static void UC16() {
        // TODO implement UC16
        System.out.println("Running UC16!");
    }

}
