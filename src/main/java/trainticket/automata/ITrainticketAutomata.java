package trainticket.automata;

import trainticket.enums.Function;
import trainticket.enums.PaymentType;

public interface ITrainticketAutomata {

	/**
	 * Method for choosing the functionality of the automata.
	 * @param function can be INTERNET_TICKET or PURCHASE_TICKET
	 * @return the chosen function
	 */
	public Function chooseFunction(Function function);
	
	/**
	 * Method for granting the code.
	 * @param code of the internet ticket
	 * @return true if the code is valid, false otherwise
	 */
	public boolean grantCode(String code);
	
	/**
	 * Method for granting the initial station.
	 * @param from the initial station
	 * @return true if the given station exists, false otherwise
	 */
	public boolean fromStation(String from);
	
	/**
	 * Method for granting the destination station.
	 * @param to the destination station
	 * @return true if the given station exists and not equal to "from" station, false otherwise
	 */
	public boolean toStation(String to);
	
	/**
	 * Method for granting the leaving time of the train.
	 * @param time the leaving time of the train
	 * @return true if the train leaves in the given time, false otherwise
	 */
	public boolean leavingTime(String time);
	
	/**
	 * Method for choosing the type of the payment.
	 * @param paymentType can be CASH or CREDITCARD
	 * @return the chosen type
	 */
	public PaymentType paymentType(PaymentType paymentType);
	
	/**
	 * Method for payment with cash.
	 * @param amount of money
	 * @return true if the payment was successful, false otherwise
	 */
	public boolean payWithCash(int amount);
	
	/**
	 * Method for payment with credit card.
	 * @param cardNumber the number of the card
	 * @return true if the payment was successful, false otherwise
	 */
	public boolean payWithCreditCard(String cardNumber);
	
	/**
	 * Method for printing the ticket.
	 * @param ticketId is the unique id of the ticket
	 * @return true if the ticket was printed successfully, false otherwise
	 */
	public boolean printTicket(String ticketId);
	
	/**
	 * Exit means returning to the starting point of ticket automata.
	 */
	public void exit();
	
}
