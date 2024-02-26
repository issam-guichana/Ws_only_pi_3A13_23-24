package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FXmain extends Application {


    public static void main(String[] args) {

        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        //  FXMLLoader loader = new FXMLLoader(getClass()
        //        .getResource("/AjouterroomFXML.fxml"));

        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/DashbordadminFXML.fxml"));

        // FXMLLoader loader = new FXMLLoader(getClass()
        //  .getResource("/AjoutermsgFXML.fxml"));

        //   FXMLLoader loader = new FXMLLoader(getClass()
        //         .getResource("/AjoutermsgformateurFXML.fxml"));

        //FXMLLoader loader = new FXMLLoader(getClass()
        //      .getResource("/Listroom.fxml"));

        // FXMLLoader loader = new FXMLLoader(getClass()
        //         .getResource("/ListmsgFXML.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        stage.setTitle("Ajouter room ");
        // stage.setTitle("Ajouter message ");
        stage.setScene(scene);

        stage.show();

    }
}
