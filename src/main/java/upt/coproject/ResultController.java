package upt.coproject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import lombok.Getter;
import lombok.Setter;
import upt.coproject.testbench.PartialResult;

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


        //scoreLabel.textProperty().bind(this.score);
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

