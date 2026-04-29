public class StartGame {

    public static void main(String[] args) {

        System.out.println("------------------------------------------------");
        System.out.println("  Játék folyamatban [Felhasználói mód] [Rand]");
        System.out.println("------------------------------------------------");
        System.out.println("              Jelenlegi kör: 1");
        System.out.println("------------------------------------------------");

        System.out.println("\n=================== DOCS ====================");

        System.out.println("? ----------------------------------------- ?");
        System.out.println("| Játék vezérlésének leírása: help game     |");
        System.out.println("? ----------------------------------------- ?");
        System.out.println("| Tesztelési útmutató: help test            |");
        System.out.println("? ----------------------------------------- ?\n");


        System.out.println("================= JÁTÉKOSOK ================");

        System.out.println("Jelenlegi játékos: player_1 [BUS]");
        System.out.println("Score: 120");
        System.out.println("--------------------------------------------");
        System.out.println("Következő játékos: player_2 [CLEANER]");
        System.out.println("Score: 95\n");

        System.out.println("================== TÉRKÉP ==================");

        System.out.println("Jelenlegi út: road_4");
        System.out.println("Jelenlegi sáv: lane_12  [DRY ✓]");
        System.out.println("--------------------------------------------");
        System.out.println("Célállomás: junction_21");
        System.out.println("--------------------------------------------");


        System.out.println("Következő kereszteződés: junction_3");
        System.out.println("-----------------------");
        System.out.println("Innen elérhető: ");
        System.out.println("--------------");

        System.out.println("# road_5");
        System.out.println("\t #lane_14  [OL] [SNOWY ✓]");
        System.out.println("\t #lane_15  [OL] [SNOWY x]");
        System.out.println("\t #lane_16  [OL] [ICY x]");
        System.out.println("\t #lane_17  [TL] [DRY ✓]");
        System.out.println("# road_6");
        System.out.println("\t #lane_18  [TL] [DRY ✓]");
        System.out.println("\t #lane_19  [OL] [SNOWY x]");
        System.out.println("\t #lane_20  [OL] [ICY x]");
        System.out.println("\t #lane_21  [TL] [DRY ✓]");
        System.out.println("# road_7");
        System.out.println("\t #lane_22  [OL] [SNOWY ✓]");
        System.out.println("\t #lane_23  [OL] [CRASHED x]");
        System.out.println("\t #lane_24  [OL] [ICY x]");
        System.out.println("\t #lane_25  [TL] [DRY ✓]");

        System.out.println("\n* ============================= VEZÉRLÉS ============================ *");
        System.out.println("| Sáv választása: pick <lane>                                         |");
        System.out.println("| Sáv választása takarítással: pick <lane> -clean                     |");
        System.out.println("* ------------------------------------------------------------------- *");
        System.out.println("| Fej cseréje: swap <attachment>                                      |");
        System.out.println("| Fej/Hókotró vásárlása: buy <attachment/snowplow>                    |");
        System.out.println("| Fej újratöltése: refill <attachment>                                |");
        System.out.println("* ------------------------------------------------------------------- *");

        System.out.println("\n* =========================== KONFIGURÁCIÓ ========================== *");
        System.out.println("| Aktuális állapotkonfiguráció fájlba mentése: save <dst filepath>    |");
        System.out.println("| Új állapotkonfiguráció betöltése fájlból: load <src filepath>       |");
        System.out.println("* ------------------------------------------------------------------- *");

        System.out.println("\n* ======================== TESZTELÉS [LOG] =========================== *");
        System.out.println("| Akutális állapot loggolása fájlba: snapshot <dst filepath>          |");
        System.out.println("| Aktuális állapot loggolása konzolra: state                          |");
        System.out.println("* ------------------------------------------------------------------- *");
        System.out.println("| Kilépés: exit                                                       |");
        System.out.println("* ------------------------------------------------------------------- *");

        System.out.println("\nplayer_1 [BUS] > ");
    }

}
