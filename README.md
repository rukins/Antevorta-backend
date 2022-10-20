# Inventory_Management_System. Backend side

## Authentication
### Login
```http request
POST /auth/login
```
- Body:
```json
{
  "email": "email@email.com",
  "password": "password"
}
 ```
- Response if there is no problem:
```json
{
  "status": 200,
  "message": "Logged in successfully",
  "path": "/auth/login"
}
```
- Response if there are problems:
```json
{
  "status": 401,
  "message": "User with first@gmail.co email is not found",
  "path": "/auth/login"
}
```
OR
```json
{
  "status": 401,
  "message": "Wrong password",
  "path": "/auth/login"
}
```

### Logout
```http request
POST /auth/logout
```
- Response if there is no problem:
```json
{
  "status": 200,
  "message": "Logged out successfully",
  "path": "/auth/logout"
}
```

## Signing up
```http request
POST /signup
```
- Body:
```json
{
  "firstname": "firstname",
  "lastname": "lastname",
  "email": "email@email.com",
  "password": "password"
}
```
- Response if there is no problem:
```json
{
  "status": 201,
  "message": "Signed up successfully",
  "path": "/signup"
}
```
- Response if there are problems:
```json
{
  "status": 400,
  "message": "User with first@gmail.com email already exists",
  "path": "/signup"
}
```
OR
```json
{
  "status": 400,
  "message": "Incorrect email",
  "path": "/signup"
}
```
OR
```json
{
  "status": 400,
  "message": "Please enter your first and last names. One of them or both are empty",
  "path": "/signup"
}
```

## Online stores
### getAllOnlineStoresOfCurrentUser
```http request
GET /onlinestores
```
- Body:
```json
{
  "onlineStores": [
    {
      "id": "first@gmail.com:storeName",
      "arbitraryStoreName": "storeName",
      "type": "SHOPIFY"
    }
  ]
}
```
### addOnlineStoreToUser
```http request
POST /onlinestores
```
- Body:
```json
{
  "type": "SHOPIFY",
  "arbitraryStoreName": "storeName",
  "storeName": "someStoreName",
  "accessKey": "someAccessKey"
}
```
- Response if there is no problem:
```json
{
  "status": 201,
  "message": "Online store 'storeName' successfully added",
  "path": "/onlinestores"
}
```
- Response if there are problems:
```json
{
  "status": 400,
  "message": "Arbitrary store name shouldn't be empty",
  "path": "/onlinestores"
}
```
OR
```json
{
  "status": 400,
  "message": "Store with 'SHOPIFY' type already exists",
  "path": "/onlinestores"
}
```
OR
```json
{
  "status": 400,
  "message": "Store with 'storeName' arbitrary name already exists",
  "path": "/onlinestores"
}
```
OR
```json
{
  "status": 400,
  "message": "Store with 'SHOPIFY' type already exists",
  "path": "/onlinestores"
}
```

### updateOnlineStoreName
```http request
PUT /onlinestores/{currentName}?newName={newStoreName}
```
- Response if there is no problem:
```json
{
  "status": 200,
  "message": "Name 'storeName' of online store successfully updated to 'newStoreName'",
  "path": "/onlinestores/storeName?newName=newStoreName"
}
```
- Response if there are problems:
```json
{
  "status": 400,
  "message": "Arbitrary store name shouldn't be empty",
  "path": "/onlinestores/storeName?newName="
}
```
OR
```json
{
  "status": 400,
  "message": "Online store with 'anotherName' name not found",
  "path": "/onlinestores/anotherName?newName=newStoreName"
}
```

### deleteOnlineStoreByName
```http request
DELETE /onlinestores/{name}
```
- Response if there is no problem:
```json
{
  "status": 200, 
  "message": "Online store 'storeName' successfully deleted",
    "path": "/onlinestores/storeName"
}
```
- Response if there are problems:
```json
{
    "status": 400,
    "message": "Online store with 'storeNam' name not found",
    "path": "/onlinestores/storeNam"
}
```

## ___Other response bodies___
```json
{
  "status": 401,
  "message": "Unauthorized",
  "path": "/somePath"
}
```
```json
{
  "status": 404,
  "message": "404 NOT_FOUND",
  "path": "/error"
}
```
```json
{
  "status": 400,
  "message": "400 BAD_REQUEST",
  "path": "/error"
}
```
```json
{
  "status": 405,
  "message": "405 METHOD_NOT_ALLOWED",
  "path": "/error"
}
```