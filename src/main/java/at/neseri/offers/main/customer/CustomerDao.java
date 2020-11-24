package at.neseri.offers.main.customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.Database;

public class CustomerDao extends ADao {

	public CustomerDao(Database database) {
		super(database);
	}

	public List<Customer> getCustomers() {
		List<Customer> customers = new ArrayList<>();
		iterateResultSet("SELECT id, vorname, nachname FROM customer", rs -> {
			customers.add(new Customer().withId(rs.getInt("id")).withNachname(rs.getString("nachname"))
					.withVorname(rs.getString("vorname")));
		});
		return customers;
	}

	public void saveCustomer(Customer customer) {

		if (customer.getId() > 0) {
			executeUpdate("UPDATE customer SET vorname = ?, nachname = ? WHERE id = ?", Optional.of(ps -> {
				ps.setString(1, customer.getVorname());
				ps.setString(2, customer.getNachname());
				ps.setInt(3, customer.getId());
			}));
		} else {
			executeUpdate("INSERT INTO customer (vorname, nachname) VALUES(?, ?)", Optional.of(ps -> {
				ps.setString(1, customer.getVorname());
				ps.setString(2, customer.getNachname());
			}));
			iterateResultSet("SELECT last_insert_rowid()", rs -> {
				customer.setId(rs.getInt(1));
			});
		}

	}

	public void deleteCustomer(Customer customer) {
		executeUpdate("DELETE FROM customer WHERE id = ?", Optional.of(ps -> {
			ps.setInt(1, customer.getId());
		}));
	}
}
