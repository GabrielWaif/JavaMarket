package com.market.cashier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bson.Document;

public class ShoppingCart {

  private Map<Integer, Integer> shoppingCart;
  private Products products;
  private double totalPrice;

  public ShoppingCart(Products products) {
    this.shoppingCart = new HashMap<Integer, Integer>();
    this.products = products;
    this.totalPrice = 0;
  }

  //Adds an item to shopping cart based on id
  public boolean addCart(int id, int quantity) {
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
      }
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
        double itemPrice = Double.parseDouble(quantityItem.getValue().toString()) * ((Double)productInfo.get("price"));
        totalValue += itemPrice;
      System.out.println(productInfo.get("name") + " - quant. "  + quantityItem.getValue() + " | Price: $" + itemPrice);
    }
    System.out.println("-----------------");
    System.out.println("Total: $" + totalValue);
  }
}