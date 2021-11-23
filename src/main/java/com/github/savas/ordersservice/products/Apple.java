package com.github.savas.ordersservice.products;

import com.github.savas.ordersservice.orders.OrderEntry;

public class Apple implements Product {
    private static final String name = "apple";
    private static final double price = 0.25;

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
