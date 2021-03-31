package at.neseri.offers.main;

import at.neseri.offers.main.customer.CustomerController;
import at.neseri.offers.main.item.ItemController;
import at.neseri.offers.main.offer.OfferController;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class MainController {

	private static MainController instance;

	@FXML
	private MenuBar menuBar;
	@FXML
	private MenuItem closeMenuItem;
	@FXML
	private CustomerController customerController;
	@FXML
	private ItemController itemController;
	@FXML
	private OfferController offerController;

	public void initialize() {
		instance = this;
	}

	public void onActionCloseMenuItem() {
		((Stage) menuBar.getScene().getWindow()).close();
	}

	public static MainController getInstance() {
		return instance;
	}

	public CustomerController getCustomerController() {
		return customerController;
	}

	public OfferController getOfferController() {
		return offerController;
	}

	public ItemController getItemController() {
		return itemController;
	}
}
