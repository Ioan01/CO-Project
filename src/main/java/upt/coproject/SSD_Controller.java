package upt.coproject;

import bench.Benchmark;
import bench.SSD_Benchmark;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;


public class SSD_Controller extends Controller{

    @FXML
    private ProgressBar progress_ssd = new ProgressBar();

    @FXML
    private TextField progress_ssd_done = new TextField();

    @FXML
    private Button results_ssd = new Button();

    Benchmark ssd = new SSD_Benchmark();
    double progress;

    public void initialize()
    {
        progress = 0.0;
        progress_ssd.setProgress(progress);
        results_ssd.setVisible(false);
    }

    public void goToResults(ActionEvent event)
    {
        changePage("result.fxml");
    }

    public void start_SSD(ActionEvent event)
    {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (progress = 1; progress <= 100; progress++){
                    try {
                        //progress = i;
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(progress);
                    updateProgress(progress, 100.0);
                    //progress_hdd.setProgress(progress);
                    progress_ssd_done.setText(Integer.toString((int)Math.round(progress)) + "%");
                    if(progress_ssd.getProgress() == 1)
                        results_ssd.setVisible(true);
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        progress_ssd.progressProperty().bind(task.progressProperty());
    }
}
