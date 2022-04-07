package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;

public class MainPageController extends Controller{
    @FXML
    public void goToHDDPage(ActionEvent event){
        getWindow().setUserData("1000");
        changePage("result.fxml");
    }

    @FXML
    public void goToSSDPage(ActionEvent event){
        getWindow().setUserData("2000");
        changePage("result.fxml");
    }

    @FXML
    public void goToRAMPage(ActionEvent event){
        getWindow().setUserData("3000");
        changePage("result.fxml");
    }
}
