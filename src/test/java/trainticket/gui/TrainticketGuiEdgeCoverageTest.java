package trainticket.gui;

import org.graphwalker.java.annotation.GraphWalker;
import org.slf4j.LoggerFactory;


/**
 * Class for 100% edge coverage.
 * @author verboczy
 *
 */
@GraphWalker(value = "random(edge_coverage(100))")
public class TrainticketGuiEdgeCoverageTest extends TrainticketGuiTest {
	
	
	public TrainticketGuiEdgeCoverageTest() {
		
		super(LoggerFactory.getLogger(TrainticketGuiEdgeCoverageTest.class));
	}
	
}
