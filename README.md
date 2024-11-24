# Exercice Java/Angular Telemis
The code is separated in two directory :
- exercice
- ui

These two projects are not connected to each other.\
**You'll need to deploy and run them separatly.** \
They will then communicate using HTTP REST API.

### How to play
 1. Connecte to the front using your credentials (you can also sign in using the default user:test pwd:test).
 2. Either Create (by setting a name and choosing the rules) or Join a Game.
 3. Roll (random or input) pins to refresh the score

**One of the examples in the statement is wrong and display a wrong score.\
 You can find it in the already created "Problem in the example" game.\
You can roll the same input as the example to get the right score and see the differences.**

## Exercice
The Spring boot (v3.3.5) backend.\

It handle all the game logics (eg: create new players, joining games, calculating the games score, etc).

Technical features :
- Games, players & rules are stored in an in-memory H2 Database with Spring Data
- Protected API endpoints with Spring Security
- JWT generation & Check using [jjwt](https://github.com/jwtk/jjwt)
- Custom Error Responses from custom Exception

## UI
The Angular (v18) frontend using the classless version of [Pico](https://picocss.com/).

build with:
`npm run build`

Technical features :
- Authentication Guards & Interceptors (Bearer token)
- Toast with [Toastify](https://apvarun.github.io/toastify-js/)

## TODO & Need to Fix
 - Prevent the user to roll more pins than available 
 - Fix the ErrorResponse Interceptor (actually not catching all the errors)
 - **TESTS !**

## Possible improvement
 - let the user create it's own rules 
 - Improve security with a better SecretKey for JWT hashing
 - Better styling for toast
 - Realtime score changes with websocket
