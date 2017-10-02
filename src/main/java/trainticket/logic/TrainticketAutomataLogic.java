package trainticket.logic;

import trainticket.automata.ITrainticketAutomata;
import trainticket.automata.TrainTicketAutomata;
import trainticket.userinterface.ConsoleOutputInterface;
import trainticket.userinterface.IUserInputInterface;

public class TrainticketAutomataLogic {

	private static TrainticketAutomataLogic instance = null;
	private ITrainticketAutomata trainticketAutomata = null;
	
	private TrainticketAutomataLogic() {

	}
	
	public static TrainticketAutomataLogic getInstance() {

		if (instance == null) {
			instance = new TrainticketAutomataLogic();
		}

		return instance;

	}
	
	public void setUserInterface(IUserInputInterface userInterface) {
		trainticketAutomata = new TrainTicketAutomata(userInterface, new ConsoleOutputInterface());
	}

	public void start() {		

		if (trainticketAutomata == null) {
			trainticketAutomata = new TrainTicketAutomata();
		}
		
		while (true) {
			int function = chooseFunction();

			// internet ticket
			if (function == 0) {
				internetTicket();
			}
			// ticket purchase
			else if (function == 1) {
				ticketPurchase();
			}
		}
	}

	private int chooseFunction() {

		int function = trainticketAutomata.chooseFunction();
		while (function != 0 && function != 1) { // De Morgen, it can be tricky
			function = trainticketAutomata.chooseFunction();
		}

		return function;
	}

	private void ticketPurchase() {
		boolean fromChecked = false;
		while (!fromChecked) {
			fromChecked = trainticketAutomata.fromStation();
		}

		boolean toChecked = false;
		while (!toChecked) {
			toChecked = trainticketAutomata.toStation();
		}

		boolean hasEmptySeat = false;
		while (!hasEmptySeat) {

			boolean leavingChecked = false;
			while (!leavingChecked) {
				leavingChecked = trainticketAutomata.leavingTime();
			}

			hasEmptySeat = trainticketAutomata.hasEmptySeat();

		}

		boolean paymentSuccessful = false;
		while (!paymentSuccessful) {
			paymentSuccessful = trainticketAutomata.payment();
		}

		trainticketAutomata.printTicket();
	}

	private void internetTicket() {

		boolean validCode = trainticketAutomata.grantCode();

		while (!validCode) {
			validCode = trainticketAutomata.grantCode();
		}

		trainticketAutomata.printTicket();
	}

}
