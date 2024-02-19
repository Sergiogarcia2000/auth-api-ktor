# AUTHENTICATION IN KTOR

This project is made to test the different types of authenticaiton in KTOR using "authentication" plugin.
Documentation: https://ktor.io/docs/authentication.html

# Basic Authentication

For basic authentication we will use this dependency
>  `implementation("io.ktor:ktor-server-auth:$ktor_version")`

The basic authentication flow looks as follows:

1.  A client makes a request without the `Authorization` header to a specific [route](https://ktor.io/docs/routing-in-ktor.html) in a server application.

2.  A server responds to a client with a `401` (Unauthorized) response status and uses a `WWW-Authenticate` response header to provide information that the basic authentication scheme is used to protect a route. A typical `WWW-Authenticate` header looks like this:

    ```
    WWW-Authenticate: Basic realm="Access to the '/' path", charset="UTF-8"
    ```


-   In Ktor, you can specify the realm and charset using corresponding properties when [configuring](https://ktor.io/docs/basic.html#configure-provider) the `basic` authentication provider.

-   Usually, a client displays a login dialog where a user can enter credentials. Then, a client makes a request with the `Authorization` header containing a username and password pair encoded using Base64, for example:

    ```
    Authorization: Basic amV0YnJhaW5zOmZvb2Jhcg
    ```

-   A server [validates](https://ktor.io/docs/basic.html#configure-provider) credentials sent by a client and responds with the requested content.

Example of usage with curl:
> `curl -v -u "sergio:1234" "http://localhost:8080/basic"`

# Digest Authentication

For digest authentication we will use this dependency
>  `implementation("io.ktor:ktor-server-auth:$ktor_version")`

The digest authentication flow looks as follows:

1.  A client makes a request without the `Authorization` header to a specific [route](https://ktor.io/docs/routing-in-ktor.html) in a server application.

2.  A server responds to a client with a `401` (Unauthorized) response status and uses a `WWW-Authenticate` response header to provide information that the digest authentication scheme is used to protect a route. A typical `WWW-Authenticate` header looks like this:

    ```
    WWW-Authenticate: Digest
            realm="Access to the '/' path",
            nonce="e4549c0548886bc2",
            algorithm="MD5"
    ```


-   In Ktor, you can specify the realm and the way of generating a nonce value when [configuring](https://ktor.io/docs/digest.html#configure-provider) the `digest` authentication provider.

-   Usually a client displays a login dialog where a user can enter credentials. Then, a client makes a request with the following `Authorization` header:

    ```
    Authorization: Digest username="jetbrains",
            realm="Access to the '/' path",
            nonce="e4549c0548886bc2",
            uri="/",
            algorithm=MD5,
            response="6299988bb4f05c0d8ad44295873858cf"
    ```


The `response` value is generated in the following way:

a. `HA1 = MD5(username:realm:password)`

> ### TIP

-   > This part [is stored](https://ktor.io/docs/digest.html#digest-table) on a server and can be used by Ktor to validate user credentials.

    b. `HA2 = MD5(method:digestURI)`

    c. `response = MD5(HA1:nonce:HA2)`

-   A server [validates](https://ktor.io/docs/digest.html#configure-provider) credentials sent by a client and responds with the requested content.

Example of usage with curl:
> `curl -v --digest -u "sergio:1234" "http://localhost:8080/digest"`

# JWT Auth
[JSON Web Token (JWT)](https://jwt.io/) is an open standard that defines a way for securely transmitting information between parties as a JSON object. This information can be verified and trusted since it is signed using a shared secret (with the `HS256` algorithm) or a public/private key pair (for example, `RS256`).

Ktor handles JWTs passed in the `Authorization` header using the `Bearer` schema and allows you to:

-   verify the signature of a JSON web token;

-   perform additional validations on the JWT payload.

You will need the following dependecies:
```
implementation("io.ktor:ktor-server-auth:$ktor_version")
implementation("io.ktor:ktor-server-auth-jwt:$ktor_version")
```
### The JWT authorization flow in Ktor might look as follows:

1.  A client makes a `POST` request with the credentials to a specific authentication [route](https://ktor.io/docs/routing-in-ktor.html) in a server application. The example below shows an [HTTP client](https://www.jetbrains.com/help/idea/http-client-in-product-code-editor.html) `POST` request with the credentials passed in JSON:

    ```
    POST http://localhost:8080/JWT/login
    Content-Type: application/json
    
    {
      "username": "jetbrains",
      "password": "jetbrains"
    }
    ```


-   If the credentials are valid, a server generates a JSON web token and signs it with the specified algorithm. For example, this might be `HS256` with a specific shared secret.

-   A server sends a generated JWT to a client.

-   A client can now make a request to a protected resource with a JSON web token passed in the `Authorization` header using the `Bearer` schema.

    ```
    GET http://localhost:8080/JWT
    Authorization: Bearer {{auth_token}}
    ```

-   A server receives a request and performs the following validations:

    -   Verifies a token's signature. Note that a [verification way](https://ktor.io/docs/jwt.html#configure-verifier) depends on the algorithm used to sign a token.

    -   Perform [additional validations](https://ktor.io/docs/jwt.html#validate-payload) on the JWT payload.

-   After validation, a server responds with the contents of a protected resource.