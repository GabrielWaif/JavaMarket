package com.market.cashier;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class Products {
        private String url = "mongodb://localhost:27017";
        private MongoClient client;
        private MongoDatabase database;
        private MongoCollection<Document> products;

        Products(){
          try{
            client = MongoClients.create(url);
            database = client.getDatabase("Market");
            products = database.getCollection("Products");
            System.out.println("Connected to database...");
          }
          catch(Error err){
            System.err.println(err);
          }
        }

        public void showProducts(){
          System.out.println("----------------");
          System.out.println("Produtos:");
          System.out.println("----------------");
          MongoCursor cursor = this.products.find().iterator();
          try{
            while(cursor.hasNext()){
              Document currentItem = ((Document) cursor.next());
              System.out.println(currentItem.get("name") + " - $" + currentItem.get("price"));
            }
          }
          finally{
            cursor.close();
          System.out.println("----------------");
          }
        }

        public boolean addProduct(String name, String ammount, double price){
          try{
          Document newProduct = new Document("id", 10).append("name", name).append("ammount", ammount).append("price", price);
          this.products.insertOne(newProduct);
          System.out.println(name + " was successfully added to the database.");

          return true;
          }
          catch(Error err){
            System.err.println(err + " - something wrong happened");
          return false;
          }
        }

        public boolean removeProduct(int id){
          try{
            return true;
          }
          catch(Error err){
            System.err.println(err);
            return false;
          }
        }

        public boolean hasId(int id){
          try{
            Bson filter = Filters.eq("id", id);
            MongoCursor cursor = this.products.find(filter).iterator();
            return cursor.hasNext();
          }
          catch(Error err){
            System.err.println(err);
            return false;
          }
        }


}
