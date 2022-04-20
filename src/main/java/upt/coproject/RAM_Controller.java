package upt.coproject;

import bench.Benchmark;
import bench.RAM_Benchmark;
import bench.SSD_Benchmark;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;


public class RAM_Controller extends Controller{

    @FXML
    private ProgressBar progress_ram = new ProgressBar();

    @FXML
    private TextField progress_ram_done = new TextField();

    @FXML
    private Button results_ram = new Button();

    Benchmark ram = new RAM_Benchmark();
    double progress;

    public void initialize()
    {
        progress = 0.0;
        progress_ram.setProgress(progress);
        results_ram.setVisible(false);
    }


    public void start_RAM(ActionEvent event)
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
                    progress_ram_done.setText(Integer.toString((int)Math.round(progress)) + "%");
                    if(progress_ram.getProgress() == 1)
                        results_ram.setVisible(true);
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        progress_ram.progressProperty().bind(task.progressProperty());
    }
    public void goToResults(ActionEvent event)
    {
        changePage("result.fxml");
    }


}
