package application;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import model.Difficulty;
import model.GameSessionMaster;

/**
 * This class controls the GUI screen which appears while the user is playing
 * a game.
 */
public class GameSessionController implements InterpreterListener {
	
	private static final String PATH_TO_PASS_SOUND = "pass.mp3";

	private MaoriNumberInterpreter voiceRecogniser;
	
	@FXML
	ImageView difficultyIcon;
	@FXML
	ImageView firstLifeIcon;
	@FXML
	ImageView secondLifeIcon;
	@FXML
	Label score;
	@FXML
	ProgressBar progressBar;
	@FXML
	Label numberProblem;
	@FXML
	Button exitButton;
	@FXML
	Button micButton;
	@FXML
	Button skipButton;
	@FXML 
	BorderPane backgroundPanel;
	
	/**
	 * Sets up the screen to be displayed when the user has just started a game.
	 */
	@FXML
	private void initialize() {
		setDifficultyIcon();
		setProblem();
	}
	
	/**
	 * Sets the difficulty icon to tell the user the difficulty of the game
	 * they are playing.
	 */
	private void setDifficultyIcon() {
		Difficulty difficulty = GameSessionMaster.getInstance().getDifficulty();
		Image image;
		
		if (difficulty.equals(Difficulty.EASY)) {
			image = new Image(TataiApp.EASY_ICON_LOCATION);
		} else {
			image = new Image(TataiApp.HARD_ICON_LOCATION);
		}
		difficultyIcon.setImage(image);
	}
	
	/**
	 * Displays the number the user is to pronounce in Maori
	 */
	private void setProblem() {
		// Display the problem
		String problem = GameSessionMaster.getInstance().getProblem();
		numberProblem.setText("" + problem);
		
		// Enable microphone button
		micButton.setDisable(false);
	}

	/**
	 * Changes back to the opening screen
	 */
	@FXML
	private void exitGame() {
		Alert alert = new Alert(AlertType.CONFIRMATION, "End current game?",  ButtonType.NO,ButtonType.YES);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			TataiApp.getInstance().nextScene(false);
		}
		
	}

	/**
	 * Takes a three second recording of the user pronouncing the Maori number
	 */
	@FXML
	private void takeRecording() {
		
		// Disable the microphone button
		micButton.setDisable(true);

		// Take recording
		voiceRecogniser = new MaoriNumberInterpreter();
		voiceRecogniser.addListener(this);
		voiceRecogniser.recordAudio();

	}

	@Override
	public void recordStart() {
		// Do nothing
	}

	/**
	 * Converts the user's speech into a string of Maori numbers
	 */
	@Override
	public void recordEnd() {
		// Play the recording
		voiceRecogniser.playbackRecording();
		
		// Get text
		voiceRecogniser.runPharser();
	}
	
	/**
	 * Gives the user feedback on whether their answer was correct
	 * @param text a string representation of the Maori number the voice
	 * recognition software detected the user as saying
	 */
	@Override
	public void interpretedText(String text) {
		// Enable the microphone button again
		micButton.setDisable(false);

		// Tell the GameSessionMaster the answer
		boolean isCorrect = GameSessionMaster.getInstance().answerProblem(text);

		// Use sound effects and a flashing screen to tell the user if they were
		// correct
		BackgroundFlasher bf = new BackgroundFlasher();
		if (isCorrect) {
			doCorrectSound();
			bf.flashColour("green");
		} else if (GameSessionMaster.getInstance().oneAttemptLeft()){
			bf.flashColour("orange");
		} else {
			bf.flashColour("red");
		}

		updateGame();
	}

	/**
	 * Updates the GUI components after the user has answered a problem
	 */
	private void updateGame() {
		
		// Check if game is over
		checkGameOver();
				
		// Update attempts remaining
		setLivesRemaining();

		// Update progress bar
		setProgress();

		// Update score
		setScore();

		// Set a new problem
		setProblem();
		
	}
	
	/**
	 * Tells TataiApp to change to the next screen if the game is over
	 */
	private void checkGameOver() {		
		if (GameSessionMaster.getInstance().gameIsOver()) {
			TataiApp.getInstance().nextScene(true);
		}
	}

	/**
	 * Tells the user how many lives they have remaining by displaying a 
	 * corresponding number of hearts
	 */
	private void setLivesRemaining() {
		boolean oneAttemptRemaining = GameSessionMaster.getInstance().oneAttemptLeft();

		if (oneAttemptRemaining) {
			secondLifeIcon.setVisible(false);
		} else {
			secondLifeIcon.setVisible(true);
		} 
	}
	
	/**
	 * Shows the user how far through the game they are
	 */
	private void setProgress() {
		double progress = GameSessionMaster.getInstance().getProgress();
		progressBar.setProgress(progress);
	}
	
	/**
	 * Tells the user their score
	 */
	private void setScore() {
		int score = GameSessionMaster.getInstance().getScore();
		this.score.setText("" + score);
	}

	/**
	 * Tells the user they have answered correctly by making a sound
	 */
	private void doCorrectSound() {
		BashProcess process = new BashProcess();
		process.addCommand("ffplay " + PATH_TO_PASS_SOUND + " -nodisp -autoexit");
		process.execute();
	}

	/**
	 * Skips a question by failing the question without the user needing to
	 * attempt to pronounce the Maori number
	 */
	@FXML
	private void skipProblem() {
		// Tell the game state class to skip the problem
		GameSessionMaster.getInstance().skipProblem();
		
		// Set a new problem
		updateGame();
	}
	
	/**
	 * This class makes the GUI background flash red, orange or green depending
	 * on whether the user got an answer wrong and failed the question (red),
	 * got an answer wrong but did not fail the question (orange), or passed the
	 * question (green).
	 */
	private class BackgroundFlasher extends Service<Void>{
		public void flashColour(String colour) {
			String css;
			if (colour.equalsIgnoreCase("green")) {
				css = "-fx-background-color: green;";
			} else if (colour.equalsIgnoreCase("orange")) {
				css = "-fx-background-color: orange;";
			} else {
				css = "-fx-background-color: red;";
			}
			Background oldb = backgroundPanel.getBackground();
			backgroundPanel.setStyle(css);
			this.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

				@Override
				public void handle(WorkerStateEvent event) {
					backgroundPanel.setBackground(oldb);
				}
			
			});
			this.start();
		}
		@Override
		protected Task<Void> createTask() {	
			
			return new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					Thread.sleep(250); // Pause for 0.25 seconds
					return null;
				}	
			};
		}
		
	}

}

