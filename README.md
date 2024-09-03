# Concurrent Order Management System

A Spring Boot application that provides a RESTful API for managing orders in a concurrent environment. This system allows creating new orders, updating order statuses, and retrieving orders while ensuring thread-safe operations

- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Structure](#project-structure)
- [Setup and Installation](#setup-and-installation)
- [Testing the API](#testing-the-api)
- [Concurrency Handling](#concurrency-handling)

## Features
* Create Orders: Submit new orders containing multiple items.
* Update Order Status: Modify the status of existing orders.
* Retrieve Orders: Fetch individual orders or list all orders.
* Concurrency Support: Ensures thread-safe operations for managing orders in a concurrent environment.

## Technologies Used
* Java 17 and above
* Spring Boot 3.x
* Lombok
* Maven
* Concurrent Data Structures

## Project Structure
```plaintext
concurrent-order-management/
├── src
│   ├── main
│   │   ├── java/com/example/demo
│   │   │   ├── Controllers
│   │   │   │   └── OrderController.java
│   │   │   ├── Models
│   │   │   │   ├── Order.java
│   │   │   │   └── OrderItem.java
│   │   │   ├── Services
│   │   │   │   ├── OrderService.java
│   │   │   │   └── OrderServiceImpl.java
│   │   │   └── DemoApplication.java
│   │   └── resources
│   │       └── application.properties
├── pom.xml
└── README.md

```
### Directory and File Explanations
* Controllers/OrderController.java: Handles HTTP requests related to orders. Defines endpoints for creating, updating, and retrieving orders.
* Models/Order.java: Represents the Order entity containing order details and a list of order items.
* Models/OrderItem.java: Represents individual items within an order.
* Services/OrderService.java: Defines the service interface for order operations.
* Services/OrderServiceImpl.java: Implements the OrderService interface, providing thread-safe operations using concurrent data structures.
* DemoApplication.java: The main entry point of the Spring Boot application.
* pom.xml: Maven configuration file managing project dependencies and build configurations.

## Setup and Installation
1. **Clone the repository:**

   ```bash
   git clone https://github.com/gulshanb098/concurrent-order-management.git
   ```
2. **Build the project:**
    ```bash
   cd concurrent-order-management/
   mvn clean install
   ```
3. **Run the Application:**
    ```bash
   mvn spring-boot:run
   ```

## Testing the API
1. **Create a New Order:**
    ```bash
    curl -X POST http://localhost:8080/orders \
     -H "Content-Type: application/json" \
     -d '{
           "status": "NEW",
           "items": [
             {
               "productName": "Product A",
               "quantity": 2
             },
             {
               "productName": "Product B",
               "quantity": 5
             }
           ]
         }'

    ```

2. **Update Order Status:**
    ```bash
    curl -X PUT "http://localhost:8080/orders/1/status?status=SHIPPED"
    ```

3. **Get Order by ID:**
    ```bash
    curl -X GET http://localhost:8080/orders/1
    ```

4. **Get All Orders:**
    ```bash
    curl -X GET http://localhost:8080/orders
    ```

## Concurrency Handling
Concurrency is a critical aspect of this application, ensuring that multiple threads can safely interact with the order data without causing inconsistencies or data corruption. Here's how concurrency is managed:

### Thread-Safe Data Structures
* **Synchronized List**: The orders list is wrapped with ***Collections.synchronizedList(new ArrayList<>())***, ensuring that all operations on the list are thread-safe. This prevents concurrent modification issues when multiple threads add or modify orders simultaneously.
    ```java
    private final List<Order> orders = Collections.synchronizedList(new ArrayList<>());
    ```

* **AtomicLong for ID Generation**: The ***orderIdGenerator*** is an instance of ***AtomicLong***, which provides atomic operations for generating unique order IDs without the need for explicit synchronization.
    ```java
    private final AtomicLong orderIdGenerator = new AtomicLong();
    ```

* **Synchronized Blocks**: Although the list is synchronized, iterating over it requires additional synchronization to prevent ***ConcurrentModificationException***. Therefore, synchronized (orders) blocks are used when searching for an order by ID to ensure thread safety during iteration.
    ```java
    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        synchronized (orders) {
            for (Order order : orders) {
                if (order.getId().equals(orderId)) {
                    order.setStatus(status);
                    return order;
                }
            }
        }
        return null;
    }
    ```

### Benefits
* **Consistency**: Ensures that all read and write operations on orders are performed consistently, preventing race conditions.
* **Scalability**: Allows the application to handle multiple concurrent requests efficiently without compromising data integrity.
* **Reliability**: Reduces the risk of data corruption and ensures that order operations behave predictably under concurrent access.
