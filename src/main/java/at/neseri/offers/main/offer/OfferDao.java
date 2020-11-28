package at.neseri.offers.main.offer;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import at.neseri.offers.main.MainController;
import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.Database;
import at.neseri.offers.main.item.Item;
import at.neseri.offers.main.utils.Reference;

public class OfferDao extends ADao<Offer> {

	public OfferDao(Database database) {
		super(database, "offer");
	}

	@Override
	protected DaoBiConsumer<ResultSet, Offer> getSelectDbMapper() {
		return (rs, o) -> {
			o.withId(rs.getInt("id")).withNote(rs.getString("note"))
					.withItemIds(getItemIds(rs.getInt("id")))
					.withCustomerId(rs.getInt("id_customer"))
					.withItemReference(new Reference<Set<Integer>, List<Item>>((ids) -> ids.stream()
							.map(id -> MainController.getInstance().getItemController().getMasterMap().get(id))
							.collect(Collectors.toList())))
					.withCustomerReference(new Reference<Integer, Customer>(
							(id) -> MainController.getInstance().getCustomerController().getMasterMap().get(id)));
		};
	}

	@Override
	protected Map<String, DaoFunction<Offer, Object>> getUpsertDbMapper() {
		Map<String, DaoFunction<Offer, Object>> map = new HashMap<String, ADao.DaoFunction<Offer, Object>>();
		map.put("note", e -> e.getNote());
		return map;
	}

	private Set<Integer> getItemIds(int offerId) {
		Set<Integer> set = new HashSet<Integer>();
		iterateResultSet("SELECT id_item FROM offerToItem WHERE id_offer = " + offerId,
				rs -> set.add(rs.getInt("id_item")));
		return set;
	}

	@Override
	public Offer getInstance() {
		return new Offer();
	}

}
