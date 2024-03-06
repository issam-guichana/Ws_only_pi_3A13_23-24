package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    public static final String URL="jdbc:mysql://localhost:3306/formini.tn2";
    public static final String USER="root";
    public static final String PASSWORD="";
    private Connection cnx;
    //second step creer une instance de meme type que la classe
    private static DBConnection instance;

    //first step rendre le constructeur private
    private DBConnection() {
        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("connected to database");
        } catch (SQLException e) {
            System.err.println("Error" + e.getMessage());
        }
    }


    //third step creer une methode static pour recuperer l instance

    public static DBConnection getInstance() {
        if ( instance== null) instance = new DBConnection();
        return instance;
    }

    public Connection getCnx() {
        return cnx;
    }

}
