package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

/**
 * An instance of BashProcess can take string commands and run them in a bash shell. 
 * Commands are run by a worker thread in the order they are added to the object.
 */
public class BashProcess extends Service<Integer>{
	private List<String> commands;
	private BufferedReader stdout;
	private BufferedReader stderr;
	private Runnable afterTask = null;
	private int exitStatus = -1; // an exitcode of -1 indicates that process has not been run
	
	/**
	 * Constructs a new BashProcess object without any commands.
	 */
	public BashProcess(){
		commands = new LinkedList<String>();
		this.setOnSucceeded(new EventHandler<WorkerStateEvent>() {

			@Override
			public void handle(WorkerStateEvent event) {
				exitStatus = getValue();
				
				//if there is something else to do, do it
				if (afterTask != null) {
					afterTask.run();
				}
			}
		});
	}
	
	/**
	 * Adds a command to be executed in bash. Commands are executed in the order they are
	 * added.
	 * @param command a single bash command to be run (expansions are supported)
	 */
	public void addCommand(String command) {
		commands.add(command);
	}
	
	public int getExitStatus() {
		return exitStatus;
	}
	
	/**
	 * @return the standard output of the last process run
	 */
	public BufferedReader getStdOut() {
		return stdout;
	}
	
	/**
	 * @return the standard error of the last process run
	 */
	public BufferedReader getStdError() {
		return stderr;
	}
	
	/**
	 * @param the task action to be run on the JavaFX dispatch thread after bash 
	 * process has finished
	 */
	public void setAfterTask(Runnable task) {
		afterTask = task;		
	}
	
	/**
	 * Adds a task to be completed on a background thread.
	 */
	@Override
	protected Task<Integer> createTask() {
		return new Task<Integer>() {
			protected Integer call() {
				// Concatenate commands together so that they can be run in one process
				String toExecute = "";
				
				for (String command: commands) {
					toExecute = toExecute + command + "; ";
				}

				toExecute = toExecute.substring(0, toExecute.length() - 2);
				
				// Initialise bash process and exit status
				Process bashProcess = null;
				int exitstatus = -1;
				
				// Put the concatenated command into a new bash process
				ProcessBuilder pb = new ProcessBuilder("bash", "-c", toExecute);
				
				// Run the process
				try {
					 bashProcess = pb.start();
					 exitstatus = bashProcess.waitFor();
					 
				// Print any exceptions that the process throws for debugging purposes
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				
				// Fetch the standard output and standard error of the last process
				stdout = new BufferedReader(new InputStreamReader(bashProcess.getInputStream()));
				stderr = new BufferedReader(new InputStreamReader(bashProcess.getErrorStream()));
				return exitstatus;
			}
		};
		
	}
	
	/**
	 * Starts the Bash process on a background thread.
	 */
	public void execute() {
		this.start();
	}

}