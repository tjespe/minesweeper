package minesweeper.fxui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import minesweeper.model.Score;

public class GameWonController {
    @FXML
    private AnchorPane rootPane;
    @FXML
    private TextField name;

    private MinesweeperController parentController;

    public void setParentController(MinesweeperController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void submitClickHandler(MouseEvent event) {
        if (parentController == null) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Unexpected error!");
            alert.setHeaderText("Unexpected error!");
            alert.showAndWait();
            return;
        }
        String time = parentController.getCurrentTime();
        Score score = new Score(name.getText(), time);
        parentController.highscores.addScore(score);
        Scene scene = rootPane.getScene();
        if (scene != null) {
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        }
    }

}
