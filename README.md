# orders-service-coding-task

## Endpoints

###Place an order:

POST /order

Example: 

POST http://localhost:8080/order

{
    "apple": 2,
    "orange": 5
}


###Get a past order by id:

GET /orders/{id}

Example:

GET http://localhost:8080/orders/1


###Get all past orders:

GET /orders

Example: 

GET http://localhost:8080/orders
