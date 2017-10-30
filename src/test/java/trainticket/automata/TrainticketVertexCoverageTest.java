package trainticket.automata;

import org.graphwalker.java.annotation.GraphWalker;
import org.slf4j.LoggerFactory;

/**
 * Class for 100% vertex coverage.
 * @author verboczy
 *
 */
@GraphWalker(value = "random(vertex_coverage(100))")
public class TrainticketVertexCoverageTest extends TrainticketTest {
	
	public TrainticketVertexCoverageTest() {
		
		super(LoggerFactory.getLogger(TrainticketVertexCoverageTest.class));
	}
}
