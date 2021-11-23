package com.github.savas.ordersservice.products;

import com.github.savas.ordersservice.orders.OrderEntry;

public class Orange implements Product {
    private static final String name = "orange";
    private static final double price = 0.60;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public OrderEntry makeOrderEntry(int quantity) {
        return null;
    }
}
