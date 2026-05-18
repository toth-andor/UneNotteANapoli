package controller.StateLogic;

import vehicle.Bus;
import controller.Controller;
import controller.Message;
import controller.PlayerLogic.Player;
import controller.PlayerLogic.PlayerType;

/**
 * A buszvezető köréért felelős állapot.
 * Mivel a busz nem rendelkezik cserélhető fejekkel, ez az állapot
 * csak a mozgást (sávválasztást) várja el a játékostól.
 */
public class BusActionState extends GameState {

    /** Az éppen irányított busz */
    private Bus currentBus;

    /**
     * Létrehoz egy új BusActionState példányt.
     *
     * @param controller a vezérlő kontextus
     * @param currentBus az éppen irányított busz
     */
    public BusActionState(Controller controller, Bus currentBus) {
        super(controller);
        this.currentBus = currentBus;
    }

    /**
     * Kizárólag a PickLane üzenetet kezeli, majd átadja a kört
     * a következő egységnek (következő játékos vagy rendszerfázis).
     *
     * @param msg a feldolgozandó üzenet
     * @return a következő állapot
     */
    @Override
    public GameState process(Message msg) {
        return switch (msg) {
            case Message.PickLane pickLaneMsg -> {
                currentBus.gotoLane(pickLaneMsg.lane(), controller.getRoundNumber());

                if (controller.getPlayers().isLastPlayer()) {
                    controller.endOfTurn();
                }

                if(currentBus.getCurrentDestination().equals(currentBus.getCurrentLane().getRoad())) {
                    currentBus.addIncome(1);
                }

                Player nextPlayer = controller.getPlayers().nextPlayer();
                yield switch (nextPlayer.getType()) {
                    case PlayerType.PCleaner pCleaner -> {
                        yield new CleanerActionState(controller, pCleaner.cleaner());
                    }
                    case PlayerType.PBusDriver pBusDriver -> {
                        yield new BusActionState(controller, pBusDriver.bus());
                    }
                };
            }
            default -> this;
        };
    }

    /**
     * Visszaadja az éppen irányított buszt.
     *
     * @return az éppen irányított busz
     */
    public Bus getCurrentBus() {
        return currentBus;
    }
}
