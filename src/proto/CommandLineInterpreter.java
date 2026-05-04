package proto;

import controller.AttachmentType;
import Vehicle.Cleaner;
import controller.IController;
import controller.Message;
import controller.Player;
import controller.PlayerDirectory;
import controller.PlayerType;
import map.Lane;
import map.OutdoorLane;
import map.Road;
import states.CrashedState;
import states.DryState;
import states.GraveledState;
import states.IcyState;
import states.SaltedState;
import states.SnowyState;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;

import Vehicle.Car;
import Vehicle.SnowPlow;

// HASZNÁLJA A CONTROL LAYER ÁLTAL NYÚJTOTT INTERFACET

public class CommandLineInterpreter implements ICommandLineInterpreter {

    private final File workingDirectory;
    private  final Scanner scanner;
    private final IController controller;

    public CommandLineInterpreter(IController ctrl) {
        workingDirectory = new File(System.getProperty("user.dir"));
        scanner = new Scanner(System.in);
        this.controller = ctrl;
    }
    @Override
    public void parse(int param) {
        System.out.print(workingDirectory.getName() + "  $  ");

        String currentLine = scanner.nextLine();
        if(currentLine.trim().isEmpty()) {
            return;
        }

        String[] splitLine = currentLine.split(" ");
        String command = splitLine[0];

        handleCommand(command, splitLine);
    }

    private void handleCommand(String _command, String[] _args) {
        Message message = null;

        switch(_command.toLowerCase()){

            case "exit":
                exitProgram();
                break;

            case "help":

                if(_args.length == 2)
                message = new Message.RequestHelp(_args[1]);
                else if(_args.length == 1)
                message = new Message.RequestHelp(_args[0]);
            break;

            case "addjunction":
                if (_args.length == 2){
                    message = new Message.AddJunction(Integer.parseInt(_args[1]));
                } else System.out.println("Hibás bemenet, helyes használat: addjunction count\n");
            break;

            case "addroad":
                if(_args.length != 3)
                    System.out.println("Hibás bemenet, helyes használat: addroad j1 j2\n");
                else
                    message = new Message.AddRoad(_args[1], _args[2]);
            break;

            case "savemap":
                if(_args.length != 1) {
                    System.out.println("Hibás bemenet, helyes használat: savemap\n");
                } else {
                    message = new Message.SaveMap();
                }
                break;

            case "carcount":
                if (_args.length != 2){
                    System.out.println("Helytelen formátum, helyes használat: addNPCcar darabszám");
                    break;
                }
                int temp = Integer.parseInt(_args[1]);
                message = new Message.AddNPCCar(temp);
                break;

            case "addplayer":
                if(_args[1].equalsIgnoreCase("cleaner"))
                    message = new Message.AddCleaner(_args[2]);
                else if(_args[1].equals("bus"))
                    message = new Message.AddBusDriver(_args[2]);
            break;

            case "removeplayer":
                //message-ben nincs hozzá
                break;

            case "buy":
                if(_args[1].equalsIgnoreCase("snowplow")){
                    message = new Message.BuySnowPlow();
                } else if(_args[1].equalsIgnoreCase("attachment") && _args.length == 3){
                    try {
                        AttachmentType type = AttachmentType.valueOf(_args[2].toUpperCase());
                        message = new Message.BuyAttachment(type);
                    }
                    catch (IllegalArgumentException ex) {
                        System.out.println("Nem létezik ilyen fej: " + _args[2] + "\n");
                    }
                }
            break;

            case "swapattachment":
                if(_args.length != 2){
                    System.out.println("Hibás bemenet, helyes formátum: swapattachment mire");
                }
                try {
                    AttachmentType type = AttachmentType.valueOf(_args[1].toUpperCase());
                    message = new Message.SwapAttachment(type);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Nem letezik ilyen fej: " + _args[1] + "\n");
                }
            break;

            case "refillattachment":
                message = new Message.RefillAttachment();
                break;

            case "pick":
                if(_args.length != 2 && _args.length != 3){
                    System.out.println("Helytelen bemenet, érvényes: picklane melyikre");
                }
                Lane selectedLane = controller.getMapModel().getLaneByName(_args[1]);
                message = new Message.PickLane(selectedLane);
                break;

            case "save":
                if (_args.length == 2) {
                    message = new Message.SaveGame(_args[1]);
                } else {
                    System.out.println("Helytelen formátum, helyes: save fájlnév");
                }
                break;

            case "load":
                if (_args.length == 2) {
                    message = new Message.LoadGame(_args[1]);
                } else {
                    System.out.println("Helytelen formátum, helyes: load fájlnév");
                }
                break;
            case "snapshot":
                if (_args.length == 2) {
                    writeSnapshot(_args[1]);
                } else {
                    System.out.println("Helyes használat: snapshot <fájlútvonal>");
                }
                break;

            case "start":
                if (_args.length == 1) {
                    message = new Message.StartGame();
                }
                break;

            case "randomoff":
                if (_args.length == 2) {
                    try {
                        message = new Message.RandomOff(Integer.parseInt(_args[1]));
                    } catch (NumberFormatException e) {
                        System.out.println("Hibás seed érték: " + _args[1]);
                    }
                } else {
                    System.out.println("Helytelen formátum, helyes: randomoff <seed>");
                }
                break;

            case "randomon":
                message = new Message.RandomOn();
                break;
        }


        if (message != null) {
            controller.receive(message);
        }

    }

    private void exitProgram() {
        System.out.println("Kilépés...");
        System.exit(0);
    }

    private void writeSnapshot(String path) {
        try (PrintWriter out = new PrintWriter(new FileWriter(path))) {
            int round = controller.getRoundNumber();
            out.println("[SNAPSHOT OF ROUND " + round + "]");
            out.println();

            // @global
            PlayerDirectory dir = controller.getPlayers();
            List<Player> players = dir.getPlayers();
            Player current = dir.getCurrentPlayer();
            int nextIdx = (dir.getCurrentPlayerIndex() + 1) % players.size();
            Player next = players.get(nextIdx);

            out.println("@global");
            out.println("ROUND_ID " + round);
            out.println("CURRENT_PLAYER " + current.getName() + " ROLE " + roleName(current));
            out.println("NEXT_PLAYER " + next.getName() + " ROLE " + roleName(next));
            out.println();

            // @players
            out.println("@players");
            for (Player p : players) {
                out.println("PLAYER " + p.getName() + " ROLE " + roleName(p) + " SCORE " + score(p));
            }
            out.println();

            // @positions
            out.println("@positions");
            for (Player p : players) {
                Lane lane = vehicleLane(p);
                if (lane != null) {
                    Road road = lane.getRoad();
                    String roadName = road != null ? road.getName() : "?";
                    out.println("POSITION " + p.getName()
                            + " ROAD " + roadName
                            + " LANE " + lane.getName()
                            + " STATE " + stateName(lane));
                }
            }
            out.println();

            // @lanes
            out.println("@lanes");
            for (Road road : controller.getMapModel().getRoads()) {
                for (Lane lane : road.getLanes()) {
                    String type = lane instanceof OutdoorLane ? "outdoor" : "tunnel";
                    out.println("LANE " + lane.getName()
                            + " ROAD " + road.getName()
                            + " TYPE " + type
                            + " STATE " + stateName(lane)
                            + " AVAILABLE " + isAvailable(lane));
                }
            }
            out.println();

            // @attachments
            out.println("@attachments");
            for (Player p : players) {
                if (p.getType() instanceof PlayerType.PCleaner pc) {
                    Cleaner cleaner = pc.cleaner();
                    int spIdx = 0;
                    for (SnowPlow sp : cleaner.getSnowPlows()) {
                        for (var tool : sp.getOwnedTools()) {
                            out.println("ATTACHMENT " + p.getName()
                                    + " SNOWPLOW " + spIdx
                                    + " TOOL " + tool.getClass().getSimpleName());
                        }
                        spIdx++;
                    }
                }
            }
            out.println();

            // @cars
            out.println("@cars");
            for (Car car : controller.getNpcHandler().getNpcCars()) {
                Lane lane = car.getCurrentLane();
                if (lane != null) {
                    Road road = lane.getRoad();
                    String roadName = road != null ? road.getName() : "?";
                    out.println("CAR ROAD " + roadName
                            + " LANE " + lane.getName()
                            + " STATE " + stateName(lane));
                }
            }
            out.println();

            out.println("[END SNAPSHOT]");
            System.out.println("Snapshot mentve: " + path);
        } catch (IOException e) {
            System.out.println("Snapshot hiba: " + e.getMessage());
        }
    }

    private String roleName(Player p) {
        return switch (p.getType()) {
            case PlayerType.PCleaner c -> "cleaner";
            case PlayerType.PBusDriver b -> "bus";
        };
    }

    private int score(Player p) {
        return switch (p.getType()) {
            case PlayerType.PCleaner c -> c.cleaner().getScore();
            case PlayerType.PBusDriver b -> b.bus().getScore();
        };
    }

    private Lane vehicleLane(Player p) {
        return switch (p.getType()) {
            case PlayerType.PCleaner c -> c.cleaner().getSnowPlows().isEmpty()
                    ? null : c.cleaner().getSnowPlows().get(0).getCurrentLane();
            case PlayerType.PBusDriver b -> b.bus().getCurrentLane();
        };
    }

    private String stateName(Lane lane) {
        if (lane instanceof OutdoorLane ol) {
            if (ol.getCurrentState() instanceof DryState)     return "dry";
            if (ol.getCurrentState() instanceof SnowyState)   return "snowy";
            if (ol.getCurrentState() instanceof IcyState)     return "icy";
            if (ol.getCurrentState() instanceof SaltedState)  return "salted";
            if (ol.getCurrentState() instanceof CrashedState) return "crashed";
            if (ol.getCurrentState() instanceof GraveledState) return "graveled";
        }
        return "dry";
    }

    private boolean isAvailable(Lane lane) {
        if (lane instanceof OutdoorLane ol) {
            return ol.isNavigable() && !(ol.getCurrentState() instanceof CrashedState);
        }
        return true;
    }
}

