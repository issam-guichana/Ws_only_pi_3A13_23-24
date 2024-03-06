package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBConnection {
    private static final String URL= "jdbc:mysql://localhost:3306/formini.tn";
    private static final String USER= "root";
    private static final String PASSWORD= "";
    //second Step:  créer une instance static de meme type que la classe
    private static DBConnection instance;

    private Connection cnx ;


    //first Step : Rendre le constructeur privé
    private DBConnection () {
        try {
            cnx= DriverManager.getConnection(URL,USER,PASSWORD);
            System.out.println("Connected to DataBase !");
        } catch (SQLException e) {
            System.err.println("Error: "+e.getMessage());

        }
    }

    //Third Step : crrer une méthode static pour récupérer l'instance

    public static DBConnection getInstance() {
        if (instance == null) instance = new DBConnection();
        return instance;
    }
    public Connection getCnx() {
        return cnx;
    }
}
