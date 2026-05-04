package Vehicle;

import attachments.Attachment;
import attachments.Sweeper;

import java.util.ArrayList;

import attachments.VomitingHead;
import map.Lane;

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
    public SnowPlow(Cleaner owner, Lane currentLane, String name) {
        super(currentLane, name);
        this.owner = owner;
        this.ownedTools = new ArrayList<>();
        VomitingHead v = new VomitingHead();
        this.ownedTools.add(v);
        this.currentTool = v;
        owner.registerSnowPlow(this);
    }

    /**
     * Az a fejre vált, ha az szerepel az ownedTools listában.
     *
     * @param a az új fej
     * @return true, ha a váltás sikerült, egyébként false
     */
    public boolean changeAttachment(Attachment a) {
        for (Attachment tool : ownedTools) {
            if (tool.equals(a)) {
                currentTool = tool;
                return true;
            }
        }
        return false;
    }

    /**
     * Megvásárolja az új fejet, ha a tulajdonos egyenlegén elegendő fedezet áll rendelkezésre.
     *
     * @param newAttachment a megvásárolni kívánt fej
     * @return true, ha a vásárlás sikerült, egyébként false
     */
    public boolean buyAttachment(Attachment newAttachment) {
        if (newAttachment.getPrice() <= owner.getScore()) {
            ownedTools.add(newAttachment);
            owner.setScore(owner.getScore() - newAttachment.getPrice());
            return true;
        }
        return false;
    }

    /**
     * Az aktív fej fogyóanyagát tölti fel, ha a tulajdonos egyenlege fedezi a feltöltés árát.
     */
    public void refillAttachment() {
        owner.setScore(currentTool.refill(owner.getScore()));
    }

    /**
     * Meghívja az aktív fej cleanLane metódusát, ezzel kezdeményezi a sáv takarítását,
     * amelynek tényleges logikáját a LaneState végzi a cleanWithHead-en belül.
     *
     * @param l a takarítandó sáv
     * @param timestamp az aktuális idő
     */
    public void interactWithLane(Lane l, int timestamp) {
        if (currentTool.cleanLane(l, timestamp))
            owner.addIncome(2);
    }

    /**
     * Üres implementáció — a hókotró ütközés esetén is folytatja a munkát.
     *
     * @param timestamp az ütközés időpontja
     */
    public void crash(int timestamp) {
    }

    public Lane getCurrentLane() {
        return currentLane;
    }
}
