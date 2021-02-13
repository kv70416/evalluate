package mainapp.controllers;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import mainapp.PhaseMediator;
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
    
    private PhaseMediator mediator = null;
    private FileFetchingService ffService = null;
    private FileFetchingConfiguration ffConfig = null;
    
    private String viewedModuleID = null;
    
    public FileFetchingGUIController(PhaseMediator med, FileFetchingService service, FileFetchingConfiguration config) {
        mediator = med;
        ffService = service;
        ffConfig = config;
    }


    private boolean configurationCheck() {
        return ffService != null && ffConfig != null && ffService.isConfigurationValid(ffConfig);
    }


    public void initialize() {
        if (ffService == null || ffConfig == null) {
            return; // TODO error
        }

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
                return; // TODO error
            }
        }

        ffModuleConfirmBtn.setOnAction(ev -> {
            ffConfig.selectModule(ffService, viewedModuleID);
            
            showModuleGUI(ffConfig.getSelectedModuleIndex());
            ffModuleConfirmBtn.setDisable(true);
            mediator.updateForConfigStatus(configurationCheck());
        });
    }
    
    private boolean showModuleGUI(int moduleIndex) {
        ffSubmenuPane.getChildren().clear();
        Runnable configCheck = () -> {
            mediator.updateForConfigStatus(configurationCheck());
        };
        Node moduleGUI = ffService.getModuleGUI(moduleIndex, mediator.getMainWindow(), configCheck);
        if (moduleGUI != null) {
            return ffSubmenuPane.getChildren().add(moduleGUI);
        }
        return false;
    }
    
}
