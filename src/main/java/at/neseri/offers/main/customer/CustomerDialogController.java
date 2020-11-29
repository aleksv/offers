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
	public void onLoad() {
		if (entry.getId() != 0)
			customerIdTextfield.setText(String.valueOf(entry.getId()));
		if (entry.getNachname() != null)
			nachnameTextfield.setText(String.valueOf(entry.getNachname()));
		if (entry.getVorname() != null)
			vornameTextfield.setText(String.valueOf(entry.getVorname()));
	}

	@Override
	protected void save() {
		entry.setVorname(vornameTextfield.getText());
		entry.setNachname(nachnameTextfield.getText());
	}
}
