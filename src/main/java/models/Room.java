package models;

public class Room {

    private int id_room ;

    public Room(int id_room) {
        this.id_room = id_room;
    }

    public Room() {
    }

    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id_room=" + id_room +
                '}';
    }
}
