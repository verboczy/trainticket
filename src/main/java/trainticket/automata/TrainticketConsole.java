package trainticket.automata;

import java.util.Scanner;

import trainticket.enums.Function;

public class TrainticketConsole {
	
	public static void main(String[] args) {

		boolean exit = false;
		Scanner scanner = new Scanner(System.in);
		ITrainticketAutomata automata = new TrainTicketAutomata();
		
		System.out.println("yeeeeeeeeeeeeeaaaaaaaaaaaaah");
		
		while (!exit) {
			System.out.println("Choose function: ");
			String function = scanner.nextLine();
			if ("exit".equals(function)) {
				System.exit(0);
			}
			Function chosenFucntion = automata.chooseFunction(Function.valueOf(function.toUpperCase()));
			if (chosenFucntion == Function.INTERNET_TICKET) {
				System.out.println("Grant your code: ");
				String code = scanner.nextLine();
				if (automata.grantCode(code)) {
					if (automata.printTicket(code)) {
						System.out.println("Ticket printed. Thank you for chosing railways!");
					}
				}
				else {
					System.out.println("Invalid code!");
				}
			}
			else if (chosenFucntion == Function.PURCHASE_TICKET) {
				// TODO
			}
			else {
				System.out.println("Not valid function! Valid functions: internet_ticket, purchase_ticket");
			}
		}
		
		scanner.close();
	}
	
}
