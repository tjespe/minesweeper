package minesweeper.fxui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import minesweeper.model.Board;
import minesweeper.model.Field;


public class MinesweeperController {

	@FXML private ChoiceBox<String> dropDown;
	@FXML private GridPane boardParent;
	private Board board;
	
	
	
	public void initialize() {
		dropDown.getItems().addAll("Easy", "Normal", "Hard");
		dropDown.setValue("Normal");
		dropDown.setOnAction(event -> {changeDifficultLevel();});
		board = new Board(14,18,40);
		drawBoard();
		
		
	}
	
	private void drawBoard() {
		
		
		//create board tiles 
		boardParent.getChildren().clear();
		for (int col = 0; col < 18; col++) {
			for (int row = 0; row < 14; row++) {
				createBoardField(col, row);
				//((Labeled) boardParent.getChildren().get(col*14+row)).setText("1");
				//boardParent.getChildren().get(col*14+row);
				
				//board.getFieldStatus(col, row);
			}
		}
		
		//TODO ADD STYLE
		//check status of game 
		if (board.getStatus() == 'w') {
			System.out.println("You won");
		} else if (board.getStatus() == 'l') {
			System.out.println("You lost");
		}
		
	}

	private void createBoardField(int row, int col) {
		//Button field = new Button();
        Pane field = new Pane();
		//button.setText(" ");
        //button.setMaxSize(40, 40);
        field.setTranslateX(row);
		field.setTranslateY(col);
		field.setPrefWidth(30);
		field.setPrefHeight(30);
		field.setStyle("-fx-background-color: " + getFieldStyle(board.getFieldStatus(row,col))); //#1db121
        boardParent.add(field, row+1, col+1);
		
	}
	
	@FXML
	public void changeDifficultLevel() {
		System.out.println(dropDown.getValue());
	}
	
	@FXML
	public void boardMouseClickedHandler(MouseEvent e) {
		Node boardField = e.getPickResult().getIntersectedNode();
		int x = GridPane.getColumnIndex(boardField).intValue();
		int y = GridPane.getRowIndex(boardField).intValue();
		System.out.println(x+","+y);
		if (e.getButton() == MouseButton.PRIMARY) {
			System.out.println("Left button clicked");
			board.openField(x-1,y-1);
			drawBoard();
		} else if (e.getButton() == MouseButton.SECONDARY) {
			System.out.println("Right button clicked");
			//TODO toggle flag 
		}
		
	}
	
	
	
	private String getFieldStyle(char fieldStatus) {
		if (fieldStatus == 'u') {
			return "#1db121";
		} else if (fieldStatus == 'f') {
			return "#FF0000";
		} else if (fieldStatus == 'o') {
			return "#ffffff";
		} else if (fieldStatus == 'b') {
			return "#000000";
		}
		return null;
		
	}
	
}
