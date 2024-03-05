package models;

public class Message {
private int id_msg;
private String contenu;

private String sender;
private int id_room;
private String image;
//private byte[] imageData;
    public Message() {
    }
    //public Message(int roomId,String image) {

      //  this.id_room = roomId;
        //this.imageData = imageData;
        //this.image=image;
    //}

    public Message(int id_msg, String contenu,String sender,int id_room, String imageData) {
        this.id_msg = id_msg;
        this.contenu=contenu;
        this.sender=sender;
        this.id_room = id_room;
        this.image = image;
    }

    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    public Message(int id_msg, String contenu) {
        this.id_msg = id_msg;
        this.contenu=contenu;

    }


    public Message(String contenu, String sender) {

        this.contenu=contenu;
        this.sender=sender;
    }
    public Message(String contenu,int id_room) {

        this.contenu=contenu;

        this.id_room = id_room;
    }
    public Message(String contenu, String sender,int id_room) {

        this.contenu=contenu;
        this.sender=sender;
        this.id_room = id_room;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }



    public int getId_msg() {
        return id_msg;
    }

    public String getContenu() {
        return contenu;
    }

    public void setId_msg(int id_msg) {
        this.id_msg = id_msg;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }
       public String getcontent() {
        return contenu ;
       }
    @Override
    public String toString() {
        return "Message{" +
                "id_msg=" + id_msg +
                ", contenu='" + contenu + '\'' +
                '}';
    }
}
