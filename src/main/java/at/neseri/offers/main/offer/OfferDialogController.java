package at.neseri.offers.main.offer;

import at.neseri.offers.main.utils.AStageController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class OfferDialogController extends AStageController<Offer, OfferDao> {

	@FXML
	private TextField idTextfield;

	public void initialize() {
	}

	@Override
	public void setEntry(Offer entry) {
		this.entry = entry;
		idTextfield.setText(String.valueOf(entry.getId()));
	}

	@Override
	protected void save() {
	}

}
