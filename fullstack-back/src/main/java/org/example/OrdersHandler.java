package org.example;

import org.example.model.MenuItem;
import org.example.model.Order;
import org.example.view.MainGuiView;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OrdersHandler extends Component implements Runnable {
    private String API_URL = "http://localhost:8080/api/order/latest"; // URL to fetch orders
    private final ArrayList<Order> orders;
    private final ArrayList<Integer> processedOrders;
    private final MainGuiView frame;



    public OrdersHandler(ArrayList<Order> orders, ArrayList<Integer> processedOrders, MainGuiView mainGuiView) {
        this.orders = orders;
        this.processedOrders = processedOrders;
        this.frame = mainGuiView;
    }

    @Override
    public void run() {
        while (true) { // Continuously fetch orders
           // deleteProcessedOrders();
            fetchLatestOrder();
            fetchGetCheck();
            fetchGetWaiter();
            try {
                Thread.sleep(1000); // Wait 1 second between fetches
            } catch (InterruptedException e) {
                System.err.println("OrderClient thread interrupted: " + e.getMessage());
                Thread.currentThread().interrupt(); // Restore interrupt flag
                break;
            }
        }
    }

    // Method to fetch the latest order from Spring Boot API
    private void fetchLatestOrder() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the response as a single order
                JSONObject orderJson = new JSONObject(response.toString());
                int orderId = orderJson.getInt("id"); // Get the order ID
                int tableId = orderJson.getInt("tableId"); // Get the table ID
               // System.out.println(tableId);

                //System.out.println("Order ID: " + orderId + ", Table ID: " + tableId);

               if (processedOrders.contains(orderId)) {
                    //System.out.println("No new orders received.");
                    return;
               }

                // Process items in the order
                ArrayList<MenuItem> items = new ArrayList<>();
                JSONArray itemsJson = orderJson.getJSONArray("items");
                for (int j = 0; j < itemsJson.length(); j++) {
                    JSONObject itemJson = itemsJson.getJSONObject(j);
                    String name = itemJson.getString("name");
                    double price = itemJson.getDouble("price");
                    items.add(new MenuItem(name, price));
                }

                Order order = new Order(items);
                order.setId(orderId);
                order.setTableId(tableId);

                synchronized (processedOrders) {
                    processedOrders.add(orderId); // Ensure thread safety
                }

                synchronized (orders) {
                    orders.add(order); // Add the new order to orders list
                }

                // Add the order to processed list and print details
                System.out.println("New latest order received");

                DefaultListModel<String> model = frame.getModel();

                model.addElement("Order: " + orderId);



            } else if (responseCode == HttpURLConnection.HTTP_NO_CONTENT) {
                //System.out.println("No latest order available.");
            } else {
                System.out.println("Error: HTTP response code " + responseCode);
            }
        } catch (Exception e) {
            System.err.println("Error fetching latest order: " + e.getMessage());
        }
    }

    public void fetchGetCheck(){
        try {
            URL url = new URL("http://localhost:8080/api/check");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Set headers if necessary (e.g., for JSON response)
            connection.setRequestProperty("Content-Type", "application/json");

            // Get the response code to ensure the request was successful
            int responseCode = connection.getResponseCode();
          //  System.out.println("Response Code: " + responseCode);

            // If the response code is HTTP_OK (200), handle the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    // Read the response
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    // Print the response from the server
                    System.out.println("Response: " + response.toString());
                    JOptionPane.showMessageDialog(this, "TABLE "+response.toString()+" NEEDS THEIR CHECK", "CHECK REQUIRED", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                //System.out.println("Request failed");
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void fetchGetWaiter(){
        try {
            URL url = new URL("http://localhost:8080/api/waiter");

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Set headers if necessary (e.g., for JSON response)
            connection.setRequestProperty("Content-Type", "application/json");

            // Get the response code to ensure the request was successful
            int responseCode = connection.getResponseCode();
            //System.out.println("Response Code: " + responseCode);

            // If the response code is HTTP_OK (200), handle the response
            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String inputLine;
                    StringBuilder response = new StringBuilder();

                    // Read the response
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    // Print the response from the server
                    //System.out.println("Response: " + response.toString());
                    JOptionPane.showMessageDialog(this, "TABLE "+response.toString()+" NEEDS THEIR WAITER", "WAITER REQUIRED", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                //System.out.println("Request failed");
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteProcessedOrders(){
        for(Order order : orders) {
            if(order.getProcessed()){
                DefaultListModel<String> model = frame.getModel();
                System.out.println("Order: " + order.getId() + "Is processed");
                synchronized (orders) {
                    orders.remove(order); // Add the new order to orders list
                }
            }
        }
    }

}
