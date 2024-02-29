package services;

import models.Message;
import models.Room;
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
    public void InsertOne(Room room) throws SQLException {
        String req = "INSERT INTO `room` (`nom_room`,`formation_id`,`description`) VALUES (?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        //  ps.setInt(1, room.getId_room());
        ps.setString(1, room.getNom_room(""));
        ps.setInt(2, room.getFormation_id());
        ps.setString(3, room.getDescription());

        //  ps.setString(2, msg.getContenu());

        ps.executeUpdate(); // Exécuter la requête

        // Fermez la PreparedStatement après utilisation.
        ps.close();
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




}
