package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.fxml.Initializable;
import javafx.event.ActionEvent;
import javafx.scene.layout.RowConstraints;
import models.CalendarData;

import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

public class CalendarController implements Initializable {

    @FXML
    private GridPane daysGrid;

    @FXML
    private MenuButton monthMenuBtn;

    @FXML
    private MenuButton yearMenuBtn;

    Calendar c = Calendar.getInstance();

    private boolean _monthChosen = false;
    private boolean _yearChosen = false;
    private Button[] btns;
    private final int BUTTONS_IN_A_ROW = 7;
    private final int LINES = 6;
    CalendarData calendarData = new CalendarData();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Set default values to today's month and year
        int initialMonth = c.get(Calendar.MONTH) + 1;
        int initialYear = c.get(Calendar.YEAR);

        monthMenuBtn.setText(String.valueOf(initialMonth));
        yearMenuBtn.setText(String.valueOf(initialYear));

        c.set(Calendar.MONTH, initialMonth - 1);
        c.set(Calendar.YEAR, initialYear);

        _monthChosen = true;
        _yearChosen = true;
        numberOfDaysInMonth();
    }

    @FXML
    void monthPressed(ActionEvent event) {
        MenuItem btn = (MenuItem) event.getSource();
        monthMenuBtn.setText(btn.getText());
        int month = Integer.parseInt(btn.getText());
        c.set(Calendar.MONTH, month - 1);
        _monthChosen = true;
        numberOfDaysInMonth();
    }

    @FXML
    void yearPressed(ActionEvent event) {
        MenuItem btn = (MenuItem) event.getSource();
        yearMenuBtn.setText(btn.getText());
        int year = Integer.parseInt(btn.getText());
        _yearChosen = true;
        c.set(Calendar.YEAR, year);
        numberOfDaysInMonth();
    }

    private void numberOfDaysInMonth() {
        if (!_monthChosen || !_yearChosen) {
            return;
        }
        int daysInMonth = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        createDaysBtns(daysInMonth);
    }

    private void createDaysBtns(int daysInMonth) {
        c.set(Calendar.DAY_OF_MONTH, 1);
        btns = new Button[daysInMonth];
        daysGrid.getChildren().clear();

        // Clear existing row constraints
        daysGrid.getRowConstraints().clear();

        for (int i = 0; i < daysInMonth; i++) {
            int day = i + 1;  // Day number

            c.set(Calendar.DAY_OF_MONTH, day);
            List<String> eventNames = calendarData.getEventNamesForDay(c);

            btns[i] = new Button(day + "");
            btns[i].setPrefSize(80, 80); // Set your desired button size here

            // Customize button appearance
            btns[i].setStyle("-fx-font-size: 18; -fx-background-color: " + (eventNames.isEmpty() ? "#F4F2EF;" : "#E68C3A;") + " -fx-text-fill: " + (eventNames.isEmpty() ? "#e68c3a;" : "#F4F2EF;"));

            // If there are events, set the text to include both day number and event names
            if (!eventNames.isEmpty()) {
                StringBuilder buttonText = new StringBuilder(day + "\n");
                for (String eventName : eventNames) {
                    buttonText.append(eventName).append("\n");
                }
                btns[i].setText(buttonText.toString().trim());
            }

            // Add the button to the correct position in the grid
            int row = i / BUTTONS_IN_A_ROW;
            int col = i % BUTTONS_IN_A_ROW;
            daysGrid.add(btns[i], col, row);

            // Add press-release effect
            addPressReleaseEffect(btns[i], day);
        }

        // Set the height of the first row
        RowConstraints firstRowConstraints = new RowConstraints();
        firstRowConstraints.setPrefHeight(120); // Set your desired height for the first row
        daysGrid.getRowConstraints().add(0, firstRowConstraints);
    }

    private void addPressReleaseEffect(Button button, int day) {
        button.setOnMousePressed(event -> {
            button.setStyle("-fx-font-size: 18; -fx-background-color: #9C6744; -fx-text-fill: #F4F2EF;");
        });

        button.setOnMouseReleased(event -> {
            c.set(Calendar.DAY_OF_MONTH, day);
            List<String> eventNames = calendarData.getEventNamesForDay(c);

            // Revert to original style
            button.setStyle("-fx-font-size: 18; -fx-background-color: " + (eventNames.isEmpty() ? "#F4F2EF;" : "#E68C3A;") + " -fx-text-fill: " + (eventNames.isEmpty() ? "#e68c3a;" : "#F4F2EF;"));
        });
    }

    private void dayPressed(int day) {
        int dayInMonth = day;
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        List<String> eventNames = calendarData.getEventNamesForDay(c);
        System.out.println("Chosen Date: " + dayInMonth + "/" + month + "/" + year);
        if (!eventNames.isEmpty()) {
            for (String eventName : eventNames) {
                System.out.println("Event: " + eventName);
            }
        }
    }
}
