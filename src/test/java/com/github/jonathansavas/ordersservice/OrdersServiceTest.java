package com.github.jonathansavas.ordersservice;

import com.github.savas.ordersservice.OrdersService;
import com.github.savas.ordersservice.products.Apple;
import com.github.savas.ordersservice.products.Orange;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OrdersServiceTest {

    private static final String host = "http://localhost:8080";

    private final RestTemplate rest = new RestTemplateBuilder().build();

    private final double applePrice = new Apple().getPrice();
    private final double orangePrice = new Orange().getPrice();

    @BeforeClass
    public static void setUp() {
        OrdersService.main(new String[]{});
    }

    @AfterClass
    public static void cleanUp() {
      OrdersService.stop();
    }

    @Test
    public void basicOrderRequestTest() {
        String request = "{\"apple\": 1, \"orange\": 1}";
        String uri = "/order";

        ResponseEntity<String> response = this.rest.postForEntity(host + uri, request, String.class);

        Assert.assertTrue(response.getBody().contains("Total quantity: 2"));
        Assert.assertTrue(response.getBody().contains("Total paid: $0.85"));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void multipleQuantityTest() {
        String request = "{\"apple\": 3, \"orange\": 5}";
        String uri = "/order";

        ResponseEntity<String> response = this.rest.postForEntity(host + uri, request, String.class);

        Assert.assertTrue(response.getBody().contains("Total quantity: 8"));
        Assert.assertTrue(response.getBody().contains("Total paid: $" +
                (3 * applePrice + 5 * orangePrice - applePrice - orangePrice) // discount
        ));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void singleItemTest() {
        String request = "{\"apple\": 3}";
        String uri = "/order";

        ResponseEntity<String> response = this.rest.postForEntity(host + uri, request, String.class);
        Assert.assertTrue(response.getBody().contains("Total quantity: 3"));
        Assert.assertTrue(response.getBody().contains("Total paid: $" +
                (3 * applePrice - applePrice)
        ));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void malformedJsonTest() {
        String request = "{\"apple\": \"a\"}";
        String uri = "/order";

        try {
            this.rest.postForEntity(host + uri, request, String.class);
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertSame(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void noItemsAddedTest() {
        String request = "{}";
        String uri = "/order";

        try {
            this.rest.postForEntity(host + uri, request, String.class);
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertSame(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void productNotFoundTest() {
        String request = "{\"appleaplsdf\": 10, \"orange\": 5}";
        String uri = "/order";

        try {
            this.rest.postForEntity(host + uri, request, String.class);
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertSame(HttpStatus.BAD_REQUEST, e.getStatusCode());
        }
    }

    @Test
    public void orderNotFoundTest() {
        String uri = "/orders/999999999";

        try {
            this.rest.getForEntity(host + uri, String.class);
            Assert.fail();
        } catch (HttpClientErrorException e) {
            Assert.assertSame(HttpStatus.NOT_FOUND, e.getStatusCode());
        }
    }

    @Test
    public void getOrderTest() {
        String request = "{\"apple\": 3}";
        String uri = "/order";

        ResponseEntity<String> response = this.rest.postForEntity(host + uri, request, String.class);
        System.out.println(response.getBody());
        long orderId = getOrderIdFromResponse(response.getBody());

        uri = "/orders/" + orderId;
        response = this.rest.getForEntity(host + uri, String.class);

        Assert.assertTrue(response.getBody().contains("Total quantity: 3"));
        Assert.assertTrue(response.getBody().contains("Total paid: $" +
                (3 * applePrice - applePrice)
        ));
        Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void getAllOrdersTest() {
        String request = "{\"apple\": 3}";
        String uri = "/order";

        Set<Long> orderIds = new HashSet<>();

        for (int i = 0; i < 5; i++) {
            ResponseEntity<String> response = this.rest.postForEntity(host + uri, request, String.class);
            orderIds.add(getOrderIdFromResponse(response.getBody()));
        }

        uri = "/orders";

        for (Object orderSummary : this.rest.getForEntity(host + uri, List.class).getBody()) {
            orderIds.remove(getOrderIdFromResponse((String) orderSummary));
        }

        Assert.assertEquals(0, orderIds.size());
    }

    public long getOrderIdFromResponse(String response) {
        return Long.parseLong(response.split("\n")[0].split(": ")[1]);
    }
}
