package proto;

// HASZNÁLJA AZ INTERPRETER ÁLTAL NYÚJTOTT INTERFACET

public class CLIProto {

    private final ICommandLineInterpreter parser;

    public CLIProto(ICommandLineInterpreter _parser) {
        this.parser = _parser;
    }

    public static void main(String[] args) {
        ICommandLineInterpreter interpreter = new CommandLineInterpreter();
        CLIProto app = new CLIProto(interpreter);
        app.run();
    }

    /** A programot futtató függvény **/
    private void run() {
        while (true) {
            parser.parse();
            // TODO
        }
    }

    /** A játék aktuális állapotát és fázisát megjelenítő függvények**/
    // Ezekhez kéne majd a control layertől egy interface
    private void displayInitMenu() { }
    private void displayCurrentRound() { }
}
