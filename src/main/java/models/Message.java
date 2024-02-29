package models;

public class Message {
private int id_msg;
private String contenu;

private String sender;

    public Message() {
    }

    public Message(int id_msg, String contenu,String sender) {
        this.id_msg = id_msg;
        this.contenu=contenu;
        this.sender=sender;
    }

    public Message(String text, int roomId) {
    }

    public Message(String content, String sender) {

        this.contenu=contenu;
        this.sender=sender;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Message(int idMsg, String contenu) {

        this.id_msg = id_msg;
        this.contenu=contenu;
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

    @Override
    public String toString() {
        return "Message{" +
                "id_msg=" + id_msg +
                ", contenu='" + contenu + '\'' +
                '}';
    }
}
