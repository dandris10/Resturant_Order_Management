package com.denis.fullstack.controller;

import com.denis.fullstack.model.MenuItem;
import com.denis.fullstack.model.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class OrderController {

    private static final ArrayList<Order> orders = new ArrayList<>(); // Temporary order storage
    private static final ArrayList<String> checks = new ArrayList<>();
    private static final ArrayList<String> waiters = new ArrayList<>();

    @PostMapping("/order")
    public String submitOrder(@RequestBody Map<String, Object> orderData) {
        // Extract the cart data from the request body
        List<Map<String, Object>> cartData = (List<Map<String, Object>>) orderData.get("cart");

        // Convert each map to a MenuItem using a loop
        List<MenuItem> items = new ArrayList<>();
        for (Map<String, Object> itemData : cartData) {
            String name = (String) itemData.get("name");
            Double price = ((Number) itemData.get("price")).doubleValue(); // Safely cast Integer to Double
            items.add(new MenuItem(name, price));
        }

        // Get the table ID
        Object tableIdObj = orderData.get("tableId");
        Integer tableId;
        if (tableIdObj instanceof Integer) {
            tableId = (Integer) tableIdObj;
        } else if (tableIdObj instanceof String) {
            tableId = Integer.parseInt((String) tableIdObj);
        } else {
            return "Invalid table ID format";
        }

        // Create a new order with the items and table ID
        Order order = new Order(items);
        order.setTableId(tableId); // Set the table ID
        orders.add(order);

        return "Order submitted successfully!";
    }

    @PostMapping("/waiter")
    public ResponseEntity<String> getWaiter(@RequestBody Map<String, String> payload) {
        String tableId = payload.get("tableId");
        System.out.println("Waiter requested for table: " + tableId);
        waiters.add(tableId);
        // Handle the "get waiter" signal (e.g., notify staff)
        return ResponseEntity.ok("Waiter request received");
    }

    @PostMapping("/check")
    public ResponseEntity<String> getCheck(@RequestBody Map<String, String> payload) {
        String tableId = payload.get("tableId");
        checks.add(tableId);
        System.out.println("Check requested for table: " + tableId);
        // Handle the "get check" signal (e.g., calculate bill)
        return ResponseEntity.ok("Check request received");
    }

    @GetMapping("/order/latest")
    public ResponseEntity<Order> getLatestOrder() {
        if (orders.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content when no orders are present
        }
        Order latestOrder = orders.get(orders.size() - 1);
        orders.remove(latestOrder);
        return ResponseEntity.ok(latestOrder); // Return the latest order with HTTP 200
    }

    @GetMapping("/check")
    public ResponseEntity<String> getCheck() {
        if (checks.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content when no orders are present
        }
        String lastCheck = checks.get(checks.size() - 1);
        checks.remove(lastCheck);
        return ResponseEntity.ok(lastCheck); // Return the latest order with HTTP 200
    }

    @GetMapping("/waiter")
    public ResponseEntity<String> getWaiter() {
        if (waiters.isEmpty()) {
            return ResponseEntity.noContent().build(); // Return 204 No Content when no orders are present
        }
        String lastWaiter = waiters.get(waiters.size() - 1);
        waiters.remove(lastWaiter);
        return ResponseEntity.ok(lastWaiter); // Return the latest order with HTTP 200
    }

}
