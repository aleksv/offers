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
	private CustomerDao customerDao;

	public void initialize() {

	}

	public void setCustomer(Customer customer) {
		this.customer = customer;
		if (customer.getId() != 0)
			customerIdTextfield.setText(String.valueOf(customer.getId()));
		if (customer.getNachname() != null)
			nachnameTextfield.setText(String.valueOf(customer.getNachname()));
		if (customer.getVorname() != null)
			vornameTextfield.setText(String.valueOf(customer.getVorname()));
	}

	public void onSaveButton() {
		customer.setVorname(vornameTextfield.getText());
		customer.setNachname(nachnameTextfield.getText());
		customerDao.saveCustomer(customer);
		getStage().close();
	}

	public void onCancelButton() {
		getStage().close();
	}

	public void setCustomerDao(CustomerDao customerDao) {
		this.customerDao = customerDao;
	}
}
