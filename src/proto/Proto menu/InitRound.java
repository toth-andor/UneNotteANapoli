public class InitRound {

    public static void main(String[] args) {

        System.out.println("-----------------------------------------------------------------");
        System.out.println("         Játék inicializálása [Felhasználói mód] [Rand]");
        System.out.println("-----------------------------------------------------------------");

        System.out.println("\n? =========================== DOCS ============================ ?");
        System.out.println("| Útmutató a program működéséhez: help                          |");
        System.out.println("? ------------------------------------------------------------- ?");
        System.out.println("| Játék vezérlésének leírása: help game                         |");
        System.out.println("? ------------------------------------------------------------- ?");
        System.out.println("| Konfigurációs útmutató: help conf                             |");
        System.out.println("| Külső konfigurációs fájl elvárt formátuma: help conf format   |");
        System.out.println("? ------------------------------------------------------------- ?");
        System.out.println("| Tesztelési útmutató: help test                                |");
        System.out.println("? ------------------------------------------------------------- ?\n");

        System.out.println("# ================= TESZTELÉS [RANDOMIZÁCIÓ] =================== #");
        System.out.println("| A randomizáció kikapcsolása: randomoff <SEED>                  |");
        System.out.println("| A randomizáció bekapcsolása: randomon                          |");
        System.out.println("# -------------------------------------------------------------- #\n");


        System.out.println("# ========================= VEZÉRLÉS ========================== #");
        System.out.println("| Játék indítása: start                                         |");
        System.out.println("# ------------------------------------------------------------- #");
        System.out.println("| Kilépés: exit                                                 |");
        System.out.println("# ------------------------------------------------------------- #\n");


        System.out.println("* ======================== BEÁLLÍTÁSOK ======================== *");
        System.out.println("| Konfiguráció betöltése: load <src filepath>                   |");
        System.out.println("* ------------------------------------------------------------- *");
        System.out.println("| Konfiguráció törlése: clear                                   |");
        System.out.println("* ------------------------------------------------------------- *");
        System.out.println("| Tesztelési mód engedélyezése: mode test                       |");
        System.out.println("| Tesztelési mód kikapcsolása: mode user                        |");
        System.out.println("* ------------------------------------------------------------- *\n");

        System.out.println("============================= AUTÓK =============================");
        System.out.println("----------------------------");
        System.out.println("Autók száma: nincs megadva");
        System.out.println("----------------------------");
        System.out.println("* ------------------------------------------------------------- *");
        System.out.println("| Konfigurálása: carcount <value: non-negative integer>         |");
        System.out.println("* ------------------------------------------------------------- *\n");

        System.out.println("* ========================= JÁTÉKOSOK ========================= *");
        System.out.println("| Játékos felvétele: addplayer <role: bus/cleaner>              |");
        System.out.println("| Játékos eltávolítása:  rmplayer <name>                        |");
        System.out.println("* ------------------------------------------------------------- *");

        System.out.println("\n----------------------------");
        System.out.println("Aktív játékosok");
        System.out.println("----------------------------");
        System.out.println("# player_1 [BUS]");
        System.out.println("# player_2 [CLEANER]");
        System.out.println("----------------------------");



        System.out.println("\n> ");

    }

}
