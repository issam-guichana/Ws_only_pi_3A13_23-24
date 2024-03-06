package controllers;

        import com.google.zxing.WriterException;
        import models.Question;
        import models.Quiz;
        import org.apache.pdfbox.pdmodel.PDDocument;
        import org.apache.pdfbox.pdmodel.PDPage;
        import org.apache.pdfbox.pdmodel.PDPageContentStream;
        import org.apache.pdfbox.pdmodel.font.PDType1Font;
        import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
        import services.ServiceQuestion;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.sql.SQLException;
        import java.util.List;


public class PDFGenerator {
    public static byte[] generateQuizPDF(List<Quiz> quizzes) throws IOException, SQLException {
        try (PDDocument document = new PDDocument()) {
            for (Quiz quiz : quizzes) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    // Load quiz image
                    PDImageXObject quizImage = PDImageXObject.createFromFile(quiz.getImage(), document);

                    // Draw quiz image
                    contentStream.drawImage(quizImage, 50, 700, 200, 100);

                    // Set font and font size for quiz name
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);

                    // Set initial position for quiz name
                    contentStream.beginText();
                    contentStream.newLineAtOffset(300, 750);

                    // Draw quiz name
                    contentStream.showText(quiz.getNom_quiz());
                    contentStream.endText();

                    // Set font and font size for questions and options
                    contentStream.setFont(PDType1Font.HELVETICA, 12);

                    // Draw questions and options for the current quiz
                    ServiceQuestion sq = new ServiceQuestion();
                    List<Question> questions = sq.selectQuestionByQuiz(quiz); // Retrieve questions associated with the quiz

                    int yOffset = 700; // Initial y-offset for questions and options
                    int lineHeight = 14; // Height of each line

                    for (Question question : questions) {
                        // Display question
                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yOffset);
                        contentStream.showText(question.getQuestion());
                        contentStream.endText();
                        yOffset -= lineHeight; // Move to the next line

                        // Display options
                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yOffset);
                        contentStream.showText("1) " + question.getOption1());
                        contentStream.endText();
                        yOffset -= lineHeight; // Move to the next line

                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yOffset);
                        contentStream.showText("2) " + question.getOption2());
                        contentStream.endText();
                        yOffset -= lineHeight; // Move to the next line

                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yOffset);
                        contentStream.showText("3) " + question.getOption3());
                        contentStream.endText();
                        yOffset -= lineHeight; // Move to the next line

                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yOffset);
                        contentStream.showText("4) " + question.getOption4());
                        contentStream.endText();
                        yOffset -= lineHeight; // Move to the next line

                    }
                }
            }

            // Save PDF to byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            document.save(outputStream);
            return outputStream.toByteArray();
        }
    }
}

