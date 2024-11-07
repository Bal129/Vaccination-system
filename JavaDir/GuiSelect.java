package JavaDir;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/** 
 * the beginning of the program that will ask the user whether they will login/register as recipient, moh or vc 
 * this window will redirect user to either RegisterWindow or LoginWindow
*/

public class GuiSelect {
    public static void display() {
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Keys.ICON));
        stage.setResizable(false);
        stage.setTitle("Vaccination Program Menu");
        stage.setOnCloseRequest(e-> {
            e.consume();
            if (GuiConfirmWindow.display("Are you Sure?", "Exiting the program"))
                stage.close();
        });

        // labels for select scene
        Label labelTopMode = new Label("Select the Following");
        labelTopMode.setId("layoutFont");
        labelTopMode.setAlignment(Pos.CENTER);

        // buttons for select scene
        // buttonRecipient will redirect to login as recipient, also use recipient database and menu
        Button buttonRecipient = new Button("Login as Recipient");
        buttonRecipient.setId("buttonFlat"); // set style
        buttonRecipient.setPrefSize(200, 500);
        buttonRecipient.setOnAction(e -> {
            GuiLogin guiLogin = new GuiLogin();
            guiLogin.display('r');
            stage.close();
        });

        // buttonMOH will redirect to login as MOH, use MOH database and menu
        Button buttonMOH = new Button("Ministry of Health");
        buttonMOH.setId("buttonFlat");
        buttonMOH.setPrefSize(200, 500);
        buttonMOH.setOnAction(e -> {
            GuiLogin guiLogin = new GuiLogin();
            guiLogin.display('m');
            stage.close();
        });
        
        // buttonvc will redirect to login as vc, use vc database and menu
        Button buttonVC = new Button("Vaccination Center");
        buttonVC.setId("buttonFlat");
        buttonVC.setPrefSize(200, 500);
        buttonVC.setOnAction(e -> {
            GuiLogin guiLogin = new GuiLogin();
            guiLogin.display('v');
            stage.close();
        });

        Button buttonSim = new Button("Simulation");
        buttonSim.setId("buttonFlat");
        buttonSim.setPrefSize(200, 500);
        buttonSim.setOnAction(e -> {
            GuiMenuSimulation guiSim = new GuiMenuSimulation();
            guiSim.display();
            stage.close();
        });

        // layout for select scene
        // using stackpane and gridpane
        StackPane stackMode = new StackPane();
        stackMode.setPadding(new Insets(10,10,10,10));
        stackMode.getChildren().add(labelTopMode);

        // setting up grid for select mode scene
        HBox gridMode = new HBox();
        gridMode.setPadding(new Insets(5, 10, 10, 10));
        gridMode.getChildren().addAll(buttonRecipient, buttonMOH, buttonVC, buttonSim);
        gridMode.setAlignment(Pos.CENTER);
        gridMode.setSpacing(10);

        // layout borderpane
        BorderPane borderPaneMode = new BorderPane();
        borderPaneMode.setTop(stackMode);
        borderPaneMode.setCenter(gridMode);
        BorderPane.setAlignment(stackMode, Pos.CENTER);
        BorderPane.setAlignment(gridMode, Pos.CENTER);

        // login scene
        Scene sceneMode = new Scene(borderPaneMode, 900, 600);
        sceneMode.getStylesheets().add(Keys.STYLING_FILE);

        stage.setScene(sceneMode);
        stage.show();
    }
}
