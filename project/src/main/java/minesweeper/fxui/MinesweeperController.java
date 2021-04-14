package minesweeper.fxui;

import javafx.util.Duration;
import java.io.IOException;
import java.net.URL;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import minesweeper.model.Board;
import minesweeper.model.Field;
import minesweeper.model.Stopwatch;

public class MinesweeperController {

	private Board board;
	private Timeline timeline;
	private Stopwatch stopwatch;
	private boolean gameStarted = false; // for testing

	@FXML
	private URL location; // for testing,

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
		for (int col = 0; col < board.getWidth(); col++) {
			for (int row = 0; row < board.getHeight(); row++) {
				createBoardField(col, row);
			}
		}

		// TODO ADD METHODS FOR LOST AND WON AND STYLE
		if (board.getStatus() == Board.WON) {
			System.out.println("You won");
		} else if (board.getStatus() == Board.LOST) {
			timeline.stop();
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
			Image imgBomb = new Image("file:bomb1.png");
			ImageView imgBombView = new ImageView(imgBomb);
			field.getChildren().add(imgBombView);
			// TODO handle fail if image doesen't load and make folder for images
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
		board = new Board(dropDown.getValue());
		gridPane.setPrefWidth(board.getWidth() * 30);
		gridPane.setPrefHeight(50 + board.getHeight() * 30);
		drawBoard();
		Scene scene = rootPane.getScene();
		if (scene != null) {
			Stage stage = (Stage) scene.getWindow();
			stage.sizeToScene();
		}

	}

	@FXML
	public void boardMouseClickedHandler(MouseEvent event) {
		Node boardField = event.getPickResult().getIntersectedNode();
		int x = GridPane.getColumnIndex(boardField).intValue();
		int y = GridPane.getRowIndex(boardField).intValue();
		if (event.getButton() == MouseButton.PRIMARY) {
			board.openField(x - 1, y - 1);
			if (!gameStarted) { // TODO find better solution
				stopwatch = new Stopwatch();
				timer();
				gameStarted = true;
			}
			drawBoard();
		} /*
			 * else if (event.getButton() == MouseButton.SECONDARY) {
			 * board.getField(x-1,y-1).toggleFlag(); drawBoard(); }
			 */
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

	@FXML
	public void highscoreHoverEffectEntered() {
		highscoreLinkText.setStyle("-fx-font-size: 14px;");
	}

	@FXML
	public void highscoreHoverEffectExited() {
		highscoreLinkText.setStyle("-fx-font-size: 12px;");
	}

	private void timer() { // TODO move this to stopwatch class (Stopwatch.java) bruke
							// observeringteksnikken? propertyChangedListener så den slipper å kaøøle hele
							// tiden, abstact klasse??
		timeline = new Timeline(
				new KeyFrame(Duration.millis(1000), actionEvent -> timer.setText(stopwatch.updateTime())));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}
}
