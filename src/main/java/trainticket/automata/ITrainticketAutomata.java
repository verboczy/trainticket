package trainticket.automata;

import trainticket.enums.Function;
import trainticket.enums.PaymentType;

public interface ITrainticketAutomata {

	public Function chooseFunction(Function function);
	
	public boolean grantCode(String code);
	
	public boolean fromStation(String from);
	
	public boolean toStation(String to);
	
	public boolean leavingTime(String time);
	
	public PaymentType paymentType(PaymentType paymentType);
	
	public boolean payWithCash(int amount);
	
	public boolean payWithCreditCard(int moneyToTransfer);
	
	public boolean printTicket();
	
}
