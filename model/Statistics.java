package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * This class reads in and saves user statistics
 */
public class Statistics {
	private final String filename = "Stats.txt";
	
	/**
	 * Maps the names of difficulties to the number of games played
	 */
	private HashMap<String,Integer> games;
	 
	/**
	 * Maps the names of difficulties to the number of games won
	 */
	private HashMap<String,Integer> wins;
	
	/**
	 * An array of all the difficulties
	 */
	private final Difficulty[] alldiffs = Difficulty.values();
	
	public Statistics() {
		try {
			// Creates the maps
			games = new HashMap<String,Integer>();
			wins = new HashMap<String,Integer>();
			
			// Do an initial reset
			reset(); // A null get() means string is not a difficulty from the enum
			
			// Check if file exists
			File file = new File(filename);
			if (file.exists()) {
				
				// Read file and populate scores
				BufferedReader reader = new BufferedReader(new FileReader(filename));
				String line =reader.readLine();
				while (line != null) {
					
					// Split by space
					String[] splited = line.split("\\s+");
					
					// If difficulty exists it must be part a value in the enum
					if (splited.length==3 && games.get(splited[0])!=null) {
						Integer readgames;
						Integer readwins;
						try {
							readwins = Integer.parseInt(splited[1]);
							readgames = Integer.parseInt(splited[2]);
							
							if (readwins>readgames) {
								// Invalid score, as win rate is higher than 100%
								break;
							}
						} catch (NumberFormatException e) {
							// If values are not integers, formatting is wrong
							break; // Reset file
							
						}
						games.put(splited[0], readgames);
						wins.put(splited[0], readwins);
						
					} else {
						
						// Formatting is wrong, so file needs to be reset
						break;
					}
					
					// Read next line
					line =reader.readLine();
				}
				
				// Close reader to prevent memory leaks
				reader.close();
				
				// Reset and rewrite file
				if (line != null) {
					reset();
					writefile();
				}
				
				
			} else {
				// File did not exist
				file.createNewFile(); // Create a new file
				
				// Scores are already reset, so file can be written to
				writefile();
				
			}
		} catch (IOException e) {
			// Ignore
		}
	}
	
	/**
	 * Returns a string representing statistics about a specified difficulty 
	 * level
	 * @param dif the difficulty of the statistics to be returned
	 * @return text message about the player statistics
	 */
	public String getStatistics(Difficulty dif) {
		
		//fetch scores from maps
		Integer game = games.get(dif.toString());
		Integer win = wins.get(dif.toString());
		
		//no percentage when no games
		if (game == 0) {
			return "0/0 games won";
		}else {
			return win + "/" + game + " games won: " + win*100/game +"%";
		}
	}
	
	/**
	 * Saves a game win or loss of a specified difficulty
	 * @param dif the difficulty of the game finished
	 * @param didWin whether the player won the game
	 */
	public void finishGame(Difficulty dif, boolean didWin) {
		
		// If the player has won, increase the win counter
		if (didWin) {
			wins.put(dif.toString(), wins.get(dif.toString())+1);
		}
		
		games.put(dif.toString(), games.get(dif.toString())+1);
		
		// Write to file immediately as program could close at any time
		writefile();

	}
	
	/**
	 * Resets the scores at all difficulty levels
	 */
	private void reset() {
		
		// Reset statistics for all difficulties so the mapping is aware of the 
		// enums
		for (Difficulty d:alldiffs) {
			games.put(d.toString(), 0);
			wins.put(d.toString(), 0);
		}
	}
	
	/**
	 * Writes the statistics to the file
	 */
	private void writefile() {
		
		try {
			PrintWriter writer = new PrintWriter(filename, "UTF-8");
			for (Difficulty d:alldiffs) {
				writer.println(d.toString() + " "+wins.get(d.toString()) + " " + games.get(d.toString()));
			}
			writer.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
	
}