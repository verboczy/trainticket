package trainticket.automata;

import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;

import trainticket.logic.TrainticketAutomataLogic;
import trainticket.userinterface.TestInterface;


@GraphWalker(value = "random(edge_coverage(100))", start = "Start")
public class TrainticketTest extends ExecutionContext implements TrainticketTester {

	private TrainticketAutomataLogic sut = TrainticketAutomataLogic.getInstance();
	private TestInterface testInterface;

	public TrainticketTest() {
		testInterface = new TestInterface();
		sut.setUserInterface(testInterface);
	}

	@Override
	public void e_returnToWaiting() {
		testInterface.initialize();
	}

	@Override
	public void v_chooseFunction() {
		System.out.println("Choose function!");
	}

	@Override
	public void e_toInvalid() {
		testInterface.setDestStation("NoSuchCity");
	}

	@Override
	public void e_creditcardSuccessful() {
		
	}

	@Override
	public void e_whenInvalid() {
		testInterface.setLeavingTime("NoSuchTime");
	}

	@Override
	public void e_cash() {
		testInterface.setPaymentType("1");
	}

	@Override
	public void e_cashError() {
		testInterface.setCash(-1);
	}

	@Override
	public void e_toValid() {
		testInterface.setDestStation("Budapest");
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
	public void e_validCode() {
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
	public void e_cashSuccessful() {
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
		testInterface.initialize();
		sut.start();
	}

}
