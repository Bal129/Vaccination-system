package JavaDir;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/** 
 * main menu for recipient
 * here, recipient will be able to look at their profile
 * which consists of id, name, phone and status
*/

public class GuiMenuRecipient extends GuiInterface {
    private Recipient recipient;
    private GridPane layoutProfile;

    GuiMenuRecipient(){}
    GuiMenuRecipient(int id){
        recipient = Finder.getRecipient(id);
    }

    public void display() {
        // initially, setup, label top and bottom
        setup();
        labelMenuTop.setText("Recipient");
        labelMenuDown.setText("You are signing as " + recipient.getName());

        buttonBack.setMinWidth(190);

        // side button
        sideMenu.getChildren().addAll(buttonBack);

        // setting up show profile
        showProfile();

        // embedding layout
        borderPaneMenu.setLeft(sideMenu);
        borderPaneMenu.setCenter(layoutProfile);
        BorderPane.setAlignment(sideMenu, Pos.CENTER);
        BorderPane.setAlignment(layoutProfile, Pos.CENTER);

        // applying scene
        sceneMenu = new Scene(borderPaneMenu);
        sceneMenu.getStylesheets().add(Keys.STYLING_FILE);

        // displaying stage
        stage.setScene(sceneMenu);
        stage.show();
    }

    private void showProfile() {
        Image imageProfile = new Image("userIcon.png");
        ImageView viewImageProfile = new ImageView(imageProfile);
        viewImageProfile.setFitWidth(150);
        viewImageProfile.setFitHeight(130);
        Label labelStatus = new Label(GuiInterface.getDoseStatus(recipient)); // get the data from database

        VBox boxTop = new VBox();
        boxTop.setAlignment(Pos.CENTER);
        boxTop.setSpacing(5);
        boxTop.setId("topProfile");
        boxTop.getChildren().addAll(viewImageProfile, new Label(recipient.getName()),labelStatus);

        String vcName;

        try {
            vcName = Finder.getVc(recipient.getStatus().getVcId()).getName();    
        } catch (NullPointerException e) {
            vcName = "Not Assigned";
        }
        
        Label labelHeader = new Label("Profile");
        Label labelLName = new Label("Name");
        Label labelLId = new Label("ID");
        Label labelLAge = new Label("Age");
        Label labelLPhone = new Label("Phone");
        Label labelHistory = new Label(
            "First Dose" +
            "\nDate: " + recipient.getStatus().getDose(1).getAppointment() +
            "\nVaccine Batch: " + recipient.getStatus().getDose(1).getVaccine() +
            "\nCenter: " + vcName
        );
        Label labelHistory2 = new Label(
            "Second Dose" +
            "\nDate: " + recipient.getStatus().getDose(2).getAppointment() +
            "\nVaccine Batch: " + recipient.getStatus().getDose(2).getVaccine() +
            "\nCenter: "  + vcName
        );

        Label labelRName = new Label(recipient.getName());
        Label labelRId = new Label(String.valueOf(recipient.getId()));
        Label labelRAge = new Label(String.valueOf(recipient.getAge()));
        Label labelRPhone = new Label(recipient.getPhone());

        StackPane paneHeader = new StackPane(labelHeader);
        paneHeader.setAlignment(Pos.CENTER);
        labelHeader.setAlignment(Pos.CENTER);
        labelHeader.setId("labelHeader");
        labelLName.setId("labelLProfile");
        labelLId.setId("labelLProfile");
        labelLAge.setId("labelLProfile");
        labelLPhone.setId("labelLProfile");
        labelRName.setId("labelRProfile");
        labelRId.setId("labelRProfile");
        labelRAge.setId("labelRProfile");
        labelRPhone.setId("labelRProfile");
        labelHistory.setId("labelHistory");
        labelHistory2.setId("labelHistory");
        viewImageProfile.setId("imageStyle");
        labelStatus.setId("labelStatus");

        VBox boxL = new VBox();
        boxL.setAlignment(Pos.CENTER_LEFT);
        boxL.setSpacing(4);
        boxL.setPrefWidth(75);
        boxL.getChildren().addAll(labelLName, labelLId, labelLAge, labelLPhone);

        VBox boxR = new VBox();
        boxR.setAlignment(Pos.CENTER_RIGHT);
        boxR.setSpacing(4);
        boxR.getChildren().addAll(labelRName, labelRId, labelRAge, labelRPhone);

        HBox boxBottom = new HBox();
        boxBottom.setAlignment(Pos.CENTER);
        boxBottom.setPadding(new Insets(15));
        boxBottom.setSpacing(4);
        boxBottom.getChildren().addAll(labelHistory, labelHistory2);

        GridPane.setConstraints(boxTop, 0, 0);
        GridPane.setConstraints(paneHeader, 0, 1);
        GridPane.setConstraints(boxL, 0, 2);
        GridPane.setConstraints(boxR, 1, 2);
        GridPane.setConstraints(boxBottom, 0, 3);
        GridPane.setColumnSpan(boxTop, 2);
        GridPane.setColumnSpan(paneHeader, 2);
        GridPane.setColumnSpan(boxBottom, 2);
        GridPane.setFillWidth(boxL, true);
        
        layoutProfile = new GridPane();
        layoutProfile.setId("layoutDesign");
        layoutProfile.setAlignment(Pos.CENTER);
        layoutProfile.getChildren().addAll(boxTop, paneHeader, boxL, boxR, boxBottom);
    }
}