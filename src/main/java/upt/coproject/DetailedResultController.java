package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class DetailedResultController extends Controller{
    @FXML
    private Button mainPageButton;
    @FXML
    private Button prevPageButton;
    @FXML
    private Button saveToDatabaseButton;

    @FXML
    void goToMainPage(ActionEvent event) {
        changePage("testDev.fxml");
    }

    @FXML
    void saveToDatabase(ActionEvent event) {

    }

    @FXML
    void goToPrevPage(ActionEvent event){
        Controller.prevPage();
    }

}
