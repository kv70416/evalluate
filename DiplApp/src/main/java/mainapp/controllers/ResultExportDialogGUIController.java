package mainapp.controllers;

import java.io.File;
import java.util.List;

import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import mainapp.PhaseMediator;
import mainapp.results.ratings.DuplicateRatings;
import mainapp.results.scores.Results;
import mainapp.services.ResultExportService;

public class ResultExportDialogGUIController {
    public AnchorPane resultExportPane = null;
    public VBox resultExportBox = null;
    public MenuButton resModuleMenu = null;
    public Button resModuleConfirmBtn = null;
    public Text statusText = null;

    private PhaseMediator mediator = null;
    private ResultExportService service = null;
    private Results results = null;
    private DuplicateRatings ratings = null;

    private String viewedModuleID = null;

    public ResultExportDialogGUIController(PhaseMediator med, ResultExportService svc, Results res, DuplicateRatings rat) {
        mediator = med;
        service = svc;
        results = res;
        ratings = rat;
	}



	public void initialize() {
        AnchorPane.setLeftAnchor(resultExportBox, 12.0);
        AnchorPane.setTopAnchor(resultExportBox, 12.0);
        AnchorPane.setBottomAnchor(resultExportBox, 12.0);
        AnchorPane.setRightAnchor(resultExportBox, 12.0);

        List<ResultExportService.ModuleInformation> infos = service.getAllModuleInfo();
        for (ResultExportService.ModuleInformation info : infos) {
            MenuItem item = new MenuItem(info.getName());
            item.setOnAction(ev -> {
                viewedModuleID = info.getID();
                
                resModuleMenu.setText(info.getName());
                
                resModuleConfirmBtn.setDisable(viewedModuleID == null);
            });
            if (!resModuleMenu.getItems().add(item)) {
                resModuleMenu.getItems().clear();
                return; // TODO error
            }
        }

        resModuleConfirmBtn.setOnAction(ev -> {
            resModuleConfirmBtn.setDisable(true);
            
            int modIndex = service.createModuleInstance(viewedModuleID);
            statusText.setText("Exporting...");
            proceedWithExport(modIndex);
            service.removeModuleInstance(modIndex); // TODO ok?
            statusText.setText("Export complete!");
        });
    }

    private boolean proceedWithExport(int modIndex) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showSaveDialog(mediator.getMainWindow());
        if (selectedFile == null) {
            return false;
        }

        service.setExportPath(modIndex, selectedFile.getAbsolutePath());

        return service.runExport(modIndex, results, ratings);
    }

}
