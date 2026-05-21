package runnableGUI;

import controller.ControllerLogic.Controller;
import controller.InterpreterLogic.CommandLineInterpreter;
import controller.InterpreterLogic.ICommandLineInterpreter;
import runnableCLI.CommandLineRunnable;
import view.DisplayLogic.GameWindow;

import javax.swing.SwingUtilities;

public class GraphicalRunnable {

    public static void main(String[] args) {
        Controller controller = new Controller();
        ICommandLineInterpreter interpreter = new CommandLineInterpreter(controller);
        CommandLineRunnable cli = new CommandLineRunnable(interpreter, controller);

        SwingUtilities.invokeLater(() -> {
            GameWindow window = new GameWindow(controller);
            window.setVisible(true);
        });

        cli.run();
    }

}
