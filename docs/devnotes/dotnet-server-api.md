
# .NET Server (`BetaGo.Server`) API and Specifications

By 0xFireball (Nihal Talur)

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
    "username": "SomeBody",
    "phoneNumber": "18004567890"
  },
  "apikey": "W2ThJ7xqtB3znyj7g0KF4jU2HJbcqdg1Un8eLXAV"
}
```

##### API Registration Tips

After registering an account through the API, be sure to save the user's `apikey`.
This will act as a fully privileged access token to the user's account. 



Copyright &copy; 2016 0xFireball