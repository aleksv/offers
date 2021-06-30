package at.neseri.offers.main.customer;

import java.util.LinkedHashMap;
import java.util.Map;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.MainController;
import at.neseri.offers.main.utils.AListController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;

public class CustomerController extends AListController<Customer, CustomerDao> {

	@Override
	protected CustomerDao getDaoInstance() {
		return new CustomerDao(Main.getDatabase());
	}

	@Override
	public boolean hasFilterMatched(Customer customer, String lowerCaseFilter) {
		return customer.getVorname().toLowerCase().contains(lowerCaseFilter)
				|| customer.getNachname().toLowerCase().contains(lowerCaseFilter)
				|| customer.getFirma().toLowerCase().contains(lowerCaseFilter)
				|| customer.getOrt().toLowerCase().contains(lowerCaseFilter);
	}

	@Override
	protected Map<String, String> getColumnFields() {
		Map<String, String> m = new LinkedHashMap<>();
		m.put("ID", "id");
		m.put("Nachname", "nachname");
		m.put("Vorname", "vorname");
		m.put("Ort", "ort");
		m.put("Firma", "firma");
		return m;
	}

	@Override
	public void initialize() {
		super.initialize();
		TableColumn<Customer, ?> idCol = tableView.getColumns().get(0);
		idCol.setMinWidth(80);
		idCol.setMaxWidth(80);

		tableView.getSortOrder().clear();
		TableColumn<Customer, ?> nachnameCol = tableView.getColumns().get(1);
		nachnameCol.setSortType(SortType.ASCENDING);
		tableView.getSortOrder().add(nachnameCol);
		tableView.sort();
	}

	@Override
	protected boolean canDelete() {
		Customer customer = tableView.getSelectionModel().getSelectedItem();
		if (customer == null) {
			return false;
		}
		return !MainController.getInstance().getOfferController().getMasterList().stream().map(o -> o.getCustomer())
				.anyMatch(c -> c.equals(customer));
	}

	@Override
	protected String getTitle() {
		return "Kunden";
	}

	@Override
	protected int getHeight() {
		return 300;
	}
}
