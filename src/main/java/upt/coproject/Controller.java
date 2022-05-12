package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public abstract class Controller {
    @Getter @Setter
    private static Stage window;
    private static Scene prev;
    //ProgressBar pb = new ProgressBar();

    public static void changePage(String fxml){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource(fxml));
            prev = window.getScene();
            Scene scene = new Scene(fxmlLoader.load(), 1024, 576);
            window.setScene(scene);
            window.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    public void goToPrevPage(ActionEvent event){
        Scene crt = window.getScene();
        window.setScene(prev);
        prev = crt;
        window.show();
    }

    @FXML
    public void goToMainPage(ActionEvent event){
        changePage("mainScene.fxml");
    }

    @FXML
    public void goToResults(ActionEvent event){ changePage("result.fxml"); }
}
