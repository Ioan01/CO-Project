package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ResultController extends Controller{

    @FXML
    private Label gandacelLabel;
    @FXML
    private Button mainPageButton;
    @FXML
    private Label scoreLabel;

    @FXML
    void goToMainPage(ActionEvent event) {
        changePage("testDev.fxml");
    }

}

