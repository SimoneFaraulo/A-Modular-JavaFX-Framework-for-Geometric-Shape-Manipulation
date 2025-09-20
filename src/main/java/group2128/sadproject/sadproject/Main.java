package group2128.sadproject.sadproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {

    /**
     * @throws IOException
     * Start the application, give it a title and also the "FXML" file is loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Paint");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     * It launchs the program.
     */
    public static void main(String[] args) {
        launch(args);
    }
}

