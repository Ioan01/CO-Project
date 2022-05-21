package upt.coproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.Map;

public class ResultController extends Controller{

    @FXML
    private Label gandacelLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private TableView<PartialResult> partialResultsTable;

    private Map<String, Object> results;
    private Map<String, List<PartialResult>> partialResults;


    StringProperty score;

    @FXML
    public void initialize() {
        Map<String, Object> getFromBenchmarkPage = ( Map<String, Object>) getWindow().getUserData();
        results = (Map<String, Object>) getFromBenchmarkPage.get("results");
        partialResults = (Map<String, List<PartialResult>>) getFromBenchmarkPage.get("partialResults");
        /*
        List<PartialResult> res = partialResults.get("SEQ_READ");
        for(PartialResult part: res){
            System.out.println(part.bufferSize + " " + part.memorySize + " " + part.time + " " + part.speed);
        }//*/


        TableColumn<PartialResult, Double> speedCol = new TableColumn<>("speed [MB/s]");
        speedCol.setCellValueFactory(new PropertyValueFactory<>("speed"));
        speedCol.setMinWidth(100);
        TableColumn<PartialResult, Integer> bufferCol = new TableColumn<>("buffer [KB]");
        bufferCol.setCellValueFactory(new PropertyValueFactory<>("buffer"));
        bufferCol.setMinWidth(100);
        TableColumn<PartialResult, Integer> sizeCol = new TableColumn<>("size [KB]");
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setMinWidth(100);
        TableColumn<PartialResult, Double> timeCol = new TableColumn<>("time [ms]");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        timeCol.setMinWidth(100);

        //partialResultsTable = new TableView<>();
        partialResultsTable.setEditable(true);
        partialResultsTable.setItems(FXCollections.observableList(partialResults.get("SEQ_READ")));
        System.out.println(FXCollections.observableList(partialResults.get("SEQ_READ")));
        partialResultsTable.getColumns().addAll(speedCol, bufferCol, sizeCol, timeCol);

        this.score = new SimpleStringProperty("1234");
        scoreLabel.textProperty().bind(this.score);
    }

    @FXML
    void saveToDatabase(ActionEvent event) {
    }

    @FXML
    void goToLeaderboardPage(ActionEvent event){
        changePage("leaderboard.fxml");
    }

    double calculateScore(){
        return 0;
    }
}

