package trainticket.creditcard;

import java.util.regex.Pattern;

public class CreditcardPayment implements ICreditcardPayment {

	@Override
	public boolean pay(String cardNumber) {

		StringBuilder sb = new StringBuilder();
		
		// Valid card number formats:
		// 00000000-00000000 or
		// 00000000-00000000-00000000
		sb.append("\\d{8}").append("-").append("\\d{8}").append("(-\\d{8})?");
		
		return Pattern.matches(sb.toString(), cardNumber);
		
	}

}
