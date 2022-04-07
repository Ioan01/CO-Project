package upt.coproject;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

public abstract class Controller {
    @Getter @Setter
    private static Stage window;

    public static void changePage(String fxml){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource(fxml));
            Scene scene = new Scene(fxmlLoader.load(), 1024, 576);
            window.setScene(scene);
            window.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }
}
