package models;

import utils.DBConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.time.LocalDate;
/**
 * This Class holds a HashMap with a Calendar object as key and String as value
 * @author BenA
 */
public class CalendarData {
    private HashMap<Calendar, String> calendarData = new HashMap<>();
    private Connection cnx;

    // Constructor to initialize the database connection
    public CalendarData() {
        this.cnx = DBConnection.getInstance().getCnx();
    }

    /*
     * This Method adds the data value with the Calendar date as key
     */
    public void setData(Calendar date, String data) {
        calendarData.put(date, data);
    }

    /*
     * This method returns the String in the given Calendar date
     */
    public String getData(Calendar date) {
        return calendarData.get(date);
    }

    public String getEventName(java.util.Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = dateFormat.format(date);
        System.out.println("Formatted Date: " + formattedDate);

        String req = "SELECT nom_event FROM evenement WHERE date_event = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setString(1, formattedDate);
            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                String eventName = resultSet.getString("nom_event");
                System.out.println("Event Name from Database: " + eventName);
                return eventName;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
