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

## Purchase
  ## Create Purchase
- Request :
-  Method: POST
  - Endpoint: /api/purchases
    - Header:
    - Content-Type: application/json
    - Accept: application/json
      - X-Api-Key: "Your API keys"
  - Body :
    ```json
    {
    "userId": "String",
    "productId": "String",
    "quantity": "integer"
    }
     ```

    - Response:
   ``` json
    {
    "code": "number",
    "status": "string",
    "data": {
    "purchaseId": "String",
    "userId": "String",
    "productId": "String",
    "quantity": "integer",
    "totalPrice": "long",
    "createdAt": "date"
      } 
    }
```

## Get Purchase History
- Request : 
  - Method : GET
  - EndPoint : /api/purchases
  - Header :
  - Accept : application/json
    - X-Api-Key: "Your API keys"
  - Query Param :
    - userId : "String"
  - Response :
  ```json
    {
    "code": "number",
    "status": "string",
      "data": [
          {
          "purchaseId": "String",
          "userId": "String",
          "productId": "String",
          "quantity": "integer",
          "totalPrice": "long",
          "createdAt": "date"
          }
      ]
    }
  ```
## Payment
  ## Initiate Payment
  - Method: POST
  - Endpoint: /api/payments/initiate
  - Header:
    - Content-Type: application/json
    - Accept: application/json
    - X-Api-Key: "Your API keys"
  - Body:
```json
{
  "purchaseId": "String",
  "amount": "long"
}
```
  - Response :
```json
{
  "code": "number",
  "status": "string",
  "data": {
    "paymentId": "String",
    "status": "string",
    "amount": "long",
    "createdAt": "date"
  }
}
```

  ## Confirm Payment
  - Method: PUT
  - Endpoint: /api/payments/confirm/{paymentId}
  - Header:
    - Content-Type: application/json
    - Accept: application/json
    - X-Api-Key: "Your API keys"
  - Body:
```json
{
      "status": "String" // Possible values: "confirmed", "failed"
}
```
  - Response:
```json
{
      "code": "number",
      "status": "string",
      "data": {
        "paymentId": "String",
        "status": "string",
        "confirmedAt": "date"
      }
}
```

## Cancel Payment
- Method : PUT
- EndPoint : /api/payments/cancel/{paymentId}
- Header :
  - Content-Type : application/json
  - Accept : application/json
- Body :
```json
{
  "reason": "String"
}
```
- Response :
```json
{
  "code": "number",
  "status": "string"
}
```

## Inventory
  ## Get User Inventory
  - Method: GET
  - Endpoint: /api/inventory
  - Header:
    - Accept: application/json
    - X-Api-Key: "Your API keys"
- Query Parameters:
  - userId: "String"
  - Response:
    ```json
    {
    "code" : "number",
    "status" : "string",
    "data" : [
          {
            "itemId": "String",
            "name": "String",
            "quantity": "integer",
            "lastUpdated": "date"
         }
      ] 
    }
    ```
    
## Use Inventory Item
- Method: POST
- Endpoint: /api/inventory/use
- Header:
  - Content-Type: application/json
  - Accept: application/json
  - X-Api-Key: "Your API keys"
- Body:
```json
{
"userId": "String",
"itemId": "String",
"quantity": "integer"
}
```
- Response:
```json
{
"code": "number",
"status": "string",
"data": {
"userId": "String",
"itemId": "String",
"quantityUsed": "integer",
"remainingQuantity": "integer",
"updatedAt": "date"
}
}
```


## Admin Dashboard
  ### Get Sales Report
- Method: GET
- Endpoint: /api/admin/sales-report
- Header:
  - Accept: application/json
  - X-Api-Key: "Your API keys"
- Response:
```json
{
  "code": "number",
  "status": "string",
  "data": {
  "totalSales": "long",
  "topSellingItems": [
        {
          "itemId": "String",
          "name": "String",
          "quantitySold": "integer",
          "totalRevenue": "long"
        }
      ]
    }
  }
```

## Manage Users
- Method: POST
- Endpoint: /api/admin/users/manage
- Header:
  - Content-Type: application/json
  - Accept: application/json
  - X-Api-Key: "Your API keys"
- Body:
```json
 {
    "userId": "String",
    "action": "String" // Possible values: "delete", "disable", "enable"
    }
```
   
- Response:
```json
  {
    "code": "number",
    "status": "string"
    }
```
    
  
