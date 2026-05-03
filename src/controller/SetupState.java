package controller;

import Vehicle.Cleaner;
import Vehicle.SnowPlow;
import Vehicle.Bus;

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
                map.Lane lane = mm.getRandomLane();
                if (lane == null) { yield this; }
                Cleaner owner = new Cleaner(1000);
                new SnowPlow(owner, lane);
                Player player = new Player(new PlayerType.PCleaner(owner), playerName.name());
                controller.getPlayers().addPlayer(player);
                yield this;
            }
            case Message.AddBusDriver playerName -> {
                map.Road r1 = mm.getRandomRoad();
                map.Road r2 = mm.getRandomRoad();
                map.Lane lane = mm.getRandomLane();
                if (r1 == null || r2 == null || lane == null) { yield this; }
                Bus bd = new Bus(r1, r2, lane, 500);
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
                    map.Road d1 = mm.getRandomRoad();
                    map.Road d2 = mm.getRandomRoad();
                    map.Lane l  = mm.getRandomLane();
                    if (d1 == null || d2 == null || l == null) break;
                    controller.getNpcHandler().addNPC(d1, d2, l);
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
