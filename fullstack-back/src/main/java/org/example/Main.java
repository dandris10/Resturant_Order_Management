package org.example;

import org.example.controller.MainGuiController;
import org.example.model.Order;
import org.example.view.MainGuiView;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Order> orders = new ArrayList<>();
        ArrayList<Integer> processedOrders = new ArrayList<>();
        MainGuiView view = new MainGuiView();
        MainGuiController mainGuiController = new MainGuiController(view,orders,processedOrders);
        OrdersHandler ordersHandler = new OrdersHandler(orders,processedOrders,view);

        ordersHandler.run();

    }

}
