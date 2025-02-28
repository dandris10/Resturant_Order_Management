package org.example.controller;

import org.example.model.MenuItem;
import org.example.model.Order;
import org.example.view.ModifyOrderView;
import org.example.view.OrdersView;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;


public class OrdersController implements ActionListener, ListSelectionListener{

    private final ArrayList<Order> orders;
    private final OrdersView ordersView;

    public OrdersController(ArrayList<Order> orders, OrdersView ordersView) {
        this.orders = orders;
        this.ordersView = ordersView;

        ordersView.addActionListener(this);
        ordersView.addListSelectionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == ordersView.getJComboBox1()) {
            DefaultListModel<String> model = ordersView.getModel();
            JComboBox jComboBox = ordersView.getJComboBox1();
            String s = (String) jComboBox.getSelectedItem();

            model.clear();

            switch (s) {
                case "Waiter 1":
                    System.out.println("Waiter 1 Selected");
                    for (Order order : orders) {
                        if (order.getWaiterId() == 1) {
                            model.addElement("Order " + order.getId());
                        }
                    }
                    break;

                case "Waiter 2":
                    System.out.println("Waiter 2 Selected");
                    for (Order order : orders) {
                        if (order.getWaiterId() == 2) {
                            //System.out.println("Waiter 2 Selected");
                            model.addElement("Order " + order.getId());
                        }
                    }
                    break;

                case "Waiter 3":
                    System.out.println("Waiter 3 Selected");
                    for (Order order : orders) {
                        if (order.getWaiterId() == 3) {
                            model.addElement("Order " + order.getId());
                        }
                    }
                    break;


                case "Waiter 4":
                    System.out.println("Waiter 4 Selected");
                    for (Order order : orders) {
                        if (order.getWaiterId() == 4) {
                            model.addElement("Order " + order.getId());
                        }
                    }
                    break;


                default:
                    System.out.println("Oare merge??");
                    break;
            }
        }

        if (e.getSource() == ordersView.getjButton1()){
            JList<String> list = ordersView.getjList1();
            String s = (String) list.getSelectedValue();
            DefaultListModel<String> model = ordersView.getModel();
            int selectedIndex = list.getSelectedIndex();
            String orderId = s.replaceAll("\\D","");

            String filePath = "receipt" +orderId+ ".txt";
            File file = new File(filePath);

            if (!file.exists()) {
                System.out.println("The file does not exist: " + filePath);
                return;
            }

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

            synchronized (orders) {
                orders.removeIf(order -> order.getId() == Integer.parseInt(orderId));
            }
            model.remove(selectedIndex);
        }

            if(e.getSource() == ordersView.getjButton2()){
                System.out.println("Button pressed");
                JList<String> list = ordersView.getjList1();
                String s = (String) list.getSelectedValue();
                DefaultListModel<String> model = ordersView.getModel();
                String orderIdString = s.replaceAll("\\D","");
                int orderId = Integer.parseInt(orderIdString);

                ModifyOrderView modifyOrderView = new ModifyOrderView(orders, orderId);
                //ordersView.setVisible(false);
            }
        }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        try {
            onListSelection(e);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }


    public void onListSelection(ListSelectionEvent e) throws IOException {
        if (!e.getValueIsAdjusting()) {
            JList<String> list = ordersView.getjList1();
            JTable table = ordersView.getJTable();
            DefaultTableModel model2 = (DefaultTableModel) table.getModel();
            JLabel totalCheck = ordersView.setCheckVisible();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();

            // Clear existing rows in the table
            model2.setRowCount(0);

            String itemText = list.getSelectedValue();
            if (itemText != null) {
                String stringNumber = itemText.replaceAll("\\D+", "");
                int number = Integer.parseInt(stringNumber);

                //aici fac check si jtable-ul at the same time
                for (Order order : orders) {
                    if (order.getId() == number) {
                            List<MenuItem> items = order.getItems();
                                FileWriter myWriter = new FileWriter("receipt" + order.getId() + ".txt");
                                myWriter.write("-----RECEIPT-----\n");
                                myWriter.append("Date and time: "+ dtf.format(now)+"\n");
                                myWriter.append("Waiter: " + order.getWaiterId() + "\n");
                                myWriter.append("--------------\n");
                                for (MenuItem item : items) {
                                    model2.addRow(new Object[]{item.getName(), item.getPrice()});
                                    myWriter.append(item.getName()+": " + item.getPrice() + "\n");
                                }

                                myWriter.append("----------\n");
                                myWriter.append("TOTAL: "+ order.getCheck());
                                myWriter.close();
                                totalCheck.setText((String.valueOf(order.getCheck())));
                    }
                }

            }
        }
    }



}
