package controller;

/**
 * A Controller interfész, amely meghatározza a külső felületek
 * (View/Proto) számára elérhető metódusokat.
 */
public interface IController {

    /**
     * A külső felületekről (View/Proto) érkező üzenetek fogadása.
     *
     * @param msg a feldolgozandó üzenet
     */
    void receive(Message msg);

    /**
     * Konfigurációs fájl betöltése és feldolgozása.
     *
     * @param cfg a konfigurációs fájl tartalma
     */
    void loadConfig(String cfg);

    /**
     * @return a játéktérkép modellje
     */
    IMapModel getMapModel();

    /**
     * @return a játékosok nyilvántartása
     */
    PlayerDirectory getPlayers();

    /**
     * @return az aktuális játékállapot
     */
    GameState getGameState();

    /**
     * @return az NPC-ket kezelő komponens
     */
    NPCHandler getNpcHandler();

    /**
     * @return az aktuális kör sorszáma
     */
    int getRoundNumber();
}
