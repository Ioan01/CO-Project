package upt.coproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.ImageInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Pair;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultController extends Controller{
    @FXML
    private Label gandacelLabel;
    @FXML
    private Label scoreLabel;
    @FXML
    private TableView<PartialResult> partialResultsTableSeqRead;
    @FXML
    private TableView<PartialResult> partialResultsTableRandomRead;
    @FXML
    private TableView<PartialResult> partialResultsTableSeqWrite;
    @FXML
    private TableView<PartialResult> partialResultsTableRandomWrite;
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private ImageView gandacelImage;

    private Map<String, Object> results;
    private Map<String, List<PartialResult>> partialResults;

    StringProperty score;

    public void makeTable(TableView table, String mapKey){
        table.setEditable(true);
        table.setItems(FXCollections.observableList(partialResults.get(mapKey)));

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
        table.getColumns().addAll(speedCol, bufferCol, sizeCol, timeCol);
    }

    public void selectTable(Event event){
        Object selectedItem = choiceBox.getSelectionModel().getSelectedItem();
        partialResultsTableSeqRead.setVisible(false);
        partialResultsTableRandomRead.setVisible(false);
        partialResultsTableSeqWrite.setVisible(false);
        partialResultsTableRandomWrite.setVisible(false);

        if(selectedItem.equals("Sequential Read")){
            partialResultsTableSeqRead.setVisible(true);
            System.out.println("SEQ_READ");
            System.out.println(partialResults.get("SEQ_READ").get(0).getSpeed());
        }
        else if(selectedItem.equals("Random Read")){
            partialResultsTableRandomRead.setVisible(true);
            System.out.println("RND_READ");
            System.out.println(partialResults.get("RND_READ").get(0).getSpeed());
        }
        else if(selectedItem.equals("Sequential Write")){
            partialResultsTableSeqWrite.setVisible(true);
            System.out.println("SEQ_WRITE");
        }
        else if(selectedItem.equals("Random Write")){
            partialResultsTableRandomWrite.setVisible(true);
            System.out.println("SEQ_WRITE");
        }
    }

    @FXML
    public void initialize() {
        Map<String, Object> getFromBenchmarkPage = ( Map<String, Object>) getWindow().getUserData();
        results = (Map<String, Object>) getFromBenchmarkPage.get("results");
        partialResults = (Map<String, List<PartialResult>>) getFromBenchmarkPage.get("partialResults");

        makeTable(partialResultsTableSeqRead, "SEQ_READ");
        makeTable(partialResultsTableRandomRead, "RND_READ");
        //makeTable(partialResultsTableSeqWrite, "SEQ_WRITE");
        //makeTable(partialResultsTableRandomWrite, "RND_WRITE");

        choiceBox.getItems().addAll("Sequential Read", "Random Read", "Sequential Write", "Random Write");
        choiceBox.setOnAction(this::selectTable);

        //choiceBox.setValue(choiceBox.getItems().get(0));

        int score = calculateScore();
        this.score = new SimpleStringProperty("" + score);
        scoreLabel.textProperty().bind(this.score);
        setGandacel(score);
    }

    @FXML
    void saveToDatabase(ActionEvent event) {
    }

    @FXML
    void goToLeaderboardPage(ActionEvent event){
        changePage("leaderboard.fxml");
    }

    private double logarithm(int base, double number){
        return Math.log(number) / Math.log(base);
    }

    private void setGandacel(int score){
        Map<Pair<Integer, Integer>, Pair<String, String>> tierList = new HashMap<>();
        tierList.put(new Pair<>(0, 80), new Pair<>("D_tier.jpg", "Cărăbuș"));
        tierList.put(new Pair<>(80, 90), new Pair<>("C_tier.jpg", "Furnicuță"));
        tierList.put(new Pair<>(90, 100), new Pair<>("B_tier.jpg", "Buburuză"));
        tierList.put(new Pair<>(100, 10000), new Pair<>("A_tier.jpg", "Albinuță"));

        for(Pair<Integer, Integer> key : tierList.keySet()){
            if(score > key.getKey() && score < key.getValue()){
                System.out.println(tierList.get(key));
                Image image = new Image("/" + tierList.get(key).getKey());
                gandacelImage.setImage(image);

                gandacelLabel.setText(tierList.get(key).getValue());
                return;
            }
        }
    }

    private int calculateScore(){
        final double optimalSpeed = 1500;
        double sumSpeed = 0;
        int numberOfResults = 0;
        String[] keys = {"SEQ_READ", "RND_READ", "SEQ_WRITE", "RND_WRITE"};

        for(String key: keys){
            if (results.get(key) != null){
                sumSpeed += (Double) results.get(key);
                numberOfResults++;
            }
        }
        double avgSpeed = sumSpeed / numberOfResults;

        double score = 0;

        score = logarithm(100, (avgSpeed / optimalSpeed) * 100) * 100;

        return (int) score;
    }
}

