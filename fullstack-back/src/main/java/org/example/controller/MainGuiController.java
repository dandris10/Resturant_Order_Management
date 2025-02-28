package org.example.controller;

import org.example.OrdersHandler;
import org.example.dao.SqlQueries;
import org.example.model.MenuItem;
import org.example.model.Order;
import org.example.view.MainGuiView;
import org.example.view.OrdersView;
import org.example.view.StatisticsView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


//import static org.example.view.MainGuiView.processedOrders;

public class MainGuiController implements ListSelectionListener, ActionListener {

//    private final MainGuiView view; // Reference to the View
//    private final ArrayList<Order> orders; // Reference to the model data
//    private final ArrayList<Integer> processedOrders; // Processed orders

    SqlQueries sqlQueries = new SqlQueries();
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now;

    private static ArrayList<Order> orders;
    private static ArrayList<Integer> processedOrders;
    private static MainGuiView mainGuiView;

    public MainGuiController(MainGuiView mainGuiView, ArrayList<Order> orders, ArrayList<Integer> processedOrders){
        super();
        this.orders = orders;
        this.processedOrders = processedOrders;;
        this.mainGuiView = mainGuiView;

        mainGuiView.addListSelectionListener(this);
        mainGuiView.addActionListener(this);
    }



    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == mainGuiView.getjButton5()){
            System.out.println("Button pressed");
            OrdersView ordersView = new OrdersView(orders);
        }

        if(e.getSource() == mainGuiView.getjButton2()){
            StatisticsView statisticsView = new StatisticsView();
        }

        if (e.getSource() == mainGuiView.getjButton1()) {
            System.out.println("Button pressed1");

            JList<String> list = mainGuiView.getjList1();
            String itemText = list.getSelectedValue();

            if (itemText == null) {
                JOptionPane.showMessageDialog(mainGuiView, "Please select an order to process.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int selectedIndex = list.getSelectedIndex();
            String stringNumber = itemText.replaceAll("\\D+", "");
            int number = Integer.parseInt(stringNumber);
            DefaultListModel<String> model = mainGuiView.getModel();


            JList<String> list2 = mainGuiView.getjList2();
            String itemText2 = list2.getSelectedValue();
            int selectedIndex2 = list2.getSelectedIndex();
            String stringNumber2 = itemText2.replaceAll("\\D+", "");
            int number2 = Integer.parseInt(stringNumber2);
            DefaultListModel<String> model2 = mainGuiView.getModel2();


            // Find the corresponding order
            for (Order order : orders) {
                if (order.getId() == number) {
                    order.setProcessed(true); // Mark order as processed
                    model.remove(selectedIndex);
                    order.setWaiterId(number2);

                    sqlQueries.insertOrder(order.getId(), order.getTableId(), order.getWaiterId(), dtf.format( now = LocalDateTime.now()));
                    ArrayList<MenuItem> items = order.getItems();
                    for (MenuItem item : items) {
                        sqlQueries.insertOrderItems(order.getId(), sqlQueries.getMenuItemIdByName(item.getName()), 1);
                    }

                    System.out.println(order.toString()+ " .." + order.getProcessed() + ".." + order.getWaiterId());
                    break;
                }
            }
        }
    }

    public void onListSelection(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            JList<String> list = mainGuiView.getjList1();
            JTextArea textArea = mainGuiView.getJTextArea1();
            String itemText = list.getSelectedValue();

            if (itemText == null) { // Check if no item is selected
                textArea.setText(""); // Clear the text area if no selection
                return;
            }

            // Process the selected item
            String stringNumber = itemText.replaceAll("\\D+", "");
            int number = Integer.parseInt(stringNumber);

            for (Order order : orders) {
                if (order.getId() == number) {
                    textArea.setText(order.toStringNice() + "\n");
                    break;
                }
            }
        }
    }




    @Override
    public void valueChanged(ListSelectionEvent e) {
        onListSelection(e);
    }
}
