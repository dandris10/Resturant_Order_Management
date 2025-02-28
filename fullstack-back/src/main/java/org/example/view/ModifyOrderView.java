package org.example.view;

import org.example.controller.ModifyOrderController;
import org.example.model.MenuItem;
import org.example.model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ModifyOrderView extends javax.swing.JFrame {

    private static ArrayList<Order> orders;
    private static int orderId;

    public ModifyOrderView(ArrayList<Order> orders, int orderId) {
        this.orders = orders;
        this.orderId = orderId;
        initComponents();
        setTitle("Orders");
        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);

        ModifyOrderController modifyOrderController = new ModifyOrderController(orders,this, orderId);
    }

    private void initComponents() {

        System.out.println(orderId);
        jPanel1 = new javax.swing.JPanel();
        jButton3 = createStyledButton("Salad");
        jButton4 = createStyledButton("Pizza");
        jButton5 = createStyledButton("Burger");
        jButton6 = createStyledButton("..Other items");
        jButton7 = createStyledButton("Delete");
        jButton8 = createStyledButton("Save");
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = createStyledButton("Pasta");
        jButton2 = createStyledButton("Cola");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Console", Font.BOLD, 14)); // NOI18N
        jLabel1.setText("Total :");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Item", "Price"
                }
        ) {
            boolean[] canEdit = new boolean[]{
                    false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });

        DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
        for(Order order : orders) {
            if(order.getId() == orderId){
                ArrayList<MenuItem> items = order.getItems();
                for(MenuItem item : items){
                    model.addRow(new Object[]{item.getName(),item.getPrice()});
                }
            }
        }


        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
        }

        jLabel2.setText("$$");
        jLabel2.setToolTipText("");

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 2, 10, 10)); // Rows, columns, horizontal gap, vertical gap
        buttonPanel.add(jButton3);
        buttonPanel.add(jButton4);
        buttonPanel.add(jButton5);
        buttonPanel.add(jButton6);
        buttonPanel.add(jButton1);
        buttonPanel.add(jButton2);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        bottomPanel.add(jButton7);
        bottomPanel.add(jButton8);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(buttonPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabel1)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabel2)))
                                .addGap(30, 30, 30))
                        .addComponent(bottomPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(buttonPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                                .addComponent(bottomPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    public void addActionListener(ActionListener listener) {
        jButton1.addActionListener(listener);
        jButton2.addActionListener(listener);
        jButton3.addActionListener(listener);
        jButton4.addActionListener(listener);
        jButton5.addActionListener(listener);
        jButton6.addActionListener(listener);
        jButton7.addActionListener(listener);
        jButton8.addActionListener(listener);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Lucida Sans Unicode", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 50));
        button.setBackground(new Color(60, 179, 113)); // Light green
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 2)); // Darker green border
        return button;
    }

    public DefaultTableModel getTableModel() {
        return (DefaultTableModel) jTable1.getModel();
    }

    public JButton getjButton3(){
        return jButton3;
    }

    public JButton getjButton1(){
        return jButton1;
    }

    public JButton getjButton2(){
        return jButton2;
    }

    public JButton getjButton4(){
        return jButton4;
    }

    public JButton getjButton5(){
        return jButton5;
    }

    public JButton getjButton6(){
        return jButton6;
    }

    public JButton getjButton8(){
        return jButton8;
    }

    public JButton getjButton7(){
        return jButton7;
    }

    public JTable getjTable1() {
        return jTable1;
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel jPanel1;
    private DefaultListModel<String> model;
    // End of variables declaration
}
