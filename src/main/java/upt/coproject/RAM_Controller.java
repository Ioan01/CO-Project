package upt.coproject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private VBox vboxProgress, vboxChoice;
    @FXML
    private HBox hboxButtons;
    @FXML
    public ProgressBar progressRAM;
    @FXML
    private Label labelReadWrite;
    @FXML
    private ComboBox<String> comboBuffer, comboFile;

    private final ObservableList<String> bufferList = FXCollections.observableArrayList("4 KB", "16 KB", "64 KB", "256 KB", "1 MB", "4 MB", "16 MB", "64 MB", "256 MB", "512 MB");
    private final ObservableList<String> fileList = FXCollections.observableArrayList("2 GB", "4 GB", "8 GB", "16 GB", "32 GB");

    public void initialize(URL location, ResourceBundle resources) {

        vboxProgress.setVisible(false);
        buttonStart.setOnAction(this::startRAM);
        buttonBack.setOnAction(this::goToMainPage);
        comboBuffer.setItems(bufferList);
        comboFile.setItems(fileList);
        comboBuffer.setValue("512 MB");
        comboFile.setValue("2 GB");
    }

    public void startRAM(ActionEvent event) {

        progressRAM.setProgress(0);
        hboxButtons.setDisable(true);
        buttonCancel.setText("Cancel");
        buttonCancel.setStyle("-fx-background-color: #d9d9d9;");
        buttonCancel.setOnAction(this::cancelRAM);
        vboxProgress.setVisible(true);
        vboxChoice.setDisable(true);

        Thread thread = new Thread(this::startBenching);
        thread.start();
    }

    private void startBenching() {

        VirtualMemoryBenchmark bench = new VirtualMemoryBenchmark();
        progressRAM.progressProperty().bind(bench.getRunningProgress());

        long fileSize = convertToBytes(comboFile.getValue());  //2L * 1024 * 1024 * 1024;
        int bufferSize = (int) convertToBytes(comboBuffer.getValue());  //512*1024*1024;

        bench.initialize("C://", bufferSize, fileSize);
        bench.run();
        System.out.println(bench.getResult());

        Platform.runLater(() -> {
            labelReadWrite.setText("Done.");
            buttonCancel.setText("Results");
            buttonCancel.setStyle("-fx-background-color: #0DE254;");
            //hboxButtons.setDisable(false);
            buttonCancel.setOnAction(RAM_Controller.this::goToResults);
        });
    }

    public void cancelRAM(ActionEvent event) {
        hboxButtons.setDisable(false);
    }

    public void goToResults(ActionEvent event) {
        changePage("ramResult.fxml");
    }

    private long convertToBytes(String size)
    {
        long result = 1;
        if(size != null) {
            String[] arr = size.split(" ");
            long value = Long.valueOf(arr[0]);
            String unit = arr[1];
            if (unit.equals("B"))
                result = value;
            else if (unit.equals("KB"))
                result = value * 1024;
            else if (unit.equals("MB"))
                result = value * 1024*1024;
            else if (unit.equals("GB"))
                result = value * 1024*1024*1024;
        }

        return result;
    }
}
