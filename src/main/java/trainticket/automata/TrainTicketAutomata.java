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

import trainticket.creditcard.CreditcardPayment;
import trainticket.creditcard.ICreditcardPayment;
import trainticket.enums.Function;
import trainticket.enums.PaymentType;

public class TrainTicketAutomata implements ITrainticketAutomata, ITestInterface {
	
	private static final String CODE_FILE = "src/main/resources/codes.txt";
	private static final String STATION_FILE = "src/main/resources/stations.txt";
	private static final int PRICE_PER_KM = 10;
	
	private List<String> ticketCodes;
	private Map<String, Integer[]> stationMap;
	private Map<Integer, Integer> changeCash;
	private List<Integer> denominationList;

	private Function function;
	private PaymentType paymentType;
	private String code;
	private String fromStation;
	private String toStation;
	private String leavingTime;
	private int price;
	
	private ICreditcardPayment creditcardPayment;
	
	public TrainTicketAutomata() {
		creditcardPayment = new CreditcardPayment();
		
		createListsAndMaps();
		
		initializeChangeCashMap();
	}
	
	public TrainTicketAutomata(ICreditcardPayment creditcardPayment) {
		this.creditcardPayment = creditcardPayment;
		
		createListsAndMaps();
	}
	
	private void createListsAndMaps() {
		ticketCodes = new ArrayList<>();
		stationMap = new HashMap<>();
		changeCash = new HashMap<>();
		denominationList = new ArrayList<>();
	}
	
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
	}
	
	@Override
	public Function chooseFunction(Function function) {
		
		this.function = function;			
		return this.function;
		
	}
	

	@Override
	public boolean grantCode(String code) {	
		
		loadCodes();
				
		if (ticketCodes.contains(code)) {
			this.code = code;
			ticketCodes.remove(code);
			updateCodeList();

			return true;
		}		
		
		return false;
	}
	

	@Override
	public boolean fromStation(String from) {
		
		loadStations();
		
		if (stationMap.containsKey(from)) {
			fromStation = from;
			return true;
		}
		
		return false;
	}
	

	@Override
	public boolean toStation(String to) {

		if (stationMap.containsKey(to) && !to.equals(fromStation)) {
			toStation = to;
			return true;
		}

		return false;
	}
	

	@Override
	public boolean leavingTime(String time) {
		
		List<String> timeList = new ArrayList<>();

		for (int i = 7; i <= 21; i += 2) {
			StringBuilder sb = new StringBuilder();
			timeList.add(sb.append(i).append(":00").toString());
		}

		if (timeList.contains(time)) {
			leavingTime = time;
			return true;
		}

		return false;
	}
	

	@Override
	public PaymentType paymentType(PaymentType paymentType) {

		price = computePrice();
		
		this.paymentType = paymentType;
		return this.paymentType;
		
	}
	

	@Override
	public boolean payWithCash(int amount) {

		if (amount < price) {
			return false;
		} else if (amount == price) {
			return true;
		} else {
			return changeHandle(price, amount);
		}
		
	}
	

	@Override
	public boolean payWithCreditCard(String cardNumber) {
		
		return creditcardPayment.pay(cardNumber);
		
	}

	@Override
	public boolean printTicket(String ticketId) {
		
		boolean alright = true;
		
		FileWriter fw = null;
		BufferedWriter bw = null;

		StringBuilder sb = new StringBuilder();
		sb.append("src/main/resources/ticket").append(ticketId).append(".txt");

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

		} catch (IOException e) {
			e.printStackTrace();
			alright = false;
		} finally {
			try {
				if (bw != null)
					bw.close();
				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
				alright = false;
			}
		}
		
		return alright;
		
	}
	
	
	private void loadCodes() {
		
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(CODE_FILE);
			br = new BufferedReader(fr);
			
			String currentLine;
			
			while ((currentLine = br.readLine()) != null) {
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
	
	private void loadStations() {
		
		BufferedReader br = null;
		FileReader fr = null;
		
		try {
			fr = new FileReader(STATION_FILE);
			br = new BufferedReader(fr);
			
			String currentLine;
			
			while ((currentLine = br.readLine()) != null) {
				
				String[] lineArray = currentLine.split(";");
				Integer[] positions = { Integer.parseInt(lineArray[1]), Integer.parseInt(lineArray[2]) };
				stationMap.put(lineArray[0], positions);
				
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
	
	
	private int computePrice() {

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
			
			return false;
			
		} else {
			for (Map.Entry<Integer, Integer> entry : helperMap.entrySet()) {
				int oldValue = changeCash.get(entry.getKey());
				int difference = entry.getValue();
				changeCash.put(entry.getKey(), oldValue - difference);
			}
			
			return true;
		}
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
	public void setToDefault() {
		initialize();
	}

	@Override
	public void setChangeCashInAutomata(Map<Integer, Integer> changeCashMap) {
		this.changeCash = changeCashMap;
	}
	
}
