package at.neseri.offers.main.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sun.javafx.collections.ObservableListWrapper;

import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.IIdentity;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class AListController<T extends IIdentity, TT extends ADao<T>> {
	@FXML
	protected TextField filterTextfield;
	@FXML
	protected TableView<T> tableView;
	protected TT dao;
	protected ObservableListWrapper<T> masterList = new ObservableListWrapper<>(Collections.emptyList());
	protected Map<Integer, T> masterMap;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void initialize() {
		dao = getDaoInstance();

		getColumnFields().forEach((title, prop) -> {
			TableColumn c = new TableColumn();
			c.setText(title);
			c.setCellValueFactory(new PropertyValueFactory<>(prop));
			setCellFactory(c, title);
			tableView.getColumns().add(c);
		});

		initContextMenu();
		tableView.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
					T entry = tableView.getSelectionModel().getSelectedItem();
					openDialog(entry);
				}
			}
		});

		updateEntryTable();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setCellFactory(TableColumn c, String title) {
		if (getColumnTypes().getOrDefault(title, Object.class).equals(LocalDate.class)) {
			c.setCellFactory(column -> {
				TableCell<T, LocalDate> cell = new TableCell<T, LocalDate>() {
					@Override
					protected void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						setText(empty || item == null ? null
								: item.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
					}
				};
				return cell;
			});
		}
	}

	protected ContextMenu initContextMenu() {
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
		return menu;
	}

	protected void updateEntryTable() {
		masterList = new ObservableListWrapper<>(dao.getEntries(masterList));
		masterList.sort(getMasterListSortComparator());
		masterMap = masterList.stream().collect(Collectors.toMap(o -> o.getId(), o -> o));
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
		Stage stage = UiUtils.getStage(
				getClass().getResource("/" + getClass().getName().replace(".", "/") + "/Dialog.fxml"),
				(AStageController<T, TT> c1) -> {
					c1.setEntry(entry);
					c1.setListController(AListController.this);
					c1.onLoad();
				});
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle(getTitle());
		stage.setMinWidth(getWidth());
		stage.setMinHeight(getHeight());
		stage.showAndWait();
		updateEntryTable();
	}

	public void onNeuButton() {
		openDialog(dao.getInstance());
	}

	protected Comparator<T> getMasterListSortComparator() {
		return (o1, o2) -> {
			return String.valueOf(o1).compareTo(String.valueOf(o2));
		};
	}

	public abstract boolean hasFilterMatched(T entry, String lowerCaseFilter);

	protected abstract TT getDaoInstance();

	protected abstract Map<String, String> getColumnFields();

	protected Map<String, Class<?>> getColumnTypes() {
		return Collections.emptyMap();
	}

	public Map<Integer, T> getMasterMap() {
		return Collections.unmodifiableMap(masterMap);
	}

	public ObservableList<T> getMasterList() {
		return masterList;
	}

	protected int getHeight() {
		return 400;
	}

	protected int getWidth() {
		return 400;
	}

	protected abstract String getTitle();

}
