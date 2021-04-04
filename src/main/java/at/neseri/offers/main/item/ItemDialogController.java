package at.neseri.offers.main.item;

import java.util.Optional;

import at.neseri.offers.main.utils.AStageController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class ItemDialogController extends AStageController<Item, ItemDao> {

	@FXML
	private TextField idTextfield;
	@FXML
	private TextField nameTextfield;

	public void initialize() {
	}

	@Override
	public void onLoad() {
		idTextfield.setText(String.valueOf(entry.getId()));
		if (entry.getName() != null)
			nameTextfield.setText(String.valueOf(entry.getName()));
	}

	@Override
	protected boolean save() {
		if (Optional.ofNullable(nameTextfield.getText()).orElse("").isBlank()) {
			showMandatoryError("Name", nameTextfield);
			return false;
		}
		entry.setName(nameTextfield.getText());
		return true;
	}

}
