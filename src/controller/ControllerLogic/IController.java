package controller.ControllerLogic;

import controller.MapLogic.IMapModel;
import controller.MessageLogic.Message;
import controller.PlayerLogic.NPCHandler;
import controller.PlayerLogic.PlayerDirectory;
import controller.RandomizerLogic.Randomizer;
import controller.StateLogic.GameState;
import map.Lane;
import vehicle.Vehicle;
import java.util.List;

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

    Randomizer getRng();

    /**
     * @return az éppen mozgatandó jármű, vagy null ha nem játékfázis
     */
    Vehicle getCurrentVehicle();

    /**
     * @return a jelenlegi jármű által elérhető sávok listája
     */
    List<Lane> getSelectableLanes();

    /**
     * @return az aktuális busz célállomásának sávjai, vagy üres lista ha nem buszvezető az aktív játékos
     */
    List<Lane> getDestinationLanes();
}
