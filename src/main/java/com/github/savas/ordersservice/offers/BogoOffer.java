package com.github.savas.ordersservice.offers;

public class BogoOffer implements Offer {

    @Override
    public double getDiscount(int productQuantity, double productPrice) {
        return this.getNumOffersApplied(productQuantity) * productPrice;
    }

    @Override
    public int getNumOffersApplied(int productQuantity) {
        return productQuantity / 2;
    }

    @Override
    public String getDescription() {
        return "Buy one get one free";
    }
}
