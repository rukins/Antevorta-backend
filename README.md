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

## User
### getUserinfo
```http request
GET /userinfo
```
- Returns:
```json
{
  "id": 1,
  "email": "email@email.com",
  "firstname": "firstname",
  "lastname": "lastname"
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
    },
    {
      "id": 2,
      "product": {
        "title": "some product linker"
      },
      "mergedProducts": [
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
      ],
      "type": "PRODUCT_LINKER"
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
- Body (can have any json, depends on the type.):
```json
{
  "productId": 999999,
  "title": "Some title"
}
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
### update
```http request
PUT /products/{id}
```
- Body (can have any json, depends on the type):
```json
{
  "productId": 999999,
  "title": "Some updated title"
}
```
- Returns:
```json
{
  "id": 1,
  "productId": 999999,
  "title": "Some updated title",
  "product": {
    "productId": 999999,
    "title": "Some updated title"
  },
  "type": "SHOPIFY",
  "arbitraryStoreName": "storeName"
}
```
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
### merge
```http request
POST /products/merge?id=1,2
```
- Body (can have any json):
```json
{
  "title": "some product linker"
}
```
- Returns:
```json
{
  "id": 1,
  "product": {
    "title": "some product linker"
  },
  "mergedProducts": [
    {
      "id": 1,
      "productId": 888888,
      "title": "Some title",
      "product": {
        "productId": 888888,
        "title": "Some title"
      },
      "type": "SHOPIFY",
      "arbitraryStoreName": "storeName"
    },
    {
      "id": 2,
      "productId": 999999,
      "title": "Some title",
      "product": {
        "productId": 999999,
        "title": "Some title"
      },
      "type": "AMAZON",
      "arbitraryStoreName": "anotherStoreName"
    }
  ],
  "type": "PRODUCT_LINKER"
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