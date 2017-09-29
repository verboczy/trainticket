package trainticket.automata;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import trainticket.userinterface.ConsoleOutputInterface;
import trainticket.userinterface.ConsoleUserInterface;
import trainticket.userinterface.IUserInputInterface;
import trainticket.userinterface.IUserOutputInterface;

public class TrainTicketAutomata implements ITrainticketAutomata {

	private IUserInputInterface userInterface = null;
	private IUserOutputInterface userOutput = null;

	private List<String> ticketCodes;
	private Map<String, Integer[]> stationMap;
	private List<String[]> soldOutTickets;
	private Map<Integer, Integer> changeCash;
	private List<Integer> denominationList;
	private static final String CODE_FILE = "src/main/resources/codes.txt";
	private static final String STATION_FILE = "src/main/resources/stations.txt";
	private static final int PRICE_PER_KM = 10;

	private String code = "";
	private String fromStation;
	private String toStation;
	private String leavingTime;

	private static int id = 0;

	public TrainTicketAutomata() {
		userInterface = new ConsoleUserInterface();
		userOutput = new ConsoleOutputInterface();
		initialization();
	}

	public TrainTicketAutomata(int type) {
		switch (type) {
		case 0:
			userInterface = new ConsoleUserInterface();
			userOutput = new ConsoleOutputInterface();
			break;

		default:
			userInterface = new ConsoleUserInterface();
			userOutput = new ConsoleOutputInterface();
			break;
		}
		initialization();
	}

	private void initialization() {
		ticketCodes = new ArrayList<>();
		stationMap = new HashMap<>();
		soldOutTickets = new ArrayList<>();
		denominationList = new ArrayList<>();
		changeCash = new HashMap<>();
		initializeChangeCashMap();
	}

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

	}


	@Override
	public int chooseFunction() {

		userOutput.printTypeSeletionHelp();
		
		int choice = -1;

		while (choice != 0 && choice != 1) {
			choice = userInterface.getFunction();
		}

		return choice;

	}

	private void loadCodes() {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(CODE_FILE);
			br = new BufferedReader(fr);
			String currentLine;
			boolean finished = false;
			while ((currentLine = br.readLine()) != null && !finished) {
				ticketCodes.add(currentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void updateCodeList() {
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			fw = new FileWriter(CODE_FILE);
			bw = new BufferedWriter(fw);

			for (String ticketCode : ticketCodes) {
				bw.write(ticketCode + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public boolean grantCode() {

		loadCodes();
		
		userOutput.printGrantCode();

		String currentLine = userInterface.getCode();
		if (ticketCodes.contains(currentLine)) {
			code = currentLine;
			ticketCodes.remove(currentLine);
			updateCodeList();

			return true;
		}

		return false;
	}

	private void loadStations() {
		BufferedReader br = null;
		FileReader fr = null;
		try {
			fr = new FileReader(STATION_FILE);
			br = new BufferedReader(fr);
			String currentLine;
			boolean finished = false;
			while ((currentLine = br.readLine()) != null && !finished) {
				String[] lineArray = currentLine.split(";");
				Integer[] positions = { Integer.parseInt(lineArray[1]), Integer.parseInt(lineArray[2]) };
				stationMap.put(lineArray[0], positions);
				// stationList.add(currentLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public boolean fromStation() {
		loadStations();
		
		userOutput.printFromStation();

		String currentLine = userInterface.getInitialStation();
		if (stationMap.containsKey(currentLine)) {
			fromStation = currentLine;

			return true;
		}

		return false;
	}

	@Override
	public boolean toStation() {
		userOutput.printToStation();

		String currentLine = userInterface.getDestinationStation();
		if (stationMap.containsKey(currentLine) && !currentLine.equals(fromStation)) {
			toStation = currentLine;
			return true;
		}

		return false;
	}

	@Override
	public boolean leavingTime() {
		userOutput.printLeavingTime();
		
		List<String> timeList = new ArrayList<>();

		for (int i = 7; i <= 21; i += 2) {
			StringBuilder sb = new StringBuilder();
			timeList.add(sb.append(i).append(":00").toString());
		}

		String currentLine = userInterface.getLeavingTime();
		if (timeList.contains(currentLine)) {
			leavingTime = currentLine;
			return true;
		}

		return false;
	}

	@Override
	public boolean hasEmptySeat() {

		String[] travelArray = { fromStation, toStation, leavingTime };

		if (soldOutTickets.contains(travelArray)) {
			return false;
		}

		Random rand = new Random();
		int randInt = rand.nextInt(10); // 0-9

		if (randInt >= 8) {
			soldOutTickets.add(travelArray);
			return false;
		}

		return true;
	}

	private void printPurchase() {
		FileWriter fw = null;
		BufferedWriter bw = null;

		StringBuilder sb = new StringBuilder();
		sb.append("src/main/resources/ticket").append(id++).append(".txt");

		try {
			fw = new FileWriter(sb.toString());
			bw = new BufferedWriter(fw);

			bw.write("From: " + fromStation + "\n");
			bw.write("To: " + toStation + "\n");
			bw.write("Leaving: " + leavingTime);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void printCode() {
		FileWriter fw = null;
		BufferedWriter bw = null;

		StringBuilder sb = new StringBuilder();
		sb.append("src/main/resources/ticket").append(code).append(".txt");

		try {
			fw = new FileWriter(sb.toString());
			bw = new BufferedWriter(fw);

			bw.write(code);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	@Override
	public String printTicket() {

		if ("".equals(code)) {
			printPurchase();
		} else {
			printCode();
		}
		
		userOutput.printThanks();

		return null;
	}

	@Override
	public boolean userExit() {
		return false;
	}

	private boolean paymentWithCash(int price) {
		int amountOfCash = userInterface.getCash();

		if (amountOfCash < price) {
			return false;
		} else if (amountOfCash == price) {
			return true;
		} else {
			return changeHandle(price, amountOfCash);
		}
	}

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
			userOutput.printCannotReturnChange();
			return false;
		} else {
			for (Map.Entry<Integer, Integer> entry : helperMap.entrySet()) {
				int oldValue = changeCash.get(entry.getKey());
				int difference = entry.getValue();
				changeCash.put(entry.getKey(), oldValue - difference);
			}
			
			userOutput.printChange(amountOfCash - price);

			return true;
		}
	}

	private boolean paymentWithCreditCard(int price) {
		int moneyToTransfer = userInterface.getMoneyToTransfer();

		return moneyToTransfer == price;
	}

	@Override
	public boolean payment() {
		boolean success = false;

		userOutput.printPaymentSelectionHelp();

		String currentLine = userInterface.getPaymentType();
		if (!"0".equals(currentLine) && !"1".equals(currentLine)) {
			return false;
		}
		int price = computePrice();
		userOutput.printAmount(price);
		
		if ("0".equals(currentLine)) {
			success = paymentWithCreditCard(price);
		} else if ("1".equals(currentLine)) {
			success = paymentWithCash(price);
		}

		return success;
	}

	private int roundToZeroOrFive(int value) {
		int newValue;
		int modulo = value % 5;

		if (modulo <= 2) {
			newValue = value - modulo;
		} else {
			newValue = value + 5 - modulo;
		}

		return newValue;
	}

	private int computePrice() {

		int price;
		Integer[] fromPosition = stationMap.get(fromStation);
		int x1 = fromPosition[0];
		int y1 = fromPosition[1];

		Integer[] toPosition = stationMap.get(toStation);
		int x2 = toPosition[0];
		int y2 = toPosition[1];

		float distance;

		distance = (float) Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));

		price = (int) distance * PRICE_PER_KM;

		return roundToZeroOrFive(price);
	}

}
