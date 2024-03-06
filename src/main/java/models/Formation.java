package models;

import java.util.Objects;

public class Formation {
    private int id_form;
    private String nom_form;
    private String description;
    private int prix;
    private User_formation user_id;
    private Categorie categorie;
    private int certif_id;

    public Formation(String nomFormation, String description, String formateur, Categorie categorie, int prix) {
    }


    public Formation(String nom_form, String description, int prix, Categorie categorie) {
        this.id_form = id_form;
        this.nom_form = nom_form;
        this.description = description;
        this.prix = prix;
        this.user_id = user_id;
        this.categorie = categorie;
        this.certif_id = certif_id;
    }

    public Formation(String nom_form, String description, int prix, User_formation user_id, Categorie categorie, int certif_id) {
        this.nom_form = nom_form;
        this.description = description;
        this.prix = prix;
        this.user_id = user_id;
        this.categorie = categorie;
        this.certif_id = certif_id;
    }

    public Formation() {

    }

    public Formation(String nomForm, int userId, String description, int idCat, Categorie categorie) {
    }

    public Formation(String nomForm, String description, int prix, User_formation userFormation, Categorie categorie) {
    }

    public int getId_form() {
        return this.id_form;
    }

    public void setId_form(int id_form) {
        this.id_form = id_form;
    }

    public String getNom_form() {
        return this.nom_form;
    }

    public void setNom_form(String nom_form) {
        this.nom_form = nom_form;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User_formation getUser_id() {
        return user_id;
    }

    public void setUser_id(User_formation user_id) {
        this.user_id = user_id;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public int getCertif_id() {
        return certif_id;
    }

    public void setCertif_id(int certif_id) {
        this.certif_id = certif_id;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id_form=" + id_form +
                ", nom_form='" + nom_form + '\'' +
                ", description='" + description + '\'' +
                ", prix=" + prix +
                ", user_id=" + user_id +
                ", categorie=" + categorie +
                ", certif_id=" + certif_id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Formation formation)) return false;
        return getId_form() == formation.getId_form() && getPrix() == formation.getPrix() && getCertif_id() == formation.getCertif_id() && Objects.equals(getNom_form(), formation.getNom_form()) && Objects.equals(getDescription(), formation.getDescription()) && Objects.equals(getUser_id(), formation.getUser_id()) && Objects.equals(getCategorie(), formation.getCategorie());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_form(), getNom_form(), getDescription(), getPrix(), getUser_id(), getCategorie(), getCertif_id());
    }
}