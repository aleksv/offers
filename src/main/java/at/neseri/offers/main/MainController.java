package at.neseri.offers.main;

import at.neseri.offers.main.customer.CustomerController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MainController {
	@FXML
	private MenuBar menuBar;
	@FXML
	private MenuItem closeMenuItem;

	@FXML
	private CustomerController customerController;

	public void initialize() {

	}

	public void onActionCloseMenuItem() {
		((Stage) menuBar.getScene().getWindow()).close();
	}

}
