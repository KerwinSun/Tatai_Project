package model;

/**
 * An instance of the GameMaster class holds the state of a Tatai! game.
 */
public class GameMaster {

	private final int NUM_PROBLEMS;
	private ProblemGenerator generator;
	private Problem currentProblem;
	private int currentProbNum;
	private int score;
	
	/**
	 * Constructs a GameMaster object to hold the state of a new Tatai! game.
	 * @param generator an object that randomly generates number problems of a specified difficulty
	 * @param NUM_PROBLEMS the number of problems to give the user in this game
	 */
	public GameMaster(ProblemGenerator generator, int numProblems) {
		if (generator == null) {
			throw new TataiException("Problem generator must be non null.");
		}
		if (numProblems <= 0) {
			throw new TataiException("Number of problems must be greater than zero.");
		}
		NUM_PROBLEMS = numProblems;
		this.generator = generator;
		currentProblem = generator.newProblem();
		currentProbNum = 1;
		score = 0;
	}
	
	/**
	 * Returns the current problem the game is on.
	 */
	public Problem getProblem() {
		return currentProblem;
	}
	
	/**
	 * Passes the current problem. The game score is increased and a new problem is 
	 * generated.
	 * @throws TataiException if this method is called after the maximum number of
	 * problems allowed in the game has been surpassed 
	 */
	public void passProblem() {
		
		// Check if the maximum number of allowed problems has been reached
		if (currentProbNum > NUM_PROBLEMS) {
			throw new TataiException("Cannot pass the problem as game has already finished.");
		}
		
		// Increment score
		score++;
		
		newProblem();
	}
	
	/**
	 * Fails the current problem. The game score stays the same and a new problem is 
	 * generated.
	 * @throws TataiException if this method is called after the maximum number of
	 * problems allowed in the game has been surpassed
	 */
	public void failProblem() {
		
		// Check if the maximum number of allowed problems has been reached
		if (currentProbNum > NUM_PROBLEMS) {
			//throw new TataiException("Cannot fail the problem as game has already finished.");
		}
		
		newProblem();
	}
	
	/**
	 * Generates a new problem for the game and increments the problem number.
	 */
	private void newProblem() {
		// Get new problem
		currentProblem = generator.newProblem();
		
		// Increase problem count
		currentProbNum++;
	}
	
	/**
	 * Returns the current score of the game.
	 */
	public int getScore() {
		return score;
	}
	
}