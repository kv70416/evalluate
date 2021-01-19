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
import mainapp.moduleInterfaces.IDuplicateDetectionModule;
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
    //private String viewedModuleName = null;
    
    public DuplicateDetectionGUIController(DuplicateDetectionService service, DuplicateDetectionConfiguration config) {
        ddService = service;
        ddConfig = config;
    }
    
    public boolean setupDuplicateDetectionModuleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (ddService == null || ddConfig == null) {
            return false;
        }
        
        ddModuleConfirmBtn.setOnAction(ev -> {
            //if (!checkIfSelected(this.viewedModuleID)) {
                int index = ddConfig.addModule(ddService, this.viewedModuleID);
                
                Tab newTab = new Tab(Integer.toString(index));
                newTab.setClosable(true);
                newTab.setContent(ddService.getModuleGUI(index, mainWindow, mainSceneRefresh));
                newTab.setUserData(index);
                newTab.setOnClosed(evt -> {
                    Object data = newTab.getUserData();
                    ddConfig.removeModule(ddService, (int)data);
                    //ddModuleConfirmBtn.setDisable(checkIfSelected(this.viewedModuleID));
                    mainSceneRefresh.run();
                });
                ddSubmenuTabs.getTabs().add(newTab);
                
                //ddModuleConfirmBtn.setDisable(true);
                mainSceneRefresh.run();
            //}
        });
        
        List<ModuleService<IDuplicateDetectionModule>.ModuleInformation> infos = ddService.getAllModuleInfo();
        for (ModuleService.ModuleInformation info : infos) {
            MenuItem item = new MenuItem(info.getName());
            item.setOnAction(ev -> {
                viewedModuleID = info.getID();
                //viewedModuleName = info.getName();
                
                ddModuleMenu.setText(info.getName());
                ddModuleDescription.setText(info.getDescription());
                ddModuleID.setText(info.getID());
                
                ddModuleConfirmBtn.setDisable(false);
                //ddModuleConfirmBtn.setDisable(checkIfSelected(this.viewedModuleID));
            });
            if (!ddModuleMenu.getItems().add(item)) {
                ddModuleMenu.getItems().clear();
                return false;
            }
        }
        
        return true;
    }
    
    /*
    private boolean checkIfSelected(String id) {
        for (String i : ddConfig.getSelectedModules()) {
            if (i.equals(id)) {
                return true;
            }
        }
        return false;
    }
    */
}
