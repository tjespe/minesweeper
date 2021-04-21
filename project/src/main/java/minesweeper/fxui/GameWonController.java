package minesweeper.fxui;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import minesweeper.model.HighscoreList;
import minesweeper.model.ReadAndWriteHighscoreList;

public class GameWonController {
    @FXML
    private TextField name;

    private MinesweeperController parentController;

    public void setParentController(MinesweeperController parentController) {
        this.parentController = parentController;
    }

    @FXML
    public void submitClickHandler(MouseEvent event) {
        System.out.println(name.getText());

    }

}
