package trainticket.userinterface;

public class ConsoleOutputInterface implements IUserOutputInterface {

	@Override
	public void printTypeSeletionHelp() {
		 System.out.println("Please choose from services!");
		 System.out.println("0: Print ticket purchased on the internet");
		 System.out.println("1: Purchase ticket");		
	}
	
	@Override
	public void printGrantCode() {
		System.out.println("Write the 10 digits long code!");
	}

	@Override
	public void printFromStation() {
		System.out.println("Please choose the initial station!");
	}

	@Override
	public void printToStation() {
		System.out.println("Please choose the destination station!");	
	}

	@Override
	public void printLeavingTime() {
		System.out.println("Please choose the time for the train!");
		System.out.println("Every train travels from 7:00 to 21:00, in every 2 hours.");
	}
	
	@Override
	public void printNoMoreSeat(String from, String to, String time) {
		System.out.println("Train from " + from + " to " + to + " at " + time + " has no more seat.");
	}

	@Override
	public void printThanks() {
		System.out.println("Thank you for choosing the railway! Have a nice day!");		
	}

	@Override
	public void printPaymentSelectionHelp() {
		 System.out.println("Choose payment type!");
		 System.out.println("0: Credit card");
		 System.out.println("1: Cash");		
	}

	@Override
	public void printAmount(int amount) {
		System.out.println("Pay " + amount + " Ft!");
	}

	@Override
	public void printCannotReturnChange() {
		System.out.println("The automata cannot return the change.");
	}

	@Override
	public void printChange(int change) {
		System.out.println("Money given back: " + change);
	}

}
