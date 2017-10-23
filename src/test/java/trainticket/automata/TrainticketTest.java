package trainticket.automata;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;
import org.junit.Assert;

import trainticket.enums.Function;
import trainticket.enums.PaymentType;


@GraphWalker(value = "random(edge_coverage(100))")
public class TrainticketTest extends ExecutionContext implements TrainticketTester {
	
	private static final String source = "src/test/resources/codes.txt";
	private static final String target = "src/main/resources/codes.txt";
	
	ITestInterface sut; 
	private int id = 0;
	private Function function;
	private boolean code;
	private boolean to;
	private boolean from;
	private boolean time;
	private PaymentType paymentType;
	private boolean paymentCash;
	private boolean paymentCreditcard;
	private boolean printed;
	
	private final String validFrom = "Budapest";
	private final String validTo = "Szeged";
	private final String validTime = "7:00";
	
	private Integer vc = 0;
	
	@Override
	public void e_Init() {
		Path codePathSource = Paths.get(source);
		Path codePathTarget = Paths.get(target);
		
		try {
			Files.copy(codePathSource, codePathTarget, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}

		sut = new TrainTicketAutomata();
	}

	@Override
	public void v_chooseFunction() {
		System.out.println("Choose function!");
		
		code= false;
		to = false;
		from = false;
		time = false;
		paymentCash = false;
		paymentCreditcard = false;
		printed = false;
	}
	
	@Override
	public void e_chooseInternetTicket() {
		function = sut.chooseFunction(Function.INTERNET_TICKET);
	}
	
	@Override
	public void v_internetTicket() {
		Assert.assertEquals(function, Function.INTERNET_TICKET);
	}
	
	@Override
	public void e_invalidCode() {
		sut.grantCode("invalid code");
	}
	
	/**
	 * Magic. 
	 * 1 -> 0000000001
	 * 2 -> 0000000002
	 * ...
	 * 10 -> 0000000010
	 */
	private String getNextValidCode() {
		StringBuilder sb = new StringBuilder();
		vc++;
		for (int i = 0; i < 10 - vc.toString().length(); i++) {
			sb.append("0");
		}
		
		sb.append(vc.toString());
		
		return sb.toString();
	}
	
	@Override
	public void e_validCode() {
		code = sut.grantCode(getNextValidCode());
	}

	@Override
	public void v_codeValid() {
		Assert.assertTrue(code);
	}

	@Override
	public void e_chooseTicketPurchase() {
		function = sut.chooseFunction(Function.PURCHASE_TICKET);
	}
	
	@Override
	public void v_from() {
		Assert.assertEquals(function, Function.PURCHASE_TICKET);
	}

	@Override
	public void e_fromInvalid() {
		sut.fromStation("invalid from");
	}
	
	@Override
	public void e_fromValid() {
		from = sut.fromStation(validFrom);
	}
	
	@Override
	public void v_to() {
		Assert.assertTrue(from);
	}
	
	@Override
	public void e_toInvalid() {
		sut.toStation(validFrom); // because the from and to cannot be the same
	}	

	@Override
	public void e_toValid() {
		to = sut.toStation(validTo);
	}
	
	@Override
	public void v_when() {
		Assert.assertTrue(to);
	}

	@Override
	public void e_whenInvalid() {
		sut.leavingTime("invalid time");
	}
	
	@Override
	public void e_whenValid() {
		time = sut.leavingTime(validTime);
	}
	
	@Override
	public void v_choosePaymentType() {
		Assert.assertTrue(time);
	}

	@Override
	public void e_cash() {
		paymentType = sut.paymentType(PaymentType.CASH);
	}
	
	@Override
	public void v_cash() {
		Assert.assertEquals(paymentType, PaymentType.CASH);
	}

	@Override
	public void e_cashError() {
		sut.payWithCash(-1);
	}

	@Override
	public void e_creditcard() {
		paymentType = sut.paymentType(PaymentType.CREDITCARD);
	}
	
	@Override
	public void v_creditcard() {
		Assert.assertEquals(paymentType, PaymentType.CREDITCARD);
	}
	
	@Override
	public void e_creditcardError() {
		sut.payWithCreditCard("not a valid creditcard number");	
	}

	@Override
	public void e_payingWithCash() {
		paymentCash = sut.payWithCash(5000);
	}
	
	
	@Override
	public void e_payingByCreditcard() {
		paymentCreditcard = sut.payWithCreditCard("00000000-00000000");
	}
	
	@Override
	public void v_paymentSuccessful() {
		boolean success = paymentCash || paymentCreditcard;
		Assert.assertTrue(success);
	}

	@Override
	public void e_printPurchaseTicket() {
		id++;
		printed = sut.printTicket((new Integer(id)).toString());
	}
	
	@Override
	public void e_printInternetTicket() {
		id++;
		printed = sut.printTicket((new Integer(id)).toString());
	}
	
	@Override
	public void v_printTicket() {
		Assert.assertTrue(printed);
	}

	@Override
	public void e_returnToWaiting() {
		sut.setToDefault();
	}

}
