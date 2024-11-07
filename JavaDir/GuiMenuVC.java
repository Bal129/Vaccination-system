package JavaDir;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 * main menu for vaccination center
 * here, vc will be able to
 * 1. look at the recipients data
 * 2. set appoinment
 * 3. update recipient status
 * 4. statistics
 */

public class GuiMenuVC extends GuiInterface {
    // static LocalDate date;
    private Vc vc;
    private StackPane layoutViewAll;
    private BorderPane layoutAppointment;
    private VBox layoutUpdate;
    private VBox layoutStatistics;

    private Button buttonViewAll = new Button("View All Recipient Data");
    private Button buttonAppointment = new Button("Set Appointment");
    private Button buttonUpdate = new Button("Update Recipient Status");
    private Button buttonStats = new Button("View Statistics");

    private ArrayList<String> successDistribute = new ArrayList<>();
    private ArrayList<String> failDistribute = new ArrayList<>();
    
    public static final int BUTTONVIEW = 0;
    public static final int BUTTONAPPO = 1;
    public static final int BUTTONUPDATE = 2;
    public static final int BUTTONSTATS = 3;

    private Label labelViewCapacity = new Label(); 
    private Label labelViewVacQuantity = new Label();
    private Label labelViewVacCount = new Label();
    private VBox tableViewStatistics = new VBox();
    

    GuiMenuVC() {}
    GuiMenuVC(int id) {
        vc = Finder.getVc(id);
    }

    public void display() {
        // initially, setup, label top and bottom
        setup();
        labelMenuTop.setText("Vaccination Center");
        labelMenuDown.setText("You are signing as " + vc.getName());

        buttonViewAll.setMinWidth(190);
        buttonAppointment.setMinWidth(190);
        buttonUpdate.setMinWidth(190);
        buttonStats.setMinWidth(190);
        buttonBack.setMinWidth(190);

        buttonViewAll.setId("buttonFocus");

        sideMenu.getChildren().addAll(buttonViewAll, buttonAppointment, buttonUpdate, buttonStats, buttonBack);

        // setting up layouts
        viewAll();
        appointment();
        update();
        statistics();

        // embedding layout
        borderPaneMenu.setLeft(sideMenu);
        borderPaneMenu.setCenter(layoutViewAll);
        BorderPane.setAlignment(sideMenu, Pos.CENTER);
        BorderPane.setAlignment(layoutViewAll, Pos.CENTER);
        BorderPane.setAlignment(layoutAppointment, Pos.CENTER);
        BorderPane.setAlignment(layoutUpdate, Pos.CENTER);
        BorderPane.setAlignment(layoutStatistics, Pos.CENTER);

        // side buttons functionality
        buttonViewAll.setOnAction(e -> {
            buttonFocus(BUTTONVIEW);
            borderPaneMenu.setCenter(layoutViewAll);
        });

        buttonAppointment.setOnAction(e-> {
            buttonFocus(BUTTONAPPO);
            borderPaneMenu.setCenter(layoutAppointment);
        });

        buttonUpdate.setOnAction(e-> {
            buttonFocus(BUTTONUPDATE);
            borderPaneMenu.setCenter(layoutUpdate);
        });

        buttonStats.setOnAction(e-> {
            labelViewCapacity.setText("Capacity/Day: " + vc.getCapacity());
            labelViewVacQuantity.setText("Unused Vaccine Quantity: " + vc.getVaccines().size());
            labelViewVacCount.setText("Total Vaccine Count(Dose1+2): " + vc.getTotalVacCount());
            buttonFocus(BUTTONSTATS);
            borderPaneMenu.setCenter(layoutStatistics);
        });

        // applying scene
        sceneMenu = new Scene(borderPaneMenu);
        sceneMenu.getStylesheets().add(Keys.STYLING_FILE);
        
        // display stage
        stage.setScene(sceneMenu);
        stage.show();
    }

    private void buttonFocus(int button) {
        buttonViewAll.setId("buttonNotFocus");
        buttonAppointment.setId("buttonNotFocus");
        buttonUpdate.setId("buttonNotFocus");
        buttonStats.setId("buttonNotFocus");
        
        if (button == BUTTONVIEW) {
            buttonViewAll.setId("buttonFocus");
        } else if (button == BUTTONAPPO) {
            buttonAppointment.setId("buttonFocus");
        } else if (button == BUTTONUPDATE) {
            buttonUpdate.setId("buttonFocus");
        } else {
            buttonStats.setId("buttonFocus");
        }
    }

    // updating the recipient status 
    private boolean updateRecipientStatus(int id, String status) throws IOException, Exception {
        VcStatus vcStatus = vc.getRecipient(id).getStatus();
        Dose dose1 = vcStatus.getDose(1);
        Dose dose2 = vcStatus.getDose(2);

        if (status.equals("Completed First Dose") && 
            (!dose1.isComplete()) && 
            dose1.getAppointment() != null) {
            Vaccine vaccine = new Vaccine(vc.getId(), id, 1);
            vc.updateStatus(id, 1, vaccine);
            Finder.loadDatabase().addVaccine(vaccine);
            Finder.loadDatabase().saveVaccines();
            Finder.loadDatabase().saveVcs();
            return true;
        } else if (status.equals("Completed Second Dose") && 
                  (!dose2.isComplete()) && 
                  dose2.getAppointment() != null) {
            Vaccine vaccine = new Vaccine(vc.getId(), id, 2);
            vc.updateStatus(id, 2, vaccine);
            Finder.loadDatabase().addVaccine(vaccine);
            Finder.loadDatabase().saveVaccines();
            Finder.loadDatabase().saveVcs();
            return true;
        } else {
            GuiPopupWindow.display("Error", "No Appointment Set");
            return false;
        }
    }

    // setting the appointment
    private boolean setAppointment(LocalDate datePicker, int id) {
        VcStatus temp = vc.getRecipient(id).getStatus();
        Dose dose1 = temp.getDose(1);
        Dose dose2 = temp.getDose(2);

        if (dose1.getAppointment() == null) {
            vc.setAppointment(datePicker, id, vc.getId(), 1);
            return true;
        } else if (dose1.getAppointment() != null && !dose1.isComplete()) {
            return false;
        } else if (dose1.isComplete() && dose2.getAppointment() == null)
            if (datePicker.compareTo(dose1.getAppointment()) < 0)
                return false;
            else {
                vc.setAppointment(datePicker, id, vc.getId(), 2);
                return true;
            }
        else
            return false;
    }

    private void viewAll() {
        TableView<Recipient> tableViewAll = new TableView<>();
        TableColumn<Recipient, String> columnRecipientId = new TableColumn<>("ID");
        TableColumn<Recipient, String> columnRecipientName = new TableColumn<>("Name");
        TableColumn<Recipient, String> columnViewRecipientAge = new TableColumn<>("Age");
        TableColumn<Recipient, String> columnRecipientPhone = new TableColumn<>("Phone");
        columnRecipientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnRecipientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnViewRecipientAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        columnRecipientPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        tableViewAll.getColumns().add(columnRecipientId);
        tableViewAll.getColumns().add(columnRecipientName);
        tableViewAll.getColumns().add(columnViewRecipientAge);
        tableViewAll.getColumns().add(columnRecipientPhone);
        tableViewAll.setPlaceholder(new Label("No Data"));
        tableViewAll.setMinHeight(540);

        try {
            for (int i=0; i<vc.getRecipients().size(); i++)
            tableViewAll.getItems().add(vc.getRecipients().get(i));
        } catch (Exception ex) {
            GuiPopupWindow.display("Error", "Cannot Create Table\n" + ex.getMessage());
        }
        
        tableViewAll.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        layoutViewAll = new StackPane();
        layoutViewAll.setMinHeight(540);
        layoutViewAll.getChildren().add(tableViewAll);
    }

    private void appointment() {
        Label labelAppoSearchFor = new Label("Set Appointment For: ");
        Label labelStatus = new Label("Appointment:\nSuccess For:\nFail For:");

        labelAppoSearchFor.setId("headerStyling");
        labelStatus.setId("labelAppoSet");
        
        TextField fieldSearchRecipient = new TextField();
        fieldSearchRecipient.setPromptText("Separate IDs with ','");

        DatePicker datePickerAppo = new DatePicker();
        datePickerAppo.setValue(LocalDate.now());

        Button buttonAppoSearch = new Button("Find");
        Button buttonAppoSet = new Button("Set");

        buttonAppoSet.setDisable(true);
        buttonAppoSearch.setId("buttonInLayout");

        buttonAppoSearch.setOnAction(e -> {
            try {
                buttonAppoSet.setDisable(false);
            } catch (Error ex) {
                fieldSearchRecipient.clear();
                buttonAppoSet.setDisable(true);
                GuiPopupWindow.display("Error", "Recipient Not Found\n" + ex.getMessage());
            } catch (Exception ex) {
                fieldSearchRecipient.clear();
                buttonAppoSet.setDisable(true);
                GuiPopupWindow.display("Error", "Please Enter Correctly\n" + ex.getMessage());
            } finally {
                labelStatus.setText("Appointment:\nSuccess For:\nFail For:");
            }
        });

        buttonAppoSet.setId("buttonInLayout");
        buttonAppoSet.setOnAction(e -> {
            try {
                LocalDate date = datePickerAppo.getValue();

                if (date.compareTo(LocalDate.now()) < 0)
                    throw new DateTimeException("Must Be At Least Tomorrow");

                Set<String> targetRecipients = new HashSet<>(Arrays.asList(fieldSearchRecipient.getText().split(",")));

                successDistribute.clear();
                failDistribute.clear();

                for (String id : targetRecipients) {
                    boolean appoSetComplete = setAppointment(date, Integer.parseInt(id));
                    if (appoSetComplete)
                        successDistribute.add(id);
                    else
                        failDistribute.add(id);
                }
                labelStatus.setText(
                    "Appointment:" +
                    "\nSuccess For: " + successDistribute +
                    "\nFail For: " + failDistribute
                );
                
                Finder.loadDatabase().saveDailyRecords();
                Finder.loadDatabase().saveVcs();
            } catch (NullPointerException ex) {
                buttonAppoSet.setDisable(true);
                GuiPopupWindow.display("Error", Keys.NOTFOUND);
            } catch (DateTimeException ex) {
                GuiPopupWindow.display("Error", ex.getMessage());
            } catch (Error ex) {
                buttonAppoSet.setDisable(true);
                GuiPopupWindow.display("Error", "Unable to Update Appointment\n" + ex.getMessage());
            } catch (Exception ex) {
                buttonAppoSet.setDisable(true);
                GuiPopupWindow.display("Error", "Unable to Update Appointment\n" + ex.getMessage());
            }
        });

        HBox boxTop = new HBox(labelAppoSearchFor, fieldSearchRecipient, buttonAppoSearch);
        HBox boxBottomUp = new HBox(datePickerAppo, buttonAppoSet);
        VBox boxBottomDown = new VBox(boxBottomUp, labelStatus);

        boxTop.setAlignment(Pos.CENTER);
        boxBottomUp.setAlignment(Pos.CENTER);
        boxBottomDown.setAlignment(Pos.CENTER);

        boxTop.setPadding(new Insets(30));

        boxTop.setSpacing(5);
        boxBottomDown.setSpacing(10);

        boxBottomDown.setMinHeight(300);
        boxBottomDown.setMaxHeight(300);

        layoutAppointment = new BorderPane();
        layoutAppointment.setId("layoutDesign");
        layoutAppointment.setTop(boxTop);
        layoutAppointment.setCenter(boxBottomDown);
    }

    private void update() {
        Label labelSearch = new Label("Enter Recipient ID:");
        Label labelFound = new Label("Search the recipient");
        Label labelUpdate = new Label("Update: Success/fail");

        labelSearch.setId("headerStyling");
        labelFound.setId("headerStyling");
        labelUpdate.setId("failStyling");
        labelFound.setAlignment(Pos.CENTER);

        TextField fieldSearch = new TextField();
        fieldSearch.setPromptText("Recipient ID");

        Button butttonUpdateSearch = new Button("Find");
        butttonUpdateSearch.setId("buttonInLayout");

        Button buttonUpdateRecipient = new Button("Update");
        buttonUpdateRecipient.setDisable(true);
        buttonUpdateRecipient.setId("buttonInLayout");
        buttonUpdateRecipient.setPrefSize(100, 50);
        buttonUpdateRecipient.setStyle("-fx-font-size: 20px;");

        HBox boxTopUpdate = new HBox(
            labelSearch, fieldSearch, 
            butttonUpdateSearch
        );

        boxTopUpdate.setAlignment(Pos.CENTER);
        boxTopUpdate.setSpacing(5);
        boxTopUpdate.setPrefHeight(200);

        ChoiceBox<String> dropDownUpdateRecipient = new ChoiceBox<>();
        dropDownUpdateRecipient.getItems().addAll("Completed First Dose",
                                                  "Completed Second Dose");
        dropDownUpdateRecipient.setValue("Completed First Dose");

        buttonUpdateRecipient.setOnAction(e-> {
            try {
                int idStringToInt = Integer.parseInt(fieldSearch.getText());
                boolean updateSuccess = updateRecipientStatus(idStringToInt, dropDownUpdateRecipient.getValue());
                if (updateSuccess)
                    labelUpdate.setText("Update: Success");
                else 
                    labelUpdate.setText("Update: Fail");
            } catch (IOException ex) {
                GuiPopupWindow.display("Error", Keys.CANNOTSAVE);
            } catch (Exception ex) {
                labelUpdate.setText("Update: Fail");
                fieldSearch.clear();
                buttonUpdateRecipient.setDisable(true);
                GuiPopupWindow.display("Error", "Unable to Update Status\n" + ex.getMessage());
            } finally {
                labelFound.setText("Search the recipient");
            }
        });

        butttonUpdateSearch.setOnAction(e -> {
            try {
                int idStringToInt = Integer.parseInt(fieldSearch.getText());
                labelFound.setText("Changing status for: " + vc.getRecipient(idStringToInt).getName());
                buttonUpdateRecipient.setDisable(false);
            } catch (Error ex) {
                labelFound.setText("Search the recipient");
                fieldSearch.clear();
                buttonUpdateRecipient.setDisable(true);
                GuiPopupWindow.display("Error", "Unable to Update Status\n" + ex.getMessage());
            } catch (Exception ex) {
                labelFound.setText("Search the recipient");
                fieldSearch.clear();
                buttonUpdateRecipient.setDisable(true);
                GuiPopupWindow.display("Error", "Unable to Update Status\n" + ex.getMessage());
            } finally {
                labelUpdate.setText("Update: Success/fail");
            }
        });

        VBox boxBottomUpdate = new VBox(
            labelFound, dropDownUpdateRecipient,
            buttonUpdateRecipient, labelUpdate
        );

        boxBottomUpdate.setSpacing(10);
        boxBottomUpdate.setPadding(new Insets(20));
        boxBottomUpdate.setAlignment(Pos.CENTER);

        layoutUpdate = new VBox();
        layoutUpdate.setId("layoutDesign");
        layoutUpdate.getChildren().addAll(boxTopUpdate, boxBottomUpdate);
    }

    private void statistics() {
        labelViewCapacity.setId("headerStyling");
        labelViewVacQuantity.setId("headerStyling");
        labelViewVacCount.setId("headerStyling");
        tableViewStatistics.setSpacing(20);
        tableViewStatistics.setId("layoutDesign");
        tableViewStatistics.setMinHeight(270);
        tableViewStatistics.setPadding(new Insets(10));
        tableViewStatistics.setAlignment(Pos.CENTER);
        tableViewStatistics.getChildren().addAll(labelViewCapacity, labelViewVacQuantity, labelViewVacCount);

        TableView<VcDaily> tableVcDaily = new TableView<>();
        TableColumn<VcDaily, LocalDate> columnVcDailyDate = new TableColumn<>("Date");
        TableColumn<VcDaily, Integer> columnVcDailyRecipient = new TableColumn<>("Total Recipient");
        columnVcDailyDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnVcDailyRecipient.setCellValueFactory(new PropertyValueFactory<>("totalRecipient"));

        tableVcDaily.getColumns().add(columnVcDailyDate);
        tableVcDaily.getColumns().add(columnVcDailyRecipient);
        tableVcDaily.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableVcDaily.getItems().clear();
        tableVcDaily.getItems().addAll(vc.getDailys());

        layoutStatistics = new VBox();
        layoutStatistics.setId("layoutDesign");
        layoutStatistics.getChildren().addAll(tableViewStatistics, tableVcDaily);
    }
}