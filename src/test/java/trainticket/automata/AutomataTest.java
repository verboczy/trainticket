package trainticket.automata;

import static org.junit.Assert.assertEquals;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trainticket.enums.Function;
import trainticket.enums.PaymentType;

public class AutomataTest {
	
	private Logger logger = LoggerFactory.getLogger(AutomataTest.class);
	
	private final String originalCodeFile = "src/main/resources/codes.txt";
	private final String originalStationFile = "src/main/resources/stations.txt";
	
	private final String codeFile = "src/test/resources/codes.txt";
	private final String stationFile = "src/test/resources/stations.txt";
	private final String ticketFolder = "src/test/resources/ticket";
	
	private ITrainticketAutomata sut;
	
	private final String validCode = "0000000000";
	private final String invalidCode = "0000";
	
	private final String validFrom = "Budapest";
	private final String validTo = "Pécs";
	private final String invalidStation = "New York";
	private final String sameToAsFrom = validFrom;
	
	private final String validTime = "11-00";
	private final String invalidTime = "11-01";
	
	private final String validCreditCard = "54093216-83433599";
	private final String invalidCreditCard = "0000";
	
	private Integer ticketId;
	
	
	@Before
	public void setup() {
		
		sut = new TrainTicketAutomata(codeFile, stationFile, ticketFolder);
		
		ticketId = 0;
		
		try {
			// Copy resources to test folder
			copyFile(originalCodeFile, codeFile);
			copyFile(originalStationFile, stationFile);
		}
		catch (IOException e) {
			logger.error(e.getMessage());
		}
		
		// Delete previous testing's tickets
		deleteTickets(ticketFolder);
	}
	
	
	/**
	 * Copies source file to target.
	 * 
	 * @param source
	 * @param target
	 * @throws IOException
	 */
	private void copyFile(String source, String target) throws IOException {
		
		logger.debug("Copy " + source + " to " + target);
		
		Path codePathSource = Paths.get(source);
		Path codePathTarget = Paths.get(target);
		
		Files.copy(codePathSource, codePathTarget, REPLACE_EXISTING);
	}
	
	
	/**
	 * Deletes files in a given folder.
	 * 
	 * @param ticketFolder
	 */
	private void deleteTickets(String ticketFolder) {
		
		logger.debug("Delete tickets from folder " + ticketFolder);
		
		File dir = new File(ticketFolder);
		if (dir.exists()) {
			
			for (File file : dir.listFiles()) {
				file.delete();
			}
		}
	}
	
	
	@Test
	public void testInternetTicketSuccess() {
		
		// Choose internet ticket function
		sut.chooseFunction(Function.INTERNET_TICKET);
		// Grant valid code
		assertEquals(sut.grantCode(validCode), true);
		// Print ticket
		assertEquals(sut.printTicket(validCode), true);
	}
	
	
	@Test
	public void testInternetTicketFailure() {
		
		// Choose internet ticket function
		sut.chooseFunction(Function.INTERNET_TICKET);
		// Grant invalid code
		assertEquals(sut.grantCode(invalidCode), false);
		// Grant invalid code
		assertEquals(sut.grantCode(invalidCode), false);
	}
	
	
	@Test
	public void testPurchaseFailAtStation() {
		
		// Choose ticket purchase function
		sut.chooseFunction(Function.PURCHASE_TICKET);
		// Grant invalid station
		assertEquals(sut.fromStation(invalidStation), false);
	}
	
	
	@Test
	public void testPurchaseFailAtSameStation() {
		
		// Choose ticket purchase function
		sut.chooseFunction(Function.PURCHASE_TICKET);
		// Grant valid station
		assertEquals(sut.fromStation(validFrom), true);
		// Grant the same station
		assertEquals(sut.toStation(sameToAsFrom), false);
	}
	
	
	@Test
	public void testTimeFail() {
		
		// Choose ticket purchase function
		sut.chooseFunction(Function.PURCHASE_TICKET);
		// Grant valid station
		assertEquals(sut.fromStation(validFrom), true);
		// Grant valid to
		assertEquals(sut.toStation(validTo), true);
		// Grant invalid time
		assertEquals(sut.leavingTime(invalidTime), false);
	}
	
	
	@Test
	public void testPurchasePaymentFail() {
		
		// Choose ticket purchase function
		sut.chooseFunction(Function.PURCHASE_TICKET);
		// Grant valid from
		assertEquals(sut.fromStation(validFrom), true);
		// Grant valid to
		assertEquals(sut.toStation(validTo), true);
		// Grant valid time
		assertEquals(sut.leavingTime(validTime), true);
		// Choose credit card payment
		sut.paymentType(PaymentType.CREDITCARD);
		// Grant invalid credit card number
		assertEquals(sut.payWithCreditCard(invalidCreditCard), false);
	}
	
	
	@Test
	public void testPurchaseSuccess() {
		
		// Choose ticket purchase function
		sut.chooseFunction(Function.PURCHASE_TICKET);
		// Grant valid from
		assertEquals(sut.fromStation(validFrom), true);
		// Grant valid to
		assertEquals(sut.toStation(validTo), true);
		// Grant valid time
		assertEquals(sut.leavingTime(validTime), true);
		// Choose credit card payment
		sut.paymentType(PaymentType.CREDITCARD);
		// Grant valid credit card number
		assertEquals(sut.payWithCreditCard(validCreditCard), true);
		// Print ticket
		assertEquals(sut.printTicket(ticketId.toString()), true);
	}
	
}
