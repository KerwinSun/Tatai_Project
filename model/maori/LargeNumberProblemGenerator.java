package model.maori;

import java.util.Random;

/**
 * This class randomly generates numbers between 1 and 99
 */
public class LargeNumberProblemGenerator extends MaoriNumberMathProblemGenerator {
	public LargeNumberProblemGenerator() {
		lowrange = 1;
		highrange = 99;
		
		// By default, the generator has a reference to a random object
		rng = new Random();
	}
}
