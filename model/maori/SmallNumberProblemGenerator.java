package model.maori;

import java.util.Random;

/**
 * This class randomly generates numbers between 1 and 10
 */
public class SmallNumberProblemGenerator extends MaoriNumberMathProblemGenerator{
	public SmallNumberProblemGenerator() {
		lowrange = 1;
		highrange = 10;
		
		// By default, the generator has a reference to a random object
		rng = new Random();
	}
}
