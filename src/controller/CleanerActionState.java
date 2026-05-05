package controller;

import Vehicle.Cleaner;
import Vehicle.SnowPlow;
import attachments.Attachment;
import attachments.Dragon;
import attachments.IceBreaker;
import attachments.SaltVommiter;
import attachments.Sweeper;
import attachments.VomitingHead;
import attachments.StoneVommiter;

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

                yield this;
            }

            case Message.BuyAttachment buyAttachmentMsg -> {
                switch (buyAttachmentMsg.type()) {
                    case DRAGON -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new Dragon());
                    case ICE_BREAKER -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new IceBreaker());
                    case SALT_VOMITTER -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new SaltVommiter());
                    case SWEEPER -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new Sweeper());
                    case VOMITING_HEAD -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new VomitingHead());
                    case STONE_VOMITTER -> cleaner.getSnowPlows().get(currentSnowPlowIdx).buyAttachment(new StoneVommiter());
                    default -> {}
                }
                yield this;
            }

            case Message.PickLane pickLaneMsg -> {
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

            case Message.SwapAttachment swapAttachmentMsg -> {
                SnowPlow sp = cleaner.getSnowPlows().get(currentSnowPlowIdx);
                for (Attachment tool : sp.getOwnedTools()) {
                    boolean matches = switch (swapAttachmentMsg.type()) {
                        case DRAGON -> tool instanceof Dragon;
                        case ICE_BREAKER -> tool instanceof IceBreaker;
                        case SALT_VOMITTER -> tool instanceof SaltVommiter;
                        case SWEEPER -> tool instanceof Sweeper;
                        case VOMITING_HEAD -> tool instanceof VomitingHead;
                        case STONE_VOMITTER -> tool instanceof StoneVommiter;
                    };
                    if (matches) {
                        sp.changeAttachment(tool);
                        break;
                    }
                }
                yield this;
            }

            case Message.RefillAttachment refillAttachmentMsg -> {
                cleaner.getSnowPlows().get(currentSnowPlowIdx).refillAttachment();
                yield this;
            }

            default -> this;
        };
    }

}
