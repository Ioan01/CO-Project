package upt.coproject;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import upt.coproject.benchmark.MockBench;
import upt.coproject.testbench.DriveTestBench;

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
    private final DriveTestBench testBench;
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
    private Text textHDDProgressTracker, textRandomWriteSpeed, textRandomReadSpeed, textSeqWriteSpeed, textSeqReadSpeed;
    @FXML
    private Label labelSeqReadSpeed, labelSeqWriteSpeed, labelRandomReadSpeed, labelRandomWriteSpeed;

    private File drivePath;
    private long fileSize, ioStart, ioEnd; // bytes

    public HDD_Controller()
    {

        testBench = new DriveTestBench();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboBoxIOSizeStart.setItems(ioList);
        comboBoxIOSizeEnd.setItems(ioList);
        comboBoxFileSize.setItems(fsList);
        buttonCheckResults.setVisible(false);
        buttonCancel.setVisible(false);
        labelRandomReadSpeed.setVisible(false);
        labelRandomWriteSpeed.setVisible(false);
        labelSeqWriteSpeed.setVisible(false);
        labelSeqReadSpeed.setVisible(false);
        textRandomReadSpeed.setVisible(false);
        textRandomWriteSpeed.setVisible(false);
        textSeqWriteSpeed.setVisible(false);
        textSeqReadSpeed.setVisible(false);

    }

    public void chooseDirectory(ActionEvent event)
    {

        final DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) anchorID.getScene().getWindow();
        drivePath = directoryChooser.showDialog(stage);

        if(drivePath != null)
        {
            textFieldPath.setText(drivePath.getAbsolutePath());
            System.out.println("Chosen path: "+drivePath.getAbsolutePath());
        }
    }

    public void startHDD(ActionEvent event)
    {
        if(drivePath == null || !drivePath.exists())
        {
            displayError("Invalid path!");
        }
        else if(ioStart == 0 || ioEnd == 0)
        {
            displayError("Select start and end size!");
        }
        else if(ioStart > ioEnd)
        {
            displayError("Starting size must be smaller!");
        }
        else if(fileSize == 0)
        {
            displayError("Select file size!");
        }
        else {
            progressBarProgressHDD.progressProperty().bind(testBench.getRunningProgress());


            testBench.getFinished().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                    if (t1) {
                        buttonCancel.setVisible(false);
                        buttonStart.setVisible(true);
                        buttonCancel.setVisible(false);
                        buttonStart.setVisible(true);
                        buttonCheckResults.setVisible(true);
                        labelRandomWriteSpeed.setVisible(true);
                        labelRandomReadSpeed.setVisible(true);
                        labelSeqReadSpeed.setVisible(true);
                        labelSeqWriteSpeed.setVisible(true);
                        textRandomWriteSpeed.setVisible(true);
                        textRandomReadSpeed.setVisible(true);
                        textSeqReadSpeed.setVisible(true);
                        textSeqWriteSpeed.setVisible(true);
                        setSeqWriteSpeed((Double) testBench.getResults().get("SEQ_WRITE"));
                    }
                }
            });

            testBench.initialize(drivePath.getAbsolutePath(), ioStart, ioEnd, (int) fileSize);

            testBench.start();


            buttonCancel.setVisible(true);
            buttonStart.setVisible(false);
        }

    }
    public void stopHDD(ActionEvent event)
    {
        testBench.cancel();
        //text_progress_hdd_tracker.textProperty().setValue("");
        //pb_progress_hdd.progressProperty().set(0);
        buttonCancel.setVisible(false);
        buttonStart.setVisible(true);
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
                result = value * 1024*1024;
            else if (unit.equals("GB"))
                result = value * 1024*1024*1024;
        }

        return result;
    }

    public double setSeqReadSpeed(double speed) // MB/s
    {
        double result = 1;
        return result;
    }

    public void setSeqWriteSpeed(double speed) // MB/s
    {
        speed = Math.round(speed);
        textSeqWriteSpeed.textProperty().setValue((String.valueOf(speed)) + " MB/s");
    }

    public double setRandomReadSpeed(double speed) // MB/s
    {
        double result = 1;
        return result;
    }

    public double setRandomWriteSpeed(double speed) // MB/s
    {
        double result = 1;
        return result;
    }
}
