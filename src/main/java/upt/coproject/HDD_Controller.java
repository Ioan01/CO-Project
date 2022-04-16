package upt.coproject;

import bench.Benchmark;
import bench.HDD_Benchmark;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;

import java.io.IOException;


public class HDD_Controller extends Controller{

    public ProgressBar progress_hdd = new ProgressBar();
    private TextField progress_hdd_done = new TextField();
    Benchmark hdd = new HDD_Benchmark();

    public void start_HDD(ActionEvent event)
    {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (int i = 1; i <= 100; i++)    {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println(i);
                    updateProgress(i , 100);
                    if (progress_hdd.getProgress() == 0.99)
                        progress_hdd_done.setText("DONE!");
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        progress_hdd.progressProperty().bind(task.progressProperty());
    }


}
