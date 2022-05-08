package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;


public class RAM_Controller extends Controller{


    public ProgressBar progress_ram;
    public TextField progress_ram_done;

    public void start_RAM(ActionEvent event)
    {

    }
    public void goToResults(ActionEvent event)
    {
        changePage("result.fxml");
    }


}
