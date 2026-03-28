package skeleton;

import Vehicle.Car;
import Vehicle.Cleaner;
import Vehicle.SnowPlow;
import attachments.IceBreaker;
import attachments.Sweeper;
import map.Junction;
import map.OutdoorLane;
import map.Road;
import skeleton.Skeleton.CallChainLogger;
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
        // TODO implement UC2

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
        // TODO implement UC4
        System.out.println("Running UC4!");
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
        // TODO implement UC6
        System.out.println("Running UC6!");
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
        System.out.println("Running UC10!");
        Cleaner cleaner = new Cleaner(100);
        Skeleton.pushEntity("cleaner", cleaner);
        CallChainLogger.printCall(cleaner, "Cleaner(100)");
        CallChainLogger.printReturn(null);
        SnowPlow snowplow = new SnowPlow(cleaner, null);
        Skeleton.pushEntity("snowplow", snowplow);
        CallChainLogger.printCall(snowplow, "SnowPlow(" + Skeleton.getEntityByRef(cleaner) + ", )");
        CallChainLogger.printReturn(null);
        IceBreaker iceBreaker = new IceBreaker();
        Skeleton.pushEntity("icebreaker", iceBreaker);
        CallChainLogger.printCall(iceBreaker, "IceBreaker()");
        CallChainLogger.printReturn(null);
        snowplow.buyAttachment(iceBreaker);
        snowplow.changeAttachment(iceBreaker);
    }

    public static void UC11() {
        System.out.println("Running UC11!");

        Junction sourceJunction = new Junction();
        Skeleton.pushEntity("sourceJunction", sourceJunction);
        CallChainLogger.printCall(sourceJunction, "Junction()");
        CallChainLogger.printReturn(null);
        Junction destinationJunction = new Junction();
        Skeleton.pushEntity("targetJunction", destinationJunction);
        CallChainLogger.printCall(destinationJunction, "Junction()");
        CallChainLogger.printReturn(null);

        DryState dryState = new DryState();
        Skeleton.pushEntity("dryState", dryState);
        CallChainLogger.printCall(dryState, "DryState()");
        CallChainLogger.printReturn(null);

        OutdoorLane outdoorLane = new OutdoorLane(dryState);
        Skeleton.pushEntity("outdoorLane", outdoorLane);
        CallChainLogger.printCall(outdoorLane, "OutdoorLane(" + Skeleton.getEntityByRef(dryState) + ")");
        CallChainLogger.printReturn(null);
        Road road1 = new Road(sourceJunction, destinationJunction);
        Skeleton.pushEntity("road1", road1);
        CallChainLogger.printCall(road1, "Road(" +
            Skeleton.getEntityByRef(sourceJunction) +
            ", " +
            Skeleton.getEntityByRef(destinationJunction) +
            ")");
        CallChainLogger.printReturn(null);
        Road road2 = new Road(destinationJunction, sourceJunction);
        Skeleton.pushEntity("road2", road2);
        CallChainLogger.printCall(road2, "Road(" +
            Skeleton.getEntityByRef(destinationJunction) +
            ", " +
            Skeleton.getEntityByRef(sourceJunction) +
            ")");
        CallChainLogger.printReturn(null);

        Car car = new Car(road1, road2, outdoorLane);
        Skeleton.pushEntity("car", car);
        CallChainLogger.printCall(car, "Car(" +
            Skeleton.getEntityByRef(road1) +
            ", " +
            Skeleton.getEntityByRef(road2) +
            ", " +
            Skeleton.getEntityByRef(outdoorLane) +
            ")");
        CallChainLogger.printReturn(null);
        car.gotoLane(outdoorLane, 0);
    }

    public static void UC12() {
        System.out.println("Running UC12!");
        System.out.println("[Teszt: Hókotró sószórófejjel száraz úton]\n");

        DryState dryState = new DryState();
        Skeleton.pushEntity("dryState", dryState);
        CallChainLogger.printCall(dryState, "DryState()");
        CallChainLogger.printReturn(null);

        OutdoorLane outdoorLane = new OutdoorLane(dryState);
        Skeleton.pushEntity("outdoorLane", outdoorLane);
        CallChainLogger.printCall(outdoorLane, "OutdoorLane(" + Skeleton.getEntityByRef(dryState) + ")");
        CallChainLogger.printReturn(null);

        Cleaner cleaner = new Cleaner(100);
        Skeleton.pushEntity("cleaner", cleaner);
        CallChainLogger.printCall(cleaner, "Cleaner(100)");
        CallChainLogger.printReturn(null);

        attachments.SaltVommiter saltVommiter = new attachments.SaltVommiter();
        Skeleton.pushEntity("saltVommiter", saltVommiter);
        CallChainLogger.printCall(saltVommiter, "SaltVommiter()");
        CallChainLogger.printReturn(null);

        SnowPlow snowplow = new SnowPlow(cleaner, outdoorLane);
        Skeleton.pushEntity("snowplow", snowplow);
        CallChainLogger.printCall(snowplow, "SnowPlow(" + Skeleton.getEntityByRef(cleaner) + ", " + Skeleton.getEntityByRef(outdoorLane) + ")");
        CallChainLogger.printReturn(null);

        snowplow.buyAttachment(saltVommiter);
        snowplow.changeAttachment(saltVommiter);
        snowplow.refillAttachment();
        snowplow.interactWithLane(outdoorLane, 0);
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
