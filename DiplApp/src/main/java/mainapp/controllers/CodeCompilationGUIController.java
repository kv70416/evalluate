package mainapp.controllers;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import mainapp.PhaseMediator;
import mainapp.configurations.CodeCompilationConfiguration;
import mainapp.modules.interfaces.ICodeCompilationModule;
import mainapp.services.CodeCompilationService;
import mainapp.services.ModuleService;

public class CodeCompilationGUIController extends ModuleGUIController {
    public MenuButton ccModuleMenu = null;
    public Button ccModuleConfirmBtn = null;
    public Label ccModuleDescription = null;
    public Label ccModuleID = null;
    public Pane ccSubmenuPane = null;

    private PhaseMediator mediator = null;
    private CodeCompilationService ccService = null;
    private CodeCompilationConfiguration ccConfig = null;

    private String viewedModuleID = null;
    
    public CodeCompilationGUIController(PhaseMediator med, CodeCompilationService service, CodeCompilationConfiguration config) {
        mediator = med;
        ccService = service;
        ccConfig = config;
    }

    private boolean configurationCheck() {
        return ccService != null && ccConfig != null && ccService.isConfigurationValid(ccConfig);
    }


    public void initialize() {
        if (ccService == null || ccConfig == null) {
            return; // TODO error
        }

        List<ModuleService<ICodeCompilationModule>.ModuleInformation> infos = ccService.getAllModuleInfo();
        for (ModuleService<ICodeCompilationModule>.ModuleInformation info : infos) {
            MenuItem item = new MenuItem(info.getName());
            item.setOnAction(ev -> {
                viewedModuleID = info.getID();
                
                ccModuleMenu.setText(info.getName());
                ccModuleDescription.setText(info.getDescription());
                ccModuleID.setText(info.getID());
                
                ccModuleConfirmBtn.setDisable(viewedModuleID.equals(ccConfig.getSelectedModuleID()));
            });
            if (!ccModuleMenu.getItems().add(item)) {
                ccModuleMenu.getItems().clear();
                return; // TODO error
            }
        }

        ccModuleConfirmBtn.setOnAction(ev -> {
            ccConfig.selectModule(ccService, viewedModuleID);

            showModuleGUI(ccConfig.getSelectedModuleIndex());
            ccModuleConfirmBtn.setDisable(true);
            mediator.updateForConfigStatus(configurationCheck());
        });

    }
    
    private boolean showModuleGUI(int moduleIndex) {
        ccSubmenuPane.getChildren().clear();
        Runnable configCheck = () -> {
            mediator.updateForConfigStatus(configurationCheck());
        };
        Node moduleGUI = ccService.getModuleGUI(moduleIndex, mediator.getMainWindow(), configCheck);
        if (moduleGUI != null) {
            return ccSubmenuPane.getChildren().add(moduleGUI);
        }
        return false;
    }

}
