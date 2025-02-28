package org.example.controller;

import org.example.view.StatisticsView;
import org.example.dao.SqlQueries;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import raven.datetime.DatePicker;
import raven.datetime.event.DateSelectionEvent;
import raven.datetime.event.DateSelectionListener;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;



public class StatisticsController implements ActionListener, DateSelectionListener {

    private boolean isDateSelected = false;
    private boolean isWaiterSelected = false;
    private final StatisticsView view;
    private int waiterId;
    SqlQueries queries = new SqlQueries();
    StringBuilder finalDate = new StringBuilder();

    public StatisticsController(StatisticsView statisticsView) {
        this.view = statisticsView;
        view.addActionListener(this);
        view.addDateSelectionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        // Check if a date has been selected
        if (!isDateSelected) {
            JOptionPane.showMessageDialog(view, "Please select a date first.", "Date Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (e.getSource() == view.getWaiterComboBox()) {
            String waiterIdString = view.getWaiterComboBox().getSelectedItem().toString();
            String stringNumber = waiterIdString.replaceAll("\\D+", "");
            int number = Integer.parseInt(stringNumber);
            waiterId = number;
            isWaiterSelected = true;
        }


        // Check if a valid table has been selected
        if (e.getSource() == view.getTableComboBox()) {
            if (!view.getTableComboBox().getSelectedItem().equals("No table") &&
                    !view.getTableComboBox().getSelectedItem().equals("All tables") && isWaiterSelected) {

                String tableId = view.getTableComboBox().getSelectedItem().toString();
                String stringNumber = tableId.replaceAll("\\D+", "");
                int number = Integer.parseInt(stringNumber);

                DefaultTableModel model = (DefaultTableModel) view.getTable1().getModel();
                model.setRowCount(0);
                Map<String, Double> totalPriceForTable = queries.seeOrdersByDateAndTable(number, finalDate.toString(), waiterId);
                for (Map.Entry<String, Double> entry : totalPriceForTable.entrySet()) {
                    model.addRow(new Object[]{entry.getKey(), entry.getValue()});
                }
            } else {
                JOptionPane.showMessageDialog(view, "Please select a valid table.", "Table Required", JOptionPane.WARNING_MESSAGE);
            }
        }

        // Handle the "See" button click
        if (e.getSource() == view.getSeeButton()) {
            DefaultTableModel model = (DefaultTableModel) view.getTable1().getModel();
            DefaultTableModel model2 = (DefaultTableModel) view.getTable2().getModel();

            int selectedRow = view.getTable1().getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(view, "Please select a row from the table.", "Row Selection Required", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String date = model.getValueAt(selectedRow, 0).toString();
            Map<String, Object> list = queries.getItemsOrderedAtSpecificDateTime(date);

            model2.setRowCount(0);
            for (Map.Entry<String, Object> entry : list.entrySet()) {
                Object[] values = (Object[]) entry.getValue();
                String itemName = entry.getKey();
                int quantity = (int) values[0];
                double totalPrice = (double) values[1];

                model2.addRow(new Object[]{itemName, quantity, totalPrice});
            }
        }

        if(e.getSource() == view.getGetZButton()){
            if(isWaiterSelected && isDateSelected){
                Map<String, Double> zFinal = queries.getZForWaiter(finalDate.toString(), waiterId);
                double grandTotal = 0.0;
                try {
                    FileWriter myWriter = new FileWriter("z-"+ finalDate.toString() +"-"+ waiterId + ".txt");
                    myWriter.write("FINAL Z FOR " + waiterId + "AT TIME: " + finalDate.toString() + "\n");
                    for (Map.Entry<String, Double> entry : zFinal.entrySet()) {
                        String datetime = entry.getKey(); // The datetime key
                        double totalPrice = entry.getValue(); // The total price value

                        grandTotal = grandTotal + totalPrice;
                        myWriter.append("Datetime: " + datetime + ", Total Price: " + totalPrice);
                        myWriter.append("\n");

                    }
                    myWriter.append("\n");
                    myWriter.append("Total made: " + grandTotal + "\n");
                    myWriter.close();

                    String filePath = "z-"+ finalDate.toString() +"-"+ waiterId + ".txt";
                    File file = new File(filePath);
                    // Check if the Desktop class is supported
                    if (Desktop.isDesktopSupported()) {
                        Desktop desktop = Desktop.getDesktop();
                        try {
                            // Open the file in the default text editor
                            desktop.open(file);
                            System.out.println("File opened successfully!");
                        } catch (IOException ioException) {
                            System.err.println("An error occurred while opening the file.");
                            ioException.printStackTrace();
                        }
                    } else {
                        System.out.println("Desktop operations are not supported on this system.");
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    // Handle date selection
    @Override
    public void dateSelected(DateSelectionEvent e) {
        finalDate.setLength(0);
        DatePicker sourceDatePicker = (DatePicker) e.getSource();
        LocalDate selectedDate = sourceDatePicker.getSelectedDate();
        finalDate.append(selectedDate).append("%");

        System.out.println("Selected date: " + finalDate);
        isDateSelected = true;
    }


    public static JFreeChart createLineChart() {
        SqlQueries queries = new SqlQueries();
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        ArrayList<String> dates = queries.getAllOrderDatesForMonth(2025, 01);
        for(String date : dates) {
            dataset.addValue(queries.getTotalProfitForDay(date),"Revenue", date );
        }

        // Create the line chart
        return ChartFactory.createLineChart(
                "Monthly revenue", // Chart Title
                "Day",             // X-axis Label
                "Profit",             // Y-axis Label
                dataset,             // Dataset
                org.jfree.chart.plot.PlotOrientation.VERTICAL, // Plot Orientation
                true,                // Show legend
                true,                // Tooltips
                false                // URLs
        );
    }

}
