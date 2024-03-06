package services;

import models.Message;
import models.Room;
import models.user_formation;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Serviceroom implements CRUD<Room> {
    private Connection cnx ;
    private Message msg;

    public Serviceroom() {

        cnx = DBConnection.getInstance().getCnx();
    }
    public void InsertOne(Room room, user_formation uf) throws SQLException {
        String roomInsertQuery = "INSERT INTO `room` (`nom_room`, `description`) VALUES (?, ?)";
        String userFormationInsertQuery = "INSERT INTO `user_formation` (`user_id`, `form_id`, `room_id`) VALUES (1, ?, ?)";

        try (PreparedStatement roomPs = cnx.prepareStatement(roomInsertQuery, Statement.RETURN_GENERATED_KEYS);

             PreparedStatement ufPs = cnx.prepareStatement(userFormationInsertQuery)) {

            roomPs.setString(1, room.getNom_room(""));
            roomPs.setString(2, room.getDescription());
            roomPs.executeUpdate(); // Execute the room insert query

            ResultSet generatedKeys = roomPs.getGeneratedKeys();
            int roomId;
            if (generatedKeys.next()) {
                roomId = generatedKeys.getInt(1);
            } else {
                throw new SQLException("Failed to retrieve generated room ID.");
            }

            ufPs.setInt(1, uf.getForm_id());
            ufPs.setInt(2, roomId);
            ufPs.executeUpdate(); // Execute the user_formation insert query

        } catch (SQLException e) {
            // Handle any SQL errors
            e.printStackTrace();
            throw e; // Re-throw the exception to be handled by the caller
        }
    }


    @Override
    public void InsertOne(Room room) throws SQLException {

    }

    public List<Room> SelectAll() throws SQLException {
        List<Room> roomList = new ArrayList<>();

        String req = "SELECT * FROM `room`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Room r = new Room();

            r.setId_room(rs.getInt(("id_room")));
            r.setNom_room(rs.getString(2));
            r.setFormation_id(rs.getInt("formation_id"));




            roomList.add(r);
        }

        return roomList;
    }

    @Override
    public void UpdateOne(int id_room, String nouveaunom) throws SQLException {
        String req = "UPDATE room SET nom_room = ? WHERE id_room = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, nouveaunom);
        ps.setInt(2, id_room);

        ps.executeUpdate(); // Exécuter la requête

        // Fermez la PreparedStatement après utilisation.
        ps.close();
    }
    //@Override
    //public void Updatemessage(int id_msg, String nouveauContenu) throws SQLException {

   // }

    @Override
    public void DeleteOne(int id_room) throws SQLException {
        //String req = "DELETE FROM room WHERE id_room = ?;";

        String req ="UPDATE room SET status='Désactiver' WHERE id_room = ?";
                PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, id_room);

        ps.executeUpdate(); // Exécuter la requête


        // Fermez la PreparedStatement après utilisation.
        ps.close();
    }


    public void suspendroom(int id_room, int durationInMinutes) throws SQLException {
        String req = "UPDATE room SET status='suspend', suspend_time=? WHERE id_room = ?";
        try (PreparedStatement ps = cnx.prepareStatement(req)) {
            ps.setInt(1, durationInMinutes);
            ps.setInt(2, id_room);
            ps.executeUpdate(); // Execute the query
        } // The PreparedStatement will be automatically closed here
    }

    public void resuspendroom (int id_room) throws SQLException {
        String req ="UPDATE room SET status='Active' WHERE id_room = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, id_room);

        ps.executeUpdate(); // Exécuter la requête


        // Fermez la PreparedStatement après utilisation.
        ps.close();
    }



}
