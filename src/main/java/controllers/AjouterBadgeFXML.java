package controllers;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Badge;
import services.ServiceBadge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AjouterBadgeFXML {
    @FXML
    private Button idbtncertif;


    @FXML
    private TableColumn<Badge, String> colnom;

    @FXML
    private TableColumn<Badge, String> coltype;

    @FXML
    private TableColumn <Badge, String> colimg;

    @FXML
    private Button idajouter;

    @FXML
    private Label lblTotalBadges;


    @FXML
    private Label lblMostAttributedBadge;

    @FXML
    private TableView<Badge> tabbadge;



    public void displayAllBadgesInTableView() {
        colnom.setCellValueFactory(new PropertyValueFactory<>("nomBadge"));
        coltype.setCellValueFactory(new PropertyValueFactory<>("type"));
        colimg.setCellValueFactory(new PropertyValueFactory<>("imgBadge"));


        try {
            ServiceBadge serviceBadge = new ServiceBadge();
            List<Badge> badges = serviceBadge.selectAll();
            ObservableList<Badge> ql = FXCollections.observableArrayList(badges);
            tabbadge.setItems(ql);
            System.out.println("affichage... ");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des personnes : " + e.getMessage());
        }
    }

    @FXML
    void ajouterBadge(ActionEvent event) {
        idajouter.setOnAction(e -> {
            Badge badge = new Badge();
            displayAddDialog(badge);
        });
    }
    private void displayAddDialog(Badge badge) {
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

        TextField imageField = new TextField();
        imageField.setPromptText("Nom de l'image");

        // Image selection button
        Button selectImageButton = new Button("Sélectionner une image");
        selectImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner une image");
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                // Afficher le nom du fichier dans le champ de texte
                imageField.setText(selectedFile.getName());
            }
        });

        // Enable/disable the add button depending on whether fields are filled
        Node addButtonNode = dialog.getDialogPane().lookupButton(addButton);
        addButtonNode.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax)
        nomField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButtonNode.setDisable(newValue.trim().isEmpty() || typeField.getText().trim().isEmpty() || isDuplicate(newValue.trim(), typeField.getText()));
        });

        typeField.textProperty().addListener((observable, oldValue, newValue) -> {
            addButtonNode.setDisable(newValue.trim().isEmpty() || nomField.getText().trim().isEmpty() || isDuplicate(nomField.getText(), newValue.trim()));
        });

        // Layout for the dialog
        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(
                new Label("Nom du badge:"),
                nomField,
                new Label("Type du badge:"),
                typeField,
                new Label("Nom de l'image:"),
                imageField,
                selectImageButton
        );
        dialog.getDialogPane().setContent(vbox);

        // Request focus on the nomField by default
        Platform.runLater(nomField::requestFocus);

        // Convert the result to a Badge object when the add button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButton) {
                // Get the selected image path
                String imagePath = null; // Ne pas utiliser l'image, uniquement le nom
                if (!imageField.getText().isEmpty()) {
                    imagePath = imageField.getText();
                }

                // Return the Badge object with image path
                return new Badge(nomField.getText(), typeField.getText(), imagePath);
            }
            return null;
        });

        // Show the dialog and wait for the user's response
        Optional<Badge> result = dialog.showAndWait();
        result.ifPresent(newBadge -> {
            try {
                // Mettre à jour le champ colimg de Badge avec le nom de l'image
                newBadge.setImgBadge(imageField.getText());

                ServiceBadge serviceBadge = new ServiceBadge();
                serviceBadge.insertOne(newBadge);
                // Refresh the table view to reflect the changes
                displayAllBadgesInTableView();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception
            }
        });
    }


    //Method to check for duplicates
    private boolean isDuplicate(String nom, String type) {
        try {
            ServiceBadge serviceBadge = new ServiceBadge();
            List<Badge> existingBadges = serviceBadge.selectAll() ;// Assuming this method exists to get all badges

            for (Badge badge : existingBadges) {
                if (badge.getNomBadge().equalsIgnoreCase(nom) || badge.getType().equalsIgnoreCase(type)) {
                    // Display a warning dialog
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Erreur");
                    alert.setHeaderText("Badge déjà existant");
                    alert.setContentText("Le nom ou le type du badge existe déjà. Veuillez entrer un nom ou un type différent.");
                    alert.showAndWait();
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception
        }
        return false;
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
        tabbadge.getColumns().add(modifierColumn);
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
        TextField imageField = new TextField(badge.getImgBadge());
        imageField.setPromptText("Chemin de l'image");

        // Image selection button
        Button selectImageButton = new Button("Sélectionner une nouvelle image");
        selectImageButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Sélectionner une nouvelle image");
            File selectedFile = fileChooser.showOpenDialog(dialog.getOwner());
            if (selectedFile != null) {
                // Afficher le chemin du fichier dans le champ de texte
                imageField.setText(selectedFile.getPath());
            }
        });

        // Enable/disable the modify button depending on whether fields are filled
        Node modifyButtonNode = dialog.getDialogPane().lookupButton(modifyButtonType);
        modifyButtonNode.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax)
        nomField.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButtonNode.setDisable(newValue.trim().isEmpty() || typeField.getText().trim().isEmpty() || imageField.getText().trim().isEmpty());
        });

        typeField.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButtonNode.setDisable(newValue.trim().isEmpty() || nomField.getText().trim().isEmpty() || imageField.getText().trim().isEmpty());
        });

        imageField.textProperty().addListener((observable, oldValue, newValue) -> {
            modifyButtonNode.setDisable(newValue.trim().isEmpty() || nomField.getText().trim().isEmpty() || typeField.getText().trim().isEmpty());
        });

        // Layout for the dialog
        VBox vbox = new VBox(20);
        vbox.getChildren().addAll(
                new Label("Nom du badge:"), nomField,
                new Label("Type du badge:"), typeField,
                new Label("Chemin de l'image:"), imageField,
                selectImageButton
        );
        dialog.getDialogPane().setContent(vbox);

        // Convert the result to a Badge object when the modify button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == modifyButtonType) {
                return new Badge(nomField.getText(), typeField.getText(), imageField.getText());
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
                    ServiceBadge sq = new ServiceBadge();
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
        tabbadge.getColumns().add(supprimerColumn);
    }

    @FXML
    public void initialize() {
        displayAllBadgesInTableView();
        setupButtonColumns();

        try {
            // Calculate total badges
            ServiceBadge serviceBadge = new ServiceBadge();
            int totalBadges = serviceBadge.countTotalBadges();

            // Set the total badges count to the label
            lblTotalBadges.setText(String.valueOf(totalBadges));
            String mostAttributedBadge = serviceBadge.findMostAttributedBadge();
            lblMostAttributedBadge.setText(mostAttributedBadge);
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul du nombre total de badges : " + e.getMessage());
        }
    }


    @FXML
    void accederCertif(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass()
                    .getResource("/AjouterCertificatXML.fxml"));
            Parent root = loader.load();
            AjouterCertificateFXML lc = loader.getController();
            idbtncertif.getScene().setRoot(root);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

