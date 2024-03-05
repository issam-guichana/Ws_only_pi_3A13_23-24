package models;

import utils.DBConnection;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;

public class CalendarData {
    private HashMap<Calendar, List<String>> calendarData = new HashMap<>();
    private Connection cnx;

    public CalendarData() {
        this.cnx = DBConnection.getInstance().getCnx();
    }

    public void setData(Calendar date, String data) {
        if (!calendarData.containsKey(date)) {
            calendarData.put(date, new ArrayList<>());
        }
        calendarData.get(date).add(data);
    }

    public List<String> getData(Calendar date) {
        return calendarData.getOrDefault(date, new ArrayList<>());
    }

    // Updated method to get events for the entire month
    public List<String> getEventNamesForDay(Calendar date) {
        String req = "SELECT nom_event FROM evenement WHERE date_event = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            java.sql.Date sqlDate = new java.sql.Date(date.getTimeInMillis());
            ps.setDate(1, sqlDate);

            ResultSet resultSet = ps.executeQuery();
            List<String> eventNames = new ArrayList<>();

            while (resultSet.next()) {
                String eventName = resultSet.getString("nom_event");
                eventNames.add(eventName);
            }

            System.out.println("Events for " + sqlDate + ": " + eventNames); // Debugging line

            return eventNames;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
    public Evenement getEventForDay(Calendar date) {
        String req = "SELECT * FROM evenement WHERE date_event = ?";

        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            java.sql.Date sqlDate = new java.sql.Date(date.getTimeInMillis());
            ps.setDate(1, sqlDate);

            ResultSet resultSet = ps.executeQuery();

            if (resultSet.next()) {
                return new Evenement(
                        resultSet.getInt("id_event"),
                        resultSet.getString("nom_event"),
                        resultSet.getString("description"),
                        resultSet.getDate("date_event"),
                        resultSet.getTime("heure_deb").toLocalTime(),
                        resultSet.getString("lieu"),
                        resultSet.getInt("prix"),
                        resultSet.getInt("nbrP"),
                        resultSet.getString("image_event")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
