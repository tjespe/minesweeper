package minesweeper.fxui;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.stage.Stage;
import minesweeper.model.Board;
import minesweeper.model.Field;
import minesweeper.model.StopwatchListener;

public class MinesweeperController implements StopwatchListener {

	private Board board;

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

	public void initialize() {
		dropDown.getItems().addAll(Board.EASY, Board.NORMAL, Board.HARD);
		dropDown.setOnAction(event -> {
			applyDifficultyLevel();
		});
		dropDown.setValue(Board.NORMAL);
		applyDifficultyLevel();

	}

	private void drawBoard() {
		boardParent.setLayoutX(500);
		boardParent.getChildren().clear();
		updateFlagCount();
		for (int col = 0; col < board.getWidth(); col++) {
			for (int row = 0; row < board.getHeight(); row++) {
				createBoardField(col, row);
			}
		}

		// TODO ADD METHODS FOR LOST AND WON AND STYLE
		if (board.getStatus() == Board.WON) {
			System.out.println("You won");
		} else if (board.getStatus() == Board.LOST) {
			// TODO gjøre kall på get time
			System.out.println("You lost");
		}
	}

	private void createBoardField(int row, int col) { // TODO divide into several methods
		StackPane field = new StackPane();
		field.setPrefWidth(30);
		field.setPrefHeight(30);
		field.setStyle("-fx-padding: 0; -fx-margin: 0; -fx-background-color: "
				+ getFieldStyle(board.getFieldStatus(row, col), row, col));
		if (board.getFieldStatus(row, col) == Field.BOMB) {
			Image imgBomb = new Image("/bomb.png");
			ImageView imgBombView = new ImageView(imgBomb);
			field.getChildren().add(imgBombView);
			// TODO handle fail if image doesen't load and make folder for images
		}
		if (board.getFieldStatus(row, col) == Field.FLAGGED) {
			Image imgRedFlag = new Image("/flag.png");
			ImageView imgRedFlagView = new ImageView(imgRedFlag);
			field.getChildren().add(imgRedFlagView);
		}

		if (board.countAdjacentBombs(row, col) != 0 && board.getFieldStatus(row, col) == Field.OPENED) {
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
	public void applyDifficultyLevel() {
		if (board != null) {
			board.removeStopwatchListener(this);
		}
		board = new Board(dropDown.getValue());
		gridPane.setPrefWidth(board.getWidth() * 30);
		gridPane.setPrefHeight(50 + board.getHeight() * 30);
		drawBoard();
		Scene scene = rootPane.getScene();
		if (scene != null) {
			Stage stage = (Stage) scene.getWindow();
			stage.sizeToScene();
		}
		board.addStopwatchListener(this);
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

	}

	private String getFieldStyle(char fieldStatus, int x, int y) {
		boolean dark = (x + y) % 2 == 0;
		if (fieldStatus == Field.UNOPENED || fieldStatus == Field.FLAGGED) {
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
	public void showHighscores() {
		System.out.println("highscores");
		// TODO showHighscore display
	}
  
	@Override
	public void timeChanged(String newTimeValue) {
		timer.setText(newTimeValue);
	}

	public void updateFlagCount() {
		numOfFlagsLeft.setText(String.valueOf(board.getRemainingFlags()));
	}
}
