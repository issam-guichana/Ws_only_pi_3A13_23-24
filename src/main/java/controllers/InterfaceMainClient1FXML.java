package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import models.Formation;
import services.ServiceFormation;
import java.sql.*;
import java.util.List;

public class InterfaceMainClient1FXML {
    @FXML
    private AnchorPane root;

    @FXML
    private ImageView Menu;

    @FXML
    private ImageView MenuBack;

    @FXML
    private AnchorPane slider;

    @FXML
    private Rectangle element1;

    @FXML
    private ComboBox<String> catbox;

    @FXML
    private Label title; // Label pour afficher les formations

    private Connection connection; // Connexion à la base de données

    @FXML
    public void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/testf", "root", "");

            // Charger les catégories depuis la base de données et les ajouter à la ComboBox
            loadCategories();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour charger les catégories depuis la base de données et les ajouter à la ComboBox
    private void loadCategories() throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("SELECT `nom_cat` FROM `categorie`");
        ObservableList<String> categoriesList = FXCollections.observableArrayList();

        while (resultSet.next()) {
            categoriesList.add(resultSet.getString("nom_cat"));
        }
        catbox.setItems(categoriesList);
    }

    // Méthode appelée lorsque la catégorie est sélectionnée dans la ComboBox
    @FXML
    private void handleCategorySelection() {
        String selectedCategory = catbox.getValue(); // Récupérer le nom de la catégorie sélectionnée
        int categoryId = getCategoryID(selectedCategory); // Récupérer l'ID de la catégorie sélectionnée

        // Vérifier si l'ID de la catégorie est valide
        if (categoryId != -1) {
            List<Formation> formations = ServiceFormation.getFormationsByCategory(String.valueOf(categoryId)); // Récupérer les formations de la catégorie sélectionnée

            // Afficher les formations dans la zone de texte ou dans une autre partie de l'interface utilisateur
            StringBuilder formationsText = new StringBuilder();
            for (Formation formation : formations) {
                formationsText.append(formation.getNom_form()).append("\n"); // Ajouter le nom de la formation
            }

            // Mettre à jour le label ou une autre partie de l'interface avec les informations des formations
            title.setText(formationsText.toString());
        }
    }

    // Méthode pour récupérer l'ID de la catégorie sélectionnée à partir de son nom
    private int getCategoryID(String categoryName) {
        int categoryId = -1; // Initialiser l'ID de la catégorie à -1 par défaut
        try {
            // Requête SQL pour récupérer l'ID de la catégorie à partir de son nom
            PreparedStatement statement = connection.prepareStatement("SELECT `id_cat` FROM `categorie` WHERE `nom_cat` = ?");
            statement.setString(1, categoryName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                categoryId = resultSet.getInt("id_cat");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryId;
    }
}
