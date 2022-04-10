package upt.coproject;

import bench.Benchmark;
import bench.HDD_Benchmark;
import bench.SSD_Benchmark;
import javafx.event.ActionEvent;

import java.io.IOException;


public class SSD_Controller extends Controller{

    Benchmark ssd = new SSD_Benchmark();

    public void start_SSD(ActionEvent event)
    {
        ssd.run();
    }

}
