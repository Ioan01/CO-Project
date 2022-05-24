package upt.coproject;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import javax.swing.text.View;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;


public class MainPageController extends Controller implements Initializable {

    @FXML
    private AnchorPane opacityPane,drawerPane;

    @FXML
    private Label drawerImage;

    @FXML
    private ImageView exit;
    @FXML
    private JFXButton jfxButtonInfo;
    @FXML
    private JFXButton jfxButtonSettings;
    @FXML
    private RadioButton radioButtonPlayMusic;
    @FXML
    private TitledPane paneSettings;
    @FXML
    private AudioClip audioClip = new AudioClip(this.getClass().getResource("cantecel.wav").toString());

    boolean musicPlaying = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //paneSettings.setVisible(false);

        exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        jfxButtonInfo.setOnMouseClicked(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://github.com/Ioan01/CO-Project"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

        jfxButtonSettings.setOnMouseClicked(event -> {
            //System.out.println("clicked settings");
            audioClip = new AudioClip(this.getClass().getResource("cantecel.wav").toString());
            audioClip.setVolume(0.1);
            audioClip.setCycleCount(AudioClip.INDEFINITE);
            audioClip.play();
        });

        opacityPane.setVisible(false);

        FadeTransition fadeTransition=new FadeTransition(Duration.seconds(0.5),opacityPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

        TranslateTransition translateTransition=new TranslateTransition(Duration.seconds(0.5),drawerPane);
        translateTransition.setByX(-600);
        translateTransition.play();

        drawerImage.setOnMouseClicked(event -> {


            opacityPane.setVisible(true);

            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0);
            fadeTransition1.setToValue(0.15);
            fadeTransition1.play();

            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5), drawerPane);
            translateTransition1.setByX(+600);
            translateTransition1.play();
        });

        opacityPane.setOnMouseClicked(event -> {



            FadeTransition fadeTransition1=new FadeTransition(Duration.seconds(0.5),opacityPane);
            fadeTransition1.setFromValue(0.15);
            fadeTransition1.setToValue(0);
            fadeTransition1.play();

            fadeTransition1.setOnFinished(event1 -> {
                opacityPane.setVisible(false);
            });


            TranslateTransition translateTransition1=new TranslateTransition(Duration.seconds(0.5),drawerPane);
            translateTransition1.setByX(-600);
            translateTransition1.play();
        });
    }

    @FXML
    public void goToHDDPage(ActionEvent event){
        //getWindow().setUserData("1000");
        changePage("scene_hdd.fxml", 1300, 800);
    }

    @FXML
    public void goToRAMPage(ActionEvent event){
        //getWindow().setUserData("3000");
        changePage("scene_ram.fxml");
    }
    @FXML
    public void goToResultsPage(ActionEvent event)
    {
        changePage("result.fxml");
    }
    @FXML
    public void exitApp(ActionEvent event)
    {
        System.exit(0);
    }
}
