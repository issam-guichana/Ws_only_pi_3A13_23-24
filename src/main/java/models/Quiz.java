package models;

public class Quiz {
private int id_quiz;
private String nom_quiz;
private String Image;


    public Quiz(String nom_quiz, String image) {
        this.nom_quiz = nom_quiz;
        this.Image = image;
    }

    public Quiz(int id_quiz, String nom_quiz, String image) {
        this.id_quiz = id_quiz;
        this.nom_quiz = nom_quiz;
        Image = image;
    }

    public Quiz() {}

    public Quiz(String nom_quiz) {
        this.nom_quiz = nom_quiz;
    }

    public Quiz(int id_quiz, String nom_quiz) {
        this.id_quiz = id_quiz;
        this.nom_quiz = nom_quiz;
    }

    public int getId_quiz() {
        return id_quiz;
    }

    public void setId_quiz(int id_quiz) {
        this.id_quiz = id_quiz;
    }

    public String getNom_quiz() {
        return nom_quiz;
    }

    public void setNom_quiz(String nom_quiz) {
        this.nom_quiz = nom_quiz;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    @Override
    public String toString() {
        return "Quiz{" +
                "id_quiz=" + id_quiz +
                ", nom_quiz='" + nom_quiz + '\'' +
                ", Image='" + Image + '\'' +
                '}';
    }
}
