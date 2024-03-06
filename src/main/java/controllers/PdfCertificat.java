package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PdfCertificat {
    @FXML
    private void initialize() {
        // Initialization code, if needed
    }

    public void generateCertificate(String userName, File backgroundImageFile) {
        try {
            // Create a new PDF document
            PDDocument document = new PDDocument();
            PDPage page = new PDPage();
            page.setRotation(90);
            document.addPage(page);

            // Get the content stream of the PDF page
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            // Add background photo
            PDImageXObject backgroundImage = PDImageXObject.createFromFileByContent(backgroundImageFile, document);
            contentStream.drawImage(backgroundImage, 0, 0, page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

            // Adjust coordinates for rotated content
            float x = 300; // Adjust X coordinate
            float y = 250; // Adjust Y coordinate

            // Add details to the certificate template
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 40);
            // Set text matrix for rotated text
            contentStream.setTextMatrix(0, 1, -1, 0, x, y);
            contentStream.showText("Issam Guichana");

            contentStream.setFont(PDType1Font.HELVETICA, 18);

            contentStream.newLineAtOffset(100, -60);
            contentStream.showText(userName); // Add the name of the user

            contentStream.newLineAtOffset(-200, -130);
            contentStream.showText(getCurrentDate()); // Completion date
            contentStream.endText();

            // Close the content stream
            contentStream.close();

            // Save the PDF document
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Certificate");
            fileChooser.setInitialFileName("certificate.pdf");
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                document.save(file);
                System.out.println("Certificate saved successfully.");
            }

            // Close the PDF document
            document.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private String getCurrentDate() {
        // Get current date in the format: dd MMMM yyyy (e.g., 01 March 2024)
        Calendar calendar = Calendar.getInstance();
        return String.format("%1$td %1$tB %1$tY", calendar);
    }

    public void ExportPdf(ActionEvent event) {
        generateCertificate("JAVA", new File("C:/Users/rymra/Downloads/Certificat.png"));
    }
}