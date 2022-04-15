package com.asgrim.harvest;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.prefs.Preferences;

public class summaryForm {
    private static JFrame frame;
    private JComboBox<DatePeriod> dateList;
    private JButton summariseButton;
    private JPanel mainPanel;
    private JTable hoursTable;
    private JButton harvestConfigurationButton;
    private final DefaultTableModel hoursTableModel;

    public static final String PREFERENCE_API_KEY = "com/asgrim/harvest/api_key";
    public static final String PREFERENCE_ACCOUNT_ID = "com/asgrim/harvest/account_id";
    public static final String PREFERENCE_USER_ID = "com/asgrim/harvest/user_id";

    public summaryForm() {
        Object[] columns = {"Hours", "Description"};
        hoursTableModel = new DefaultTableModel();
        hoursTableModel.setColumnIdentifiers(columns);
        hoursTable.setModel(hoursTableModel);
        hoursTable.setRowHeight(30);
        hoursTable.getColumnModel().getColumn(0).setMaxWidth(100);

        for (DatePeriod datePeriod : DatePeriod.generateDatePeriodsRelativeToDate(DatePeriod.nextInvoiceDate())) {
            dateList.addItem(datePeriod);
        }

        summariseButton.addActionListener(actionEvent -> {
            while(hoursTableModel.getRowCount() > 0) {
                hoursTableModel.removeRow(0);
            }

            DatePeriod selectedPeriod = (DatePeriod) dateList.getSelectedItem();
            assert selectedPeriod != null;

            String apiKey = Preferences.userRoot().get(PREFERENCE_API_KEY, "");
            String accountId = Preferences.userRoot().get(PREFERENCE_ACCOUNT_ID, "");
            String userId = Preferences.userRoot().get(PREFERENCE_USER_ID, "");
            if (apiKey.equals("") || accountId.equals("")) {
                JOptionPane.showMessageDialog(
                    frame,
                    "Harvest API settings were not configured yet.",
                    "Cannot fetch Harvest hours",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String responseBody;
            try {
                responseBody = new HarvestClient(apiKey, accountId, userId).requestTimeEntriesForPeriod(selectedPeriod);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                return;
            }

            CompleteHoursList hoursList;
            try {
                hoursList = HarvestResponseProcessor.processHarvestResponse(responseBody);
            } catch (ParseException e) {
                e.printStackTrace();
                return;
            }

            for (HoursForDay hoursForDay : hoursList.sortedDailyHours()) {
                Object[] row = new Object[2];
                row[0] = hoursForDay.hours().toString();
                row[1] = hoursForDay.toKey();
                hoursTableModel.addRow(row);
            }
            int plainRows = hoursTableModel.getRowCount();

            for (Map.Entry<String, Float> me : hoursList.totalHoursPerClient()) {
                Object[] row = new Object[2];
                row[0] = me.getValue().toString();
                row[1] = me.getKey();
                hoursTableModel.addRow(row);
            }

            TableColumnModel columnModel = hoursTable.getColumnModel();
            for (int col = 0; col < columnModel.getColumnCount(); col++) {
                columnModel.getColumn(col).setCellRenderer(new DefaultTableCellRenderer() {
                    @Override
                    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                        if (row >= plainRows) {
                            this.setFont(this.getFont().deriveFont(Font.BOLD));
                        }
                        return this;
                    }
                });
            }
        });
        harvestConfigurationButton.addActionListener(e -> {
            Runnable doRun = () -> {
                setHarvestSettings dlg = new setHarvestSettings();
                dlg.pack();
                dlg.setLocationRelativeTo(frame);
                dlg.setVisible(true);
            };
            SwingUtilities.invokeLater(doRun);
        });
    }

    public static void main(String[] args) {
        frame = new JFrame("Harvest Summary");
        frame.setContentPane(new summaryForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
