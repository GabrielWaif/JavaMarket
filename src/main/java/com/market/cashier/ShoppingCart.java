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

  //Adds an item to shopping cart based on id
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

  public void payCart() {
    double totalValue = 0;
    Iterator listQuantity = this.shoppingCart.entrySet().iterator();
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
  }

  public boolean applyCupom(String code){
    if(this.products.hasCupom(code)){
      this.apliedCupom = code;
      System.out.println(code+ " cupom applied!");
      return true;
    }
    else {
      System.out.println("Cupom doesn't exist! enter a valid code.");
      return false;
    }
  }
}
