
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
`username`

#### API Registration Tips

Copyright &copy; 2016 0xFireball