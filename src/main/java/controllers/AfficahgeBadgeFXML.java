package controllers;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import models.Badge;
import services.ServiceBadge;

import java.sql.SQLException;
import java.util.List;

public class AfficahgeBadgeFXML {

    @FXML
    private HBox idCertifContainer;

    @FXML
    private AnchorPane parent;

    private ServiceBadge badgeService = new ServiceBadge();
    private List<Badge> allBadges;

    private int pageSize = 10; // Number of badges per page

    public void displayBadgesForPage(int page) {
        int startIndex = (page - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, allBadges.size());

        idCertifContainer.getChildren().clear(); // Clear previous badge items

        GridPane badgesGrid = new GridPane();
        badgesGrid.setHgap(10); // Horizontal gap between columns
        badgesGrid.setVgap(10); // Vertical gap between rows

        int col = 0;
        int row = 0;

        for (int i = startIndex; i < endIndex; i++) {
            Badge badge = allBadges.get(i);

            // Create a VBox to hold the components of each badge
            VBox badgeBox = new VBox();
            badgeBox.setSpacing(10); // Vertical spacing between components
            badgeBox.setAlignment(Pos.CENTER); // Align components to the center horizontally
            badgeBox.setStyle("-fx-background-color: #E68C3AFF;-fx-background-radius: 10px; -fx-padding: 10px;"); // Set background color
            badgeBox.setEffect(new DropShadow());

            // Create a label to display the badge name
            Label badgeNameLabel = new Label(badge.getNomBadge());
            badgeNameLabel.setStyle("-fx-text-fill: white");

            // Create a label to display the badge type
            Label badgeTypeLabel = new Label(badge.getType());
            badgeTypeLabel.setStyle("-fx-text-fill: white");

            Button button = new Button();
            Text buttonText = new Text("View Details");
            buttonText.setFont(Font.font("Arial", FontWeight.BOLD, FontPosture.REGULAR, 12));
            buttonText.setFill(Color.WHITE);
            buttonText.setUnderline(true);
            button.setGraphic(buttonText);
            button.setStyle("-fx-background-color: transparent; -fx-border-color: rgba(38,38,38,0.64);-fx-border-radius: 10px;-fx-background-radius: 10px");
            // Add event handler to the button to handle viewing badge details

            // Add nodes to the VBox in the desired order
            badgeBox.getChildren().addAll(badgeNameLabel, badgeTypeLabel, button);

            // Add the VBox to the grid at the specified column and row
            badgesGrid.add(badgeBox, col, row);

            // Increment column index and reset to 0 if it reaches 5
            col++;
            if (col == 5) {
                col = 0;
                row++;
            }
        }

        // Add the grid to the idCertifContainer
        idCertifContainer.getChildren().add(badgesGrid);

        // Log message for debugging
        System.out.println("Displayed badges: " + startIndex + " to " + (endIndex - 1));
    }

    // This method can be called to initialize the badges and display the first page
    public void initialize() {
        try {
            allBadges = badgeService.selectAll();
            displayBadgesForPage(1); // Display the first page of badges initially
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle the exception as needed
        }
    }
}

