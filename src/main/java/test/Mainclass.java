package test;

import models.Message;
import services.Servicemessage;
import utils.DBConnection;

import java.sql.SQLException;

public class Mainclass {

    public static void main(String[] args) {
        DBConnection cn1 = DBConnection.getInstance();

        Message m = new Message();


        int id_message_a_modifier = 15; // ID du message à mettre à jour
        String nouveau_contenu = "Nouveau contenu du message";
        int id_message_a_supprimer=15;

        Servicemessage sp = new Servicemessage();

        try {
           // sp.InsertOne(m);
            //(id_message_a_modifier, nouveau_contenu);
            sp.UpdateOne(id_message_a_modifier,nouveau_contenu);
            sp.DeleteOne(id_message_a_supprimer);
           // System.out.println(sp.SelectAll());
        } catch (SQLException e) {
            System.err.println("Erreur: "+e.getMessage());
        }
    }
}
