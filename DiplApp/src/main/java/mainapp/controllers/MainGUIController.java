package mainapp.controllers;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class MainGUIController {
    public Pane phasePane = null;
    public Button nextBtn = null;
    public Button backBtn = null;

    public void setPreviousPhase(Runnable previousPhaseSetup) {
        backBtn.setOnAction(ev -> {
            previousPhaseSetup.run();
        });
        backBtn.setVisible(true);
        backBtn.setDisable(false);
    }

    public void setNextPhase(Runnable nextPhaseSetup) {
        nextBtn.setOnAction(ev -> {
            nextPhaseSetup.run();
        });
        nextBtn.setVisible(true);
    }

    public void removePhaseNavigation() {
        backBtn.setOnAction(ev -> {
            return;
        });
        nextBtn.setOnAction(ev -> {
            return;
        });
        backBtn.setVisible(false);
        backBtn.setDisable(true);
        nextBtn.setVisible(false);
        nextBtn.setDisable(true);
    }

    public boolean showPhaseNode(Node phaseNode) {
        boolean ret = false;
        phasePane.getChildren().clear();
        if (phaseNode != null) {
            ret = phasePane.getChildren().add(phaseNode);
        }
        Platform.requestNextPulse();
        return ret;
    }

    public void updateForConfigStatus(boolean valid) {
        nextBtn.setDisable(!valid);
    }
}
