package model;

import javafx.beans.property.SimpleStringProperty;

/**
 * This class represents a Tatai problem that can be used in a JavaFX table view
 */
public class ProblemDatum {

	private final SimpleStringProperty questionNum;
    private final SimpleStringProperty question;
    private final SimpleStringProperty answer;
    private final SimpleStringProperty userAnswer;

    public ProblemDatum(String questionNum, String question, String answer, String userAnswer) {
        this.questionNum = new SimpleStringProperty(questionNum);
        this.question = new SimpleStringProperty(question);
        this.answer = new SimpleStringProperty(answer);
        this.userAnswer = new SimpleStringProperty(userAnswer);
    }

    /**
     * @return the question number of this problem 
     */
    public String getQuestionNum() {
        return questionNum.get();
    }

    /**
     * @return the number this problem asks the user to pronounce
     */
    public String getQuestion() {
        return question.get();
    }

    /**
     * @return the Maori string representation of the problem
     */
    public String getAnswer() {
        return answer.get();
    }
    
    /**
     * @return a string representation of the user's answer to this problem
     */
    public String getUserAnswer() {
    	return userAnswer.get();
    }
}
