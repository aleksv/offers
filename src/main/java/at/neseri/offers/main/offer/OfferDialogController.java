package at.neseri.offers.main.offer;

import java.util.Optional;

import at.neseri.offers.main.MainController;
import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.utils.AStageController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class OfferDialogController extends AStageController<Offer, OfferDao> {

	@FXML
	private TextField idTextfield;
	@FXML
	private ChoiceBox<Customer> customerChoiceBox;
	@FXML
	private TextField subjectTextfield;

	public void initialize() {
		ObservableList<Customer> masterList = MainController.getInstance().getCustomerController().getMasterList();
		customerChoiceBox.setItems(masterList);
	}

	@Override
	public void onLoad() {
		idTextfield.setText(String.valueOf(entry.getId()));
		customerChoiceBox.getSelectionModel().select(entry.getCustomer());
		Optional.ofNullable(entry.getSubject()).ifPresent(s -> subjectTextfield.setText(s));
	}

	@Override
	protected void save() {
		entry.setCustomerId(customerChoiceBox.getSelectionModel().getSelectedItem().getId());
		entry.setSubject(subjectTextfield.getText());
	}

}
