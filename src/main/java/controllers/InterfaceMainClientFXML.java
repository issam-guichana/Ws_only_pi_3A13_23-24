package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import models.Categorie;
import models.Formation;
import services.ServiceCategorie;
import services.ServiceFormation;
import test.MyListener;
import javafx.geometry.Insets;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class InterfaceMainClientFXML implements Initializable {
    @FXML
    private Button ToFormations;

    @FXML
    private AnchorPane parent;

    @FXML
    private VBox chosenformation;

    @FXML
    private Label nomf;

    @FXML
    private Label prix;

    @FXML
    private ChoiceBox<Categorie> catchoice;

    @FXML
    private Label description;

    @FXML
    private ScrollPane scroll;

    @FXML
    private GridPane grid;

    @FXML
    private TextField search;

    @FXML
    private Button searchbutton;
    @FXML
    private Button buttonpay;

    private MyListener myListener;

    private ServiceFormation formationService = new ServiceFormation();
    private ServiceCategorie categorieDAO = new ServiceCategorie();

    private int startIndex = 0;
    int column = 0;
    int row = 1;
    private Formation formation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Récupérer toutes les catégories depuis la base de données
            List<Categorie> categories = categorieDAO.selectAll();

            // Ajouter les catégories à la ChoiceBox
            ObservableList<Categorie> categorieObservableList = FXCollections.observableArrayList(categories);
            catchoice.setItems(categorieObservableList);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        // Ajouter un écouteur de changement de sélection à la ChoiceBox
        catchoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Filtrer les formations en fonction de la catégorie sélectionnée
            List<Formation> filteredFormations = formationService.getFormationsByCategorie(newValue);

            // Afficher les formations filtrées dans la grille
            displayFormations(filteredFormations);
        });

        // Ajouter un écouteur de changement de texte au champ de recherche
        search.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                // Filtrer les formations en fonction du texte entré par l'utilisateur
                List<Formation> filteredFormations = formationService.searchFormations(newValue);

                // Afficher les formations filtrées dans la grille
                displayFormations(filteredFormations);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });


    }

    private void displayFormations(List<Formation> formations) {
        grid.getChildren().clear();
        row = 1;
        for (Formation formation : formations) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FormationFXML.fxml"));
            try {
                AnchorPane parent = loader.load();
                FormationFXML controller = loader.getController();
                controller.setData(formation, myListener);
                GridPane formationGrid = new GridPane();
                formationGrid.setMinWidth(Region.USE_PREF_SIZE);
                formationGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                formationGrid.setMaxWidth(Double.MAX_VALUE);
                formationGrid.setMinHeight(Region.USE_PREF_SIZE);
                formationGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                formationGrid.setMaxHeight(Double.MAX_VALUE);
                formationGrid.getChildren().add(parent);
                grid.add(formationGrid, 0, row);
                row++;
                formationGrid.getColumnConstraints().add(new ColumnConstraints());
                formationGrid.getRowConstraints().add(new RowConstraints());
                GridPane.setMargin(formationGrid, new Insets(10));
                parent.setOnMouseClicked(event -> {
                    setChosenFormation(formation);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setChosenFormation(Formation formation) {
        Text nomfText = new Text(formation.getNom_form());
        Text prixText = new Text(Integer.toString(formation.getPrix()));
        Text descriptionText = new Text(formation.getDescription());
        nomfText.setStyle("-fx-font-size: 14px; -fx-fill: black; -fx-font-weight: bold;");
        prixText.setStyle("-fx-font-size: 14px; -fx-fill: black;");
        descriptionText.setStyle("-fx-font-size: 14px; -fx-fill: black;");
        TextFlow nomfFlow = new TextFlow(nomfText);
        TextFlow formateurFlow = new TextFlow(prixText);
        TextFlow descriptionFlow = new TextFlow(descriptionText);
        nomfFlow.setPrefWidth(200);
        formateurFlow.setPrefWidth(200);
        descriptionFlow.setPrefWidth(200);
        nomf.setGraphic(nomfFlow);
        prix.setGraphic(formateurFlow);
        description.setGraphic(descriptionFlow);
    }
    @FXML
    private void handlePayButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PaymentFXML.fxml"));
            Parent paymentRoot = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(paymentRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void handleToFormationsButton(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InterfaceTClientFXML.fxml"));
            Parent formationsRoot = loader.load();
            Stage stage = (Stage) parent.getScene().getWindow(); // Obtenez la fenêtre actuelle
            stage.setScene(new Scene(formationsRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
