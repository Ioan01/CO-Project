package upt.coproject;

import javafx.application.Application;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application
{
    HDD_Controller hdd = new HDD_Controller();
    @Override
    public void start(Stage primaryStage)
    {
        Controller.setWindow(primaryStage);
        primaryStage.setTitle("Amazing benchmarking program");
        //primaryStage.setResizable(false);

        Controller.changePage("mainScene.fxml");

    }

    public static void main(String[] args) {
        launch();
    }
}
