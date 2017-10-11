package model;

import model.maori.LargeNumberProblemGenerator;
import model.maori.MaoriNumberMathProblemGenerator;
import model.maori.SmallNumberProblemGenerator;

/**
 * This class allows the controller modules to interface with the model modules.
 */
public class GameSessionMaster {

	public static final int NUM_QUESTIONS = 10;
	public static final int NUM_CORRECT_TO_WIN = 8;
	private static final int MAX_ATTEMPTS = 2;
	
	private static GameSessionMaster instance;
	
	private Difficulty difficulty;
	private Statistics stats = new Statistics();
	private int attemptsRemaining;
	private int questionNum;
	private GameMaster game;
	private boolean gameIsOver;
	
	private String[] questionNumLog;
	private String[] valueLog;
	private String[] answerLog;
	private String[] userAnswerLog;
	
	/**
	 * Private constructor ensures GameSessionMaster is only instantiated once, 
	 * through the getInstance() method
	 */
	private GameSessionMaster() {
		
	}
	
	/**
	 * Returns the single instance of the GameSessionMaster class
	 */
	public static GameSessionMaster getInstance() {
		if (instance == null) {
			instance = new GameSessionMaster();
		}
		return instance;
	}
	
	/**
	 * Creates a model for a new game
	 * @param difficulty the difficulty level of the game to be started
	 */
	public void newGame(Difficulty difficulty,boolean wordmode) {
		MaoriNumberMathProblemGenerator gen;
		// Create a GameMaster to store the state of the game
		if (difficulty.equals(Difficulty.EASY)) {
			
			gen = new SmallNumberProblemGenerator();
		} else {
			gen = new LargeNumberProblemGenerator();
		}
		if (wordmode) {
			gen.wordMode(wordmode);
		}
		gen.mathMode(true);
		game = new GameMaster(gen, NUM_QUESTIONS);
		attemptsRemaining = MAX_ATTEMPTS;
		questionNum = 1;
		gameIsOver = false;
		
		questionNumLog = new String[NUM_QUESTIONS+1];
		valueLog = new String[NUM_QUESTIONS+1];
		answerLog = new String[NUM_QUESTIONS+1];
		userAnswerLog = new String[NUM_QUESTIONS+1];
		
		// Remember the difficulty of the game
		this.difficulty = difficulty;
	}
	
	/**
	 * @return the current score of the game
	 */
	public int getScore() {
		return game.getScore();
	}
	
	/**
	 * Checks whether an answer passed in is the correct answer to the current
	 * problem
	 * @param answer the answer given to the problem
	 * @return true if the given answer is correct, or false otherwise
	 */
	public boolean answerProblem(String answer) {
	
		userAnswerLog[questionNum-1] = answer;
		
		boolean isCorrect = game.getProblem().checkAnswer(answer);
		
		if (isCorrect) {
			passProblem();
		} else {
			attemptsRemaining--;
			
			if (attemptsRemaining == 0) {	
				failProblem();
			}
		}
		
		return isCorrect;
	}
	
	/**
	 * @return true if the user has one attempt left for the question, or false
	 * otherwise
	 */
	public boolean oneAttemptLeft() {
		if (attemptsRemaining == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * @return a string representation of the current problem
	 */
	public String getProblem() {
		
		String problem = game.getProblem().toString();
		valueLog[questionNum-1] = problem;
		answerLog[questionNum-1] = game.getProblem().getAnswerAsString();
	
		return problem;
	}
	
	/**
	 * Ends a game by saving the statistics for the game and recording that
	 * the game has finished
	 */
	private void endGame() {
		
		if (!gameIsOver) { // Do not count the game again if the game has already finished
			
			if (game.getScore() >= NUM_CORRECT_TO_WIN) {
				stats.finishGame(difficulty, true);
			} else {
				stats.finishGame(difficulty, false);
			}
			gameIsOver = true;
		}			
	}
	
	public boolean gameIsOver() {
		return gameIsOver;
	}
	
	public double getProgress() {
		return (double) (questionNum-1) / (NUM_QUESTIONS - 1);
	}
	
	public ProblemDatum[] getGameHistory() {
		
		String[][] gameData = new String[4][NUM_QUESTIONS];
		
		ProblemDatum[] problemData = new ProblemDatum[NUM_QUESTIONS];
		
		for (int i = 0; i < gameData[0].length; i++) {
			problemData[i] = new ProblemDatum(questionNumLog[i], valueLog[i], answerLog[i], userAnswerLog[i]);
		}
		
		return problemData;
	}
	
	public Difficulty getDifficulty() {
		return difficulty;
	}
	
	public String getStatsAsString(Difficulty difficulty) {
		return stats.getStatistics(difficulty);
	}

	public void skipProblem() {
		userAnswerLog[questionNum-1] = "SKIPPED";
		failProblem();
	}
	
	private void passProblem() {
		game.passProblem();
		
		finishProblem();
	}
	
	private void failProblem() {
		
		game.failProblem();
		
		finishProblem();
	}
	
	private void finishProblem() {
		if (questionNum == NUM_QUESTIONS) {
			endGame();
		}
		questionNumLog[questionNum-1] = "" + questionNum;
		questionNum++;
		attemptsRemaining = MAX_ATTEMPTS;
	}
}
