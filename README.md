
# BetaGo

An open source tourism app using augmented reality (currently Android only!)

## Architecture

APIs utilized

- Google Maps Android API
- Google Maps Directions API
- Inspired by Pokemon Go by Niantic Inc
- android tutorials coming soon
  - intents
  - camera
  - activity


### Part 1: The App

- Simplistic minimalistic map from google
- Record
- Stop
- Snap
- Download
- Upload

### Part 2: The server

Servers are located in `Server/` in the repository.

Right now, there are 2 server implementations in place.

- A Python server built with Flask, using SQLite as a backend
- An ASP.NET Core server that uses LiteDB (MongoDB-like database) as a backend

For now, we're still using the Python server, but plan to eventually
replace it with the ASP.NET Core server for scalability and performance.

## Quick Start For Developers

BetaGo was developed using Android Studio

- <https://developer.android.com/studio/index.html?gclid=Cj0KEQjwztG8BRCJgseTvZLctr8BEiQAA_kBD8iqav_IkrM0sgLT7b1XJBAHE_Us2n-48onMabU9NDsaAsig8P8HAQ>

BetaGo was also built using Google Maps V2 Android

- <https://developers.google.com/maps/documentation/android-api/start>

## Tutorials

Check out my blog for some tutorials on Google Maps V2 Android

- <http://principal-programming-fundamentals.blogspot.com/>

## License and Copyrights

&copy; 2016 ZetaPhase Technologies. All Rights Reserved.  
&copy; 2016 0xFireball. All Rights Reserved.  

Licensed under the GPLv3.
