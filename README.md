# API Spec

## Authentication
All API must use this auth
Request :
- Header :
    - X-Api-Key : "Your api keys"

## - USER AUTHENTICATION

## Register User :

Request :
- Method : POST
    - Endpoint : `/api/auth/register`
    - Header :
        - Content-Type : application/json
        - Accept : application/json
    - Body : 
        ```json 
        {
          "username": "String",
          "email": "String",
          "password": "String"
        }
        ```
  - Response :
      ```json
      {
        "code" : "number",
        "status": "string",
        "data" : {
          "id" : "String, unique",
          "username" : "String",
          "email" : "String",
          "created at" : "date"
        }
      }
      ```

## Login User
Request : 
- Method : POST
  - Endpoint : `/api/auth/login`
  - Header : 
    - Content-Type : application/json
    - Accept : application/json
  - Body :
    ```json
    {
      "username" : "String",
      "password" : "String"
    }
    ```
    - Response : 
    ```json
    {
      "code" : "number",
      "status" : "String",
      "data" : {
        "token" : "String",
        "expiresAt" : "Date"
      }
    }
    ```

## Logout
- Request : 
  - Method : POST
    - Endpoint : `/api/auth/logout`
    - Header :
      - Content-Type : application/json
      - Accept : application/json
  - Header :
      - X-Api-Key : "Your api keys"

    - Response : 
```json
{
  "code" : "number",
  "status" : "String"
}
```
## Item Management


## Create Product
Request :
- Method : POST
  - Endpoint : `/api/products`
  - Header :
    - Content-Type : application/json
    - Accept : application/json
  - Body :
```json 
{
  "id" : "String, unique",
  "name" : "Srting",
  "price" :"long",
  "quantity" : "integer"
} 
  ```
- Response :
```json 
    {
  "code" : "number",
  "status" : "string",
  "data" : {
    "id" : "String, unique",
    "name" : "Srting",
    "price" : "long",
    "quantity" : "integer",
    "created at" : "date",
    "updatedAt" : "date"
  }
}
  ```

## Get Product
Request :
- Method : GET
  - Endpoint : `/api/products`
  - Header :
    - Accept : application/json
  - Response :
```json 
     {
  "code" : "number",
  "status" : "string",
  "data" : {
    "id" : "String, unique",
    "name" : "Srting",
    "price" : "long",
    "quantity" : "integer",
    "created at" : "date",
    "updatedAt" : "date"
  }
}
  ```
## Update Product
Request :
- Method : PUT
  - Endpoint : `/api/products/{id_product}`
  - Header :
    - Content-Type : application/json
    - Accept : application/json
  - Body :
```json 
{
  "id" : "String, unique",
  "name" : "Srting",
  "quantity" : "integer"
} 
  ```
- Response :
```json 
    {
  "code" : "number",
  "status" : "string",
  "data" : {
    "id" : "String, unique",
    "name" : "Srting",
    "price" : "Long",
    "quantity" : "integer",
    "created at" : "date",
    "updatedAt" : "date"
  }
}
  ```
## List Product
Request :
- Method : GET
  - Endpoint : `/api/products/`
  - Header :

    - Accept : application/json
  - Query Param :
    - pageSize : number,
    - page : number
  - Body :

  - Response :
```json 
    {
  "code" : "number",
  "status" : "string",
  "data" : [
    {
      "id" : "String, unique",
      "name" : "Srting",
      "price" : "Long",
      "quantity" : "integer",
      "created at" : "date",
      "updatedAt" : "date"
    }, {
      "id" : "String, unique",
      "name" : "Srting",
      "price" : "Long",
      "quantity" : "integer",
      "created at" : "date",
      "updatedAt" : "date"
    }
  ]
}
  ```
## Delete Product
Request :
- Method : DELETE
  - Endpoint : `/api/products/{id_product}`
  - Header :

    - Accept : application/json
  - Body :

  - Response :
```json 
    {
  "code" : "number",
  "status" : "string"
}
  ```