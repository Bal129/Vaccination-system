package JavaDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/** 
 * registerWindow let the user to register their recipient account
 * this window redirected from SelectModeWindow
 * this window will redirect to MainMenuRecipient
*/

public class GuiRegister {
    private Stage stage = new Stage();
    private TextField fieldName = new TextField();
    private PasswordField fieldPassword = new PasswordField();
    private TextField fieldPhone = new TextField();
    private TextField fieldAge = new TextField();

    public void display(){
        stage.getIcons().add(new Image(Keys.ICON));
        stage.setResizable(false);
        stage.setTitle("Register");
        stage.setOnCloseRequest(e -> {
            e.consume();
            if (GuiConfirmWindow.display("Are you Sure?", "Exiting the program"))
                stage.close();
        });

        // labels for register scene
        Label labelName = new Label("Name");
        Label labelPassword = new Label("Password");
        Label labelPhone = new Label("Phone");
        Label labelAge = new Label("Age");
        labelName.setId("layoutFont");
        labelPassword.setId("layoutFont");
        labelPhone.setId("layoutFont");
        labelAge.setId("layoutFont");

        // fields for register scene
        fieldName.setPromptText("Enter Name");
        fieldPassword.setPromptText("Enter Password");
        fieldPhone.setPromptText("+60-***** ****");
        fieldAge.setPromptText("Enter Age");

        // buttons for register scene
        Button buttonRegister = new Button("Register");
        buttonRegister.setStyle("-fx-font-size: 20px;");
        buttonRegister.setDefaultButton(true); // pressing enter key will launch this button
        buttonRegister.setOnAction(e -> register());

        // button back to select mode
        Button buttonBack = new Button("Back");
        buttonBack.setOnAction(e -> {
            GuiLogin guiLogin = new GuiLogin();
            guiLogin.display('r');
            stage.close();
        });

        HBox boxBottom = new HBox(buttonBack, buttonRegister);
        boxBottom.setAlignment(Pos.CENTER_RIGHT);
        boxBottom.setSpacing(10);
        
        // setting up grid for register scene
        GridPane gridRegister = new GridPane();

        // setting up labels for grid in register scene
        GridPane.setConstraints(labelName, 0, 0);
        GridPane.setConstraints(labelPassword, 0, 1);
        GridPane.setConstraints(labelPhone, 0, 2);
        GridPane.setConstraints(labelAge, 0, 3);

        // setting up fields for grid in register scene
        GridPane.setConstraints(fieldName, 1, 0);
        GridPane.setConstraints(fieldPassword, 1, 1);
        GridPane.setConstraints(fieldPhone, 1, 2);
        GridPane.setConstraints(fieldAge, 1, 3);

        // setting up button for grid in register scene
        GridPane.setConstraints(boxBottom, 0, 4);
        GridPane.setColumnSpan(boxBottom, 2);

        // add all nodes inside the grid in register scene
        gridRegister.setAlignment(Pos.CENTER);
        gridRegister.setVgap(20);
        gridRegister.setHgap(5);
        gridRegister.getChildren().addAll(
            labelName, labelPassword, labelAge, labelPhone, 
            fieldName, fieldPassword, fieldAge, fieldPhone,
            boxBottom
        );

        Scene sceneRegister = new Scene(gridRegister, 400, 300);
        sceneRegister.getStylesheets().add(Keys.STYLING_FILE);

        stage.setScene(sceneRegister);
        stage.show();
    }

    private void register() {
        try {
            // checking whether the recipient insert non-empty
            // textfield(s) or not
            if (fieldName.getText().trim().isEmpty() || 
                fieldPassword.getText().trim().isEmpty() ||
                fieldPhone.getText().trim().isEmpty() ||
                fieldAge.getText().trim().isEmpty())
                GuiPopupWindow.display("Error", "Please enter the values");
            else{
                int newId = Finder.loadDatabase().loadRecipients().size();
                Recipient recipient = new Recipient(
                    newId, fieldName.getText(), fieldPassword.getText(), 
                    fieldPhone.getText(), Integer.parseInt(fieldAge.getText())
                );
                Finder.loadDatabase().loadRecipients().add(recipient);
                Finder.loadDatabase().saveRecipients();
                GuiPopupWindow.display("Success", "Succcessfully Registered\nYour ID is: " + recipient.getId());
                GuiMenuRecipient mainMenuRecipient = new GuiMenuRecipient(recipient.getId());
                mainMenuRecipient.display();
                stage.close();
            }
        } catch (NumberFormatException ex) {
            GuiPopupWindow.display("Error", "Message:" + ex.getMessage());
        } catch (FileNotFoundException ex){
            GuiPopupWindow.display("Error", "Cannot access the database\n" + ex.getMessage());
        } catch (IOException ex){
            GuiPopupWindow.display("Error", "Unable to Register");
        }
    }
}
