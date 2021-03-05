package minesweeper.fxui;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;

public class MinesweeperController {

	@FXML
	private ChoiceBox<String> dropDown;
	
	public void initialize() {
		dropDown.getItems().addAll("Easy", "Normal", "Hard");
		dropDown.setValue("Normal");
	}
}
