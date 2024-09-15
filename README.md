# Online Bookstore REST API

A Spring Boot application that provides a RESTful API for managing an online bookstore, including book management and shopping cart functionalities.

## Table of Contents

- [Project Description](#project-description)
- Setup and Run Instructions
  - [Prerequisites](#prerequisites)
  - [Installation Steps](#installation-steps)
- API Usage Guide
  - Books API
    - [Add a Book](#add-a-book)
    - [Get All Books](#get-all-books)
    - [Get Book by ID](#get-book-by-id)
    - [Update a Book](#update-a-book)
    - [Delete a Book](#delete-a-book)
    - [Search Books](#search-books)
  - Cart API
    - [Create a Cart](#create-a-cart)
    - [Add Item to Cart](#add-item-to-cart)
    - [Update Item Quantity](#update-item-quantity)
    - [Remove Item from Cart](#remove-item-from-cart)
    - [Get Cart Details](#get-cart-details)
    - [Get Cart Total Price](#get-cart-total-price)
- [Design Decisions](#design-decisions)
- Executing Tests
  - [Running Unit Tests](#running-unit-tests)
  - [Running Integration Tests](#running-integration-tests)

------

## Project Description

This project is a RESTful API for an online bookstore, built using Spring Boot. It allows users to:

- **Manage Books**: Add, update, delete, and retrieve books.
- **Manage Shopping Cart**: Create a cart, add items, update item quantities, remove items, and calculate the total price.

------

## Setup and Run Instructions

### Prerequisites

- **Java Development Kit (JDK) 17 or higher**: Ensure Java is installed and `JAVA_HOME` is set.
- **Maven**: Build tool for Java projects.
- **Git**: For cloning the repository (if necessary).

### Installation Steps

1. **Clone the Repository**

   ```
   git clone https://github.com/your-username/bookstore.git
   cd bookstore
   ```

2. **Build the Project**

   Use Maven to build the project and download dependencies:

   ```
   mvn clean package
   ```
   
3. **Run the Application**

   ```Bash
   mvn spring-boot:run
   ```
   
   Or run the generated JAR file:
   
   ```
    java -jar target/bookstore-0.0.1-SNAPSHOT.jar
   ```

4. **Access the Application**

   The application will start on `http://localhost:8080`.

5. **Access H2 Database Console (Optional)**

   - **URL**: `http://localhost:8080/h2-console`
   - **JDBC URL**: `jdbc:h2:mem:testdb`
   - **Username**: `sa`
   - **Password**: `password`

6. **Access Swagger UI for API Documentation**

   - **URL**: `http://localhost:8080/swagger-ui/index.html`

7. **Load Dummy Data to H2(optional)**

   - **Login into the H2 console**: `http://localhost:8080/h2-console`
   - **Run the dummy sql script via the H2 console, the script is under the resouces folder**

------

## API Usage Guide

### Books API

#### Add a Book

- **Endpoint**: `/api/books/add`

- **Method**: `POST`

- **Request Body**:

  ```JSON
  {
    "title": "Effective Java",
    "author": "Joshua Bloch",
    "category": "Programming",
    "price": 45.0,
    "discount": 0,
    "publisher": "Addison-Wesley",
    "publicationDate": "2018-01-06"
  }
  ```

- **Response**: Returns the created book object with an assigned `id`.

#### Get All Books

- **Endpoint**: `/api/books/all`
- **Method**: `GET`
- **Response**: A list of all books.

#### Get Book by ID

- **Endpoint**: `/api/books/{id}`
- **Method**: `GET`
- **Path Variable**: `id` - The ID of the book.
- **Response**: The book object with the specified ID.

#### Update a Book

- **Endpoint**: `/api/books/{id}/update`

- **Method**: `PUT`

- **Path Variable**: `id` - The ID of the book to update.

- **Request Body**:

  ```JSON
  {
    "title": "Effective Java - 3rd Edition",
    "author": "Joshua Bloch",
    "category": "Programming Lang",
    "price": 50.0,
    "discount": 5,
    "publisher": "Addison-Wesley",
    "publicationDate": "2018-01-06"
  }
  ```

- **Response**: The updated book object.

#### Delete a Book

- **Endpoint**: `/api/books/{id}/delete`
- **Method**: `DELETE`
- **Path Variable**: `id` - The ID of the book to delete.
- **Response**: `200 OK` if the deletion is successful.

#### Search Books

- **Endpoint**: `/api/books/search`

- **Method**: `GET`

- **Query Parameters**:

  - `keyword` - The search keyword (title, author, or category).

- **Response**: A list of books matching the search criteria.

  **Example**:

  ```http
  GET /api/books/search?keyword=Java
  ```

### Cart API

#### Create a Cart

- **Endpoint**: `/api/cart/create`
- **Method**: `POST`
- **Response**: The created cart object with an assigned `cartId`.

#### Add Item to Cart

- **Endpoint**: `/api/cart/{cartId}/add`

- **Method**: `POST`

- **Path Variable**: `cartId` - The ID of the cart.

- **Query Parameters**:

  - `bookId` - The ID of the book to add.
  - `quantity` - The quantity of the book.

- **Response**: The updated cart object.

  **Example**:

  ```HTTP
  POST /api/cart/1/add?bookId=2&quantity=3
  ```

#### Update Item Quantity

- **Endpoint**: `/api/cart/{cartId}/update/{itemId}`

- **Method**: `PUT`

- **Path Variables**:
- `cartId` - The ID of the cart.
  - `itemId` - The ID of the cart item.

- **Query Parameters**:
  - `quantity` - The new quantity.
  
- **Response**: The updated cart object.

#### Remove Item from Cart

- **Endpoint**: `/api/cart/{cartId}/remove/{itemId}`

- **Method**: `DELETE`

- Path Variables:

  - `cartId` - The ID of the cart.
  - `itemId` - The ID of the cart item to remove.
  
- **Response**: The updated cart object.

#### Get Cart Details

- **Endpoint**: `/api/cart/{cartId}`
- **Method**: `GET`
- **Path Variable**: `cartId` - The ID of the cart.
- **Response**: The cart object with all its items.

#### Get Cart Total Price

- **Endpoint**: `/api/cart/{cartId}/total`
- **Method**: `GET`
- **Path Variable**: `cartId` - The ID of the cart.
- **Response**: The total price of all items in the cart.

------

## Design Decisions

1. **Framework Choice**: Spring Boot was chosen for its rapid development capabilities, auto-configuration, and ease of integration with other Spring projects like Spring Data JPA and Spring MVC.
2. **In-Memory Database**: H2 was used as an in-memory database for simplicity and ease of setup, eliminating the need for external database configuration.
3. **Layered Architecture**:
   - **Controller Layer**: Handles HTTP requests and maps them to the service layer.
   - **Service Layer**: Contains business logic and interacts with the repository layer.
   - **Repository Layer**: Interacts with the database using Spring Data JPA.
   - **Model Layer**: Defines the data models/entities.
4. **Entity Relationships**:
   - `Cart` and `CartItem`: One-to-many relationship to model a shopping cart containing multiple items.
   - `CartItem` and `Book`: Many-to-one relationship since multiple cart items can reference the same book.
5. **RESTful API Design**: Follows REST principles with clear endpoints and appropriate HTTP methods (`GET`, `POST`, `PUT`, `DELETE`).
6. **Exception Handling**: Basic exception handling is implemented to return appropriate HTTP status codes (e.g., `404 Not Found` when a resource isn't found).
7. **Swagger Integration**: Swagger UI is integrated using Springdoc OpenAPI for easy API documentation and testing.
8. **Testing**:
   - **Unit Tests**: Test individual units of code in isolation (e.g., services).
   - **Integration Tests**: Test the application components' interaction with each other (e.g., controllers, repositories).

------

## Executing Tests

### Running Unit Tests

Unit tests are located in the `src/test/java/com/demo/bookstore/service` directory.

To run unit tests:

```bash
mvn test -Dtest=BookServiceTest,CartServiceTest
```

This command runs the `BookServiceTest` and `CartServiceTest` unit tests.

### Running Integration Tests

Integration tests are located in the `src/test/java/com/demo/bookstore/integration` directory.

To run integration tests:

```bash
mvn test -Dtest=BookControllerIntegrationTest,CartControllerIntegrationTest
```

This command runs the `BookControllerIntegrationTest` and `CartControllerIntegrationTest`.

### Running All Tests

To execute all tests (unit and integration):

```BASH
mvn clean test
```

**Note**: Ensure the application is not running when executing tests, as they may use the same ports and resources.
