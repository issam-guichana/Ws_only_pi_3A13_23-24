package controllers;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrCode {

    public static void generateQRCode()  {
        String data = "Cette certificat est attribué de formini.tn";
        String path = "C:/Users/rymra/IdeaProjects/Formini.tn/src/main/resources/images/";

        try {
            BitMatrix matrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, 500, 500);
            Path imagePath = FileSystems.getDefault().getPath(path + "output.jpg");
            MatrixToImageWriter.writeToPath(matrix, "jpg", imagePath);
            System.out.println("QR Code generated successfully at: " + imagePath);
        } catch (Exception e) {
            System.out.println("Could not generate QR Code: " + e.getMessage());
        }
    }
}
