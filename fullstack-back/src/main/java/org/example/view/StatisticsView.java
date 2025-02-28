package org.example.view;

import com.formdev.flatlaf.FlatLightLaf;
import net.miginfocom.swing.MigLayout;
import org.example.controller.StatisticsController;
import raven.datetime.DatePicker;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import raven.datetime.event.DateSelectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class StatisticsView extends JFrame {

    private JComboBox<String> waiterComboBox;
    private JComboBox<String> tableComboBox;
    //private JButton seeGraphsButton;
    private JButton getZButton;
    private JTable table1;  // Define table1 globally
    private JTable table2;  // Define table2 globally
    private JButton seeButton;
    DatePicker datePicker;

    public StatisticsView() {
        FlatLightLaf.setup();
        initComponents();
        setTitle("StatisticsView");
        setSize(1000, 700); // Set size before centering
        setLocationRelativeTo(null); // Center the frame on the screen
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setVisible(true); // Make the frame visible last
        StatisticsController statisticsController = new StatisticsController(this);
    }

    private void initComponents() {
        // Set layout for the main frame
        setLayout(new MigLayout("wrap 1, align center, insets 20", "[center]", "[]20[]"));

        // Create buttons
        getZButton = new JButton("Get Z");
        //seeGraphsButton = new JButton("See Graphs");
        seeButton = new JButton("See");  // Create the "See" button

        // DatePicker setup
        datePicker = new DatePicker();
        datePicker.setDateSelectionMode(DatePicker.DateSelectionMode.SINGLE_DATE_SELECTED);
        JFormattedTextField editor = new JFormattedTextField();
        datePicker.setEditor(editor);

        // Waiter JComboBox and JLabel
        JLabel waiterLabel = new JLabel("Waiters");
        waiterLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); // Nice font
        String[] waiters = {"No waiter", "Waiter 1", "Waiter 2", "Waiter 3", "Waiter 4"};
        waiterComboBox = new JComboBox<>(waiters);  // Save the combo box in the field

        // Table JComboBox and JLabel
        JLabel tableLabel = new JLabel("Tables");
        tableLabel.setFont(new Font("SansSerif", Font.BOLD, 16)); // Nice font
        String[] tables = {"No table", "Table 1", "Table 2", "Table 3", "Table 4", "All tables"};
        tableComboBox = new JComboBox<>(tables);  // Save the combo box in the field

        // Create a panel to hold the labels and combo boxes in a row
        JPanel comboPanel = new JPanel(new MigLayout("insets 0, gap 20", "[left][10][left]"));
        comboPanel.setOpaque(false); // To match the frame's background

        // Add components to comboPanel: Waiter label + combo and Table label + combo
        comboPanel.add(waiterLabel);
        comboPanel.add(waiterComboBox, "width 150!");
        comboPanel.add(tableLabel);
        comboPanel.add(tableComboBox, "width 150!");

        // Create tables
        String[] table1Columns = {"Date & Time", "Price"};
        Object[][] table1Data = {
        };

        String[] table2Columns = {"Name", "Quantity", "Price"};
        Object[][] table2Data = {
        };

        // Initialize table1 and table2 globally
        table1 = new JTable(new DefaultTableModel(table1Data, table1Columns));
        table2 = new JTable(new DefaultTableModel(table2Data, table2Columns));

        JScrollPane table1ScrollPane = new JScrollPane(table1);
        JScrollPane table2ScrollPane = new JScrollPane(table2);

        // Create a panel to hold the buttons horizontally
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
       // buttonPanel.add(seeGraphsButton);
        buttonPanel.add(getZButton);

        // Add components to the frame
        add(buttonPanel, "align center, gapy 10"); // Button panel at the top
        add(editor, "width 250"); // DatePicker editor
        add(comboPanel, "align center, gapy 30");

        // Create a panel to hold the tables
        JPanel tablePanel = new JPanel(new MigLayout("insets 0, gap 20"));
        tablePanel.add(table1ScrollPane, "width 450, height 200");
        tablePanel.add(table2ScrollPane, "width 450, height 200");

        // Add the table panel to the frame
        add(tablePanel, "align center, gapy 20");

        // Add the "See" button below the tables
        add(seeButton, "align center, gapy 20");

        // Create the line chart
        JFreeChart lineChart = StatisticsController.createLineChart();
        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(400, 200)); // Set the size for the chart

        // Add the chart panel below the tables and button
        add(chartPanel, "align center, gapy 20");
    }



    // Getter methods for combo boxes
    public JComboBox<String> getWaiterComboBox() {
        return waiterComboBox;
    }

    public JComboBox<String> getTableComboBox() {
        return tableComboBox;
    }

    public JTable getTable1() {
        return table1;
    }

    public JTable getTable2() {
        return table2;
    }

    public JButton getSeeButton(){
        return seeButton;
    }

    public JButton getGetZButton(){
        return getZButton;
    }

    public void addActionListener(ActionListener listener) {
        waiterComboBox.addActionListener(listener);
        tableComboBox.addActionListener(listener);
        seeButton.addActionListener(listener);
        getZButton.addActionListener(listener);
    }

    public void addDateSelectionListener(DateSelectionListener listener) {
        datePicker.addDateSelectionListener(listener);
    }


}
