package trainticket.userinterface;

public interface IUserInterface {
	
	// 0: Internet ticket
	// 1: ticket purchase
	int getFunction();
	
	String getCode();
	
	String getInitialStation();
	
	String getDestinationStation();
	
	String getLeavingTime();
	
	// 0: cash
	// 1: credit card
	String getPaymentType();
	
	int getCash();
	
	int getMoneyToTransfer();

}
