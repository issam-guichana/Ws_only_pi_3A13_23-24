package models;

public class Quiz {
private int id_quiz;
private String nom_quiz;


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

    @Override
    public String toString() {
        return "Quiz{" +
                "id_quiz=" + id_quiz +
                ", nom_quiz='" + nom_quiz + '\'' +
                '}';
    }
}
