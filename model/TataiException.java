package model;

/**
* An exception class specific to the Tatai! game.
*/
public class TataiException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	* Construct a new TataiException.
	* @param message a message explaining why the exception has been thrown
	*/
	public TataiException(String message) {
		super(message);
	}

}
