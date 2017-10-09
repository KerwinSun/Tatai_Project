package application;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import model.Difficulty;
import model.GameSessionMaster;

/**
 * This class controls the GUI screen which appears before the user has finished 
 * a game.
 */
public class BeforeGameController {
	
	@FXML
	private Button startButton;
	@FXML
	private Label statsDescription;
	@FXML
	private Label userStats;
	@FXML
	private ToggleGroup difficultyGroup;
	
	private Difficulty difficulty;
	
	/**
	 * Starts a new game of Tatai
	 */
	@FXML
	private void startGame() {
		
		// Tell the GameSessionMaster to start a new game
		GameSessionMaster.getInstance().newGame(difficulty);
		
		// Change the GUI to the next scene
		TataiApp.getInstance().nextScene(false);
	}
	
	/**
	 * Sets 'easy' as the difficulty level for the game 
	 */
	@FXML
	private void easyGameSelected() {
		difficulty = Difficulty.EASY;
		gameSelected();
	}
	
	/**
	 * Sets 'hard' as the difficulty level for the game
	 */
	@FXML
	private void hardGameSelected() {
		difficulty = Difficulty.HARD;
		gameSelected();
	}
	
	/**
	 * Shows the user statistics for their specified difficulty level and allows
	 * a game to be started.
	 */
	private void gameSelected() {
		
		// Show user statistics for their specified difficulty level
		showStats();

		// Allow game to be started
		enableStart();
	}
	
	/**
	 * Shows the user's winning statistics for the difficulty level they have 
	 * selected
	 */
	private void showStats() {
		
		String statsAsString = GameSessionMaster.getInstance().getStatsAsString(difficulty);
		userStats.setText(statsAsString);
		
		// Tell user the difficulty level for the statistics they are seeing
		statsDescription.setText("Your statistics for " + difficulty.toString() + ":");
	}
	
	/**
	 * Allows game to begin by enabling the 'start' button
	 */
	private void enableStart() {
		startButton.setDisable(false);
	}
}
