package com.market.cashier;

import java.util.Scanner;
import java.util.function.BiConsumer;

public class App {

  private static Products products = new Products();
  private static Scanner scanner = new Scanner(System.in);
  private static ShoppingCart shoppingCart = new ShoppingCart(products);

  public static void main(String[] args) {
    String userInput = "";
    do {
      System.out.print("Market/> ");
      userInput = scanner.nextLine();
      if (!userInput.equals("exit")) {
        String[] inputs = userInput.split(" ");
        if (inputs[0].equals("use")) {
          switch (inputs[1]) {
            case "owner":
              terminalCommands(
                (String[] commands, String command) -> {
                  switch (commands[0]) {
                    case "show":
                      products.showProducts();
                      break;
                    case "add":
                      try {
                        String[] parameters = command.split("\"");

                        String name = parameters[1];
                        String description = parameters[3];
                        double price = Double.parseDouble(
                          parameters[4].replace(" ", "")
                        );

                        if (!(name == "" || name == "")) {
                          products.addProduct(name, description, price);
                        } else invalidCommand(command);
                      } catch (Error err) {
                        invalidCommand(command);
                      }
                      break;
                    default:
                      invalidCommand(command);
                      break;
                  }
                },
                "Owner"
              );
              break;
            case "cart":
              terminalCommands(
                (String[] commands, String command) -> {
                  switch (commands[0]) {
                    case "show":
                      shoppingCart.showCart();
                      break;
                  }
                },
                "Cart"
              );
              break;
            default:
              invalidCommand(userInput);
              break;
          }
        } else invalidCommand(userInput);
      } else System.out.println("Exiting market...");
    } while (!userInput.equals("exit"));
  }

  static void ownerCommands() {}

  static void invalidCommand(String command) {
    System.out.println(command + " is not a valid command!");
  }

  static void terminalCommands(
    BiConsumer<String[], String> func,
    String terminalPath
  ) {
    String command = "";
    do {
      System.out.print(terminalPath + "/> ");
      command = scanner.nextLine();
      String[] args = command.split(" ");

      if (!command.equals("exit")) {
        func.accept(args, command);
      } else System.out.println(
        "exiting " + terminalPath.toLowerCase() + "..."
      );
    } while (!command.equals("exit"));
  }
}
