package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleGroup;
import model.Difficulty;
import model.GameSessionMaster;
import model.Highscore;

/**
 * This class controls the GUI screen which appears before the user has finished 
 * a game.
 */
public class BeforeGameController implements Initializable{
	
	@FXML
	private Button startButton;
	@FXML
	private Label statsDescription;
	@FXML
	private Label userStats;
	@FXML
	private ToggleGroup difficultyGroup;
	@FXML
	private Tab highscoreBar;
	@FXML
	private TableView easyTable;
	@FXML
	private TableView hardTable;
	
	@FXML
	private CheckBox wordToggle;
	@FXML
	private RadioButton practiseRadio;
	
	@FXML
	private TextArea feedBack;
	
	Highscore highscore = new Highscore();
	
	private Difficulty difficulty;
	/**
	 * Starts a new game of Tatai
	 */
	@FXML
	private void startGame() {
		
		
		if(difficulty.equals(Difficulty.PRACTISE) ) {
		
		TataiApp.getInstance().setScene("practiseGameScreen.fxml");
		
		
		}else {
		// Tell the GameSessionMaster to start a new game
		GameSessionMaster.getInstance().newGame(difficulty,wordToggle.selectedProperty().getValue());
		
		// Change the GUI to the next scene
		TataiApp.getInstance().nextScene(false);
		}
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
	
	@FXML
	private void practiseGameSelected() {
		
		difficulty = Difficulty.PRACTISE;
		enableStart();
		
	}
	
	@FXML
	private void highscoreUpdate(){
		
		
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

	//calls update when game starts
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		scoreUpdate(easyTable, "Easy");
		scoreUpdate(hardTable, "Hard");
		feedBackUpdate();
		
	}
	
	//sets and formats all the text for the feedback area
	private void feedBackUpdate() {
		
		
		Integer easyHighest = Collections.max(highscore.getHighscoreInt("Easy"));
		Integer hardHighest = Collections.max(highscore.getHighscoreInt("Hard"));
		
		
		feedBack.setText("");
		feedBack.appendText("Your top score for easy mode is: " + easyHighest + "/10 \n\n");
		feedBack.appendText("Your top score for hard mode is: " + hardHighest + "/10 \n\n");
		
		feedBack.appendText("You have won: " +highscore.getWinTotal()[0]+ " out of " +highscore.getWinTotal()[1]+" games \n\n");
		
		feedBack.appendText("Some helpful tips: \n\n");
		
		feedBack.appendText("The word you are best at saying is: "+ highscore.getWordFrequency("Success")+ "\n\n");

		
		feedBack.appendText("The word that needs some practise is: "+ highscore.getWordFrequency("Failed")+ "\n\n");
	
		feedBack.appendText("KEEP IT UP!!!");
	}

	//updates the score on startup or after a game
	private void scoreUpdate(TableView Table, String mode)
	{
		ArrayList<String> highscoreData = highscore.getHighscore(mode);
		
		final ObservableList<String> data = FXCollections.observableArrayList(highscoreData);
		
		Table.setPlaceholder(new Label("No Scores"));
		Table.setEditable(true);
		Table.setItems(data);
		
		TableColumn<String, String> tc = new TableColumn<>(mode);
		tc.setPrefWidth(99);
		tc.setCellValueFactory((p) -> {
			return new ReadOnlyStringWrapper(p.getValue());
		});
		Table.getColumns().add(tc);

	
	}
}
