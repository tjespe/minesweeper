package minesweeper.fxui;

import java.util.ArrayList;
import java.util.Collection;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import minesweeper.model.Score;

public class HighscoreListController {
	@FXML
	private ListView<String> highscores;

	@FXML
	private GridPane highscoreTable;

	private MinesweeperController parentController;

	public void setParentController(MinesweeperController parentController) {
		this.parentController = parentController;
		this.initialize();
	}

	public void initialize() {
		loadHighscoreList();
	}

	private void loadHighscoreList() {
		if (parentController == null)
			return;
		Collection<Score> topFive = new ArrayList<>(
				parentController.highscores.getBestScores(parentController.getCurrentDifficultyLevel()));
		int i = 0;
		for (Score score : topFive) {
			Text name = new Text(score.getName());
			Text time = new Text(score.getTime());
			name.setFont(Font.font("Verdana", 12));
			time.setFont(Font.font("Verdana", 12));
			highscoreTable.add(name, 0, i);
			highscoreTable.add(time, 1, i);
			i++;
		}
	}
}
