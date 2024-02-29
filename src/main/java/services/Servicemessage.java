package services;

import models.Message;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Servicemessage implements CRUD<Message> {
    private Connection cnx ;
    private Message msg;

    public Servicemessage() {

        cnx = DBConnection.getInstance().getCnx();
    }


    public void InsertOne(Message msg) throws SQLException {
        String req = "INSERT INTO `message` (`contenu`, `room_id`) VALUES (?, ?)";

        try (PreparedStatement preparedStatement = cnx.prepareStatement(req)) {
            // Set the values for the parameters
            preparedStatement.setString(1, msg.getContenu());
           // preparedStatement.setString(2, msg.getSender());
            preparedStatement.setInt(2, msg.getId_room());

            // Execute the update
            preparedStatement.executeUpdate();

            System.out.println("Message Added!");
        }

    }



    public List<Message> SelectAll() throws SQLException {
        List<Message> personList = new ArrayList<>();

        String req = "SELECT * FROM `message`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Message p = new Message();

            p.setId_msg(rs.getInt(("id_msg")));
            p.setContenu(rs.getString((2)));


            personList.add(p);
        }

        return personList;
    }


    @Override
    public void UpdateOne(int id_msg, String nouveauContenu) throws SQLException {
        String req = "UPDATE message SET contenu = ? WHERE id_msg = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, nouveauContenu);
        ps.setInt(2, id_msg);

        ps.executeUpdate(); // Exécuter la requête

        // Fermez la PreparedStatement après utilisation.
        ps.close();
    }


    @Override
    public void DeleteOne(int id_msg) throws SQLException {
        //String req = "DELETE FROM message WHERE id_msg = ?";
        String req ="UPDATE message SET status='Supprimer' WHERE id_msg = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, id_msg);

        ps.executeUpdate(); // Exécuter la requête

        // Fermez la PreparedStatement après utilisation.
        ps.close();
    }
    }


