package at.neseri.offers.main.utils;

import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.IIdentity;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public abstract class AStageController<T extends IIdentity, TT extends ADao<T>> {

	@FXML
	private Button saveButton;
	@FXML
	private Button cancelButton;
	private Stage stage;
	protected T entry;
	private AListController<T, TT> listController;

	public Stage getStage() {
		return stage;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setListController(AListController<T, TT> listController) {
		this.listController = listController;
	}

	private void updateEntry() {
		listController.getDaoInstance().saveEntry(entry);
	}

	public final void onSaveButton() {
		save();
		updateEntry();
		getStage().close();
	}

	public final void onCancelButton() {
		getStage().close();
	}

	final public void setEntry(T entry) {
		this.entry = entry;
	}

	public abstract void onLoad();

	protected abstract void save();
}
