package minesweeper.fxui;

import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import minesweeper.model.Board;
import minesweeper.model.Stopwatch;


public class MinesweeperController {

	private Board board;
	private Timeline timeline;
	private Stopwatch stopwatch;
	private boolean gameStarted = false; //for testing
	
	@FXML private ChoiceBox<String> dropDown;
	@FXML private GridPane boardParent;
	@FXML private Text timer;
	@FXML private Text highscoreLinkText;
	
	
	public void initialize() {
		dropDown.getItems().addAll("Easy", "Normal", "Hard");
		dropDown.setValue("Normal");
		dropDown.setOnAction(event -> {changeDifficultLevel();});
		board = new Board(14,18,40);
		drawBoard();	
		//timer();
		//startTime = System.currentTimeMillis();
	}
	
	private void drawBoard() {
		boardParent.getChildren().clear();
		for (int col = 0; col < 18; col++) {
			for (int row = 0; row < 14; row++) {
				createBoardField(col, row);
			}
		}
		
		//TODO ADD STYLE
		//check status of game 
		if (board.getStatus() == 'w') {
			System.out.println("You won");
		} else if (board.getStatus() == 'l') {
			timeline.stop();
			System.out.println("You lost");
		}		
	}
	
	private void createBoardField(int row, int col) { //TODO divide into several methods
        StackPane field = new StackPane();
        field.setTranslateX(row);
		field.setTranslateY(col);
		field.setPrefWidth(30);
		field.setPrefHeight(30);
		field.setStyle("-fx-background-color: " + getFieldStyle(board.getFieldStatus(row,col)));
		if (board.getFieldStatus(row,col) == 'b') {
			Image imgBomb = new Image("file:bomb1.png");
			ImageView imgBombView = new ImageView(imgBomb);
			field.getChildren().add(imgBombView);
			
			//TODO handle fail if image doesen't load and make folder for images
		}
		
		if (board.countAdjacentBombs(row, col) != 0 && board.getFieldStatus(row,col) == 'o') {
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
			numOfAdjacentBombs.setFont(Font.font ("Verdana", FontWeight.BOLD, 18));
			StackPane.setAlignment(numOfAdjacentBombs, Pos.CENTER);
			field.getChildren().add(numOfAdjacentBombs);
		}
        boardParent.add(field, row+1, col+1);
	}
	
	@FXML
	public void changeDifficultLevel() {
		System.out.println(dropDown.getValue());
	}
	
	@FXML
	public void boardMouseClickedHandler(MouseEvent event) {
		Node boardField = event.getPickResult().getIntersectedNode();
		int x = GridPane.getColumnIndex(boardField).intValue();
		int y = GridPane.getRowIndex(boardField).intValue();
		if (event.getButton() == MouseButton.PRIMARY) {
			board.openField(x-1,y-1);
			if (!gameStarted) {						//TODO find better solution
				stopwatch = new Stopwatch();	
				timer();
				gameStarted = true;
			}
			drawBoard();
		} /*else if (event.getButton() == MouseButton.SECONDARY) {
			board.getField(x-1,y-1).toggleFlag();
			drawBoard();
		}
		*/
	}
	
	private String getFieldStyle(char fieldStatus) {
		if (fieldStatus == 'u') {
			return "#73b504"; //darker green #66a103
		} else if (fieldStatus == 'f') {
			return "#ff0000";
		} else if (fieldStatus == 'o') {
			return "#C39B77"; //darker brown #b6916f
		} else if (fieldStatus == 'b') {
			return "#C39B77"; //black for testing "#000000"
		}
		return null;	
	}
	
	@FXML
	public void showHighscores() {
		System.out.println("highscores");
		//TODO showHighscore display
	}
	@FXML
	public void highscoreHoverEffectEntered() {
		highscoreLinkText.setStyle("-fx-font-size: 14px;");
	}
	@FXML
	public void highscoreHoverEffectExited() {
		highscoreLinkText.setStyle("-fx-font-size: 12px;");
	}
	//TODO move this to stopwatch class (Stopwatch.java)
	private void timer() {
		timeline = new Timeline(new KeyFrame(
		        Duration.millis(1000),
		        actionEvent -> timer.setText(stopwatch.updateTime())));
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}	
}
