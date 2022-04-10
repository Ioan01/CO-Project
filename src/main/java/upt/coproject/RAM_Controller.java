package upt.coproject;

import bench.Benchmark;
import bench.HDD_Benchmark;
import bench.RAM_Benchmark;
import javafx.event.ActionEvent;

import java.io.IOException;


public class RAM_Controller extends Controller{

    Benchmark ram = new RAM_Benchmark();

    public void start_RAM(ActionEvent event)
    {
        ram.run();
    }
}
