package trainticket.gui;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.graphwalker.core.machine.ExecutionContext;
import org.graphwalker.java.annotation.GraphWalker;
import org.sikuli.basics.Settings;
import org.sikuli.script.FindFailed;
import org.sikuli.script.Pattern;
import org.sikuli.script.Screen;

import trainticket.automata.TrainticketTester;

@GraphWalker(value = "random(edge_coverage(100))")
public class TrainticketGuiTest extends ExecutionContext implements TrainticketTester {
	
	private Screen screen = new Screen();
	
	private Pattern imageCashRadioButton = new Pattern("src/test/resources/cashRadioButton.PNG");
	private Pattern imageCreditcardRadioButton = new Pattern("src/test/resources/creditcardRadioButton.PNG");
	private Pattern imageInternetTicket = new Pattern("src/test/resources/internetticket.PNG");
	private Pattern imagePrint = new Pattern("src/test/resources/print.PNG");
	private Pattern imageTicketPurchase = new Pattern("src/test/resources/ticketpurchase.PNG");
	
	private Pattern codeMatch = new Pattern("src/test/resources/codematch.PNG");
	private Pattern purchaseMatch = new Pattern("src/test/resources/purchasematch.PNG");
	private Pattern cashMatch = new Pattern("src/test/resources/cashmatch.PNG");
	private Pattern creditcardMatch = new Pattern("src/test/resources/creditcardmatch.PNG");
	
	private static final int deltaX = 120; 
	private static final float similarity = 0.75f;
	
	private static final String source = "src/test/resources/codes.txt";
	private static final String target = "src/main/resources/codes.txt";
		
	private final String validFrom = "Budapest";
	private final String validTo = "Szeged";
	private String validTime = "7-00";
	
	private Integer vc = 0;
	

	
	/**
	 * Setting some parameter according to SikuliX.
	 * - mouse delay time
	 * - similarity value of images
	 */
	private void setSikuliParameters() {
		Settings.MoveMouseDelay = 0;
		
		imageCashRadioButton.similar(similarity);
		imageCreditcardRadioButton.similar(similarity);
		imageInternetTicket.similar(similarity);
		imagePrint.similar(similarity);
		imageTicketPurchase.similar(similarity);
		
		codeMatch.similar(similarity);
		purchaseMatch.similar(similarity);
	}
	
	private void handleFindFailed() {
		System.exit(1);
	}
	
	@Override
	public void e_Init() {
		
		setSikuliParameters();
		
		Path codePathSource = Paths.get(source);
		Path codePathTarget = Paths.get(target);
		
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
			screen.wait(imageInternetTicket);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
	}

	@Override
	public void v_chooseFunction() {

	}
	
	@Override
	public void e_chooseInternetTicket() {
		
		try {
			screen.click(imageInternetTicket);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}

	}
	
	@Override
	public void v_internetTicket() {
		
		codeMatch.targetOffset(0, -45);
		
		try {
			screen.click(codeMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_invalidCode() {
				
		screen.write("invalid code");
		codeMatch.targetOffset(deltaX, -45);
		try {
			screen.click(codeMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
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
		
		screen.write(getNextValidCode());
		codeMatch.targetOffset(deltaX, -45);
		try {
			screen.click(codeMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
		
	}

	@Override
	public void v_codeValid() {
		
	}

	@Override
	public void e_chooseTicketPurchase() {

		try {
			screen.click(imageTicketPurchase);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_from() {
		purchaseMatch.targetOffset(0, -10);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
	}

	@Override
	public void e_fromInvalid() {
		
		screen.write("invalid from");
		purchaseMatch.targetOffset(deltaX, -10);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_fromValid() {
		
		screen.write(validFrom);
		purchaseMatch.targetOffset(deltaX, -10);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_to() {
		
		purchaseMatch.targetOffset(0, 30);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_toInvalid() {
		
		screen.write("invalid to");
		purchaseMatch.targetOffset(deltaX, 30);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}	

	@Override
	public void e_toValid() {
		
		screen.write(validTo);
		purchaseMatch.targetOffset(deltaX, 30);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_when() {
		
		purchaseMatch.targetOffset(0, 60);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}

	@Override
	public void e_whenInvalid() {
		
		screen.write("invalid time");
		purchaseMatch.targetOffset(deltaX, 60);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_whenValid() {
		
		screen.write(validTime);
		purchaseMatch.targetOffset(deltaX, 60);
		try {
			screen.click(purchaseMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_choosePaymentType() {
		
	}

	@Override
	public void e_cash() {
		
		try {
			screen.click(imageCashRadioButton);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_cash() {
		
		cashMatch.targetOffset(0, -5);
		try {
			screen.click(cashMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}

	@Override
	public void e_cashError() {
		
		screen.write("-1");
		cashMatch.targetOffset(deltaX, -5);
		try {
			screen.click(cashMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}

	@Override
	public void e_creditcard() {
		
		try {
			screen.click(imageCreditcardRadioButton);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_creditcard() {
		
		creditcardMatch.targetOffset(0, 10);
		try {
			screen.click(creditcardMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_creditcardError() {
		
		screen.write("not a valid creditcard number");
		creditcardMatch.targetOffset(deltaX, 10);
		try {
			screen.click(creditcardMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void e_payingWithCash() {
		screen.write("5000");
		cashMatch.targetOffset(deltaX, -5);
		try {
			screen.click(cashMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
	}
	
	
	@Override
	public void e_payingByCreditcard() {
		
		screen.write("00000000-00000000");
		creditcardMatch.targetOffset(deltaX, 10);
		try {
			screen.click(creditcardMatch);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_paymentSuccessful() {
		
	}

	@Override
	public void e_printPurchaseTicket() {
		
		try {
			screen.click(imagePrint);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void e_printInternetTicket() {
		
		try {
			screen.click(imagePrint);
		} catch (FindFailed e) {
			e.printStackTrace();
			handleFindFailed();
		}
		
	}
	
	@Override
	public void v_printTicket() {
		
	}

	@Override
	public void e_returnToWaiting() {
		
	}
	
}
