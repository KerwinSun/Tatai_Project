package model;

/**
 * This class represents a single problem
 */
public class Problem {
	private String question; //the question the user gets
	private Object answer; //the answer to the question
	
	/**
	 * 
	 * @param question what the question is
	 * @param answer the answer to the problem 
	 */
	public Problem(String question, Object answer) {
		this.question = question;
		this.answer = answer;
		
	}
	
	/**
	 * Returns a string representation of the question
	 */
	public String toString() {
		return question;
	}
	
	/**
	 * Checks if a query is the correct answer to the problem
	 * @param query the query being checked
	 * @return true if query is the correct answer, and false otherwise
	 */
	public boolean checkAnswer(Object query) {
		return answer.equals(query);
	}
	
	/**
	 * @return a string representation of the answer to the problem
	 */
	public String getAnswerAsString() {
		return answer.toString();
	}
}
