package at.neseri.offers.main.item;

import java.util.Arrays;

import com.sun.javafx.collections.ObservableListWrapper;

import at.neseri.offers.main.utils.AStageController;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

public class ItemDialogController extends AStageController<Item, ItemDao> {

	@FXML
	private TextField idTextfield;
	@FXML
	private ChoiceBox<Unit> unitChoiceBox;
	@FXML
	private TextField nameTextfield;
	@FXML
	private TextField priceTextfield;

	public void initialize() {
		unitChoiceBox.setItems(new ObservableListWrapper<Unit>(Arrays.asList(Unit.values())));
	}

	@Override
	public void setEntry(Item entry) {
		this.entry = entry;
		idTextfield.setText(String.valueOf(entry.getId()));
		if (entry.getUnit() != null)
			unitChoiceBox.getSelectionModel().select(entry.getUnit());
		if (entry.getName() != null)
			nameTextfield.setText(String.valueOf(entry.getName()));
		priceTextfield.setText(String.valueOf(entry.getPrice()));
	}

	@Override
	protected void save() {
		entry.setName(nameTextfield.getText());
		entry.setUnit(unitChoiceBox.getValue());
		entry.setPrice(Double.valueOf(priceTextfield.getText()));
	}

}
