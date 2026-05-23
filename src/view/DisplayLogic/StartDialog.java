package view.DisplayLogic;

import controller.ControllerLogic.IController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A játék indításakor megjelenő modális párbeszédablak.
 * Lehetővé teszi a játékos számára, hogy a {@code config/} mappából
 * kiválasszon egy térképkonfigurációs fájlt, amelyet a játék betölt.
 *
 * <p>Ha a játékos bezárja az ablakot vagy a „Kilépés" gombra kattint,
 * az alkalmazás azonnal leáll. Sikeres betöltés után az ablak bezárul,
 * és a hívó {@link #isLoaded()} segítségével lekérdezheti az eredményt.</p>
 */
public class StartDialog extends JDialog {

    /** Jelzi, hogy a játékos sikeresen betöltött-e egy térképet. */
    private boolean loaded = false;

    /**
     * Létrehozza és felépíti a párbeszédablakot.
     *
     * @param controller a betöltési logikát megvalósító kontroller;
     *                   a kiválasztott fájl tartalmát ide adja át a dialog
     */
    public StartDialog(IController controller) {
        // Modális dialog, nincs szülőablak; megnyitásig blokkol az EDT-n
        super((Frame) null, "Une Notte a Napoli – Térkép betöltése", true);

        // Az X gombra ne záródjon be magától – a windowClosing kezeli
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // Ablak bezárásakor kilép az alkalmazásból
                System.exit(0);
            }
        });

        // A fájlválasztó alapértelmezetten a config/ mappát nyitja meg;
        // ha az nem létezik, a munkamappa lesz a kiindulópont
        File configDir = new File(System.getProperty("user.dir"), "config");
        JFileChooser fileChooser = new JFileChooser(configDir.exists() ? configDir : new File(System.getProperty("user.dir")));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // A JFileChooser saját OK/Mégse gombjait elrejtjük – mi adjuk a gombokat
        fileChooser.setControlButtonsAreShown(false);

        // Fejléc felirat az ablak tetején
        JLabel header = new JLabel("Válassz térképfájlt a config mappából:", SwingConstants.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 14));
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JButton loadBtn = new JButton("Betöltés");
        JButton exitBtn = new JButton("Kilépés");
        loadBtn.setPreferredSize(new Dimension(100, 28));
        exitBtn.setPreferredSize(new Dimension(100, 28));

        loadBtn.addActionListener(e -> {
            File selected = fileChooser.getSelectedFile();

            // Ha nincs kiválasztott fájl, figyelmeztet és nem lép tovább
            if (selected == null || !selected.isFile()) {
                JOptionPane.showMessageDialog(this,
                        "Kérlek válassz egy fájlt!",
                        "Figyelem",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                // Beolvassa a fájl tartalmát és átadja a kontrollernek
                String cfg = new String(Files.readAllBytes(selected.toPath()));
                controller.loadConfig(cfg);
                loaded = true;   // siker jelzése az isLoaded()-nek
                dispose();       // bezárja a dialogot, folytatódik a hívó
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Nem sikerült betölteni: " + ex.getMessage(),
                        "Hiba",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        // Kilépés gomb: azonnali programleállás
        exitBtn.addActionListener(e -> System.exit(0));

        // Gombok panel – jobbra igazítva az ablak aljára
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        buttons.add(exitBtn);
        buttons.add(loadBtn);

        // Tartalom összerakása: fejléc felül, fájlválasztó középen, gombok alul
        JPanel content = new JPanel(new BorderLayout());
        content.add(header, BorderLayout.NORTH);
        content.add(fileChooser, BorderLayout.CENTER);
        content.add(buttons, BorderLayout.SOUTH);

        setContentPane(content);
        setSize(620, 480);
        setLocationRelativeTo(null); // képernyő közepére helyezi az ablakot
    }

    /**
     * @return {@code true}, ha a játékos sikeresen betöltött egy térképet;
     *         {@code false}, ha a dialog betöltés nélkül záródott be
     */
    public boolean isLoaded() {
        return loaded;
    }
}
