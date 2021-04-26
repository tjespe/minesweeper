package minesweeper.fxui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import minesweeper.model.ReadAndWriteHighscoreList;
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
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Unexpected error!");
            alert.setHeaderText("Unexpected error!");
            alert.showAndWait();
            return;
        }
        if (name.getText().length() == 0 || name.getText().length() > 15) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Please enter a new name!");
            alert.setHeaderText("Please enter a name with maximum 15 characters!");
            alert.setContentText("Please enter a name to submit your score.");
            alert.showAndWait();
            return;
        }
        String time = parentController.getCurrentTime();
        Score score = new Score(name.getText(), time, parentController.getCurrentDifficultyLevel());
        parentController.highscores.addScore(score);
        try {
            new ReadAndWriteHighscoreList().writeToFile(parentController.highscores);
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Failed to save!");
            alert.setHeaderText("Failed to save highscore!");
            alert.setContentText("Unfortunately, an unexpected error occurred and your score could not be saved.");
            alert.showAndWait();
        }
        parentController.newGameWithSelectedLevel();
        Scene scene = rootPane.getScene();
        if (scene != null) {
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        }
    }
}
