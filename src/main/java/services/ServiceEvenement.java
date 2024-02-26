package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import models.Evenement;
import models.Userparticipants;
import utils.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class ServiceEvenement implements CRUD<Evenement> {

    private Connection cnx;
    private TableView<Evenement> tbEvents;

    public ServiceEvenement(TableView<Evenement> tbEvents) {
        this.tbEvents = tbEvents;
        cnx = DBConnection.getInstance().getCnx();
    }


    public ServiceEvenement() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override

    public void insertOne(Evenement evenement) throws SQLException {
        String req = "INSERT INTO `evenement`(`nom_event`,`description`,`date_event`, `heure_deb`, `prix`,`nbrP`,`image_event`) VALUES " +
                "(?,?,?,?,?,?,?)";

        try (PreparedStatement ps = cnx.prepareStatement(req, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, evenement.getNom_event());
            ps.setString(2, evenement.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(evenement.getDate_event()));
            ps.setTime(4, java.sql.Time.valueOf(evenement.getHeure_deb()));
            ps.setInt(5, evenement.getPrix());
            ps.setInt(6, evenement.getNbrP());
            ps.setString(7, evenement.getImage_event());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Insertion failed, no rows affected.");
            }

            // Get the generated ID if needed
            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    evenement.setId_event(generatedId);
                } else {
                    throw new SQLException("Insertion failed, no ID obtained.");
                }
            }
        }

        // Update the TableView right after insertion
        updateTableView();
    }

    private void updateTableView() {
        try {
            List<Evenement> events = selectAll();
            ObservableList<Evenement> el = FXCollections.observableArrayList(events);
            tbEvents.setItems(el);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnes : " + e.getMessage());
        }
    }


    public void updateOne(Evenement evenement) throws SQLException {
        try {
            String req = "UPDATE evenement SET nom_event=?,description=?,date_event=?,heure_deb=?,prix=?,nbrP=?,image_event=? WHERE id_event=?";
            PreparedStatement ps = cnx.prepareStatement(req);

            // Définition des valeurs des paramètres de substitution
            ps.setString(1, evenement.getNom_event());
            ps.setString(2, evenement.getDescription());
            ps.setObject(3, java.sql.Date.valueOf(evenement.getDate_event()));
            ps.setObject(4, evenement.getHeure_deb());
            ps.setInt(5, evenement.getPrix());
            ps.setInt(6, evenement.getNbrP());
            ps.setInt(7, evenement.getId_event());
            ps.setString(8, evenement.getImage_event());
            // Exécution de la requête préparée
            ps.executeUpdate();
            System.out.println("Evenement modifié avec succès...");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override

    public void deleteOne(Evenement evenement) throws SQLException {
        try {
            Statement st = cnx.createStatement();
            String req = "DELETE FROM evenement WHERE id_event = " + evenement.getId_event() + "";
            st.executeUpdate(req);
            System.out.println("evenement  supperimer avec succès...");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public List<Evenement> selectAll() throws SQLException {
        List<Evenement> evenementsList = new ArrayList<>();

        String req = "SELECT * FROM evenement";
        try (Statement st = cnx.createStatement();
             ResultSet rs = st.executeQuery(req)) {

            while (rs.next()) {
                Evenement e = new Evenement();

                e.setId_event(rs.getInt(1));
                e.setNom_event(rs.getString(5));
                e.setDescription(rs.getString(2));
                e.setPrix(rs.getInt(6));

                // Assuming column 5 is date_event (LocalDate)
                e.setDate_event(rs.getObject(3, LocalDate.class));

                // Assuming column 6 is heure_deb (LocalTime)
                e.setHeure_deb(rs.getObject(4, LocalTime.class));

                e.setNbrP(rs.getInt(7));

                evenementsList.add(e);
            }
        }

        return evenementsList;
    }

    public List<Userparticipants> getParticipants(int event_id) throws SQLException {
        List<Userparticipants> participants = new ArrayList<>();

        // Query to fetch participants for the given event including event_name
        String query = "SELECT up.user_id, up.userName, up.event_id, e.nom_event " +
                "FROM usr_evt up " +
                "JOIN evenement e ON up.event_id = e.id_event " +
                "WHERE up.event_id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, event_id);

            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    Userparticipants participant = new Userparticipants();
                    participant.setUser_id(resultSet.getInt("user_id"));
                    participant.setUserName(resultSet.getString("userName"));
                    participant.setEvent_id(resultSet.getInt("event_id"));
                    participant.setEvent_name(resultSet.getString("nom_event"));
                    participants.add(participant);
                }
            }
        }
        System.out.println("getParticipants");
        return participants;
    }

    public void deletetwo(Userparticipants up) throws SQLException {
        try {
            Statement st = cnx.createStatement();
            String req = "DELETE FROM usr_evt WHERE User_id = " + up.getUser_id() + "";
            st.executeUpdate(req);
            System.out.println("Participant  supperimer avec succès...");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}




