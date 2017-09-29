package trainticket.userinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUserInterface implements IUserInterface {

	@Override
	public int getFunction() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int choice = -1;

		try {
			choice = Integer.parseInt(br.readLine());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return choice;
	}

	@Override
	public String getCode() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = "";

		try {
			code = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return code;
	}

	@Override
	public String getInitialStation() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String from = "";

		try {
			from = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return from;
	}

	@Override
	public String getDestinationStation() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String to = "";

		try {
			to = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return to;
	}

	@Override
	public String getLeavingTime() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String time = "";
		
		try {
			time = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return time;
	}

	@Override
	public String getPaymentType() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String type = "";
		
		try {
			type = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return type;
	}

	@Override
	public int getCash() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int amountOfCash = 0;
		
		try {
			String currentLine = br.readLine();
			amountOfCash = Integer.parseInt(currentLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return amountOfCash;
	}

	@Override
	public int getMoneyToTransfer() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		int moneyToTransfer = 0;
		
		try {
			String currentLine = br.readLine();
			moneyToTransfer = Integer.parseInt(currentLine);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return moneyToTransfer;
	}

}
