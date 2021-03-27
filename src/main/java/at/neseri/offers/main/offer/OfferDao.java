package at.neseri.offers.main.offer;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import at.neseri.offers.main.MainController;
import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.Database;
import at.neseri.offers.main.offer.Offer.OfferPosition;
import at.neseri.offers.main.utils.Reference;

public class OfferDao extends ADao<Offer> {

	public OfferDao(Database database) {
		super(database, "offer");
	}

	@Override
	protected DaoBiConsumer<ResultSet, Offer> getSelectDbMapper() {
		return (rs, o) -> {
			o.withId(rs.getInt("id")).withNote(rs.getString("note")).withCustomerId(rs.getInt("id_customer"))
					.withSubject(rs.getString("subject")).withCreated(LocalDate.ofEpochDay(rs.getLong("created")));
		};
	}

	@Override
	protected Map<String, DaoFunction<Offer, Object>> getUpsertDbMapper() {
		Map<String, DaoFunction<Offer, Object>> map = new HashMap<String, ADao.DaoFunction<Offer, Object>>();
		map.put("note", Offer::getNote);
		map.put("id_customer", Offer::getCustomerId);
		map.put("created", e -> e.getCreated().toEpochDay());
		map.put("subject", Offer::getSubject);
		return map;
	}

	@Override
	public Offer getInstance() {
		Offer o = new Offer().withCustomerReference(new Reference<Integer, Customer>(
				(id) -> MainController.getInstance().getCustomerController().getMasterMap().get(id)));
		getOfferPositions(o);
		return o;
	}

	private void getOfferPositions(Offer offer) {
		iterateResultSet("SELECT * FROM offerPosition WHERE id_offer = " + offer.getId(), rs -> {
			OfferPosition p = offer.new OfferPosition();
			p.setCost(rs.getDouble("cost"));
			p.setDetails(rs.getString("details"));
			p.setPosition(rs.getInt("position"));
			p.setPosTitle(rs.getString("title"));
			offer.getOfferPositions().add(p);
		});
	}
}
