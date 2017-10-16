package application;


import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.Difficulty;
import model.GameSessionMaster;
import model.Highscore;
import model.ProblemDatum;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * This class controls the GUI screen which appears after the user has finished 
 * a game.
 */
public class AfterGameController {

	
	@FXML
	private Button returnButton;
	@FXML
	private TableView<ProblemDatum> historyTable;
	@FXML
	private TableColumn<ProblemDatum, String> questionNumColumn;
	@FXML
	private TableColumn<ProblemDatum, String> questionColumn;
	@FXML
	private TableColumn<ProblemDatum, String> answerColumn;
	@FXML
	private TableColumn<ProblemDatum, String> userAnswerColumn;
	@FXML
	private ImageView difficultyIcon;
	@FXML
	private Label scoreFeedback;
	@FXML
	private Label scoreDisplay;
	
	/**
	 * A model for the game statistics table
	 */
	private ObservableList<ProblemDatum> gameData;
	
	@FXML
	private void goHome() {
		TataiApp.getInstance().setScene(TataiApp.BEFORE_GAME_VIEW);
	}
	
	/**
	 * Sets up the screen to be displayed after the user has finished a game.
	 */
	@FXML
	private void initialize() {
		try {
			
			saveHighscore();
			saveFailedWords();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		displayGameStats();
        displayScore();
        giveFeedback();
        
        
	}

	/**
	 * Tells the user which questions they answered, how they replied, and
	 * what the answers were.
	 */
	private void displayGameStats() {
		ProblemDatum[] gameData = GameSessionMaster.getInstance().getGameHistory();

		this.gameData = FXCollections.observableArrayList(gameData);

		questionNumColumn.setCellValueFactory(
				new PropertyValueFactory<ProblemDatum, String>("questionNum"));

		questionColumn.setCellValueFactory(
				new PropertyValueFactory<ProblemDatum, String>("question"));

		answerColumn.setCellValueFactory(
				new PropertyValueFactory<ProblemDatum, String>("answer"));

		userAnswerColumn.setCellValueFactory(
				new PropertyValueFactory<ProblemDatum, String>("userAnswer"));

		historyTable.setItems(this.gameData);
	}
	
	/**
	 * Displays user's score.
	 */
	private void displayScore() {
        int score = GameSessionMaster.getInstance().getScore();
        int numQuestions = GameSessionMaster.NUM_QUESTIONS;
        scoreDisplay.setText("You scored " + score + "/" + numQuestions);
	}
	
	/**
	 * Tells the user the difficulty of the game they have just played, and
	 * whether they won the game. If the user has won an easy game, this method
	 * suggests the user try hard mode.
	 */
	private void giveFeedback() {
		Difficulty difficulty = GameSessionMaster.getInstance().getDifficulty();
        boolean gameWon = (GameSessionMaster.getInstance().getScore() >= GameSessionMaster.NUM_CORRECT_TO_WIN);
        Image image;
        
        if (difficulty.equals(Difficulty.EASY)) {
        	
        	// Set difficulty icon to indicate difficulty of game user has finished
        	image = new Image(TataiApp.EASY_ICON_LOCATION);
        	
        	// Tell user whether they won or lost
        	if (gameWon) {
            	scoreFeedback.setText("Well done for beating EASY! Now try HARD Mode.");
            } else {
            	scoreFeedback.setText("You didn't beat EASY. Better luck next time.");
            }
        } else {
        	image = new Image(TataiApp.HARD_ICON_LOCATION);
        	if (gameWon) {
            	scoreFeedback.setText("Well done for beating HARD! Try HARD again?");
            } else {
            	scoreFeedback.setText("You didn't beat HARD. Better luck next time.");
            }
        }
        difficultyIcon.setImage(image);
	}
	
	//saves the high score in array list format to text file
	private void saveHighscore() throws IOException {
		
		Difficulty difficulty = GameSessionMaster.getInstance().getDifficulty();
		int score = GameSessionMaster.getInstance().getScore();
		Highscore saveScore = new Highscore();
		
		if(difficulty.equals(Difficulty.HARD)) {
			
			
			saveScore.addHighscore("Hard", score);
			
		}else if(difficulty.equals(Difficulty.EASY)) {
			
			
			saveScore.addHighscore("Easy", score);
			
		}
		
	
	
	}

	/**
	 * Keeps a record of what words the user had trouble with so the user
	 * can be informed on areas to improve
	 * @throws IOException
	 */
	private void saveFailedWords() throws IOException {
	
	ProblemDatum[] gameData = GameSessionMaster.getInstance().getGameHistory();
	Highscore saveScore = new Highscore();
	
	for(ProblemDatum problem: gameData) {
		
		if(!(problem.getAnswer().equals(problem.getUserAnswer()))) {
			
			saveScore.addWordsData("Failed",problem.getAnswer());
			
		}else {
			
			saveScore.addWordsData("Success",problem.getAnswer());
			
		}
		
	}
	
	}

}
