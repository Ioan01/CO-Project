package upt.coproject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import upt.coproject.Benchmark.MockTestBench;


public class HDD_Controller extends Controller{


    public TextField progress_hdd_done;
    @FXML
    private ProgressBar progress_hdd;
    private final MockTestBench testBench;


    public HDD_Controller()
    {
        testBench = new MockTestBench();




    }
    public void start_HDD(ActionEvent event)
    {
        progress_hdd.progressProperty().bind(testBench.getRunningProgress());


        progress_hdd.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                progress_hdd_done.textProperty().setValue(Integer.toString((int)(number.doubleValue()*100) )+ '%');
            }
        });
        testBench.initialize(10);
        testBench.start();
    }


}
