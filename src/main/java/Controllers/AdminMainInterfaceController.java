package Controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminMainInterfaceController {
    @FXML
    public Button btn_Gestion_Users;
    @FXML
    public Button btn_Logout;

    @FXML
    public void Gestion_users(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass()
                .getResource("/AdminInterface.fxml"));
        Parent root = loader.load();
        AdminInterfaceController lc = loader.getController();
        btn_Gestion_Users.getScene().setRoot(root);
    }
    @FXML
    public void Lougout(ActionEvent event) throws IOException{
        try {
            //taawed thezzek lel inscription
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginUser.fxml"));
            Parent root = loader.load();
            btn_Logout.getScene().setRoot(root);

        } catch (IOException ex) {
            Logger.getLogger(LoginUserController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
