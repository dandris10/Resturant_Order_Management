package org.example.controller;

import org.example.dao.SqlQueries;
import org.example.model.MenuItem;
import org.example.model.Order;
import org.example.view.ModifyOrderView;
import org.example.view.OrdersView;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ModifyOrderController implements ActionListener {

    private final ArrayList<Order> orders;
    private final ModifyOrderView modifyOrderView;
    private final int orderId;
    private ArrayList<MenuItem> items = new ArrayList<>();
    private ArrayList<MenuItem> itemsToBeDeleted = new ArrayList<>();
    SqlQueries sqlQueries = new SqlQueries();

    public ModifyOrderController(ArrayList<Order> orders, ModifyOrderView modifyOrderView, int orderId) {
        this.orders = orders;
        this.modifyOrderView = modifyOrderView;
        this.orderId = orderId;

        modifyOrderView.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        DefaultTableModel model = modifyOrderView.getTableModel();
        Order order = getOrder(); // Get the current order instance

        if (e.getSource() == modifyOrderView.getjButton3()) { // Add Salad
            model.addRow(new Object[]{"Salad", 5.00});
            MenuItem menuItem = new MenuItem("Salad", 5.00);
            items.add(menuItem); // Add to the persistent list
        }

        if (e.getSource() == modifyOrderView.getjButton1()) { // Add Pasta
            model.addRow(new Object[]{"Pasta", 8.00});
            MenuItem menuItem = new MenuItem("Pasta", 8.00);
            items.add(menuItem); // Add to the persistent list
        }

        if (e.getSource() == modifyOrderView.getjButton2()) { // Add Pasta
            model.addRow(new Object[]{"Cola", 7.00});
            MenuItem menuItem = new MenuItem("Cola", 7.00);
            items.add(menuItem); // Add to the persistent list
        }

        if (e.getSource() == modifyOrderView.getjButton4()) { // Add Pasta
            model.addRow(new Object[]{"Pizza", 8.00});
            MenuItem menuItem = new MenuItem("Pizza", 8.00);
            items.add(menuItem); // Add to the persistent list
        }

        if (e.getSource() == modifyOrderView.getjButton5()) { // Add Pasta
            model.addRow(new Object[]{"Burger", 7.00});
            MenuItem menuItem = new MenuItem("Burger", 7.00);
            items.add(menuItem); // Add to the persistent list
        }

        // Adauga fiecare item din meniu...

        if (e.getSource() == modifyOrderView.getjButton8()) { // Save button
            order.addMenuItems(items); // Add all items to the order
            for(MenuItem menuItem : items) {
                sqlQueries.insertOrderItems(order.getId(), sqlQueries.getMenuItemIdByName(menuItem.getName()),1);
            }
            items.clear(); // Clear the list after saving
           // System.out.println("Items saved to the order: " + order.getItems());
            modifyOrderView.setVisible(false);
        }

        if (e.getSource() == modifyOrderView.getjButton7()){
            JTable table = modifyOrderView.getjTable1();
            int[] selectedRows = table.getSelectedRows();
            for(int i = selectedRows.length - 1; i >= 0; i--){
                int rowIndex = selectedRows[i];

                String name = (String) table.getValueAt(rowIndex,0);
                double price = (double) table.getValueAt(rowIndex,1);
                MenuItem item = new MenuItem(name,price);
                itemsToBeDeleted.add(item);
                model.removeRow(rowIndex);
            }


            for(MenuItem menuItem : itemsToBeDeleted) {
                sqlQueries.deleteOrderItem(order.getId(), sqlQueries.getMenuItemIdByName(menuItem.getName()));
            }
            order.deleteMenuItems(itemsToBeDeleted);
            itemsToBeDeleted.clear();
            modifyOrderView.setVisible(false);
        }
    }


    private Order getOrder(){
        for(Order order : orders){
            if(order.getId() == orderId){
                return order;
            }
        }
        return null;
    }
}
