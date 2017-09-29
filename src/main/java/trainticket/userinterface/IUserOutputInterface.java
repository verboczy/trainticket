package trainticket.userinterface;

public interface IUserOutputInterface {

	void printTypeSeletionHelp();
	
	void printGrantCode();
	
	void printFromStation();
	
	void printToStation();
	
	void printLeavingTime();
	
	void printNoMoreSeat(String from, String to, String time);
	
	void printThanks();
	
	void printPaymentSelectionHelp();
	
	void printAmount(int amount);
	
	void printCannotReturnChange();
	
	void printChange(int change);
	
}
