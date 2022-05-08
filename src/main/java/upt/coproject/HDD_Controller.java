package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import upt.coproject.Benchmark.MockTestBench;

import java.io.IOException;


public class HDD_Controller extends Controller{

    @FXML
    private ProgressBar progressBar;
    private final MockTestBench testBench;


    public HDD_Controller()
    {
        testBench = new MockTestBench();




    }
    public void start_HDD(ActionEvent event)
    {
        progressBar.progressProperty().bind(testBench.getRunningProgress());
        testBench.initialize(10);
        testBench.start();
    }
}
