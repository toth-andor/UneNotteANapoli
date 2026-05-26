package controller.PlayerLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * A PlayerDirectory osztály felelős a játékosok és járműveik nyilvántartásáért.
 * Külön listákban tárolja a különböző típusú járműveket, és nyilvántartja
 * a soron következő játékos indexét.
 */
public class PlayerDirectory {
    private int roundNumber;
    private List<Player> players;

    private int currentPlayerIndex;

    public PlayerDirectory() {
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.roundNumber = 0;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player nextPlayer() {
        int originalIndex = currentPlayerIndex;
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            if (currentPlayerIndex == 0) {
                roundNumber++;
            }
            if (!isPlayerImmobile(players.get(currentPlayerIndex))) {
                return players.get(currentPlayerIndex);
            }
        } while (currentPlayerIndex != originalIndex);
        
        return players.get(currentPlayerIndex);
    }

    private boolean isPlayerImmobile(Player player) {
        if (player.getType() instanceof PlayerType.PCleaner c) {
            for (vehicle.SnowPlow sp : c.cleaner().getSnowPlows()) {
                if (!sp.isImmobile(roundNumber)) return false;
            }
            return !c.cleaner().getSnowPlows().isEmpty();
        } else if (player.getType() instanceof PlayerType.PBusDriver b) {
            return b.bus().isImmobile(roundNumber);
        }
        return false;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean isLastPlayer() {
        return currentPlayerIndex == players.size() - 1;
    }

    public int getRoundNumber() {
        return roundNumber;
    }
}
