package trainticket.gui;

import org.graphwalker.java.annotation.GraphWalker;
import org.slf4j.LoggerFactory;


/**
 * Class for running GraphWalker for 60 seconds.
 * @author verboczy
 *
 */
//@GraphWalker(value = "random(time_duration(60))")
public class TrainticketGuiOneMinuteTest extends TrainticketGuiTest {

	// TODO - failing because of the code.txt does not have so many elements (not a problem, but good to know)
	
	public TrainticketGuiOneMinuteTest() {
		super(LoggerFactory.getLogger(TrainticketGuiOneMinuteTest.class));
	}
	
}
