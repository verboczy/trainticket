package trainticket.automata;

import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;

import trainticket.enums.Function;
import trainticket.enums.PaymentType;

import org.junit.Assert;


@GraphWalker(value = "random(edge_coverage(100))")
public class TrainticketTest extends ExecutionContext implements TrainticketTester {
	
	ITestInterface sut; 
	private int id = 0;
	private Function function;
	private boolean to;
	private boolean from;
	private boolean time;
	private PaymentType paymentType;
	private boolean printed;
	
	private final String validFrom = "Budapest";
	private final String validTo = "Szeged";
	private final String validTime = "7:00";
	
	@Override
	public void e_Init() {
		sut = new TrainTicketAutomata();
	}

	@Override
	public void v_chooseFunction() {
		System.out.println("Choose function!");
		
		to = false;
		from = false;
		time = false;
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
	public void e_creditCardError() {
		sut.payWithCreditCard(-1);
	}

	@Override
	public void e_printInternetTicket() {
		sut.grantCode("9876543210");
		id++;
		printed = sut.printTicket((new Integer(id)).toString());
	}

	@Override
	public void e_printCashTicket() {
		sut.payWithCash(5000);
		id++;
		printed = sut.printTicket((new Integer(id)).toString());
	}

	@Override
	public void e_printCreditcardTicket() {
		// TODO sut.payWithCreditCard(sut.getPrice());
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
