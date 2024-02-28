package models;

import java.util.Date;

public class Room {

    private int id_room ;
    private String nom_room ;
    private int formation_id ;
    private String description;
    private Date date_c_creation;


    public Room(int id_room,String  nom_room, int formation_id, Date date_c_creation , String description) {
        this.id_room = id_room;
        this.nom_room= nom_room;
        this.formation_id= formation_id;
        this.date_c_creation=date_c_creation;
        this.description=description;
    }
    public Room(String  nom_room, int formation_id ) {

        this.nom_room= nom_room;
        this.formation_id= formation_id;

    }

    public Room() {
    }

    public Room(String nom_room, int formation_id,String description ) {
        this.nom_room= nom_room;
        this.formation_id= formation_id;
        this.description=description;
    }



    public int getId_room() {
        return id_room;
    }

    public void setId_room(int id_room) {
        this.id_room = id_room;
    }

    public String getNom_room(String s) {
        return nom_room;
    }

    public void setNom_room(String nom_room) {
        this.nom_room = nom_room;
    }

    public int getFormation_id() {
        return formation_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFormation_id(int formation_id) {
        this.formation_id = formation_id;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id_room=" + id_room +
                '}';
    }

    public Date getDate_c_creation() {
        return date_c_creation;
    }

    public void setDate_c_creation(Date date_c_creation) {
        this.date_c_creation = date_c_creation;
    }

}
