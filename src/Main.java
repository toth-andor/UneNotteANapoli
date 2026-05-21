import controller.ControllerLogic.Controller;
import controller.InterpreterLogic.CommandLineInterpreter;
import controller.InterpreterLogic.ICommandLineInterpreter;
import runnableCLI.CommandLineRunnable;
import view.DisplayLogic.GameWindow;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        Controller controller = new Controller();
        ICommandLineInterpreter interpreter = new CommandLineInterpreter(controller);
        CommandLineRunnable cli = new CommandLineRunnable(interpreter, controller);

        // GUI az EDT-n – a Controller közös, a CLI és a View ugyanazt az állapotot olvassák
        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow(controller);
            window.setVisible(true);
        });

        // CLI a main szálon – blokkoló olvasás a stdin-ről
        cli.run();
    }
}