package upt.coproject;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class App extends Application
{
    HDD_Controller hdd = new HDD_Controller();
    @Override
    public void start(Stage primaryStage)
    {
        Controller.setWindow(primaryStage);
        primaryStage.setTitle("Gândăceii Pofticioși");//linia 13
        primaryStage.setResizable(false);
        Image image = null;
        try {
            image = new Image(new FileInputStream("src/main/java/upt/coproject/Icon.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        primaryStage.getIcons().add(image);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        Controller.changePage("Dashboard.fxml");

    }

    public static void main(String[] args) {
        launch();
    }
}
