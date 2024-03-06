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


        // Interface user
     //  FXMLLoader loader = new FXMLLoader(getClass()
        //        .getResource("/AjoutermsgFXML.fxml"));


         //interface admin
      // FXMLLoader loader = new FXMLLoader(getClass()
        //       .getResource("/DashbordadminFXML.fxml"));

  //  FXMLLoader loader = new FXMLLoader(getClass()
             //  .getResource("/DashbordmsgFXML.fxml"));



        //interface formateur

    // FXMLLoader loader = new FXMLLoader(getClass()
      //       .getResource("/Ajoutroomformateur.fxml"));
   FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AjoutermsgformateurFXML.fxml"));

                //FXMLLoader loader = new FXMLLoader(getClass()
          //      .getResource("/test.fxml"));

        // FXMLLoader loader = new FXMLLoader(getClass()
        //  .getResource("/AjoutermsgFXML.fxml"));

//        FXMLLoader loader = new FXMLLoader(getClass()
  //             .getResource("/AjoutermsgformateurFXML.fxml"));

        //FXMLLoader loader = new FXMLLoader(getClass()
        //      .getResource("/Listroom.fxml"));

        // FXMLLoader loader = new FXMLLoader(getClass()
        //         .getResource("/ListmsgFXML.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        stage.setTitle("Formini.TN ");
        // stage.setTitle("Ajouter message ");
        stage.setScene(scene);

        stage.show();

    }
}
