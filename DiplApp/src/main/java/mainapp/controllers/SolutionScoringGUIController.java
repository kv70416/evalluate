package mainapp.controllers;

import java.util.List;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainapp.configurations.SolutionScoringConfiguration;
import mainapp.moduleInterfaces.ISolutionScoringModule;
import mainapp.services.ModuleService;
import mainapp.services.SolutionScoringService;

public class SolutionScoringGUIController extends ModuleGUIController {
    public VBox ssBox = null;
    public MenuButton ssModuleMenu = null;
    public Button ssModuleConfirmBtn = null;
    public Label ssModuleDescription = null;
    public Label ssModuleID = null;
    public TabPane ssSubmenuTabs = null;
    
    private SolutionScoringService ssService = null;
    private SolutionScoringConfiguration ssConfig = null;
    
    private String viewedModuleID = null;
    //private String viewedModuleName = null;
    
    public SolutionScoringGUIController(SolutionScoringService service, SolutionScoringConfiguration config) {
        ssService = service;
        ssConfig = config;
    }
    
    public boolean setupSolutionScoringModuleGUI(Stage mainWindow, Runnable mainSceneRefresh) {
        if (ssService == null || ssConfig == null) {
            return false;
        }
        
        ssModuleConfirmBtn.setOnAction(ev -> {
            //if (!checkIfSelected(this.viewedModuleID)) {
                int index = ssConfig.addModule(ssService, this.viewedModuleID);
                
                Tab newTab = new Tab(Integer.toString(index));
                newTab.setClosable(true);
                newTab.setContent(ssService.getModuleGUI(index, mainWindow, mainSceneRefresh));
                newTab.setUserData(index);
                newTab.setOnClosed(evt -> {
                    Object data = newTab.getUserData();
                    ssConfig.removeModule(ssService, (int)data);
                    //ssModuleConfirmBtn.setDisable(checkIfSelected(this.viewedModuleID));
                    mainSceneRefresh.run();
                });
                ssSubmenuTabs.getTabs().add(newTab);
                
                //ssModuleConfirmBtn.setDisable(true);
                mainSceneRefresh.run();
            //}
        });
        
        List<ModuleService<ISolutionScoringModule>.ModuleInformation> infos = ssService.getAllModuleInfo();
        for (ModuleService.ModuleInformation info : infos) {
            MenuItem item = new MenuItem(info.getName());
            item.setOnAction(ev -> {
                viewedModuleID = info.getID();
                //viewedModuleName = info.getName();
                
                ssModuleMenu.setText(info.getName());
                ssModuleDescription.setText(info.getDescription());
                ssModuleID.setText(info.getID());
                
                ssModuleConfirmBtn.setDisable(false);
                //ssModuleConfirmBtn.setDisable(checkIfSelected(this.viewedModuleID));
            });
            if (!ssModuleMenu.getItems().add(item)) {
                ssModuleMenu.getItems().clear();
                return false;
            }
        }
        return true;
    }
    
    /*
    private boolean checkIfSelected(String id) {
        for (String i : ssConfig.getSelectedModules()) {
            if (i.equals(id)) {
                return true;
            }
        }
        return false;
    }
    */
    
}
