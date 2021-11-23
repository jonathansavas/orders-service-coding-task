package com.github.savas.ordersservice.products;

import com.github.savas.ordersservice.orders.OrderEntry;

public interface Product {

    String getName();

    double getPrice();

    OrderEntry makeOrderEntry(int quantity);
}
