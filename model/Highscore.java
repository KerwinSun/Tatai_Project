package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class Highscore {
	
	private final String filename = "Highscores.txt";
	
	
	//writes a highscore to highscore file
	public void addHighscore(String difficulty, int Score) throws IOException {
		
		try(FileWriter fw = new FileWriter(filename, true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			   
			    
			    out.println(difficulty + Score);
			   
			} catch (IOException e) {
			   
			}
		
		
	}
	
	//returns highscores for a specific mode in array list form to be used for highscore screen
	public ArrayList<String> getHighscore(String lookFor) {
		
		ArrayList<String> returnableList = new ArrayList<String>();
		
		try{
		
		FileReader recout = new FileReader("Highscores.txt");
		BufferedReader stdoutBuffered = new BufferedReader(recout);


		String line;


		while((line = stdoutBuffered.readLine()) != null){

			
			if(line.contains(lookFor)) {
				
				returnableList.add(line.replace(lookFor, ""));

			}else {
				
			}

		}
		stdoutBuffered.close();
		
		
		}
		catch (IOException e) {
		   
		}
		
		return returnableList;
	}
	

}
