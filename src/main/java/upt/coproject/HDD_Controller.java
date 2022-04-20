package upt.coproject;

import bench.Benchmark;
import bench.HDD_Benchmark;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.w3c.dom.Text;


public class HDD_Controller extends Controller{

    @FXML
    private ProgressBar progress_hdd = new ProgressBar();

    @FXML
    private TextField progress_hdd_done = new TextField();

    @FXML
    private Button results_hdd = new Button();

    Benchmark hdd = new HDD_Benchmark();
    double progress;

    public void initialize()
    {
        progress = 0.0;
        progress_hdd.setProgress(progress);
        results_hdd.setVisible(false);
    }

    public void goToResults(ActionEvent event)
    {
        changePage("result.fxml");
    }

    public void start_HDD(ActionEvent event)
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
                    progress_hdd_done.setText(Integer.toString((int)Math.round(progress)) + "%");
                    if(progress_hdd.getProgress() == 1)
                        results_hdd.setVisible(true);
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        progress_hdd.progressProperty().bind(task.progressProperty());
    }


}
