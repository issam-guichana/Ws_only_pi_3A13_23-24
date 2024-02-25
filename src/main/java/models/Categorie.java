package models;

import java.util.ArrayList;
import java.util.Objects;

public class Categorie {
    private int id_cat;
    private String nom_cat;
    private ArrayList<Formation> formation;

    public Categorie() {
    }

    public Categorie(int id_cat, String nom_cat) {
        this.id_cat = id_cat;
        this.nom_cat = nom_cat;
    }

    public Categorie(int id_cat, String nom_cat, ArrayList<Formation> formation) {
        this.id_cat = id_cat;
        this.nom_cat = nom_cat;
        this.formation = formation;
    }

    public Categorie(String nom_cat, ArrayList<Formation> formation) {
        this.nom_cat = nom_cat;
        this.formation = formation;
    }

    public int getId_cat() {
        return this.id_cat;
    }

    public void setId_cat(int id_cat) {
        this.id_cat = id_cat;
    }

    public String getNom_cat() {
        return this.nom_cat;
    }

    public void setNom_cat(String nom_cat) {
        this.nom_cat = nom_cat;
    }

    public String toString() {
        return "Categorie{id_cat=" + this.id_cat + ", nom_cat='" + this.nom_cat + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categorie categorie)) return false;
        return getId_cat() == categorie.getId_cat() && Objects.equals(getNom_cat(), categorie.getNom_cat()) && Objects.equals(formation, categorie.formation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId_cat(), getNom_cat(), formation);
    }
}
