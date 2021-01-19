package mainapp.controllers;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.configurations.FileFetchingConfiguration;
import mainapp.moduleInterfaces.IFileFetchingModule;
import mainapp.services.FileFetchingService;
import mainapp.services.ModuleService;

public class FileFetchingGUIController extends ModuleGUIController {
    public VBox ffBox = null;
    public MenuButton ffModuleMenu = null;
    public Button ffModuleConfirmBtn = null;
    public Label ffModuleDescription = null;
    public Label ffModuleID = null;
    public Pane ffSubmenuPane = null;
    
    private FileFetchingService ffService = null;
    private FileFetchingConfiguration ffConfig = null;
    
    private String viewedModuleID = null;
    
    public FileFetchingGUIController(FileFetchingService service, FileFetchingConfiguration config) {
        ffService = service;
        ffConfig = config;
    }
    
    public boolean setupFileFetchingModuleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (ffService == null || ffConfig == null) {
            return false;
        }
        
        ffModuleConfirmBtn.setOnAction(ev -> {
            ffConfig.selectModule(ffService, viewedModuleID);
            
            showModuleGUI(ffConfig.getSelectedModuleIndex(), mainWindow, mainSceneRefresh);
            ffModuleConfirmBtn.setDisable(true);
            mainSceneRefresh.run();
        });
        
        List<ModuleService<IFileFetchingModule>.ModuleInformation> infos = ffService.getAllModuleInfo();
        for (ModuleService.ModuleInformation info : infos) {
            MenuItem item = new MenuItem(info.getName());
            item.setOnAction(ev -> {
                viewedModuleID = info.getID();
                
                ffModuleMenu.setText(info.getName());
                ffModuleDescription.setText(info.getDescription());
                ffModuleID.setText(info.getID());
                
                ffModuleConfirmBtn.setDisable(viewedModuleID.equals(ffConfig.getSelectedModuleID()));
            });
            if (!ffModuleMenu.getItems().add(item)) {
                ffModuleMenu.getItems().clear();
                return false;
            }
        }
        return true;
    }
    
    private boolean showModuleGUI(int moduleIndex, Stage mainWindow, Runnable mainSceneRefresh) {
        ffSubmenuPane.getChildren().clear();
        Node moduleGUI = ffService.getModuleGUI(moduleIndex, mainWindow, mainSceneRefresh);
        if (moduleGUI != null) {
            return ffSubmenuPane.getChildren().add(moduleGUI);
        }
        return false;
    }
    
}
