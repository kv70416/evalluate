package mainapp.controllers;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import mainapp.configurations.SolutionScoringConfiguration;
import mainapp.modules.interfaces.ISolutionScoringModule;
import mainapp.results.AggregationType;
import mainapp.services.ModuleService;
import mainapp.services.SolutionScoringService;

public class SolutionScoringGUIController extends ModuleGUIController {
    public MenuButton ssModuleMenu = null;
    public Button ssModuleConfirmBtn = null;
    public Label ssModuleDescription = null;
    public Label ssModuleID = null;
    public TabPane ssSubmenuTabs = null;
    public HBox comboBox = null;
    public MenuButton comboMenu = null;
    
    private SolutionScoringService ssService = null;
    private SolutionScoringConfiguration ssConfig = null;
    
    private String viewedModuleID = null;
    
    public SolutionScoringGUIController(SolutionScoringService service, SolutionScoringConfiguration config) {
        ssService = service;
        ssConfig = config;
    }
    
    public boolean setupSolutionScoringModuleGUI(Stage mainWindow, Runnable configRefresh) {
        if (ssService == null || ssConfig == null) {
            return false;
        }
        
        ssModuleConfirmBtn.setOnAction(ev -> {
            int index = ssConfig.addModule(ssService, this.viewedModuleID);
            
            Tab newTab = new Tab("Module #" + Integer.toString(index + 1));
            newTab.setClosable(true);
            newTab.setContent(ssService.getModuleGUI(index, mainWindow, configRefresh));
            newTab.setUserData(index);
            newTab.setOnClosed(evt -> {
                Object data = newTab.getUserData();
                ssConfig.removeModule(ssService, (int)data);

                setComboVisibility(ssConfig.getSelectedModules().size() > 1);

                configRefresh.run();
            });
            ssSubmenuTabs.getTabs().add(newTab);

            setComboVisibility(ssConfig.getSelectedModules().size() > 1);
            
            configRefresh.run();
        });
        
        List<ModuleService<ISolutionScoringModule>.ModuleInformation> infos = ssService.getAllModuleInfo();
        for (ModuleService<ISolutionScoringModule>.ModuleInformation info : infos) {
            MenuItem item = new MenuItem(info.getName());
            item.setOnAction(ev -> {
                viewedModuleID = info.getID();
                
                ssModuleMenu.setText(info.getName());
                ssModuleDescription.setText(info.getDescription());
                ssModuleID.setText(info.getID());
                
                ssModuleConfirmBtn.setDisable(false);
            });
            if (!ssModuleMenu.getItems().add(item)) {
                ssModuleMenu.getItems().clear();
                return false;
            }
        }
        return true;
    }

    private void setComboVisibility(boolean visible) {
        comboBox.setVisible(visible);
        comboBox.setManaged(visible);
    }

    @FXML
    public void setSumAggregation(ActionEvent e) {
        ssConfig.setAggregationType(AggregationType.SUM);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
    }
    
    @FXML
    public void setMinAggregation(ActionEvent e) {
        ssConfig.setAggregationType(AggregationType.MIN);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
    }
    
    @FXML
    public void setMaxAggregation(ActionEvent e) {
        ssConfig.setAggregationType(AggregationType.MAX);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
    }
    
    @FXML
    public void setAvgAggregation(ActionEvent e) {
        ssConfig.setAggregationType(AggregationType.AVG);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
    }
    
}
