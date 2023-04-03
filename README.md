# Inventory Management System (Training project). Backend side

## General info
Inventory management system for e-commerce. 
The API allows you to manage data from different online stores 
like Amazon, Walmart, Etsy, Shopify, Ebay etc. and stores it in one place.

Initially the user creates an account, 
then they connect their e-commerce stores, 
and then when the stores are connected the data populates the app, 
showing the inventory items(products)' data (title, price, quantity, etc.). 
Through the API you can read, create, update and delete inventory items(products).
All data is pulled from online store's API.

For now the following stores are available:
- Shopify
- Ebay

What improvements can be made:
- More e-commerce stores (Amazon, Walmart, Etsy, etc.)
- Update the method of user authorization, maybe integrate OAuth2. 
Now it doesn't provide very good protection
- Use hashing instead of encryption for user passwords
- Inventory item reports
- Webhooks for inventory items
- More data to manage in the app (for example, orders)

## Auth
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
GET /user
```
- Returns:
```json
{
  "user": {
    "id": 1,
    "email": "email@email.com",
    "firstname": "firstname",
    "lastname": "lastname",
    "verified": false,
    "authorities": [
      {
        "name": "someAuthority"
      }
    ]
  }
}
```
### updateUser
```http request
PUT /user?field=
```
- Parameter _field_ can be equal to `firstname, lastname`, `firstname`, `lastname`, `email` or `password` (if it is equal to the last two, the session will be logged out)
- Body depends on what you want to update
- Returns:
```json
{
  "user": {
    "id": 1,
    "email": "new@email.com",
    "firstname": "new",
    "lastname": "new",
    "verified": false,
    "authorities": []
  }
}
```
### verify
```http request
POST /user/verification/verify
```
- Returns:
```json
{
  "status": 200,
  "message": "User email has been successfully verified"
}
```
### sendVerificationCode
```http request
POST /user/verification/mail
```
- Returns:
```json
{
  "status": 200,
  "message": "Verification code has been successfully sent"
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
  "credentials": {
    "storeName": "someStoreName",
    "token": "",
    "extra": {
      "username": "username",
      "password": "password"
    }
  }
}
```
`credentials` depends on inventory item type.
1. _Shopify_ requires `storeName` and `token`
2. _Ebay_ requires `token` and all `extra` values (`storeName` is required only if this store is sandbox - _api.sandbox_. Default value - _api_) 
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
GET /inventoryitems/{id}
```
- Returns:
```json
{
  "id": 1,
  "inventoryId": "999999",
  "title": "Some title",
  "inventoryItem": {
    "productId": 999999,
    "title": "Some title"
  },
  "type": "SHOPIFY",
  "arbitraryStoreName": "storeName"
}
```
`inventoryItem` can have any json, depends on the type.
### getAll
```http request
GET /inventoryitems
```
- Returns:
```json
{
  "inventoryItems": [
    {
      "id": 1,
      "inventoryId": "999999",
      "title": "Some title",
      "inventoryItem": {
        "productId": "999999",
        "title": "Some title"
      },
      "type": "SHOPIFY",
      "arbitraryStoreName": "storeName"
    },
    {
      "id": 2,
      "inventoryItem": {
        "title": "some product linker"
      },
      "mergedProducts": [
        {
          "id": 1,
          "inventoryId": "999999",
          "title": "Some title",
          "inventoryItem": {
            "productId": "999999",
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
`inventoryItem` can have any json, depends on the type.
### getAllByArbitraryStoreName
```http request
GET /onlinestores/{arbitraryStoreName}/inventoryitems
```
- Returns:
```json
{
  "inventoryItems": [
    {
      "id": 1,
      "inventoryId": "999999",
      "title": "Some title",
      "inventoryItem": {
        "productId": "999999",
        "title": "Some title"
      },
      "type": "SHOPIFY",
      "arbitraryStoreName": "storeName"
    }
  ]
}
```
`inventoryItem` can have any json, depends on the type.
### create
```http request
POST /onlinestores/{arbitraryStoreName}/inventoryitems
```
- Body (can have any json, depends on the type.):
```json
{
  "productId": "999999",
  "title": "Some title"
}
```
- Returns:
```json
{
  "id": 1,
  "inventoryId": "999999",
  "title": "Some title",
  "inventoryItem": {
    "productId": "999999",
    "title": "Some title"
  },
  "type": "SHOPIFY",
  "arbitraryStoreName": "storeName"
}
```
### update
```http request
PUT /inventoryitems/{id}
```
- Body (can have any json, depends on the type):
```json
{
  "productId": "999999",
  "title": "Some updated title"
}
```
- Returns:
```json
{
  "id": 1,
  "inventoryId": "999999",
  "title": "Some updated title",
  "inventoryItem": {
    "productId": "999999",
    "title": "Some updated title"
  },
  "type": "SHOPIFY",
  "arbitraryStoreName": "storeName"
}
```
### delete
```http request
DELETE /inventoryitems/{id}
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
POST /inventoryitems/merge?id=1,2
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
  "inventoryItem": {
    "title": "some product linker"
  },
  "mergedProducts": [
    {
      "id": 1,
      "productId": 888888,
      "title": "Some title",
      "inventoryItem": {
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
      "inventoryItem": {
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
POST /inventoryitems/update
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