package trainticket.creditcard;

public interface ICreditCardPayment {

	/**
	 * Payment with credit card.
	 * @param cardNumber
	 * @return true if payment was successful, false otherwise
	 */
	public boolean pay(String cardNumber);
	
}
