package com.github.savas.ordersservice.orders;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final List<OrderEntry> orderEntries;

    public Order() {
        this.orderEntries = new ArrayList<>();
    }

    public void addOrderEntry(OrderEntry orderEntry) {
        this.orderEntries.add(orderEntry);
    }

    public double getTotal() {
        return orderEntries.stream().mapToDouble(OrderEntry::getTotal).sum();
    }

    public int getTotalQuantity() {
        return orderEntries.stream().mapToInt(OrderEntry::getQuantity).sum();
    }

    public List<OrderEntry> getOrderEntries() {
        return orderEntries;
    }
}
