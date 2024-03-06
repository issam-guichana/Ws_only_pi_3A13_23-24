package test;

import models.Evenement;
import services.ServiceEvenement;
import utils.DBConnection;

import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;

public class Main {
    public static void main(String[] args) {
        LocalTime heure_deb;
        Date date_event;
        DBConnection cn1 = DBConnection.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date parsedDate = dateFormat.parse("2014-05-12");
            date_event = new java.sql.Date(parsedDate.getTime());

            // Use LocalTime.of() to create a LocalTime instance
            heure_deb = LocalTime.of(12, 0, 0); // Replace with your actual time values
        } catch (ParseException e2) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd");
            e2.printStackTrace();
            return;
        }

        //Evenement e = new Evenement(1, "hello", "test", date_event, heure_deb, 2500, 5);
        //Evenement e1 =new Evenement(7,"zazee",2024-02-15,00:00:00,"aziz",55,11);

        ServiceEvenement sp = new ServiceEvenement();

        try {
            //sp.insertOne(e);
            //sp.updateOne(e);
            //sp.deleteOne(e);

            System.out.println(sp.getParticipants(5));
        } catch (SQLException e2) {
            System.err.println("Erreur: "+e2.getMessage());
        }


    }
}

