package org.example.dao;
import org.example.connection.ConnectionFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

public class SqlQueries {

    private static final Logger LOGGER = Logger.getLogger(SqlQueries.class.getName());
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    public void printAllMenuItems() {
        String query = "SELECT itemid, name, price FROM menu";
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = ConnectionFactory.getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);

            System.out.println("Menu Items:");
            while (resultSet.next()) {
                int id = resultSet.getInt("itemid");
                String name = resultSet.getString("name");
                double price = resultSet.getDouble("price");

                System.out.println("ID: " + id + ", Name: " + name + ", Price: $" + price);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching menu items", e);
        } finally {
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(connection);
        }
    }

    public void seeOrdersForTable1() {
        String query = "SELECT "
                + "orders.orderid, "
                + "orders.datetime, "
                + "tables.description AS table_description, "
                + "waiters.name AS waiter_name, "
                + "menu.name AS item_name, "
                + "order_items.quantity, "
                + "menu.price "
                + "FROM orders "
                + "JOIN tables ON orders.tableid = tables.tableid "
                + "JOIN waiters ON orders.waiterid = waiters.waiterid "
                + "JOIN order_items ON orders.orderid = order_items.orderid "
                + "JOIN menu ON order_items.itemid = menu.itemid "
                + "WHERE orders.tableid = 2";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {


            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    int orderId = rs.getInt("orderid");
                    String datetime = rs.getString("datetime");
                    String tableDescription = rs.getString("table_description");
                    String waiterName = rs.getString("waiter_name");
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("quantity");
                    double price = rs.getDouble("price");

                    // Print or store the data
                    System.out.printf("Order ID: %d, Date: %s, Table: %s, Waiter: %s, Item: %s, Quantity: %d, Price: %.2f%n",
                            orderId, datetime, tableDescription, waiterName, itemName, quantity, price);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching order details", e);
        }
    }


    public void insertOrder(int orderId, int tableId,int waiterId, String dateTime)  {
        String query = "INSERT INTO orders (orderid, tableid, waiterid, datetime) VALUES (?, ?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, tableId);
            preparedStatement.setInt(3, waiterId);
            preparedStatement.setString(4,dateTime);

            preparedStatement.executeUpdate();

            System.out.println("Order successfully inserted!");

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while inserting the order", e);
        } finally {
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(connection);
        }
    }

    public void insertOrderItems(int orderid, int itemid, int quantity){
        String query = "INSERT INTO order_items (orderid, itemid, quantity) VALUES (?, ?, ?)";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, orderid);
            preparedStatement.setInt(2, itemid);
            preparedStatement.setInt(3, quantity);
            preparedStatement.executeUpdate();

            System.out.println("Order items successfully inserted!");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while inserting the order", e);
        } finally {
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(connection);
        }
    }

    public void deleteOrderItem(int orderId, int itemId) {
        // Modify the query to delete only the first matching row
        String query = "DELETE FROM order_items WHERE orderid = ? AND itemid = ? LIMIT 1";

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query);

            preparedStatement.setInt(1, orderId);
            preparedStatement.setInt(2, itemId);
            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Order item successfully deleted!");
            } else {
                System.out.println("No order item found with the given order ID and item ID.");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while deleting the order item", e);
        } finally {
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(connection);
        }
    }

    public Map<String, Double> getZForWaiter(String date, int waiterId) {
        // Modify the query to sum the total price of orders for the given tableid
        String query = "SELECT "
                + "orders.datetime, "
                + "SUM(menu.price * order_items.quantity) AS total_price "
                + "FROM orders "
                + "JOIN order_items ON orders.orderid = order_items.orderid "
                + "JOIN menu ON order_items.itemid = menu.itemid "
                + "WHERE orders.datetime LIKE ? AND orders.waiterid = ? "
                + "GROUP BY orders.datetime";


        // Create a HashMap to store the results
        Map<String, Double> totalPriceMap = new HashMap<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the value of the tableId parameter
            preparedStatement.setString(1, date + "%");  // Date with wildcard for partial match
            preparedStatement.setInt(2, waiterId);       // Waiter ID

            try (ResultSet rs = preparedStatement.executeQuery()) {
                // Iterate through the result set and populate the HashMap
                while (rs.next()) {
                    String datetime = rs.getString("datetime");
                    double totalPrice = rs.getDouble("total_price");

                    // Store the result in the HashMap
                    totalPriceMap.put(datetime, totalPrice);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching total prices", e);
        }

        // Return the HashMap with datetime as key and total_price as value
        return totalPriceMap;
    }

    public Map<String, Double> seeOrdersByDateAndTable(int tableId, String date, int waiterId) {
        // Modify the query to sum the total price of orders for the given tableid
        String query = "SELECT "
                + "orders.datetime, "
                + "SUM(menu.price * order_items.quantity) AS total_price "
                + "FROM orders "
                + "JOIN order_items ON orders.orderid = order_items.orderid "
                + "JOIN menu ON order_items.itemid = menu.itemid "
                + "WHERE orders.tableid = ? AND orders.datetime LIKE ? AND orders.waiterid = ? "
                + "GROUP BY orders.datetime";


        // Create a HashMap to store the results
        Map<String, Double> totalPriceMap = new HashMap<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the value of the tableId parameter
            preparedStatement.setInt(1, tableId);         // Table ID
            preparedStatement.setString(2, date + "%");  // Date with wildcard for partial match
            preparedStatement.setInt(3, waiterId);       // Waiter ID

            try (ResultSet rs = preparedStatement.executeQuery()) {
                // Iterate through the result set and populate the HashMap
                while (rs.next()) {
                    String datetime = rs.getString("datetime");
                    double totalPrice = rs.getDouble("total_price");

                    // Store the result in the HashMap
                    totalPriceMap.put(datetime, totalPrice);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching total prices", e);
        }

        // Return the HashMap with datetime as key and total_price as value
        return totalPriceMap;
    }

    public Map<String, Double> seeAllOrdersByDate(String date) {
        // Modify the query to sum the total price of orders for the given tableid
        String query = "SELECT "
                + "orders.datetime, "
                + "SUM(menu.price * order_items.quantity) AS total_price "
                + "FROM orders "
                + "JOIN order_items ON orders.orderid = order_items.orderid "
                + "JOIN menu ON order_items.itemid = menu.itemid "
                + "WHERE orders.datetime LIKE ? "
                + "GROUP BY orders.datetime";  // Group by datetime to calculate total for each order

        // Create a HashMap to store the results
        Map<String, Double> totalPriceMap = new HashMap<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the value of the tableId parameter
            preparedStatement.setString(1, date);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                // Iterate through the result set and populate the HashMap
                while (rs.next()) {
                    String datetime = rs.getString("datetime");
                    double totalPrice = rs.getDouble("total_price");

                    // Store the result in the HashMap
                    totalPriceMap.put(datetime, totalPrice);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching total prices", e);
        }

        // Return the HashMap with datetime as key and total_price as value
        return totalPriceMap;
    }

    public Map<String, Object> getItemsOrderedAtSpecificDateTime(String dateTime) {
        String query = "SELECT "
                + "menu.name AS item_name, "
                + "SUM(order_items.quantity) AS total_quantity, "
                + "SUM(order_items.quantity * menu.price) AS total_price "
                + "FROM orders "
                + "JOIN order_items ON orders.orderid = order_items.orderid "
                + "JOIN menu ON order_items.itemid = menu.itemid "
                + "WHERE orders.datetime = ? "
                + "GROUP BY menu.name;";

        Map<String, Object> orderedItems = new HashMap<>();

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the value of the datetime parameter
            preparedStatement.setString(1, dateTime);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    String itemName = rs.getString("item_name");
                    int quantity = rs.getInt("total_quantity");
                    double totalPrice = rs.getDouble("total_price");

                    // Store each ordered item in the map or process as needed
                    orderedItems.put(itemName, new Object[]{quantity,totalPrice});
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching ordered items", e);
        }

        return orderedItems;
    }



    public Integer getMenuItemIdByName(String itemName) {
        String query = "SELECT itemid FROM menu WHERE name = ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, itemName);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("itemid");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching the menu item ID", e);
        }

        return null;  // Return null if the item is not found
    }

    public Double getTotalProfitForDay(String date) {
        String query = "SELECT SUM(order_items.quantity * menu.price) AS total_profit "
                + "FROM orders "
                + "JOIN order_items ON orders.orderid = order_items.orderid "
                + "JOIN menu ON order_items.itemid = menu.itemid "
                + "WHERE orders.datetime LIKE ?";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Set the parameter for the query (like '%YYYY-MM-DD%')
            preparedStatement.setString(1, date + "%");

            // Execute the query and process the result
            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    // Return the total profit for the day
                    return rs.getDouble("total_profit");
                }
            }
        } catch (SQLException e) {
            // Log the exception if an error occurs
            LOGGER.log(Level.SEVERE, "An error occurred while calculating the total profit for the day", e);
        }

        // Return null if no profit is found
        return null;
    }

    public ArrayList<String> getAllOrderDatesForMonth(int year, int month) {
        ArrayList<String> dates = new ArrayList<>();
        String query = "SELECT DISTINCT DATE(datetime) AS order_date FROM orders WHERE DATE(datetime) LIKE ?";

        // Format the month as two digits (e.g., "01" for January)
        String monthString = String.format("%02d", month);
        String monthPattern = year + "-" + monthString + "-%";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, monthPattern);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                // Process each date found in the result set
                while (rs.next()) {
                    dates.add(rs.getString("order_date"));
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An error occurred while fetching all order dates for the specified month", e);
        }

        return dates;  // Return the list of dates for the specified month
    }




}
