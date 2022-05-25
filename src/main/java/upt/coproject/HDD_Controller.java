package upt.coproject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Pair;
import lombok.SneakyThrows;
import upt.coproject.testbench.DriveTestBench;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;


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
    private ComboBox<String> comboBoxFileSize;

    private  ObservableList<String> fsList = FXCollections.observableArrayList(
            "512 KB", "1 MB", "2 MB","4 MB", "8 MB", "16 MB", "32 MB", "64 MB", "128 MB", "256 MB", "512 MB", "1 GB");
    @FXML
    private Text textRunningProgress, textRandomWriteSpeed, textRandomReadSpeed, textSeqWriteSpeed, textSeqReadSpeed;
    @FXML
    private Label labelSeqReadSpeed, labelSeqWriteSpeed, labelRandomReadSpeed, labelRandomWriteSpeed;
    @FXML
    private Text textHDDModel;
    @FXML
    private ComboBox comboBoxPath;
    @FXML
    private BarChart barChart;
    @FXML
    private XYChart.Series seriesSeqWrite = new XYChart.Series(), seriesSeqRead = new XYChart.Series(), seriesRandomWrite = new XYChart.Series(), seriesRandomRead = new XYChart.Series();
    private TitledPane titledPane;

    private File drivePath;
    private long fileSize; // bytes
    private String hddModel = "";
    List<Pair<String, List<Character>>> disks = new ArrayList<>(); // model and partitions

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

        comboBoxPath.setItems(getAvailableDisks());
        textHDDModel.setText("");
        comboBoxFileSize.setItems(fsList);

        buildDiskList();

        comboBoxPath.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                File file = new File(newValue);
                drivePath = file;
                System.out.println("Chosen path: "+ drivePath.getAbsolutePath());
                getHDDModel();
                System.out.println(hddModel);
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
                        setSpeed((Double) testBench.getResults().get("SEQ_READ"), textSeqReadSpeed);
                        setSpeed((Double) testBench.getResults().get("RND_WRITE"), textRandomWriteSpeed);
                        setSpeed((Double) testBench.getResults().get("RND_READ"), textRandomReadSpeed);

                        Platform.runLater(() -> {
                            updateBarChart((Double.valueOf(textSeqWriteSpeed.textProperty().getValue())), (Double.valueOf(textSeqReadSpeed.textProperty().getValue())), (Double.valueOf(textRandomWriteSpeed.textProperty().getValue())), (Double.valueOf(textRandomReadSpeed.textProperty().getValue())));
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

    public void setSpeed(double speed, Text textSpeed)
    {
        speed = Math.round(speed);
        textSpeed.textProperty().setValue((String.valueOf(speed)));
    }

    @SneakyThrows
    public void buildDiskList(){
        Runtime rt = Runtime.getRuntime();
        String[] cmd = {"Powershell", "Get-Disk | ft -AutoSize"};
        Process proc = rt.exec(cmd);

        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        List<String> output = new ArrayList<>();
        String line;
        while((line = stdInput.readLine()) != null)
        {
            output.add(line);
        }
        int nameStart = 0;
        int nameEnd = 0;
        nameStart = output.get(1).indexOf("Friendly Name");
        nameEnd = output.get(1).indexOf("Serial Number");

        boolean start = false;

        for (int i = 0; i <output.size(); i++){
            line = output.get(i);
            System.out.println(line);
            if (!start && line.length() > 0 && line.substring(0, 1).equals("-")){
                start = true;
                continue;
            }
            if(!start)
                continue;
            //System.out.println("XXXX" + output.get(i));
            if (!start && line.length() > 0 && line.substring(0, 1).equals("-")){
                start = true;
                continue;
            }
            if(!start)
                continue;
            if(nameEnd > output.get(i).length())
                continue;
            disks.add(new Pair<>(output.get(i).substring(nameStart, nameEnd).trim(), new ArrayList<>()));
        }

        String[] cmd_part = {"Powershell", "Get-Partition | ft DiskPath, DriveLetter"};
        Process proc_part = rt.exec(cmd_part);

        stdInput = new BufferedReader(new InputStreamReader(proc_part.getInputStream()));
        output = new ArrayList<>();
        while((line = stdInput.readLine()) != "" && line != null) {
            output.add(line);
            //System.out.println(line);
        }
        nameStart = output.get(1).indexOf("DriveLetter");

        String prevDiskPath = null;
        int diskIndex = -1;

        start = false;

        for (int i = 0; i <output.size(); i++){
            line = output.get(i);
            System.out.println(line);
            if (!start && line.length() > 0 && line.substring(0, 1).equals("-")){
                start = true;
                continue;
            }
            if(!start)
                continue;
            if(line.indexOf(' ') == -1 || line.indexOf("prod") == -1)
                continue;
            String diskPath = line.substring(line.indexOf("prod"), line.indexOf(' '));
            if(!diskPath.equals(prevDiskPath)){
                diskIndex++;
                prevDiskPath = diskPath;
            }

            String letter = line.substring(line.indexOf(' ')).trim();
            if(!letter.equals("")){
                disks.get(diskIndex).getValue().add(letter.charAt(0));
            }
        }

        for(int i = 0 ; i< disks.size(); i++){
            System.out.print(disks.get(i).getKey() + ": ");
            for(Character letter: disks.get(i).getValue())
                System.out.print(letter + " ");
            System.out.println();
        }
    }

    private void getHDDModel(){
        hddModel = "unknown";
        Character partition = drivePath.getAbsolutePath().charAt(0);
        for(int i = 0 ; i< disks.size(); i++){
            for(Character letter: disks.get(i).getValue()){
                if(letter.equals(partition)){
                    hddModel = disks.get(i).getKey();
                }
            }
        }
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
        //titledPane.setExpanded(false);
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

    public void updateBarChart(double seqWriteSpeed, double seqReadSpeed, double randomWriteSpeed, double randomReadSpeed)
    {
        seriesSeqWrite.getData().add(new XYChart.Data(seqWriteSpeed, comboBoxFileSize.getValue()));
        seriesSeqRead.getData().add(new XYChart.Data(seqReadSpeed, comboBoxFileSize.getValue()));
        seriesRandomWrite.getData().add(new XYChart.Data(randomWriteSpeed, comboBoxFileSize.getValue()));
        seriesRandomRead.getData().add(new XYChart.Data(randomReadSpeed, comboBoxFileSize.getValue()));

        System.out.println(comboBoxFileSize.getValue());

        barChart.getData().addAll(seriesSeqWrite, seriesSeqRead, seriesRandomWrite, seriesRandomRead);

    }

    @FXML
    public void goToResults(ActionEvent event){
        Map<String, Object> sendToResultPage = new HashMap<>();
        sendToResultPage.put("results", testBench.getResults());
        sendToResultPage.put("partialResults", testBench.getPartialResults());
        sendToResultPage.put("model", hddModel);
        sendToResultPage.put("fileSize", fileSize);
        getWindow().setUserData(sendToResultPage);
        changePage("result.fxml", 1300 ,800);
    }
}
