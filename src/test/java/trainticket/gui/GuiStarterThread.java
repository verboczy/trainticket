package trainticket.gui;

/**
 * This class is used to start the GUI application on a 
 * separate thread.
 * @author verboczy
 *
 */
public class GuiStarterThread implements Runnable {

	@Override
	public void run() {
		
		TrainticketGUI.main(null);
	}

}
