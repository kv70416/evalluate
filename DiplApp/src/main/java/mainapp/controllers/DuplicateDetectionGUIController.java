package mainapp.controllers;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import mainapp.configurations.DuplicateDetectionConfiguration;
import mainapp.modules.interfaces.IDuplicateDetectionModule;
import mainapp.services.DuplicateDetectionService;
import mainapp.services.ModuleService;

public class DuplicateDetectionGUIController extends ModuleGUIController {
    public Button ddModuleConfirmBtn = null;
    public TabPane ddSubmenuTabs = null;
    public MenuButton ddModuleMenu = null;
    public Label ddModuleDescription = null;
    public Label ddModuleID = null;
    
    private DuplicateDetectionService ddService = null;
    private DuplicateDetectionConfiguration ddConfig = null;
    
    private String viewedModuleID = null;
    
    public DuplicateDetectionGUIController(DuplicateDetectionService service, DuplicateDetectionConfiguration config) {
        ddService = service;
        ddConfig = config;
    }
    
    public boolean setupDuplicateDetectionModuleGUI(Stage mainWindow, Runnable configRefresh) {
        if (ddService == null || ddConfig == null) {
            return false;
        }
        
        ddModuleConfirmBtn.setOnAction(ev -> {
            int index = ddConfig.addModule(ddService, this.viewedModuleID);
            
            Tab newTab = new Tab(Integer.toString(index));
            newTab.setClosable(true);
            newTab.setContent(ddService.getModuleGUI(index, mainWindow, configRefresh));
            newTab.setUserData(index);
            newTab.setOnClosed(evt -> {
                Object data = newTab.getUserData();
                ddConfig.removeModule(ddService, (int)data);
                configRefresh.run();
            });
            ddSubmenuTabs.getTabs().add(newTab);
            
            configRefresh.run();
        });
        
        List<ModuleService<IDuplicateDetectionModule>.ModuleInformation> infos = ddService.getAllModuleInfo();
        for (ModuleService<IDuplicateDetectionModule>.ModuleInformation info : infos) {
            MenuItem item = new MenuItem(info.getName());
            item.setOnAction(ev -> {
                viewedModuleID = info.getID();
                
                ddModuleMenu.setText(info.getName());
                ddModuleDescription.setText(info.getDescription());
                ddModuleID.setText(info.getID());
                
                ddModuleConfirmBtn.setDisable(false);
            });
            if (!ddModuleMenu.getItems().add(item)) {
                ddModuleMenu.getItems().clear();
                return false;
            }
        }
        
        return true;
    }
    
}
