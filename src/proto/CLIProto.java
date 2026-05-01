package proto;

import java.util.List;
import controller.*;

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
     * TODO: mód (user/test) és randomizáció állapotának megjelenítése a fejlécben —
     *       szükséges hozzá lekérdező metódus az IController-en
     * TODO: autók száma — szükséges hozzá carcount getter az IController-en
     */
    private void displayInitMenu() {
        System.out.println("-----------------------------------------------------------------");
        System.out.println("          Játék inicializálása");
        System.out.println("-----------------------------------------------------------------");
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
        System.out.println("| Tesztelési mód engedélyezése: mode test                       |");
        System.out.println("| Tesztelési mód kikapcsolása: mode user                        |");
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
     * TODO: kör száma — szükséges hozzá getter az IController-en
     * TODO: TÉRKÉP szekció — szükséges hozzá Vehicle.getCurrentLane() public getter
     *       és az elérhető szomszédos sávok lekérdezése az IMapModel-en keresztül
     */
    private void displayCurrentRound() {
        System.out.println("------------------------------------------------");
        System.out.println("   Játék folyamatban");
        System.out.println("------------------------------------------------");
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

        System.out.println("================== TÉRKÉP ==================");
        System.out.println("// TODO");
        System.out.println();

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
