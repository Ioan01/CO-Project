package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

public class TestDevController extends Controller{
    @FXML
    private Button goToResultButton;

    @FXML
    public void goToResultPage(ActionEvent event){
        changePage("result.fxml");
    }

}
