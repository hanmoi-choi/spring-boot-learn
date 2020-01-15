# How to Test or Run App
## With docker
Initially takes time so please be patient.

1. Run Test
   - `./auto/test`

2. Run App
   - `./auto/run`

# How to use app
I have forced to use `https` but SSL cert is self-signed.
So if you are using `curl`, please use `-k` option to ignore SSL related warning.

## How to get JWT token
### Admin User

> curl -k -X POST -H "CONTENT-TYPE: application/json" --data '{"username":"admin","password":"password1"}' https://localhost:8443/token/generate-token

### Normal User

> curl -k -X POST -H "CONTENT-TYPE: application/json" --data '{"username":"user1","password":"password2"}' https://localhost:8443/token/generate-token

![](./resource/get-token.png)

## How to use `/api/v1/product/**`
Please read [Swagger API DOC](https://localhost:8443/swagger-ui.html#/)

### Example
Get Products

> curl -k -H "CONTENT-TYPE: application/json" -H 'Authorization: Bearer <your-token>'  https://localhost:8443/api/v1/products

![](./resource/get.png)

### Authorization
1. Admin is allowed to use all end-points
2. User is allowed to use only `GET` method

# Todo List
1. Setup CI/CD pipeline configuration
   - This is infrastructure dependent task so not applied here
