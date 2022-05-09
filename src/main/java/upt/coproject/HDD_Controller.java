package upt.coproject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import upt.coproject.benchmark.MockBench;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class HDD_Controller extends Controller implements Initializable {

    @FXML
    private AnchorPane anchorid;
    @FXML
    private ProgressBar pb_progress_hdd;
    @FXML
    private Button button_checkResults;
    @FXML
    private final MockBench testBench;
    @FXML
    private TextField tf_path;
    @FXML
    private Button button_browse, button_start, button_cancel;
    @FXML
    private ComboBox<String> cb_ioSize_1 , cb_ioSize_2, cb_fileSize;

    private ObservableList<String> io_list = FXCollections.observableArrayList("512 B", "1 KB", "2 KB", "4 KB", "8 KB",
            "16 KB", "32 KB", "64 KB", "128 KB", "256 KB", "512 KB", "1 MB", "2 MB", "4 MB", "8 MB", "16 MB", "32 MB", "64 MB");
    private  ObservableList<String> fs_list = FXCollections.observableArrayList("64 KB", "128 KB", "256 KB", "512 KB",
            "1 MB", "2 MB","4 MB", "8 MB", "16 MB", "32 MB", "64 MB", "128 MB", "256 MB", "512 MB", "1 GB", "2 GB", "4 GB",
            "8 GB", "16 GB", "32 GB");
    @FXML
    private Text text_progress_hdd_tracker;

    public HDD_Controller()
    {

        testBench = new MockBench();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cb_ioSize_1.setItems(io_list);
        cb_ioSize_2.setItems(io_list);
        cb_fileSize.setItems(fs_list);
        button_checkResults.setVisible(false);
        button_cancel.setVisible(false);

    }

    public void chooseDirectory(ActionEvent event)
    {

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage)anchorid.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if(file != null)
        {
            System.out.println("Path : "+file.getAbsolutePath());
            tf_path.setText(file.getAbsolutePath());
        }
    }

    public void start_HDD(ActionEvent event)
    {
        pb_progress_hdd.progressProperty().bind(testBench.getRunningProgress());


        pb_progress_hdd.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                text_progress_hdd_tracker.textProperty().setValue(Integer.toString((int)(number.doubleValue()*100) )+ '%');
                if(text_progress_hdd_tracker.getText().equals("99%"))
                {
                    button_checkResults.setVisible(true);
                }
            }
        });
        testBench.initialize(10);
        testBench.start();
        button_start.setVisible(false);
        button_cancel.setVisible(true);
    }
    public void stop_HDD(ActionEvent event)
    {
        testBench.cancel();
        //text_progress_hdd_tracker.textProperty().setValue("");
        //pb_progress_hdd.progressProperty().set(0);
    }


}
