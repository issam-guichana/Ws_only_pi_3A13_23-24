package test;

import models.User;
import services.UserService;
import utils.DBconnection;

import java.sql.SQLException;

public class MainClass {
    public static void main(String[] args) {
        DBconnection cn1 =DBconnection.getInstance();

        User u = new User(3,"issam","issam@esprit.tn","azerty",22,"admin");
       // User uid = new User(3);

        UserService us = new UserService();

        try {
           // us.deleteOne(u);
            System.out.println(us.selectAll());
        } catch (SQLException e) {
            System.err.println("Erreur: "+e.getMessage());
        }

    }
}