package runnableGUI;

import controller.ControllerLogic.Controller;
import controller.InterpreterLogic.CommandLineInterpreter;
import controller.InterpreterLogic.ICommandLineInterpreter;
import runnableCLI.CommandLineRunnable;
import view.DisplayLogic.GameWindow;
import view.DisplayLogic.PlayerDialog;
import view.DisplayLogic.StartDialog;

import javax.swing.SwingUtilities;

public class GraphicalRunnable {

    public static void main(String[] args) {
        Controller controller = new Controller();
        //ICommandLineInterpreter interpreter = new CommandLineInterpreter(controller);
        //CommandLineRunnable cli = new CommandLineRunnable(interpreter, controller);

        SwingUtilities.invokeLater(() -> {
            StartDialog dialog = new StartDialog(controller);
            dialog.setVisible(true);

            if (dialog.isLoaded()) {
                PlayerDialog playerDialog = new PlayerDialog(controller);
                playerDialog.setVisible(true);

                if (playerDialog.isConfirmed()) {
                    GameWindow window = new GameWindow(controller);
                    window.setVisible(true);
                }
            }
        });

        //cli.run();
    }

}
