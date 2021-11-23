package com.github.savas.ordersservice.offers;

public class ThreeForTwoOffer implements Offer {

    @Override
    public double getDiscount(int productQuantity, double productPrice) {
        return this.getNumOffersApplied(productQuantity) * productPrice;
    }

    @Override
    public int getNumOffersApplied(int productQuantity) {
        return productQuantity / 3;
    }

    @Override
    public String getDescription() {
        return "3 for price of 2";
    }
}
