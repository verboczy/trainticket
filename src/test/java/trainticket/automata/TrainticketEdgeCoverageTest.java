package trainticket.automata;

import org.graphwalker.java.annotation.GraphWalker;
import org.slf4j.LoggerFactory;


/**
 * Class for 100% edge coverage.
 * @author verboczy
 *
 */
//@GraphWalker(value = "random(edge_coverage(100))")
public class TrainticketEdgeCoverageTest extends TrainticketTest {
	
	
	/**
	 * Constructor without parameters, for setting the logger of the ancestor.
	 */
	public TrainticketEdgeCoverageTest() {
	
		  super(LoggerFactory.getLogger(TrainticketVertexCoverageTest.class));
	}
	
}
