package mainapp.modules.interfaces;

import javafx.scene.Node;
import javafx.stage.Stage;

public interface IModule {
    public Node moduleGUI(Stage mainWindow, Runnable mainSceneRefresh);
    public boolean isConfigured();
}
