package test;

import java.sql.SQLException;
import models.Categorie;
import services.ServiceCategorie;
import utils.DBConnection;

public class MainClass {
    public MainClass() {
    }

    public static void main(String[] args) {
        try {
            DBConnection dbConnection = DBConnection.getInstance();
            ServiceCategorie serviceCategorie = new ServiceCategorie();
            Categorie categorie = new Categorie(2, "cc");
            serviceCategorie.insertOne(categorie);
            //serviceCategorie.deleteOne(categorie);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
