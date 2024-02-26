package test;


import models.Question;
import models.Quiz;
import services.ServiceQuestion;
import services.ServiceQuiz;
import utils.DBConnection;

import java.sql.SQLException;
import java.sql.SQLOutput;

public class Main {
    public static void main(String[] args) {
        DBConnection cn1 = DBConnection.getInstance();
        Quiz q = new Quiz("TestQuiz","azerfdsq");
        Quiz q1 = new Quiz(2,"TestQuiz2");
        Question quest = new Question(1,"1+1?","2","1","5","4","2",q);
        Question quest1 = new Question(2,"2+2?","4","1","5","4","2",q);

        ServiceQuestion squest =new ServiceQuestion();
        ServiceQuiz sq = new ServiceQuiz();

        try {
            //squest.insertOne(quest);
            //sq.insertOne(q);
            System.out.println(q.getImage());
            System.out.println(squest.selectAll());
            //sq.updateOne(q1);
            //sq.deleteOne(q1);
            //squest.updateOne(quest1);
            //squest.deleteOne(quest1);
        } catch (SQLException e) {
            System.err.println("Erreur: "+e.getMessage());
        }

    }

    }
