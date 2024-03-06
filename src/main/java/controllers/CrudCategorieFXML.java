package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import services.ServiceCategorie;
import models.Categorie;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CrudCategorieFXML implements Initializable {

    @FXML
    private AnchorPane parent;

    @FXML
    private Button annulerb;

    @FXML
    private Button ajouterb;

    @FXML
    private TextField idnom;

    @FXML
    private TableColumn<Categorie, String> cattable; // Fixer le type générique de la TableColumn

    @FXML
    private TableView<Categorie> table; // Fixer le type générique de la TableView

    ServiceCategorie cat = new ServiceCategorie();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Charger les catégories existantes depuis la base de données
            List<Categorie> categories = cat.selectAll();

            // Convertir la liste en ObservableList pour la TableView
            ObservableList<Categorie> categorieObservableList = FXCollections.observableArrayList(categories);
            table.setItems(categorieObservableList);

            // Configurer la colonne pour afficher les noms de catégorie
            cattable.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom_cat())); // Utiliser getNom_cat()

        } catch (SQLException ex) {
            Logger.getLogger(CrudCategorieFXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void insertOne(ActionEvent event) throws SQLException {
        String nom_cat = idnom.getText();
        if (nom_cat != null && !nom_cat.isEmpty()) {
            if (!cat.isExistingCategorie(nom_cat)) { // Vérifier si le nom de catégorie est unique
                Categorie c = new Categorie(nom_cat);
                cat.insertOne(c);

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Création de la catégorie");
                alert.setHeaderText("Création de la catégorie");
                alert.setContentText("Catégorie créée!");
                alert.showAndWait();

                idnom.setText("");
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
    private void Cancel(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/CrudFormationFXML.fxml"));
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
}
