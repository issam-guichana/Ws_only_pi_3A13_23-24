package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import models.Formation;
import test.MyListener;
import javafx.scene.input.MouseEvent; // Correct import for MouseEvent

public class FormationFXML {

    @FXML
    private AnchorPane parent;

    @FXML
    private Label name;

    private Formation formation;

    private MyListener myListener;

    @FXML
    void click(MouseEvent event) {
        // Check if myListener is not null before calling onClickListener
        if (myListener != null) {
            myListener.onClickListener(formation);
        }
    }

    public void setData(Formation formation, MyListener myListener) {
        this.myListener = myListener;
        this.formation = formation;
        name.setText(formation.getNom_form());
        name.setStyle("-fx-font-size: 14px; -fx-text-fill: black; -fx-font-weight: bold;");
    }
}
