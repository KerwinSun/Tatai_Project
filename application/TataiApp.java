package application;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * This class launches the application and controls the transition from one
 * game screen to another.
 */
public class TataiApp extends Application {

	public static final String BEFORE_GAME_VIEW = "InitialGameScreen.fxml";
	public static final String GAME_SESSION_VIEW = "SessionGameScreen.fxml";
	public static final String AFTER_GAME_VIEW = "AfterGameScreen.fxml";
	public static final String HARD_ICON_LOCATION = "media/hard.png";
	public static final String EASY_ICON_LOCATION = "media/easy.png";
	
	private String currentScene;
	private Stage primaryStage;
	private static TataiApp app;
	
	
	/**
	 * Starts the Tatai program
	 * @param args a String array representing the command line input
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	public static TataiApp getInstance() {
		return app;
	}

	/**
	 * Creates the Tatai GUI and forces the GUI to display the screen that allows
	 * the user to start a new game
	 */
	@Override
	public void start(Stage primaryStage) {	
		
		this.primaryStage = primaryStage;
		this.primaryStage.setResizable(false);
		nextScene(false); // Initialise game to first scene
		app = this;
	}
	
	/**
	 * Sets the next screen to be displayed
	 */
	public void nextScene(boolean gameComplete) {
		
		if (currentScene == null) {
			currentScene = BEFORE_GAME_VIEW;
		} else if (currentScene.equals(BEFORE_GAME_VIEW)) {
			currentScene = GAME_SESSION_VIEW;
		} else if (currentScene.equals(GAME_SESSION_VIEW)){
			if (gameComplete == true) {
				currentScene = AFTER_GAME_VIEW;
			} else {
				currentScene = BEFORE_GAME_VIEW;
			}
		} else {		
			currentScene = GAME_SESSION_VIEW;
		}
		changeScene();
	}
	
	
	/**
	 * 
	 * @param scene forces app to specific scene
	 */
	public void setScene(String scene) {
		currentScene=scene;
		changeScene();
	}
	
	/**
	 * Changes the screen being displayed
	 */
	private void changeScene() {
		
		FXMLLoader loader = new FXMLLoader(getClass().getResource(currentScene));

		try {
			Pane mainPane = loader.load();

			Scene scene = new Scene(mainPane, 600, 450); // create the game window
			this.primaryStage.setScene(scene);
			this.primaryStage.show();

		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

	//testing
}
