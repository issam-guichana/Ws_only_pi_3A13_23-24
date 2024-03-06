package test;

import com.vdurmont.emoji.EmojiParser;
import models.Message;
import models.Room;
import services.Servicemessage;
import services.Serviceroom;
import utils.DBConnection;

import java.sql.SQLException;

public class Mainclass {

    public static void main(String[] args) {
        DBConnection cn1 = DBConnection.getInstance();

        Message m = new Message();
        Room r=new Room();
        String message = "Hello! 😊 This is a message with emojis! 🎉";
        String parsedMessage = EmojiParser.parseToUnicode(message);
        System.out.println(parsedMessage);

        int id_message_a_modifier = 15; // ID du message à mettre à jour
        String nouveau_contenu = "Nouveau contenu du message";
        int id_message_a_supprimer=1;
        int id_room=20;

        Servicemessage sp = new Servicemessage();
        Serviceroom sr=new Serviceroom();

        try {
           // sp.InsertOne(m);
            //(id_message_a_modifier, nouveau_contenu);
           // sp.DeleteOne(id_message_a_supprimer);
            sr.DeleteOne(id_room);
            //System.out.println("Room with ID " + id_room + " deleted successfully.");

            // sp.UpdateOne(id_message_a_modifier,nouveau_contenu);
            //sp.DeleteOne(id_message_a_supprimer);
           // System.out.println(sp.SelectAll());
        } catch (SQLException e) {
            System.err.println("Erreur: "+e.getMessage());
        }
    }
}
