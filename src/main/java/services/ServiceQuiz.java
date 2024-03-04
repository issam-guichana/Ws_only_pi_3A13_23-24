package services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import models.Quiz;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceQuiz implements CRUD<Quiz> {
    private Connection cnx ;

    public ServiceQuiz() {
        cnx = DBConnection.getInstance().getCnx();
    }

    @Override
    public void insertOne(Quiz quiz) throws SQLException {
        String req = "INSERT INTO `quiz`(`nom_quiz`,`image`) VALUES " +
                "(?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setString(1, quiz.getNom_quiz());
        ps.setString(2, quiz.getImage());
        ps.executeUpdate();
        System.out.println("Quiz Added !");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Dialog");
        alert.setContentText("Quiz insérée avec succés!");
        alert.show();

    }

    @Override
    public void updateOne(Quiz quiz) throws SQLException {
        String req = "UPDATE `quiz` SET `nom_quiz` = ?,`image` = ? WHERE `id_quiz` = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, quiz.getNom_quiz());
        ps.setString(2, quiz.getImage());
        ps.setInt(3, quiz.getId_quiz());
        ps.executeUpdate();
        System.out.println("Quiz Updated !");
    }

    @Override
    public void deleteOne(Quiz quiz) throws SQLException {
        String req = "DELETE FROM `quiz` WHERE `id_quiz` = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, quiz.getId_quiz());

        ps.executeUpdate();
        System.out.println("Quiz Deleted !");

    }

    @Override
    public List<Quiz> selectAll() throws SQLException {
        List<Quiz> quizList = new ArrayList<>();

        String req = "SELECT * FROM `quiz`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Quiz p = new Quiz();

            p.setId_quiz(rs.getInt(("id_quiz")));
            p.setNom_quiz(rs.getString(("nom_quiz")));
            p.setImage(rs.getString(("image")));

            quizList.add(p);
        }

        return quizList;
    }

    public Quiz getQuizById(int quizId) throws SQLException {
        String req = "SELECT * FROM quiz WHERE id_quiz = ?";
        PreparedStatement ps = cnx.prepareStatement(req);
        ps.setInt(1, quizId);
        ResultSet rs = ps.executeQuery();

        Quiz quiz = null;
        if (rs.next()) {
            quiz = new Quiz();
            quiz.setId_quiz(rs.getInt("id_quiz"));
            quiz.setNom_quiz(rs.getString("nom_quiz"));
        }

        rs.close();
        ps.close();

        return quiz;
    }
    public List<Quiz> ChercherParQuizName(String quizName) throws SQLException {
        List<Quiz> quizList = new ArrayList<>();
        PreparedStatement pre = cnx.prepareStatement("SELECT * FROM quiz WHERE nom_quiz LIKE ?");
        pre.setString(1, "%" + quizName + "%");
        ResultSet result = pre.executeQuery();
        while (result.next()) {
            Quiz p = new Quiz();
            p.setId_quiz(result.getInt("id_quiz"));
            p.setNom_quiz(result.getString("nom_quiz"));
            p.setImage(result.getString("image"));
            quizList.add(p);
        }
        return quizList;
    }


}
