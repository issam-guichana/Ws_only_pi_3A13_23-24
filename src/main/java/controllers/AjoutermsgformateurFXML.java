package controllers;
import java.awt.*;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.Message;
import services.Servicemessage;
import utils.DBConnection;

import javax.swing.*;

public class AjoutermsgformateurFXML implements Initializable {
    private Connection cnx ;
    private Stage primaryStage;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;
    @FXML
    private TableColumn<String, String> cnmsg;


    @FXML
    private TableColumn<Message, String> emet;

    @FXML
    private TableView<String> listmsg;
    //@FXML
    // private ListView<String> listmsg;

    @FXML
    private ComboBox<String> selectroom;

    //  @FXML
    //private ListView<String> listpart;

    @FXML
    private ListView<?> nom_room;
    @FXML
    private Button selectimage;
    @FXML
    private TextArea textarea;
    @FXML
    private TextField sendmsg;

    @FXML
    private TableColumn<String, String> cpartic;
    @FXML
    private TableView<String> listpart;

    @FXML
    private FileInputStream fis;
    @FXML
    private ImageView msgimg;
    private String Imgmsg;
    @FXML
    public Label ImagemsgName;

    private HashMap<String, Integer> roomFormationMap = new HashMap<>();




    private boolean processMessage(Message msg, String filePath) {
        try (Scanner scanner = new Scanner(new File(filePath))) {
            Set<String> badWords = new HashSet<>();
            while (scanner.hasNext()) {
                badWords.add(scanner.next().toLowerCase()); // Convert to lowercase for case-insensitive matching
            }

            // Split the message content into individual words
            String[] words = msg.getcontent().split("\\s+"); // Split by whitespace
            boolean containsBadWords = false;
            // Check if any bad words are present in the message
            for (int i = 0; i < words.length; i++) {
                if (badWords.contains(words[i].toLowerCase())) {
                    words[i] = "*".repeat(words[i].length());
                    containsBadWords = true;// Replace the bad word with stars
                }
            }


            String maskedMessage = String.join(" ", words);

            // Proceed with adding the message (with masked bad words) to the database
            Message msgmasque = new Message(maskedMessage, msg.getId_room());
            addmessage(msgmasque);
            return containsBadWords;

        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
            return false; // Error occurred, treat as if message contains bad words
        }
    }


    public void addmessage(Message msg) throws SQLException {
        Servicemessage sp = new Servicemessage();
        sp.InsertOne(msg);
    }

    @FXML
    public void addb3(javafx.event.ActionEvent event) {
        String selectedRoom = "";
        if (selectedRoom == null) {
            // Handle case where no room is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Espace non sélectionné");
            alert.setContentText("Vous devez choisir un espace d'abord !");
            alert.show();
            return;
        }
        try {

            selectedRoom = selectroom.getValue();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "");

            // Prepare statement to retrieve the id of the selected room
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_room` FROM `room` WHERE `nom_room` = ?");
            preparedStatement.setString(1, selectedRoom);

            // Execute the query to retrieve the id of the selected room
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                int roomId = resultSet.getInt("id_room");


                Message default1 = new Message("Veuillez interagir svp", roomId);
                addmessage(default1);
                initial();
            } else {

            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQL or Number Format exception
            // You can show an alert or perform other error handling here
        }
    }



    public void UploadImageroom(javafx.event.ActionEvent event) throws FileNotFoundException, IOException {
        FileChooser fc = new FileChooser();
        //fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Images", listFichier));
        File f = fc.showOpenDialog(null);
        if (f != null) {
            //Commentaire.setText("Image sélectionnée" + f.getAbsolutePath());
            InputStream is = null;
            OutputStream os = null;
            try {
                is = new FileInputStream(f);
                os = new FileOutputStream(new File("C:/xampp/htdocs/image_pi/" + f.getName()));
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }
            } finally {
                if (is != null) {
                    is.close();
                }
                if (os != null) {
                    os.close();
                }
            }
            String selectedRoom = selectroom.getValue();
            if (selectedRoom == null || selectedRoom.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Espace non sélectionné");
                alert.setContentText("Vous devez choisir un espace d'abord !");
                alert.show();
                return;
            }
            try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "");
                 PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_room` FROM `room` WHERE `nom_room` = ?")) {

                preparedStatement.setString(1, selectedRoom);

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        int roomId = resultSet.getInt("id_room");
                        System.out.println(roomId);
                        File file = new File("C:/xampp/htdocs/image_pi/" + f.getName());
                        System.out.println(file.toURI());
                        msgimg.setImage(new Image(file.toURI().toString()));
                        Imgmsg = f.getName();
                        System.out.println(Imgmsg);
                      //  ImagemsgName.setText(Imgmsg);
                        Servicemessage sr = new Servicemessage();
                        sr.Insertwithimage(roomId, Imgmsg);
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Image ajouté");
                        alert.setContentText("image est envoyé avec succés !");
                        alert.show();
                    } else {
                        //Commentaire.setText("Erreur chargement de l'image");
                        //Alert
                        System.out.println("Erreur !");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    @FXML
    public void useemojis(javafx.event.ActionEvent event) {
        // Create a list of emojis



        ObservableList<String> emojis = FXCollections.observableArrayList(
              "", // Add an empty string as the first item to allow deselecting emojis
        "\uD83D\uDE00", // Grinning face
         "\uD83D\uDE01", // Grinning face with smiling eyes
           "\uD83D\uDE02" // Face with tears of joy
        );

        // Create a ListView to display the emojis

         ListView<String> emojiListView = new ListView<>(emojis);

        // Set the preferred height of each item in the ListView to make emojis more visible
        emojiListView.setFixedCellSize(40);
        emojiListView.setCellFactory(listView -> new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item);
                    setTextFill(Color.BLUE); // Set text color to red
                }
            }
        });

        // Handle emoji selection
        emojiListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Do something with the selected emoji (newValue)
            System.out.println("Selected emoji: " + newValue);

            sendmsg.appendText(newValue);


        });
        // Get the source node
        Node sourceNode = (Node) event.getSource();

        // Get the parent of the source node (which is an AnchorPane)
        AnchorPane parent = (AnchorPane) sourceNode.getParent();

        // Add the ListView to the existing layout
        parent.getChildren().add(emojiListView);
    }





    @FXML
    public void addb2(javafx.event.ActionEvent event) {
        String selectedRoom = "";
        if (selectedRoom == null) {
            // Handle case where no room is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Espace non sélectionné");
            alert.setContentText("Vous devez choisir un espace d'abord !");
            alert.show();
            return;
        }
        try {

            selectedRoom = selectroom.getValue();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn1", "root", "");

            // Prepare statement to retrieve the id of the selected room
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_room` FROM `room` WHERE `nom_room` = ?");
            preparedStatement.setString(1, selectedRoom);

            // Execute the query to retrieve the id of the selected room
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

                int roomId = resultSet.getInt("id_room");


                Message default1 = new Message("Je suis à votre disposition", roomId);
                addmessage(default1);
                initial();
            } else {

            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQL or Number Format exception
            // You can show an alert or perform other error handling here
        }
    }


    @FXML
    public void addb1(javafx.event.ActionEvent event) {

        String selectedRoom = "";
        if (selectedRoom == null) {
            // Handle case where no room is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Espace non sélectionné");
            alert.setContentText("Vous devez choisir un espace d'abord !");
            alert.show();
            return;
        }
        try {

            //selectedRoom = roomid.getValue();
            selectedRoom = selectroom.getValue();
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "");

            // Prepare statement to retrieve the id of the selected room
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_room` FROM `room` WHERE `nom_room` = ?");
            preparedStatement.setString(1, selectedRoom);

            // Execute the query to retrieve the id of the selected room
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {

            int roomId = resultSet.getInt("id_room");


        Message default1= new Message("Bienvenue dans mon espace",roomId);
        addmessage(default1);
        initial();
            } else {

            }
        }catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQL or Number Format exception
            // You can show an alert or perform other error handling here
        }
    }

    @FXML
    public void sendmsg(javafx.event.ActionEvent actionEvent) {
        String selectedRoom = selectroom.getValue();
        if (selectedRoom == null) {
            // Handle case where no room is selected
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Espace non sélectionné");
            alert.setContentText("Vous devez choisir un espace d'abord !");
            alert.show();
            return;
        }
        try {

            selectedRoom = selectroom.getValue();
            // Establish connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "");

            // Prepare statement to retrieve the id of the selected room
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `id_room` FROM `room` WHERE `nom_room` = ?");
            preparedStatement.setString(1, selectedRoom);

            // Execute the query to retrieve the id of the selected room
            ResultSet resultSet = preparedStatement.executeQuery();

            // Check if the result set is not empty
            if (resultSet.next()) {
                // Retrieve the id of the selected room

                if (!sendmsg.getText().trim().isEmpty()){
                int roomId = resultSet.getInt("id_room");
                   // String messageText = sendmsg.getText();
                    Message p = new Message(sendmsg.getText(), roomId);
                    String filePath = "src/main/java/controllers/bad_words.txt";
                    boolean containsBadWords = processMessage(p, filePath);


                    if (containsBadWords) {

                        initial();

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Message masqué");
                        alert.setContentText("Désolé, mais une partie de  votre message sera masqué pour des raisons ethique ");
                        alert.show();

                    } else {
                        // addmessage(p);
                        initial();
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Message envoyé");
                        alert.show();


                    }

                // Create a message object with the message text and the id of the selected room

                }
                else  {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Message vide ");
                    alert.setContentText("Vous ne pouvez pas envoyer un message vide ");
                    alert.show();
                }

            } else {

            }
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            // Handle SQL or Number Format exception
            // You can show an alert or perform other error handling here
        }

    }



    @FXML
    void initialize() {
        assert listmsg != null : "fx:id=\"listmsg\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";
        assert listpart != null : "fx:id=\"listpart\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";
        assert nom_room != null : "fx:id=\"nom_room\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";
        assert sendmsg != null : "fx:id=\"sendmsg\" was not injected: check your FXML file 'AjoutermsgformateurFXML.fxml'.";

    }

    public void initial () {
        HashMap<String, Integer> roomFormationMap = new HashMap<>();



        // Liste des rooms eli il appartient lihom
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "")) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT `nom_room`, `id_room` FROM `room` where status IN ('Active', 'Suspend')");
            ObservableList<String> nomformationList = FXCollections.observableArrayList();

            while (resultSet.next()) {
                String nomRoom = resultSet.getString("nom_room");
                int idRoom = resultSet.getInt("id_room");
                nomformationList.add(nomRoom);
                roomFormationMap.put(nomRoom, idRoom); // Store the room ID along with the room name
            }

            selectroom.setItems(nomformationList);
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle SQL exception
            // You might want to show an error message to the user here
            return;
        }

        selectroom.setOnAction(event -> {
            String selectedNomFormation = selectroom.getSelectionModel().getSelectedItem();
            System.out.println(selectedNomFormation);
            if (selectedNomFormation != null) {

                        // change in the integration

                        try (Connection connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "")) {
                            PreparedStatement preparedStatement2 = connection2.prepareStatement("\n" +
                                    " SELECT DISTINCT (u.username)\n" +
                                    " FROM user u\n" +
                                    " JOIN user_formation uf ON uf.user_id = u.id_user \n" +
                                    " JOIN room r ON uf.room_id=r.id_room WHERE r.nom_room = ?;");

                            //SELECT u.username FROM user u JOIN user_formation uf1 ON u.id_user = uf1.user_id JOIN user_formation uf2 ON uf1.form_id = uf2.form_id JOIN formation f ON uf2.form_id = f.id_form WHERE f.id_form = ?;
                            preparedStatement2.setString(1, selectedNomFormation);
                            ResultSet resultSet2 = preparedStatement2.executeQuery();
                            ObservableList<String> Listparti = FXCollections.observableArrayList();
                            while (resultSet2.next()) {
                                Listparti.add(resultSet2.getString("username"));
                            }
                            System.out.println(Listparti);
                            cpartic.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
                            listpart.setItems(Listparti);

                            // Query to retrieve the id_room based on the selected nom_room
                            if (roomFormationMap.containsKey(selectedNomFormation)) {
                                int roomId = roomFormationMap.get(selectedNomFormation);
                                System.out.println(roomId);
                                try (  Connection connection3 = DriverManager.getConnection("jdbc:mysql://localhost:3306/formini.tn2", "root", "")) {
                                        PreparedStatement preparedStatement4 = connection3.prepareStatement("SELECT `contenu`, `sender_msg` FROM `message` WHERE room_id=? and status='Active'") ;
                                    {
                                    preparedStatement4.setInt(1, roomId);
                                    ResultSet resultSet4 = preparedStatement4.executeQuery();
                                                                       ObservableList<String> contenuList = FXCollections.observableArrayList();
                                    while (resultSet4.next()) {
                                        String cnmsgValue = resultSet4.getString("contenu");
                                        String emetteurValue = resultSet4.getString("sender_msg");
                                        contenuList.add(cnmsgValue);
                                    }

                                    // Set the items in the TableView
                                    cnmsg.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
                                    //System.out.println(contenuList);
                                    listmsg.setItems(contenuList);
                                    // listmsg.setItems(messageList);
                                }

                            }

                            } else {
                     System.out.println("No formation found for selected room: " + selectedNomFormation);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();

                }
            }
        });

    }
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initial();
    }



}