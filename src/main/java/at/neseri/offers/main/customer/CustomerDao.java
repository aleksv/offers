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
	protected DaoBiConsumer<ResultSet, Customer> getSelectDbMapper() {
		return (rs, c) -> {
			c.withId(rs.getInt("id")).withVorname(rs.getString("vorname"))
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

	@Override
	public Customer getInstance() {
		return new Customer();
	}
}
