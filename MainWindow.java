import java.io.IOException;

import JavaDir.Finder;
import JavaDir.GuiSelect;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * The program starts here
 */

public class MainWindow extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        try {
            Finder.loadDatabase();
        } catch (IOException e) {
            System.out.println("Error Loading Database");   
        }
        GuiSelect.display();
    }
}