package com.market.cashier;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.function.BiConsumer;

public class App {

  //Class instances of the created objects
  private static Products products = new Products();
  private static ShoppingCart shoppingCart = new ShoppingCart(products);

  //Global scannner object
  private static Scanner scanner = new Scanner(System.in);

  //Help text variables
  private static String marketHelp =
    "-----------------------\nMarket commands description:\n-----------------------\nuse owner - will ask the owner password and then will enter owner command terminal\n\nuse cart - will enter the shopping cart command terminal\n\nexit - Exits the program\n-----------------------";
  private static String ownerHelp =
    "-----------------------\nOwner commands description:\n-----------------------\nshow products - Shows every products that is in the database\n\nshow cupons - Shows every cupom that is in the database\n\nadd product - Receives 2 strings and a number as parameters and adds them to the database. Example: add product \"name\" \"amount\" 10\n\nadd cupom - Receives 1 string  and a number from 0.1 -> 0.99 Adds a cupom to the database. Example: add cupom \"name\" 0.25\n\nremove product - Receives an id as parameters and removes the item with that id from the database. Example: add product \"name\" \"amount\" 10\n\nremove cupom - Receives a string and removes the cupom with that code. Example: add cupom \"name\" 0.25\n\nexit - Exits from the owner commands\n-----------------------";
  private static String cartHelp =
    "-----------------------\nShopping cart commands description:\n-----------------------\nshow - Shows the current shopping cart with the price of everything\n\nadd - Receives an id and an amount. Adds to the cart the product with the given id Example: add 10\n\nremove - Receives an id and an amount. Removes from the cart the product with the given id Example: add 10 2\n\ncupom - Receives an string and applies the cupom with that code. Example: use \"code\".\n\npay - Pays the current cart price\n\nexit - Exits the shopping cart commands\n-----------------------";

  public static void main(String[] args) {
    String userInput = "";
    //Loop that reads the terminal input that is used to choosee the menu(owner/cart) until he exits.
    do {
      System.out.print("Market/> ");
      userInput = scanner.nextLine();
      if (!userInput.replace(" ", "").equals("exit")) {
        //Storage the user input in args[]
        String[] inputs = getArgs(userInput);
        if (userInput.equals("help")) System.out.println(marketHelp); else if (
          inputs[0].equals("use")
        ) {
          //Based in the "use ..." calls the "terminalCommands" function passing an Lambda with the commands for that choice.
          switch (inputs[1]) {
            case "owner":
              if (products.loginOwner()) terminalCommands(
                (String[] commands, String command) -> {
                  switch (commands[0]) {
                    case "show":
                      if (
                        commands[1].equals("products")
                      ) products.showProducts(); else if (
                        commands[1].equals("cupons")
                      ) products.showCupons(); else invalidCommand(command);
                      break;
                    case "add":
                      if (commands[1].equals("cupom")) {
                        try {
                          String code = commands[2];
                          double value = Double.parseDouble(commands[3]);
                          products.addCupom(code, value);
                        } catch (Exception err) {
                          invalidCommand(command);
                        }
                      } else if (commands[1].equals("product")) {
                        try {
                          String name = commands[2];
                          String description = commands[3];
                          double price = Double.parseDouble(commands[4]);
                          if (!(name == "" || description == "")) {
                            products.addProduct(name, description, price);
                          } else invalidCommand(command);
                        } catch (Exception err) {
                          invalidCommand(command);
                        }
                      } else invalidCommand(command);
                      break;
                    case "remove":
                      if (commands[1].equals("product")) {
                        try {
                          int id = Integer.parseInt(commands[2]);

                          products.removeProduct(id);
                        } catch (Exception err) {
                          invalidCommand(command);
                        }
                      } else if (commands[1].equals("cupom")) {
                        try {
                          String code = commands[2];
                          products.removeCupom(code);
                        } catch (Exception err) {
                          invalidCommand(command);
                        }
                      } else invalidCommand(command);
                      break;
                    default:
                      invalidCommand(command);
                      break;
                  }
                },
                "Owner",
                ownerHelp
              );
              break;
            case "cart":
              terminalCommands(
                (String[] commands, String command) -> {
                  switch (commands[0]) {
                    case "show":
                      shoppingCart.showCart();
                      break;
                    case "add":
                      try {
                        int id = Integer.parseInt(commands[1]);
                        int quantity = Integer.parseInt(commands[2]);

                        shoppingCart.addToCart(id, quantity);
                      } catch (Exception err) {
                        invalidCommand(command);
                      }
                      break;
                    case "remove":
                      try {
                        int id = Integer.parseInt(commands[1]);
                        int quantity = Integer.parseInt(commands[2]);

                        shoppingCart.removeFromCart(id, quantity);
                      } catch (Exception err) {
                        invalidCommand(command);
                      }
                      break;
                    case "pay":
                      shoppingCart.payCart();
                      break;
                    case "cupom":
                      break;
                    default:
                      invalidCommand(command);
                      break;
                  }
                },
                "Cart",
                cartHelp
              );
              break;
            default:
              invalidCommand(userInput);
              break;
          }
        } else invalidCommand(userInput);
      } else System.out.println("Exiting market...");
    } while (!userInput.replace(" ", "").equals("exit"));
  }

  //Prints the user input telling him that it isn't a valid command
  static void invalidCommand(String command) {
    System.out.println(command + " is not a valid command!");
  }

  //Loops through the user input calling the commands based in the Consumer passed through the main function
  static void terminalCommands(
    BiConsumer<String[], String> func,
    String terminalPath,
    String help
  ) {
    String command = "";
    do {
      System.out.print(terminalPath + "/> ");
      command = scanner.nextLine();
      String[] args = getArgs(command);

      if (!command.equals("exit") && args.length > 0) {
        if (command.equals("help")) {
          System.out.println(help);
        } else func.accept(args, command);
      } else {
        System.out.println("exiting " + terminalPath.toLowerCase() + "...");
        if (terminalPath.equals("Owner")) products.logout();
      }
    } while (!command.equals("exit"));
  }

  public static String[] getArgs(String command) {
    boolean isInside = false;
    String[] characters = command.split("");
    ArrayList<String> res = new ArrayList<String>();
    String currentWord = "";
    String prevCharacter = " ";
    for (int i = 0; i < characters.length; i++) {
      if (isInside) {
        if (characters[i].equals("\"")) {
          isInside = false;
          res.add(currentWord);
          currentWord = "";
        } else {
          currentWord += characters[i];
        }
      } else {
        if (
          characters[i].equals(" ") &&
          (prevCharacter.equals(" ") || prevCharacter.equals("\""))
        ) {} else if (characters[i].equals(" ")) {
          res.add(currentWord);
          currentWord = "";
        } else if (characters[i].equals("\"")) {
          isInside = true;
        } else if (i == characters.length - 1) {
          currentWord += characters[i];
          res.add(currentWord);
        } else {
          currentWord += characters[i];
        }
      }
      prevCharacter = characters[i];
    }
    return res.toArray(new String[res.size()]);
  }
}
