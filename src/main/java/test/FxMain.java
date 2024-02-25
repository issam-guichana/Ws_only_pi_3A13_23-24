package test;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FxMain extends Application {
    public FxMain() {
    }

    public static void main(String[] args) {
        launch(new String[0]);
    }

    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/InterfaceMainClient1FXML.fxml"));
        Parent root = (Parent)loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("root");
        stage.setScene(scene);
        stage.show();
    }
}