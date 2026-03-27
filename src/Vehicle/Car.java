package Vehicle;

/**
 * Egy gépi vezérlésű személyautót reprezentál, amely a legrövidebb járható úton közlekedik
 * két célpont között. Hozzájárul a sávok letaposásához és ezáltal a jégesedéshez, de nem
 * termel bevételt és nem tart nyilván pontszámot.
 */
public class Car extends Commuter {

    /**
     * Üres implementáció, mivel a személyautók nem termelnek bevételt.
     */
    @Override
    protected void updateIncome() {}
}
