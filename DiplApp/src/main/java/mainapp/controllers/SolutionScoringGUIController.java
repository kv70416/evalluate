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
import mainapp.PhaseMediator;
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
    
    private PhaseMediator mediator = null;
    private SolutionScoringService ssService = null;
    private SolutionScoringConfiguration ssConfig = null;
    
    private String viewedModuleID = null;
    
    public SolutionScoringGUIController(PhaseMediator med, SolutionScoringService service, SolutionScoringConfiguration config) {
        mediator = med;
        ssService = service;
        ssConfig = config;
    }

    private boolean configurationCheck() {
        return ssService != null && ssConfig != null && ssService.isConfigurationValid(ssConfig);
    }


    public void initialize() {
        if (ssService == null || ssConfig == null) {
            return; // TODO error
        }

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
                return; // TODO error
            }
        }

        ssModuleConfirmBtn.setOnAction(ev -> {
            int index = ssConfig.addModule(ssService, this.viewedModuleID);
            
            Tab newTab = new Tab("Module #" + Integer.toString(index + 1));
            newTab.setContent(ssService.getModuleGUI(index, mediator.getMainWindow(), () -> { mediator.updateForConfigStatus(configurationCheck()); }));
            newTab.setClosable(true);
            newTab.setUserData(index);
            newTab.setOnClosed(evt -> {
                Object data = newTab.getUserData();
                ssConfig.removeModule(ssService, (int)data);

                setComboVisibility(ssConfig.getSelectedModules().size() > 1);
                mediator.updateForConfigStatus(configurationCheck());
            });

            ssSubmenuTabs.getTabs().add(newTab);
            setComboVisibility(ssConfig.getSelectedModules().size() > 1);
            mediator.updateForConfigStatus(configurationCheck());
        });

    }
    

    private void setComboVisibility(boolean visible) {
        comboBox.setVisible(visible);
        comboBox.setManaged(visible);
    }

    @FXML
    public void setSumAggregation(ActionEvent e) {
        ssConfig.setAggregationType(AggregationType.SUM);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
        mediator.updateForConfigStatus(configurationCheck());
    }
    
    @FXML
    public void setMinAggregation(ActionEvent e) {
        ssConfig.setAggregationType(AggregationType.MIN);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
        mediator.updateForConfigStatus(configurationCheck());
    }
    
    @FXML
    public void setMaxAggregation(ActionEvent e) {
        ssConfig.setAggregationType(AggregationType.MAX);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
        mediator.updateForConfigStatus(configurationCheck());
    }
    
    @FXML
    public void setAvgAggregation(ActionEvent e) {
        ssConfig.setAggregationType(AggregationType.AVG);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
        mediator.updateForConfigStatus(configurationCheck());
    }
    
}
