package services;

import models.Question;
import models.Quiz;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ServiceQuestion implements CRUD<Question>{
private Connection cnx ;

public ServiceQuestion() {
        cnx = DBConnection.getInstance().getCnx();
        }
@Override
    public void insertOne(Question quest) throws SQLException {
        String req = "INSERT INTO `question`(`question`,`reponse`,`option1`,`option2`,`option3`,`option4`,`id_quiz`) VALUES " +
                "(?,?,?,?,?,?,?)";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, quest.getQuestion());
        ps.setString(2, quest.getReponse());
        ps.setString(3, quest.getOption1());
        ps.setString(4, quest.getOption2());
        ps.setString(5, quest.getOption3());
        ps.setString(6, quest.getOption4());
        ps.setInt(7, quest.getQuiz().getId_quiz());



        ps.executeUpdate();
    }
@Override
public void updateOne(Question quest) throws SQLException {
        String req = "UPDATE `question` SET `question` = ?,`reponse` = ?,`option1` = ?,`option2` = ?,`option3` = ?,`option4` = ? WHERE `id_quiz` = ? AND `id_quest` = ? ";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setString(1, quest.getQuestion());
        ps.setString(2, quest.getReponse());
        ps.setString(3, quest.getOption1());
        ps.setString(4, quest.getOption2());
        ps.setString(5, quest.getOption3());
        ps.setString(6, quest.getOption4());
        ps.setInt(7, quest.getQuiz().getId_quiz());
        ps.setInt(8, quest.getId_quest());

        ps.executeUpdate();
        System.out.println("Quiz Updated !");
        }

@Override
public void deleteOne(Question quest) throws SQLException {
        String req = "DELETE FROM `question` WHERE `id_quest` = ?";
        PreparedStatement ps = cnx.prepareStatement(req);

        ps.setInt(1, quest.getId_quest());

        ps.executeUpdate();
        System.out.println("Quiz Deleted !");

        }


    @Override
    public List<Question> selectAll() throws SQLException {
        List<Question> questionList = new ArrayList<>();

        String req = "SELECT * FROM `question`";
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Question p = new Question();

            p.setId_quest(rs.getInt(("id_quest")));
            p.setQuestion(rs.getString(("question")));
            p.setOption1(rs.getString(("option1")));
            p.setOption2(rs.getString(("option2")));
            p.setOption3(rs.getString(("option3")));
            p.setOption4(rs.getString(("option4")));
            p.setReponse(rs.getString(("reponse")));

            int quizId = rs.getInt("id_quiz");
            ServiceQuiz sq=new ServiceQuiz();
            Quiz quiz = sq.getQuizById(quizId);
            p.setQuiz(quiz);

            questionList.add(p);
        }

        return questionList;
    }
    public List<Question> selectQuestionByQuiz(Quiz quiz) throws SQLException {
        List<Question> questionList = new ArrayList<>();

        String req = "SELECT * FROM `question` WHERE `id_quiz`= "+quiz.getId_quiz();
        Statement st = cnx.createStatement();

        ResultSet rs = st.executeQuery(req);

        while (rs.next()){
            Question p = new Question();

            p.setId_quest(rs.getInt(("id_quest")));
            p.setQuestion(rs.getString(("question")));
            p.setOption1(rs.getString(("option1")));
            p.setOption2(rs.getString(("option2")));
            p.setOption3(rs.getString(("option3")));
            p.setOption4(rs.getString(("option4")));
            p.setReponse(rs.getString(("reponse")));
            p.setQuiz(quiz);

            questionList.add(p);
        }

        return questionList;
    }


}
