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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.util.ResourceBundle;

public class RAM_Result_Controller extends Controller implements Initializable {

    @FXML
    private Button buttonBack;
    @FXML
    private Label labelRead, labelWrite;

    public void initialize(URL location, ResourceBundle resources) {
        buttonBack.setOnAction(this::goToMainPage);
        labelRead.setText(BigDecimal.valueOf(VirtualMemoryBenchmark.speedRead).setScale(2, RoundingMode.HALF_UP).doubleValue() + " MB/s");
        labelWrite.setText(BigDecimal.valueOf(VirtualMemoryBenchmark.speedWrite).setScale(2, RoundingMode.HALF_UP).doubleValue()  + " MB/s");
    }


}
