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
import mainapp.configurations.DuplicateDetectionConfiguration;
import mainapp.modules.interfaces.IDuplicateDetectionModule;
import mainapp.results.AggregationType;
import mainapp.services.DuplicateDetectionService;
import mainapp.services.ModuleService;

public class DuplicateDetectionGUIController extends ModuleGUIController {
    public Button ddModuleConfirmBtn = null;
    public TabPane ddSubmenuTabs = null;
    public MenuButton ddModuleMenu = null;
    public Label ddModuleDescLabel = null;
    public Label ddModuleDescription = null;
    public Label ddModuleIDLabel = null;
    public Label ddModuleID = null;
    public HBox comboBox = null;
    public MenuButton comboMenu = null;
    
    private PhaseMediator mediator = null;
    private DuplicateDetectionService ddService = null;
    private DuplicateDetectionConfiguration ddConfig = null;
    
    private String viewedModuleID = null;
    
    public DuplicateDetectionGUIController(PhaseMediator med, DuplicateDetectionService service, DuplicateDetectionConfiguration config) {
        mediator = med;
        ddService = service;
        ddConfig = config;
    }

    private boolean configurationCheck() {
        return ddService != null && ddConfig != null && ddService.isConfigurationValid(ddConfig);
    }


    public void initialize() {
        if (ddService == null || ddConfig == null) {
            return; // TODO error
        }

        ddModuleDescLabel.setMinWidth(Label.USE_PREF_SIZE);
        ddModuleIDLabel.setMinWidth(Label.USE_PREF_SIZE);

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
                return; // TODO error
            }
        }

        ddModuleConfirmBtn.setOnAction(ev -> {
            int index = ddConfig.addModule(ddService, this.viewedModuleID);
            
            Tab newTab = new Tab("Module #" + Integer.toString(index + 1));
            newTab.setContent(ddService.getModuleGUI(index, mediator.getMainWindow(), () -> { mediator.updateForConfigStatus(configurationCheck()); }));
            newTab.setClosable(true);
            newTab.setUserData(index);
            newTab.setOnClosed(evt -> {
                Object data = newTab.getUserData();
                ddConfig.removeModule(ddService, (int)data);

                setComboVisibility(ddConfig.getSelectedModules().size() > 1);
                mediator.updateForConfigStatus(configurationCheck());
            });

            ddSubmenuTabs.getTabs().add(newTab);
            setComboVisibility(ddConfig.getSelectedModules().size() > 1);
            mediator.updateForConfigStatus(configurationCheck());
        });

    }
    
    
    private void setComboVisibility(boolean visible) {
        comboBox.setVisible(visible);
        comboBox.setManaged(visible);
    }

    @FXML
    public void setSumAggregation(ActionEvent e) {
        ddConfig.setAggregationType(AggregationType.SUM);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
        mediator.updateForConfigStatus(configurationCheck());
    }
    
    @FXML
    public void setMinAggregation(ActionEvent e) {
        ddConfig.setAggregationType(AggregationType.MIN);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
        mediator.updateForConfigStatus(configurationCheck());
    }
    
    @FXML
    public void setMaxAggregation(ActionEvent e) {
        ddConfig.setAggregationType(AggregationType.MAX);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
        mediator.updateForConfigStatus(configurationCheck());
    }
    
    @FXML
    public void setAvgAggregation(ActionEvent e) {
        ddConfig.setAggregationType(AggregationType.AVG);
        comboMenu.setText(((MenuItem)e.getSource()).getText());
        mediator.updateForConfigStatus(configurationCheck());
    }

}
