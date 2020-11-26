package at.neseri.offers.main.customer;

import at.neseri.offers.main.utils.AStageController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class CustomerDialogController extends AStageController<Customer, CustomerDao> {

	@FXML
	private TextField customerIdTextfield;
	@FXML
	private TextField nachnameTextfield;
	@FXML
	private TextField vornameTextfield;

	@Override
	public void setEntry(Customer customer) {
		this.entry = customer;
		if (customer.getId() != 0)
			customerIdTextfield.setText(String.valueOf(customer.getId()));
		if (customer.getNachname() != null)
			nachnameTextfield.setText(String.valueOf(customer.getNachname()));
		if (customer.getVorname() != null)
			vornameTextfield.setText(String.valueOf(customer.getVorname()));
	}

	@Override
	protected void save() {
		entry.setVorname(vornameTextfield.getText());
		entry.setNachname(nachnameTextfield.getText());
	}
}
