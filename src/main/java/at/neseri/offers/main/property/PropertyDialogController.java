package at.neseri.offers.main.property;

import at.neseri.offers.main.utils.AStageController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class PropertyDialogController extends AStageController<Property, PropertyDao> {

	@FXML
	private TextField idTextfield;
	@FXML
	private TextField valueTextfield;

	@Override
	public void onLoad() {
		valueTextfield.setText(entry.getValue());
	}

	@Override
	protected boolean save() {
		entry.setValue(valueTextfield.getText());
		return true;
	}

}
