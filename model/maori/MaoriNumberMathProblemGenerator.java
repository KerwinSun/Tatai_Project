package model.maori;

import java.util.Hashtable;
import java.util.Random;

import model.Problem;
import model.ProblemGenerator;

public abstract class MaoriNumberMathProblemGenerator implements ProblemGenerator{

		
		protected int lowrange; // Lowest supported value is 1
		protected int highrange; // Highest supported value is 99
		protected Random rng; // Dependency injection

		/**
		 * Sets the random number generator
		 * @param rng changes the random number generator 
		 */
		public void setRandomGenerator(Random rng) {
			this.rng = rng;
		}

		/**
		 * Returns a new problem
		 */
		public Problem newProblem() {
			int value = rng.nextInt(highrange-lowrange+1) + lowrange;
			String maoriValue = int2maori(value);
			return new Problem(genQuestion(value),maoriValue);
		}
		
		/**
		 * An enum that represents Maori digits.
		 */
		public enum MaoriNum {
			
			TAHI, RUA, TORU, WHAA, RIMA, ONO, WHITU, WARU, IWA, TEKAU, MAA;
			private Hashtable<String,String> htkWordMap;
			public String toString() {
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
				return htkWordMap.get(super.toString().toLowerCase());
			}
		}

		/**
		 * Returns a Maori string representation of an integer.
		 * @param value the integer to be converted to a Maori number
		 */
		protected String int2maori(int value) {

			MaoriNum[] nums = {MaoriNum.TEKAU, MaoriNum.TAHI, MaoriNum.RUA, MaoriNum.TORU,
					MaoriNum.WHAA, MaoriNum.RIMA, MaoriNum.ONO, MaoriNum.WHITU, 
					MaoriNum.WARU, MaoriNum.IWA};
			
			String result = "";

			if (value > 10) {

				// Add most significant digit for numbers larger than 19
				if (value > 19) {	
					result += nums[value/10] + " ";
				}

				// Add 'tekau ma' for numbers that are not multiples of 10
				if (value % 10 != 0){
					result += MaoriNum.TEKAU + " " + MaoriNum.MAA + " ";
				}

			}

			// Add least significant digit
			result += nums[value % 10];

			return result.toString();
		}

		/**
		 * Returns a string representation of a number problem
		 * @param answer the answer to the problem
		 * @return a string representation of the question
		 */
		protected String genQuestion(int answer) {
			return new Integer(answer).toString();
		}
	}
