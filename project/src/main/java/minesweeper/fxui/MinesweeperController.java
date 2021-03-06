package minesweeper.fxui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Labeled;
import javafx.scene.layout.GridPane;
import minesweeper.model.Board;


public class MinesweeperController {

	@FXML private ChoiceBox<String> dropDown;
	@FXML private GridPane boardParent;
	
	
	public void initialize() {
		dropDown.getItems().addAll("Easy", "Normal", "Hard");
		dropDown.setValue("Normal");
		dropDown.setOnAction(event -> {changeDifficultLevel();});
		
		
		
		Board board = new Board(14,18,40);
		
		//create board tiles 
		boardParent.getChildren().clear();
		for (int col = 0; col < 18; col++) {
			for (int row = 0; row < 14; row++) {
				createBoardTile(col, row);
				//((Labeled) boardParent.getChildren().get(col*14+row)).setText("1");
				boardParent.getChildren().get(col*14+row);
				//board.fields.get();
				
			}
		}
	}
	
	//TODO create method with initial game state that is called in initialize

	private void createBoardTile(int row, int col) {
		Button field = new Button();
        //Pane field = new Pane();
		//button.setText(" ");
        //button.setMaxSize(40, 40);
		field.setTranslateX(row);
		field.setTranslateY(col);
		field.setPrefWidth(30);
		field.setPrefHeight(30);
        boardParent.add(field, row+1, col+1);
		
	}
	
	
	
	@FXML
	public void changeDifficultLevel() {
		System.out.println(dropDown.getValue());
	}
	
}
