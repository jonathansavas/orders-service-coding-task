package com.github.savas.ordersservice.offers;

public interface Offer {

    double getDiscount(int productQuantity, double productPrice);

    int getNumOffersApplied(int productQuantity);

    String getDescription();
}
