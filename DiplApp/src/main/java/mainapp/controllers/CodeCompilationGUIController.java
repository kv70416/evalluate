package mainapp.controllers;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import mainapp.configurations.CodeCompilationConfiguration;
import mainapp.moduleInterfaces.ICodeCompilationModule;
import mainapp.services.CodeCompilationService;
import mainapp.services.ModuleService;

public class CodeCompilationGUIController extends ModuleGUIController {
    public MenuButton ccModuleMenu = null;
    public Button ccModuleConfirmBtn = null;
    public Label ccModuleDescription = null;
    public Label ccModuleID = null;
    public Pane ccSubmenuPane = null;

    private CodeCompilationService ccService = null;
    private CodeCompilationConfiguration ccConfig = null;

    private String viewedModuleID = null;
    
    public CodeCompilationGUIController(CodeCompilationService service, CodeCompilationConfiguration config) {
        ccService = service;
        ccConfig = config;
    }
    
    public boolean setupCodeCompilationModuleGUI(Stage mainWindow, Runnable configRefresh) {
        if (ccService == null || ccConfig == null) {
            return false;
        }
        
        ccModuleConfirmBtn.setOnAction(ev -> {
            ccConfig.selectModule(ccService, viewedModuleID);

            showModuleGUI(ccConfig.getSelectedModuleIndex(), mainWindow, configRefresh);
            ccModuleConfirmBtn.setDisable(true);
            configRefresh.run();
        });
        
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
                return false;
            }
        }
        return true;
    }
    
    private boolean showModuleGUI(int moduleIndex, Stage mainWindow, Runnable configRefresh) {
        ccSubmenuPane.getChildren().clear();
        Node moduleGUI = ccService.getModuleGUI(moduleIndex, mainWindow, configRefresh);
        if (moduleGUI != null) {
            return ccSubmenuPane.getChildren().add(moduleGUI);
        }
        return false;
    }

}
