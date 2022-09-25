package com.market.cashier;

import java.util.Scanner;
import java.util.function.BiConsumer;

public class App {

  //Class instances of the created objects
  private static Products products = new Products();
  private static ShoppingCart shoppingCart = new ShoppingCart(products);

  //Global scannner object
  private static Scanner scanner = new Scanner(System.in);

  public static void main(String[] args) {
    String userInput = "";

    //Loop that reads the terminal input that is used to choosee the menu(owner/cart) until he exits.
    do {
      System.out.print("Market/> ");
      userInput = scanner.nextLine();
      if (!userInput.equals("exit")) {
        //Storage the user input in args[]
        String[] inputs = userInput.split(" ");
        if (inputs[0].equals("use")) {
          //Based in the "use ..." calls the "terminalCommands" function passing an Lambda with the commands for that choice.
          switch (inputs[1]) {
            case "owner":
              terminalCommands(
                (String[] commands, String command) -> {
                  switch (commands[0]) {
                    case "show":
                      products.showProducts();
                      break;
                    case "add":
                      String[] parameters = command.split("\"");

                      if (parameters.length == 4) {
                        String name = parameters[1];
                        String description = parameters[3];
                        double price = Double.parseDouble(
                          parameters[4].replace(" ", "")
                        );

                        if (!(name == "" || name == "")) {
                          products.addProduct(name, description, price);
                        } else invalidCommand(command);
                      } else {
                        invalidCommand(command);
                      }
                      break;
                    case "remove":
                    try{
                      int id = Integer.parseInt(commands[1]);

                      products.removeProduct(id);
                    }
                    catch(Exception err){
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

  //Prints the user input telling him that it isn't a valid command
  static void invalidCommand(String command) {
    System.out.println(command + " is not a valid command!");
  }

  //Loops through the user input calling the commands based in the Consumer passed through the main function
  static void terminalCommands(
    BiConsumer<String[], String> func,
    String terminalPath
  ) {
    String command = "";
    do {
      System.out.print(terminalPath + "/> ");
      command = scanner.nextLine();
      String[] args = command.split(" ");

      if (!command.equals("exit") && args.length > 0) {
        func.accept(args, command);
      } else System.out.println(
        "exiting " + terminalPath.toLowerCase() + "..."
      );
    } while (!command.equals("exit"));
  }
}
