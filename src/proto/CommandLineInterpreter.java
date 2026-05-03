package proto;

import controller.AttachmentType;
import controller.IController;
import controller.Message;

import java.io.File;
import java.util.Scanner;

// HASZNÁLJA A CONTROL LAYER ÁLTAL NYÚJTOTT INTERFACET

public class CommandLineInterpreter implements ICommandLineInterpreter {

    private final File workingDirectory;
    private final Scanner scanner;
    private final IController controller;

    public CommandLineInterpreter(IController ctrl) {
        workingDirectory = new File(System.getProperty("user.dir"));
        scanner = new Scanner(System.in);
        this.controller = ctrl;
    }

    @Override
    public void parse(int param) {
        System.out.print(workingDirectory.getName() + " > ");

        String currentLine = scanner.nextLine();
        if (currentLine.trim().isEmpty()) {
            return;
        }

        String[] splitLine = currentLine.trim().split("\\s+");
        String command = splitLine[0];

        handleCommand(command, splitLine);
    }

    private void handleCommand(String _command, String[] _args) {
        Message message = null;

        switch (_command.toLowerCase()) {

            case "exit":
                exitProgram();
                break;

            case "help":
                if (_args.length == 2)
                    message = new Message.RequestHelp(_args[1]);
                else
                    message = new Message.RequestHelp("general");
                break;

            case "addjunction":
                if (_args.length == 2) {
                    message = new Message.AddJunction(_args[1]);
                } else {
                    System.out.println("Hibás bemenet, helyes használat: addjunction <név>");
                }
                break;

            case "addroad":
                if (_args.length == 3) {
                    message = new Message.AddRoad(_args[1], _args[2]);
                } else {
                    System.out.println("Hibás bemenet, helyes használat: addroad <j1> <j2>");
                }
                break;

            case "addnpccar":
                if (_args.length != 2) {
                    System.out.println("Helytelen formátum, helyes használat: addnpccar <darabszám>");
                    break;
                }
                try {
                    int count = Integer.parseInt(_args[1]);
                    message = new Message.AddNPCCar(count);
                } catch (NumberFormatException e) {
                    System.out.println("Hibás szám: " + _args[1]);
                }
                break;

            case "addplayer":
                if (_args.length < 3) {
                    System.out.println("Hibás bemenet, helyes használat: addplayer <bus|cleaner> <név>");
                    break;
                }
                if (_args[1].equalsIgnoreCase("cleaner")) {
                    message = new Message.AddCleaner(_args[2]);
                } else if (_args[1].equalsIgnoreCase("bus")) {
                    message = new Message.AddBusDriver(_args[2]);
                } else {
                    System.out.println("Ismeretlen szerepkör: " + _args[1] + " (érvényes: bus, cleaner)");
                }
                break;

            case "removeplayer":
                System.out.println("A removeplayer parancs még nincs implementálva.");
                break;

            case "start":
                message = new Message.StartGame();
                break;

            case "randomon":
                message = new Message.RandomOn();
                break;

            case "randomoff":
                if (_args.length == 2) {
                    try {
                        int seed = Integer.parseInt(_args[1]);
                        message = new Message.RandomOff(seed);
                    } catch (NumberFormatException e) {
                        System.out.println("Hibás seed érték: " + _args[1]);
                    }
                } else {
                    System.out.println("Helyes használat: randomoff <seed>");
                }
                break;

            case "buy":
                if (_args.length < 2) {
                    System.out.println("Helyes használat: buy <snowplow|attachment <típus>>");
                    break;
                }
                if (_args[1].equalsIgnoreCase("snowplow")) {
                    message = new Message.BuySnowPlow();
                } else if (_args[1].equalsIgnoreCase("attachment") && _args.length == 3) {
                    try {
                        AttachmentType type = AttachmentType.valueOf(_args[2].toUpperCase());
                        message = new Message.BuyAttachment(type);
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Nem létezik ilyen fej: " + _args[2]);
                    }
                } else {
                    System.out.println("Helyes használat: buy <snowplow|attachment <típus>>");
                }
                break;

            case "swap":
            case "swapattachment":
                if (_args.length != 2) {
                    System.out.println("Hibás bemenet, helyes formátum: swap <fej>");
                    break;
                }
                try {
                    AttachmentType type = AttachmentType.valueOf(_args[1].toUpperCase());
                    message = new Message.SwapAttachment(type);
                } catch (IllegalArgumentException ex) {
                    System.out.println("Nem létezik ilyen fej: " + _args[1]);
                }
                break;

            case "refill":
            case "refillattachment":
                message = new Message.RefillAttachment();
                break;

            case "pick":
            case "picklane":
                if (_args.length < 2) {
                    System.out.println("Helyes használat: pick <sáv>");
                    break;
                }
                System.out.println("A sávválasztás névalapú keresése még nincs implementálva.");
                break;

            case "save":
                if (_args.length == 2) {
                    message = new Message.SaveGame(_args[1]);
                } else {
                    System.out.println("Helyes formátum: save <fájlnév>");
                }
                break;

            case "load":
                if (_args.length == 2) {
                    message = new Message.LoadGame(_args[1]);
                } else {
                    System.out.println("Helyes formátum: load <fájlnév>");
                }
                break;

            default:
                System.out.println("Ismeretlen parancs: " + _command + " (help a súgóhoz)");
                break;
        }

        if (message != null) {
            int playersBefore = controller.getPlayers().getPlayers().size();
            controller.receive(message);
            int playersAfter = controller.getPlayers().getPlayers().size();

            if (playersAfter > playersBefore) {
                System.out.println("Játékos sikeresen hozzáadva. (összesen: " + playersAfter + ")");
            }
        }
    }

    private void exitProgram() {
        System.out.println("Kilépés...");
        System.exit(0);
    }
}
