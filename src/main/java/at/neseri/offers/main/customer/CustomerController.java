package at.neseri.offers.main.customer;

import java.util.LinkedHashMap;
import java.util.Map;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.utils.AListController;

public class CustomerController extends AListController<Customer, CustomerDao> {

	@Override
	protected Customer getInstance() {
		return new Customer();
	}

	@Override
	protected CustomerDao getDaoInstance() {
		return new CustomerDao(Main.getDatabase());
	}

	@Override
	public boolean hasFilterMatched(Customer customer, String lowerCaseFilter) {
		return customer.getVorname().toLowerCase().contains(lowerCaseFilter)
				|| customer.getNachname().toLowerCase().contains(lowerCaseFilter);
	}

	@Override
	protected Map<String, String> getColumnFields() {
		Map<String, String> m = new LinkedHashMap<>();
		m.put("ID", "id");
		m.put("Nachname", "nachname");
		m.put("Vorname", "vorname");
		return m;
	}
}
