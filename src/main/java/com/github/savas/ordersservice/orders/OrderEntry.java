package com.github.savas.ordersservice.orders;

import com.github.savas.ordersservice.offers.Offer;
import com.github.savas.ordersservice.products.Product;

public class OrderEntry {
    private final Product product;
    private final int quantity;
    private boolean offerApplied = false;
    private int numOffersApplied = 0;
    private double discountApplied = 0.0;
    private String offerDesc;

    public OrderEntry(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public double getTotal() {
        return this.getOriginalTotal() - this.discountApplied;
    }

    public double getOriginalTotal() {
        return this.product.getPrice() * this.quantity;
    }

    public double getDiscountApplied() {
        return this.discountApplied;
    }

    public Product getProduct() {
        return this.product;
    }

    public void applyOffer(Offer offer) {
        this.offerApplied = true;
        this.numOffersApplied = offer.getNumOffersApplied(this.quantity);
        this.discountApplied = offer.getDiscount(this.quantity, this.product.getPrice());
        this.offerDesc = offer.getDescription();
    }

    public String getEntryString() {
        String entryDesc = this.product.getName() + "   " + this.quantity + " @   $" +
            this.product.getPrice() + "       $" + this.getOriginalTotal();

        if (this.offerApplied) {
            entryDesc += "\n    " + this.offerDesc + ": " + this.numOffersApplied +
                    " applied      -$" + this.discountApplied;
        }

        return entryDesc;
    }
}
