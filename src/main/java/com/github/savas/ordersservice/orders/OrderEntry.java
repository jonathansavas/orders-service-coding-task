package com.github.savas.ordersservice.orders;

import com.github.savas.ordersservice.products.Product;

public class OrderEntry {
    private final Product product;
    private final int quantity;

    public OrderEntry(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getTotal() {
        return this.product.getPrice() * this.quantity;
    }

    public Product getProduct() {
        return this.product;
    }

    public String getEntryString() {
        return this.product.getName() + "   " + this.quantity + " @   $" +
            this.product.getPrice() + "       $" + this.getTotal();
    }
}
