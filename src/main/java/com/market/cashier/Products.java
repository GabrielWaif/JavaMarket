package com.market.cashier;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import java.util.Random;
import java.util.Scanner;
import org.bson.Document;
import org.bson.conversions.Bson;

public class Products {

  //here you can set your local mongodb uri - the default is "mongodb://localhost:27017"
  private String uri = "mongodb://localhost:27017";
  private MongoClient client;
  private MongoDatabase database;
  private MongoCollection<Document> products;
  private MongoCollection<Document> cupons;
  private String ownerPassword = "admin";
  private Scanner scanner = new Scanner(System.in);
  private boolean logedIn = false;

  //Initializes the data base and connects to 27017 -> Market -> Products.
  Products() {
    try {
      client = MongoClients.create(uri);
      database = client.getDatabase("Market");
      products = database.getCollection("Products");
      cupons = database.getCollection("Cupons");
      System.out.println("Connected to database...");
    } catch (Exception err) {
      System.err.println(err);
    }
  }

  //Shows all products that are in the database - sorted by id
  public void showProducts() {
    System.out.println("----------------");
    System.out.println("Products:");
    System.out.println("----------------");
    Bson filter = Filters.eq("id", 1);
    MongoCursor cursor = this.products.find().sort(filter).iterator();
    try {
      while (cursor.hasNext()) {
        Document currentItem = ((Document) cursor.next());
        System.out.println(
          currentItem.get("id") +
          ") " +
          currentItem.get("ammount") +
          " / " +
          currentItem.get("name") +
          " - $" +
          currentItem.get("price")
        );
      }
    } catch (Exception err) {
      System.out.println("Error showing products! try again.");
    } finally {
      cursor.close();
      System.out.println("----------------");
    }
  }

  public boolean loginOwner() {
    System.out.print("Password: ");
    String bufferPassword = scanner.nextLine();
    this.logedIn = bufferPassword.equals(this.ownerPassword);
    if (!this.logedIn) System.out.println("wrong password");
    return this.logedIn;
  }

  //adds a product to the database based on the parametters given
  public boolean addProduct(String name, String ammount, double price) {
    if (this.logedIn) {
      if (price > 0) {
        try {
          Document newProduct = new Document("id", newUniqueId())
            .append("name", name)
            .append("ammount", ammount)
            .append("price", price);
          this.products.insertOne(newProduct);
          System.out.println(name + " was successfully added to the database.");
          return true;
        } catch (Exception err) {
          System.out.println(name + " could not be added to the database!");
          return false;
        }
      } else {
        System.out.println("Price is too low! choose a higher one.");
        return false;
      }
    } else return false;
  }

  //removes the product from the database based from the id given
  public boolean removeProduct(int id) {
    if (this.logedIn) {
      if (this.hasId(id)) {
        try {
          Bson filter = Filters.eq("id", id);
          System.out.println(
            this.getOneInfo(id).get("name") + " was removed from products!"
          );
          this.products.deleteOne(filter);
          return true;
        } catch (Exception err) {
          System.out.println("Error removing product! try again.");
          return false;
        }
      } else {
        System.out.println("Id doesn't exist! please enter a valid one.");
        return false;
      }
    } else return false;
  }

  // returns a boolean saying if the informed id exists in the database or not
  public boolean hasId(int id) {
    try {
      Bson filter = Filters.eq("id", id);
      MongoCursor cursor = this.products.find(filter).iterator();
      return cursor.hasNext();
    } catch (Exception err) {
      System.out.println("Error looking for id! try again.");
      return false;
    }
  }

  // Returns an id that doesn't exist in the datatbase from 1 to 100
  private int newUniqueId() {
    Random r = new Random();
    int newId = r.nextInt(100) + 1;
    if (this.hasId(newId)) return newUniqueId(); else return newId;
  }

  public Document getOneInfo(int id) {
    Bson filter = Filters.eq("id", id);
    MongoCursor cursor = this.products.find(filter).iterator();
    return (Document) cursor.next();
  }

  public boolean hasCupom(String cupom) {
    try {
      Bson filter = Filters.eq("code", cupom);
      MongoCursor cursor = this.cupons.find(filter).iterator();
      return cursor.hasNext();
    } catch (Exception err) {
      System.out.println("Error looking for cupom! Try again.");
      return false;
    }
  }

  public double cupomValue(String cupom) {
    try {
      Bson filter = Filters.eq("code", cupom);
      MongoCursor cursor = this.cupons.find(filter).iterator();
      Document dCupom = (Document) cursor.next();
      return (Double) dCupom.get("value");
    } catch (Exception err) {
      return 0;
    }
  }

  public boolean addCupom(String cupom, double discount) {
    if (this.logedIn) {
      if (!this.hasCupom(cupom)) {
        if (discount > 0 && discount < 1) {
          Document newCupom = new Document("code", cupom)
          .append("value", discount);
          cupons.insertOne(newCupom);
          System.out.println("Cupom " + cupom + " successfully added!");
          return true;
        } else {
          System.out.println("Invalid cupom discount value!");
          return false;
        }
      } else {
        System.out.println("That cupom already exists! Pick a new name.");
        return false;
      }
    } else return false;
  }

  public boolean removeCupom(String cupom) {
    if (this.logedIn) {
      if (this.hasCupom(cupom)) {
        try {
          Bson filter = Filters.eq("code", cupom);
          this.cupons.deleteOne(filter);
          System.out.println(cupom + " was removed from cupons!");
          return true;
        } catch (Exception err) {
          System.out.println("Error removing cupom! try again.");
          return false;
        }
      } else {
        System.out.println("Cupom doesn't exist! Please enter a valid one.");
        return false;
      }
    } else return false;
  }

  public void showCupons() {
    if (this.logedIn) {
      try {
        MongoCursor bufferCupons = cupons.find().iterator();
        System.out.println("---------------");
        System.out.println("Cupons:");
        System.out.println("---------------");
        while (bufferCupons.hasNext()) {
          Document currentCupom = (Document) bufferCupons.next();
          System.out.println(
            currentCupom.get("code") +
            " - " +
            (((Double) (currentCupom.get("value"))) * 100) +
            "%"
          );
        }
        System.out.println("---------------");
      } catch (Exception err) {
        System.out.println("Error looking for cupons! try again.");
      }
    }
  }

  public void logout() {
    this.logedIn = false;
  }
}
