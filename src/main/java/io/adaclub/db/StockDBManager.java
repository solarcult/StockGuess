package io.adaclub.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class StockDBManager {

    public static String DB_IP_ADDRESS = "127.0.0.1";
    public static String DB_NAME = "stock";
    public static String DB_USER = "root";
    public static String DB_USER_PASSWORD = "sl134120";

    public static Connection stockConnection;

    public static Connection getStockConnection()
    {
        if(stockConnection == null){
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                stockConnection = DriverManager.getConnection("jdbc:mysql://"+DB_IP_ADDRESS+":3306/"+DB_NAME, DB_USER, DB_USER_PASSWORD);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return stockConnection;
    }

    public static void main(String[] args){
        getStockConnection();
    }
}
