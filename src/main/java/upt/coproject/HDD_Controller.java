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
    private AnchorPane anchorID;
    @FXML
    private ProgressBar progressBarProgressHDD;
    @FXML
    private Button buttonCheckResults;
    @FXML
    private final MockBench testBench;
    @FXML
    private TextField textFieldPath;
    @FXML
    private Button button_browse, buttonStart, buttonCancel;
    @FXML
    private ComboBox<String> comboBoxIOSizeStart, comboBoxIOSizeEnd, comboBoxFileSize;

    private ObservableList<String> ioList = FXCollections.observableArrayList("512 B", "1 KB", "2 KB", "4 KB", "8 KB",
            "16 KB", "32 KB", "64 KB", "128 KB", "256 KB", "512 KB", "1 MB", "2 MB", "4 MB", "8 MB", "16 MB", "32 MB", "64 MB");
    private  ObservableList<String> fsList = FXCollections.observableArrayList("64 KB", "128 KB", "256 KB", "512 KB",
            "1 MB", "2 MB","4 MB", "8 MB", "16 MB", "32 MB", "64 MB", "128 MB", "256 MB", "512 MB", "1 GB", "2 GB", "4 GB",
            "8 GB", "16 GB", "32 GB");
    @FXML
    private Text textHDDProgressTracker;

    private String drivePath;
    private long fileSize, ioStart, ioEnd; // bytes

    public HDD_Controller()
    {

        testBench = new MockBench("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxIOSizeStart.setItems(ioList);
        comboBoxIOSizeEnd.setItems(ioList);
        comboBoxFileSize.setItems(fsList);
        buttonCheckResults.setVisible(false);
        buttonCancel.setVisible(false);

    }

    public void chooseDirectory(ActionEvent event)
    {

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) anchorID.getScene().getWindow();
        File file = directoryChooser.showDialog(stage);

        if(file != null)
        {
            textFieldPath.setText(file.getAbsolutePath());
            drivePath = file.getAbsolutePath();
            System.out.println("Chosen path: "+drivePath);
        }
    }

    public void start_HDD(ActionEvent event)
    {
        progressBarProgressHDD.progressProperty().bind(testBench.getRunningProgress());


        progressBarProgressHDD.progressProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                textHDDProgressTracker.textProperty().setValue(Integer.toString((int)(number.doubleValue()*100) )+ '%');
                if(textHDDProgressTracker.getText().equals("99%"))
                {
                    buttonCheckResults.setVisible(true);
                }
            }
        });
        testBench.initialize(10);
        testBench.start();
        buttonStart.setVisible(false);
        buttonCancel.setVisible(true);
    }
    public void stop_HDD(ActionEvent event)
    {
        testBench.cancel();
        //text_progress_hdd_tracker.textProperty().setValue("");
        //pb_progress_hdd.progressProperty().set(0);
    }
    public void ioStartSizeSelected(ActionEvent event)
    {
        String size = comboBoxIOSizeStart.getValue();
        ioStart = convertToBytes(size);
        System.out.println("IO start size selected: "+ioStart+" bytes");
        //if(sizeString.equals())
    }
    public void ioEndSizeSelected(ActionEvent event)
    {
        String size = comboBoxIOSizeEnd.getValue();
        ioEnd = convertToBytes(size);
        System.out.println("IO end size selected: "+ioEnd+" bytes");
    }

    public void fileSizeSelected(ActionEvent event)
    {
        String size = comboBoxFileSize.getValue();
        fileSize = convertToBytes(size);
        System.out.println("File size selected: "+fileSize+" bytes");
    }

    public long convertToBytes(String size)
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
                result = value * 1048576;
            else if (unit.equals("GB"))
                result = value * 1073741824;
        }

        return result;
    }
}
