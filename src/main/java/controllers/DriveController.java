package controllers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.FileList;
import com.google.auth.http.HttpCredentialsAdapter;
import com.google.auth.oauth2.GoogleCredentials;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class DriveController {

    private static final String APPLICATION_NAME = "Formini.tn";
    private static final String CLIENT_ID = "847964430065-uhaf96gfig9elq14ete37pc8r83v896k.apps.googleusercontent.com";
    private static final String CLIENT_SECRET = "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\n";
    private static final String refreshToken = "1//042A-ZkndqaaACgYIARAAGAQSNwF-L9Ir03vELTybOWIg4pMg76JCnpu3JsSm9Nbe0WxlPHmsosX2OUyxHidMbdot-WpdWcEknJk";
    private static final HttpTransport httpTransport;

    static {
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final List<String> SCOPES = Collections.singletonList(DriveScopes.DRIVE);

    public static Drive getDriveService() throws IOException, GeneralSecurityException {
        GoogleCredentials credentials;
        try (InputStream in = DriveController.class.getResourceAsStream("/formini-83adda5815f5.json")) {
            credentials = GoogleCredentials.fromStream(in)
                    .createScoped(Collections.singletonList(DriveScopes.DRIVE));
        }
        return new Drive.Builder(httpTransport, JSON_FACTORY, new HttpCredentialsAdapter(credentials))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static java.io.File downloadFile(String fileId) throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();
        java.io.File tempFile = java.io.File.createTempFile("temp", ".pdf");
        try (OutputStream outputStream = new FileOutputStream(tempFile)) {
            driveService.files().get(fileId).executeMediaAndDownloadTo(outputStream);
        }
        return tempFile;
    }

    public static List<com.google.api.services.drive.model.File> listFiles() throws IOException, GeneralSecurityException {
        Drive driveService = getDriveService();
        FileList result = driveService.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        return result.getFiles();
    }
    public static List<com.google.api.services.drive.model.File> listFilesForClient(Drive driveService) throws IOException {
        FileList result = driveService.files().list()
                .setPageSize(10)
                .setFields("nextPageToken, files(id, name)")
                .execute();
        return result.getFiles();
    }
}
