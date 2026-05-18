package vehicle;

import map.Lane;
import map.Road;


/**
 * Egy gépi vezérlésű személyautót reprezentál, amely a legrövidebb járható úton közlekedik
 * két célpont között. Hozzájárul a sávok letaposásához és ezáltal a jégesedéshez, de nem
 * termel bevételt és nem tart nyilván pontszámot.
 */
public class Car extends Commuter {
    /**
     * Létrehoz egy új személyautót.
     *
     * @param destination1 az első végállomás
     * @param destination2 a második végállomás
     * @param currentLane az autó kezdeti sáva
     */
    public Car(Road destination1, Road destination2, Lane currentLane, String name) {
        super(destination1, destination2, currentLane, name);
    }

    /**
     * Üres implementáció, mivel a személyautók nem termelnek bevételt.
     */
    @Override
    protected void updateIncome() {
    }
}
