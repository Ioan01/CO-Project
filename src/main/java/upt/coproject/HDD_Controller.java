package upt.coproject;

import bench.Benchmark;
import bench.HDD_Benchmark;
import javafx.event.ActionEvent;

import java.io.IOException;


public class HDD_Controller extends Controller{

    Benchmark hdd = new HDD_Benchmark();

    public void start_HDD(ActionEvent event)
    {
        hdd.run();
    }
}
