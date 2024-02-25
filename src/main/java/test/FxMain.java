package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxMain extends Application {

    public static void main(String[] args) {launch();}

    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass()
               .getResource("/LoginUser.fxml"));
                //.getResource("/UpdateProfile.fxml"));
                //.getResource("/AdminInterface.fxml"));
                 //.getResource("/azizdashboard.fxml"));
                ///.getResource("/UserSettings.fxml"));

        Parent parent = loader.load();

        Scene scene = new Scene(parent);

        stage.setTitle("Ajouter User");
        stage.setScene(scene);

        stage.show();
    }
}
