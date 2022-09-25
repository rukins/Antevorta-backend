# Inventory_Management_System. Backend side

## Authentication
- Http Method: POST
- Headers: "Content-Type: application/json"

### Login
- Path: ```/auth/login```
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
- Path: ```/auth/logout```
- Response if there is no problem:
```json
{
  "status": 200,
  "message": "Logged out successfully",
  "path": "/auth/logout"
}
```

## Signing up
- Http Method: POST
- Headers: "Content-Type: application/json"

- Path: ```/signup```
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


## And only for testing 
#### i have added one endpoint to see all users in database (work only if user who requests is authenticated). I will remove it later.
- Http Method: GET

- Path: ```/users```
- Response if there is no problem:
```json
[
  {
    "id": 1,
    "email": "first@email.com",
    "password": "$2a$12$HRGF7Oe73XF4CcjAfqRpteQN3VDv3SIG3KC3Wo/5SgTUrf81Td2Fe",
    "firstname": "firstname1",
    "lastname": "lastname1"
  },
  {
    "id": 2,
    "email": "second@email.com",
    "password": "$2a$12$tKKDv/mgIxa5ACmWylgpBOoz5AcRdaig68lFSmlI8tY9ct2uDIENK",
    "firstname": "firstname2",
    "lastname": "lastname2"
  },
  {
    "id": 3,
    "email": "third@email.co",
    "password": "$2a$12$2IuZNiCt2DhpRkmiXA9LP.38oj/czL91y9zZZ5ZYAWZ9NNw5uPhuK",
    "firstname": "firstname3",
    "lastname": "firstname3"
  }
]
```
- Response if there are problems:
```json
{
  "status": 401,
  "message": "Unauthorized",
  "path": "/users"
}
```

## ___Other response bodies___
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