package upt.coproject;

import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application
{
    HDD_Controller hdd = new HDD_Controller();
    @Override
    public void start(Stage primaryStage)
    {
        Controller.setWindow(primaryStage);
        primaryStage.setTitle("Gândăceii Pofticioși");
        primaryStage.setResizable(false);
        Controller.changePage("Dashboard.fxml");

    }

    public static void main(String[] args) {
        launch();
    }
}
