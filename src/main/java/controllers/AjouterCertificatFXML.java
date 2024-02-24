package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import models.Badge;
import services.ServiceBadge;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AjouterCertificatFXML {
    @FXML
    private Button btnrefresh;

    @FXML
    private TableColumn<Badge, String> colnom;

    @FXML
    private TableColumn<Badge, String> coltype;

    @FXML
    private Button idajouter;

    @FXML
    private Button idmodifier;

    @FXML
    private Button idsupprimer;

    @FXML
    private TableView<Badge> tabcertif;
    public void displayAllBadgesInTableView() {
        colnom.setCellValueFactory(new PropertyValueFactory<>("nomBadge"));
        coltype.setCellValueFactory(new PropertyValueFactory<>("type"));


        try {
            ServiceBadge serviceBadge = new ServiceBadge();
            List<Badge> badges = serviceBadge.selectAll();
            ObservableList<Badge> ql = FXCollections.observableArrayList(badges);
            tabcertif.setItems(ql);
            System.out.println("affichage... ");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnes : " + e.getMessage());
        }
    }
    @FXML
    void refresh(ActionEvent event) {
        btnrefresh.setOnAction(e->{
            displayAllBadgesInTableView();
        });
    }
    @FXML
    void ajouterBadge(ActionEvent event) {
        idajouter.setOnAction(e->{
            Badge badge = new Badge();
            displayAddDialog(badge);
        });
    }
    private void displayAddDialog(Badge badge){
        // Create the dialog components
        Dialog<Badge> dialog = new Dialog<>();
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
        vbox.getChildren().addAll(new Label("Nom du badge:"), nomField, new Label("Type du badge:"), typeField);
        dialog.getDialogPane().setContent(vbox);

        // Request focus on the nomField by default
        Platform.runLater(nomField::requestFocus);

        // Convert the result to a Badge object when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                return new Badge(nomField.getText(), typeField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Badge> result = dialog.showAndWait();
        result.ifPresent(newBadge -> {
            try {
                ServiceBadge serviceBadge = new ServiceBadge();
                serviceBadge.insertOne(newBadge);
                // Refresh the table view to reflect the changes
                displayAllBadgesInTableView();
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
        TableColumn<Badge, Void> modifierColumn = new TableColumn<>("Modifier");
        modifierColumn.setCellFactory(col -> new TableCell<Badge, Void>() {
            private final Button modifyButton = new Button("Modifier");

            {
                modifyButton.setOnAction(event -> {
                    Badge badge = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Modifier" button is clicked
                    System.out.println("Modify badge: " + badge.getNomBadge());
                    displayModifyDialog(badge); // Call method to display dialog
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
    private void displayModifyDialog(Badge badge) {
        // Create the dialog components
        Dialog<Badge> dialog = new Dialog<>();
        dialog.setTitle("Modifier un Badge");
        dialog.setHeaderText("Modifier les détails du badge");

        // Set the button types
        ButtonType modifyButtonType = new ButtonType("Modifier", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(modifyButtonType, ButtonType.CANCEL);

        // Create the input fields
        TextField nomField = new TextField(badge.getNomBadge());
        nomField.setPromptText("Nom du badge");
        TextField typeField = new TextField(badge.getType());
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
        vbox.getChildren().addAll(new Label("Nom du badge:"), nomField, new Label("Type du badge:"), typeField);
        dialog.getDialogPane().setContent(vbox);

        // Convert the result to a Badge object when the modify button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                return new Badge(nomField.getText(), typeField.getText());
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Badge> result = dialog.showAndWait();
        result.ifPresent(newBadge -> {
            try {
                ServiceBadge serviceBadge = new ServiceBadge();
                newBadge.setIdBadge(badge.getIdBadge()); // Set the ID of the existing badge
                serviceBadge.updateOne(newBadge);
                // Refresh the table view to reflect the changes
                displayAllBadgesInTableView();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception
            }
        });
    }
    private void setupSupprimerButtonColumn() {
        TableColumn<Badge, Void> supprimerColumn = new TableColumn<>("Supprimer");
        supprimerColumn.setCellFactory(col -> new TableCell<Badge, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Badge badge = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Supprimer" button is clicked
                    ServiceBadge sq=new ServiceBadge();
                    System.out.println("Delete Badge " + badge.getNomBadge());
                    try {
                        sq.deleteOne(badge);
                        displayAllBadgesInTableView();
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
        displayAllBadgesInTableView();
        setupButtonColumns();

    }
}

