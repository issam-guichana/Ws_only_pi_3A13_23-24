package models;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Evenement {
    private int nbrP;
    private String nom_event;
    private String lieu;
    private String description;
    private int prix,id_event;
    private LocalTime heure_deb;
    private LocalDate date_event;
    private String image_event;
    private List<Userparticipants> participants;

    public Evenement(String nom_event, String description, java.sql.Date date_event, LocalTime heure_deb,String lieu, int prix, int nbrP,String image_event){
        this.nom_event = nom_event;
        this.description = description;
        this.prix = prix;
        this.nbrP = nbrP;
        // Convert java.sql.Date to LocalDate
        this.date_event = date_event.toLocalDate();
        this.image_event = image_event;
        // Set LocalTime directly
        this.heure_deb = heure_deb;
        this.lieu = lieu;
    }

    public Evenement(int id_event,String nom_event, String description, java.sql.Date date_event, LocalTime heure_deb,String lieu, int prix,int nbrP,String image_event) {
        this.nom_event = nom_event;
        this.description = description;
        this.prix = prix;
        this.nbrP = nbrP;
        this.lieu = lieu;
        // Convert java.sql.Date to LocalDate
        this.date_event = date_event.toLocalDate();
        this.id_event=id_event;
        // Set LocalTime directly
        this.heure_deb = heure_deb;
        this.image_event = image_event;

    }
    public List<Userparticipants> getParticipants() {
        return participants;
    }
    public void setParticipants(List<Userparticipants> participants) {
        this.participants = participants;
    }
    public Evenement() {

    }

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public String getImage_event() {
        return image_event;
    }

    public void setImage_event(String image_event) {
        this.image_event = image_event;
    }

    public int getNbrP() {
        return nbrP;
    }

    public void setNbrP(int nbrP) {
        this.nbrP = nbrP;
    }

    public String getNom_event() {
        return nom_event;
    }

    public void setNom_event(String nom_event) {
        this.nom_event = nom_event;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getId_event() {
        return id_event;
    }

    public void setId_event(int id_event) {
        this.id_event = id_event;
    }

    public LocalTime getHeure_deb() {
        return heure_deb;
    }

    public void setHeure_deb(LocalTime heure_deb) {
        this.heure_deb = heure_deb;
    }

    public LocalDate getDate_event() {
        return date_event;
    }

    public void setDate_event(LocalDate date_event) {
        this.date_event = date_event;
    }

    @Override
    public String toString() {
        return "Evenement{" +
                "nbrP=" + nbrP +
                ", nom_event='" + nom_event + '\'' +
                ", lieu='" + lieu + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", id_event=" + id_event +
                ", heure_deb=" + heure_deb +
                ", date_event=" + date_event +
                ", image_event='" + image_event + '\'' +
                ", participants=" + participants +
                '}';
    }
    public String generateMapUrl() {
        // Use the Google Maps Static API to generate a map URL based on the location
        // Example URL format: https://maps.googleapis.com/maps/api/staticmap?center=place&size=600x300&key=YOUR_API_KEY
        // Replace YOUR_API_KEY with your actual Google Maps API key
        return "https://maps.googleapis.com/maps/api/staticmap?center=" + lieu + "&size=600x300&key=YOUR_API_KEY";
    }
}
