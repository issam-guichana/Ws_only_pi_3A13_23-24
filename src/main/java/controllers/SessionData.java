package controllers;

import models.Quiz;

import java.util.HashMap;
import java.util.Map;

public class SessionData {
    private static Map<Integer, Integer> quizScores = new HashMap<>();
    private static int sumOfScores = 0;
    private static int numberOfQuizzesCompleted = 0;
    private static int numberOfQuizzes;
    private static float moyenne = 0; // Add moyenne variable

    public static int getNumberOfQuizzes() {
        return numberOfQuizzes;
    }

    public static void setNumberOfQuizzes(int numberOfQuizzes) {
        SessionData.numberOfQuizzes = numberOfQuizzes;
    }

    public static float getAverageScore() {
        if (numberOfQuizzesCompleted == 0) {
            return 0; // To avoid division by zero
        }
        return (float) sumOfScores / numberOfQuizzes;
    }

    public static float getMoyenne() {
        return moyenne;
    }

    public static void setMoyenne(float moyenne) {
        SessionData.moyenne = moyenne;
    }

    public static void addToSumOfScores(int score) {
        sumOfScores += score;
    }

    public static void incrementNumberOfQuizzesCompleted() {
        numberOfQuizzesCompleted++;
    }

    public static void storeQuizScore(int quizId, int score) {
        quizScores.put(quizId, score);
    }

    public static int getQuizScore(int quizId) {
        return quizScores.getOrDefault(quizId, 0);
    }

    public static void clearSession() {
        quizScores.clear();
        sumOfScores = 0;
        numberOfQuizzesCompleted = 0;
    }
}

