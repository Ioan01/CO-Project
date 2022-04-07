package upt.coproject;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class TestDev extends Application
{
    @Override
    public void start(Stage primaryStage)
    {
        Controller.setWindow(primaryStage);
        primaryStage.setTitle("Amazing benchmarking program");
        //primaryStage.setResizable(false);

        Controller.changePage("testDev.fxml");
    }

    public static void main(String[] args) {
        launch();
    }
}