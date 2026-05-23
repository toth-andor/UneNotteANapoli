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
     * receive(msg)-t a megfelelő Message-el.
     *
     * @param cfg a konfigurációs fájl elérési útvonala
     */
    @Override
    public void loadConfig(String cfg) {
        String[] lines = cfg.split("\\r?\\n");

        for (String rawLine : lines) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;

            String[] args = line.split(" ");
            String command = args[0].toLowerCase();

            Message message = null;

            switch (command) {
                case "randomoff" -> {
                    if (args.length == 2) {
                        try {
                            message = new Message.RandomOff(Integer.parseInt(args[1]));
                        } catch (NumberFormatException e) {
                            System.out.println("Hibás seed érték: " + args[1]);
                        }
                    }
                }
                case "randomon" -> message = new Message.RandomOn();

                case "addjunction" -> {
                    if (args.length == 3) {
                        message = new Message.AddJunction(Float.parseFloat(args[1]), Float.parseFloat(args[2]));
                    }
                }
                case "addroad" -> {
                    if (args.length == 3) {
                        message = new Message.AddRoad(args[1], args[2]);
                    }
                }
                case "savemap" -> message = new Message.SaveMap();

                case "carcount" -> {
                    if (args.length == 2) {
                        message = new Message.AddNPCCar(Integer.parseInt(args[1]));
                    }
                }
                case "addplayer" -> {
                    if (args.length == 3) {
                        if (args[1].equalsIgnoreCase("cleaner")) {
                            message = new Message.AddCleaner(args[2]);
                        } else if (args[1].equalsIgnoreCase("bus")) {
                            message = new Message.AddBusDriver(args[2]);
                        }
                    }
                }
                default -> System.out.println("Ismeretlen konfig parancs: " + command);
            }

            if (message != null) {
                receive(message);
            }
        }
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
            npcHandler.moveNPCs(mapModel, getRoundNumber());
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
