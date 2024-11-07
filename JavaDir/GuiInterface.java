package JavaDir;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.geometry.Insets;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Scene;

public abstract class GuiInterface {
    Stage stage;
    Scene sceneMenu;

    Button buttonBack;

    Label labelMenuTop;
    Label labelMenuDown;

    VBox sideMenu;
    BorderPane borderPaneMenu;

    protected void setup() {
        stage = new Stage();
        stage.getIcons().add(new Image(Keys.ICON));
        stage.setTitle("Covid-19 Vaccination Program");
        stage.setOnCloseRequest(e-> {
            e.consume();
            if (GuiConfirmWindow.display("Are you Sure?", "Exiting the program"))
                stage.close();
        });

        // layout for top
        labelMenuTop = new Label("Menu");
        labelMenuTop.setId("layoutFont");
        labelMenuTop.setAlignment(Pos.CENTER_LEFT);
        labelMenuTop.setMinHeight(60);

        // layout for bottom
        labelMenuDown = new Label("You are signing as");
        labelMenuDown.setId("layoutFont");
        labelMenuDown.setAlignment(Pos.CENTER);
        labelMenuDown.setMinHeight(60);

        // setting up layout
        borderPaneMenu = new BorderPane();
        borderPaneMenu.setTop(labelMenuTop);
        borderPaneMenu.setBottom(labelMenuDown);
        BorderPane.setAlignment(labelMenuTop, Pos.CENTER);
        BorderPane.setAlignment(labelMenuDown, Pos.CENTER);

        // side menu
        sideMenu = new VBox();
        sideMenu.getStyleClass().add("borderDesign");
        sideMenu.setPrefWidth(200);
        sideMenu.setSpacing(20);
        sideMenu.setAlignment(Pos.TOP_RIGHT);
        sideMenu.setPadding(new Insets(20, 0, 20, 20));

        // side button
        buttonBack = new Button("Log Out");
        buttonBack.setOnAction(e-> {
            GuiSelect.display();
            stage.close();
        });

        // stage setup
        stage.setMinHeight(700);
        stage.setMinWidth(900);
    }

    public static String getDoseStatus(Recipient recipient){
        VcStatus temp = recipient.getStatus();
        Dose dose1 = temp.getDose(1);
        Dose dose2 = temp.getDose(2);

        if (!dose1.isComplete() && dose1.getAppointment() != null)
            return "First Appointment: " + dose1.getAppointment().format(Keys.FORMATTER);
        else if (!dose2.isComplete() && dose2.getAppointment() != null)
            return "Second Appointment: " + dose2.getAppointment().format(Keys.FORMATTER);
        else if (dose1.isComplete() && !dose2.isComplete())
            return "Completed First Dose";
        else if (dose2.isComplete())
            return "Completed Second Dose";
        return "Pending";
    }
}