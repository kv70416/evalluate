package mainapp.controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class ErrorWindowGUIController {
    public AnchorPane errorPane = null;
    public Text errorText = null;
    public Button okBtn = null;

    private String errorMsg = null;

    public ErrorWindowGUIController(String msg) {
        errorMsg = msg;
    }

    public void initialize() {
        AnchorPane.setLeftAnchor(errorText, 12.0);
        AnchorPane.setTopAnchor(errorText, 12.0);
        AnchorPane.setBottomAnchor(okBtn, 12.0);
        AnchorPane.setRightAnchor(okBtn, 12.0);

        errorText.setText(errorMsg);

        okBtn.setOnAction(ev -> {
            okBtn.getScene().getWindow().hide();
        });
    }
}
