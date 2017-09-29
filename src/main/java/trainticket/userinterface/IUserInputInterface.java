package trainticket.userinterface;

public interface IUserInputInterface {
	
	// 0: Internet ticket
	// 1: ticket purchase
	int getFunction();
	
	String getCode();
	
	String getInitialStation();
	
	String getDestinationStation();
	
	String getLeavingTime();
	
	// 0: credit card
	// 1: cash
	String getPaymentType();
	
	int getCash();
	
	int getMoneyToTransfer();

}
