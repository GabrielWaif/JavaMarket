# Java MongoDB market

## Introduction

Small java and MongoDB project. This project was made to simulate an owner loading new products to a database and an end user adding those products to his shopping cart.

## Prerequisites

- OpenJDK v17.0.4.1

Check the JDK version:

```sh
java --version
```

<center>
<img src="https://i.imgur.com/75ZSNFw.png" width"300"\>
</center>

- mongoDB 6.0.1 on port "mongodb://127.0.0.1:27017"

Enter the MongoDB environment:

```sh
monogosh
```

<center>
<img src="https://i.imgur.com/tSX11Jy.png" width"300"\>
</center>

## Instalation

1. Clone the git repository to the desired folder:
```sh
git clone https://github.com/GabrielWaif/JavaMarket.git
```
2. Run the .jar file:

```sh
java -jar Market.jar
```

## Usage

The program is divided into 3 main areas:
1. Market - the beginning where you choose where you are going.
2. Owner - Where you manage the products and coupons.
3. Cart - Where you add items to your cart and see the total value.

When you open the .jar file you will be met with the Market tab.

To enter a useful terminal use the "use" keyword.

The owner terminal requires a password, the default is "admin".
```sh
use owner
```

```sh
use cart
```

Ps:

Inside any of the terminals, you can use "exit" to quit.

```sh
exit
```

## Market commands

| Command   | Description                                                                 |
|-----------|-----------------------------------------------------------------------------|
| use owner | Will ask for the owner password and then will enter the owner command terminal. |
| use cart  | Will enter the shopping cart command terminal.                              |
| exit      | Exits the program.                                                          |

## Owner commands

| Command        | Description                                                                                                                      |
|----------------|----------------------------------------------------------------------------------------------------------------------------------|
| show products  | Shows every product that is in the database                                                                                     |
| show coupons    | Shows every coupon that is in the database                                                                                        |
| add product    | Receives 2 strings and a number as parameters and adds them to the database. Example: add product \"name\" \"amount\" 10         |
| add custom      | and a number from 0.1 -> 0.99 Adds a coupon to the database. Example: add coupon \"name\" 0.25                                     |
| remove product | Receives an id as parameters and removes the item with that id from the database. Example: remove product \"name\" \"amount\" 10 |
| remove coupon   | Receives a string and removes the coupon with that code. Example: add coupon \"name\" 0.25                                         |
| exit           | Exits from the owner commands                                                                                                    |
## Cart commands

| Command | Description                                                                                         |
|---------|-----------------------------------------------------------------------------------------------------|
| show    | Shows the current shopping cart with the price of everything.                                       |
| add     | Receives an id and an amount. Adds to the cart the product with the given id Example: add 10.       |
| remove  | Receives an id and an amount. Removes from the cart the product with the given id Example: add 10 2 |
| coupon   | Receives a string and applies the coupon with that code. Example: use \"code\".                      |
| pay     | Pays the current cart price.                                                                        |
| exit    | Exits the shopping cart commands.                                                                   |