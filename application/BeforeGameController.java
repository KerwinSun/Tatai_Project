package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
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
	private CheckBox addSub;
	@FXML
	private CheckBox divmode;
	@FXML
	private CheckBox multimode;
	@FXML
	private RadioButton practiseRadio;
	
	@FXML
	private TextArea feedBack;
	
	Highscore highscore = new Highscore();
	
	private Difficulty difficulty;
	private boolean problemSetSelected=false;
	private boolean gameModeSelected=false;
	/**
	 * Starts a new game of Tatai
	 */
	@FXML
	private void startGame() {
		
		
		if(difficulty.equals(Difficulty.PRACTISE) ) {
		
		TataiApp.getInstance().setScene("practiseGameScreen.fxml");
		
		
		}else {
		// Tell the GameSessionMaster to start a new game
		
		boolean wordmode = wordToggle.selectedProperty().getValue();
		boolean sumprob = addSub.selectedProperty().getValue();
		boolean multiprob = multimode.selectedProperty().getValue();
		boolean divprob = divmode.selectedProperty().getValue();
		GameSessionMaster.getInstance().newGame(difficulty,wordmode,sumprob,multiprob,divprob);
		
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
		gameModeSelected=true;
		gameSelected();
	}
	
	@FXML
	private void questionType() {
		if (addSub.selectedProperty().getValue()|| divmode.selectedProperty().getValue()||multimode.selectedProperty().getValue()) {
			problemSetSelected=true;
		}else {
			problemSetSelected=false;
		}
		enableStart();
	}
	
	/**
	 * Sets 'hard' as the difficulty level for the game
	 */
	@FXML
	private void hardGameSelected() {
		difficulty = Difficulty.HARD;
		gameModeSelected=true;
		gameSelected();
	}
	/**
	 * Sets 'PRACTISE' as the difficulty level for the game
	 */
	@FXML
	private void practiseGameSelected() {
		
		difficulty = Difficulty.PRACTISE;
		gameModeSelected=true;
		enableStart();
		
	}
	
	@FXML
	private void highscoreUpdate(){
		
		//empty method that may be elaborated on in futre updates
	}
	
	
	
	/**
	 * Shows the user statistics for their specified difficulty level and allows
	 * a game to be started.
	 */
	private void gameSelected() {
		
		
	
		// Allow game to be started
		enableStart();
	}
	
	
	
	/**
	 * Allows game to begin by enabling the 'start' button
	 */
	private void enableStart() {
		if(gameModeSelected && difficulty == Difficulty.PRACTISE) {
			
			startButton.setDisable(false);
		}
		else if (gameModeSelected && problemSetSelected) {
			startButton.setDisable(false);
		}else {
			startButton.setDisable(true);
		}
		
	}

	//calls update when game starts
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		dataFileCreator();		
		scoreUpdate(easyTable, "Easy");
		System.out.println("1");
		scoreUpdate(hardTable, "Hard");
		System.out.print("2");
		feedBackUpdate();
		
		
		
	}
	


	//sets and formats all the text for the feedback area
	private void feedBackUpdate() {
		
		Integer easyHighest = 0;
		Integer hardHighest = 0;
		
		
		//calls to different methods in the model.Highscores class to 
		//fill data into the textfield
		if(!highscore.getHighscoreInt("Easy").isEmpty()) {
		
		easyHighest = Collections.max(highscore.getHighscoreInt("Easy"));
		}
		if(!highscore.getHighscoreInt("Hard").isEmpty()) {
		hardHighest = Collections.max(highscore.getHighscoreInt("Hard"));
		}
		
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
	//methold takes in a table to update and what difficulty to update that
	//table with
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
	

	//creates txt datafiles if they don't already exist
	private void dataFileCreator() {
		File highscoreFile = new File("Highscores.txt");
		File wordDataFile = new File("WordData.txt");
		try {
			highscoreFile.createNewFile();
			wordDataFile.createNewFile();
		} catch (IOException ignore) {
		
		}
		
	}


}
