package com.asgrim.harvest;

import javax.swing.*;
import java.awt.event.*;
import java.util.prefs.Preferences;

public class setHarvestSettings extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField apiKeyEntry;
    private JTextField accountIdEntry;
    private JTextField userIdEntry;

    public setHarvestSettings() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        apiKeyEntry.setText(Preferences.userRoot().get(summaryForm.PREFERENCE_API_KEY, ""));
        accountIdEntry.setText(Preferences.userRoot().get(summaryForm.PREFERENCE_ACCOUNT_ID, ""));
        userIdEntry.setText(Preferences.userRoot().get(summaryForm.PREFERENCE_USER_ID, ""));

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    private void onOK() {
        // add your code here
        Preferences.userRoot().put(summaryForm.PREFERENCE_API_KEY, apiKeyEntry.getText());
        Preferences.userRoot().put(summaryForm.PREFERENCE_ACCOUNT_ID, accountIdEntry.getText());
        Preferences.userRoot().put(summaryForm.PREFERENCE_USER_ID, userIdEntry.getText());
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static void main(String[] args) {
        setHarvestSettings dialog = new setHarvestSettings();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
