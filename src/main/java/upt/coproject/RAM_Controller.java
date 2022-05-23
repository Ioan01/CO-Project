package upt.coproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

    public void initialize(URL location, ResourceBundle resources) {

        vboxProgress.setVisible(false);
        buttonStart.setOnAction(this::startRAM);
        buttonBack.setOnAction(this::goToMainPage);
    }

    @FXML
    public ProgressBar progressRAM;

    public void startRAM(ActionEvent event) {

        progressRAM.setProgress(0);
        hboxButtons.setDisable(true);
        buttonCancel.setText("Cancel");
        buttonCancel.setStyle("-fx-background-color: #d9d9d9;");
        buttonCancel.setOnAction(this::cancelRAM);
        vboxProgress.setVisible(true);

        new java.util.Timer().schedule(
                new java.util.TimerTask() {
                    @Override
                    public void run() {


                    }
                },
                5000
        );
        progressRAM.setProgress(1);
        buttonCancel.setText("Results");
        buttonCancel.setStyle("-fx-background-color: #0DE254;");
        hboxButtons.setDisable(false);
        buttonCancel.setOnAction(event1 -> goToResults(event));
    }

    public void cancelRAM(ActionEvent event) {
        hboxButtons.setDisable(false);
    }

    public void goToResults(ActionEvent event) {
        changePage("result.fxml");
    }


}
