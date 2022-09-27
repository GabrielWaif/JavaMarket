package com.market.cashier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bson.Document;

public class ShoppingCart {

  private Map<Integer, Integer> shoppingCart;
  private Products products;
  private String apliedCupom;

  public ShoppingCart(Products products) {
    this.shoppingCart = new HashMap<Integer, Integer>();
    this.products = products;
    this.apliedCupom = null;
  }

  /**
   * Adds an database product to the shoppingCart list with the given its id and the quantity.
   *
   * @param  id
   *         The id of the product that will be added.
   *
   * @param  quantity
   *         The quantity of the product that will be added to the shopping cart.
   *
   * @return
   *         A boolean if the product was successfully added to the shopping cart List.
   */
  public boolean addToCart(int id, int quantity) {
    if (quantity > 0) {
      if (this.products.hasId(id)) {
        if (this.shoppingCart.containsKey(id)) {
          this.shoppingCart.put(id, this.shoppingCart.get(id) + quantity);
        } else {
          this.shoppingCart.put(id, quantity);
        }
        System.out.println(
          this.products.getOneInfo(id).get("name") +
          " was added to the shopping cart"
        );
        return true;
      } else {
        System.out.println("Not a valid id! please enter a valid one.");
      }
    } else {
      System.out.println("Pick a higher quantity!");
    }

    return false;
  }

  /**
   * Removes the quantiny given of the given product from the shoppingCart list based on the id and quantity given.
   *
   * @param  id
   *         The id of the product that will be removed.
   *
   * @param  quantity
   *         The quantity of the product that will be removed from the shopping cart.
   *
   * @return
   *         A boolean if the product was successfully removed from the shopping cart List.
   */
  public boolean removeFromCart(int id, int quantity) {
    if (this.products.hasId(id)) {
      if (this.shoppingCart.containsKey(id)) {
        int currentAmmount = this.shoppingCart.get(id);
        if (currentAmmount > quantity) {
          this.shoppingCart.put(id, this.shoppingCart.get(id) - quantity);
        } else if (currentAmmount == quantity) {
          this.shoppingCart.remove(id);
        } else {
          System.out.println("Not enough products on cart to remove!");
          return false;
        }
      } else {
        System.out.println(
          "There is no" +
          this.products.getOneInfo(id).get("name") +
          " in your shoping cart!"
        );
        return false;
      }
      System.out.println(
        this.products.getOneInfo(id).get("name") +
        " was removed from the shopping cart"
      );
      return true;
    } else {
      System.out.println("Not a valid id! please enter a valid one.");
    }

    return false;
  }

  /**
   * Shows the price, name and quantity of every product in the database, the total price and any discounts based in the given cupom.
   */
  public void showCart() {
    double totalValue = 0;
    Iterator listQuantity = this.shoppingCart.entrySet().iterator();
    System.out.println("-----------------");
    System.out.println("Shoping Cart:");
    System.out.println("-----------------");
    while (listQuantity.hasNext()) {
      Map.Entry quantityItem = (Map.Entry) listQuantity.next();
      Document productInfo =
        this.products.getOneInfo((int) quantityItem.getKey());
      double itemPrice =
        Double.parseDouble(quantityItem.getValue().toString()) *
        ((Double) productInfo.get("price"));
      totalValue += itemPrice;
      System.out.println(
        productInfo.get("name") +
        " - quant. " +
        quantityItem.getValue() +
        " | Price: $" +
        itemPrice
      );
    }
    System.out.println("-----------------");
    if (this.apliedCupom != null) {
      System.out.println("Valor inicial: $" + totalValue);
      double desconto = this.products.cupomValue(this.apliedCupom) * totalValue;
      System.out.println("Desconto: $-" + desconto);
      System.out.println("Valor final: $" + (totalValue - desconto));
    } else System.out.println("Total: $" + totalValue);
  }

  /**
   * Pays toatl price of the shopping cart with its discounts, clears the shopping cart list and removes the used cupom from the database.
   */
  public void payCart() {
    double totalValue = 0;
    Iterator listQuantity = this.shoppingCart.entrySet().iterator();
    if (listQuantity.hasNext()) {
      while (listQuantity.hasNext()) {
        Map.Entry quantityItem = (Map.Entry) listQuantity.next();
        Document productInfo =
          this.products.getOneInfo((int) quantityItem.getKey());
        double itemPrice =
          Double.parseDouble(quantityItem.getValue().toString()) *
          ((Double) productInfo.get("price"));
        totalValue += itemPrice;
      }
      this.shoppingCart.clear();
      System.out.println(
        "Purchcase made with a value of $" +
        (
          totalValue *
          (
            this.apliedCupom == null
              ? 1
              : (1 - this.products.cupomValue(this.apliedCupom))
          )
        )
      );
      if (this.apliedCupom != null) {
        this.products.removeCupom(apliedCupom);
        this.apliedCupom = null;
      }
    } else {
      System.out.println("Nothing in the cart! Add something before buying.");
    }
  }

  /**
   * Applies the discount cupom with the given code to the total price of the shopping cart.
   *
   * @param  code
   *         The cupom code of the cupom that will be applied.
   *
   * @return
   *         A boolean if the cupom was successfully applied.
   */
  public boolean applyCupom(String code) {
    if (this.products.hasCupom(code)) {
      this.apliedCupom = code;
      System.out.println(code + " cupom applied!");
      return true;
    } else {
      System.out.println("Cupom doesn't exist! enter a valid code.");
      return false;
    }
  }
}
