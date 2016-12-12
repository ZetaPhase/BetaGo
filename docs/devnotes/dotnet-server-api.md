
# .NET Server (`BetaGo.Server`) API and Specifications

By 0xFireball (Nihal Talur)

Specification version `v0.1.1`.
This version ID will be updated as the specification changes.
API clients will be required to adapt to API changes, and backwards
compatibility is not guaranteed.

## `BetaGo.Server` Intro

Some quick notes:

- `BetaGo.Server` is an ASP.NET web application that provides
a REST API for BetaGo clients
- A high performance NoSQL database is used as the data storage backend

## Consuming the REST API

The `BetaGo.Server` application was designed to provide a powerful REST API
that allows clients to automate everything from authentication to querying.

### How to use this guide

Routes will be described in the format:

```text
[path] [method] - [description]
[parameters]
```

#### To use a route:

Send an HTTP request with the method `[method]` to `http(s)://server-address/[path]`
with the specified parameters. In a `POST` request, parameters will be in the request
body in JSON format. In a `GET` request, parameters should be in the query string.

#### API authentication

Most user-related actions require authentication. If there is an asterisk (`*`) after
the route path, then you will need to add a parameter `apiKey` set to the user's API key.

All requests accept JSON, while most also accept XML.

See the **API Registration Tips** section for information about how to get the user API key.

### API Routes

#### Account Registration

`/register` (POST) - Registers a new user account. The server
performs a number of checks and validations to ensure that account data is valid.

Parameters:

`username` - The username of the new user. Must be at least 4 characters.

`password` - The new user's password. This will later be hashed securely on the server

`phoneNumber` (optional) - The new user's phone number. If specified, must be of valid format.

Response type:

Assuming a valid request, the server will send a JSON response containing
information about the newly registered user.

Example response:

```json
{
  "user": {
    "username": "BoeTheBeast",
    "phoneNumber": "18004567890"
  },
  "apikey": "W2ThJ7xqtB3znyj7g0KF4jU2HJbcqdg1Un8eLXAV"
}
```

#### Account Login

`/login` (POST) - Logs in to an existing account.

Parameters:

`username` - The username

`password` - The password

Response:

A JSON response with user details.

Example response:

```json
{
  "user": {
    "username": "BoeTheBeast",
    "phoneNumber": "18004567890"
  },
  "apikey": "W2ThJ7xqtB3znyj7g0KF4jU2HJbcqdg1Un8eLXAV"
}
```

#### Account details querying

`/api/userinfo`* (GET) - Retrieves user info in JSON format. The client
MUST be authenticated using a Statless auth token (the API key for example)

Response type:

User details will be returned in JSON format.

Example:

```json
{
  "username": "BoeTheBeast",
  "phoneNumber": "18004567890"
}
```

This route can be used to retrieve information about the user. Additional fields may be
added in future versions of the server.

##### API Registration Tips

After registering an account through the API, be sure to save the user's `apikey`.
This will act as a fully privileged access token to the user's account. 

When performing subsequent actions in the application, this API Key can be
supplied to API routes that require authentication to authenticate on behalf
of the user.

Copyright &copy; 2016 [0xFireball](https://github.com/0xFireball). All Rights Reserved.