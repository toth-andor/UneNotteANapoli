package skeleton;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import map.*;
import Vehicle.*;
import states.DryState;
import states.LaneState;
import states.SnowyState;
import attachments.*;

public class Skeleton {

    /** Loggolás engedélyezése a modell osztályaiban **/
    public static final boolean ENABLE_LOGGING = true;

    /** Debug ágak engedélyezése **/
    private static final boolean DEBUG = true;

    /** Scanner a use input kezeléséhez **/
    private static final Scanner scanner = new Scanner(System.in);

    /** HashMap, ami a teszteseteket tárolja **/
    private static final Map<Integer, TestCase> testCases = new HashMap<>();

    /** A létrehozott objektumokat tároló HashMap-ek
     activeObjects: név - objektum azonosító
     objectToName: objektum azonosító - név **/
    private static final Map<String, Object> activeObjects = new HashMap<>();
    private static final Map<Object, String> objectToName = new HashMap<>();

    /** Objektumok regisztrációja a HashMap-ekbe **/
    public static void pushEntity(String _name, Object _object) {
        activeObjects.put(_name, _object);
        objectToName.put(_object, _name + ": " + _object.getClass().getSimpleName());
    }

    /** Objektumok kiregisztrációja a HashMap-ekből **/
    public static void popEntity(String _name) {
        Object removedObject = activeObjects.remove(_name);
        if(removedObject != null) {
            objectToName.remove(removedObject);
        }
    }

    /** Objektum lekérése név alapján **/
    public static Object getEntityByName(String _name) {
        return activeObjects.get(_name);
    }

    /** Objektum lekérése azonosító alapján **/
    public static String getEntityByRef(Object _ref) { return objectToName.get(_ref); }

    /** HashMap-ek ürítése, indentálás alaphelyzetbe állítása **/
    private static void flushEveryEntity() {
        activeObjects.clear();
        objectToName.clear();
        CallChainLogger.reset();
    }

    /** Kiírja a HashMap-ek tartalmát
     Az execute fgv-ben a DEBUG flaggel bekapcsolható **/
    private static void dumpEveryEntity() {

        System.out.println("\n########################");
            System.out.println("ALL REGISTERED ENTITIES");
        System.out.println("########################\n");

        System.out.println("**********************");
        System.out.println("Map: Names -> Objects ");
        System.out.println("**********************");

        for(Map.Entry<String, Object> entry : activeObjects.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        System.out.println("\n**********************");
        System.out.println("Map: Objects -> Names ");
        System.out.println("**********************");

        for(Map.Entry<Object, String> entry : objectToName.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    /** Osztály a tesztesetek kezeléséhez **/
    private static class TestCase {
        /** Teszteset neve **/
        private final String name;
        /** Maga a futtatható use case a teszteset mögött **/
        private final Runnable action;

        public TestCase(String _name, Runnable _action) {
            this.name = _name;
            this.action = _action;
        }

        /** Run implementáció a Runnable interface-nek **/
        public void run() {
            action.run();
        }

        /** Teszteset nevének lekérése **/
        public String getName() {
            return this.name;
        }
    }

    /** Osztály a loggoláshoz **/
    public static class CallChainLogger {

        /** A jelenlegi behúzások száma **/
        private static int indentCount = 0;

        /** Lenullázza a behúzást **/
        private static void reset() {
            indentCount = 0;
        }

        /** String-ként adja vissza a jelenlegi behúzást **/
        private static String getIndent() {
            StringBuilder stringBuilder = new StringBuilder();

            for(int i = 0; i < indentCount; i++) {
                stringBuilder.append("  ");
            }

            return stringBuilder.toString();
        }

        /** Metódus a függvényhívás loggolásához
         *  @param _caller A függvényt hívó objektum
         *  @param _methodSignature A függvény szignatúrája **/
        public static void printCall(Object _caller, String _methodSignature) {
            if (!Skeleton.ENABLE_LOGGING)
                return;
            String name = objectToName.getOrDefault(_caller, "unknown");
            String callIndent =  getIndent();

            System.out.println(callIndent + "-> " + name + "." + _methodSignature);
            indentCount++;
        }

        /** Metódus a függvények visszatérésének loggolásához
         @param _value A visszatérés értéke **/
        public static void printReturn(String _value) {
            if (!Skeleton.ENABLE_LOGGING)
                return;
            indentCount--;
            String returnIndent = getIndent();

            if (_value == null || _value.isEmpty()) {
                System.out.println(returnIndent + "<-");
            } else {
                System.out.println(returnIndent + "<- " + _value);
            }
        }

        /** Metódus a felhasználótól való kérdezéshez **/
        public static String askQuestion(String _question, String _options) {
            String questionIndent = getIndent();

            if(_options == null || _options.isEmpty()) {
                System.out.print(questionIndent + "? " + _question + " ");
            } else {
                System.out.print(questionIndent + "? " + _question + " (" + _options + "): ");
            }


            return scanner.next();
        }
    }

    public static void main(String[] args) {
        registerTestCases();
        execute();
    }

    /** A tesztesetek regisztrációja a testCases HashMap-be **/
    private static void registerTestCases() {

        testCases.put(0, new TestCase("Kilépés\n", Skeleton::exitProgram));

        testCases.put(1, new TestCase("Havazás", UseCaseImplementations::UC1));
        testCases.put(2, new TestCase("Hatásos általános takarítás: Hóeltakarítás söpréssel", UseCaseImplementations::UC2));
        testCases.put(3, new TestCase("Takarítás és ennek hatására egyenleg változása", UseCaseImplementations::UC3));
        testCases.put(4, new TestCase("Busz megfordulása és pontszerzés", UseCaseImplementations::UC4));
        testCases.put(5, new TestCase("Hókotró fej vásárlása", UseCaseImplementations::UC5));
        testCases.put(6, new TestCase("Hatástalan takarítás: Hóeltakarítás jégtörővel", UseCaseImplementations::UC6));
        testCases.put(7, new TestCase("Két autó ütközik jeges úton",  UseCaseImplementations::UC7));
        testCases.put(8, new TestCase("Hókotró fej újratöltése", UseCaseImplementations::UC8));
        testCases.put(9, new TestCase("Busz/Autó interakció hóval borított úton", UseCaseImplementations::UC9));
        testCases.put(10, new TestCase("Hókotró fej csere",  UseCaseImplementations::UC10));
        testCases.put(11, new TestCase("Autó interakció száraz úton", UseCaseImplementations::UC11));
        testCases.put(12, new TestCase("Hóeltakarítás sószóró fejjel",  UseCaseImplementations::UC12));
        testCases.put(13, new TestCase("Autó interakció sószórt úton", UseCaseImplementations::UC13));
        testCases.put(14, new TestCase("Söprőfej hatása szomszédos sávok esetén: Hókotró söprőfejjel takarít havas utat", UseCaseImplementations::UC14));
        testCases.put(15, new TestCase("Hókotró utat próbál takarítani kifogyott Sárkány vagy Sószóró fejjel", UseCaseImplementations::UC15));
        testCases.put(16, new TestCase("Jeges út takarítása IceBreaker fejjel", UseCaseImplementations::UC16));
    }

    /** A testCases HashMap tartalma alapján a menü kiírása **/
    private static void displayMenu() {
        System.out.println("\n----------------------");
        System.out.println("--- Skeleton Menu ---");
        System.out.println("----------------------\n");

        testCases.entrySet().stream()
                 .sorted(Map.Entry.comparingByKey())
                 .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue().getName()));

        System.out.println("---------------------------");
        System.out.print(">  ");
    }


    private static void exitProgram() {
        System.out.println("Kilépés...");
        System.exit(0);
    }

    /** A programot futtató függvény **/
    private static void execute() {

        while (true) {

            displayMenu();

            // Választott teszteset beolvasása és tárolása
            int choice = scanner.nextInt();
            TestCase currentTestCase = testCases.get(choice);

            // Ha létező teszteset, akkor meghívjuk a run() metódust
            // Minden TestCase rendelkezik egy Runnable adattaggal
            // Ezek a lent definiált UC1, UC2, stb.. függvények
            if (currentTestCase != null) {
                currentTestCase.run();

                // Debug flag a HashMap-ek tartalmának kiiírásához
                if(DEBUG) {
                    dumpEveryEntity();
                }

            } else continue;

            // A teszteset futása után kiürítjük a HashMap-eket
            flushEveryEntity();

            System.out.println("\n[Teszteset " + choice + " vége]\n");

            System.out.println("0: Kilépés");
            System.out.println("1: Új teszt indítása");

            System.out.println("---------------------");
            System.out.print(">  ");

            if(scanner.nextInt() == 0) { exitProgram(); } {}
        }
    }
}
