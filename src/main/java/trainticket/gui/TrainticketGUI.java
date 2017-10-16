package trainticket.gui;
	

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import trainticket.automata.ITrainticketAutomata;
import trainticket.automata.TrainTicketAutomata;
import trainticket.enums.Function;
import trainticket.enums.PaymentType;


public class TrainticketGUI extends Application {
	
	private boolean isCodeGiven = false;
	private boolean isFromGiven = false;
	private boolean isToGiven = false;
	private boolean isTimeGiven = false;
	private boolean isCashGiven = false;
	private boolean isCreditcardGiven = false;
	
	boolean isInternetTicket = false;
	boolean isCashPayment = false;
	boolean setInternetDisabled = true;
	boolean setPaymentDisabled = true;
	
	RadioButton rbInternet = new RadioButton("Internet ticket");
	RadioButton rbPurchase = new RadioButton("Ticket purchase");
	
	Label lGrantCode = new Label("Code:");
	TextField tfGrantCode = new TextField();
	Button bGrantCode = new Button("Send");	
	
	Label lFromStation = new Label("From:");
	TextField tfFromStation = new TextField();	
	Button bFromStation = new Button("Send");
	
	Label lToStation = new Label("To:");
	TextField tfToStation = new TextField();	
	Button bToStation = new Button("Send");
	
	Label lTime = new Label("Time:");
	TextField tfTime = new TextField();	
	Button bTime = new Button("Send");
	
	RadioButton rbCash = new RadioButton("Cash");
	RadioButton rbCreditcard = new RadioButton("Creditcard");
	
	Label lCash = new Label("Cash:");
	TextField tfCash = new TextField();	
	Button bCash = new Button("Send");
	
	Label lCreditcard = new Label("Creditcard:");
	TextField tfCreditcard = new TextField();	
	Button bCreditcard = new Button("Send");
	
	Button bPrintTicket = new Button("Print");
	
	private ITrainticketAutomata trainticketAutomata;
	
	public TrainticketGUI() {
		trainticketAutomata = new TrainTicketAutomata();
	}
	
	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("Train ticket application");			
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.TOP_CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));
			
			
			addElementsToPane(grid);
			
			Scene scene = new Scene(grid, 400, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	
	private void addRadioButtonToPane(GridPane grid) {
		
		final ToggleGroup groupTicket = new ToggleGroup();
		final ToggleGroup groupPayment = new ToggleGroup();

		rbInternet.setUserData("rbInternet");
		rbInternet.setToggleGroup(groupTicket);
				
		rbPurchase.setUserData("rbPurchase");
		rbPurchase.setToggleGroup(groupTicket);

		grid.add(rbInternet, 0, 0);
		grid.add(rbPurchase, 1, 0);
		
		// Function
		groupTicket.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (groupTicket.getSelectedToggle() != null) {
					Object userData = groupTicket.getSelectedToggle().getUserData();
					if (userData != null) {						
						if ("rbInternet".equals(userData)) {
							trainticketAutomata.chooseFunction(Function.INTERNET_TICKET);
							isInternetTicket = true;
							setInternetDisabled = false;
						}
						else if ("rbPurchase".equals(userData)) {
							trainticketAutomata.chooseFunction(Function.PURCHASE_TICKET);
							isInternetTicket = false;
							setInternetDisabled = false;
						}						
						refresh();
					}
				}
			}		
		});		
		
		
		rbCash.setUserData("rbCash");
		rbCash.setToggleGroup(groupPayment);
		rbCash.setDisable(true);
		
		rbCreditcard.setUserData("rbCreditcard");
		rbCreditcard.setToggleGroup(groupPayment);
		rbCreditcard.setDisable(true);
		
		grid.add(rbCash, 0, 5);
		grid.add(rbCreditcard, 1, 5);
		
		// Payment
		groupPayment.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if (groupPayment.getSelectedToggle() != null) {
					Object userData = groupPayment.getSelectedToggle().getUserData();
					if (userData != null) {						
						if ("rbCash".equals(userData)) {
							trainticketAutomata.paymentType(PaymentType.CASH);
							isCashPayment = true;
							setPaymentDisabled = false;
						}
						else if ("rbCreditcard".equals(userData)) {
							trainticketAutomata.paymentType(PaymentType.CREDITCARD);
							isCashPayment = false;
							setPaymentDisabled = false;
						}						
						refresh();
					}
				}
			}			
		});
		
	}

	private void addCodeControls(GridPane grid) {
		grid.add(lGrantCode, 0, 1);		
		grid.add(tfGrantCode, 1, 1);		
		grid.add(bGrantCode, 2, 1);		
		bGrantCode.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				String codeFromTextField = tfGrantCode.getText();
				System.out.println(codeFromTextField);
				if (!"".equals(codeFromTextField)) { 
					isCodeGiven = trainticketAutomata.grantCode(codeFromTextField);
				}
				
				refreshPrint();
			}
		});
	}
	
	private void addFromControls(GridPane grid) {
		grid.add(lFromStation, 0, 2);		
		grid.add(tfFromStation, 1, 2);		
		grid.add(bFromStation, 2, 2);		
		bFromStation.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				String fromTextField = tfFromStation.getText();
				System.out.println(fromTextField);
				if (!"".equals(fromTextField)) {
					isFromGiven = trainticketAutomata.fromStation(fromTextField);
					if (isFromGiven) System.out.println("valid from");
				}
				
				refreshPaymentRadioButton();
				refreshPrint();
			}
		});
	}
	
	private void addToControls(GridPane grid) {
		grid.add(lToStation, 0, 3);		
		grid.add(tfToStation, 1, 3);		
		grid.add(bToStation, 2, 3);		
		bToStation.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				String toFromTextField = tfToStation.getText();
				System.out.println(toFromTextField);
				if (!"".equals(toFromTextField)) {
					isToGiven = trainticketAutomata.toStation(toFromTextField);
					if (isToGiven) System.out.println("valid to");
				}
				
				refreshPaymentRadioButton();
				refreshPrint();
			}
		});
	}
	
	private void addTimeControls(GridPane grid) {
		grid.add(lTime, 0, 4);		
		grid.add(tfTime, 1, 4);		
		grid.add(bTime, 2, 4);		
		bTime.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				String timeFromTextField = tfTime.getText();
				System.out.println(timeFromTextField);
				if (!"".equals(timeFromTextField)) {
					isTimeGiven = trainticketAutomata.leavingTime(timeFromTextField);
					if (isTimeGiven) System.out.println("valid time");
				}
				
				refreshPaymentRadioButton();
				refreshPrint();
			}
		});
	}
	
	private void addCashControls(GridPane grid) {
		grid.add(lCash, 0, 6);		
		grid.add(tfCash, 1, 6);		
		grid.add(bCash, 2, 6);		
		bCash.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				String cashFromTextField = tfCash.getText();
				System.out.println(cashFromTextField);
				if (!"".equals(cashFromTextField)) {
					int amount = Integer.parseInt(cashFromTextField);
					isCashGiven = trainticketAutomata.payWithCash(amount);
				}
				
				refreshPrint();
			}
		});
	}
	
	private void addCreditcardControls(GridPane grid) {
		grid.add(lCreditcard, 0, 7);		
		grid.add(tfCreditcard, 1, 7);		
		grid.add(bCreditcard, 2, 7);		
		bCreditcard.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				String creditcardFromTextField = tfCreditcard.getText();
				System.out.println(creditcardFromTextField);
				if (!"".equals(creditcardFromTextField)) {
					int card_no = Integer.parseInt(creditcardFromTextField);
					isCreditcardGiven = trainticketAutomata.payWithCreditCard(card_no);
				}
				
				refreshPrint();
			}
		});
	}
	
	private void addPrintButton(GridPane grid) {
		bPrintTicket.setDisable(true);
		grid.add(bPrintTicket, 1, 8);		
		bPrintTicket.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				boolean printInternetTicket = isInternetTicket && isCodeGiven;
				boolean printPurchaseTicket = !isInternetTicket && isFromGiven && isToGiven && isTimeGiven 
						&& ((isCashPayment && isCashGiven) || (!isCashPayment && isCreditcardGiven));

				if (printInternetTicket || printPurchaseTicket) {
					System.out.println("Printing ticket...");
				}
				
				clear();
			}
			
		});
	}
	
	private void addElementsToPane(GridPane grid) {
		
		// Add radioButtons
		addRadioButtonToPane(grid);
		
		// Add "code" controls
		addCodeControls(grid);
		
		// Add "from" controls
		addFromControls(grid);
		
		// Add "to" controls 
		addToControls(grid);	
		
		// Add "time" controls
		addTimeControls(grid);	
		
		// Add "cash" controls
		addCashControls(grid);	
		
		// Add "creditcard" controls
		addCreditcardControls(grid);	
		
		// Add "print" button
		addPrintButton(grid);		
		
		refresh();
	}
	
	
	private void initializeRadioButtons() {
		
		rbInternet.setSelected(false);
		rbPurchase.setSelected(false);
		rbCash.setSelected(false);
		rbCreditcard.setSelected(false);
		rbCash.setDisable(true);		
		rbCreditcard.setDisable(true);
		
	}
	
	private void refreshPaymentRadioButton() {
		rbCash.setDisable(isInternetTicket || !isFromGiven || !isToGiven || !isTimeGiven);
		rbCreditcard.setDisable(isInternetTicket || !isFromGiven || !isToGiven || !isTimeGiven);
	}
	
	private void clear() {
		// Clear text fields
		tfCash.clear();
		tfCreditcard.clear();
		tfFromStation.clear();
		tfGrantCode.clear();
		tfTime.clear();
		tfToStation.clear();
		
		// Clear bool flags
		isCodeGiven = false;
		isFromGiven = false;
		isToGiven = false;
		isTimeGiven = false;
		isCashGiven = false;
		isCreditcardGiven = false;
		
		setInternetDisabled = true;
		setPaymentDisabled = true;
		
		// Refresh print button
		refreshPrint();
		
		// Set radio buttons into initial state
		initializeRadioButtons();
		
		// Refresh textfields and buttons
		refresh();

	}
	
	private void refreshPrint() {
		boolean printInternetTicket = isInternetTicket && isCodeGiven;
		boolean printPurchaseTicket = !isInternetTicket && isFromGiven && isToGiven && isTimeGiven 
				&& ((isCashPayment && isCashGiven) || (!isCashPayment && isCreditcardGiven));
		
		bPrintTicket.setDisable(!printInternetTicket && !printPurchaseTicket);
	}
	
	private void refresh() {
		System.out.println(isInternetTicket);
		bGrantCode.setDisable(!isInternetTicket || setInternetDisabled);
		tfGrantCode.setDisable(!isInternetTicket || setInternetDisabled);
		
		bFromStation.setDisable(isInternetTicket || setInternetDisabled);
		tfFromStation.setDisable(isInternetTicket || setInternetDisabled);
		
		bToStation.setDisable(isInternetTicket || setInternetDisabled);
		tfToStation.setDisable(isInternetTicket || setInternetDisabled);
		
		bTime.setDisable(isInternetTicket || setInternetDisabled);
		tfTime.setDisable(isInternetTicket || setInternetDisabled);
		
		bCash.setDisable(isInternetTicket || !isCashPayment || setPaymentDisabled);
		tfCash.setDisable(isInternetTicket || !isCashPayment || setPaymentDisabled);
		
		bCreditcard.setDisable(isInternetTicket || isCashPayment || setPaymentDisabled);
		tfCreditcard.setDisable(isInternetTicket || isCashPayment || setPaymentDisabled);
		
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}
}
