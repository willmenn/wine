[![Build Status](https://travis-ci.org/willmenn/wine.svg?branch=master)](https://travis-ci.org/willmenn/wine)

### This Project was created as a exercise of the class Prog II from SENAC-RS.

**The project idea is to create a simple Wine Store API.**

The project is made with Java, Spring Boot and Gradle.

To acces it's Swagger view click in this [link](ine-prog-2.herokuapp.com), the app is hosted in Heroku.

It's using Travis to build and deploy, see this [link](https://travis-ci.org/willmenn/wine).

To build the project and run the tests:

```
$ ./gradlew build
```

OBS: The project is using H2 as database.

----

### Small info

To be able to use the app you will need to create a User.
```
POST http://wine-prog-2.herokuapp.com/users

data:
{
"usernmae" : "{nickaname}",
"password" : "{password}
}
```

Then you will need to "authenticate"(it's a very simple implementation).

```
POST http://wine-prog-2.herokuapp.com/users/auth?username={nick}&password={pass}
```

the return will be a simple Long trnasformed in Hexadecimal that need to be passed in all others requests in the header with this naming:

```
sessionId: {Hexadecimal}
```

In the others API's you will be able to create/update and delete wines and orders.

````
http://wine-prog-2.herokuapp.com/wines

http://wine-prog-2.herokuapp.com/orders
