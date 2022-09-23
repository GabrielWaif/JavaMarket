package com.market.cashier;

import org.bson.Document;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

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
          MongoCursor cursor = products.find().iterator();
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
}
