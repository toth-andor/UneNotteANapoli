package controller;

import Vehicle.Cleaner;
import Vehicle.SnowPlow;
import Vehicle.Bus;

/**
 * A játék kezdete előtti fázis, ahol a világ felépítése zajlik.
 * Ebben az állapotban adhatóak hozzá a játékosok és építhető fel a térkép.
 */
public class SetupState extends GameState {

    public SetupState(Controller controller) {
        super(controller);
    }

    @Override
    public GameState process(Message msg) {
        IMapModel mm = controller.getMapModel();
        return switch (msg) {
            case Message.AddCleaner playerName -> {
                Cleaner owner = new Cleaner(1000);
                new SnowPlow(owner, mm.getRandomLane());
                Player player = new Player(new PlayerType.PCleaner(owner), playerName.name());
                controller.getPlayers().addPlayer(player);
                yield this;
            }
            case Message.AddBusDriver playerName -> {
                // TODO: ellenőrizni kéne, hogy a két road nem ugyanaz
                Bus bd = new Bus(mm.getRandomRoad(), mm.getRandomRoad(), mm.getRandomLane(), 500);
                Player player = new Player(new PlayerType.PBusDriver(bd), playerName.name());
                controller.getPlayers().addPlayer(player);
                yield this;
            }
            case Message.AddJunction addJunctionsMsg -> {
                mm.addJunction(addJunctionsMsg.name());
                yield this;
            }
            case Message.AddRoad addRoadMsg -> {
                mm.addRoad(addRoadMsg.j1(), addRoadMsg.j2());
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
            default -> this;
        };
    }
}
