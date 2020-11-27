package at.neseri.offers.main.utils;

import java.util.Map;
import java.util.Optional;

import com.sun.javafx.collections.ObservableListWrapper;

import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.IIdentity;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class AListController<T extends IIdentity, TT extends ADao<T>> {
	@FXML
	protected TextField filterTextfield;
	@FXML
	protected TableView<T> tableView;
	protected TT dao;
	protected ObservableListWrapper<T> masterList;

	public void initialize() {
		dao = getDaoInstance();

		getColumnFields().forEach((title, prop) -> {
			TableColumn c = new TableColumn();
			c.setText(title);
			c.setCellValueFactory(new PropertyValueFactory<>(prop));
			tableView.getColumns().add(c);
		});

		initContextMenu();
		updateEntryTable();
	}

	protected void initContextMenu() {
		ContextMenu menu = new ContextMenu();

		menu.getItems().add(new MenuItem("Bearbeiten"));
		menu.getItems().get(menu.getItems().size() - 1).setOnAction((ActionEvent event) -> {
			T entry = tableView.getSelectionModel().getSelectedItem();
			openDialog(entry);
		});

		menu.getItems().add(new MenuItem("Löschen"));
		menu.getItems().get(menu.getItems().size() - 1).setOnAction((ActionEvent event) -> {
			T entry = tableView.getSelectionModel().getSelectedItem();

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setHeaderText("Eintrag löschen");
			alert.setContentText(entry.toString());

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				dao.deleteEntry(entry);
				updateEntryTable();
			}

		});

		tableView.setContextMenu(menu);
	}

	protected void updateEntryTable() {
		masterList = new ObservableListWrapper(dao.getEntries());
		FilteredList<T> filteredData = new FilteredList<>(masterList, p -> true);

		filterTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(entry -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();
				return hasFilterMatched(entry, lowerCaseFilter); // Does not match.
			});
		});

		SortedList<T> sortedList = new SortedList<T>(filteredData);
		sortedList.comparatorProperty().bind(tableView.comparatorProperty());
		tableView.setItems(sortedList);
		tableView.refresh();
	}

	protected void openDialog(T entry) {
		Stage stage = UiUtils.getStage(getClass().getResource("Dialog.fxml"),
				(AStageController c1) -> {
					c1.setEntry(entry);
					c1.setListController(AListController.this);
				});
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Kunde");
		stage.setWidth(200);
		stage.setHeight(200);
		stage.showAndWait();
		updateEntryTable();
	}

	public void onNeuButton() {
		openDialog(getInstance());
	}

	public abstract boolean hasFilterMatched(T entry, String lowerCaseFilter);

	protected abstract T getInstance();

	protected abstract TT getDaoInstance();

	protected abstract Map<String, String> getColumnFields();
}
