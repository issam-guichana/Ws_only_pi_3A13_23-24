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
        //  this.msg = msg;
        String req = "INSERT INTO `message`(`id_msg`, `contenu`) VALUES " +
                "("+msg.getId_msg()+",'"+msg.getContenu()+"')";
        Statement st = cnx.createStatement();
        st.executeUpdate(req);
        System.out.println("Person Added !");

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
        String req = "DELETE FROM message WHERE id_msg = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, id_msg);

        ps.executeUpdate(); // Exécuter la requête

        // Fermez la PreparedStatement après utilisation.
        ps.close();
    }
    }


