package at.neseri.offers.main.customer;

import at.neseri.offers.main.utils.AStageController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CustomerDialogController extends AStageController {

	@FXML
	private TextField customerIdTextfield;
	@FXML
	private TextField nachnameTextfield;
	@FXML
	private TextField vornameTextfield;
	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	private Customer customer;

	public void initialize() {

	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		customerIdTextfield.setText(String.valueOf(customer.getId()));
		nachnameTextfield.setText(String.valueOf(customer.getNachname()));
		vornameTextfield.setText(String.valueOf(customer.getVorname()));
	}

	public void onSaveButton() {
		customer.setVorname(vornameTextfield.getText());
		customer.setNachname(nachnameTextfield.getText());
		getStage().close();
	}

	public void onCancelButton() {
		getStage().close();
	}
}
