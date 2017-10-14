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
	
	@FXML
	private void initialize() {

		micButton.setDisable(true);
		numberEntered.textProperty().addListener((observable, oldValue, newValue) -> {
		    textValidityChecker();
		});
	}


	@Override
	public void interpretedText(String text) {

		saidText = text;
		checkCorrectness();
		micButton.setDisable(false);
		numberEntered.setEditable(true);
	}


	@FXML
	private void takeRecording(){

		micButton.setDisable(true);
		numberEntered.setEditable(false);
		voiceRecogniser = new MaoriNumberInterpreter();
		voiceRecogniser.addListener(this);
		voiceRecogniser.recordAudio();

	}



	@Override
	public void recordStart() {
		

	}

	@Override
	public void recordEnd() {

		voiceRecogniser.playbackRecording();
		voiceRecogniser.runPharser();



	}

	private void checkCorrectness() {

		int parsedNum = Integer.parseInt(numberEntered.getText());
		enteredText = int2maori(parsedNum);
		//debugging code
		System.out.print(int2maori(parsedNum));
		System.out.print(saidText);
		
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


		}else if(text.equals("")) {
			
			warning.setText("");
			micButton.setDisable(true);
			
		}else if(!text.matches("[0-9]+")) {
			
			warning.setText("numbers only");
			micButton.setDisable(true);
			
		}
		


	}

	@FXML
	private void exitGame(){

		Alert alert = new Alert(AlertType.CONFIRMATION, "End Practise Mode?",  ButtonType.NO,ButtonType.YES);
		alert.showAndWait();
		if (alert.getResult() == ButtonType.YES) {
			TataiApp.getInstance().setScene("InitialGameScreen.fxml");
		}

	}

}
