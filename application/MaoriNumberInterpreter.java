package application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class can take a recording interpret spoken Maori numbers by running 
 * voice recognition software on a recording.
 */
public class MaoriNumberInterpreter {
	
	private BashProcess audiorecorder;
	private BashProcess htkScript;
	private int audioexit;
	private List<InterpreterListener> listeners;
	private Hashtable<String,String> htkWordMap;
	
	public MaoriNumberInterpreter() {
		htkWordMap = new Hashtable<String,String>();
		htkWordMap.put("tahi", "tahi");
		htkWordMap.put("rua", "rua");
		htkWordMap.put("toru", "toru");
		htkWordMap.put("whaa", "whā");
		htkWordMap.put("rima", "rima");
		htkWordMap.put("ono", "ono");
		htkWordMap.put("whitu", "whitu");
		htkWordMap.put("waru", "waru");
		htkWordMap.put("iwa", "iwa");
		htkWordMap.put("tekau", "tekau");
		htkWordMap.put("maa", "mā");
		audiorecorder = new BashProcess();
		audiorecorder.addCommand("arecord -d 3 -r 22050 -c 1 -i -t wav -f s16_LE foo.wav");
		audiorecorder.setAfterTask(new AudioRecordTask());
		htkScript = new BashProcess();
		htkScript.addCommand("HVite -H ~/Documents/HTK/MaoriNumbers/HMMs/hmm15/macros -H ~/Documents/HTK/MaoriNumbers/HMMs/hmm15/hmmdefs -C ~/Documents/HTK/MaoriNumbers/user/configLR  -w ~/Documents/HTK/MaoriNumbers/user/wordNetworkNum -o SWT -l '*' -i recout.mlf -p 0.0 -s 5.0  ~/Documents/HTK/MaoriNumbers/user/dictionaryD ~/Documents/HTK/MaoriNumbers/user/tiedList foo.wav");
		htkScript.setAfterTask(new HTKTask());
		listeners=new LinkedList<InterpreterListener>();

	}
	
	/**
	 * Adds a listener to the Maori number interpreter
	 * @param l the listener
	 */
	public void addListener(InterpreterListener l) {
		listeners.add(l);
	}

	/**
	 * Takes a three second recording. Any existing recordings will be
	 * overwritten.
	 */
	public void recordAudio() {
		for (InterpreterListener l:listeners) {
			l.recordStart();
		}
		try {
			audiorecorder.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Plays the three second recording
	 */
	public void playbackRecording() {
		BashProcess ffplay = new BashProcess();
		ffplay.addCommand("ffplay foo.wav -nodisp -autoexit");
		ffplay.execute();
	}

	/**
	 * Converts the three second recording into a string of Maori numbers
	 */
	public void runPharser() {
		try {
			htkScript.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This class informs listeners that a voice recording has finished when
	 * the run() method is called.
	 */
	private class AudioRecordTask implements Runnable {

		public void run() {

			audioexit = audiorecorder.getExitStatus();
			if (audioexit == 0) {
				for (InterpreterListener l:listeners) {
					l.recordEnd();
				}

			} else {
				System.out.println("problem with recording");
			}

		}

	}
	
	/**
	 * This class runs voice recognition software and returns a string
	 * representation of the Maori numbers recognised, when the run() method
	 * is called.
	 */
	private class HTKTask implements Runnable {

		public void run() {
			BufferedReader br = null;
			String saidMaori = null;
			try {
				br = new BufferedReader(new FileReader("recout.mlf"));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			
			try {
				StringBuilder st = new StringBuilder();
				String line = br.readLine();
				boolean saidText = false;
				while (line != null) {
					if (saidText && !line.equals("sil")) {
						st.append(htkWordMap.get(line));
						st.append(" ");

					}
					// saidText becomes true after first sil and false after second
					saidText = (saidText != (line.equals("sil")));

					line = br.readLine();
				}
				if (st.length()>0) {
					st.deleteCharAt(st.length()-1);
				}
				
				saidMaori = st.toString();
			
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			for (InterpreterListener l:listeners) {
				l.interpretedText(saidMaori);
			}
		}

	}
}