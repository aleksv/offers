package at.neseri.offers.main.offer;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import at.neseri.offers.main.MainController;
import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.Database;
import at.neseri.offers.main.utils.Reference;

public class OfferDao extends ADao<Offer> {

	public OfferDao(Database database) {
		super(database, "offer");
	}

	@Override
	protected DaoBiConsumer<ResultSet, Offer> getSelectDbMapper() {
		return (rs, o) -> {
			o.withId(rs.getInt("id")).withNote(rs.getString("note"))
					.withCustomer(new Reference<Customer>(
							() -> MainController.getInstance().getCustomerController().getMasterMap().get(o.getId())));
		};
	}

	@Override
	protected Map<String, DaoFunction<Offer, Object>> getUpsertDbMapper() {
		Map<String, DaoFunction<Offer, Object>> map = new HashMap<String, ADao.DaoFunction<Offer, Object>>();
		map.put("note", e -> e.getNote());
		return map;
	}

	@Override
	public Offer getInstance() {
		return new Offer();
	}

}
