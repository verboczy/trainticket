package trainticket.gui;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.graphwalker.core.machine.ExecutionContext;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trainticket.automata.TrainticketTester;

/**
 * Class for testing the GUI application with Sikuli, based on GraphWalker.
 * @author verboczy
 *
 */
public class TrainticketGuiTest extends ExecutionContext implements TrainticketTester {
	
	private Logger logger = LoggerFactory.getLogger(TrainticketGuiTest.class);
	
	private Screen screen = new Screen();
	
	private Pattern imageCashRadioButton = new Pattern("src/test/resources/images/cashRadioButton.PNG");
	private Pattern imageCreditcardRadioButton = new Pattern("src/test/resources/images/creditcardRadioButton.PNG");
	private Pattern imageInternetTicket = new Pattern("src/test/resources/images/internetticket.PNG");
	private Pattern imagePrint = new Pattern("src/test/resources/images/print.PNG");
	private Pattern imageTicketPurchase = new Pattern("src/test/resources/images/ticketpurchase.PNG");
	private Pattern imageAlert = new Pattern("src/test/resources/images/alertok.PNG");
	
	private Pattern codeMatch = new Pattern("src/test/resources/images/codematch.PNG");
	private Pattern purchaseMatch = new Pattern("src/test/resources/images/purchasematch.PNG");
	private Pattern cashMatch = new Pattern("src/test/resources/images/cashmatch.PNG");
	private Pattern creditcardMatch = new Pattern("src/test/resources/images/creditcardmatch.PNG");
	
	private static final int deltaX = 120; 
	private static final float similarity = 0.75f;
	
	private static final String source = "src/test/resources/codes.txt";
	private static final String target = "src/main/resources/codes.txt";
	private static final String ticketFolder = "src/test/resources/ticket";
		
	private final String validFrom = "Budapest";
	private final String validTo = "Szeged";
	private String validTime = "7-00";
	
	private Integer vc = 0;
	
	
	/**
	 * Constructor without parameters.
	 */
	public TrainticketGuiTest() {
		
	}
	
	/**
	 * Constructor for setting the Logger.
	 * @param logger
	 */
	public TrainticketGuiTest(Logger logger) {
		
		this.logger = logger;
	}	
	
	
	/**
	 * Setting some parameter according to SikuliX.
	 * - mouse delay time
	 * - similarity value of images
	 */
	private void setSikuliParameters() {
		
		logger.debug("setSikuliParameters()");
		
		Settings.MoveMouseDelay = 0;
		
		imageCashRadioButton.similar(similarity);
		imageCreditcardRadioButton.similar(similarity);
		imageInternetTicket.similar(similarity);
		imagePrint.similar(similarity);
		imageTicketPurchase.similar(similarity);
		imageAlert.similar(similarity);
		
		codeMatch.similar(similarity);
		purchaseMatch.similar(similarity);
	}
	
	private void handleFindFailed() {
		
		logger.error("handleFindFailed()");
		
		System.exit(1);
	}
	
	@Override
	public void e_Init() {
		
		logger.info("e_Init()");
		
		setSikuliParameters();
		
		Path codePathSource = Paths.get(source);
		Path codePathTarget = Paths.get(target);
		
		deleteTickets();
		
		try {
			Files.copy(codePathSource, codePathTarget, REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// Start the GUI on a new thread
		Thread t = new Thread(new GuiStarterThread());
		t.setDaemon(true);
		t.start();
		
		// Wait for the window to appear
		try {
			screen.wait(imageInternetTicket, 10);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
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
	}
	
	@Override
	public void e_chooseInternetTicket() {
		
		logger.info("e_chooseInternetTicket()");
		
		try {
			screen.click(imageInternetTicket);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}

	}
	
	@Override
	public void v_internetTicket() {
		
		logger.info("v_internetTicket()");
		
		codeMatch.targetOffset(0, -45);
		
		try {
			screen.click(codeMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_invalidCode() {
		
		logger.info("e_invalidCode()");
				
		screen.write("invalid code");
		codeMatch.targetOffset(deltaX, -45);
		try {
			screen.click(codeMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
		okAlert();		
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
		
		screen.write(getNextValidCode());
		codeMatch.targetOffset(deltaX, -45);
		try {
			screen.click(codeMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
		
	}

	@Override
	public void v_codeValid() {
		
		logger.info("v_codeValid()");
	}

	@Override
	public void e_chooseTicketPurchase() {
		
		logger.info("e_chooseTicketPurchase()");

		try {
			screen.click(imageTicketPurchase);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_from() {
		
		logger.info("v_from()");
		
		purchaseMatch.targetOffset(0, -10);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
	}

	@Override
	public void e_fromInvalid() {
		
		logger.info("e_fromInvalid()");
		
		screen.write("invalid from");
		purchaseMatch.targetOffset(deltaX, -10);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
		okAlert();
	}
	
	@Override
	public void e_fromValid() {
		
		logger.info("e_fromValid()");
		
		screen.write(validFrom);
		purchaseMatch.targetOffset(deltaX, -10);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_to() {
		
		logger.info("v_to()");
		
		purchaseMatch.targetOffset(0, 30);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_toInvalid() {
		
		logger.info("e_toInvalid()");
		
		screen.write("invalid to");
		purchaseMatch.targetOffset(deltaX, 30);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
		okAlert();
	}	

	@Override
	public void e_toValid() {
		
		logger.info("e_toValid()");
		
		screen.write(validTo);
		purchaseMatch.targetOffset(deltaX, 30);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_when() {
		
		logger.info("v_when()");
		
		purchaseMatch.targetOffset(0, 60);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}

	@Override
	public void e_whenInvalid() {
		
		logger.info("e_whenInvalid()");
		
		screen.write("invalid time");
		purchaseMatch.targetOffset(deltaX, 60);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
		okAlert();
	}
	
	@Override
	public void e_whenValid() {
		
		logger.info("e_whenValid()");
		
		screen.write(validTime);
		purchaseMatch.targetOffset(deltaX, 60);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_choosePaymentType() {
		
		logger.info("v_choosePaymentType()");
	}

	@Override
	public void e_cash() {
		
		logger.info("e_cash()");
		
		try {
			screen.click(imageCashRadioButton);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_cash() {
		
		logger.info("v_cash()");
		
		cashMatch.targetOffset(0, -5);
		try {
			screen.click(cashMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}

	@Override
	public void e_cashError() {
		
		logger.info("e_cashError()");
		
		screen.write("-1");
		cashMatch.targetOffset(deltaX, -5);
		try {
			screen.click(cashMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
		okAlert();
	}

	@Override
	public void e_creditcard() {
		
		logger.info("e_creditcard()");
		
		try {
			screen.click(imageCreditcardRadioButton);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_creditcard() {
		
		logger.info("v_creditcard()");
		
		creditcardMatch.targetOffset(0, 30);
		try {
			screen.click(creditcardMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_creditcardError() {
		
		logger.info("e_creditcardError()");
		
		screen.write("not a valid creditcard number");
		creditcardMatch.targetOffset(deltaX, 30);
		try {
			screen.click(creditcardMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
		okAlert();
	}

	@Override
	public void e_payingWithCash() {
		
		logger.info("e_payingWithCash()");
		
		screen.write("5000");
		cashMatch.similar(0.5f);
		cashMatch.targetOffset(deltaX, -5);
		try {
			screen.click(cashMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		cashMatch.similar(similarity);
	}
	
	
	@Override
	public void e_payingByCreditcard() {
		
		logger.info("e_payingByCreditcard()");
		
		screen.write("00000000-00000000");
		creditcardMatch.similar(0.5f);
		creditcardMatch.targetOffset(deltaX, 30);
		try {
			screen.click(creditcardMatch);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		creditcardMatch.similar(similarity);
	}
	
	@Override
	public void v_paymentSuccessful() {
		
		logger.info("v_paymentSuccessful()");
	}

	@Override
	public void e_printPurchaseTicket() {
		
		logger.info("e_printPurchaseTicket()");
		
		try {
			screen.click(imagePrint);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_printInternetTicket() {
		
		logger.info("e_printInternetTicket()");
		
		try {
			screen.click(imagePrint);
		} catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_printTicket() {
		
		logger.info("v_printTicket()");
	}

	@Override
	public void e_returnToWaiting() {
		
		logger.info("e_returnToWaiting()");
	}
	
	/**
	 * Waits for the alert window to pop up, then click on the OK.
	 */
	private void okAlert() {
		
		logger.debug("okAlert()");
		
		try {
			screen.wait(imageAlert, 5);
			screen.click(imageAlert);
		}
		catch (FindFailed e) {
			logger.error(e.getMessage());
			handleFindFailed();
		}
		
	}
	
}
