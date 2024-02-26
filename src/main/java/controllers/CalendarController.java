package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import models.CalendarData;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarController {

    @FXML
    private GridPane daysGrid;

    @FXML
    private MenuButton monthMenuBtn;

    @FXML
    private Label chosenDateLabel;

    @FXML
    private MenuButton yearMenuBtn;

    Calendar c = Calendar.getInstance();

    private boolean _monthChosen = false;
    private boolean _yearChosen = false;
    private Button[] btns;
    private final int BUTTONS_IN_A_ROW = 7;
    private final int LINES = 6;
    CalendarData calendarData = new CalendarData();

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

        for (int i = 0; i < daysInMonth; i++) {
            int day = i + 1;  // Day number
            c.set(Calendar.DAY_OF_MONTH, day);

            btns[i] = new Button(day + "");
            btns[i].setPrefSize(daysGrid.getPrefWidth() / BUTTONS_IN_A_ROW, daysGrid.getPrefHeight() / LINES);

            // Fetch event names for the current day
            String eventName = calendarData.getEventName(c.getTime());
            System.out.println("Event Name for " + day + ": " + eventName);

            // If there is an event, set the text to include both day number and event name
            if (eventName != null) {
                btns[i].setText(day + "\n" + eventName);
            } else {
                btns[i].setText(day + "");
            }

            // Add the button to the correct position in the grid
            int row = i / BUTTONS_IN_A_ROW;
            int col = i % BUTTONS_IN_A_ROW;
            daysGrid.add(btns[i], col, row);

            btns[i].setOnAction(event -> dayPressed(event));
        }
    }

    private void dayPressed(ActionEvent event) {
        Button dayBtn = (Button) event.getSource();
        int day = Integer.parseInt(dayBtn.getText());
        c.set(Calendar.DAY_OF_MONTH, day);

        int dayInMonth = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH) + 1;
        int year = c.get(Calendar.YEAR);

        // Create a java.util.Date from the Calendar instance
        java.util.Date utilDate = c.getTime();

        String eventName = calendarData.getEventName(utilDate);
        String dateText = "Chosen Date: " + dayInMonth + "/" + month + "/" + year;
        if (eventName != null) {
            dateText += "\nEvent: " + eventName;
        }

        chosenDateLabel.setText(dateText);
    }
}