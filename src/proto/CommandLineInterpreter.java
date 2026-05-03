package proto;

import controller.AttachmentType;
import controller.IController;
import controller.Message;
import map.Lane;

import java.io.File;
import java.util.Scanner;

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
        System.out.println(workingDirectory.getName() + "  ");

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

            case "help":

                if(_args.length == 2)
                message = new Message.RequestHelp(_args[1]);
                else if(_args.length == 1)
                message = new Message.RequestHelp(_args[0]);
            break;

            case "addjunction":
                if (_args.length == 2){
                    message = new Message.AddJunction(_args[1]);
                } else System.out.println("Hibás bemenet, helyes használat: addjunction név\n");
            break;

            case "addroad":
                if(_args.length != 3)
                    System.out.println("Hibás bemenet, helyes használat: addroad j1 j2\n");
                message = new Message.AddRoad(_args[1], _args[2]);
            break;

            case "addnpccar":
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

            case "picklane":
                if(_args.length != 2){
                    System.out.println("Helytelen bemenet, érvényes: picklane melyikre");
                }

                Lane selectedLane = null;

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
        }


        if (message != null) {
            controller.receive(message);
        }

    }

    // ------------------------------------ //


    private void exitProgram() {
        System.out.println("Kilépés...");
        System.exit(0);
    }


    /** EZEK A FÜGGVÉNYEK A CONTROL LAYER INTERFACE-ÉN KERESZTÜL MŰKÖDNEK **/
    private void help() { }
    private void helpGame() { }
    private void helpConf() { }
    private void helpConfFormat() { }
    private void helpTest() { }

    private void randomoff() { }
    private void randomon() { }

    private void startGame() { }

    private void load() { }
    private void save() { }

    private void clear() { }
    private void modeTest() { }
    private void modeUser() { }

    private void carcount() { }

    private void addPLayer() {

    }
    private void rmPlayer() { }

    private void pick() { }
    private void pickClean() { }

    private void swap() { }
    private void buy() { }
    private void refill() { }

    private void snapshot() { }
    private void state() { }
}

