package trainticket.automata;

public interface ITrainticketAutomata {

	public int chooseFunction();
	
	public boolean userExit();
	
	public boolean grantCode();
	
	public boolean fromStation();
	
	public boolean toStation();
	
	public boolean leavingTime();
	
	public boolean hasEmptySeat();
	
	public boolean payment();
	
	public String printTicket();
	
}
