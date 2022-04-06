package upt.coproject;

import javafx.event.ActionEvent;
import javafx.scene.Node;

public class MainPageController extends Controller{

    public void switchScene(ActionEvent event)
    {
        Node source = (Node) event.getSource();
        Controller.changePage((String) source.getUserData());
    }

}
