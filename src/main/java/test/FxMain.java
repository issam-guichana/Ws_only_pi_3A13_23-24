package test;



import com.google.api.services.drive.Drive;
import controllers.CrudFormationFXML;
import controllers.DriveController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// import models.GoogleDriveUploader;

// import java.io.IOException;
// import javafx.application.Application;
// import javafx.fxml.FXMLLoader;
// import javafx.scene.Parent;
// import javafx.scene.Scene;
// import javafx.stage.Stage;


public class FxMain extends Application {
    public static void main(String[] args) {launch();}

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass()
              .getResource("/LoginUser.fxml"));
                //.getResource("/UpdateProfile.fxml"));
                //.getResource("/AdminInterface.fxml"));
                 //.getResource("/WebcamInterface.fxml"));
                ///.getResource("/UserSettings.fxml"));
      
      //**********AZIZ
         // FXMLLoader loader = new FXMLLoader(getClass().getResource("/AjouterEvenemntFXML.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/calendierFXML.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/EventDetailsFXML.fxml"));
        //FXMLLoader loader = new FXMLLoader(getClass().getResource("/participerFXML.fxml"));
      
      //******************Sadok
//        FXMLLoader loader= new FXMLLoader(getClass().getResource("/AjouterQuiz.fxml"));
// //        FXMLLoader loader= new FXMLLoader(getClass().getResource("/UserQuiz.fxml"));
      //*************Rim
//       FXMLLoader loader = new FXMLLoader(getClass().getResource("/AfficherCertificatXML.fxml"));
      

        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        stage.setTitle("Formini.tn");
        stage.setScene(scene);
        stage.setResizable(true);
        stage.show();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrudFormationFXML.fxml"));
            Parent root = loader.load();

            // Check if the loaded FXML file is CrudFormationFXML
            if (loader.getController() instanceof CrudFormationFXML) {
                // Get Drive service from DriveController
                Drive driveService = DriveController.getDriveService();

                if (driveService != null) {
                    // Inject Drive service into GoogleDriveUploader
                    GoogleDriveUploader driveUploader = new GoogleDriveUploader(driveService);

                    // Set GoogleDriveUploader in the controller
                    CrudFormationFXML controller = loader.getController();
                    controller.setDriveUploader(driveUploader);
                } else {
                    System.err.println("Failed to get Drive service from DriveController.");
                }
            } else {
                System.err.println("Loaded FXML controller is not an instance of CrudFormationFXML.");
            }

            primaryStage.setTitle("Your Application Title");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            System.err.println("Error loading FXML file: " + e.getMessage());
        }
    }
}
