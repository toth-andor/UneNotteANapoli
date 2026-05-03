package controller;

/**
 * Az állapotgép állapotainak közös absztrakt ősosztálya.
 * Definiálja az üzenetek feldolgozásának keretrendszerét.
 */
public abstract class GameState {

    /**
     * Visszamutató referencia a kontextusra az állapotátmenetekhez.
     */
    protected Controller controller;

    /**
     * @param controller a vezérlő kontextus
     */
    public GameState(Controller controller) {
        this.controller = controller;
    }

    /**
     * Absztrakt metódus az üzenetek feldolgozására.
     * Visszatérési értéke a következő állapot.
     *
     * @param msg a feldolgozandó üzenet
     * @return a következő állapot
     */
    public abstract GameState process(Message msg);
}
