package com.github.savas.ordersservice.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.savas.ordersservice.exceptions.MalformedJsonRequestException;
import com.github.savas.ordersservice.exceptions.NoItemsAddedException;
import com.github.savas.ordersservice.exceptions.OrderNotFoundException;
import com.github.savas.ordersservice.exceptions.ProductNotFoundException;
import com.github.savas.ordersservice.offers.BogoOffer;
import com.github.savas.ordersservice.offers.Offer;
import com.github.savas.ordersservice.offers.ThreeForTwoOffer;
import com.github.savas.ordersservice.products.Apple;
import com.github.savas.ordersservice.products.Orange;
import com.github.savas.ordersservice.products.Product;

import java.util.*;
import java.util.stream.Collectors;

public class OrderHandler {
    private static final Map<String, Product> products = new HashMap<String, Product>() {{
        put("apple", new Apple());
        put("orange", new Orange());
    }};

    private static final Map<String, Offer> offers = new HashMap<String, Offer>() {{
        put("apple", new BogoOffer());
        put("orange", new ThreeForTwoOffer());
    }};

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static Map<Long, Order> orders = new HashMap<>();
    private static long orderId = 1;

    public static String takeOrder(String orderString) {
        Map<String, Integer> orderReq;

        try {
            orderReq = objectMapper.readValue(orderString, new TypeReference<Map<String, Integer>>() {
            });
        } catch (JsonProcessingException e) {
            throw new MalformedJsonRequestException(e.getMessage());
        }

        Order order = new Order(orderId);
        List<String> notFoundProducts = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : orderReq.entrySet()) {
            String productName = entry.getKey();
            Product p = products.get(productName);

            if (p == null) {
                notFoundProducts.add(productName);
            } else {
                OrderEntry orderEntry = new OrderEntry(p, entry.getValue());
                Offer offer = offers.get(productName);

                if (offer != null) {
                    orderEntry.applyOffer(offer);
                }

                order.addOrderEntry(orderEntry);
            }
        }

        if (notFoundProducts.size() > 0) {
            throw new ProductNotFoundException("The following products were not found: "
                + notFoundProducts);
        }

        if (order.getTotalQuantity() == 0) {
            throw new NoItemsAddedException("No items were requested in the order.");
        }

        orders.put(orderId, order);
        orderId++;

        return createOrderSummary(order);
    }

    public static String getOrder(long orderId) {
        Order order = orders.get(orderId);

        if (order == null) {
            throw new OrderNotFoundException("The following order ID was not found: " + orderId);
        }

        return createOrderSummary(order);
    }

    public static List<String> getAllOrders() {
        return orders.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .map(OrderHandler::createOrderSummary)
                .collect(Collectors.toList());
    }

    public static String createOrderSummary(Order order) {
        StringBuilder summary = new StringBuilder();
        summary.append("Order ID: ").append(order.getOrderId()).append("\n");
        summary.append("Order Summary:\n");
        for (OrderEntry entry : order.getOrderEntries()) {
            summary.append(entry.getEntryString()).append("\n");
        }

        summary.append("Total quantity: ").append(order.getTotalQuantity()).append("\n");
        summary.append("Total paid: $").append(order.getTotal()).append("\n");

        double discount = order.getTotalDiscounts();

        if (discount > 0) {
            summary.append("Total discount: $").append(discount);
        }

        return summary.toString();
    }
}
