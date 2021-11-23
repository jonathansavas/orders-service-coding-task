package com.github.savas.ordersservice;

import com.github.savas.ordersservice.exceptions.MalformedJsonRequestException;
import com.github.savas.ordersservice.exceptions.NoItemsAddedException;
import com.github.savas.ordersservice.exceptions.ProductNotFoundException;
import com.github.savas.ordersservice.orders.OrderHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableAutoConfiguration
public class OrdersService {

    public static void main(String[] args) {
        SpringApplication.run(OrdersService.class, args);
    }

    public static void stop() {
        SpringApplication.exit(
            new SpringApplicationBuilder(OrdersService.class).web(WebApplicationType.NONE).run(),
            () -> 0
        );
    }

    @PostMapping("/order")
    String takeOrder(@RequestBody String order) {
        return OrderHandler.takeOrder(order);
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MalformedJsonRequestException.class)
    public String handleMalformedJsonRequestException(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoItemsAddedException.class)
    public String handleNoItemsAddedException(Exception e) {
        return e.getMessage();
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ProductNotFoundException.class)
    public String handleProductNotFoundException(Exception e) {
        return e.getMessage();
    }
}
