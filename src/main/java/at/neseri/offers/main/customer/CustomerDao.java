package at.neseri.offers.main.customer;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.Database;

public class CustomerDao extends ADao<Customer> {

	public CustomerDao(Database database) {
		super(database, "customer");
	}

	@Override
	protected DaoFunction<ResultSet, Customer> getSelectDbMapper() {
		return rs -> {
			return new Customer().withId(rs.getInt("id")).withVorname(rs.getString("vorname"))
					.withNachname(rs.getString("nachname"));
		};
	}

	@Override
	protected Map<String, DaoFunction<Customer, Object>> getUpsertDbMapper() {
		Map<String, DaoFunction<Customer, Object>> map = new HashMap<String, ADao.DaoFunction<Customer, Object>>();
		map.put("vorname", c -> c.getVorname());
		map.put("nachname", c -> c.getNachname());
		return map;
	}
//
//	@Override
//	public List<Customer> getEntries() {
//		List<Customer> customers = new ArrayList<>();
//		iterateResultSet("SELECT id, vorname, nachname FROM customer", rs -> {
//			customers.add(new Customer().withId(rs.getInt("id")).withNachname(rs.getString("nachname"))
//					.withVorname(rs.getString("vorname")));
//		});
//		return customers;
//	}
//
//	@Override
//	public void saveEntry(Customer customer) {
//
//		if (customer.getId() > 0) {
//			executeUpdate("UPDATE customer SET vorname = ?, nachname = ? WHERE id = ?", Optional.of(ps -> {
//				ps.setString(1, customer.getVorname());
//				ps.setString(2, customer.getNachname());
//				ps.setInt(3, customer.getId());
//			}));
//		} else {
//			executeUpdate("INSERT INTO customer (vorname, nachname) VALUES(?, ?)", Optional.of(ps -> {
//				ps.setString(1, customer.getVorname());
//				ps.setString(2, customer.getNachname());
//			}));
//			iterateResultSet("SELECT last_insert_rowid()", rs -> {
//				customer.setId(rs.getInt(1));
//			});
//		}
//
//	}
//
//	@Override
//	public void deleteEntry(Customer customer) {
//		executeUpdate("DELETE FROM customer WHERE id = ?", Optional.of(ps -> {
//			ps.setInt(1, customer.getId());
//		}));
//	}
}
