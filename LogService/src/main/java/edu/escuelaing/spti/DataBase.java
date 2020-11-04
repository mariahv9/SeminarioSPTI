package edu.escuelaing.spti;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.*;
import org.bson.Document;
import java.util.*;

/**
 * Class that makes the connection with mongo Database
 * @author Maria Fernanda Hernandez Vargas
 * @author Andres Orlando Sotelo Fajardo
 * @author Johan David Rueda Rofríguez
 */
public class DataBase {

    /**
     * Method that insert data on DB
     * @param user
     */
    public static void insert (String user) {
        MongoClient mongoClient = new MongoClient( "ec2-35-175-129-150.compute-1.amazonaws.com" , 27017 );
        MongoDatabase database = mongoClient.getDatabase("local");
        //database.createCollection("Users");
        MongoCollection<Document> collection = database.getCollection("Users");
        Date date = new Date();
        Document document1 = new Document("Usuario", user).append("Fecha", date.toString());
        List<Document> list = new ArrayList<Document>();
        list.add(document1);
        collection.insertMany(list);
    }

    /**
     * Method that returns data from DB
     * @return
     */
    public static String consult (){
        MongoClient mongoClient = new MongoClient( "ec2-35-175-129-150.compute-1.amazonaws.com" , 27017 );
        MongoDatabase database = mongoClient.getDatabase("local");
        MongoCollection<Document> collection = database.getCollection("Users");
        FindIterable <Document> iterDoc = collection.find();
        Iterator it = iterDoc.iterator();
        ArrayList<Document> list;
        list =  new ArrayList<Document>();
        String ans = "";

        while (it.hasNext()) {
            Document var = (Document) it.next();
            list.add(var);
        }
        int i = 0;
        while (i < list.size() && i < 10){
            ans += "{";
            Document var = list.get(list.size()-i-1);
            ans += "\"Usuario\": \"" + var.get("Usuario")+"\",";
            ans += " \"Fecha\": \"" + var.get("Fecha")+"\"";
            ans += "}";
            if (i < list.size() - 1 && i < 9) ans += ",";
            i ++;
        }
        mongoClient.close();
        return ans;
    }
}