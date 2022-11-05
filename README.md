# Inventory_Management_System. Backend side

## Authorization
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
- Returns:
```json
{
  "status": 200,
  "message": "Logged in successfully"
}
```
### Logout
```http request
POST /auth/logout
```
- Returns:
```json
{
  "status": 200,
  "message": "Logged out successfully"
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
- Returns:
```json
{
  "status": 201,
  "message": "Signed up successfully"
}
```

## Online stores
### getAll
```http request
GET /onlinestores
```
- Returns:
```json
{
  "onlineStores": [
    {
      "id": {
        "ordinal": 1,
        "email": "email@email.com"
      },
      "arbitraryStoreName": "storeName",
      "type": "SHOPIFY"
    },
    {
      "id": {
        "ordinal": 2,
        "email": "email@email.com"
      },
      "arbitraryStoreName": "anotherStoreName",
      "type": "AMAZON"
    }
  ]
}
```
### addToUser
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
- Returns:
```json
{
  "onlineStore": {
    "id": {
      "ordinal": 1,
      "email": "email@email.com"
    },
    "arbitraryStoreName": "storeName",
    "type": "SHOPIFY"
  }
}
```
### updateArbitraryStoreName
```http request
PUT /onlinestores/{currentName}?new={newName}
```
- Returns:
```json
{
  "onlineStore": {
    "id": {
      "ordinal": 1,
      "email": "email@email.com"
    },
    "arbitraryStoreName": "newName",
    "type": "SHOPIFY"
  }
}
```
### deleteByArbitraryStoreName
```http request
DELETE /onlinestores/{name}
```
- Returns:
```json
{
  "status": 200, 
  "message": "Online store 'storeName' has been successfully deleted"
}
```

## Products
### getById
```http request
GET /products/{id}
```
- Returns:
```json
{
  "id": 1,
  "productId": 999999,
  "title": "Some title",
  "product": {
    "productId": 999999,
    "title": "Some title"
  },
  "type": "SHOPIFY",
  "arbitraryStoreName": "storeName"
}
```
`product` can have any json, depends on the type.
### getAll
```http request
GET /products
```
- Returns:
```json
{
  "products": [
    {
      "id": 1,
      "productId": 999999,
      "title": "Some title",
      "product": {
        "productId": 999999,
        "title": "Some title"
      },
      "type": "SHOPIFY",
      "arbitraryStoreName": "storeName"
    }
  ]
}
```
`product` can have any json, depends on the type.
### getAllByArbitraryStoreName
```http request
GET /onlinestores/{arbitraryStoreName}/products
```
- Returns:
```json
{
  "products": [
    {
      "id": 1,
      "productId": 999999,
      "title": "Some title",
      "product": {
        "productId": 999999,
        "title": "Some title"
      },
      "type": "SHOPIFY",
      "arbitraryStoreName": "storeName"
    }
  ]
}
```
`product` can have any json, depends on the type.
### create
```http request
POST /onlinestores/{arbitraryStoreName}/products
```
- Returns created product:
```json
{
  "id": 1,
  "productId": 999999,
  "title": "Some title",
  "product": {
    "productId": 999999,
    "title": "Some title"
  },
  "type": "SHOPIFY",
  "arbitraryStoreName": "storeName"
}
```
`product` can have any json, depends on the type.
### update
```http request
PUT /products/{id}
```
- Returns updated product:
```json
{
  "id": 1,
  "productId": 999999,
  "title": "Some title",
  "product": {
    "productId": 999999,
    "title": "Some title"
  },
  "type": "SHOPIFY",
  "arbitraryStoreName": "storeName"
}
```
`product` can have any json, depends on the type.
### delete
```http request
DELETE /products/{id}
```
- Returns:
```json
{
  "status": 200,
  "message": "Product with id '1' has been successfully deleted"
}
```
### updateProductList
```http request
POST /products/update
```
- Returns:
```json
{
  "status": 200,
  "message": "Product list has been successfully updated"
}
```

## ___Response If there is some problem___
```json lines
{
  "status": STATUS_CODE,
  "message": "MESSAGE"
}
```