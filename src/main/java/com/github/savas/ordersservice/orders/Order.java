package com.github.savas.ordersservice.orders;

import java.util.ArrayList;
import java.util.List;

public class Order {
    private final List<OrderEntry> orderEntries;
    private final long orderId;

    public Order(long orderId) {
        this.orderEntries = new ArrayList<>();
        this.orderId = orderId;
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

    public double getTotalDiscounts() {
        return orderEntries.stream().mapToDouble(OrderEntry::getDiscountApplied).sum();
    }

    public List<OrderEntry> getOrderEntries() {
        return orderEntries;
    }

    public long getOrderId() {
        return this.orderId;
    }
}
