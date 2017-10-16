package model.maori;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import model.Problem;
import model.ProblemGenerator;

public abstract class MaoriNumberMathProblemGenerator implements ProblemGenerator{

		private boolean wordmode =false;
		private boolean mathmode =true;
		private boolean summode = false;
		private boolean prodmode = false;
		private boolean divmode = false;
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
			
			
			//mathmode is true when the problems are math problems
			if (mathmode == false) {
				//the answer to the problem
				int value = rng.nextInt(highrange-lowrange+1) + lowrange;
				String maoriValue = int2maori(value);
				//just output the number
				if (wordmode == false) {
					return new Problem(genQuestion(value),maoriValue);
				}else {
					return new Problem(maoriValue,maoriValue);
				}
				
			}else {
				//loops until rng decides on problem type
				while (true) {
					int choice = rng.nextInt(3);
					if (choice == 0 && summode) {
						return newSumProblem();
					}else if(choice == 1 && prodmode) {
						return newProductProblem();
					}else if(choice == 2 && divmode) {
						return newDivisionProblem();
					}
				}
				
				
			}
			
			
		}
		
		public Problem newSumProblem() {
			int value = rng.nextInt(highrange-lowrange+1) + lowrange;
			String maoriValue = int2maori(value);
			//this number and the answer will determine the second number
			int mathnum = rng.nextInt(highrange-lowrange+1) + lowrange;
			
			//can't have problem where operand and answer are the same
			if (mathnum==value) {
				
				//have to subtract is highrange 
				if (mathnum==highrange) {
					mathnum--;
				}else {
					mathnum++;
				}
			}
			int remain = value - mathnum;
			String equation;
			//if remain is negative do a subtraction problem other a addition problem
			if (remain < 0) {
				if (wordmode) {
					equation = int2maori(mathnum) + " - " + int2maori(Math.abs(remain));
				}else {
					equation = mathnum + " - " + Math.abs(remain);
				}
				
			}else {
				if (wordmode) {
					equation = int2maori(mathnum) + " + " + int2maori(remain);
				}else {
					equation = mathnum + " + " + remain;
				}
			}
			return new Problem(equation,maoriValue);
		}
		
		public Problem newProductProblem() {
			int value = rng.nextInt(highrange-lowrange+1) + lowrange;
			String maoriValue = int2maori(value);
			
			List<Integer> factors = new ArrayList<Integer>();
			for (int i=lowrange;i<=highrange;i++) {
				if (value % i == 0) {
					factors.add(i);
				}
	
			}
			String equation;
			
			int mathnum = factors.get(rng.nextInt(factors.size()));
			if (wordmode) {
				equation = int2maori(mathnum) + " x " + int2maori(value/mathnum);
			}else {
				equation = mathnum + " x " + value/mathnum;
			}
				
			
			
			return new Problem(equation,maoriValue);
		}
		public Problem newDivisionProblem() {
			int value = rng.nextInt(highrange-lowrange+1) + lowrange;
			String maoriValue = int2maori(value);
			
			
			List<Integer> divisors = new ArrayList<Integer>();
			for (int i=lowrange;i<=highrange;i++) {
				if (i%value==0) {
					divisors.add(i);
				}
			}
			String equation;
			
			int mathnum = divisors.get(rng.nextInt(divisors.size()));
			if (wordmode) {
				equation = int2maori(mathnum) + " / " + int2maori(mathnum/value);
			}else {
				equation = mathnum + " / " + mathnum/value;
			}
			
			
			return new Problem(equation,maoriValue);
		}
		public void wordMode(boolean mode) {
			wordmode=mode;
		}
		public void mathMode(boolean mode) {
			mathmode=mode;
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

		public void sumProbs(boolean sumprob) {
			summode=sumprob;
			
		}

		public void multiProbs(boolean multiprob) {
			prodmode=multiprob;
			
		}

		public void divProbs(boolean divprob) {
			divmode=divprob;
			
		}
	}
