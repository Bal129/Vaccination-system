package JavaDir;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 * main menu for getMOH()
 * here, getMOH() can
 * 1. view all recipient and vaccination center(vc) data
 * 2. search for recipient
 * 3. distribute recipient/vaccines to vc
 * 4. view vc statistics
 */

public class GuiMenuMOH extends GuiInterface {
    private BorderPane layoutViewAll;
    private BorderPane layoutDistribute;
    private ScrollPane layoutStatistics;
    private BorderPane layoutDailyStatistics;

    public static final int BUTTONVIEW = 0;
    public static final int BUTTONDISTRIBUTE = 1;
    public static final int BUTTONSTATS = 2;
    public static final int BUTTONDAILYSTATS = 3;

    // layout for left
    private Button buttonViewAll = new Button("View All");
    private Button buttonDistribute = new Button("Distribute");
    private Button buttonStatistics = new Button("View Statistics");
    private Button buttonDailyStats = new Button("View Daily Stats");

    private TableView<Vc> tableViewStatistics = new TableView<>();

    public void display() {
        setup();
        labelMenuTop.setText("MoH");
        labelMenuDown.setText("You are signing as " + Finder.getMOH().getName());

        buttonViewAll.setMinWidth(190);
        buttonDistribute.setMinWidth(190);
        buttonStatistics.setMinWidth(190);
        buttonDailyStats.setMinWidth(190);
        buttonBack.setMinWidth(190);

        sideMenu.getChildren().addAll(buttonViewAll, buttonDistribute, buttonStatistics, buttonDailyStats, buttonBack);

        // setting up layouts
        viewAll();
        distribute();
        statistics();
        dailyStatistics();

        // embedding layout
        borderPaneMenu.setLeft(sideMenu);
        borderPaneMenu.setCenter(layoutViewAll);
        BorderPane.setAlignment(sideMenu, Pos.CENTER);
        BorderPane.setAlignment(layoutViewAll, Pos.CENTER);
        BorderPane.setAlignment(layoutDistribute, Pos.CENTER);
        BorderPane.setAlignment(layoutStatistics, Pos.CENTER);
        BorderPane.setAlignment(layoutDailyStatistics, Pos.CENTER);

        buttonViewAll.setId("buttonFocus");

        // side button functionality
        buttonViewAll.setOnAction(e -> {
            buttonFocus(BUTTONVIEW);
            borderPaneMenu.setCenter(layoutViewAll);
        });

        buttonDistribute.setOnAction(e-> {
            buttonFocus(BUTTONDISTRIBUTE);
            borderPaneMenu.setCenter(layoutDistribute);
        });

        buttonStatistics.setOnAction(e-> {
            tableViewStatistics.refresh();
            buttonFocus(BUTTONSTATS);
            borderPaneMenu.setCenter(layoutStatistics);
        });

        buttonDailyStats.setOnAction(e-> {
            buttonFocus(BUTTONDAILYSTATS);
            borderPaneMenu.setCenter(layoutDailyStatistics);
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
        buttonDistribute.setId("buttonNotFocus");
        buttonStatistics.setId("buttonNotFocus");
        buttonDailyStats.setId("buttonNotFocus");

        if (button == BUTTONVIEW) {
            buttonViewAll.setId("buttonFocus");
        } else if (button == BUTTONDISTRIBUTE) {
            buttonDistribute.setId("buttonFocus");
        } else if (button == BUTTONSTATS) {
            buttonStatistics.setId("buttonFocus");
        } else {
            buttonDailyStats.setId("buttonFocus");
        }
    }

    private boolean distributeRecipientToVc(int vcId, int recipientId) throws Exception {
        Finder.getMOH().distributeRecipient(vcId, recipientId);
        return Finder.getMOH().getRecipient(recipientId).getStatus().getVcId() == vcId;
    }

    private void viewAll() {
        Label labelViewRecipients = new Label("Recipients");
        Label labelViewVc = new Label("Vaccination Centers");

        labelViewRecipients.setId("headerStyling");
        labelViewRecipients.setAlignment(Pos.CENTER);

        labelViewVc.setId("headerStyling");
        labelViewVc.setAlignment(Pos.CENTER);

        TableView<Recipient> tableViewRecipient = new TableView<>();
        TableColumn<Recipient, Integer> columnViewRecipientId = new TableColumn<>("Id");
        TableColumn<Recipient, String> columnViewRecipientName = new TableColumn<>("Name");
        TableColumn<Recipient, String> columnViewRecipientAge = new TableColumn<>("Age");
        TableColumn<Recipient, String> columnViewRecipientPhone = new TableColumn<>("Phone");
        columnViewRecipientId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnViewRecipientName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnViewRecipientAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        columnViewRecipientPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        
        tableViewRecipient.getColumns().add(columnViewRecipientId);
        tableViewRecipient.getColumns().add(columnViewRecipientName);
        tableViewRecipient.getColumns().add(columnViewRecipientAge);
        tableViewRecipient.getColumns().add(columnViewRecipientPhone);
        tableViewRecipient.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewRecipient.setPlaceholder(new Label("No Data"));
        tableViewRecipient.setMaxHeight(220);
        tableViewRecipient.getItems().addAll(Finder.getRecipients());
        
        TableView<Vc> tableViewVc = new TableView<>();
        TableColumn<Vc, Integer> columnViewVcId = new TableColumn<>("Id");
        TableColumn<Vc, String> columnViewVcName = new TableColumn<>("Name");
        columnViewVcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnViewVcName.setCellValueFactory(new PropertyValueFactory<>("name"));

        tableViewVc.getColumns().add(columnViewVcId);
        tableViewVc.getColumns().add(columnViewVcName);
        tableViewVc.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewVc.setPlaceholder(new Label("No Data"));
        tableViewVc.setMaxHeight(220);
        tableViewVc.getItems().addAll(Finder.getVcs());

        VBox boxTop = new VBox(labelViewRecipients, tableViewRecipient);
        VBox boxBottom = new VBox(labelViewVc, tableViewVc);
        
        boxTop.setAlignment(Pos.CENTER);
        boxTop.setPadding(new Insets(10));
        boxTop.setSpacing(10);
        boxTop.setFillWidth(true);

        boxBottom.setAlignment(Pos.CENTER);
        boxBottom.setPadding(new Insets(10));
        boxBottom.setSpacing(10);
        boxBottom.setFillWidth(true);

        BorderPane.setAlignment(boxTop, Pos.CENTER);
        BorderPane.setAlignment(boxBottom, Pos.CENTER);
        layoutViewAll = new BorderPane();
        layoutViewAll.setTop(boxTop);
        layoutViewAll.setBottom(boxBottom);
        layoutViewAll.setId("layoutDesign");
    }

    private void distribute() {
        Label labelSelectT = new Label("Set Vc Id: ");
        Label labelSelectL = new Label("Enter Recipient ID:");
        Label labelAmountR = new Label("Amount: ");

        Label labelStatusT = new Label("The Vc Id: ");
        Label labelStatusL = new Label("Status: ");
        Label labelStatusR = new Label("Status: Updated\nCurrent Amount: ");

        Label labelFail = new Label("Success For:\nFail For:");

        labelSelectT.setId("headerStyling");
        labelSelectL.setId("headerStyling");
        labelAmountR.setId("headerStyling");
        
        labelFail.setId("failStyling");
        
        TextField fieldSearchT = new TextField();
        TextField fieldSearchL = new TextField();
        TextField fieldSetR = new TextField();
        fieldSearchT.setPromptText("Vc ID");
        fieldSearchL.setPromptText("Separate IDs With ','");
        fieldSetR.setPromptText("Input Amount");

        fieldSearchT.setMaxWidth(250);
        fieldSearchL.setMaxWidth(250);
        fieldSetR.setMaxWidth(200);
        
        Button buttonSetT = new Button("Search");
        Button buttonSetL = new Button("Set Recipient");
        Button buttonSetR = new Button("Set Amount");

        buttonSetL.setDisable(true);
        buttonSetR.setDisable(true);

        buttonSetT.setId("buttonInLayout");
        buttonSetL.setId("buttonInLayout");
        buttonSetR.setId("buttonInLayout");
        
        // set update the recipient and vc
        buttonSetT.setOnAction(e->{
            try {
                int idStringToInt = Integer.parseInt(fieldSearchT.getText());
                labelStatusT.setText("The Vc: " + Finder.getVc(idStringToInt).getName());
                labelAmountR.setText("Amount:" + Finder.getVc(idStringToInt).getVaccines().size());
                buttonSetL.setDisable(false);
                buttonSetR.setDisable(false);
            } catch (NullPointerException ex) {
                buttonSetL.setDisable(true);
                buttonSetR.setDisable(true);
                GuiPopupWindow.display("Error", Keys.NOTFOUND);
            } catch (NumberFormatException ex) {
                buttonSetL.setDisable(true);
                buttonSetR.setDisable(true);
                GuiPopupWindow.display("Error", "Please Enter Properly\n" + ex.getMessage());
            }
        });

        buttonSetL.setOnAction(e -> {
            try {
                int vcIdStringToInt = Integer.parseInt(fieldSearchT.getText());
                Set<String> targetRecipients = new HashSet<>(Arrays.asList(fieldSearchL.getText().split(",")));
                ArrayList<String> successDistribute = new ArrayList<>();
                ArrayList<String> failDistribute = new ArrayList<>();

                for (String id : targetRecipients) {
                    int idParsed = Integer.parseInt(id);
                    Recipient check = Finder.getRecipient(idParsed);
                    if (check.getStatus().getVcId() < 0) {
                        distributeRecipientToVc(vcIdStringToInt, idParsed);
                        successDistribute.add(id);
                    } else failDistribute.add(id);
                }
                labelStatusL.setText("Status:");
                labelFail.setText(
                    "Success For: " + successDistribute +
                    "\nFail For: " + failDistribute
                );
                Finder.loadDatabase().saveRecipients();
            } catch (NullPointerException ex) {
                GuiPopupWindow.display("Error", Keys.NOTFOUND);
            } catch (ParseException ex) {
                GuiPopupWindow.display("Error", "Please Enter Properly\n" + ex.getMessage());
            } catch (Exception ex) {
                GuiPopupWindow.display("Error", ex.getMessage());
            } finally {
                buttonSetL.setDisable(true);
                buttonSetR.setDisable(true);
            }       
        });

        buttonSetR.setOnAction(e -> {
            try {
                int vcIdStringToInt = Integer.parseInt(fieldSearchT.getText());
                int inputQuantity = Integer.parseInt(fieldSetR.getText());

                if (inputQuantity < 0)
                    GuiPopupWindow.display("Error", "Value Must Be Positive");

                Finder.getMOH().distributeVaccine(vcIdStringToInt, inputQuantity);
                Finder.loadDatabase().saveVaccines();

                labelStatusR.setText(
                    "Status: Updated\nCurrent Amount: " + 
                    Finder.getMOH().getVc(vcIdStringToInt).getVaccines().size()
                );

                Finder.loadDatabase().saveVcs();
            } catch (NullPointerException ex) {
                GuiPopupWindow.display("Error", Keys.NOTFOUND + ex.getMessage());
                labelStatusR.setText("Status:\nCurrent Amount: ");
            } catch (NumberFormatException ex) {
                GuiPopupWindow.display("Error", "Please Enter Properly");
                labelStatusR.setText("Status:\nCurrent Amount: ");
            } catch (Exception ex) {
                GuiPopupWindow.display("Error", Keys.CANNOTSAVE);
                labelStatusR.setText("Status:\nCurrent Amount: ");
            } finally {
                buttonSetL.setDisable(true);
                buttonSetR.setDisable(true);
            }
        });

        VBox boxTopDistribute = new VBox();
        VBox boxLeftDistribute = new VBox();
        VBox boxRightDistribute = new VBox();

        boxTopDistribute.setAlignment(Pos.CENTER);
        boxTopDistribute.setId("headerStyling");
        boxTopDistribute.setSpacing(5);

        boxLeftDistribute.setAlignment(Pos.CENTER);
        boxLeftDistribute.setId("headerStyling");
        boxLeftDistribute.setSpacing(5);
        boxLeftDistribute.setMinWidth(400);

        boxRightDistribute.setAlignment(Pos.CENTER);
        boxRightDistribute.setId("headerStyling");
        boxRightDistribute.setSpacing(5);

        boxTopDistribute.getChildren().addAll(
            labelSelectT, fieldSearchT, buttonSetT, labelStatusT
        );
        
        boxLeftDistribute.getChildren().addAll(
            labelSelectL, fieldSearchL, buttonSetL, labelStatusL, labelFail
        );

        boxRightDistribute.getChildren().addAll(
            labelAmountR, fieldSetR, buttonSetR, labelStatusR
        );

        layoutDistribute = new BorderPane();
        layoutDistribute.setTop(boxTopDistribute);
        layoutDistribute.setLeft(boxLeftDistribute);
        layoutDistribute.setCenter(boxRightDistribute);
        layoutDistribute.setId("layoutDesign");
    }

    private void statistics() {
        TableColumn<Vc, Integer> columnViewStatsVcId = new TableColumn<>("Id");
        TableColumn<Vc, String> columnViewStatsVcName = new TableColumn<>("Name");
        TableColumn<Vc, Integer> columnViewStatsVcCapacity = new TableColumn<>("Capacity/Day");
        TableColumn<Vc, Integer> columnViewStatsVcVaccineQuantity = new TableColumn<>("Unused Vaccine Quantity");
        TableColumn<Vc, Integer> columnViewStatsVcTotalVacCount = new TableColumn<>("Total Vaccine Count(Dose1+2)");
        
        columnViewStatsVcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        columnViewStatsVcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnViewStatsVcCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        columnViewStatsVcVaccineQuantity.setCellValueFactory(new PropertyValueFactory<>("currentVac"));
        columnViewStatsVcTotalVacCount.setCellValueFactory(new PropertyValueFactory<>("totalVacCount"));

        tableViewStatistics.setPrefHeight(570);
        tableViewStatistics.getColumns().add(columnViewStatsVcId);
        tableViewStatistics.getColumns().add(columnViewStatsVcName);
        tableViewStatistics.getColumns().add(columnViewStatsVcCapacity);
        tableViewStatistics.getColumns().add(columnViewStatsVcVaccineQuantity);
        tableViewStatistics.getColumns().add(columnViewStatsVcTotalVacCount);
        tableViewStatistics.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewStatistics.getItems().addAll(Finder.getVcs());

        layoutStatistics = new ScrollPane();
        layoutStatistics.setFitToWidth(true);
        layoutStatistics.setPrefHeight(540);
        layoutStatistics.setContent(tableViewStatistics);
    }

    private void dailyStatistics() {
        Label labelSelectVc = new Label("Desired Vc:");
        TextField fieldSelectVc = new TextField();
        Button buttonSelectVc = new Button("Find");

        labelSelectVc.setId("headerStyling");
        fieldSelectVc.setPromptText("Enter Vc Id");
        fieldSelectVc.setMaxWidth(300);

        buttonSelectVc.setId("buttonInLayout");

        Label labelLDaily = new Label("Center");
        Label labelRDaily = new Label("None");
        labelLDaily.setId("labelLProfile");
        labelRDaily.setId("labelRProfile");

        TableView<VcDaily> tableVcDaily = new TableView<>();
        TableColumn<VcDaily, LocalDateTime> columnVcDailyDate = new TableColumn<>("Date");
        TableColumn<VcDaily, Integer> columnVcDailyRecipient = new TableColumn<>("Total Recipient");
        columnVcDailyDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        columnVcDailyRecipient.setCellValueFactory(new PropertyValueFactory<>("totalRecipient"));

        tableVcDaily.getColumns().add(columnVcDailyDate);
        tableVcDaily.getColumns().add(columnVcDailyRecipient);
        tableVcDaily.setPlaceholder(new Label("No Report"));
        tableVcDaily.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableVcDaily.setPadding(new Insets(2));

        buttonSelectVc.setOnAction(e -> {
            try {
                int vcId = Integer.parseInt(fieldSelectVc.getText());
                tableVcDaily.getItems().clear();
                tableVcDaily.getItems().addAll(Finder.getVc(vcId).getDailys());
                labelRDaily.setText(Finder.getVc(vcId).getName());
            } catch (NumberFormatException ex) {
                labelRDaily.setText("None");
                GuiPopupWindow.display("Error", "Enter Integers Only");
            } catch (NullPointerException ex) {
                labelRDaily.setText("None");
                GuiPopupWindow.display("Error", Keys.NOTFOUND);
            }
        });

        HBox boxTop = new HBox(labelSelectVc, fieldSelectVc, buttonSelectVc);
        boxTop.setPadding(new Insets(15));
        boxTop.setSpacing(5);
        boxTop.setAlignment(Pos.CENTER);

        HBox boxMiddle = new HBox(labelLDaily, labelRDaily);
        boxMiddle.setSpacing(10);
        boxMiddle.setAlignment(Pos.CENTER);

        layoutDailyStatistics = new BorderPane();
        layoutDailyStatistics.setId("layoutDesign");
        layoutDailyStatistics.setTop(boxTop);
        layoutDailyStatistics.setCenter(boxMiddle);
        layoutDailyStatistics.setBottom(tableVcDaily);
    }
}