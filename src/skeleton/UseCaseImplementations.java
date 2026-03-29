package skeleton;

import Vehicle.Car;
import Vehicle.Cleaner;
import Vehicle.SnowPlow;
import attachments.Dragon;
import attachments.IceBreaker;
import attachments.SaltVommiter;
import attachments.IceBreaker;
import attachments.Sweeper;
import map.Junction;
import map.OutdoorLane;
import map.Road;
import skeleton.Skeleton.CallChainLogger;
import states.DryState;
import states.IcyState;
import states.SaltedState;
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
        System.out.println("[Teszt: Takarítás és ennek hatására egyenleg változása]\n");

        // 1. Létrehozunk egy OutdoorLane-t SnowyState-ben
        Junction j1 = new Junction(); Skeleton.pushEntity("junction1", j1);
        Junction j2 = new Junction(); Skeleton.pushEntity("junction2", j2);
        Road road = new Road(j1, j2); Skeleton.pushEntity("road", road);

        OutdoorLane lane = new OutdoorLane(new SnowyState());
        Skeleton.pushEntity("lane", lane);
        Skeleton.pushEntity("snowy", lane.getCurrentState());
        road.addLane(lane);

        Cleaner cleaner = new Cleaner(100);
        Skeleton.pushEntity("cleaner", cleaner);
        Skeleton.pushEntity("cleanerBalance", cleaner.getScore());

        SnowPlow snowplow = new SnowPlow(cleaner, null);
        Skeleton.pushEntity("snowplow", snowplow);

        Sweeper sweeper = new Sweeper();
        Skeleton.pushEntity("sweeper", sweeper);
        snowplow.buyAttachment(sweeper);
        snowplow.changeAttachment(sweeper);

        // 2. A Cleaner letakarítja az utat — egyenlege növekszik
        snowplow.interactWithLane(lane, 0);
        // Az egynleg nem növekedik, mert az AddIncome() metódus még nincs implementálva a snowplowban.
        Skeleton.pushEntity("cleanerBalance", cleaner.getScore());
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
        SnowPlow snowplow = new SnowPlow(cleaner, null);
        Skeleton.pushEntity("snowplow", snowplow);
        IceBreaker iceBreaker = new IceBreaker();
        Skeleton.pushEntity("icebreaker", iceBreaker);
        snowplow.buyAttachment(iceBreaker);
        snowplow.changeAttachment(iceBreaker);
    }

    public static void UC11() {
        System.out.println("Running UC11!");

        Junction sourceJunction = new Junction();
        Skeleton.pushEntity("sourceJunction", sourceJunction);
        Junction destinationJunction = new Junction();
        Skeleton.pushEntity("targetJunction", destinationJunction);

        DryState dryState = new DryState();
        Skeleton.pushEntity("dryState", dryState);

        OutdoorLane outdoorLane = new OutdoorLane(dryState);
        Skeleton.pushEntity("outdoorLane", outdoorLane);
        Road road1 = new Road(sourceJunction, destinationJunction);
        Skeleton.pushEntity("road1", road1);
        Road road2 = new Road(destinationJunction, sourceJunction);
        Skeleton.pushEntity("road2", road2);

        Car car = new Car(road1, road2, outdoorLane);
        Skeleton.pushEntity("car", car);
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

        Cleaner cleaner = new Cleaner(100);
        Skeleton.pushEntity("cleaner", cleaner);

        attachments.SaltVommiter saltVommiter = new attachments.SaltVommiter();
        Skeleton.pushEntity("saltVommiter", saltVommiter);

        SnowPlow snowplow = new SnowPlow(cleaner, outdoorLane);
        Skeleton.pushEntity("snowplow", snowplow);

        snowplow.buyAttachment(saltVommiter);
        snowplow.changeAttachment(saltVommiter);
        snowplow.refillAttachment();
        snowplow.interactWithLane(outdoorLane, 0);
    }

    public static void UC13() {
        System.out.println("[Teszt: Autó interakció sószórt úton]\n");

        // 1. Létrehozunk egy OutdoorLane-t SaltedState-ben
        OutdoorLane lane = new OutdoorLane(new SaltedState(100));
        Skeleton.pushEntity("lane", lane);
        Skeleton.pushEntity("salted", lane.getCurrentState());

        Car car = new Car(null, null, null);
        Skeleton.pushEntity("car", car);

        // 2. A Car interaktál a sávval — SaltedState.handleTraffic() nem csúsztatja meg
        car.gotoLane(lane, 0);
        
    }

    public static void UC14() {
        System.out.println("[Teszt: Söprőfej hatása szomszédos sávok esetén: Hókotró söprőfejjel takarít havas utat]\n");

        // 1. Két OutdoorLane létrehozása, Road-hoz adva (azonos irány = azonos source/destination)
        //    dryLane → index 0, snowyLane → index 1 (sweeper a magasabb indexű sávról a kisebbre tolja a havat)
        Junction j1 = new Junction(); Skeleton.pushEntity("junction1", j1);
        Junction j2 = new Junction(); Skeleton.pushEntity("junction2", j2);

        Road road = new Road(j1, j2); Skeleton.pushEntity("road", road);

        OutdoorLane dryLane = new OutdoorLane(j1, j2);
        Skeleton.pushEntity("dryLane", dryLane);
        Skeleton.pushEntity("dry", dryLane.getCurrentState());

        OutdoorLane snowyLane = new OutdoorLane(j1, j2);
        Skeleton.pushEntity("snowyLane", snowyLane);
        Skeleton.pushEntity("snowyLaneDry", snowyLane.getCurrentState());

        road.addLane(dryLane);
        road.addLane(snowyLane);

        // snowFall(10) → snowAmount=10 >= 5 küszöb → snowyLane SnowyState-be kerül
        snowyLane.snowFall(10);
        Skeleton.pushEntity("snowy", snowyLane.getCurrentState());

        Cleaner cleaner = new Cleaner(0); Skeleton.pushEntity("cleaner", cleaner);
        cleaner.setScore(100);

        SnowPlow snowplow = new SnowPlow(cleaner, null); Skeleton.pushEntity("snowplow", snowplow);

        Sweeper sweeper = new Sweeper(); Skeleton.pushEntity("sweeper", sweeper);
        snowplow.buyAttachment(sweeper);
        snowplow.changeAttachment(sweeper);

        // 2. A SnowPlow Sweeper fejjel interaktál a SnowyState-ben lévő sávval
        snowplow.interactWithLane(snowyLane, 0);

        // 3. snowyLane → DryState, dryLane → SnowyState (a hó átkerült)
        Skeleton.pushEntity("snowyLaneDryState", snowyLane.getCurrentState());
        Skeleton.pushEntity("dryLaneSnowyState", dryLane.getCurrentState());
    }

    public static void UC15() {
        System.out.println("[Teszt: Hókotró utat próbál takarítani kifogyott Sárkány vagy Sószóró fejjel]\n");

        // 1. Létrehozunk egy OutdoorLane-t SnowyState-ben és egy SnowPlow-t üres fejjel
        OutdoorLane lane = new OutdoorLane(new SnowyState());
        Skeleton.pushEntity("lane", lane);
        Skeleton.pushEntity("snowy", lane.getCurrentState());

        Cleaner cleaner = new Cleaner(0);
        Skeleton.pushEntity("cleaner", cleaner);
        cleaner.setScore(100);

        SnowPlow snowplow = new SnowPlow(cleaner, null);
        Skeleton.pushEntity("snowplow", snowplow);

        String choice = Skeleton.CallChainLogger.askQuestion("Melyik fejet használjuk?", "dragon/salt");

        if (choice.equalsIgnoreCase("dragon")) {
            Dragon dragon = new Dragon();
            Skeleton.pushEntity("dragon", dragon);
            snowplow.buyAttachment(dragon);
            snowplow.changeAttachment(dragon);
        } else {
            SaltVommiter saltVommiter = new SaltVommiter();
            Skeleton.pushEntity("saltVommiter", saltVommiter);
            snowplow.buyAttachment(saltVommiter);
            snowplow.changeAttachment(saltVommiter);
        }

        // 2. A SnowPlow az aktív (üres) fejjel próbál interaktálni a sávval — takarítás nem történik
        snowplow.interactWithLane(lane, 0);

        // 3. A sáv továbbra is SnowyState-ben marad
         Skeleton.pushEntity("snowy", lane.getCurrentState());
    }

    public static void UC16() {
        System.out.println("[Teszt: Jeges út takarítása IceBreaker fejjel]\n");

        // 1. Létrehozunk egy OutdoorLane-t IcyState-ben és egy SnowPlow-t IceBreaker fejjel
        OutdoorLane lane = new OutdoorLane(new IcyState());
        Skeleton.pushEntity("lane", lane);
        Skeleton.pushEntity("icy", lane.getCurrentState());

        Cleaner cleaner = new Cleaner(0);
        Skeleton.pushEntity("cleaner", cleaner);
        cleaner.setScore(100);

        SnowPlow snowplow = new SnowPlow(cleaner, null);
        Skeleton.pushEntity("snowplow", snowplow);

        IceBreaker iceBreaker = new IceBreaker();
        Skeleton.pushEntity("iceBreaker", iceBreaker);

        snowplow.buyAttachment(iceBreaker);
        snowplow.changeAttachment(iceBreaker);

        // 2. A SnowPlow az aktív fejjel interaktál az IcyState-ben lévő sávval
        snowplow.interactWithLane(lane, 0);

        // 3. A sáv SnowyState állapotba kerül
        Skeleton.pushEntity("snowy", lane.getCurrentState());
    }

}
