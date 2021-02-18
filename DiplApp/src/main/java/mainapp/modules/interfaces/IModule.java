package mainapp.modules.interfaces;

import javafx.scene.Node;
import javafx.stage.Stage;

public interface IModule {
    /**
     * 
     * @param mainWindow
     * @param mainSceneRefresh
     * @return
     */
    public Node moduleGUI(Stage mainWindow, Runnable mainSceneRefresh);

    /*

    TODO

    public boolean importConfiguration(String config);

    public String exportConfiguration();

    */

    /**
     * 
     * @return
     */
    public boolean isConfigured();
}
