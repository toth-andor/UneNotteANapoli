package controller.PlayerLogic;

public class Player {

    private PlayerType type;
    private String name;

    public Player(PlayerType type, String name) {
        this.type = type;
        this.name = name;
    }

    public PlayerType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
