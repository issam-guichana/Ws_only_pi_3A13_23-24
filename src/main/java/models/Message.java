package models;

public class Message {
private int id_msg;
private String contenu;

    public Message() {
    }

    public Message(int id_msg, String contenu) {
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
