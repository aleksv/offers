package at.neseri.offers.main.offer;

import at.neseri.offers.main.MainController;
import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.utils.AStageController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class OfferDialogController extends AStageController<Offer, OfferDao> {

	@FXML
	private TextField idTextfield;
	@FXML
	private ChoiceBox<Customer> customerChoiceBox;

	public void initialize() {
		customerChoiceBox.setItems(MainController.getInstance().getCustomerController().getMasterList());

	}

	@Override
	public void onLoad() {
		idTextfield.setText(String.valueOf(entry.getId()));
		customerChoiceBox.getSelectionModel().select(entry.getCustomer());
	}

	@Override
	protected void save() {
		entry.setCustomerId(customerChoiceBox.getSelectionModel().getSelectedItem().getId());
	}

}
