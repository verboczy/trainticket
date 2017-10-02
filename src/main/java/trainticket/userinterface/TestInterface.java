package trainticket.userinterface;

public class TestInterface implements IUserInputInterface {

	private int function;
	private String code;
	private String initStation;
	private String destStation;
	private String leavingTime;
	private String paymentType;
	private int cash;
	private int moneyToTransfer;
	
	// Set all field to an invalid value
	public void initialize() {
		function = -1;
		code = "";
		initStation = "";
		destStation = "";
		leavingTime = "";
		paymentType = "";
		cash = -1;
		moneyToTransfer = -1;
	}
	
	@Override
	public int getFunction() {
		return function;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getInitialStation() {
		return initStation;
	}

	@Override
	public String getDestinationStation() {
		return destStation;
	}

	@Override
	public String getLeavingTime() {
		return leavingTime;
	}

	@Override
	public String getPaymentType() {
		return paymentType;
	}

	@Override
	public int getCash() {
		return cash;
	}

	@Override
	public int getMoneyToTransfer() {
		return moneyToTransfer;
	}

	public void setFunction(int function) {
		this.function = function;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setInitStation(String initStation) {
		this.initStation = initStation;
	}

	public void setDestStation(String destStation) {
		this.destStation = destStation;
	}

	public void setLeavingTime(String leavingTime) {
		this.leavingTime = leavingTime;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public void setCash(int cash) {
		this.cash = cash;
	}

	public void setMoneyToTransfer(int moneyToTransfer) {
		this.moneyToTransfer = moneyToTransfer;
	}

}
