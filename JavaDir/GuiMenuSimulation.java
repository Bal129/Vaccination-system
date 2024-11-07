package JavaDir;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class GuiMenuSimulation extends GuiInterface {
    private Vc vc;
    private BorderPane layoutSim;
    private ChoiceBox<Integer> choiceDose;
    private HBox boxSenior = new HBox(new Label("Senior Queue, No Data"));
    private HBox boxNormal = new HBox(new Label("Normal Queue, No Data"));
    private HBox boxVaccine = new HBox(new Label("Vaccine Queue, No Data"));
    private HBox boxVaccinated = new HBox(new Label("Vaccinated Queue, No Data"));
    private VcSimulationHall simulationHall;

    private int targetDose;
    private LocalDate targetDate;

    private Button buttonNext = new Button("Next");

    public void display() {
        // initially, setup, label top and bottom
        setup();

        labelMenuTop.setText("Simulation");
        labelMenuDown.setText("You are signing as Simulator Manager");

        buttonBack.setMinWidth(190);
        sideMenu.getChildren().addAll(buttonBack);

        // setting up layouts
        simulation();

        // embedding layout
        borderPaneMenu.setLeft(sideMenu);
        borderPaneMenu.setCenter(layoutSim);
        BorderPane.setAlignment(sideMenu, Pos.CENTER);
        BorderPane.setAlignment(layoutSim, Pos.CENTER);

        // applying scene
        sceneMenu = new Scene(borderPaneMenu);
        sceneMenu.getStylesheets().add(Keys.STYLING_FILE);
        
        // display stage
        stage.setScene(sceneMenu);
        stage.show();
    }

    private void simulation() throws ArrayIndexOutOfBoundsException {
        Label labelSearch = new Label("Simulation For:");
        TextField fieldSearch = new TextField();
        fieldSearch.setPromptText("Vc Id");
        Button buttonSearch = new Button("Find");
        Label labelFound = new Label("Vaccination Center:");
        labelFound.setId("headerStyling");

        DatePicker pickerSearch = new DatePicker();
        pickerSearch.setValue(LocalDate.now());
        choiceDose = new ChoiceBox<>();
        choiceDose.getItems().addAll(1, 2);
        choiceDose.setValue(1);
        Button buttonStart = new Button("Start");
        buttonStart.setPadding(new Insets(20));
        buttonStart.setDisable(true);
        buttonStart.setAlignment(Pos.CENTER);

        Label labelTotalRecipients = new Label("Recipients:\nTotal-\nThis Date-");
        labelTotalRecipients.setPadding(new Insets(2));

        boxSenior.setAlignment(Pos.TOP_LEFT);
        boxNormal.setAlignment(Pos.TOP_LEFT);
        boxVaccine.setAlignment(Pos.TOP_LEFT);
        boxSenior.setPadding(new Insets(5));
        boxNormal.setPadding(new Insets(5));
        boxVaccine.setPadding(new Insets(5));
        boxSenior.setSpacing(7);
        boxNormal.setSpacing(7);
        boxVaccine.setSpacing(7);
        boxVaccinated.setAlignment(Pos.TOP_LEFT);
        boxVaccinated.setPadding(new Insets(5));
        boxVaccinated.setSpacing(7);

        ScrollPane queueSenior = new ScrollPane(boxSenior);
        ScrollPane queueNormal = new ScrollPane(boxNormal);
        ScrollPane stackVaccine = new ScrollPane(boxVaccine);
        ScrollPane queueVaccinated = new ScrollPane(boxVaccinated);
        queueSenior.setMinHeight(60);
        queueSenior.setMaxHeight(60);
        queueNormal.setMinHeight(60);
        queueNormal.setMaxHeight(60);
        stackVaccine.setMinHeight(60);
        stackVaccine.setMaxHeight(60);
        queueVaccinated.setMinHeight(70);
        queueVaccinated.setMaxHeight(70);

        buttonNext.setAlignment(Pos.BOTTOM_RIGHT);
        buttonNext.setStyle("-fx-font-size: 15px;");
        buttonNext.setDisable(true);
        buttonNext.setPadding(new Insets(5));

        buttonSearch.setId("buttonInLayout");
        buttonStart.setId("buttonInLayout");
        buttonStart.setStyle("-fx-font-size: 20px;");
        buttonNext.setId("buttonInLayout");

        buttonSearch.setOnAction(e -> {
            try {
                vc = Finder.getVc(Integer.parseInt(fieldSearch.getText()));
                labelFound.setText("Vaccination Center: " + vc.getName());
                buttonStart.setDisable(false);
                labelTotalRecipients.setText(
                    "Recipients:" + 
                    "\nTotal-" + vc.getRecipients().size() +
                    "\nThis Date-" 
                );
            } catch (NumberFormatException ex) {
                GuiPopupWindow.display("Error", "Please Enter Proper Id");
            } catch (NullPointerException ex) {
                GuiPopupWindow.display("Error", Keys.NOTFOUND);
            }
        });

        buttonStart.setOnAction(e -> {
            targetDose = choiceDose.getValue();
            targetDate = pickerSearch.getValue();

            try {
                ArrayList<Recipient> targetRecipients = getTargetRecipients(targetDose, targetDate);
                LinkedList<Vaccine> targetVaccines = getTargetVaccines();
                simulationHall = new VcSimulationHall(targetRecipients, targetVaccines);
                int senior = simulationHall.getQueueSenior().size();
                int normal = simulationHall.getQueueNormal().size();
                // vc.getDaily(targetDate);
                labelTotalRecipients.setText(
                    "Recipients:" + 
                    "\nTotal-" + vc.getRecipients().size() +
                    "\nThis Date-" + (senior + normal) + ", S-" + senior + ", N-" + normal
                );
                updateQueue();
                buttonStart.setDisable(true);
            } catch (IndexOutOfBoundsException ex) {
                int incompleteRecipients =  getIncompleteRecipients(targetDose, targetDate);
                GuiPopupWindow.display("Error", 
                    "Not Enough Vaccine\nAvailable: " + vc.getVaccines().size() +
                    "\nTotal Needed: " + incompleteRecipients
                );
            }
        });

        buttonNext.setOnAction(e -> {
            try {
                simulationHall.next(vc.getId(),targetDose);
                vc.getVaccines().pop();
                int senior = simulationHall.getQueueSenior().size();
                int normal = simulationHall.getQueueNormal().size();
                labelTotalRecipients.setText("This Date-" + (senior + normal) + ", S-" + senior + ", N-" + normal);
                Finder.loadDatabase().saveVaccines();
                Finder.loadDatabase().saveRecipients();
                Finder.loadDatabase().saveVcs();
                updateQueue();
            } catch (IOException ex) {
                GuiPopupWindow.display("Error", Keys.CANNOTSAVE);
            } catch (Exception ex) {
                GuiPopupWindow.display("Error", ex.getMessage());
            }
        });

        HBox boxTopUp = new HBox(labelSearch, fieldSearch, buttonSearch);
        boxTopUp.setAlignment(Pos.CENTER);
        boxTopUp.setSpacing(5);

        VBox boxTopDown = new VBox(boxTopUp, labelFound);
        boxTopDown.setAlignment(Pos.CENTER);
        boxTopDown.setPadding(new Insets(20,20,5,20));
        boxTopDown.setSpacing(5);

        VBox boxSetupMiddle = new VBox(
            new Label("Choose Date:"), pickerSearch,
            new Label("Choose Dose:"), choiceDose
        );
        boxSetupMiddle.setSpacing(5);
        boxSetupMiddle.setAlignment(Pos.CENTER_LEFT);

        VBox boxMiddleStatus = new VBox(labelTotalRecipients);
        boxMiddleStatus.setStyle("-fx-border-style: solid;");
        boxMiddleStatus.setSpacing(5);
        boxMiddleStatus.setAlignment(Pos.CENTER_LEFT);
        boxMiddleStatus.setPrefWidth(150);
        boxMiddleStatus.setMinWidth(150);

        HBox boxMiddle = new HBox(boxSetupMiddle, buttonStart, boxMiddleStatus);
        boxMiddle.setAlignment(Pos.CENTER);
        boxMiddle.setMaxSize(600, 115);
        boxMiddle.setMinSize(600, 115);
        boxMiddle.setStyle("-fx-border-style: solid;");
        boxMiddle.setSpacing(30);
        boxMiddle.setPadding(new Insets(5));

        VBox boxBottom = new VBox(queueSenior, queueNormal, stackVaccine, queueVaccinated, buttonNext);
        boxBottom.setAlignment(Pos.TOP_RIGHT);
        boxBottom.setSpacing(5);

        layoutSim = new BorderPane();
        layoutSim.setTop(boxTopDown);
        layoutSim.setCenter(boxMiddle);
        layoutSim.setBottom(boxBottom);
        layoutSim.setId("layoutDesign");
    }

    private ArrayList<Recipient> getTargetRecipients(int dose, LocalDate date) {
        ArrayList<Recipient> target = new ArrayList<>();

        for (Recipient recipient : vc.getRecipients()) {
            if (recipient.getStatus().getDose(dose).getAppointment() != null &
                !recipient.getStatus().getDose(dose).isComplete() &&
                recipient.getStatus().getDose(dose).getAppointment().equals(date))
                target.add(recipient);
        }
        return target;
    }

    private LinkedList<Vaccine> getTargetVaccines() {
        LinkedList<Vaccine> target = new LinkedList<>();
        for (Vaccine vaccine : vc.getVaccines()) {
            if (vaccine.getRecipientId() < 0) {
                target.add(vaccine);
            }
        }
        return target;
    }

    private int getIncompleteRecipients(int dose, LocalDate date) {
        int count = 0;

        for (Recipient recipient : vc.getRecipients()) {
            if (recipient.getStatus().getDose(dose).getAppointment() != null &&
                recipient.getStatus().getDose(dose).getAppointment().equals(date) &&
                !recipient.getStatus().getDose(dose).isComplete())
                count++;
        }
        return count;
    }

    private void updateQueue() throws ArrayIndexOutOfBoundsException {
        boxSenior.getChildren().clear();
        boxNormal.getChildren().clear();
        boxVaccine.getChildren().clear();
        boxVaccinated.getChildren().clear();

        boxSenior.getChildren().add(new Label("Senior Queue: "));
        boxNormal.getChildren().add(new Label("Normal Queue: "));
        boxVaccine.getChildren().add(new Label("Vaccine Queue: "));
        boxVaccinated.getChildren().add(new Label("Vaccinated Queue: "));

        if (simulationHall.getQueueNormal().isEmpty() &&
            simulationHall.getQueueSenior().isEmpty()) {
            GuiPopupWindow.display("Notice", "No More Recipient Today");
            boxSenior.getChildren().add(new Label("No Recipient"));
            boxNormal.getChildren().add(new Label("No Recipient"));
            boxVaccine.getChildren().add(new Label("All Used"));
            getVaccinatedRecipients();
            buttonNext.setDisable(true);
        } else
            buttonNext.setDisable(false);
            try {
                if (simulationHall.getQueueSenior().isEmpty())
                    boxSenior.getChildren().add(new Label("No Recipient"));
                else
                    for (Recipient recipient : simulationHall.getQueueSenior()) {
                        Label labelRecipient = new Label(recipient.getName() + "\nAge: " + recipient.getAge());
                        labelRecipient.setMaxSize(150, 55);
                        labelRecipient.setMinSize(150, 55);
                        labelRecipient.setWrapText(true);
                        labelRecipient.setId("labelQueue");
                        labelRecipient.setPadding(new Insets(2));
                        labelRecipient.setAlignment(Pos.TOP_CENTER);
                        boxSenior.getChildren().add(labelRecipient);
                    }

                if (simulationHall.getQueueNormal().isEmpty())
                    boxNormal.getChildren().add(new Label("No Recipient"));
                else
                    for (Recipient recipient : simulationHall.getQueueNormal()) {
                        Label labelRecipient = new Label(recipient.getName() + "\nAge: " + recipient.getAge());
                        labelRecipient.setMaxSize(150, 55);
                        labelRecipient.setMinSize(150, 55);
                        labelRecipient.setWrapText(true);
                        labelRecipient.setId("labelQueue");
                        labelRecipient.setPadding(new Insets(2));
                        labelRecipient.setAlignment(Pos.TOP_CENTER);
                        boxNormal.getChildren().add(labelRecipient);
                    }

                if (simulationHall.getStackVaccines().isEmpty())
                    boxVaccine.getChildren().add(new Label("No Vaccine"));
                else
                    for (Vaccine vaccine : simulationHall.getStackVaccines()) {
                        Label labelVaccine = new Label(String.valueOf(vaccine.getBatchNo()));
                        labelVaccine.setMaxSize(40, 55);
                        labelVaccine.setMinSize(40, 55);
                        labelVaccine.setWrapText(true);
                        labelVaccine.setId("labelQueue");
                        labelVaccine.setPadding(new Insets(2));
                        labelVaccine.setAlignment(Pos.CENTER);
                        boxVaccine.getChildren().add(labelVaccine);
                    }
                getVaccinatedRecipients();
            } catch (ArrayIndexOutOfBoundsException e) {
                GuiPopupWindow.display("Error", e.getMessage());
            }
    }

    private void getVaccinatedRecipients() {
        if (simulationHall.getQueueVaccinated().isEmpty())
            boxVaccinated.getChildren().add(new Label("No Recipient"));
        else
            for (Recipient recipient : simulationHall.getQueueVaccinated()) {
                Dose dose = recipient.getStatus().getDose(choiceDose.getValue());
                Label labelRecipient = new Label(
                    recipient.getName() +
                    "\nAge: " + recipient.getAge() + 
                    "\nBatch No: " + dose.getVaccine().getBatchNo()
                );
                labelRecipient.setMaxSize(150, 55);
                labelRecipient.setMinSize(150, 55);
                labelRecipient.setWrapText(true);
                labelRecipient.setId("labelQueue");
                labelRecipient.setPadding(new Insets(2));
                labelRecipient.setAlignment(Pos.TOP_CENTER);
                boxVaccinated.getChildren().add(labelRecipient);
            }
    }
}