# products-api

## Overview:
This API allows users to manage products, which includes creating new products, updating existing ones, and deleting them. Users can also retrieve information about a specific product by its ID or name.

## Endpoints:

- **GET /api/v1/products**
Retrieves all products from the system.
Response: List of ProductDto objects.

- **GET /api/v1/products/{id}**  
Retrieves the product with the given ID.
Parameters: id - Long  
Response: ProductDto object.

- **GET /api/v1/products/name/{name}**  
Retrieves the product with the given name.  
Parameters: name - String  
Response: ProductDto object.  

- **POST /api/v1/products**  
Creates a new product in the system.  
Request Body: CreateProductRequest object.  
Response: ProductDto object.  

- **PUT /api/v1/products/{id}**  
Updates an existing product in the system.  
Parameters: id - Long  
Request Body: UpdateProductRequest object.  
Response: ProductDto object.  

- **DELETE /api/v1/products/{id}**  
Deletes the product with the given ID from the system.  
Parameters: id - Long  

- **GET /api/v1/products/page**
Gets a page of products.  
Query parameters:  
pageNo (Integer): The page number (default: 0)  
pageSize (Integer): The page size (default: 10)  
sortBy (String): The field to sort by (default: "price")  
sortDirection (String): The sort direction (default: "DESC")  