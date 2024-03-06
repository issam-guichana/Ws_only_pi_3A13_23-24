package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Categorie;
import models.Formation;
import services.ServiceCategorie;
import services.ServiceFormation;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class InterfaceMainFormateurFXML implements Initializable {
    @FXML
    private Button ButtonFormations;
    @FXML
    private Button btnOverview;


    @FXML
    private TextField nameform;

    @FXML
    private TextField descform;

    @FXML
    private ChoiceBox<Categorie> catform;

    @FXML
    private Button btnajouter11;

    @FXML
    private Button btncat11;

    private final ServiceFormation formationDAO = new ServiceFormation();
    private final ServiceCategorie categorieDAO = new ServiceCategorie();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            List<Categorie> nomCat = categorieDAO.selectAll();
            ObservableList<Categorie> categorieList = FXCollections.observableArrayList(nomCat);
            catform.setItems(categorieList);
        } catch (SQLException ex) {
            Logger.getLogger(InterfaceMainFormateurFXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void AjouterCategorie(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceMainFormateur1FXML.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void insertOne(ActionEvent event) {
        if (isInputValid()) {
            String nom_form = nameform.getText();
            int user_id = 1; // Default user ID
            String description = descform.getText();
            Categorie categorie = catform.getValue();
            if (categorie != null) {
                Formation formation = new Formation(nom_form, description, user_id, categorie);
                try {
                    formationDAO.insertOne(formation);
                    clearFields();
                    showAlert("Formation ajoutée avec succès!");
                } catch (SQLException ex) {
                    Logger.getLogger(InterfaceMainFormateurFXML.class.getName()).log(Level.SEVERE, null, ex);
                    showAlert("Erreur lors de l'ajout de la formation.");
                }
            } else {
                showAlert("Veuillez sélectionner une catégorie.");
            }
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nameform.getText() == null || nameform.getText().isEmpty()) {
            errorMessage += "Nom de formation invalide!\n";
        } else {
            try {
                List<Formation> formations = formationDAO.selectAll();
                String nomFormation = nameform.getText().trim();
                for (Formation formation : formations) {
                    if (formation.getNom_form().equalsIgnoreCase(nomFormation)) {
                        errorMessage += "Le nom de formation existe déjà!\n";
                        break;
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(InterfaceMainFormateurFXML.class.getName()).log(Level.SEVERE, null, ex);
                errorMessage += "Erreur lors de la vérification du nom de formation.\n";
            }
        }
        if (descform.getText() == null || descform.getText().isEmpty()) {
            errorMessage += "Description invalide!\n";
        }
        if (catform.getValue() == null) {
            errorMessage += "Veuillez sélectionner une catégorie!\n";
        }
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(errorMessage);
            return false;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearFields() {
        nameform.clear();
        descform.clear();
        catform.getSelectionModel().clearSelection();
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
