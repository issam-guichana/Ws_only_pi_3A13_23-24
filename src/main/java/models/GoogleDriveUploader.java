package models;

import com.google.api.client.http.InputStreamContent;
import com.google.api.services.drive.Drive;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;

public class GoogleDriveUploader {
    private final Drive driveService;

    public GoogleDriveUploader(Drive driveService) {
        this.driveService = driveService;
    }

    public void uploadFile(String fileName, FileInputStream file) throws IOException {
        com.google.api.services.drive.model.File driveFile = new com.google.api.services.drive.model.File();
        driveFile.setName(fileName);
        driveFile.setParents(Collections.singletonList("1CzLY0WYQW2jUWuiq36Yt_3lDr_FwaZe_"));

        // Create a file content from FileInputStream
        InputStreamContent content = new InputStreamContent("application/pdf", file);

        // Execute the file creation request on Google Drive
        driveService.files().create(driveFile, content).execute();
    }
}
