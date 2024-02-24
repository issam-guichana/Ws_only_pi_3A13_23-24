package controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Userparticipants;
import services.ServiceEvenement;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UserParticipantController {

    @FXML
    private TableView<Userparticipants> tbParticipants;

    @FXML
    private TableColumn<Userparticipants, Integer> colUserId;

    @FXML
    private TableColumn<Userparticipants, String> colUserName;

    @FXML
    private TableColumn<Userparticipants, String> colEventName;

    @FXML
    private Label lblParticipantCount;

    @FXML
    private TextField tfSearch;

    private ServiceEvenement serviceEvenement;

    private int eventId; // Set the event ID when navigating to this interface

    private List<Userparticipants> allParticipants; // Store all participants for filtering

    public void initialize() {


        colUserId.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUser_id()).asObject());
        colUserName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUserName()));
        colEventName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEvent_name()));

        // Load and display participants
        loadParticipants();
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
        loadParticipants(); // Ensure that participants are loaded when the event ID is set
    }

    private void loadParticipants() {
        try {
            ServiceEvenement se=new ServiceEvenement();
            allParticipants = se.getParticipants(eventId);
            ObservableList<Userparticipants> participantList = FXCollections.observableArrayList(allParticipants);
            tbParticipants.setItems(participantList);

            // Update participant count label
            lblParticipantCount.setText("Participant Count: " + allParticipants.size());
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
    }


    @FXML
    private void searchParticipants() {
        String searchText = tfSearch.getText().toLowerCase().trim();

        List<Userparticipants> filteredParticipants;

        if (searchText.isEmpty()) {
            // If search text is empty, display all participants
            filteredParticipants = allParticipants;
        } else {
            // Filter participants based on the search text
            filteredParticipants = allParticipants.stream()
                    .filter(participant -> String.valueOf(participant.getUser_id()).contains(searchText) ||
                            participant.getUserName().toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
        }
        System.out.println("rechercher...");
        updateTableView(filteredParticipants);
    }

    private void updateTableView(List<Userparticipants> participants) {
        ObservableList<Userparticipants> participantList = FXCollections.observableArrayList(participants);
        tbParticipants.setItems(participantList);

        // Update participant count label
        lblParticipantCount.setText("Participant Count: " + participants.size());
    }


}
