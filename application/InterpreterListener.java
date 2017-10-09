package application;

/**
 * A class that implements this interface can be informed of when a voice 
 * recognition process has started, has finished, and the Maori number string 
 * representation of the speech has been produced.
 */
public interface InterpreterListener {
	public void interpretedText(String text);
	public void recordStart();
	public void recordEnd();
}