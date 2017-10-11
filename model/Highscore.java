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
import java.util.HashMap;
import java.util.Map;

public class Highscore {




	//writes a highscore to highscore file
	public void addHighscore(String difficulty, int Score) throws IOException {

		try(FileWriter fw = new FileWriter("Highscores.txt", true);
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

			FileReader highScores = new FileReader("Highscores.txt");
			BufferedReader stdoutBuffered = new BufferedReader(highScores);
			String line;


			while((line = stdoutBuffered.readLine()) != null){


				if(line.contains(lookFor)) {

					returnableList.add(line.replace(lookFor, "") + "/10");

				}else {

				}

			}
			stdoutBuffered.close();


		}
		catch (IOException e) {

		}

		return returnableList;
	}

	//similar to gethighscore but returns an arraylist of integers
	public ArrayList<Integer> getHighscoreInt(String lookFor) {

		ArrayList<Integer> returnableList = new ArrayList<Integer>();

		try{

			FileReader highScores = new FileReader("Highscores.txt");
			BufferedReader stdoutBuffered = new BufferedReader(highScores);
			String line;


			while((line = stdoutBuffered.readLine()) != null){


				if(line.contains(lookFor)) {

					returnableList.add(Integer.parseInt(line.replace(lookFor, "")));

				}else {

				}

			}
			stdoutBuffered.close();


		}
		catch (IOException e) {

		}

		return returnableList;
	}
	//
	
	//similar to gethighscore but returns an arraylist of integers
	public int[] getWinTotal() {

		int gamesWon = 0;
		int gamesTotal = 0;

		ArrayList<Integer> TotalScores = this.getHighscoreInt("Easy");
		TotalScores.addAll(this.getHighscoreInt("Hard"));
		for(int score:TotalScores) {
			if(score >= 8) {

				gamesWon ++;

			}

		}

		gamesTotal = TotalScores.size();


		int[] WinTotalData = {gamesWon,gamesTotal};
		return WinTotalData;
	}
//----------------------------------------------------------------------------------------
	//methods to save/get scores end here
	//methods to save/get words + states begin here
	public void addWordsData(String state, String word) throws IOException {


		try(FileWriter fw = new FileWriter("WordData.txt", true);
				BufferedWriter bw = new BufferedWriter(fw);
				PrintWriter out = new PrintWriter(bw))
		{

			out.println(state+word);

		} catch (IOException e) {

		}


	}
	public ArrayList<String> getWordsData(String lookFor) throws IOException {

		ArrayList<String> returnableList = new ArrayList<String>();
		
		try{

			FileReader highScores = new FileReader("WordData.txt");
			BufferedReader stdoutBuffered = new BufferedReader(highScores);
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
	
	public String getWordFrequency(String state){
		
		ArrayList<String> failedWords = null;
		try {
			failedWords = this.getWordsData(state);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String,Integer> map = new HashMap<String, Integer>();
		
		for(String word: failedWords) {
			
			Integer count = map.get(word);       
            map.put(word, count==null?1:count+1);
			
		}
		
		int max = 0;
		String returnWord = "";
		
		for (Map.Entry<String, Integer> entry : map.entrySet())
		{
		   if(entry.getValue() > max) {
			   
			   returnWord = entry.getKey();
			   max = entry.getValue();
			   
		   }
		}
		System.out.print(returnWord);
		
		return returnWord;
		
		
		
	}

}
