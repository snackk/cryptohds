# cryptohds

## Overview

Before anything, the following requirements must be met:
* **mysql server** - Server must be up and running with username *root* and password *cryptohds*. Also create database named *cryptohds*.
* **maven** - Maven must be installed in order to run the App.
* **java 8** - Install java 8 or latest.
* **IDE of your preference** - I prefer IntelliJ for Obvious reasons.

## Running backend

To run the App on the terminal do the following:
```sh
$ mvn clean install 
$ mvn spring-boot: run
```

On IntelliJ there's no need of maven vodu, it has a spring button to run.

## Client (For the test purpose use Postman)

## Ledger API
### Register 
  * *Type:* POST
  * *Headers:* Content-Type: application/json
  * *Body:* raw
  ```
  {
    "name": "nopee",
    "publicKey": "pubkey_5"
  }
```

### Balance
  * *Type:* POST
  * *Headers:* Content-Type: application/json
  * *Body:* raw
  ```
  {
    "publicKey": "pubkey_5"
  }
```