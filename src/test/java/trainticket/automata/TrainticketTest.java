package trainticket.automata;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.graphwalker.core.machine.ExecutionContext;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trainticket.enums.Function;
import trainticket.enums.PaymentType;


/**
 * Class for testing the application using GraphWalker.
 * @author verboczy
 *
 */
public class TrainticketTest extends ExecutionContext implements TrainticketTester {
	
	Logger logger = LoggerFactory.getLogger(TrainticketTest.class);
	
	private static final String source = "src/test/resources/codes.txt";
	private static final String target = "src/main/resources/codes.txt";
	private static final String ticketFolder = "src/test/resources/ticket";
	
	ITrainticketAutomata sut; 
	private int id = 0;
	private boolean setUpSuccess;
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
	private final String validTime = "7-00";
	
	private Integer vc = 0;
	

	/**
	 * Constructor without parameters.
	 */
	public TrainticketTest() {
		// default constructor
	}
	
	/**
	 * Constructor for setting the Logger.
	 * @param logger
	 */
	public TrainticketTest(Logger logger) {
		
		this.logger = logger;
	}
	
	@Override
	public void e_Init() {
		
		logger.info("e_Init()");
		
		code= false;
		to = false;
		from = false;
		time = false;
		paymentCash = false;
		paymentCreditcard = false;
		printed = false;
		
		setUpSuccess = setUp();

		sut = new TrainTicketAutomata(target, "src/main/resources/stations.txt", ticketFolder);
		
	}
	
	private boolean setUp() {
		
		logger.debug("Entering setUp()");
		
		boolean alright = true;
		
		Path codePathSource = Paths.get(source);
		Path codePathTarget = Paths.get(target);
		
		alright = deleteTickets();
		
		try {
			Files.copy(codePathSource, codePathTarget, REPLACE_EXISTING);			
			
		} catch (IOException e) {
			alright = false;
			logger.error(e.getMessage());
		}
		
		return alright;
	}
	
	private boolean deleteTickets() {
		
		logger.debug("Entering deleteTickets()");
		
		boolean alright = true;
		
		File dir = new File(ticketFolder);
		if (dir.exists()) {
		
			for (File file : dir.listFiles()) {
				alright = file.delete();
			}
			
		}
				
		return alright;
	}

	@Override
	public void v_chooseFunction() {
		
		logger.info("v_chooseFunction()");
		
		Assert.assertTrue(setUpSuccess);
	}
	
	@Override
	public void e_chooseInternetTicket() {
		
		logger.info("e_chooseInternetTicket()");
		
		function = sut.chooseFunction(Function.INTERNET_TICKET);
	}
	
	@Override
	public void v_internetTicket() {
		
		logger.info("v_internetTicket()");
		
		Assert.assertEquals(function, Function.INTERNET_TICKET);
	}
	
	@Override
	public void e_invalidCode() {
		
		logger.info("e_invalidCode()");
		
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
		
		logger.debug("getNextValidCode()");
		
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
		
		logger.info("e_validCode()");
		
		code = sut.grantCode(getNextValidCode());
	}

	@Override
	public void v_codeValid() {
		
		logger.info("v_codeValid()");
		
		Assert.assertTrue(code);
	}

	@Override
	public void e_chooseTicketPurchase() {
		
		logger.info("e_chooseTicketPurchase()");
		
		function = sut.chooseFunction(Function.PURCHASE_TICKET);
	}
	
	@Override
	public void v_from() {
		
		logger.info("v_from()");
		
		Assert.assertEquals(function, Function.PURCHASE_TICKET);
	}

	@Override
	public void e_fromInvalid() {
		
		logger.info("e_fromInvalid()");
		
		sut.fromStation("invalid from");
	}
	
	@Override
	public void e_fromValid() {
		
		logger.info("e_fromValid()");
		
		from = sut.fromStation(validFrom);
	}
	
	@Override
	public void v_to() {
		
		logger.info("v_to()");
		
		Assert.assertTrue(from);
	}
	
	@Override
	public void e_toInvalid() {
		
		logger.info("e_toInvalid()");
		
		sut.toStation(validFrom); // because the from and to cannot be the same
	}	

	@Override
	public void e_toValid() {
		
		logger.info("e_toValid()");
		
		to = sut.toStation(validTo);
	}
	
	@Override
	public void v_when() {
		
		logger.info("v_when()");
		
		Assert.assertTrue(to);
	}

	@Override
	public void e_whenInvalid() {
		
		logger.info("e_whenInvalid()");
		
		sut.leavingTime("invalid time");
	}
	
	@Override
	public void e_whenValid() {
		
		logger.info("e_whenValid()");
		
		time = sut.leavingTime(validTime);
	}
	
	@Override
	public void v_choosePaymentType() {
		
		logger.info("v_choosePaymentType()");
		
		Assert.assertTrue(time);
	}

	@Override
	public void e_cash() {
		
		logger.info("e_cash()");
		
		paymentType = sut.paymentType(PaymentType.CASH);
	}
	
	@Override
	public void v_cash() {
		
		logger.info("v_cash()");
		
		Assert.assertEquals(paymentType, PaymentType.CASH);
	}

	@Override
	public void e_cashError() {
		
		logger.info("e_cashError()");
		
		sut.payWithCash(-1);
	}

	@Override
	public void e_creditcard() {
		
		logger.info("e_creditcard()");
		
		paymentType = sut.paymentType(PaymentType.CREDITCARD);
	}
	
	@Override
	public void v_creditcard() {
		
		logger.info("v_creditcard()");
		
		Assert.assertEquals(paymentType, PaymentType.CREDITCARD);
	}
	
	@Override
	public void e_creditcardError() {
		
		logger.info("e_creditcardError()");
		
		sut.payWithCreditCard("not a valid creditcard number");	
	}

	@Override
	public void e_payingWithCash() {
		
		logger.info("e_payingWithCash()");
		
		paymentCash = sut.payWithCash(5000);
	}
	
	
	@Override
	public void e_payingByCreditcard() {
		
		logger.info("e_payingByCreditcard()");
		
		paymentCreditcard = sut.payWithCreditCard("00000000-00000000");
	}
	
	@Override
	public void v_paymentSuccessful() {
		
		logger.info("v_paymentSuccessful()");
		
		boolean success = paymentCash || paymentCreditcard;
		Assert.assertTrue(success);
	}

	@Override
	public void e_printPurchaseTicket() {
		
		logger.info("e_printPurchaseTicket()");
		
		id++;
		printed = sut.printTicket((new Integer(id)).toString());
	}
	
	@Override
	public void e_printInternetTicket() {
		
		logger.info("e_printInternetTicket()");
		
		id++;
		printed = sut.printTicket((new Integer(id)).toString());
	}
	
	@Override
	public void v_printTicket() {
		
		logger.info("v_printTicket()");
		
		Assert.assertTrue(printed);
	}

	@Override
	public void e_returnToWaiting() {
		
		logger.info("e_returnToWaiting()");
		
		sut.exit();
	}

}
