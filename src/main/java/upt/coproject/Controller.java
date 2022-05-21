package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public abstract class Controller {
    @Getter @Setter
    private static Stage  window = new Stage();
    private static Scene prev;

    public static void changePage(String fxml){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource(fxml));
            prev = window.getScene();
            Scene scene = new Scene(fxmlLoader.load(), 800, 500);
            window.setScene(scene);
            window.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void changePage(String fxml, int length, int width){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Controller.class.getResource(fxml));
            prev = window.getScene();
            Scene scene = new Scene(fxmlLoader.load(), length, width);
            window.setScene(scene);
            window.setResizable(true);
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
        changePage("Dashboard.fxml");
    }


    public void displayError(String errorMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
