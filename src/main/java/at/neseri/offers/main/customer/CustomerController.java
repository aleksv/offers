package at.neseri.offers.main.customer;

import com.sun.javafx.collections.ObservableListWrapper;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.utils.UiUtils;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CustomerController {

	@FXML
	private TextField filterTextfield;
	@FXML
	private TableView<Customer> customerTableView;
	private CustomerDao customerDao;
	private ObservableListWrapper<Customer> masterList;

	public void initialize() {
		customerDao = new CustomerDao(Main.getDatabase());
		masterList = new ObservableListWrapper<Customer>(customerDao.getCustomers());

		customerTableView.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
		customerTableView.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("nachname"));
		customerTableView.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("vorname"));

		initContextMenu(customerTableView);

		FilteredList<Customer> filteredData = new FilteredList<>(masterList, p -> true);

		filterTextfield.textProperty().addListener((observable, oldValue, newValue) -> {
			filteredData.setPredicate(customer -> {
				// If filter text is empty, display all persons.
				if (newValue == null || newValue.isEmpty()) {
					return true;
				}

				// Compare first name and last name of every person with filter text.
				String lowerCaseFilter = newValue.toLowerCase();

				if (customer.getVorname().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches first name.
				} else if (customer.getNachname().toLowerCase().contains(lowerCaseFilter)) {
					return true; // Filter matches last name.
				}
				return false; // Does not match.
			});
		});

		SortedList<Customer> sortedList = new SortedList<Customer>(filteredData);
		sortedList.comparatorProperty().bind(customerTableView.comparatorProperty());
		customerTableView.setItems(sortedList);
	}

	private void initContextMenu(TableView<Customer> customerTableView2) {
		ContextMenu menu = new ContextMenu();

		menu.getItems().add(new MenuItem("Bearbeiten"));
		menu.getItems().get(menu.getItems().size() - 1).setOnAction((ActionEvent event) -> {
			System.out.println("Menu item 1");
			Customer customer = customerTableView.getSelectionModel().getSelectedItem();
			System.out.println("Selected item: " + customer);
			openDialog(customer);
		});

		menu.getItems().add(new MenuItem("Löschen"));
		menu.getItems().get(menu.getItems().size() - 1).setOnAction((ActionEvent event) -> {
			System.out.println("Menu item 1");
			Object item = customerTableView.getSelectionModel().getSelectedItem();
			System.out.println("Selected item: " + item);
		});

		customerTableView.setContextMenu(menu);
	}

	private void openDialog(Customer customer) {
		Stage stage = UiUtils.getStage(getClass().getResource("CustomerDialog.fxml"),
				(CustomerDialogController c1) -> c1.setCustomer(customer));
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.setTitle("Kunde");
		stage.setWidth(200);
		stage.setHeight(200);
		stage.showAndWait();
		customerDao.saveCustomer(customer);
		customerTableView.refresh();
	}
}
