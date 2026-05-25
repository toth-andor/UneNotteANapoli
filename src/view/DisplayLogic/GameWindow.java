package view.DisplayLogic;

import attachments.Attachment;
import controller.AttachmentLogic.AttachmentType;
import controller.ControllerLogic.IController;
import controller.MessageLogic.Message;
import controller.PlayerLogic.Player;
import controller.PlayerLogic.PlayerType;
import controller.StateLogic.BusActionState;
import controller.StateLogic.CleanerActionState;
import controller.StateLogic.GameState;
import controller.StateLogic.SetupState;
import vehicle.SnowPlow;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;

public class GameWindow extends JFrame {

    private final IController controller;

    private JLabel roundLabel;
    private JLabel currentPlayerLabel;
    private JLabel typeLabel;
    private JLabel scoreLabel;
    private JLabel nextPlayerLabel;
    private JPanel actionPanel;

    private Class<?> lastStateClass = null;

    public GameWindow(IController controller) {
        this.controller = controller;

        setTitle("Une Notte a Napoli");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        add(new GamePanel(controller), BorderLayout.CENTER);
        add(buildSidePanel(), BorderLayout.EAST);

        setPreferredSize(new Dimension(1050, 720));
        pack();
        setLocationRelativeTo(null);

        new Timer(250, e -> updateSidePanel()).start();
    }

    private JPanel buildSidePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(190, 0));
        panel.setBackground(new Color(40, 40, 40));

        panel.add(buildInfoPanel(), BorderLayout.NORTH);

        actionPanel = new JPanel();
        actionPanel.setBackground(new Color(40, 40, 40));
        panel.add(actionPanel, BorderLayout.CENTER);

        panel.add(buildLegend(), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel buildInfoPanel() {
        JPanel info = new JPanel();
        info.setLayout(new BoxLayout(info, BoxLayout.Y_AXIS));
        info.setBackground(new Color(40, 40, 40));
        info.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        roundLabel        = makeLabel("", Font.BOLD,  13);
        currentPlayerLabel = makeLabel("", Font.BOLD,  12);
        typeLabel         = makeLabel("", Font.PLAIN, 11);
        scoreLabel        = makeLabel("", Font.PLAIN, 11);
        nextPlayerLabel   = makeLabel("", Font.ITALIC, 11);

        info.add(roundLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(currentPlayerLabel);
        info.add(typeLabel);
        info.add(scoreLabel);
        info.add(Box.createVerticalStrut(4));
        info.add(nextPlayerLabel);
        return info;
    }

    private JLabel makeLabel(String text, int style, int size) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(200, 200, 200));
        l.setFont(new Font("SansSerif", style, size));
        return l;
    }

    private void updateSidePanel() {
        GameState state = controller.getGameState();
        updateInfoLabels(state);
        if (state.getClass() != lastStateClass) {
            lastStateClass = state.getClass();
            rebuildActionButtons(state);
        }
        String titleState = state instanceof SetupState
            ? "Beállítás"
            : controller.getRoundNumber() + ". kör";
        setTitle("Une Notte a Napoli  –  " + titleState);
    }

    private void updateInfoLabels(GameState state) {
        if (state instanceof SetupState) {
            roundLabel.setText("Beállítás");
            currentPlayerLabel.setText("Játékosok: " + controller.getPlayers().getPlayers().size());
            typeLabel.setText("");
            scoreLabel.setText("");
            nextPlayerLabel.setText("");
            return;
        }

        roundLabel.setText(controller.getRoundNumber() + ". kör");

        if (controller.getPlayers().isEmpty()) return;

        Player cp = controller.getPlayers().getCurrentPlayer();
        currentPlayerLabel.setText(cp.getName());

        String typeName = switch (cp.getType()) {
            case PlayerType.PCleaner  ignored -> "Takarító";
            case PlayerType.PBusDriver ignored -> "Buszvezető";
        };
        typeLabel.setText("Típus: " + typeName);

        int score = switch (cp.getType()) {
            case PlayerType.PCleaner  p -> p.cleaner().getScore();
            case PlayerType.PBusDriver p -> p.bus().getScore();
        };
        scoreLabel.setText("Egyenleg: " + score + " Ft");

        List<Player> players = controller.getPlayers().getPlayers();
        int nextIdx = (controller.getPlayers().getCurrentPlayerIndex() + 1) % players.size();
        nextPlayerLabel.setText("Köv.: " + players.get(nextIdx).getName());
    }

    private String getAttachmentName(AttachmentType type) {
        return switch (type) {
            case SWEEPER -> "Seprő fej";
            case VOMITING_HEAD -> "Hányófej";
            case STONE_VOMITTER -> "Zúzalékszóró";
            case SALT_VOMITTER -> "Sószóró";
            case ICE_BREAKER -> "Jégtörő";
            case DRAGON -> "Sárkány";
            default -> "Ismeretlen fej";
        };
    }

    private void addDisabledButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(170, 26));
        btn.setBackground(new Color(40, 40, 50));
        btn.setForeground(new Color(100, 100, 100));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setEnabled(false);
        actionPanel.add(btn);
        actionPanel.add(Box.createVerticalStrut(3));
    }
    private int getAttachmentPrice(AttachmentType type) {
        return switch (type) {
            case SWEEPER -> 1;
            case VOMITING_HEAD -> 0;
            case STONE_VOMITTER -> 5;
            case SALT_VOMITTER -> 5;
            case ICE_BREAKER -> 3;
            case DRAGON -> 10;
            default -> 0;
        };
    }
    private void rebuildActionButtons(GameState state) {
        actionPanel.removeAll();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));

        if (state instanceof CleanerActionState s) {
            SnowPlow currentPlow = s.getCleaner().getSnowPlows().get(s.getCurrentSnowPlowIdx());

            JLabel header = makeLabel("Hókotró " + (s.getCurrentSnowPlowIdx() + 1)
                    + "/" + s.getCleaner().getSnowPlows().size(), Font.BOLD, 11);
            header.setAlignmentX(Component.CENTER_ALIGNMENT);
            actionPanel.add(header);
            actionPanel.add(Box.createVerticalStrut(6));

            addButton("Hókotró vásárlása", () -> controller.receive(new Message.BuySnowPlow()));

            for (AttachmentType type : AttachmentType.values()) {
                boolean ownsHead = currentPlow.getOwnedTools().stream()
                        .anyMatch(tool -> tool.getType() == type);

                boolean isEquipped = currentPlow.getCurrentTool() != null &&
                        currentPlow.getCurrentTool().getType() == type;

                int price = getAttachmentPrice(type); // Segédmetódusból kérjük az árat

                String buttonText = getAttachmentName(type);

                if (isEquipped) {
                    buttonText += " (Felszerelve)";
                    addDisabledButton(buttonText);
                } else if (ownsHead) {
                    buttonText += " (Felszerelés)";
                    addButton(buttonText, () -> controller.receive(new Message.SwapAttachment(type))); // SwapAttachment!
                } else {
                    buttonText += " - " + price + " Ft";
                    addButton(buttonText, () -> controller.receive(new Message.BuyAttachment(type)));
                }
            }

            actionPanel.add(Box.createVerticalStrut(6));

            Attachment current = currentPlow.getCurrentTool();
            if (current != null && (current.getType() == AttachmentType.SALT_VOMITTER || current.getType() == AttachmentType.STONE_VOMITTER || current.getType() == AttachmentType.DRAGON)) {
                addButton("Utántöltés (1 Ft)", () -> controller.receive(new Message.RefillAttachment()));
            }

            actionPanel.add(Box.createVerticalStrut(10));
            JLabel hint = makeLabel("<html><center>Kattints egy sávra<br>a mozgáshoz</center></html>", Font.ITALIC, 10);
            hint.setAlignmentX(Component.CENTER_ALIGNMENT);
            actionPanel.add(hint);
        }

        actionPanel.revalidate();
        actionPanel.repaint();
    }

    private void addButton(String text, Runnable action) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(170, 26));
        btn.setBackground(new Color(60, 60, 80));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.addActionListener(e -> {
            action.run();
            rebuildActionButtons(controller.getGameState());
        });
        actionPanel.add(btn);
        actionPanel.add(Box.createVerticalStrut(3));
    }

    private JPanel buildLegend() {
        JPanel legend = new JPanel(new GridLayout(0, 1, 1, 1));
        legend.setBackground(new Color(35, 35, 35));
        legend.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 90)),
            "Sávállapotok",
            0, 0,
            new Font("SansSerif", Font.BOLD, 10),
            new Color(170, 170, 170)));

        addRow(legend, new Color(115, 115, 115), "Száraz (OutdoorLane)");
        addRow(legend, new Color(173, 216, 230), "Havas");
        addRow(legend, new Color(65,  105, 225), "Jeges");
        addRow(legend, new Color(210, 170,   0), "Sózott");
        addRow(legend, new Color(139, 100,  20), "Zúzalékos");
        addRow(legend, new Color(200,  20,  50), "Baleset");
        addRow(legend, new Color(75,   75, 100), "Alagút (TunnelLane)");

        JPanel vehicleLegend = new JPanel(new GridLayout(0, 1, 1, 1));
        vehicleLegend.setBackground(new Color(35, 35, 35));
        vehicleLegend.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 90)),
            "Járművek",
            0, 0,
            new Font("SansSerif", Font.BOLD, 10),
            new Color(170, 170, 170)));

        addRow(vehicleLegend, new Color(0,   200, 220), "Hókotró (SnowPlow)");
        addRow(vehicleLegend, new Color(255, 140,   0), "Busz (Bus)");
        addRow(vehicleLegend, new Color(80,  160, 255), "Autó (Car)");

        JPanel highlightLegend = new JPanel(new GridLayout(0, 1, 1, 1));
        highlightLegend.setBackground(new Color(35, 35, 35));
        highlightLegend.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(90, 90, 90)),
            "Kiemelések",
            0, 0,
            new Font("SansSerif", Font.BOLD, 10),
            new Color(170, 170, 170)));

        addRow(highlightLegend, new Color(255,  50, 200), "Indulási csomópont");
        addRow(highlightLegend, new Color(255, 220,   0), "Választható sávok");
        addRow(highlightLegend, new Color(  0, 200,  80), "Busz célállomása");

        JPanel combined = new JPanel(new GridLayout(3, 1));
        combined.setBackground(new Color(35, 35, 35));
        combined.add(legend);
        combined.add(vehicleLegend);
        combined.add(highlightLegend);
        return combined;
    }

    private void addRow(JPanel panel, Color color, String text) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 1));
        row.setBackground(new Color(35, 35, 35));

        JLabel box = new JLabel("  ");
        box.setOpaque(true);
        box.setBackground(color);
        box.setPreferredSize(new Dimension(14, 10));

        JLabel label = new JLabel(text);
        label.setForeground(new Color(190, 190, 190));
        label.setFont(new Font("SansSerif", Font.PLAIN, 10));

        row.add(box);
        row.add(label);
        panel.add(row);
    }
}
