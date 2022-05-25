package upt.coproject;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import upt.coproject.benchmark.VirtualMemoryBenchmark;

import java.net.URL;
import java.util.ResourceBundle;


public class RAM_Result_Controller extends Controller implements Initializable {


    public void initialize(URL location, ResourceBundle resources) {

    }



    public void goToResults(ActionEvent event) {
        changePage("result.fxml");
    }


}
