package upt.coproject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import upt.coproject.benchmark.MockBench;
import upt.coproject.testbench.DriveTestBench;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
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
    private Button buttonStart, buttonCancel;
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
    @FXML
    private Text textHDDModel;
    @FXML
    private ComboBox comboBoxPath;
    @FXML
    private BarChart barChart;
    @FXML
    private TitledPane titledPane;

    private File drivePath;
    private long fileSize, ioStart, ioEnd; // bytes
    private String hddModel = "";
    private XYChart.Series seriesSeqWrite = new XYChart.Series(), seriesSeqRead = new XYChart.Series(), seriesRandomWrite = new XYChart.Series(), seriesRandomRead = new XYChart.Series();
    private boolean finished = false;

    public HDD_Controller()
    {

        testBench = new DriveTestBench();
    }

    @SneakyThrows
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setItemsVisibility(false);

        seriesSeqWrite = new XYChart.Series();
        seriesSeqWrite.setName("Sequential write");
        seriesSeqRead = new XYChart.Series();
        seriesSeqRead.setName("Sequential Read");
        seriesRandomWrite = new XYChart.Series();
        seriesRandomWrite.setName("Random write");
        seriesRandomRead = new XYChart.Series();
        seriesRandomRead.setName("Random read");
        //(123, 456, 678, 891, "4 KB");

        /*series1.getData().add(new XYChart.Data(123.2, "16 KB"));
        series1.getData().add(new XYChart.Data(199.12, "1 MB"));
        series1.getData().add(new XYChart.Data(250.93, "4 MB"));
        series1.getData().add(new XYChart.Data(369.5, "1 GB"));*/




        /*series2.getData().add(new XYChart.Data(143.9, "16 KB"));
        series2.getData().add(new XYChart.Data(210.1, "1 MB"));
        series2.getData().add(new XYChart.Data(289.77, "4 MB"));
        series2.getData().add(new XYChart.Data(402.5, "1 GB"));*/

        getHDDModel();
        comboBoxPath.setItems(getAvailableDisks());
        textHDDModel.setText("");
        comboBoxIOSizeStart.setItems(ioList);
        comboBoxIOSizeEnd.setItems(ioList);
        comboBoxFileSize.setItems(fsList);

        comboBoxPath.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                File file = new File(newValue);
                drivePath = file;
                System.out.println("Chosen path: "+ drivePath.getAbsolutePath());
                textHDDModel.setText(hddModel);
            }
        });

    }

    public void chooseDirectory(ActionEvent event){
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = (Stage) anchorID.getScene().getWindow();
        drivePath = directoryChooser.showDialog(stage);

        if(drivePath != null)
        {
            comboBoxPath.setValue(drivePath.getAbsolutePath());
            System.out.println("Chosen path: "+drivePath.getAbsolutePath());
            textHDDModel.setText(hddModel);
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

                        setItemsVisibility(true);
                        buttonCancel.setVisible(false);

                        setSpeed((Double) testBench.getResults().get("SEQ_WRITE"), textSeqWriteSpeed);
                        setSpeed(694, textSeqReadSpeed);
                        setSpeed((Double) testBench.getResults().get("RND_WRITE"), textRandomWriteSpeed);
                        setSpeed(65.321, textRandomReadSpeed);

                        Platform.runLater(() -> {
                            updateBarChart((Double.valueOf(textSeqWriteSpeed.textProperty().getValue())), (Double.valueOf(textSeqReadSpeed.textProperty().getValue())), (Double.valueOf(textRandomWriteSpeed.textProperty().getValue())), (Double.valueOf(textRandomReadSpeed.textProperty().getValue())), "4 KB");
                        });
                    }
                }
            });

            testBench.initialize(drivePath.getAbsolutePath(), fileSize);

            testBench.start();

            buttonCancel.setVisible(true);
            buttonStart.setVisible(false);
        }

    }
    public void stopHDD(ActionEvent event)
    {
        testBench.cancel();
        buttonCancel.setVisible(false);
        buttonStart.setVisible(true);
    }
    public void ioStartSizeSelected(ActionEvent event)
    {
        String size = comboBoxIOSizeStart.getValue();
        ioStart = convertToBytes(size);
        System.out.println("IO start size selected: "+ioStart+" bytes");
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

    public void updateBarChart(double seqWriteSpeed, double seqReadSpeed, double randomWriteSpeed, double randomReadSpeed, String filesize)
    {
        seriesSeqWrite.getData().add(new XYChart.Data(seqWriteSpeed, filesize));
        seriesSeqRead.getData().add(new XYChart.Data(seqReadSpeed, filesize));
        seriesRandomWrite.getData().add(new XYChart.Data(randomWriteSpeed, filesize));
        seriesRandomRead.getData().add(new XYChart.Data(randomReadSpeed, filesize));

        barChart.getData().addAll(seriesSeqWrite, seriesSeqRead, seriesRandomWrite, seriesRandomRead);

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

    public void setSpeed(double speed, Text textSpeed)
    {
        speed = Math.round(speed);
        textSpeed.textProperty().setValue((String.valueOf(speed)));
        //series.getData().add(new XYChart.Data(speed, "filesize"));
        //barChart.getData().addAll(series);
    }

    public void getHDDModel() throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] cmd = {"cmd","/c", "wmic diskdrive get model"};
        Process proc = rt.exec(cmd);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String res;
        while((res = stdInput.readLine()) != null)
        {
            hddModel += res;
        }
        hddModel = hddModel.trim().replaceAll(" +", " ");
        hddModel = hddModel.split(" ")[1];
    }

    public ObservableList<String> getAvailableDisks() throws IOException {
        Runtime rt = Runtime.getRuntime();
        String[] cmd = {"cmd", "/c", "fsutil fsinfo drives"};
        Process proc = rt.exec(cmd);

        ObservableList<String> disks = FXCollections.observableArrayList();

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String res;
        String[] arr = null;
        Path[] paths = null;
        while((res = stdInput.readLine()) != null)
        {
            arr = res.split(" ");
        }
        arr = Arrays.copyOfRange(arr, 1, arr.length);
        for(String item: arr)
        {
            disks.add(item);
        }

        return disks;
    }

    public void setItemsVisibility(boolean visibility)
    {
        titledPane.setExpanded(false);
        buttonCheckResults.setVisible(visibility);
        buttonCancel.setVisible(visibility);
        labelRandomReadSpeed.setVisible(visibility);
        labelRandomWriteSpeed.setVisible(visibility);
        labelSeqWriteSpeed.setVisible(visibility);
        labelSeqReadSpeed.setVisible(visibility);
        textRandomReadSpeed.setVisible(visibility);
        textRandomWriteSpeed.setVisible(visibility);
        textSeqWriteSpeed.setVisible(visibility);
        textSeqReadSpeed.setVisible(visibility);
    }
}
