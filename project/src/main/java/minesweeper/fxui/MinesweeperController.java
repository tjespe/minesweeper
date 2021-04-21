package minesweeper.fxui;

import java.io.IOException;
import java.util.Collection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import minesweeper.model.Board;
import minesweeper.model.ReadAndWriteBoard;
import minesweeper.model.DifficultyLevel;
import minesweeper.model.Field;
import minesweeper.model.StopwatchListener;

public class MinesweeperController implements StopwatchListener {

	private Board board;

	private static ReadAndWriteBoard boardSaver = new ReadAndWriteBoard();

	@FXML
	private ChoiceBox<String> dropDown;
	@FXML
	private GridPane boardParent;
	@FXML
	private Text timer;
	@FXML
	private Text highscoreLinkText;
	@FXML
	private AnchorPane rootPane;
	@FXML
	private GridPane gridPane;
	@FXML
	private Text numOfFlagsLeft;
	@FXML
	private Text saveStatus;

	public void initialize() {
		Collection<String> difficultyLabels = DifficultyLevel.getAllLabels();
		dropDown.getItems().addAll(difficultyLabels.toArray(new String[difficultyLabels.size()]));
		try {
			board = boardSaver.readFromFile();
			board.addStopwatchListener(this);
			drawBoard();
			DifficultyLevel level = board.getDifficulty();
			dropDown.setValue(level != null ? level.getLabel() : "Unknown");
			saveStatus.setText("Loaded saved game");
		} catch (IOException exception) {
			dropDown.setValue(DifficultyLevel.NORMAL.getLabel());
			newGameWithSelectedLevel();
			saveStatus.setText("Could not find a saved game");
		}
		dropDown.setOnAction(event -> {
			newGameWithSelectedLevel();
		});

	}

	private void drawBoard() {
		gridPane.setPrefWidth(board.getWidth() * 30);
		gridPane.setPrefHeight(50 + board.getHeight() * 30);
		boardParent.setLayoutX(500);
		boardParent.getChildren().clear();
		updateFlagCount();
		for (int col = 0; col < board.getWidth(); col++) {
			for (int row = 0; row < board.getHeight(); row++) {
				drawBoardField(col, row);
			}
		}

		// TODO ADD METHODS FOR LOST AND WON AND STYLE
		if (board.getStatus() == Board.WON) {
			try {
				this.showGameWonModal();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (board.getStatus() == Board.LOST) {
			// TODO gjøre kall på get time
			System.out.println("You lost");
		}

		Scene scene = rootPane.getScene();
		if (scene != null) {
			Stage stage = (Stage) scene.getWindow();
			stage.sizeToScene();
		}
	}

	private void drawBoardField(int row, int col) { // TODO divide into several methods
		StackPane field = new StackPane();
		field.setPrefWidth(30);
		field.setPrefHeight(30);
		char fieldStatus = board.getFieldStatus(row, col);
		field.setStyle("-fx-padding: 0; -fx-margin: 0; -fx-background-color: " + getFieldStyle(fieldStatus, row, col));
		if (fieldStatus == Field.BOMB) {
			Image imgBomb = new Image("/bomb.png");
			ImageView imgBombView = new ImageView(imgBomb);
			field.getChildren().add(imgBombView);
			// TODO handle fail if image doesen't load and make folder for images
		}
		if (fieldStatus == Field.FLAGGED || fieldStatus == Field.FLAGGED_WITH_BOMB) {
			Image imgRedFlag = new Image("/flag.png");
			ImageView imgRedFlagView = new ImageView(imgRedFlag);
			field.getChildren().add(imgRedFlagView);
		}

		if (board.countAdjacentBombs(row, col) != 0 && fieldStatus == Field.OPENED) {
			Text numOfAdjacentBombs = new Text();
			int adjacentBombNum = board.countAdjacentBombs(row, col);
			if (adjacentBombNum == 1) {
				numOfAdjacentBombs.setFill(Color.BLUE);
			} else if (adjacentBombNum == 2) {
				numOfAdjacentBombs.setFill(Color.GREEN);
			} else if (adjacentBombNum == 3) {
				numOfAdjacentBombs.setFill(Color.RED);
			} else if (adjacentBombNum == 4) {
				numOfAdjacentBombs.setFill(Color.DARKORCHID);
			}
			numOfAdjacentBombs.setText(String.valueOf(board.countAdjacentBombs(row, col)));
			numOfAdjacentBombs.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
			StackPane.setAlignment(numOfAdjacentBombs, Pos.CENTER);
			field.getChildren().add(numOfAdjacentBombs);
		}
		boardParent.add(field, row + 1, col + 1);
	}

	@FXML
	public void newGameWithSelectedLevel() {
		if (board != null) {
			board.removeStopwatchListener(this);
		}
		board = new Board(DifficultyLevel.getByLabel(dropDown.getValue()));
		drawBoard();
		board.addStopwatchListener(this);
		saveStatus.setText("Not saved");
	}

	@FXML
	public void boardMouseClickedHandler(MouseEvent event) {
		Node boardField = event.getPickResult().getIntersectedNode();
		while (!(boardField instanceof StackPane)) {
			boardField = boardField.getParent();
		}
		int x = GridPane.getColumnIndex(boardField).intValue();
		int y = GridPane.getRowIndex(boardField).intValue();
		if (event.getButton() == MouseButton.PRIMARY) {
			board.openField(x - 1, y - 1);
			drawBoard();
		} else if (event.getButton() == MouseButton.SECONDARY) {
			board.toggleFlag(x - 1, y - 1);
			drawBoard();
		}
		try {
			boardSaver.writeToFile(board);
			saveStatus.setText("Automatically saved");
		} catch (IOException e) {
			saveStatus.setText("Failed to save");
		}

	}

	private String getFieldStyle(char fieldStatus, int x, int y) {
		boolean dark = (x + y) % 2 == 0;
		if (fieldStatus == Field.UNOPENED || fieldStatus == Field.UNOPENED_WITH_BOMB || fieldStatus == Field.FLAGGED
				|| fieldStatus == Field.FLAGGED_WITH_BOMB) {
			if (dark)
				return "#66a103";
			return "#73b504";
		} else {
			if (dark)
				return "#b6916f";
			return "#C39B77";
		}
	}

	@FXML
	public void showHighscores() throws IOException {
		System.out.println("highscores");
		// TODO showHighscore display

		Parent highscoreScene = FXMLLoader.load(getClass().getResource("HighscoreList.fxml"));
		Stage newHighscoreWindow = new Stage();
		newHighscoreWindow.setTitle("Higscores");
		newHighscoreWindow.setScene(new Scene(highscoreScene));

		Scene rootScene = rootPane.getScene();
		if (rootScene != null) {
			Window rootStage = rootScene.getWindow();
			newHighscoreWindow.initOwner(rootStage);
		}
		newHighscoreWindow.initModality(Modality.WINDOW_MODAL);
		newHighscoreWindow.show();
	}

	public void showGameWonModal() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameWon.fxml"));
		GameWonController childController = loader.getController();
		childController.setParentController(this);
		Parent gameWonScene = loader.load();
		Stage newWindow = new Stage();
		newWindow.setTitle("You won!");
		newWindow.setScene(new Scene(gameWonScene));

		Scene rootScene = rootPane.getScene();
		if (rootScene != null) {
			Window rootStage = rootScene.getWindow();
			newWindow.initOwner(rootStage);
		}
		newWindow.initModality(Modality.WINDOW_MODAL);
		newWindow.show();
	}

	@Override
	public void timeChanged(String newTimeValue) {
		timer.setText(newTimeValue);
	}

	public void updateFlagCount() {
		numOfFlagsLeft.setText(String.valueOf(board.getRemainingFlags()));
	}
}
