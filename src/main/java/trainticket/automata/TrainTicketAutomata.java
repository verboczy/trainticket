package trainticket.automata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import trainticket.creditcard.CreditCardPayment;
import trainticket.creditcard.ICreditCardPayment;
import trainticket.enums.AutomataState;
import trainticket.enums.Function;
import trainticket.enums.PaymentType;

public class TrainTicketAutomata implements ITrainticketAutomata {
	
	// Logger, for logging the happenings
	private Logger logger = LoggerFactory.getLogger(TrainTicketAutomata.class);
	
	// The state of the automata
	private AutomataState state = AutomataState.FUNCTION_WAITING;
	
	// Price of the ticket per kilometer.
	private static final int PRICE_PER_KM = 10;
	
	// File containing the internet ticket codes.
	private String codeFile;
	// File containing the stations and their coordinates.
	private String stationFile;
	// Folder where the "printed" ticket can be found.
	private String ticketFolder;
	
	// List containing the internet ticket codes, after it was
	// read.
	private List<String> ticketCodes;
	// Map containing the station names as the String key, the
	// values are the coordinates.
	private Map<String, Integer[]> stationMap;
	// Map containing the amount of each denomination in the
	// machine.
	private Map<Integer, Integer> changeCash;
	// List containing: 5, 10, 50, 100, 200, 500, 1000, 2000,
	// 5000, 10000.
	private List<Integer> denominationList;
	
	// Function can be INTERNET_TICKET or PURCHASE_TICKET.
	private Function function;
	// Payment type can be CASH or CREDITCARD.
	private PaymentType paymentType;
	// The internet ticket code.
	private String code;
	// The initial station.
	private String fromStation;
	// The destination.
	private String toStation;
	// The time when the train leaves. In the format of: hh-mm.
	private String leavingTime;
	// The price of the ticket.
	private int price;
	
	// The service reliable for the credit card payment.
	private ICreditCardPayment creditCardPayment;
	
	
	/**
	 * Constructor without parameters, sets default values, and
	 * initializes everything.
	 */
	public TrainTicketAutomata() {
		
		codeFile = "src/main/resources/codes.txt";
		stationFile = "src/main/resources/stations.txt";
		ticketFolder = "src/main/resources/ticket";
		
		creditCardPayment = new CreditCardPayment();
		
		createListsAndMaps();
		initializeChangeCashMap();
		
		logger.debug("Trainticket automata created c(0)");
	}
	
	
	/**
	 * Constructor for creating TrainTicketAutomata with the
	 * given credit card payment.
	 * 
	 * @param creditCardPayment
	 */
	public TrainTicketAutomata(ICreditCardPayment creditCardPayment) {
		
		this.creditCardPayment = creditCardPayment;
		
		codeFile = "src/main/resources/codes.txt";
		stationFile = "src/main/resources/stations.txt";
		ticketFolder = "src/main/resources/ticket";
		
		createListsAndMaps();
		initializeChangeCashMap();
		
		logger.debug("Trainticket automata created c(1)");
	}
	
	
	/**
	 * Constructor for creating TrainTicketAutomata with the
	 * given code file, station file and ticket folder.
	 * 
	 * @param codeFile
	 * @param stationFile
	 * @param ticketFolder
	 */
	public TrainTicketAutomata(String codeFile, String stationFile, String ticketFolder) {
		
		creditCardPayment = new CreditCardPayment();
		
		this.codeFile = codeFile;
		this.stationFile = stationFile;
		this.ticketFolder = ticketFolder;
		
		createListsAndMaps();
		initializeChangeCashMap();
		
		logger.debug("Trainticket automata created (c3)");
	}
	
	
	/**
	 * Constructor for creating TrainTicketAutomata with the
	 * given credit card payment, code file, station file and
	 * ticket folder.
	 * 
	 * @param creditCardPayment
	 * @param codeFile
	 * @param stationFile
	 * @param ticketFolder
	 */
	public TrainTicketAutomata(ICreditCardPayment creditCardPayment, String codeFile,
			String stationFile, String ticketFolder) {
		
		this.creditCardPayment = creditCardPayment;
		
		this.codeFile = codeFile;
		this.stationFile = stationFile;
		this.ticketFolder = ticketFolder;
		
		createListsAndMaps();
		initializeChangeCashMap();
		
		logger.debug("Trainticket automata created (c4)");
	}
	
	
	/**
	 * Initializes the lists and maps.
	 */
	private void createListsAndMaps() {
		
		ticketCodes = new ArrayList<>();
		stationMap = new HashMap<>();
		changeCash = new HashMap<>();
		denominationList = new ArrayList<>();
		
		logger.debug("Lists and maps created");
	}
	
	
	/**
	 * Sets the variables into a initial state. Lists and maps
	 * are empty, strings are null.
	 */
	private void initialize() {
		
		ticketCodes.clear();
		stationMap.clear();
		changeCash.clear();
		denominationList.clear();
		
		function = null;
		paymentType = null;
		code = null;
		fromStation = null;
		toStation = null;
		leavingTime = null;
		
		initializeChangeCashMap();
		
		logger.debug("Initialized");
	}
	
	
	@Override
	public Function chooseFunction(Function function) {
		
		if (state == AutomataState.FUNCTION_WAITING) {
			this.function = function;
			
			logger.info("Chosen function: " + this.function.toString());
			
			if (function == Function.INTERNET_TICKET) {
				state = AutomataState.INTERNETCODE_WAITING;
			}
			else {
				state = AutomataState.STATION_WAITING;
			}
		}
		
		return this.function;
	}
	
	
	@Override
	public boolean grantCode(String code) {
		
		if (state == AutomataState.INTERNETCODE_WAITING) {
			loadCodes();
			
			if (ticketCodes.contains(code)) {
				
				this.code = code;
				
				ticketCodes.remove(code);
				updateCodeList();
				
				logger.info("Code: " + code + " was valid");
				state = AutomataState.FUNCTION_WAITING;
				
				return true;
			}
			
			logger.info("Code: " + code + " was not valid");
		}
		
		return false;
	}
	
	
	@Override
	public boolean fromStation(String from) {
		
		if (state == AutomataState.STATION_WAITING) {
			loadStations();
			
			if (stationMap.containsKey(from)) {
				
				fromStation = from;
				
				logger.info("From: " + from + " was valid");
				
				return true;
			}
			
			logger.info("From: " + from + " was not valid");
		}
		
		return false;
	}
	
	
	@Override
	public boolean toStation(String to) {
		
		if (state == AutomataState.STATION_WAITING) {
			// The destination cannot be the same as the starting
			// station.
			if (stationMap.containsKey(to) && !to.equals(fromStation)) {
				
				toStation = to;
				
				logger.info("To: " + to + " was valid");
				state = AutomataState.TIME_WAITING;
				
				return true;
			}
			
			logger.info("To: " + to + " was not valid");
		}
		
		return false;
	}
	
	
	@Override
	public boolean leavingTime(String time) {
		
		List<String> timeList = new ArrayList<>();
		
		if (state == AutomataState.TIME_WAITING) {
			// The trains leave in every two hours between 7 and 21
			for (int i = 7; i <= 21; i += 2) {
				
				StringBuilder sb = new StringBuilder();
				timeList.add(sb.append(i).append("-00").toString());
			}
			
			if (timeList.contains(time)) {
				
				leavingTime = time;
				
				logger.info("Time: " + time + " was valid");
				state = AutomataState.PAYMENT_WAITING;
				
				return true;
			}
			
			logger.info("Time: " + time + " was not valid");
		}
		
		return false;
	}
	
	
	@Override
	public PaymentType paymentType(PaymentType paymentType) {
		
		if (state == AutomataState.PAYMENT_WAITING) {
			price = computePrice();
			
			this.paymentType = paymentType;
			
			logger.info("Payment: " + this.paymentType.getClass());
		}
		
		return this.paymentType;
	}
	
	
	@Override
	public boolean payWithCash(int amount) {
		
		boolean success = false;
		
		if (state == AutomataState.PAYMENT_WAITING) {
			if (amount < price) {
				logger.info("Cash: less than price");
				success = false;
			}
			else if (amount == price) {
				logger.info("Cash: success");
				success = true;
			}
			else {
				success = changeHandle(price, amount);
			}
		}
		
		if (success == true) {
			state = AutomataState.FUNCTION_WAITING;
		}
		
		return success;
	}
	
	
	@Override
	public boolean payWithCreditCard(String cardNumber) {
		
		boolean success = false;
		
		if (state == AutomataState.PAYMENT_WAITING) {
			
			success = creditCardPayment.pay(cardNumber);
			
			logger.info("Credit card successful: " + success);
		}
		
		if (success) {
			state = AutomataState.FUNCTION_WAITING;
		}
		
		return success;
	}
	
	
	@Override
	public boolean printTicket(String ticketId) {
		
		boolean alright = true;
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		// Make sure the ticket folder exists
		folderCheck();
		
		StringBuilder sb = new StringBuilder();
		sb.append(ticketFolder).append("/ticket").append(ticketId).append(".txt");
		
		try {
			fw = new FileWriter(sb.toString());
			bw = new BufferedWriter(fw);
			StringBuilder fileContent = new StringBuilder();
			
			switch (function) {
				case INTERNET_TICKET:
					fileContent.append(code);
					break;
				
				case PURCHASE_TICKET:
					fileContent.append("From: ").append(fromStation).append("\n");
					fileContent.append("To: ").append(toStation).append("\n");
					fileContent.append("Leaves: ").append(leavingTime);
					break;
				
				default:
					fileContent.append("TICKET");
					break;
			}
			
			bw.write(fileContent.toString());
			
		}
		catch (IOException e) {
			logger.error(e.getMessage());
			alright = false;
		}
		finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			}
			catch (IOException ex) {
				logger.error(ex.getMessage());
				alright = false;
			}
		}
		
		logger.info("Print successful: " + alright);
		
		return alright;
	}
	
	@Override
	public AutomataState getState() {
		return state;
	}
	
	@Override
	public void exit() {
		
		logger.info("Exit");
		
		initialize();
	}
	
	
	/**
	 * If the ticket folder does not exist create it.
	 */
	private void folderCheck() {
		
		File dir = new File(ticketFolder);
		
		if (!dir.exists()) {
			dir.mkdir();
		}
	}
	
	
	/**
	 * Loads the codes from the codeFile into codeList.
	 */
	private void loadCodes() {
		
		logger.debug("Loading codes from file");
		
		ticketCodes.clear();
		
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(codeFile);
			br = new BufferedReader(fr);
			
			String currentLine;
			
			while ((currentLine = br.readLine()) != null) {
				ticketCodes.add(currentLine);
			}
			
		}
		catch (IOException e) {
			logger.error(e.getMessage());
		}
		finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			}
			catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}
	}
	
	
	/**
	 * Writes the codes from codeList to codeFile.
	 */
	private void updateCodeList() {
		
		logger.debug("Updating codes");
		
		FileWriter fw = null;
		BufferedWriter bw = null;
		
		try {
			fw = new FileWriter(codeFile);
			bw = new BufferedWriter(fw);
			
			for (String ticketCode : ticketCodes) {
				bw.write(ticketCode + "\n");
			}
			
		}
		catch (IOException e) {
			logger.error(e.getMessage());
		}
		finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			}
			catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}
	}
	
	
	/**
	 * Loads the station informations from the stationFile into
	 * stationMap.
	 */
	private void loadStations() {
		
		logger.debug("Loading stations");
		
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(stationFile);
			br = new BufferedReader(fr);
			
			String currentLine;
			
			while ((currentLine = br.readLine()) != null) {
				
				// City;Xcoordinate;Ycoordinate
				String[] lineArray = currentLine.split(";");
				Integer[] positions = { Integer.parseInt(lineArray[1]),
						Integer.parseInt(lineArray[2]) };
				stationMap.put(lineArray[0], positions);
				
			}
			
		}
		catch (IOException e) {
			logger.error(e.getMessage());
		}
		finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			}
			catch (IOException ex) {
				logger.error(ex.getMessage());
			}
		}
	}
	
	
	/**
	 * Computes the price by the station coordinates and the
	 * price per kilometer.
	 * 
	 * @return the price rounded to zero or five
	 */
	private int computePrice() {
		
		logger.debug("Computing price");
		
		Integer[] fromPosition = stationMap.get(fromStation);
		int x1 = fromPosition[0];
		int y1 = fromPosition[1];
		
		Integer[] toPosition = stationMap.get(toStation);
		int x2 = toPosition[0];
		int y2 = toPosition[1];
		
		int distance;
		distance = (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
		
		price = distance * PRICE_PER_KM;
		
		return roundToZeroOrFive(price);
	}
	
	
	/**
	 * Rounds the given value to five or zero. Eg: 66 -> 65, 108
	 * -> 110
	 * 
	 * @param value
	 *            to be rounded
	 * @return value rounded to zero or five
	 */
	private int roundToZeroOrFive(int value) {
		
		logger.debug("Roudning to 5 or 0");
		
		int newValue;
		int modulo = value % 5;
		
		if (modulo <= 2) {
			newValue = value - modulo;
		}
		else {
			newValue = value + 5 - modulo;
		}
		
		return newValue;
	}
	
	
	/**
	 * Computes whether the automata can give back the change.
	 * 
	 * @param price
	 *            is price of the ticket
	 * @param amountOfCash
	 *            is the amount the customer gave in
	 * @return true if the machine can give back change, false
	 *         if it cannot
	 */
	private boolean changeHandle(int price, int amountOfCash) {
		
		int remainingAmount = amountOfCash - price;
		Map<Integer, Integer> helperMap = new HashMap<>();
		
		for (Integer denomination : denominationList) {
			
			boolean automataHasDenomination = changeCash.get(denomination) > 0;
			
			while (remainingAmount >= denomination && automataHasDenomination) {
				
				remainingAmount -= denomination;
				int previousValue = 0;
				
				if (helperMap.containsKey(denomination)) {
					previousValue = helperMap.get(denomination);
				}
				
				int thisValue = previousValue + 1;
				helperMap.put(denomination, thisValue);
				automataHasDenomination = (changeCash.get(denomination) - thisValue) > 0;
			}
		}
		
		if (remainingAmount != 0) {
			
			logger.info("Cash: not enough change");
			
			return false;
			
		}
		else {
			for (Map.Entry<Integer, Integer> entry : helperMap.entrySet()) {
				int oldValue = changeCash.get(entry.getKey());
				int difference = entry.getValue();
				changeCash.put(entry.getKey(), oldValue - difference);
			}
			
			logger.info("Cash: success");
			
			return true;
		}
	}
	
	
	/**
	 * Initializes the automata with given amount of cash.
	 */
	private void initializeChangeCashMap() {
		
		changeCash.clear();
		
		changeCash.put(5, 100);
		changeCash.put(10, 100);
		changeCash.put(20, 100);
		changeCash.put(50, 100);
		changeCash.put(100, 50);
		changeCash.put(200, 50);
		changeCash.put(500, 30);
		changeCash.put(1000, 20);
		changeCash.put(2000, 15);
		changeCash.put(5000, 10);
		changeCash.put(10000, 5);
		
		denominationList.clear();
		
		denominationList.add(10000);
		denominationList.add(5000);
		denominationList.add(2000);
		denominationList.add(1000);
		denominationList.add(500);
		denominationList.add(200);
		denominationList.add(100);
		denominationList.add(50);
		denominationList.add(20);
		denominationList.add(10);
		denominationList.add(5);
		
		logger.debug("Change cash map initialized");
	}
	
}
