package controller;

/**
 * A rendszer központi osztálya, amely a State tervezési mintát alkalmazva
 * állapotgépként működik. Kontextusként tárolja a játék aktuális állapotát,
 * és az IController interfész megvalósítójaként az érkező üzeneteket továbbítja
 * a jelenlegi GameState példánynak.
 */
public class Controller implements IController {

    /** A játéktérkép (gráf) és a modell állapotának összefogója */
    private IMapModel mapModel;

    /** A regisztrált játékosok listája */
    private PlayerDirectory playerDirectory;

    /** Az állapotgép aktuális állapota */
    private GameState gameState;

    /** Az NPC autók mozgásáért felelős segédkomponens */
    private NPCHandler npcHandler;

    /**
     * Létrehoz egy új Controller példányt.
     */
    public Controller() {
        this.playerDirectory = new PlayerDirectory();
        this.gameState = new SetupState(this);
        this.npcHandler = new NPCHandler();
    }

    /**
     * A külső felületekről (View/Proto) érkező üzenetek fogadása és továbbítása
     * a gameState.process(msg) hívással.
     *
     * @param msg a feldolgozandó üzenet
     */
    @Override
    public void receive(Message msg) {
        gameState = gameState.process(msg);
    }

    /**
     * Soronként feldolgozza a kapott konfigot, és minden sorhoz hív egy
     * receive(msg)-t a megfelelő Message-dszel.
     *
     * @param cfg a konfigurációs fájl tartalma
     */
    @Override
    public void loadConfig(String cfg) {
        // TODO: Implementálás a konfigurációs fájl feldolgozásának logikája
    }

    // TODO: lehet jobb lenne a havazást és az npc-ket felcserélni
    /**
     * Végrehajtja a rendszerfázis logikáját:
     * 1. Havazás
     * 2. NPC autók mozgatása
     */
    public void endOfTurn() {
        mapModel.snow(1);

        // 2. NPC autók mozgatása
        if (npcHandler != null) {
            npcHandler.moveNPCs();
        }
    }

    // Getterek és setterek a komponensek eléréséhez

    public IMapModel getMapModel() {
        return mapModel;
    }

    public void setMapModel(IMapModel mapModel) {
        this.mapModel = mapModel;
    }

    public PlayerDirectory getPlayers() {
        return playerDirectory;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public NPCHandler getNpcHandler() {
        return npcHandler;
    }

    @Override
    public int getRoundNumber() {
        return 0;
    }

}
