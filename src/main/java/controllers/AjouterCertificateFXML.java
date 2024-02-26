package controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import models.Certificat;
import services.ServiceCertificat;
import java.sql.SQLException;
import java.util.List;
public class AjouterCertificateFXML {


    @FXML
    private TableColumn<?, ?> coldate;

    @FXML
    private TableColumn<?, ?> colnomc;

    @FXML
    private ImageView idajouter;


    @FXML
    private TableView<Certificat> tabcertif;


    public void displayAllCertificatesInTableView() {
        colnomc.setCellValueFactory(new PropertyValueFactory<>("nomCertif"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("dateCertif"));


        try {
            ServiceCertificat serviceCertificat = new ServiceCertificat();
            List<Certificat> certificates = serviceCertificat.selectAll();
            ObservableList<Certificat> ql = FXCollections.observableArrayList(certificates);
            tabcertif.setItems(ql);
            System.out.println("affichage... ");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des Certificats : " + e.getMessage());
        }
    }



    private void setupButtonColumns() {

        setupSupprimerButtonColumn();
    }



    private void setupSupprimerButtonColumn() {
        TableColumn<Certificat, Void> supprimerColumn = new TableColumn<>("Supprimer");
        supprimerColumn.setCellFactory(col -> new TableCell<Certificat, Void>() {
            private final Button deleteButton = new Button("Supprimer");

            {
                deleteButton.setOnAction(event -> {
                    Certificat certificat = getTableView().getItems().get(getIndex());
                    // Action to perform when the "Supprimer" button is clicked
                    ServiceCertificat sq = new ServiceCertificat();
                    System.out.println("Delete Certificat " + certificat.getNomCertif());
                    try {
                        sq.deleteOne(certificat);
                        displayAllCertificatesInTableView();
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
        displayAllCertificatesInTableView();
        setupButtonColumns();

    }

}
