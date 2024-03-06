package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBconnection {
    private static final String URL = "jdbc:mysql://localhost:3306/formini.tn";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private  static DBconnection insatnce;
    private Connection cnx;
    private  DBconnection(){
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected To Database :)");
        }catch (SQLException e){
            System.err.println("ERROR: "+e.getMessage());
        }
    }
    public static DBconnection getInstance(){
        if (insatnce == null) insatnce = new DBconnection();
        return insatnce;
    }
    public Connection getCnx(){return cnx;}
}
