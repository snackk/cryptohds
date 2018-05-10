# cryptohds

## Overview

Before anything, the following requirements must be met:
* **cryptohdsLibrary** - You've got to have the library locally installed for this to work. You can find the steps to install it here [cryptohdsLibrary](https://github.com/snackk/cryptohdsLibrary).
* **mysql server** - Server must be up and running with username *root* and password *cryptohds*. Also create database named *cryptohds*.
* **maven** - Maven must be installed in order to run the App.
* **java 8** - Install java 8 or latest.
* **IDE of your preference** - I prefer IntelliJ for Obvious reasons.
* **Docker** - To create containers.

## Running (Dev)

To run the App on the terminal do the following:
```sh
$ mvn clean install
$ mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## Docker

```sh
$ mvn install dockerfile:build
$ cd src/main/docker
$ ./cryptohds.sh <number of replicas>
$ # Must have execute permission, if not run:
$ chmod +x cryptohds.sh
```

### Useful commands

List all containers
```sh
$ docker ps -a
```

List running containers
```sh
$ docker ps
```

List IP of a container
```sh
$ docker exec -it docker_cryptohds-mysql1_1 less /etc/hosts
```

Run mysql on a given container
```sh
$ docker exec -it docker_cryptohds-mysql1_1 mysql -u root -p
```

Stop and Remove containers
```sh
$ docker stop $(docker ps -a -q)
$ docker rm $(docker ps -a -q)
```

## Client (Postman)

## Ledger API
### Register 
  * *endpoint:* /api/ledgers
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
  * *endpoint:* /api/ledger/balance
  * *Type:* GET
  * *Headers:* Content-Type: application/json
  * *Parameters:*
    * *key:* publicKey
    * *value:* pubkey_1
    
### Audit
  * *endpoint:* /api/ledger/audit
  * *Type:* GET
  * *Headers:* Content-Type: application/json
  * *Parameters:*
    * *key:* publicKey
    * *value:* pubkey_1
    
## Operation API
### Send 
  * *endpoint:* /api/operation/send
  * *Type:* POST
  * *Headers:* Content-Type: application/json
  * *Body:* raw
  ```
{
    "originPublicKey": "pubkey_1",
    "destinationPublicKey": "pubkey_10",
    "value": "100"
}
```

### Receive 
  * *endpoint:* /api/operation/receive
  * *Type:* POST
  * *Headers:* Content-Type: application/json
  * *Body:* raw
  ```
{
    "publicKey": "pubkey_10",
    "operationId": "101"
}
```

