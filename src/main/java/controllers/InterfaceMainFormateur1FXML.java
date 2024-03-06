package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Categorie;
import services.ServiceCategorie;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterfaceMainFormateur1FXML {

    @FXML
    private Button btnOverview;
    @FXML
    private Button btnajouter111;

    @FXML
    private TextField nomcat;

    @FXML
    private Button btnajouter11;

    private final ServiceCategorie cat = new ServiceCategorie(); // Déplacer l'initialisation ici

    @FXML
    private void insertOne(ActionEvent event) throws SQLException {
        String nom_cat = nomcat.getText();
        if (nom_cat != null && !nom_cat.isEmpty()) {
            if (!cat.isExistingCategorie(nom_cat)) { // Vérifier si le nom de catégorie est unique
                Categorie c = new Categorie(nom_cat);
                cat.insertOne(c);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Création de la catégorie");
                alert.setHeaderText("Création de la catégorie");
                alert.setContentText("Catégorie créée!");
                alert.showAndWait();

                nomcat.setText("");
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur de création de catégorie");
                alert.setHeaderText(null);
                alert.setContentText("Le nom de la catégorie existe déjà!");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Le nom de la catégorie ne peut pas être vide.");
            alert.setHeaderText("Erreur");
            alert.setContentText("Catégorie n'est pas créée!");
            alert.showAndWait();
        }
    }

    @FXML
    private void cancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMainFormateurFXML.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }

    @FXML
    void formationBack(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrudFormationFXML.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.setFullScreen(true);
        stage.show();
    }
    @FXML
    void handleButtonFormations(ActionEvent event) throws IOException {
        // Load the FXML file for the formations interface
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceTFormateurFXML.fxml"));
        Parent formationsParent = loader.load();

        // Get the current stage and set its scene to the formations interface scene
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(formationsParent);
        stage.setScene(scene);
        stage.show();
    }

}
