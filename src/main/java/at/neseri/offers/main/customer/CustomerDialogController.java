package at.neseri.offers.main.customer;

import java.util.Optional;

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
	@FXML
	private TextField strasseTextfield;
	@FXML
	private TextField plzTextfield;
	@FXML
	private TextField ortTextfield;

	@Override
	public void onLoad() {
		if (entry.getId() != 0) {
			customerIdTextfield.setText(String.valueOf(entry.getId()));
		}
		if (entry.getNachname() != null) {
			nachnameTextfield.setText(String.valueOf(entry.getNachname()));
		}
		if (entry.getVorname() != null) {
			vornameTextfield.setText(String.valueOf(entry.getVorname()));
		}
		if (entry.getOrt() != null) {
			ortTextfield.setText(String.valueOf(entry.getOrt()));
		}
		if (entry.getPlz() != null) {
			plzTextfield.setText(String.valueOf(entry.getPlz()));
		}
		if (entry.getStrasse() != null) {
			strasseTextfield.setText(String.valueOf(entry.getStrasse()));
		}
	}

	@Override
	protected boolean save() {
		if (Optional.ofNullable(vornameTextfield.getText()).orElse("").isBlank()) {
			showMandatoryError("Vorname", vornameTextfield);
			return false;
		}
		if (Optional.ofNullable(nachnameTextfield.getText()).orElse("").isBlank()) {
			showMandatoryError("Nachname", nachnameTextfield);
			return false;
		}
		if (Optional.ofNullable(ortTextfield.getText()).orElse("").isBlank()) {
			showMandatoryError("Ort", ortTextfield);
			return false;
		}
		if (Optional.ofNullable(plzTextfield.getText()).orElse("").isBlank()) {
			showMandatoryError("PLZ", plzTextfield);
			return false;
		}
		if (Optional.ofNullable(strasseTextfield.getText()).orElse("").isBlank()) {
			showMandatoryError("Straße", strasseTextfield);
			return false;
		}

		entry.setVorname(vornameTextfield.getText());
		entry.setNachname(nachnameTextfield.getText());
		entry.setOrt(ortTextfield.getText());
		entry.setPlz(plzTextfield.getText());
		entry.setStrasse(strasseTextfield.getText());
		return true;
	}
}
