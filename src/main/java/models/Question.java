package models;

public class Question {
    private int id_quest;
    private String question;
    private String reponse;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private Quiz quiz;

    public Question() {
    }

    public Question(String question, String reponse, String option1, String option2, String option3, String option4, Quiz quiz) {
        this.question = question;
        this.reponse = reponse;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.quiz = quiz;
    }

    public Question(int id_quest, String question, String reponse, String option1, String option2, String option3, String option4, Quiz quiz) {
        this.id_quest=id_quest;
        this.question = question;
        this.reponse = reponse;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.quiz = quiz;

    }

    public int getId_quest() {
        return id_quest;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse() {
        return reponse;
    }

    public void setReponse(String reponse) {
        this.reponse = reponse;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public void setId_quest(int id_quest) {
        this.id_quest = id_quest;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id_quest=" + id_quest +
                ", question='" + question + '\'' +
                ", reponse='" + reponse + '\'' +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                ", quiz=" + quiz +
                '}';
    }
}
