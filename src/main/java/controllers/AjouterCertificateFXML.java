package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;

import javafx.scene.layout.VBox;

import models.Certificat;


import services.ServiceCertificat;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AjouterCertificateFXML {


    @FXML
    private TableColumn<?, ?> coldate;

    @FXML
    private TableColumn<?, ?> colnomc;

    @FXML
    private ImageView idajouter;


    @FXML
    private TableView<Certificat> tabcertif;

    @FXML
    void handleClicks(ActionEvent event) {

    }
    public void displayAllCertificatesInTableView() {
        colnomc.setCellValueFactory(new PropertyValueFactory<>("nomCertif"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("dateCertif"));


        try {
            ServiceCertificat serviceCertificat = new ServiceCertificat();
            List<Certificat> certificats = ServiceCertificat.selectAll();
            ObservableList<Certificat> ql = FXCollections.observableArrayList(certificats);
            tabcertif.setItems(ql);
            System.out.println("affichage... ");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des Certificats : " + e.getMessage());
        }
    }
    @FXML
    /*void refresh(ActionEvent event) {
        btnrefresh.setOnAction(e->{
            //displayAllBadgesInTableView();
        });
   }
    @FXML
    /*void ajouterBadge(ActionEvent event) {
        idajouter.setOnAction(e->{
            Badge badge = new Badge();
            displayAddDialog(badge);
        });
    }
    */

    private void displayAddDialog(Certificat certificat){
        // Create the dialog components
        Dialog<Certificat> dialog = new Dialog<>();
        dialog.setTitle("Ajouter un nouveau Badge");
        dialog.setHeaderText("Entrez les détails du badge");

        // Set the button types
        ButtonType addButton = new ButtonType("Ajouter", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButton, ButtonType.CANCEL);

        // Create the input fields
        TextField nomField = new TextField();
        nomField.setPromptText("Nom du badge");
        TextField typeField = new TextField();
        typeField.setPromptText("Type du badge");

        // Enable/disable the add button depending on whether fields are filled
        Node addButtonNode = dialog.getDialogPane().lookupButton(addButton);
        addButtonNode.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax)
        nomField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButtonNode.setDisable(newValue.trim().isEmpty() || typeField.getText().trim().isEmpty());
        });

        typeField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButtonNode.setDisable(newValue.trim().isEmpty() || nomField.getText().trim().isEmpty());
        });

        // Layout for the dialog
        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(new Label("Nom du Certificat:"), nomField, new Label("Date du certificat:"), typeField);
        dialog.getDialogPane().setContent(vbox);

        // Request focus on the nomField by default
        Platform.runLater(nomField::requestFocus);

        // Convert the result to a Certificat object when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Certificat(nomField.getText(), typeField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Certificat> result = dialog.showAndWait();
        result.ifPresent(newCertificat -> {
            try {
                ServiceCertificat serviceBadge = new ServiceCertificat();
                serviceBadge.insertOne(newCertificat);
                // Refresh the table view to reflect the changes
                displayAllCertificatesInTableView();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception
            }
        });

    }
    private void setupButtonColumns() {
        setupModifierButtonColumn();
        setupSupprimerButtonColumn();
    }
    private void setupModifierButtonColumn() {
        TableColumn<Certificat, Void> modifierColumn = new TableColumn<>("Modifier");
        modifierColumn.setCellFactory(col -> new TableCell<Certificat, Void>() {
            private final Button modifyButton = new Button("Modifier");

            {
                modifyButton.setOnAction(event -> {
                    Certificat certificat = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Modifier" button is clicked
                    System.out.println("Modify badge: " + certificat.getNomCertif());
                    displayModifyDialog(certificat); // Call method to display dialog
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : modifyButton);
            }
        });
        tabcertif.getColumns().add(modifierColumn);
    }
    private void displayModifyDialog(Certificat certificat) {
        // Create the dialog components
        Dialog<Certificat> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Badge");
        dialog.setHeaderText("Modifier les détails du badge");

        // Set the button types
        ButtonType modifyButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        // Create the input fields
        TextField nomField = new TextField(certificat.getNomCertif());
        nomField.setPromptText("Nom du badge");
        TextField typeField = new TextField(certificat.getDateCertif());
        typeField.setPromptText("Type du badge");

        // Enable/disable the modify button depending on whether fields are filled
        Node modifyButtonNode = dialog.getDialogPane().lookupButton(modifyButtonType);
        modifyButtonNode.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax)
        nomField.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButtonNode.setDisable(newValue.trim().isEmpty() || typeField.getText().trim().isEmpty());
        });

        typeField.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButtonNode.setDisable(newValue.trim().isEmpty() || nomField.getText().trim().isEmpty());
        });

        // Layout for the dialog
        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(new Label("Nom du Certificat:"), nomField, new Label("Type du Certificat:"), typeField);
        dialog.getDialogPane().setContent(vbox);

        // Convert the result to a Badge object when the modify button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                return new Certificat(nomField.getText(), typeField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Certificat> result = dialog.showAndWait();
        result.ifPresent(newBadge -> {
            try {
                ServiceCertificat serviceCertificat = new ServiceCertificat();
                newCertificat.setId(certificat.getId()); // Set the ID of the existing badge
                serviceCertificat.updateOne(new Certificat());
                // Refresh the table view to reflect the changes
                displayAllCertificatesInTableView();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception
            }
        });
    }
    private void setupSupprimerButtonColumn() {
        TableColumn<Certificat, Void> supprimerColumn = new TableColumn<>("Supprimer");
        supprimerColumn.setCellFactory(col -> new TableCell<Certificat, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Certificat certificat = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Supprimer" button is clicked
                    ServiceCertificat sq=new ServiceCertificat();
                    System.out.println("Delete Certificat " + certificat.getNomCertif());
                    try {
                        sq.deleteOne(certificat);
                        displayAllCertificatesInTableView();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : deleteButton);
            }
        });
        tabcertif.getColumns().add(supprimerColumn);
    }
    @FXML
    public void initialize() {
        displayAllCertificatesInTableView();
        setupButtonColumns();

    }
}