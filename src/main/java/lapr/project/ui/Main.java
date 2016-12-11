package lapr.project.ui;

import java.util.logging.Level;
import java.util.logging.Logger;
import lapr.project.model.CalculatorExample;

/**
 * @author Nuno Bettencourt <nmb@isep.ipp.pt> on 24/05/16.
 */
class Main {

	/**
	 * Logger class.
	 */
	private static final Logger LOGGER = Logger.getLogger("MainLog");

	/**
	 * Private constructor to hide implicit public one.
	 */
	private Main() {

	}

	/**
	 * Application main method.
	 * 
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		CalculatorExample calculatorExample = new CalculatorExample();
		int value = calculatorExample.sum(3, 5);
		LOGGER.log(Level.INFO, String.valueOf(value));

	}
}
