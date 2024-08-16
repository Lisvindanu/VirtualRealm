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
        "code : "number",
        "status" : "String"
        }
        ```