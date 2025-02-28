package org.example.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Order {
    private int id; // Unique ID for each order
    private ArrayList<MenuItem> items;
    private int waiterId;
    private int tableId;
    private Boolean processed;

    // Default constructor for serialization/deserialization
    public Order() {}

    public Order(ArrayList<MenuItem> items) {
        this.id = new Random().nextInt(10000);
        this.items = items;
        this.waiterId = 0;
        this.tableId = 0;
        this.processed = false;
    }


    // Getters and setters (required for JSON serialization/deserialization)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setProcessed(Boolean processed) {
        this.processed = processed;
    }

    public Boolean getProcessed() {
        return processed;
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }

    public void setWaiterId(int waiterId) {
        this.waiterId = waiterId;
    }

    public void setItems(ArrayList<MenuItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", tableId=" + tableId + ", items=" + items + '}';
    }

    public String toStringNice(){
        StringBuilder sb = new StringBuilder();
        sb.append("Order Details:\n");
        sb.append("  Order ID: ").append(id).append("\n");
        sb.append("  Table ID: ").append(tableId).append("\n");
        sb.append("  Items:\n");
        for (MenuItem item : items) { // Assuming 'items' is a collection of MenuItem
            sb.append("    - ").append(item).append("\n");
        }
        return sb.toString();
    }

    public int getWaiterId() {
        return waiterId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public double getCheck() {
        double total = 0.0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }

    public void addMenuItems(ArrayList<MenuItem> items1) {
        for (MenuItem item : items1) {
            items.add(item);
        }
    }

    public void deleteMenuItems(ArrayList<MenuItem> items1){
        for(MenuItem item : items1) {
            items.remove(item);
        }
    }
}
