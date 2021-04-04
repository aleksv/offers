package at.neseri.offers.main.utils;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.IIdentity;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.image.Image;
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
		stage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
	}

	public void setListController(AListController<T, TT> listController) {
		this.listController = listController;
	}

	private void updateEntry() {
		listController.getDaoInstance().saveEntry(entry);
	}

	public final void onSaveButton() {
		if (save()) {
			updateEntry();
			getStage().close();
		}
	}

	public final void onCancelButton() {
		getStage().close();
	}

	final public void setEntry(T entry) {
		this.entry = entry;
	}

	public abstract void onLoad();

	protected abstract boolean save();

	protected void showMandatoryError(String fieldname, Control elem) {
		elem.requestFocus();
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText(fieldname + " ist ein Pflichtfeld");
		alert.showAndWait();
	}
}
