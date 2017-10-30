package trainticket.creditcard;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Test class used for unit testing the credit card's payment functionality.
 * @author verboczy
 *
 */
@RunWith(Parameterized.class)
public class TestCreditCardPayment {
	
	private Logger logger = LoggerFactory.getLogger(TestCreditCardPayment.class);
	
	private String name;
	private String cardNumber;
	private boolean expected;
	private CreditCardPayment sut;
	
	public TestCreditCardPayment(String name, String cardNumber, boolean expected) {
		
		this.name = name;
		this.cardNumber = cardNumber;
		this.expected = expected;
		
		sut = new CreditCardPayment();
	}
	
	@Parameters(name = "{0}")
	public static List<Object[]> data() {
		return Arrays.asList(new Object[][] {
				 {"containsCharacter_False", "00000000-0000000a", false},
				 {"tooShort_False", "0000000-0000000", false},
				 {"tooFewBlocks_False", "00000000", false},
				 {"tooLong_False", "000000000-000000000", false},
				 {"tooManyBlocks_False", "00000000-00000000-00000000-00000000", false},
				 {"twoBlocksValid_True", "00000000-00000000", true},
				 {"threeBlocksValid_True", "00000000-00000000-00000000", true}
		});
	}
	
	@Test
	public void testCreditCardNumberValidity() {
		
		boolean actual = sut.pay(cardNumber);
		
		logger.info(name + " expected to be " + expected + ", actually: " + actual);
		
		assertEquals(expected, actual);
	}
	
}