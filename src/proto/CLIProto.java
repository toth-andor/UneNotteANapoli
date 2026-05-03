package proto;

import java.util.List;
import controller.*;
import map.Junction;
import map.Lane;
import map.OutdoorLane;
import map.Road;
import states.CrashedState;
import states.DryState;
import states.IcyState;
import states.SaltedState;
import states.SnowyState;
import Vehicle.Vehicle;

/**
 * A parancssori felhasználói felület belépési pontja.
 * Felelős a képernyő megjelenítéséért és a főciklus futtatásáért.
 * Az input feldolgozását az ICommandLineInterpreter végzi,
 * az állapot lekérdezését az IController interfészen keresztül végzi.
 */
public class CLIProto {

    private final ICommandLineInterpreter parser;
    private final IController controller;

    public CLIProto(ICommandLineInterpreter parser, IController controller) {
        this.parser = parser;
        this.controller = controller;
    }

    /**
     * Létrehozza a Controller-t, az interpreter-t és elindítja az alkalmazást.
     * Megjegyzés: a CommandLineInterpreter konstruktora IController paramétert vár —
     * az implementálónak ezt a konstruktort kell megvalósítania.
     */
    public static void main(String[] args) {
        Controller controller = new Controller();
        ICommandLineInterpreter interpreter = new CommandLineInterpreter(controller); // CommandLineInterpreter(IController) konstruktor szükséges
        CLIProto app = new CLIProto(interpreter, controller);
        app.run();
    }

    /**
     * A főciklus: minden iterációban frissíti a képernyőt,
     * majd átadja a vezérlést a parsernek a következő parancs beolvasásához.
     * A tick minden körben növekszik és a Vehicle.gotoLane timestamp paramétereként szolgál.
     */
    private void run() {
        int tick = 0;
        while (true) {
            displayCurrentState();
            parser.parse(tick);
            tick++;
        }
    }

    /**
     * A Controller aktuális állapota alapján dönti el melyik képernyőt kell megjeleníteni.
     * SetupState → inicializációs menü, egyéb → játék köre.
     */
    private void displayCurrentState() {
        if (controller.getGameState() instanceof SetupState) {
            displayInitMenu();
        } else {
            displayCurrentRound();
        }
    }

    /**
     * Az inicializációs menüt jeleníti meg.
     * Tartalmazza az elérhető parancsokat és az aktív játékosok listáját.
     * TODO: randomizáció állapotának megjelenítése a fejlécben — szükséges hozzá isRandomEnabled() az IController-en
     * TODO: autók száma — szükséges hozzá carcount getter az IController-en
     */
    private void displayInitMenu() {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("               Játék inicializálása");
        System.out.println("-----------------------------------------------------------------");
        System.out.println();
        System.out.println("? =========================== DOCS ============================ ?");
        System.out.println("| Útmutató a program működéséhez: help                          |");
        System.out.println("| Játék vezérlésének leírása: help game                         |");
        System.out.println("| Konfigurációs útmutató: help conf                             |");
        System.out.println("| Külső konfigurációs fájl elvárt formátuma: help conf format   |");
        System.out.println("| Tesztelési útmutató: help test                                |");
        System.out.println("? ------------------------------------------------------------- ?");
        System.out.println();
        System.out.println("# ================= TESZTELÉS [RANDOMIZÁCIÓ] =================== #");
        System.out.println("| A randomizáció kikapcsolása: randomoff <SEED>                  |");
        System.out.println("| A randomizáció bekapcsolása: randomon                          |");
        System.out.println("# -------------------------------------------------------------- #");
        System.out.println();
        System.out.println("# ========================= VEZÉRLÉS ========================== #");
        System.out.println("| Játék indítása: start                                         |");
        System.out.println("# ------------------------------------------------------------- #");
        System.out.println("| Kilépés: exit                                                 |");
        System.out.println("# ------------------------------------------------------------- #");
        System.out.println();
        System.out.println("* ======================== BEÁLLÍTÁSOK ======================== *");
        System.out.println("| Konfiguráció betöltése: load <src filepath>                   |");
        System.out.println("* ------------------------------------------------------------- *");
        System.out.println("| Konfiguráció törlése: clear                                   |");
        System.out.println("* ------------------------------------------------------------- *");
        System.out.println("* ------------------------------------------------------------- *");
        System.out.println();
        System.out.println("============================= AUTÓK =============================");
        System.out.println("----------------------------");
        System.out.println("Autók száma: nincs megadva"); // TODO: IController.getCarCount()
        System.out.println("----------------------------");
        System.out.println("* ------------------------------------------------------------- *");
        System.out.println("| Konfigurálása: carcount <value: non-negative integer>         |");
        System.out.println("* ------------------------------------------------------------- *");
        System.out.println();
        System.out.println("* ========================= JÁTÉKOSOK ========================= *");
        System.out.println("| Játékos felvétele: addplayer <role: bus/cleaner> [name]       |");
        System.out.println("| Játékos eltávolítása: rmplayer <name>                         |");
        System.out.println("* ------------------------------------------------------------- *");
        System.out.println();
        System.out.println("----------------------------");
        System.out.println("Aktív játékosok");
        System.out.println("----------------------------");
        for (Player p : controller.getPlayers().getPlayers()) {
            System.out.println("# " + p.getName() + " [" + roleOf(p) + "]");
        }
        System.out.println("----------------------------");
        System.out.println();
        System.out.print("> ");
    }

    /**
     * A játék aktuális körét jeleníti meg.
     * Tartalmazza a játékosok adatait, a térkép állapotát és az elérhető parancsokat.
     *       és az elérhető szomszédos sávok lekérdezése az IMapModel-en keresztül
     */
    private void displayCurrentRound() {
        System.out.println("------------------------------------------------");
        System.out.println("          Játék folyamatban");
        System.out.println("------------------------------------------------");
        System.out.println("                Jelenlegi kör: " + controller.getRoundNumber());
        System.out.println();

        PlayerDirectory dir = controller.getPlayers();
        Player current = dir.getCurrentPlayer();

        // Következő játékos kiszámítása az index alapján, nextPlayer() hívása nélkül,
        // mert az előrébb léptetné a sort
        List<Player> players = dir.getPlayers();
        int nextIdx = (dir.getCurrentPlayerIndex() + 1) % players.size();
        Player next = players.get(nextIdx);

        System.out.println("================= JÁTÉKOSOK ================");
        System.out.println("Jelenlegi játékos: " + current.getName() + " [" + roleOf(current) + "]");
        System.out.println("Score: " + scoreOf(current));
        System.out.println("--------------------------------------------");
        System.out.println("Következő játékos: " + next.getName() + " [" + roleOf(next) + "]");
        System.out.println("Score: " + scoreOf(next));
        System.out.println();

        System.out.println("? ========================= DOCS ========================== ?");
        System.out.println("| Játék vezérlésének leírása: help game                      |");
        System.out.println("| Tesztelési útmutató: help test                             |");
        System.out.println("? --------------------------------------------------------- ?");
        System.out.println();
        displayMap(current);

        // A takarító-specifikus parancsok csak CLEANER típusú játékosnál jelennek meg
        System.out.println("* ============================= VEZÉRLÉS ============================ *");
        System.out.println("| Sáv választása: pick <lane>                                         |");
        if (current.getType() instanceof PlayerType.PCleaner) {
            System.out.println("| Sáv választása takarítással: pick <lane> -clean                     |");
            System.out.println("| Fej cseréje: swap <attachment>                                      |");
            System.out.println("| Fej/Hókotró vásárlása: buy <attachment/snowplow>                    |");
            System.out.println("| Fej újratöltése: refill <attachment>                                |");
        }
        System.out.println("* ------------------------------------------------------------------- *");
        System.out.println();
        System.out.println("* =========================== KONFIGURÁCIÓ ========================== *");
        System.out.println("| Aktuális állapotkonfiguráció fájlba mentése: save <dst filepath>    |");
        System.out.println("| Új állapotkonfiguráció betöltése fájlból: load <src filepath>       |");
        System.out.println("* ------------------------------------------------------------------- *");
        System.out.println();
        System.out.println("* ======================== TESZTELÉS [LOG] ========================== *");
        System.out.println("| Aktuális állapot loggolása fájlba: snapshot <dst filepath>          |");
        System.out.println("| Aktuális állapot loggolása konzolra: state                          |");
        System.out.println("* ------------------------------------------------------------------- *");
        System.out.println("| Kilépés: exit                                                       |");
        System.out.println("* ------------------------------------------------------------------- *");
        System.out.println();
        System.out.print(current.getName() + " [" + roleOf(current) + "] > ");
    }

    private void displayMap(Player current) {
        Vehicle vehicle = vehicleOf(current);
        Lane currentLane = vehicle.getCurrentLane();
        Road currentRoad = currentLane.getRoad();
        Junction nextJunction = currentLane.getDestination();

        System.out.println("================== TÉRKÉP ==================");
        System.out.println("Jelenlegi út: " + currentRoad);           // TODO: currentRoad.getName()
        System.out.println("Jelenlegi sáv: " + currentLane + "  " + laneStateDisplay(currentLane)); // TODO: currentLane.getName()
        System.out.println("--------------------------------------------");
        if (current.getType() instanceof PlayerType.PBusDriver) {
            System.out.println("Célállomás: ?");                       // TODO: Bus.getDestinationJunction() + getName()
            System.out.println("--------------------------------------------");
        }
        System.out.println("Következő kereszteződés: " + nextJunction); // TODO: nextJunction.getName()
        System.out.println("-----------------------");
        System.out.println("Innen elérhető: ");
        System.out.println("--------------");
        for (Road road : nextJunction.getRoads()) {
            System.out.println("# " + road);                           // TODO: road.getName()
            for (Lane lane : road.getLanes()) {
                String laneType = lane instanceof OutdoorLane ? "OL" : "TL";
                System.out.println("\t #" + lane + "  [" + laneType + "] " + laneStateDisplay(lane)); // TODO: lane.getName()
            }
        }
        System.out.println();
    }

    private Vehicle vehicleOf(Player p) {
        return switch (p.getType()) {
            case PlayerType.PCleaner c -> c.cleaner().getSnowPlows().get(0);
            case PlayerType.PBusDriver b -> b.bus();
        };
    }

    private String laneStateDisplay(Lane lane) {
        if (lane instanceof OutdoorLane ol) {
            String stateName;
            if (ol.getCurrentState() instanceof DryState) stateName = "DRY";
            else if (ol.getCurrentState() instanceof SnowyState) stateName = "SNOWY";
            else if (ol.getCurrentState() instanceof IcyState) stateName = "ICY";
            else if (ol.getCurrentState() instanceof CrashedState) stateName = "CRASHED";
            else if (ol.getCurrentState() instanceof SaltedState) stateName = "SALTED";
            else stateName = "?";
            boolean navigable = ol.isNavigable() && !(ol.getCurrentState() instanceof CrashedState);
            return "[" + stateName + " " + (navigable ? "✓" : "x") + "]";
        }
        return "[DRY ✓]";
    }

    private String roleOf(Player p) {
        return switch (p.getType()) {
            case PlayerType.PCleaner c -> "CLEANER";
            case PlayerType.PBusDriver b -> "BUS";
        };
    }

    private int scoreOf(Player p) {
        return switch (p.getType()) {
            case PlayerType.PCleaner c -> c.cleaner().getScore();
            case PlayerType.PBusDriver b -> b.bus().getScore();
        };
    }
}
