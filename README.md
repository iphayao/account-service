# Account Service

- Require:
  - MySQL

## How to run
After cloned this repo into your machine, follow this step:

### Configuration .yml file
Open application.yml file in `src/main/resource/application.yml` set `url` of your local MySQL database and `username` & `password` of your MySQL user.

### Run Spring Boot
To run application in your local machine use this command, it will compile, test and start this application.

```
$ mvn spring-boot:run
```

### Get Authorization Access Token
Use **POSTMAN** reqeust to `http://localhost:8080/oauth/token` with Authorization type **Basic Auth** username= `app-client` & password= `noonewilleverguess` and parameters `scope` = `openid` & `grant_type` = `client_credentials`

You will get response like this with `access_token`

```
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJvcGVuaWQiXSwiZXhwIjoxNTcxNTI0MzUzLCJqdGkiOiI5MTNkOTNiYi05MzMxLTQwYzMtOGU4Ny1jZjdhZDM3ZDdkMjIiLCJjbGllbnRfaWQiOiJhcHAtY2xpZW50In0.nXVYQwKpW62s4Hn3q1eeg_Q9_XD56dSrcxMdBIujHtI",
    "token_type": "bearer",
    "expires_in": 43199,
    "scope": "openid",
    "jti": "913d93bb-9331-40c3-8e87-cf7ad37d7d22"
}
```

### Register Account
**POST** Request to Account Service endpoint `http://localhost:8080/api/accounts` with request body as information of account and Authorization type **Bearer Token** with access token.

```
POST http://localhost:8080/api/accounts

Body:
{
	"username": "john.doe",
	"password": "P@ssw0rd",
	"first_name": "John",
	"last_name": "Done",
	"phone_number": "08123456789",
	"address": "123, Bangkok",
	"email": "john.doe@gmail.com",
	"salary": 20000
}

Response:
{
    "status": "Created",
    "data": {
        "username": "john.doe",
        "firstName": "John",
        "lastName": "Done",
        "phoneNumber": "08123456789",
        "address": "123, Bangkok",
        "email": "john.doe@gmail.com",
        "salary": 20000.0,
        "memberType": "SILVER",
        "referenceCode": "201910206789"
    }
}

```

### Retrive Account
**GET** Requst to Account Service endpoint `http://localhost:8080/api/accounts/{reference_code}` with Authorization type **Bearer Token** with access token then will get response as account information.

```
{
    "status": "OK",
    "data": {
        "username": "john.doe",
        "firstName": "John",
        "lastName": "Done",
        "phoneNumber": "08123456789",
        "address": "123, Bangkok",
        "email": "john.doe@gmail.com",
        "salary": 20000.0,
        "memberType": "SILVER",
        "referenceCode": "201910206789"
    }
}
```
