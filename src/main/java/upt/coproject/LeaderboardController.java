package upt.coproject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import jdk.internal.util.xml.impl.Input;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;


public class LeaderboardController extends Controller{
    @FXML
    TableView<Result> table;
    List<Result> results;
    @FXML
    Button refreshButton;

    @SneakyThrows
    @FXML
    void getResults(ActionEvent event) {
        URL url =  new URL("http://164.92.150.184:8080/getResults");
        HttpURLConnection con = (HttpURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);// 5000 milliseconds = 5 seconds
        con.setReadTimeout(5000);

        StringBuilder responseContent = new StringBuilder();

        if(con.getResponseCode() == HttpURLConnection.HTTP_OK){
            BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();
        }
        System.out.println(responseContent);
        Gson gson = new Gson();
        results = new ArrayList<>();



        Map<String, Result> entries = gson.fromJson(responseContent.toString(), new TypeToken<Map<String, Result>>(){}.getType());

        for (String key:entries.keySet()) {
            results.add(entries.get(key));
        }

        Collections.sort(results, new Comparator<Result>() {
            @Override
            public int compare(Result o1, Result o2) {
                return o2.getScore() - o1.getScore();
            }
        });

        table.setItems(FXCollections.observableList(results));
    }

    public void initialize(){
        getResults(null);
        table.setEditable(true);
        table.setItems(FXCollections.observableList(results));

        TableColumn<Result, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(new PropertyValueFactory<>("model"));
        modelCol.setMinWidth(300);
        TableColumn<Result, Integer> fileSizeCol = new TableColumn<>("File size [KB]");
        fileSizeCol.setCellValueFactory(new PropertyValueFactory<>("fileSize"));
        fileSizeCol.setMinWidth(100);
        TableColumn<Result, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        scoreCol.setMinWidth(100);
        scoreCol.setSortType(TableColumn.SortType.DESCENDING);
        table.getColumns().addAll(modelCol, fileSizeCol, scoreCol);
    }


}
