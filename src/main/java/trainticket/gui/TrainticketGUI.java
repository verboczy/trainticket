package trainticket.gui;
	

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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
	
	private Logger logger = LoggerFactory.getLogger(TrainticketGUI.class);
	
	private boolean isCodeGiven = false;
	private boolean isFromGiven = false;
	private boolean isToGiven = false;
	private boolean isTimeGiven = false;
	private boolean isCashGiven = false;
	private boolean isCreditCardGiven = false;
	
	boolean isInternetTicket = false;
	boolean isCashPayment = false;
	boolean setInternetDisabled = true;
	boolean setPaymentDisabled = true;
	
	private static Integer id;
	
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
	RadioButton rbCreditCard = new RadioButton("Credit card");
	
	Label lCash = new Label("Cash:");
	TextField tfCash = new TextField();	
	Button bCash = new Button("Send");
	
	Label lCreditCard = new Label("Credit card:");
	TextField tfCreditCard = new TextField();	
	Button bCreditCard = new Button("Send");
	
	Button bPrintTicket = new Button("Print");
	
	Button bExit = new Button("Exit");
	
	private ITrainticketAutomata trainticketAutomata;
	
	
	/**
	 * Constructor without parameter.
	 */
	public TrainticketGUI() {
		
		id = 0;

		trainticketAutomata = new TrainTicketAutomata();
		
		logger.debug("Trainticket GUI created");
	}
	
	/**
	 * Constructor for setting the trainticket automata.
	 * @param trainticketAutomata
	 */
	public TrainticketGUI(ITrainticketAutomata trainticketAutomata) {
		
		id = 0;
		
		this.trainticketAutomata = trainticketAutomata;
		
		logger.debug("Trainticket GUI created (c1)");
	}
	
	
	/**
	 * Initially no radio button is selected or enabled.
	 */
	private void initializeRadioButtons() {
		
		rbInternet.setSelected(false);
		rbPurchase.setSelected(false);
		rbCash.setSelected(false);
		rbCreditCard.setSelected(false);
		rbCash.setDisable(true);		
		rbCreditCard.setDisable(true);
		
		logger.debug("Radio buttons initialized");
	}
	
	
	/**
	 * Adds radio buttons to the pane.
	 * @param grid is the pane to which the radio buttons are added
	 */
	private void addRadioButtonToPane(GridPane grid) {
		
		// ToggleGroup for function selection
		final ToggleGroup groupTicket = new ToggleGroup();
		// ToggleGroup for payment selection
		final ToggleGroup groupPayment = new ToggleGroup();

		rbInternet.setUserData("rbInternet");
		rbInternet.setToggleGroup(groupTicket);
				
		rbPurchase.setUserData("rbPurchase");
		rbPurchase.setToggleGroup(groupTicket);

		grid.add(rbInternet, 0, 0);
		grid.add(rbPurchase, 1, 0);
		
		// Function selection
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
							
							logger.info("INTERNET_TICKET");
						}
						else if ("rbPurchase".equals(userData)) {
							trainticketAutomata.chooseFunction(Function.PURCHASE_TICKET);
							isInternetTicket = false;
							setInternetDisabled = false;
							
							logger.info("PURCHASE_TICKET");
						}						
						refresh();
					}
				}
			}		
		});		
		
		
		rbCash.setUserData("rbCash");
		rbCash.setToggleGroup(groupPayment);
		rbCash.setDisable(true);
		
		rbCreditCard.setUserData("rbCreditcard");
		rbCreditCard.setToggleGroup(groupPayment);
		rbCreditCard.setDisable(true);
		
		grid.add(rbCash, 0, 5);
		grid.add(rbCreditCard, 1, 5);
		
		// Payment selection
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
							
							logger.info("CASH");
						}
						else if ("rbCreditcard".equals(userData)) {
							trainticketAutomata.paymentType(PaymentType.CREDITCARD);
							isCashPayment = false;
							setPaymentDisabled = false;
							
							logger.info("CREDITCARD");
						}						
						refresh();
					}
				}
			}			
		});		
	}
	

	/**
	 * Adds controls for the "code" input.
	 * @param grid
	 */
	private void addCodeControls(GridPane grid) {
		
		grid.add(lGrantCode, 0, 1);		
		grid.add(tfGrantCode, 1, 1);		
		grid.add(bGrantCode, 2, 1);		
		
		bGrantCode.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				logger.debug("Code button clicked");
				
				String codeFromTextField = tfGrantCode.getText();
				if (!"".equals(codeFromTextField)) { 
					isCodeGiven = trainticketAutomata.grantCode(codeFromTextField);
				}
				
				if (!isCodeGiven) {
					tfGrantCode.clear();
					alertMessage(codeFromTextField + " is not a valid code."); 
				}
				
				refreshPrint();
			}
		});
	}
	
	/**
	 * Adds controls for the "from" input.
	 * @param grid
	 */
	private void addFromControls(GridPane grid) {
		
		grid.add(lFromStation, 0, 2);		
		grid.add(tfFromStation, 1, 2);		
		grid.add(bFromStation, 2, 2);
		
		bFromStation.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				logger.debug("From button clicked");
				
				String fromTextField = tfFromStation.getText();
				if (!"".equals(fromTextField)) {
					isFromGiven = trainticketAutomata.fromStation(fromTextField);
				}
				
				if(!isFromGiven) {
					tfFromStation.clear();
					alertMessage(fromTextField + " is not a valid station.");
				}
				
				refreshPaymentRadioButton();
				refreshPrint();
			}
		});
	}
	
	/**
	 * Adds controls for the "to" input.
	 * @param grid
	 */
	private void addToControls(GridPane grid) {
		
		grid.add(lToStation, 0, 3);		
		grid.add(tfToStation, 1, 3);		
		grid.add(bToStation, 2, 3);		
		
		bToStation.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				logger.debug("To button clicked");
				
				String toTextField = tfToStation.getText();
				if (!"".equals(toTextField)) {
					isToGiven = trainticketAutomata.toStation(toTextField);
				}
				
				if (!isToGiven) {
					tfToStation.clear();
					alertMessage(toTextField + " is not a valid destination.");
				}
				
				refreshPaymentRadioButton();
				refreshPrint();
			}
		});
	}
	
	/**
	 * Adds controls for the "leaving time" input.
	 * @param grid
	 */
	private void addTimeControls(GridPane grid) {
		
		grid.add(lTime, 0, 4);		
		grid.add(tfTime, 1, 4);		
		grid.add(bTime, 2, 4);	
		
		bTime.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				logger.debug("Time button clicked");
				
				String timeTextField = tfTime.getText();
				if (!"".equals(timeTextField)) {
					isTimeGiven = trainticketAutomata.leavingTime(timeTextField);
				}
				
				if (!isTimeGiven) {
					tfTime.clear();
					alertMessage(timeTextField + " is not a valid leaving time.");
				}
				
				refreshPaymentRadioButton();
				refreshPrint();
			}
		});
	}
	
	/**
	 * Adds controls for the "cash" input.
	 * @param grid
	 */
	private void addCashControls(GridPane grid) {
		
		grid.add(lCash, 0, 6);		
		grid.add(tfCash, 1, 6);		
		grid.add(bCash, 2, 6);		
		
		bCash.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				logger.debug("Cash button clicked");
				
				String cashFromTextField = tfCash.getText();
				if (!"".equals(cashFromTextField)) {
					int amount = Integer.parseInt(cashFromTextField);
					isCashGiven = trainticketAutomata.payWithCash(amount);
				}
				
				if (!isCashGiven) {
					tfCash.clear();
					alertMessage("Payment was not successful. Please try again!");
				}
				
				refreshPrint();
			}
		});
	}
	
	/**
	 * Adds controls for the "credit card" input.
	 * @param grid
	 */
	private void addCreditCardControls(GridPane grid) {
		grid.add(lCreditCard, 0, 7);		
		grid.add(tfCreditCard, 1, 7);		
		grid.add(bCreditCard, 2, 7);		
		bCreditCard.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				logger.debug("CreditCard button clicked");
				
				String creditCardFromTextField = tfCreditCard.getText();
				if (!"".equals(creditCardFromTextField)) {
					isCreditCardGiven = trainticketAutomata.payWithCreditCard(creditCardFromTextField);
				}
				
				if (!isCreditCardGiven) {
					tfCreditCard.clear();
					alertMessage("Payment was not successful. Please try again!");
				}
				
				refreshPrint();
			}
		});
	}
	
	/**
	 * Add "print" button to the pane.
	 * @param grid
	 */
	private void addPrintButton(GridPane grid) {
		
		bPrintTicket.setDisable(true);
		grid.add(bPrintTicket, 1, 8);	
		
		bPrintTicket.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				logger.debug("Print button clicked");
				
				boolean printInternetTicket = isInternetTicket && isCodeGiven;
				boolean printPurchaseTicket = !isInternetTicket && isFromGiven && isToGiven && isTimeGiven 
						&& ((isCashPayment && isCashGiven) || (!isCashPayment && isCreditCardGiven));
				
				if (printInternetTicket || printPurchaseTicket) {
					boolean printSuccessful = trainticketAutomata.printTicket(id.toString());
					id++;
					if (!printSuccessful) {
						alertMessage("Printing was not successful.");
					}
				}
				
				clear();
			}
			
		});
	}
	
	/**
	 * Add "print" button to the pane.
	 * @param grid
	 */
	private void addExitButton(GridPane grid) {
		
		bExit.setDisable(false);
		grid.add(bExit, 1, 10);
		
		bExit.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				
				logger.debug("Exit button clicked");
			
				trainticketAutomata.exit();
				
				clear();
			}
			
		});
	}
	
	/**
	 * Adds all element to the pane.
	 * @param grid
	 */
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
		
		// Add "credit card" controls
		addCreditCardControls(grid);	
		
		// Add "print" button
		addPrintButton(grid);	
		
		// Add "exit" button
		addExitButton(grid);
		
		refresh();
		
		logger.debug("Elements added to pane");
	}
	
	/**
	 * Refreshes the radio buttons by the logic.
	 */
	private void refreshPaymentRadioButton() {
		
		rbCash.setDisable(isInternetTicket || !isFromGiven || !isToGiven || !isTimeGiven);
		rbCreditCard.setDisable(isInternetTicket || !isFromGiven || !isToGiven || !isTimeGiven);
		
		logger.debug("Payment radio buttons refreshed");
	}
	
	/**
	 * Clears all text field, and sets variables to initial state.
	 */
	private void clear() {
		// Clear text fields
		tfCash.clear();
		tfCreditCard.clear();
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
		isCreditCardGiven = false;
		
		setInternetDisabled = true;
		setPaymentDisabled = true;
		
		// Refresh print button
		refreshPrint();
		
		// Set radio buttons into initial state
		initializeRadioButtons();
		
		// Refresh textfields and buttons
		refresh();

		logger.debug("Clear");
	}
	
	/**
	 * Refreshes the "print" button.
	 */
	private void refreshPrint() {
		
		boolean printInternetTicket = isInternetTicket && isCodeGiven;
		boolean printPurchaseTicket = !isInternetTicket && isFromGiven && isToGiven && isTimeGiven 
				&& ((isCashPayment && isCashGiven) || (!isCashPayment && isCreditCardGiven));
		
		bPrintTicket.setDisable(!printInternetTicket && !printPurchaseTicket);
		
		logger.debug("Print refreshed");
	}
	
	/**
	 * Refreshes the controllers by the logic.
	 */
	private void refresh() {
		
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
		
		bCreditCard.setDisable(isInternetTicket || isCashPayment || setPaymentDisabled);
		tfCreditCard.setDisable(isInternetTicket || isCashPayment || setPaymentDisabled);
		
		logger.debug("Refresh done");
	}
	
	/**
	 * A new window pops up when an error occur.
	 * @param message is the text to be written out
	 */
	private void alertMessage(String message) {
		
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error Dialog");
		alert.setContentText(message);

		logger.debug("Alert: " + message);
		
		alert.showAndWait();
	}
	
	@Override
	public void start(Stage primaryStage) {
		
		logger.debug("Start GUI");
		
		try {
			primaryStage.setTitle("Train ticket application");			
			GridPane grid = new GridPane();
			grid.setAlignment(Pos.TOP_CENTER);
			grid.setHgap(10);
			grid.setVgap(10);
			grid.setPadding(new Insets(25, 25, 25, 25));			
			
			// Adding the controllers to pane.
			addElementsToPane(grid);
			
			Scene scene = new Scene(grid, 400, 400);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Entry point of the GUI application.
	 * @param args
	 */
	public static void main(String[] args) {
				
		// Calls the start method
		launch(args);
	}

}
