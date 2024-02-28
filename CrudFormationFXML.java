package controllers;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Categorie;
import models.Formation;
import org.w3c.dom.Text;
import services.ServiceCategorie;
import services.ServiceFormation;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

public class CrudFormationFXML implements Initializable {



    @FXML
    private AnchorPane parent;

    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSettings1;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlMenus;

    @FXML
    private Pane pnlOverview;

    @FXML
    private TableView<Formation> table;

    @FXML
    private TableColumn<?, ?> NomCol;

    @FXML
    private TableColumn<?, ?> DESCRIPTIONcol;

    @FXML
    private TableColumn<?, ?> FormateurCol;

    @FXML
    private TableColumn<?, ?> CATCol;

    @FXML
    private TextField nomfx;

    @FXML
    private TextField descriptionfx;

    @FXML
    private TextField formateurfx;

    @FXML
    private Button btnsupprimer;



    @FXML
    private ChoiceBox<Categorie> fxCategorieChoiceBox;

    @FXML
    private Button btncat11;

    @FXML
    private Button btnajouter11;

    @FXML
    private Button btnmodifier;


    ServiceFormation formationDAO = new ServiceFormation();
    ServiceCategorie categorieDAO = new ServiceCategorie();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            List<Categorie> nomCat = new ServiceCategorie().selectAll();

            // Add category names to the fxCategorieChoiceBox
            for (Categorie categorie : nomCat) {
                fxCategorieChoiceBox.getItems().add(new Categorie(categorie.getNom_cat()));
            }

            ObservableList<Formation> formations = FXCollections.observableArrayList(formationDAO.selectAll());

            NomCol.setCellValueFactory(new PropertyValueFactory<>("nom_form"));
            DESCRIPTIONcol.setCellValueFactory(new PropertyValueFactory<>("description"));
            FormateurCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
            CATCol.setCellValueFactory(new PropertyValueFactory<>("categorie"));

            table.setItems(formations);
            //List<Integer> formateursIDs = formationDAO.getAllFormateursIDs();
            //TextFields.bindAutoCompletion(formateurfx, formateursIDs);
        } catch (SQLException ex) {
            Logger.getLogger(CrudFormationFXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void insertOne(ActionEvent event) {
        if (isInputValid()) {
            String nom_form = nomfx.getText();
            String description = descriptionfx.getText();
            int user_id = Integer.parseInt(formateurfx.getText());
            Categorie categorie = fxCategorieChoiceBox.getValue();

            Formation formation = new Formation(nom_form, description, user_id, categorie);
            try {
                formationDAO.insertOne(formation);
                table.setItems(FXCollections.observableArrayList(formationDAO.selectAll()).sorted());
                clearFields();
                showAlert("Formation ajoutée avec succès!");
            } catch (SQLException ex) {
                Logger.getLogger(CrudFormationFXML.class.getName()).log(Level.SEVERE, null, ex);
                showAlert("Erreur lors de l'ajout de la formation.");
            }
        }
    }

    @FXML
    private void updateOne(ActionEvent event) {
        Formation formation = table.getSelectionModel().getSelectedItem();

        if (formation != null) {
            String nom_form = nomfx.getText();
            String description = descriptionfx.getText();
            String user_id_str = formateurfx.getText(); // Get the user ID as a string

            if (!user_id_str.isEmpty()) { // Check if the string is not empty
                int user_id = Integer.parseInt(user_id_str); // Parse the string to an integer
                Categorie categorie = fxCategorieChoiceBox.getValue();

                formation.setNom_form(nom_form);
                formation.setDescription(description);
                formation.setUser_id(user_id);
                formation.setCategorie(categorie);

                try {
                    formationDAO.updateOne(formation);
                    table.setItems(FXCollections.observableArrayList(formationDAO.selectAll()));
                    clearFields();
                    showAlert("Formation modifiée avec succès!");
                } catch (SQLException ex) {
                    Logger.getLogger(CrudFormationFXML.class.getName()).log(Level.SEVERE, null, ex);
                    showAlert("Erreur lors de la modification de la formation.");
                }
            } else {
                showAlert("Formateur invalide!");
            }
        } else {
            showAlert("Veuillez sélectionner une formation à modifier.");
        }
    }


    @FXML
    private void deleteOne(ActionEvent event) {
        Formation formation = table.getSelectionModel().getSelectedItem();
        if (formation != null) {
            try {
                formationDAO.deleteOne(formation);
                table.setItems(FXCollections.observableArrayList(formationDAO.selectAll()));
                showAlert("Formation supprimée avec succès!");
            } catch (SQLException ex) {
                Logger.getLogger(CrudFormationFXML.class.getName()).log(Level.SEVERE, null, ex);
                showAlert("Erreur lors de la suppression de la formation.");
            }
        } else {
            showAlert("Veuillez sélectionner une formation à supprimer.");
        }
    }

    @FXML
    private void AjouterCategorie(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/CrudCategorieFXML.fxml"));
        Parent parent = loader.load();
        Scene scene = new Scene(parent);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (nomfx.getText() == null || nomfx.getText().isEmpty()) {
            errorMessage += "Nom de formation invalide!\n";
        }
        if (descriptionfx.getText() == null || descriptionfx.getText().isEmpty()) {
            errorMessage += "Description invalide!\n";
        }
        if (formateurfx.getText() == null || formateurfx.getText().isEmpty()) {
            errorMessage += "Formateur invalide!\n";
        }
        if (fxCategorieChoiceBox.getValue() == null) {
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
        nomfx.clear();
        descriptionfx.clear();
        formateurfx.clear();
        fxCategorieChoiceBox.getSelectionModel().clearSelection();
    }



    @FXML
    public void gotData(javafx.scene.input.MouseEvent mouseEvent) {
        Formation formation = (Formation) table.getSelectionModel().getSelectedItem();
        if (formation != null) {

            nomfx.setText(formation.getNom_form());
            descriptionfx.setText(formation.getDescription());
            formateurfx.setText(String.valueOf(formation.getUser_id()));
            fxCategorieChoiceBox.setValue(formation.getCategorie());

        }
    }

//////////////////////////

}
