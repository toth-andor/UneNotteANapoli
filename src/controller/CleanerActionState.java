package controller;

import Vehicle.Cleaner;
import attachments.Dragon;
import attachments.IceBreaker;
import attachments.SaltVommiter;
import attachments.Sweeper;
import attachments.VomitingHead;

public class CleanerActionState extends GameState {
    int currentSnowPlowIdx;
    Cleaner cleaner;

    public CleanerActionState(Controller controller, Cleaner cleaner) {
        super(controller);
        this.cleaner = cleaner;
    }

    public int getCurrentSnowPlowIdx() {
        return currentSnowPlowIdx;
    }

    public Cleaner getCleaner() {
        return cleaner;
    }

    @Override
    public GameState process(Message msg) {
        return switch (msg) {
            case Message.BuySnowPlow buySnowPlowMsg -> {
                cleaner.buySnowPlow(controller.getMapModel().getRandomLane());

                // TODO: Valahogy jelezni, hogy sikerült-e a vásárlás
                yield this;
            }

            case Message.BuyAttachment buyAttachmentMsg -> {
                // TODO: Valahogy jelezni, hogy sikerült-e a vásárlás
                switch (buyAttachmentMsg.type()) {
                    case DRAGON -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new Dragon());
                    case ICE_BREAKER -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new IceBreaker());
                    case SALT_VOMITTER -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new SaltVommiter());
                    case SWEEPER -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new Sweeper());
                    case VOMITING_HEAD -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new VomitingHead());
                    // TODO: case STONE_VOMITTER -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new StoneVomitter());
                    default -> {}
                }
                yield this;
            }

            case Message.PickLane pickLaneMsg -> {
                // TODO: return kezelése
                cleaner.getSnowPlows().get(currentSnowPlowIdx).gotoLane(pickLaneMsg.lane(), 0);
                if (++currentSnowPlowIdx < cleaner.getSnowPlows().size())
                    yield this;
                else {
                    if (controller.getPlayers().isLastPlayer()) {
                        controller.endOfTurn();
                    }
                    Player nextPlayer = controller.getPlayers().nextPlayer();
                    yield switch (nextPlayer.getType()) {
                        case PlayerType.PCleaner pCleaner -> new CleanerActionState(controller, pCleaner.cleaner());
                        case PlayerType.PBusDriver pBusDriver -> new BusActionState(controller, pBusDriver.bus());
                        default -> this;
                    };
                }
            }

            case Message.RefillAttachment refillAttachmentMsg -> {
                cleaner.getSnowPlows().get(currentSnowPlowIdx).refillAttachment();
                yield this;
            }

            default -> this;
        };
    }

}
