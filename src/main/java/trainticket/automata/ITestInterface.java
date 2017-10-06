package trainticket.automata;

import java.util.Map;

public interface ITestInterface extends ITrainticketAutomata {
	
	public void setToDefault();
	
	public void setChangeCashInAutomata(Map<Integer, Integer> changeCashMap);
	
}
