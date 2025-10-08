package gg.paceman.tracker.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import gg.paceman.tracker.PaceManTracker;
import gg.paceman.tracker.PaceManTrackerOptions;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class PaceManTrackerGUI extends JFrame {
    private static PaceManTrackerGUI instance = null;
    private static final boolean RESET_STATS_OPTION_USABLE = true;
    public JCheckBox enabledCheckBox;
    private JPanel mainPanel;
    private JButton saveButton;
    private JCheckBox resetStatsEnabled;
    private JLabel storagePathLabel;
    private JTextField requiredModsField;
    private boolean closed = false;
    private final boolean asPlugin;

    public PaceManTrackerGUI(boolean asPlugin) {
        this(asPlugin, true);
    }

    public PaceManTrackerGUI(boolean asPlugin, boolean actuallyShow) {
        this.setTitle("PaceMan Tracker");
        this.asPlugin = asPlugin;

        this.setContentPane(this.mainPanel);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PaceManTrackerGUI.this.onClose();
            }
        });

        if (!asPlugin) {
            this.mainPanel.remove(this.enabledCheckBox);
        }
        PaceManTrackerOptions options = PaceManTrackerOptions.getInstance();
        this.enabledCheckBox.setSelected(options.enabledForPlugin);
        this.enabledCheckBox.addActionListener(e -> {
            this.saveButton.setEnabled(this.hasChanges());
            if (asPlugin) {
                this.updateEnabledFields();
            }
        });
        this.resetStatsEnabled.setSelected(options.resetStatsEnabled);
        this.resetStatsEnabled.addActionListener(e -> {
            this.saveButton.setEnabled(this.hasChanges());
        });
        this.requiredModsField.setText(this.normalizeMods(options.requiredStatsMods));
        this.attachChangeListener(this.requiredModsField);
        Path runPath = PaceManTracker.getInstance().getRunStoragePath();
        String pathText = runPath.toString();
        this.storagePathLabel.setText("Runs are saved to: " + pathText);
        this.storagePathLabel.setToolTipText(pathText);
        if (!RESET_STATS_OPTION_USABLE) {
            this.remove(this.resetStatsEnabled);
        }
        if (asPlugin) {
            this.updateEnabledFields();
        }
        this.saveButton.addActionListener(e -> this.save());
        this.saveButton.setEnabled(this.hasChanges());

        this.revalidate();
        this.setMinimumSize(new Dimension(300, (asPlugin ? 180 : 160) + (RESET_STATS_OPTION_USABLE ? 20 : 0)));
        this.pack();
        this.setResizable(false);
        this.setVisible(actuallyShow);
    }

    static PaceManTrackerGUI getHeadless() {
        return new PaceManTrackerGUI(true, false);
    }

    public JPanel getMainPanel() {
        return this.mainPanel;
    }

    public static PaceManTrackerGUI open(boolean asPlugin, Point initialLocation) {
        if (instance == null || instance.isClosed()) {
            instance = new PaceManTrackerGUI(asPlugin);
            if (initialLocation != null) {
                instance.setLocation(initialLocation);
            }
        } else {
            instance.requestFocus();
        }
        return instance;
    }

    private void updateEnabledFields() {
        boolean enabled = !this.asPlugin || this.checkBoxEnabled();
        if (RESET_STATS_OPTION_USABLE) {
            this.resetStatsEnabled.setEnabled(enabled);
        }
        this.requiredModsField.setEnabled(enabled);
    }

    private boolean hasChanges() {
        PaceManTrackerOptions options = PaceManTrackerOptions.getInstance();
        return (this.asPlugin && this.checkBoxEnabled() != options.enabledForPlugin)
                || (this.resetStatsEnabled() != options.resetStatsEnabled)
                || !Objects.equals(this.getRequiredModsText(), this.normalizeMods(options.requiredStatsMods));
    }

    private void save() {
        PaceManTrackerOptions options = PaceManTrackerOptions.getInstance();
        options.enabledForPlugin = this.checkBoxEnabled();
        options.resetStatsEnabled = this.resetStatsEnabled();
        options.requiredStatsMods = this.getRequiredModsText();
        try {
            options.save();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        this.updateButtons();
        PaceManTracker.jingleQABRefresh.run();
    }

    private void updateButtons() {
        boolean hasChanges = this.hasChanges();
        this.saveButton.setEnabled(hasChanges);
    }

    private boolean checkBoxEnabled() {
        return this.enabledCheckBox.isSelected();
    }

    private boolean resetStatsEnabled() {
        return this.resetStatsEnabled.isSelected();
    }

    private String getRequiredModsText() {
        return this.normalizeMods(this.requiredModsField.getText());
    }

    private String normalizeMods(String mods) {
        return mods == null ? "" : mods.trim();
    }

    private void attachChangeListener(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                PaceManTrackerGUI.this.updateButtons();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                PaceManTrackerGUI.this.updateButtons();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                PaceManTrackerGUI.this.updateButtons();
            }
        });
    }

    public boolean isClosed() {
        return this.closed;
    }

    private void onClose() {
        // If not running as plugin, closing the GUI should stop the tracker.
        if (!this.asPlugin) {
            PaceManTracker.getInstance().stop();
        }
        this.closed = true;
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(7, 2, new Insets(5, 5, 5, 5), -1, -1));
        final JLabel label1 = new JLabel();
        label1.setText("PaceMan Tracker");
        mainPanel.add(label1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        enabledCheckBox = new JCheckBox();
        enabledCheckBox.setText("Enabled");
        mainPanel.add(enabledCheckBox, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        resetStatsEnabled = new JCheckBox();
        resetStatsEnabled.setText("Include resetting stats");
        mainPanel.add(resetStatsEnabled, new GridConstraints(2, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        storagePathLabel = new JLabel();
        storagePathLabel.setText("Runs are saved locally.");
        mainPanel.add(storagePathLabel, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Required stats mods (comma separated)");
        mainPanel.add(label2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        requiredModsField = new JTextField();
        requiredModsField.setToolTipText("Leave blank to disable the required mod check.");
        mainPanel.add(requiredModsField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        mainPanel.add(spacer1, new GridConstraints(5, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        saveButton = new JButton();
        saveButton.setText("Save");
        mainPanel.add(saveButton, new GridConstraints(6, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }

}
