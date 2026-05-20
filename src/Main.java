import controller.Controller;
import proto.CLIProto;
import proto.CommandLineInterpreter;
import proto.ICommandLineInterpreter;
import view.GameWindow;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        ICommandLineInterpreter interpreter = new CommandLineInterpreter(controller);
        CLIProto cli = new CLIProto(interpreter, controller);

        // GUI az EDT-n – a Controller közös, a CLI és a View ugyanazt az állapotot olvassák
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow(controller);
            window.setVisible(true);
        });

        // CLI a main szálon – blokkoló olvasás a stdin-ről
        cli.run();
    }
}