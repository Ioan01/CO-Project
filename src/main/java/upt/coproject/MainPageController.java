package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;


public class MainPageController extends Controller{

    @FXML
    public void goToHDDPage(ActionEvent event){
        //getWindow().setUserData("1000");
        changePage("scene_hdd.fxml");
    }

    @FXML
    public void goToSSDPage(ActionEvent event){
        //getWindow().setUserData("2000");
        changePage("scene_ssd.fxml");
    }

    @FXML
    public void goToRAMPage(ActionEvent event){
        //getWindow().setUserData("3000");
        changePage("scene_ram.fxml");
    }
}
