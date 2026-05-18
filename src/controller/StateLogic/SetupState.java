package controller.StateLogic;

import vehicle.Cleaner;
import vehicle.SnowPlow;
import vehicle.Bus;
import controller.ControllerLogic.Controller;
import controller.MapLogic.IMapModel;
import controller.MessageLogic.Message;
import controller.PlayerLogic.Player;
import controller.PlayerLogic.PlayerType;

/**
 * A játék kezdete előtti fázis, ahol a világ felépítése zajlik.
 * Ebben az állapotban adhatóak hozzá a játékosok és építhető fel a térkép.
 */
public class SetupState extends GameState {

    static int nextBusID = 1;
    static int nextPlowID = 1;

    public SetupState(Controller controller) {
        super(controller);
    }

    @Override
    public GameState process(Message msg) {
        IMapModel mm = controller.getMapModel();
        return switch (msg) {
            case Message.AddCleaner playerName -> {
                Cleaner owner = new Cleaner(1000);
                new SnowPlow(owner, mm.getRandomLane(), "snowplow_" + nextPlowID++);
                Player player = new Player(new PlayerType.PCleaner(owner), playerName.name());
                controller.getPlayers().addPlayer(player);
                yield this;
            }
            case Message.AddBusDriver playerName -> {
                // TODO: ellenőrizni kéne, hogy a két road nem ugyanaz
                Bus bd = new Bus(mm.getRandomRoad(), mm.getRandomRoad(), mm.getRandomLane(), 500, "bus_" + nextBusID++);
                Player player = new Player(new PlayerType.PBusDriver(bd), playerName.name());
                controller.getPlayers().addPlayer(player);
                yield this;
            }
            case Message.AddJunction addJunctionsMsg -> {
                mm.addJunction(addJunctionsMsg.count());
                yield this;
            }
            case Message.AddNPCCar addNPCCarMsg -> {
                for (int i = 0; i < addNPCCarMsg.count(); i++) {
                   controller.getNpcHandler().addNPC(mm.getRandomRoad(), mm.getRandomRoad(), mm.getRandomLane());
                }
                yield this;
            }
            case Message.AddRoad addRoadMsg -> {
                mm.addRoad(addRoadMsg.j1(), addRoadMsg.j2());
                yield this;
            }
            case Message.SaveMap saveMapMsg -> {
//                mm.validateMapModel();
                if(mm.validateMapModel()) {
                    System.out.println("CORRECT");
                } else {
                    System.out.println("INCORRECT");
                }
                yield this;
            }
            case Message.StartGame startGameMsg -> {
                // Játék indítása
                if (controller.getPlayers().isEmpty()) {
                    yield this; // Nincsenek játékosok
                }

                // Első játékos lekérése
                Player firstPlayer = controller.getPlayers().getCurrentPlayer();

                yield switch (firstPlayer.getType()) {
                    case PlayerType.PCleaner pCleaner -> {
                        yield new CleanerActionState(controller, pCleaner.cleaner());
                    }
                    case PlayerType.PBusDriver pBusDriver -> {
                        yield new BusActionState(controller, pBusDriver.bus());
                    }
                    default -> this;
                };
            }
            case Message.RandomOn randomOnMsg -> {
                controller.getRng().disableSeed();
                yield this;
            }
            case Message.RandomOff randomOffMsg -> {
                controller.getRng().enableSeed(randomOffMsg.seed());
                yield this;
            }
            default -> this;
        };
    }
}
