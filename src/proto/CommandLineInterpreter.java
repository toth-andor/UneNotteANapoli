package proto;

import java.io.File;
import java.util.Scanner;

// HASZNÁLJA A CONTROL LAYER ÁLTAL NYÚJTOTT INTERFACET

public class CommandLineInterpreter implements ICommandLineInterpreter {

    private final File workingDirectory;
    private  final Scanner scanner;

    public CommandLineInterpreter() {
        workingDirectory = new File(System.getProperty("user.dir"));
        scanner = new Scanner(System.in);
    }

    @Override
    public void parse() {
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
        if(_command.equalsIgnoreCase("exit")) {
            exitProgram();
        }
        // TODO
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

    private void addPLayer() { }
    private void rmPlayer() { }

    private void pick() { }
    private void pickClean() { }

    private void swap() { }
    private void buy() { }
    private void refill() { }

    private void snapshot() { }
    private void state() { }
}

