package com.github.savas.ordersservice.orders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.savas.ordersservice.exceptions.MalformedJsonRequestException;
import com.github.savas.ordersservice.exceptions.NoItemsAddedException;
import com.github.savas.ordersservice.exceptions.ProductNotFoundException;
import com.github.savas.ordersservice.products.Apple;
import com.github.savas.ordersservice.products.Orange;
import com.github.savas.ordersservice.products.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderHandler {
    private static final Map<String, Product> products = new HashMap<String, Product>() {{
        put("apple", new Apple());
        put("orange", new Orange());
    }};

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String takeOrder(String orderString) {
        Map<String, Integer> orderReq;

        try {
            orderReq = objectMapper.readValue(orderString, new TypeReference<Map<String, Integer>>() {
            });
        } catch (JsonProcessingException e) {
            throw new MalformedJsonRequestException(e.getMessage());
        }

        Order order = new Order();
        List<String> notFoundProducts = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : orderReq.entrySet()) {
            String productName = entry.getKey();
            Product p = products.get(productName);

            if (p == null) {
                notFoundProducts.add(productName);
            }

            order.addOrderEntry(new OrderEntry(p, entry.getValue()));
        }

        if (notFoundProducts.size() > 0) {
            throw new ProductNotFoundException("The following products were not found: "
                + notFoundProducts);
        }

        if (order.getTotalQuantity() == 0) {
            throw new NoItemsAddedException("No items were requested in the order.");
        }

        return createOrderSummary(order);
    }

    public static String createOrderSummary(Order order) {
        StringBuilder summary = new StringBuilder();
        summary.append("Order Summary:\n");
        for (OrderEntry entry : order.getOrderEntries()) {
            summary.append(entry.getEntryString()).append("\n");
        }

        summary.append("Total quantity: ").append(order.getTotalQuantity()).append("\n");
        summary.append("Total: $").append(order.getTotal());

        return summary.toString();
    }
}
