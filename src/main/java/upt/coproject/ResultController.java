package upt.coproject;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import lombok.Getter;
import lombok.Setter;

public class ResultController extends Controller{

    @FXML
    private Label gandacelLabel;
    @FXML
    private Label scoreLabel;


    StringProperty score;

    @FXML
    public void initialize() {
        this.score = new SimpleStringProperty((String)getWindow().getUserData());
        scoreLabel.textProperty().bind(this.score);
    }

    @FXML
    void saveToDatabase(ActionEvent event) {
    }

    @FXML
    void goToDetailedResultPage(ActionEvent event){
        changePage("detailedResult.fxml");
    }
}

