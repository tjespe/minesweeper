package minesweeper.fxui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class HighscoreListController {
	
	@FXML
	private ListView<String> highscores;
	
	
	private MinesweeperController parentController;

    public void setParentController(MinesweeperController parentController) {
        this.parentController = parentController;
    }
	
    public void initialize() {
		loadHighscoreList();
	}
	
	private void loadHighscoreList() {
		
		highscores.getItems().add("Item 1");
		highscores.getItems().add("Item 2");
		highscores.getItems().add("Item 3");		
	}
}
