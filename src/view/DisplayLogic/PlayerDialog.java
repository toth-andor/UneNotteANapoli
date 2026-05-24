package view.DisplayLogic;

import controller.ControllerLogic.IController;
import controller.MessageLogic.Message;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Modális párbeszédablak a játékosok hozzáadásához.
 * A StartDialog után jelenik meg, és lehetővé teszi Buszvezetők és
 * Hókotrók (Cleaner) hozzáadását. Bezáráskor az alkalmazás kilép.
 */
public class PlayerDialog extends JDialog {

    private boolean confirmed = false;

    /**
     * @param controller a játék kontrollere, amelynek az üzeneteket küldi
     */
    public PlayerDialog(IController controller) {
        super((Frame) null, "Une Notte a Napoli – Játékosok hozzáadása", true);

        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // --- Hozzáadott játékosok listája ---
        DefaultListModel<String> playerListModel = new DefaultListModel<>();
        JList<String> playerList = new JList<>(playerListModel);
        playerList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane listScrollPane = new JScrollPane(playerList);
        listScrollPane.setPreferredSize(new Dimension(250, 200));
        listScrollPane.setBorder(BorderFactory.createTitledBorder("Hozzáadott játékosok"));

        // --- Játékos hozzáadása panel ---
        JTextField nameField = new JTextField(15);
        JRadioButton busBtn = new JRadioButton("Buszvezető", true);
        JRadioButton cleanerBtn = new JRadioButton("Hókotró (Cleaner)");
        ButtonGroup typeGroup = new ButtonGroup();
        typeGroup.add(busBtn);
        typeGroup.add(cleanerBtn);

        JButton addBtn = new JButton("Hozzáadás");
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);

        addBtn.addActionListener(e -> {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                statusLabel.setText("A név nem lehet üres!");
                return;
            }
            if (busBtn.isSelected()) {
                controller.receive(new Message.AddBusDriver(name));
                playerListModel.addElement("[Buszvezető] " + name);
            } else {
                controller.receive(new Message.AddCleaner(name));
                playerListModel.addElement("[Hókotró] " + name);
            }
            nameField.setText("");
            statusLabel.setText(" ");
        });

        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        typePanel.add(busBtn);
        typePanel.add(cleanerBtn);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder("Új játékos"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 6, 4, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(new JLabel("Név:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(new JLabel("Típus:"), gbc);
        gbc.gridx = 1;
        inputPanel.add(typePanel, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        inputPanel.add(addBtn, gbc);

        gbc.gridy = 3;
        inputPanel.add(statusLabel, gbc);

        // --- Alsó gombok ---
        JButton exitBtn = new JButton("Kilépés");
        JButton startBtn = new JButton("Játék indítása");
        exitBtn.setPreferredSize(new Dimension(130, 28));
        startBtn.setPreferredSize(new Dimension(130, 28));

        exitBtn.addActionListener(e -> System.exit(0));

        startBtn.addActionListener(e -> {
            if (playerListModel.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Legalább egy játékost adj hozzá!",
                        "Figyelem",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            controller.receive(new Message.StartGame());
            confirmed = true;
            dispose();
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttonPanel.add(exitBtn);
        buttonPanel.add(startBtn);

        // --- Elrendezés ---
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        centerPanel.add(listScrollPane);
        centerPanel.add(inputPanel);

        JLabel header = new JLabel("Add hozzá a játékosokat, majd indítsd el a játékot!", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel content = new JPanel(new BorderLayout());
        content.add(header, BorderLayout.NORTH);
        content.add(centerPanel, BorderLayout.CENTER);
        content.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(content);
        setSize(800, 340);
        setLocationRelativeTo(null);
    }

    /**
     * @return {@code true}, ha a játékos sikeresen elindította a játékot
     */
    public boolean isConfirmed() {
        return confirmed;
    }
}
