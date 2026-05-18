package view.DisplayLogic;

import javax.swing.JFrame;

import controller.ControllerLogic.IController;
import controller.MessageLogic.Message;

public class GameWindow extends JFrame {

    private final GamePanel panel;
    private final IController controller;

    public GameWindow(IController controller) {
        this.controller = controller;
        this.panel = new GamePanel();
        initUI();
    }

    public void initUI() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.add(panel);
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);

        // Itt jönnek majd az Event Handlerek (Key/Mouse Listener), amik hívják a dispatchMessage-et

        this.setVisible(true);
    }

    private void dispatchMessage(Message msg) {
        // Event Handlerek hívják meg, üzenetet küld a kontrollernek
        if (controller != null) {
            controller.receive(msg);
        }
    }
}