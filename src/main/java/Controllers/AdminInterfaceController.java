package Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import models.User;
import services.UserService;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import utils.DBconnection;

public class AdminInterfaceController implements Initializable {
    @FXML
    public TableView <User>tabUsers;
    @FXML
    public Button bRefresh;
    @FXML
    private TableColumn<models.User, String> colUsername;
    @FXML
    private TableColumn<models.User, String> colEmail;
    @FXML
    private TableColumn<models.User, String> colAge;
    @FXML
    private TableColumn<models.User, String >colPassword;
    @FXML
    private TableColumn<models.User, String> colRole;
    @FXML
    public TableColumn<models.User, String> colGender;
    @FXML
    public TableColumn<models.User, String> colImage;
    @FXML
    public TableColumn<models.User, String> colStatus;
    @FXML
    public Button btn_Logout;
    @FXML
    public Button btn_Update_Formateur;
    @FXML
    public Button btn_Add_Formateur;
    @FXML
    public Button btn_Afficher_users;

    @FXML
    public TextField tfRecherche;
    @FXML
    public Button btn_savefile;


    UserService us = new UserService();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Refresh(new ActionEvent());
        setupStatusButtonColumn();
        //getUserList();
        searchRec();
    }
    public void Refresh(ActionEvent event) {
        UserService us = new UserService();
        ObservableList<User> oblist = FXCollections.observableArrayList();
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        //colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        try {
            oblist.addAll(us.selectAll());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        tabUsers.setItems(oblist);
    }
    private void setupStatusButtonColumn() {
        TableColumn<models.User, Void> StatusColumn = new TableColumn<>("Status");
        StatusColumn.setCellFactory(col -> new TableCell<models.User, Void>() {
            private final Button StatusButton = new Button("");

            {
                StatusButton.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    // Toggle the status of the user
                    int currentStatus = Integer.parseInt(String.valueOf(user.getStatus()));
                    int newStatus = (currentStatus == 1) ? 0 : 1; // Toggle between 0 and 1
                    user.setStatus(Integer.parseInt(String.valueOf(newStatus)));

                    us.updateStatus(user);
                    // Update the UI to reflect the status change

                    StatusButton.setText(String.valueOf(newStatus));
                    Refresh(new ActionEvent());
                });
            }
         @Override
            protected void updateItem(Void item, boolean empty) {
            super.updateItem(item, empty);
            if (empty) {
                setGraphic(null);
            } else {
                models.User user = getTableView().getItems().get(getIndex());
               // StatusButton.setText(String.valueOf(user.getStatus()));
                if (String.valueOf(user.getStatus()).equals("1"))
                    StatusButton.setText("Désactiver");
                else StatusButton.setText("Activer");
                setGraphic(StatusButton);
                Refresh(new ActionEvent());
            }
         }
        });
            tabUsers.getColumns().add(StatusColumn);
    }

    @FXML
    public void Add_Formateur(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminAddFormateur.fxml"));
        Parent root = loader.load();
        AdminAddFormateurController lc = loader.getController();
        btn_Add_Formateur.getScene().setRoot(root);
    }

    @FXML
    public void Update_Formateur(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminUpdateFormateur.fxml"));
        Parent root = loader.load();
        AdminUpdateFormateurController lc = loader.getController();
        btn_Update_Formateur.getScene().setRoot(root);
    }
    @FXML
    public void Display_Users(ActionEvent event)throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminInterface.fxml"));
        Parent root = loader.load();
        AdminInterfaceController lc = loader.getController();
        btn_Afficher_users.getScene().setRoot(root);
    }

    @FXML
    public void Lougout(ActionEvent event) {
        try {
            //taawed thezzek lel inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            btn_Logout.getScene().setRoot(root);

        } catch (IOException ex) {
            Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    @FXML
//    public void Search_Bar(KeyEvent event) {
//
//    }
public  ObservableList<User> getUserList() {
    Connection cnx = DBconnection.getInstance().getCnx();

    ObservableList<User> UserList = FXCollections.observableArrayList();
    try {
        String query2="SELECT * FROM  user ";
        PreparedStatement smt = cnx.prepareStatement(query2);
        User user;
        ResultSet rs= smt.executeQuery();
        while(rs.next()){
            User u = new User();

            u.setId_user(rs.getInt((1)));
            u.setUsername(rs.getString((2)));
            u.setEmail(rs.getString((3)));
            u.setMdp(rs.getString((4)));
            u.setAge(rs.getInt((5)));
            u.setRole(rs.getString((6)));
            u.setGender(rs.getString((7)));
            u.setImage(rs.getString((8)));
            u.setStatus(rs.getInt((9)));

            UserList.add(u);
        }
        System.out.println(UserList);
    } catch (SQLException ex) {
        System.out.println(ex.getMessage());
    }
    return UserList;
}
    private void searchRec() {
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));

        ObservableList<User> list = getUserList();
        tabUsers.setItems(list);

        FilteredList<User> filteredData = new FilteredList<>(list, b -> true);
        tfRecherche.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("Text changed: " + newValue);
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Check if any field contains the filter text
                return user.getEmail().toLowerCase().contains(lowerCaseFilter)
                        || user.getUsername().toLowerCase().contains(lowerCaseFilter);
            });
            System.out.println(filteredData);
        });

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(tabUsers.comparatorProperty());
        tabUsers.setItems(sortedData);
    }

/*ObservableList<Entities.Reclamation> data;
    ReclamationService rs = new ReclamationService();
    Entities.Reclamation r = new Entities.Reclamation();
    List<Reclamation> ls;

    public void load (){
        ListRec.setVisible(true);
        ls = rs.ListerReclamation();
        data = FXCollections.observableArrayList();
        if(!ls.isEmpty())
            ls.stream().forEach((j) -> {
                data.add(new Reclamation(j.getId(),j.getNom(),j.getTypereclamation(),j.getMessage(),j.getIdutilisateur(),j.getEtat()));
                ListRec.setItems(data);
            });

        nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        typereclamation.setCellValueFactory(new PropertyValueFactory<>("typereclamation"));
        message.setCellValueFactory(new PropertyValueFactory<>("message"));

    }

    @FXML
    private void find(ActionEvent event) {
        ObservableList table = ListRec.getItems();

        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue != null && (newValue.length() < oldValue.length())) {
                ListRec.setItems(table);
            }
            String value = newValue.toLowerCase();
            ObservableList<Entities.Reclamation> subentries = FXCollections.observableArrayList();

            long count = ListRec.getColumns().stream().count();
            for (int i = 0; i < ListRec.getItems().size(); i++) {
                for (int j = 0; j < count; j++) {
                    String entry = "" + ListRec.getColumns().get(j).getCellData(i);
                    //if (entry.toLowerCase().equals(value)

                    if (entry.toLowerCase().contains(CharSequence.class.cast(value)))
                    {
                        subentries.add(ListRec.getItems().get(i));
                        break;
                    }
                }
            }
            ListRec.setItems(subentries);
        });
    }*/
    // ******************************* EXPORT TEXT FILE *****************************************
    @FXML
    private void saveFile(ActionEvent event) throws SQLException {

        UserService us = new UserService();
        ObservableList<User> list = FXCollections.observableArrayList();
        colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colPassword.setCellValueFactory(new PropertyValueFactory<>("mdp"));
        colAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        list.addAll(us.selectAll());
        FileChooser fileChooser = new FileChooser();
        // Set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(new Stage());
        if (file != null) {
            saveSystem(file, list);
        }
    }
    public void saveSystem(File file, ObservableList<User> list) {
        try (PrintWriter printWriter = new PrintWriter(file)) {
            // Write header
            printWriter.println("Username,Email,Password,Age,Role,Gender,Image");

            // Add separator line
            printWriter.println("----------------------------------------");

            // Write user data
            for (User user : list) {
                String userData = String.format("Username: %s\nEmail: %s\nPassword: %s\nAge: %d\nRole: %s\nGender: %s\nImage: %s\n",
                        user.getUsername(),
                        user.getEmail(),
                        user.getMdp(),
                        user.getAge(),
                        user.getRole(),
                        user.getGender(),
                        user.getImage());
                printWriter.println(userData);

                // Add separator between users
                printWriter.println("----------------------------------------");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
