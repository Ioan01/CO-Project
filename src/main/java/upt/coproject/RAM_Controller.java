package upt.coproject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import upt.coproject.benchmark.VirtualMemoryBenchmark;
import upt.coproject.testbench.TestVirtualMemory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;


public class RAM_Controller extends Controller implements Initializable {

    @FXML
    private Button buttonBack, buttonStart, buttonCancel;
    @FXML
    private VBox vboxProgress;
    @FXML
    private HBox hboxButtons;
    @FXML
    public ProgressBar progressRAM;
    @FXML
    private Label labelReadWrite;


    public void initialize(URL location, ResourceBundle resources) {

        vboxProgress.setVisible(false);
        buttonStart.setOnAction(this::startRAM);
        buttonBack.setOnAction(this::goToMainPage);
    }

    public void startRAM(ActionEvent event) {

        progressRAM.setProgress(0);
        hboxButtons.setDisable(true);
        buttonCancel.setText("Cancel");
        buttonCancel.setStyle("-fx-background-color: #d9d9d9;");
        buttonCancel.setOnAction(this::cancelRAM);
        vboxProgress.setVisible(true);

        Thread thread = new Thread(this::startBenching);
        thread.start();


    }

    private void startBenching() {

        VirtualMemoryBenchmark bench = new VirtualMemoryBenchmark();
        progressRAM.progressProperty().bind(bench.getRunningProgress());

        long fileSize = 2L * 1024 * 1024 * 1024;
        int bufferSize = 512*1024*1024;

        bench.initialize("C://", bufferSize, fileSize);
        bench.run();
        System.out.println(bench.getResult());

        Platform.runLater(() -> {
            labelReadWrite.setText("Done.");
            buttonCancel.setText("Results");
            buttonCancel.setStyle("-fx-background-color: #0DE254;");
            hboxButtons.setDisable(false);
            buttonCancel.setOnAction(RAM_Controller.this::goToResults);
        });
    }

    public void cancelRAM(ActionEvent event) {
        hboxButtons.setDisable(false);
    }

    public void goToResults(ActionEvent event) {
        changePage("result.fxml");
    }


}
