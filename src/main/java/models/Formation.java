package models;

import java.util.Objects;

public class Formation {
    private int id_form;
    private String nom_form;
    private String description;
    private int user_id; // c'est a changer aprés l'intégration
    private Categorie categorie;

    public Formation() {
    }

    public Formation(int id_form, String nom_form, String description, int user_id, Categorie categorie) {
        this.id_form = id_form;
        this.nom_form = nom_form;
        this.description = description;
        this.user_id = user_id;
        this.categorie = categorie;
    }

    public Formation(String nom_form, String description, int user_id, Categorie categorie) {
        this.nom_form = nom_form;
        this.description = description;
        this.user_id = user_id;
        this.categorie = categorie;
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

    public int getUser_id() {
        return this.user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    @Override
    public String toString() {
        return "Formation{" +
                "id_form=" + id_form +
                ", nom_form='" + nom_form + '\'' +
                ", description='" + description + '\'' +
                ", user_id=" + user_id +
                ", categorie=" + categorie +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Formation formation)) return false;
        return getId_form() == formation.getId_form() && getUser_id() == formation.getUser_id() && Objects.equals(getNom_form(), formation.getNom_form()) && Objects.equals(getDescription(), formation.getDescription()) && Objects.equals(getCategorie(), formation.getCategorie());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_form(), getNom_form(), getDescription(), getUser_id(), getCategorie());
    }
}