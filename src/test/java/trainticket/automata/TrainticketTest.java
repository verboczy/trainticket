package trainticket.automata;

import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;


@GraphWalker(value = "random(edge_coverage(100))")
public class TrainticketTest extends ExecutionContext implements TrainticketTester {

	@Override
	public void e_returnToWaiting() {
		
	}

	@Override
	public void v_chooseFunction() {
		System.out.println("Choose function!");
	}

	@Override
	public void e_toInvalid() {
		
	}


	@Override
	public void e_whenInvalid() {
		
	}

	@Override
	public void e_cash() {
		
	}

	@Override
	public void e_cashError() {
		
	}

	@Override
	public void e_toValid() {
		
	}

	@Override
	public void v_to() {
		System.out.println("TO station");
	}

	@Override
	public void e_creditCardError() {
		// TODO Auto-generated method stub

	}

	@Override
	public void e_chooseInternetTicket() {
		// TODO Auto-generated method stub
	}

	@Override
	public void v_from() {
		// TODO Auto-generated method stub

	}

	@Override
	public void e_fromInvalid() {
		// TODO Auto-generated method stub

	}

	@Override
	public void v_printTicket() {
		// TODO Auto-generated method stub

	}

	@Override
	public void v_creditcard() {
		// TODO Auto-generated method stub

	}

	@Override
	public void v_cash() {
		// TODO Auto-generated method stub

	}

	@Override
	public void e_fromValid() {
		// TODO Auto-generated method stub

	}

	@Override
	public void e_chooseTicketPurchase() {
		// TODO Auto-generated method stub
	}


	@Override
	public void e_invalidCode() {
		// TODO Auto-generated method stub

	}

	@Override
	public void v_when() {
		// TODO Auto-generated method stub

	}

	@Override
	public void v_internetTicket() {
		// TODO Auto-generated method stub

	}

	@Override
	public void e_whenValid() {
		// TODO Auto-generated method stub

	}

	@Override
	public void v_choosePaymentType() {
		// TODO Auto-generated method stub

	}

	@Override
	public void e_creditcard() {
		// TODO Auto-generated method stub

	}

	@Override
	public void e_Init() {
		
	}

	@Override
	public void e_printInternetTicket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void e_printCashTicket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void e_printCreditcardTicket() {
		// TODO Auto-generated method stub
		
	}

}
