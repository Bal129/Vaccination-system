package JavaDir;

import java.io.FileNotFoundException;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;

/** 
 * LoginWindow let the user to login to their account
 * this window redirected from SelectModeWindow
 * this window will redirect to either MainMenuRecipient, MainMenuMOH or MainMenuVC
*/

public class GuiLogin {
    private static int idStringToInt;

    public void display(char selectedMode){
        Stage stage = new Stage();
        stage.getIcons().add(new Image(Keys.ICON));
        stage.setResizable(false);
        stage.setTitle("Login");
        stage.setOnCloseRequest(e->{
            e.consume();
            if (GuiConfirmWindow.display("Are you Sure?", "Exiting the program"))
                stage.close();
        });

        // Labels for login scene
        Label labelLoginID = new Label("ID");
        Label labelPassword = new Label("Password");
        labelLoginID.setId("layoutFont");
        labelPassword.setId("layoutFont");

        // Textfields for login scene
        // convert id from String to int as in the database, id is int
        TextField fieldLoginID = new TextField();
        fieldLoginID.setPromptText(" Enter ID");

        PasswordField fieldPassword = new PasswordField();
        fieldPassword.setPromptText("Enter Password");
        
        // buttons for login scene
        Button buttonLogin = new Button("Login");
        buttonLogin.setStyle("-fx-font-size: 20px;");
        buttonLogin.setDefaultButton(true); // pressing enter key will launch this button
        buttonLogin.setOnAction(e -> {
            try{
                idStringToInt = Integer.parseInt(fieldLoginID.getText());
                boolean loginSuccess = login(selectedMode, idStringToInt, fieldPassword.getText());
                if (loginSuccess){
                    if (selectedMode == 'r'){
                        GuiMenuRecipient mainMenuRecipient = new GuiMenuRecipient(idStringToInt);
                        mainMenuRecipient.display();
                    }
                    else if (selectedMode == 'm') {
                        GuiMenuMOH mainMenuMOH = new GuiMenuMOH();
                        mainMenuMOH.display();
                    }
                    else if (selectedMode == 'v') {
                        GuiMenuVC mainMenuVC = new GuiMenuVC(idStringToInt);
                        mainMenuVC.display();
                    }
                    stage.close();
                }
            } catch (NumberFormatException ex) {
                GuiPopupWindow.display("Error", "Please Enter Integers Only");
            } catch (FileNotFoundException ex) {
                GuiPopupWindow.display("Error", "Cannot access the database");
            }
        });

        // button back to select mode
        Button buttonBack = new Button("Back");
        buttonBack.setOnAction(e -> {
            GuiSelect.display();
            stage.close();
        });

        HBox boxBottom = new HBox(buttonBack, buttonLogin);
        boxBottom.setAlignment(Pos.CENTER_RIGHT);
        boxBottom.setSpacing(10);

        if (selectedMode == 'r') {
            Button buttonRegister = new Button("Sign Up");
            buttonRegister.setOnAction(e -> {
                GuiRegister guiRegister = new GuiRegister();
                guiRegister.display();
                stage.close();
            });
            boxBottom.getChildren().clear();
            boxBottom.getChildren().addAll(buttonRegister, buttonBack, buttonLogin);
        }

        // gridpane used in login
        GridPane gridLogin = new GridPane();
        GridPane.setConstraints(labelLoginID, 0, 0);
        GridPane.setConstraints(labelPassword, 0, 1);
        GridPane.setConstraints(fieldLoginID, 1, 0);
        GridPane.setConstraints(fieldPassword, 1, 1);
        GridPane.setConstraints(boxBottom, 0, 2);
        GridPane.setColumnSpan(boxBottom, 2);

        // adding up all nodes inside gridlogin
        gridLogin.setAlignment(Pos.CENTER);
        gridLogin.setVgap(20);
        gridLogin.setHgap(50);
        gridLogin.getChildren().addAll(labelLoginID, labelPassword, 
                                       fieldLoginID, fieldPassword, 
                                       boxBottom);

        // scene for login window
        Scene sceneLogin = new Scene(gridLogin, 400, 200);
        sceneLogin.getStylesheets().add(Keys.STYLING_FILE);

        stage.setScene(sceneLogin);
        stage.show();

    }

    private boolean login(char selectedMode, int id, String password) throws FileNotFoundException{
        // check whether the id and password match or not
        // if matched, program will redirect to any of the main manu
        // if not match, program will display an error

        try {
            if (selectedMode == 'r') {
                Recipient targetRecipient = Finder.getRecipient(id);
                if (targetRecipient.getPassword().equals(password))
                    return true;
                else GuiPopupWindow.display("Error", "Wrong Password");
            }
            else if (selectedMode == 'v') {
                Vc targetVc = Finder.getVc(id);
                if (targetVc.getPassword().equals(password))
                    return true;
                else GuiPopupWindow.display("Error", "Wrong Password");
            }
            else if (selectedMode == 'm')
                if (Finder.getMOH().getPassword().equals(password))
                    return true;
                else GuiPopupWindow.display("Error", "Wrong Password");
        }
        catch (NullPointerException ex) {
            GuiPopupWindow.display("Error", "Account ID Not Found");
        }

        return false;
    }
}
