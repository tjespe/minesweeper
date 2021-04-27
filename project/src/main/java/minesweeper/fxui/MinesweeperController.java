package minesweeper.fxui;

import java.io.IOException;
import java.util.Collection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert.AlertType;
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
import javafx.stage.StageStyle;
import javafx.stage.Window;
import minesweeper.model.Board;
import minesweeper.model.ReadAndWriteBoard;
import minesweeper.model.ReadAndWriteHighscoreList;
import minesweeper.model.DifficultyLevel;
import minesweeper.model.Field;
import minesweeper.model.HighscoreList;
import minesweeper.model.StopwatchListener;

public class MinesweeperController implements StopwatchListener {

	private Board board;
	protected HighscoreList highscores;

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
			board = new ReadAndWriteBoard().readFromFile();
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
		try {
			highscores = new ReadAndWriteHighscoreList().readFromFile();
		} catch (Exception e) {
			highscores = new HighscoreList();
		}
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

		if (board.getStatus() != Board.PLAYING)
			try {
				new ReadAndWriteBoard().deleteFile();
			} catch (IOException e) {
				// In this case, this exception is not fatal so we don't need to handle it
			}

		if (board.getStatus() == Board.WON) {
			try {
				this.showGameWonModal();
			} catch (IOException e) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("An error occurred!");
				alert.setHeaderText("An error occurred!");
				alert.setContentText(
						"You won the game, and a modal for saving your score should have been shown, but an unexpected error occurred.");
				alert.showAndWait();
			}
		} else if (board.getStatus() == Board.LOST) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("You lost!");
			alert.setHeaderText("You lost");
			alert.setContentText("Click OK to restart!");
			alert.showAndWait();
			this.newGameWithSelectedLevel();
			this.drawBoard();
		}

		Scene scene = rootPane.getScene();
		if (scene != null) {
			Stage stage = (Stage) scene.getWindow();
			stage.sizeToScene();
		}
	}

	private void drawBoardField(int col, int row) {
		StackPane field = new StackPane();
		field.setPrefWidth(30);
		field.setPrefHeight(30);
		char fieldStatus = board.getFieldStatus(col, row);
		field.setStyle("-fx-padding: 0; -fx-margin: 0; -fx-background-color: " + getFieldStyle(fieldStatus, col, row));
		if (fieldStatus == Field.BOMB) {
			try {
				Image imgBomb = new Image("/bomb.png");
				ImageView imgBombView = new ImageView(imgBomb);
				field.getChildren().add(imgBombView);
			} catch (Exception e) {
				Text fallback = new Text("💣");
				field.getChildren().add(fallback);
			}
		}
		if (fieldStatus == Field.FLAGGED || fieldStatus == Field.FLAGGED_WITH_BOMB) {
			try {
				Image imgRedFlag = new Image("/flag.png");
				ImageView imgRedFlagView = new ImageView(imgRedFlag);
				field.getChildren().add(imgRedFlagView);
			} catch (Exception e) {
				Text fallback = new Text("⛳");
				field.getChildren().add(fallback);
			}
		}

		if (board.countAdjacentBombs(col, row) != 0 && fieldStatus == Field.OPENED) {
			Text numOfAdjacentBombs = new Text();
			int adjacentBombNum = board.countAdjacentBombs(col, row);
			if (adjacentBombNum == 1) {
				numOfAdjacentBombs.setFill(Color.BLUE);
			} else if (adjacentBombNum == 2) {
				numOfAdjacentBombs.setFill(Color.GREEN);
			} else if (adjacentBombNum == 3) {
				numOfAdjacentBombs.setFill(Color.RED);
			} else if (adjacentBombNum == 4) {
				numOfAdjacentBombs.setFill(Color.DARKORCHID);
			}
			numOfAdjacentBombs.setText(String.valueOf(board.countAdjacentBombs(col, row)));
			numOfAdjacentBombs.setFont(Font.font("Verdana", FontWeight.BOLD, 18));
			StackPane.setAlignment(numOfAdjacentBombs, Pos.CENTER);
			field.getChildren().add(numOfAdjacentBombs);
		}
		boardParent.add(field, col + 1, row + 1);
	}

	@FXML
	public void newGameWithSelectedLevel() {
		if (board != null) {
			board.removeStopwatchListener(this);
		}
		DifficultyLevel level = DifficultyLevel.getByLabel(dropDown.getValue());
		if (level == null)
			level = DifficultyLevel.NORMAL;
		board = new Board(level);
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
			new ReadAndWriteBoard().writeToFile(board);
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("HighscoreList.fxml"));
		Parent highscoreListScene = loader.load();
		HighscoreListController highscoreListController = loader.getController();
		highscoreListController.setParentController(this);
		Stage newWindow = new Stage();
		newWindow.setTitle("Higscores");
		newWindow.setScene(new Scene(highscoreListScene));
		Scene rootScene = rootPane.getScene();
		if (rootScene != null) {
			Window rootStage = rootScene.getWindow();
			newWindow.initOwner(rootStage);
		}
		newWindow.setResizable(false);
		newWindow.initModality(Modality.WINDOW_MODAL);
		newWindow.show();
	}

	private void showGameWonModal() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("GameWon.fxml"));
		Parent gameWonScene = loader.load();
		GameWonController childController = loader.getController();
		if (childController != null)
			childController.setParentController(this);
		Stage newWindow = new Stage();
		newWindow.setTitle("You won!");
		newWindow.setScene(new Scene(gameWonScene));
		newWindow.setResizable(false);
		newWindow.initStyle(StageStyle.UNDECORATED);

		Scene rootScene = rootPane.getScene();
		if (rootScene != null) {
			Window rootStage = rootScene.getWindow();
			newWindow.initOwner(rootStage);
		}
		newWindow.setResizable(false);
		newWindow.initModality(Modality.WINDOW_MODAL);
		newWindow.show();
	}

	@Override
	public void timeChanged(String newTimeValue) {
		timer.setText(newTimeValue);
	}

	protected String getCurrentTime() {
		return timer.getText();
	}

	protected DifficultyLevel getCurrentDifficultyLevel() {
		return DifficultyLevel.getByLabel(dropDown.getValue());
	}

	private void updateFlagCount() {
		numOfFlagsLeft.setText(String.valueOf(board.getRemainingFlags()));
	}

	@Override
	public void timeIsUp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("You lost!");
		alert.setHeaderText("Time is up!");
		alert.setContentText(
				"You have spent more than 1 hour, which is the maximum time allowed. Click OK to restart.");
		alert.setOnHidden(evt -> {
			this.newGameWithSelectedLevel();
			this.drawBoard();
		});
		alert.show();
	}
}
