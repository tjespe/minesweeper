package minesweeper.fxui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class MinesweeperApp extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		Parent parent = FXMLLoader.load(getClass().getResource("Minesweeper.fxml"));
		stage.setTitle("Minesweeper");
		stage.setScene(new Scene(parent));
		stage.show();
		stage.setResizable(false);
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent t) {
				Platform.exit();
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) {
		launch(MinesweeperApp.class, args);
	}
}
