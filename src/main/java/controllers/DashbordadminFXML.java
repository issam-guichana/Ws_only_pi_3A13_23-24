package controllers;


import java.net.URL;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;


import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.util.Pair;
import models.Room;
import models.user_formation;
import services.Serviceroom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class DashbordadminFXML implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;


    @FXML
    private TableColumn<ObservableList<String>,String> statusroom;
    @FXML
    private TableColumn<ObservableList<String>, Void> button_room;
    @FXML
    private TableColumn<ObservableList<String>,String>  date_c_room;
    @FXML
    private TableColumn<ObservableList<String>,String> descroom;
    @FXML
    private TableColumn<ObservableList<String>,String> idroom;

    @FXML
    private TableColumn<ObservableList<String>,String> formateur;

    @FXML
    private TableColumn<ObservableList<String>,String> nmform;

    @FXML
    private TableColumn<ObservableList<String>,String>nmroom;
    @FXML
    private TableView<ObservableList<String>> tabr;

    @FXML
    private TextField  decsproom;

    @FXML
    private TextField nameroom;
    private boolean suspendColumnAdded = false;

    @FXML
    private ComboBox<String> nomform;
    @FXML
    private Pane modifyRoomPane;
    private Label nomRoomField;
    private Label descriptionField;


    private static final int SUSPENSION_TIME_COLUMN_INDEX = 4;


    private TableColumn<ObservableList<String>, Void> setupDeleteButtonColumn() {
        TableColumn<ObservableList<String>, Void> supprimerColumn = new TableColumn<>("Supprimer");
        supprimerColumn.setCellFactory(col -> new TableCell<>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    int index = getIndex();
                    // Get the inner ObservableList<String> corresponding to the selected row
                    ObservableList<String> row = getTableView().getItems().get(index);
                    // Retrieve the id_room from the inner list
                    String idRoomString = row.get(0);
                    System.out.println(idRoomString);
                    int idRoom = Integer.parseInt(idRoomString);
                    // Action to perform when the "Supprimer" button is clicked
                    Serviceroom sq = new Serviceroom();
                    try{
//int idRoom = Integer.parseInt(rowData.get(0));
                        sq.DeleteOne(idRoom);
                        dispalyallrooms();
                        // Refresh the table view after deletion if needed
                        //displayAllRoomsInTableView(); // Define this method to update the table view
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
        return supprimerColumn;
    }

    @FXML
    void initialize() {
        assert descroom != null : "fx:id=\"descroom\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";
        assert formateur != null : "fx:id=\"formateur\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";
        assert nmform != null : "fx:id=\"nmform\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";
        assert nmroom != null : "fx:id=\"nmroom\" was not injected: check your FXML file 'DashbordadminFXML.fxml'.";

    }
    @FXML
    void ajoutroom(ActionEvent event) throws SQLException {
        try {
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "")) {
                String selectedNomForm = nomform.getValue();

                // Prepare a statement to retrieve the id_form corresponding to the selected nom_form
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_form` FROM `formation` WHERE `nom_form`=?");
                preparedStatement.setString(1, selectedNomForm);

                // Execute the query
                ResultSet resultSet = preparedStatement.executeQuery();

                // Check if a result is found
                if (resultSet.next()) {
                    int idForm = resultSet.getInt("id_form");

                    // Check if room name, description are not null or empty
                    if (nameroom.getText() != null && !nameroom.getText().isEmpty() && decsproom.getText() != null) {
                        // Check if the room name already exists for the selected formation_id
                        PreparedStatement checkStatement = connection.prepareStatement("SELECT COUNT(*) AS count FROM room WHERE nom_room=? and status='Active'");
                        checkStatement.setString(1, nameroom.getText());
                        ResultSet checkResult = checkStatement.executeQuery();

                        if (checkResult.next()) {
                            int count = checkResult.getInt("count");
                            if (count == 0) {

                                // Create the Room object with the retrieved id_form
                                Room r = new Room(nameroom.getText(),decsproom.getText());
                                user_formation uf= new user_formation(idForm,r.getId_room());



                                // Call the service to insert the room
                                Serviceroom sp = new Serviceroom();
                                sp.InsertOne(r,uf);
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);

                                alert.setTitle("Espace crée !");
                                alert.setContentText("ADMIN, Vous pouvez maintenant communiquersur cette espace");
                                alert.show();
                                dispalyallrooms(); // Assuming this method updates the display of all rooms
                            } else {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Erreur de saisie");
                                alert.setContentText("ADMIN, Le nom de la salle existe déjà pour cette formation.");
                                alert.show();
                            }
                        }
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Erreur de saisie");
                        alert.setContentText("ADMIN, Le nom de la salle  ne peuvent pas être vides.");
                        alert.show();
                        System.out.println("Error: Le nom de la salle et la description ne peuvent pas être vides.");
                        // Display error message to the user
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Erreur de saisie");
                    alert.setContentText("ADMIN, veuillez choisir une formation appartenant à un esace");
                    alert.show();
                    // Handle case where no result is found for the selected nom_form
                    System.out.println("No id_form found for the selected nom_form: " + selectedNomForm);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle SQL exception
            }
        } finally {
            // Add any cleanup code if needed
        }
    }


    // Metier : suspendre un room pour une durée


            private TableColumn<ObservableList<String>, Void> setupsuspendButtonColumn() {
                if (tabr.getColumns().stream().noneMatch(column -> column.getText().equals("Suspendre"))) {
                    TableColumn<ObservableList<String>, Void> suspendColumn = new TableColumn<>("Suspendre");
                    TableColumn<ObservableList<String>, Void> durationColumn = new TableColumn<>("Durée restante");

                    // Set cell factory for suspend column
                    suspendColumn.setCellFactory(col -> new TableCell<ObservableList<String>, Void>() {
                        private final Button suspendButton = new Button("Suspendre");


                        {
                            // Action when the suspend button is clicked
                            suspendButton.setOnAction(event -> {
                                int index = getIndex();
                                if (index >= 0 && index < tabr.getItems().size()) {
                                    ObservableList<String> rowData = tabr.getItems().get(index);
                                    String idRoom = rowData.get(0);
                                    String nomRoom = rowData.get(1); // Assuming nom_room is at index 1
                                    // Display suspension dialog
                                    displaysuspendDialog(idRoom, nomRoom);
                                }
                            });
                            suspendButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");

                        }

                        @Override
                        protected void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                setGraphic(suspendButton);
                            }
                        }
                    });

                    // Set cell factory for duration column
                    durationColumn.setCellFactory(col -> new TableCell<ObservableList<String>, Void>() {
                        private final Label countdownLabel = new Label();

                        @Override
                        protected void updateItem(Void item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty) {
                                setGraphic(null);
                            } else {
                                ObservableList<String> rowData = getTableRow().getItem();
                                String suspensionEndTime = rowData.get(SUSPENSION_TIME_COLUMN_INDEX); // Assuming the column index of suspension end time
                                if (suspensionEndTime != null && !suspensionEndTime.isEmpty()) {
                                    long remainingTime = calculateRemainingTime(suspensionEndTime);
                                    countdownLabel.setText(formatRemainingTime(remainingTime));
                                    setGraphic(countdownLabel);
                                } else {
                                    setGraphic(null);
                                }
                            }
                        }
                    });

                    return suspendColumn;
                } else {
                    return null; // Return null if the column has already been added
                }
            }



            private long calculateRemainingTime(String suspensionTime)  {
        // Define the suspension duration in milliseconds (e.g., 1 hour)
        long suspensionDurationMillis = TimeUnit.HOURS.toMillis(1);

        // Get the current time
        long currentTimeMillis = System.currentTimeMillis();

        // Calculate the end time of suspension by adding the suspension duration to the current time
        long suspensionEndTimeMillis = currentTimeMillis + suspensionDurationMillis;

        // Calculate the remaining time by subtracting the current time from the suspension end time
        long remainingTimeMillis = suspensionEndTimeMillis - currentTimeMillis;

        // Ensure the remaining time is non-negative
        return Math.max(remainingTimeMillis, 0);
    }


    private String formatRemainingTime(long remainingTimeMillis) {
        long hours = TimeUnit.MILLISECONDS.toHours(remainingTimeMillis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remainingTimeMillis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(remainingTimeMillis) % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void displaysuspendDialog(String idRoom, String nomRoom) {
        // Create the custom dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Choisir la durée de suspension");

        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the grid layout for the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add labels and combo box for suspension duration
        ComboBox<String> durationComboBox = new ComboBox<>();
        durationComboBox.getItems().addAll("2 minutes","30 minutes", "1 heure", "2 heures", "4 heures"); // Add your duration options here
        grid.add(new Label("Durée de suspension: "), 0, 0);
        grid.add(durationComboBox, 1, 0);

        // Set the grid as the dialog content
        dialog.getDialogPane().setContent(grid);

        // Convert the result to a string when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return durationComboBox.getValue();
            }
            return null;
        });

        // Show the dialog and wait for user input
        Optional<String> result = dialog.showAndWait();

        // Process the result when the dialog is closed
        result.ifPresent(duration -> {
            // Suspend the room
            try {
                suspendRoom(idRoom, duration);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Display confirmation to the user
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Room Suspended");
            alert.setHeaderText(null);
            alert.setContentText("La discussion a été suspendue avec succès pour " + duration);
            alert.showAndWait();
        });
    }

    private void suspendRoom(String idRoom, String duration) throws SQLException {
        // Convert duration string to minutes
        int durationInMinutes;
        switch (duration) {
            case "2 minutes":
                durationInMinutes = 2;
                break;
            case "30 minutes":
                durationInMinutes = 30;
                break;
            case "1 heure":
                durationInMinutes = 60;
                break;
            case "2 heures":
                durationInMinutes = 120;
                break;
            case "4 heures":
                durationInMinutes = 240;
                break;
            default:
                durationInMinutes = 0;
        }

        // Call your service method to suspend the room with the calculated duration
        Serviceroom sr = new Serviceroom();
        int roomId = Integer.parseInt(idRoom);

        sr.suspendroom(roomId, durationInMinutes);
        dispalyallrooms();

        // Schedule task to resuspend the room after the specified duration
        //scheduleResuspendTask(roomId, durationInMinutes);
    }

    private void checkSuspensionStatus() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                releaseExpiredSuspensions();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES); // Check every minute
    }

    // Method to release expired suspensions
    private void releaseExpiredSuspensions() throws SQLException {
        long currentTimeMillis = System.currentTimeMillis();

        // Retrieve rooms with suspended status and suspension end time less than current time
        String query = "SELECT id_room FROM room WHERE status='Suspend' AND suspend_time< ?";
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setLong(1, currentTimeMillis);
            ResultSet resultSet = preparedStatement.executeQuery();


            // Release suspension for each room
            while (resultSet.next()) {
                int idRoom = resultSet.getInt("id_room");
                Serviceroom sr= new Serviceroom();
               sr.resuspendroom(idRoom);
            }
        }
    }




    // CRUD : MOdifier le nom d'un room
    private void setupModifierButtonColumn() {
        TableColumn<ObservableList<String>, Void> modifierColumn = new TableColumn<>("Modifier");
        modifierColumn.setCellFactory(col -> new TableCell<ObservableList<String>, Void>() {
            private final Button modifyButton = new Button("Modifier");

            {
                modifyButton.setOnAction(event -> {
                    int index = getIndex();
                    if (index >= 0 && index < tabr.getItems().size()) {
                        ObservableList<String> rowData = tabr.getItems().get(index);
                        String idRoom = rowData.get(0);
                        String nomRoom = rowData.get(1); // Assuming nom_room is at index 1

                        // Action to perform when the "Modifier" button is clicked
                        System.out.println("Modify room with id: " + idRoom);
                        System.out.println("Current nom_room: " + nomRoom);

                        // Call method to display modify dialog
                       displayModifyDialog(idRoom, nomRoom);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : modifyButton);
            }
        });
        tabr.getColumns().add(modifierColumn);
    }
    private void displayModifyDialog(String idRoom, String currentNomRoom) {
        // Create the custom dialog
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Modifier le nom de la salle");

        // Set the button types
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Create the grid layout for the dialog content
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add labels and text fields to the grid
        TextField newNomRoomTextField = new TextField();
        newNomRoomTextField.setText(currentNomRoom);
        grid.add(new Label("Nouveau nom de la salle:"), 0, 0);
        grid.add(newNomRoomTextField, 1, 0);

        // Set the grid as the dialog content
        dialog.getDialogPane().setContent(grid);

        // Convert the result to a pair when the OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return new Pair<>(idRoom, newNomRoomTextField.getText());
            }
            return null;
        });

        // Show the dialog and wait for user input
        Optional<Pair<String, String>> result = dialog.showAndWait();

        // Process the result when the dialog is closed
        result.ifPresent(newNomRoom -> {
            // Update the database with the new nom_room
            try {
                int idRoomInt = Integer.parseInt(newNomRoom.getKey());
                String newNomRoomValue = newNomRoom.getValue();

                // Perform update operation using idRoomInt and newNomRoomValue
                // Example:
                Serviceroom sr =new Serviceroom();
                 sr.UpdateOne(idRoomInt, newNomRoomValue);
                dispalyallrooms();

                // Display confirmation to the user
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Modification réussie");
                alert.setHeaderText(null);
                alert.setContentText("Le nom de la salle a été modifié avec succès !");
                alert.showAndWait();
            } catch (NumberFormatException e) {
                e.printStackTrace();
                // Handle conversion error
                // Display error message or log the error

        } catch (SQLException e) {
            e.printStackTrace();
            // Handle database error
            // Display error message or log the error
        }
        });
    }


    private void setupButtonColumns() {

        setupModifierButtonColumn();
        setupsuspendButtonColumn();

    }

    public void dispalyallrooms() {
        try
                (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "")){
            Statement statement = connection.createStatement();
            // Retrieve data from the 'formation' table
            ResultSet resultSet = statement.executeQuery("SELECT `nom_form` FROM `formation`");
            ObservableList<String> nomformationList = FXCollections.observableArrayList();
            // Populate the ComboBox with data from the 'nom_formation' column
            while (resultSet.next()) {
                nomformationList.add(resultSet.getString(1));
            }
            nomform.setItems(nomformationList); // Set items to ComboBox

        } catch (SQLException e) {
            throw new RuntimeException("Error initializing AjouterroomFXML", e);
        }


        Serviceroom sm = new Serviceroom();
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "");
            Statement statement = connection.createStatement();

            // Retrieve data from the 'user' table
            ResultSet resultSet = statement
                    .executeQuery("SELECT DISTINCT r.id_room, r.nom_room, f.nom_form, r.date_c_room, r.description,r.status FROM user_formation uf JOIN room r ON r.id_room = uf.room_id AND r.status IN ('Active', 'Suspend')  JOIN formation f ON uf.form_id = f.id_form;");

            //join user u ON f.user_id=u.id_user and u.role='formateur'
            ObservableList<ObservableList<String>> oblist = FXCollections.observableArrayList();

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                row.add(resultSet.getString("id_room"));
                row.add(resultSet.getString("nom_room"));
                row.add(resultSet.getString("nom_form"));
               // row.add(resultSet.getString("username"));
                row.add(resultSet.getString("description"));
                row.add(resultSet.getString("date_c_room"));
                row.add(resultSet.getString("status"));

                oblist.add(row);
            }

            tabr.setItems(oblist);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
// Bind columns to their respective data properties
      //  idroom.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(0)));

        nmroom.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(1)));
        nmform.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(2)));
        //formateur.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));
        descroom.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(3)));
       date_c_room.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(4)));
   statusroom.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(5)));
    }

    @FXML
    void actualiserroom(ActionEvent event) {
        checkSuspensionStatus();
        dispalyallrooms();
        setupButtonColumns();
        //TableColumn<ObservableList<String>, Void> modifierColumn = setupModifierButtonColumn();
        // tabr.getColumns().add(modifierColumn);
        TableColumn<ObservableList<String>, Void> deleteColumn = setupDeleteButtonColumn();
        tabr.getColumns().add(deleteColumn);

        TableColumn<ObservableList<String>, Void> suspendColumn = setupsuspendButtonColumn();
        if (suspendColumn != null) {
            tabr.getColumns().add(suspendColumn);
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        checkSuspensionStatus();
        dispalyallrooms();
        setupButtonColumns();
       //TableColumn<ObservableList<String>, Void> modifierColumn = setupModifierButtonColumn();
      // tabr.getColumns().add(modifierColumn);
        TableColumn<ObservableList<String>, Void> deleteColumn = setupDeleteButtonColumn();
        tabr.getColumns().add(deleteColumn);

        TableColumn<ObservableList<String>, Void> suspendColumn = setupsuspendButtonColumn();
        if (suspendColumn != null) {
            tabr.getColumns().add(suspendColumn);
        }

    }


}
