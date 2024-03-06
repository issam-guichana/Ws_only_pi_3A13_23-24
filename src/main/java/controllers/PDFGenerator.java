package controllers;

import com.google.zxing.WriterException;
import models.Evenement;
import models.Userparticipants;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import services.ServiceEvenement;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class PDFGenerator {
    public static byte[] generateQuizPDF(List<Evenement> events) throws IOException, SQLException {
        try (PDDocument document = new PDDocument()) {
            for (Evenement event : events) {
                PDPage page = new PDPage();
                document.addPage(page);

                try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                    // Load quiz image
                    PDImageXObject eventImage = PDImageXObject.createFromFile(event.getImage_event(), document);

                    // Draw quiz image
                    contentStream.drawImage(eventImage, 50, 700, 200, 100);

                    // Set font and font size for quiz name
                    contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);

                    // Set initial position for quiz name
                    contentStream.beginText();
                    contentStream.newLineAtOffset(300, 750);
                    contentStream.showText(event.getNom_event());
                    contentStream.endText();

// Set initial position for event date (same x-coordinate as quiz name)
                    contentStream.beginText();
                    contentStream.newLineAtOffset(300, 710); // Adjust the y-coordinate as needed
                    contentStream.showText(String.valueOf(event.getDate_event()));
                    contentStream.endText();

                    // Set font and font size for questions and options
                    contentStream.setFont(PDType1Font.HELVETICA, 12);

                    // Draw questions and options for the current quiz
                    ServiceEvenement sq = new ServiceEvenement();
                    List<Userparticipants> userparticipants = sq.getParticipants(event.getId_event()); // Retrieve questions associated with the quiz

                    int yOffset = 700; // Initial y-offset for questions and options
                    int lineHeight = 14; // Height of each line

                    for (Userparticipants up : userparticipants) {
                        // Display question
                        contentStream.beginText();
                        contentStream.newLineAtOffset(50, yOffset);
                        contentStream.showText(String.valueOf(up.getUser_id()));
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