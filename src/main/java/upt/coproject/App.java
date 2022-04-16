package upt.coproject;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application
{
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