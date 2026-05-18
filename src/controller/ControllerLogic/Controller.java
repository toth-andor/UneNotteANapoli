package controller.ControllerLogic;

import java.util.List;

import controller.MessageLogic.Message;
import vehicle.Vehicle;
import controller.MapLogic.IMapModel;
import controller.MapLogic.MapModel;
import controller.PlayerLogic.NPCHandler;
import controller.PlayerLogic.PlayerDirectory;
import controller.RandomizerLogic.Randomizer;
import controller.StateLogic.GameState;
import controller.StateLogic.SetupState;
import map.Lane;
import map.OutdoorLane;
import map.Road;
import states.IcyState;

/**
 * A rendszer központi osztálya, amely a State tervezési mintát alkalmazva
 * állapotgépként működik. Kontextusként tárolja a játék aktuális állapotát,
 * és az IController interfész megvalósítójaként az érkező üzeneteket továbbítja
 * a jelenlegi GameState példánynak.
 */
public class Controller implements IController {

    private static final int ICE_CRASH_CHANCE = 10;

    /** A játéktérkép (gráf) és a modell állapotának összefogója */
    private IMapModel mapModel;

    /** A regisztrált játékosok listája */
    private PlayerDirectory playerDirectory;

    /** Az állapotgép aktuális állapota */
    private GameState gameState;

    /** Az NPC autók mozgásáért felelős segédkomponens */
    public NPCHandler npcHandler;

    public Randomizer rng;

    /**
     * Létrehoz egy új Controller példányt.
     */
    public Controller() {
        this.rng = new Randomizer();
        this.playerDirectory = new PlayerDirectory();
        this.gameState = new SetupState(this);
        this.npcHandler = new NPCHandler();
        this.mapModel = new MapModel(rng);
    }

    /**
     * A külső felületekről (View/Proto) érkező üzenetek fogadása és továbbítása
     * a gameState.process(msg) hívással.
     *
     * @param msg a feldolgozandó üzenet
     */
    @Override
    public void receive(Message msg) {
        gameState = gameState.process(msg);
    }

    /**
     * Soronként feldolgozza a kapott konfigot, és minden sorhoz hív egy
     * receive(msg)-t a megfelelő Message-dszel.
     *
     * @param cfg a konfigurációs fájl tartalma
     */
    @Override
    public void loadConfig(String cfg) {
        // TODO: Implementálás a konfigurációs fájl feldolgozásának logikája
    }

    /**
     * Végrehajtja a rendszerfázis logikáját:
     * 1. Havazás
     * 2. NPC autók mozgatása
     */
    public void endOfTurn() {
        mapModel.snow(1);
        for (Road road : mapModel.getRoads()) {
            road.tick(getRoundNumber());
        }
        if (npcHandler != null) {
            npcHandler.moveNPCs();
        }
    }

    // Getterek és setterek a komponensek eléréséhez

    public IMapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(IMapModel mapModel) {
        this.mapModel = mapModel;
    }

    public PlayerDirectory getPlayers() {
        return playerDirectory;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public NPCHandler getNpcHandler() {
        return npcHandler;
    }

    @Override
    public int getRoundNumber() {
        return playerDirectory.getRoundNumber();
    }

    public Randomizer getRng() {
        return rng;
    }

    public void moveVehicleToLane(Vehicle vehicle, Lane lane) {
        vehicle.gotoLane(lane, 0);
        if (lane instanceof OutdoorLane) {
            OutdoorLane outdoorLane = (OutdoorLane) lane;
            if (outdoorLane.getCurrentState() instanceof IcyState) {
                List<Vehicle> vehicles = lane.getVehicles();
                for (Vehicle v : vehicles) {
                    if (rng.randomize(1, 100) < ICE_CRASH_CHANCE) {
                        v.crash(getRoundNumber());
                        vehicle.crash(getRoundNumber());
                        return;
                    }
                }
            }
        }
    }


}
