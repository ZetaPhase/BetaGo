
# BetaGo
![Logo](/misc/logo.PNG)

An open source virtual tourism app using augmented reality (currently Android only!)

# About

- This project was created by `xeliot` (Dave Ho), who was inspired by Pokemon Go by Niantic Inc
- Parts of the project collaboratively developed by `xeliot` (Dave Ho) and `0xFireball` (Nihal Talur)
- We plan to launch the app as a public preview in the near future; Stay Tuned!

## Architecture

APIs utilized

- Google Maps Android API
- Google Maps Directions API
- android tutorials coming soon
  - intents
  - camera
  - activity


### Part 1: The App

App implementations are in `app/`.

**BetaGo** client (Android, Java) - written by `xeliot` - 
This client is the main mobile app for BetaGo. It does
not yet support the new `BetaGo.Server` API; it currently only
works with the Flask server.

**BetaGoSnow** client (Android, Kotlin) - written by `0xFireball` -
This is a secondary client for BetaGo. It is developed to test
the very latest unstable changes and features in `BetaGo.Server`.
This client only supports the new API. This client does not yet
support all the BetaGo features that the **BetaGo** app supports.
Until this support is finished, this app can be found **[here](https://github.com/0xFireball/BetaGo-Snow/tree/master/app/android/BetaGoSnow)**.

#### Current Features

- Simple map from Google APIs
- Record
- Stop
- Snap
- Download
- Upload
- Sync activity with server

### Part 2: The server

Servers are located in `server/` in the repository.

Right now, there are 3 server implementations in place.

- A prototype Python base http server, using flat file storage (logic completed) inefficient - written by `xeliot`
- A Python server built with Flask, using SQLite as a backend - written by `xeliot`
- An ASP.NET Core server that uses LiteDB (MongoDB-like database) as a backend - written by `0xFireball`

For now, we're still using the Python server, but plan to eventually
replace it with flask server and sqlite for convenience and simplicity
or use ASP.NET Core server for scalability and performance.

The server API documentation is in `docs/devnotes/dotnet-server-api.md`

## Quick Start For Developers

1. Run an app implementation on a device
1. Run a server implementation that is supported by the app
1. You should be all set. 

## Read More

Check out my blog for more!

- Xeliot's blog - <http://principal-programming-fundamentals.blogspot.com/>


## License and Copyrights

&copy; 2016 Xeliot. All Rights Reserved. - (Creator of Project)  
&copy; 2016 0xFireball. All Rights Reserved. - (Collaborator from IridiumIon Software)  

Licensed under the AGPLv3.
