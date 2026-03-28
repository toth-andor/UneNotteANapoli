package Vehicle;

import attachments.Attachment;
import java.util.ArrayList;
import map.Lane;
import skeleton.Skeleton;
import skeleton.Skeleton.CallChainLogger;

/**
 * Egy takarító által irányított hókotrót reprezentál.
 * Tárolja a megvásárolt fejeket, nyilvántartja az aktív fejet, és elvégzi a sávok takarítását.
 * Egy Cleaner játékoshoz tartozik, akinek egyenlegéből finanszírozódnak a vásárlások és utántöltések.
 */
public class SnowPlow extends Vehicle implements ISnowPlow {

    /**
     * A hókotrót irányító takarító.
     */
    Cleaner owner;

    /**
     * A hókotró tulajdonában lévő, megvásárolt fejek gyűjteménye.
     */
    ArrayList<Attachment> ownedTools;


    /**
     * A hókotró tulajdonában lévő, megvásárolt gyűjteményt adja vissza.
     *
     * @return a megvásárolt fejek gyűjteménye
     */
    public ArrayList<Attachment> getOwnedTools() {
        return ownedTools;
    }

    /**
     * Az éppen aktív, takarításhoz használt fej; az ownedTools egyik eleme.
     */
    Attachment currentTool;

    /**
     * Létrehoz egy új hókotrót.
     *
     * @param owner a hókotrót irányító takarító
     * @param currentLane a hókotró kezdeti sáva
     */
    public SnowPlow(Cleaner owner, Lane currentLane) {
        super(currentLane);
        this.owner = owner;
        this.ownedTools = new ArrayList<>();
        this.currentTool = null;
    }

    /**
     * Az a fejre vált, ha az szerepel az ownedTools listában.
     *
     * @param a az új fej
     * @return true, ha a váltás sikerült, egyébként false
     */
    public boolean changeAttachment(Attachment a) {
        CallChainLogger.printCall(
            this,
            "changeAttachment(" + Skeleton.getEntityByRef(a) + ")"
        );
        for (Attachment tool : ownedTools) {
            if (tool.equals(a)) {
                currentTool = tool;
                CallChainLogger.printReturn("true");
                return true;
            }
        }
        CallChainLogger.printReturn("false");
        return false;
    }

    /**
     * Megvásárolja az új fejet, ha a tulajdonos egyenlegén elegendő fedezet áll rendelkezésre.
     *
     * @param newAttachment a megvásárolni kívánt fej
     * @return true, ha a vásárlás sikerült, egyébként false
     */
    public boolean buyAttachment(Attachment newAttachment) {
        CallChainLogger.printCall(
            this,
            "buyAttachment(" + Skeleton.getEntityByRef(newAttachment) + ")"
        );
        if (newAttachment.getPrice() <= owner.getScore()) {
            ownedTools.add(newAttachment);
            owner.setScore(owner.getScore() - newAttachment.getPrice());
            CallChainLogger.printReturn("true");
            return true;
        }

        CallChainLogger.printReturn("false");
        return false;
    }

    /**
     * Az aktív fej fogyóanyagát tölti fel, ha a tulajdonos egyenlege fedezi a feltöltés árát.
     */
    public void refillAttachment() {
        CallChainLogger.printCall(this, "refillAttachment()");
        owner.setScore(currentTool.refill(owner.getScore()));
        CallChainLogger.printReturn(null);
    }

    /**
     * Meghívja az aktív fej cleanLane metódusát, ezzel kezdeményezi a sáv takarítását,
     * amelynek tényleges logikáját a LaneState végzi a cleanWithHead-en belül.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     */
    public void interactWithLane(Lane l, int timestamp) {
        CallChainLogger.printCall(
            this,
            "interactWithLane(" +
                Skeleton.getEntityByRef(l) +
                ", " +
                timestamp +
                ")"
        );
        currentTool.cleanLane(l, timestamp);
        CallChainLogger.printReturn(null);
    }

    /**
     * Üres implementáció — a hókotró ütközés esetén is folytatja a munkát.
     *
     * @param timestamp az ütközés időpontja
     */
    public void crash(int timestamp) {
        CallChainLogger.printCall(this, "crash(" + timestamp + ")");
        CallChainLogger.printReturn(null);
    }
}
