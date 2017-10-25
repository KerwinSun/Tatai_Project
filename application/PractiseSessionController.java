package application;



import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import model.maori.MaoriNumberMathProblemGenerator;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

/**
 * 
 * This class is the FXML controller for the practice screen
 *
 */
public class PractiseSessionController extends MaoriNumberMathProblemGenerator implements InterpreterListener {

	private MaoriNumberInterpreter voiceRecogniser;

	@FXML 
	private Button micButton;
	@FXML
	private Button exitButton;
	@FXML
	private TextField numberEntered;
	@FXML
	private Label warning;

	String saidText;
	String enteredText;
	
	/**
	 * Method is called when Controller is constructed 
	 */
	@FXML
	private void initialize() {

		//mic should be disabled on instantiation
		micButton.setDisable(true);
		
		//sets the textfield to run the checker when text is typed
		numberEntered.textProperty().addListener((observable, oldValue, newValue) -> {
		    textValidityChecker();
		});
	}


	/**
	 * Response from the Interpreter
	 */
	@Override
	public void interpretedText(String text) {

		saidText = text;
		checkCorrectness();
		micButton.setDisable(false);
		numberEntered.setEditable(true);
	}


	/**
	 * records users voice
	 * method called when record is pressed
	 */
	@FXML
	private void takeRecording(){

		//disabled mic and text field
		micButton.setDisable(true);
		numberEntered.setEditable(false);
		
		//runs record audio function
		//adds this class as a listener
		voiceRecogniser = new MaoriNumberInterpreter();
		voiceRecogniser.addListener(this);
		voiceRecogniser.recordAudio();

	}



	@Override
	public void recordStart() {
		//interface method not needed does nothing at the moment
		//may be needed for future updates
		
	}

	/**
	 * interface method tells the class the recording has ended
	 */
	@Override
	public void recordEnd() {

		voiceRecogniser.playbackRecording();
		
		//run the phaser we now have the recording
		voiceRecogniser.runPharser();



	}

	/**
	 * check is what was heard by HDK is correct
	 */
	private void checkCorrectness() {

		
		
		int parsedNum = Integer.parseInt(numberEntered.getText());
		enteredText = int2maori(parsedNum);
		
		//logic to determain what prompts to show
		if(enteredText.equals(saidText)) {
			

			Alert alert = new Alert(AlertType.CONFIRMATION, "You pronounced '" + enteredText + "' perfectly", ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setTitle("Results");
			alert.setHeaderText("Well Done!");
			alert.showAndWait();
			
			
		}else {
			
			Alert alert = new Alert(AlertType.CONFIRMATION, "The number we were trying to hear was: '" + enteredText + "' unfortunatly what we heard was '" + saidText + "'.  \nPress record to try again",  ButtonType.OK);
			alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
			alert.setTitle("Results");
			alert.setHeaderText("Try again");
			alert.showAndWait();
			
		}
		
		

	}
	
	/**
	 * checks the text field is only getting numbers
	 * 
	 */
	private void textValidityChecker() {

		String text = numberEntered.getText();
		
		//check text not null and numeric
		if(!text.equals(null) && text.matches("[0-9]+")) {

			int integer = Integer.parseInt(text);
			if(integer <= 99 && integer >= 1) {
				
				warning.setText("");
				micButton.setDisable(false);

			}else {

				warning.setText("number bust be between 1 and 99");
				micButton.setDisable(true);

			}

		//checks text field empty
		}else if(text.equals("")) {
			
			warning.setText("");
			micButton.setDisable(true);
			
		//checks if only numeric
		}else if(!text.matches("[0-9]+")) {
			
			warning.setText("numbers only");
			micButton.setDisable(true);
			
		}
		


	}

	/**
	 * gives the user a prompt to confirm exiting the game
	 */
	@FXML
	private void exitGame(){

		Alert alert = new Alert(AlertType.CONFIRMATION, "End Practise Mode?",  ButtonType.NO,ButtonType.YES);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			TataiApp.getInstance().setScene("InitialGameScreen.fxml");
		}

	}

}
