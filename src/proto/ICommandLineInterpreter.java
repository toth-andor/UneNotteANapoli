package proto;

/**
 * A parancssori interpreter interfésze.
 * Az implementáló osztály (CommandLineInterpreter) felelős a felhasználói
 * bemenet beolvasásáért, parancsokra bontásáért, Message objektumok
 * létrehozásáért és azok controller.receive(msg) hívással való továbbításáért.
 */
public interface ICommandLineInterpreter {

    /**
     * Beolvas egy sort a standard inputról, értelmezi a parancsot,
     * és továbbítja a Controller felé a megfelelő Message objektumként.
     *
     * @param tick a játék aktuális időbélyege — minden körben eggyel növekszik,
     *             a Vehicle.gotoLane(lane, tick) híváshoz szükséges
     */
    void parse(int tick);
}
