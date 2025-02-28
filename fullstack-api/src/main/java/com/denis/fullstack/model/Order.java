package com.denis.fullstack.model;

import java.util.List;
import java.util.Random;

public class Order {
    private static int idCounter = 1; // Static counter to generate unique IDs
    private int id; // Unique ID for each order
    private List<MenuItem> items;
    private int waiterId;
    private int tableId;
    private Boolean processed;

    // Default constructor for serialization/deserialization
    public Order() {}

    public Order(List<MenuItem> items) {
        this.id = new Random().nextInt(1000); // Assign a unique ID to each order
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

    public List<MenuItem> getItems() {
        return items;
    }

    public void setWaiterId(int waiterId) {
        this.waiterId = waiterId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public void setItems(List<MenuItem> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", tableId=" + tableId + ", items=" + items + '}';
    }

    public int getTableId(){
        return tableId;
    }

    public int getWaiterId() {
        return waiterId;
    }
}
