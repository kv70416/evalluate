package mainapp.controllers;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mainapp.configurations.FileFetchingConfiguration;
import mainapp.modules.interfaces.IFileFetchingModule;
import mainapp.services.FileFetchingService;
import mainapp.services.ModuleService;

public class FileFetchingGUIController extends ModuleGUIController {
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
    
    public boolean setupFileFetchingModuleGUI(Stage mainWindow, Runnable configRefresh) {
        if (ffService == null || ffConfig == null) {
            return false;
        }
        
        ffModuleConfirmBtn.setOnAction(ev -> {
            ffConfig.selectModule(ffService, viewedModuleID);
            
            showModuleGUI(ffConfig.getSelectedModuleIndex(), mainWindow, configRefresh);
            ffModuleConfirmBtn.setDisable(true);
            configRefresh.run();
        });
        
        List<ModuleService<IFileFetchingModule>.ModuleInformation> infos = ffService.getAllModuleInfo();
        for (ModuleService<IFileFetchingModule>.ModuleInformation info : infos) {
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
    
    private boolean showModuleGUI(int moduleIndex, Stage mainWindow, Runnable configRefresh) {
        ffSubmenuPane.getChildren().clear();
        Node moduleGUI = ffService.getModuleGUI(moduleIndex, mainWindow, configRefresh);
        if (moduleGUI != null) {
            return ffSubmenuPane.getChildren().add(moduleGUI);
        }
        return false;
    }
    
}
