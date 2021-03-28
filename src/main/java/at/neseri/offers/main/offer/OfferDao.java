package at.neseri.offers.main.offer;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
	public void saveEntry(Offer entry) {
		super.saveEntry(entry);
		executeUpdate("DELETE FROM offerPosition WHERE id_offer = ?", Optional.of(ps -> ps.setInt(1, entry.getId())));
		entry.getOfferPositions().forEach(op -> {
			executeUpdate("INSERT INTO offerPosition (id_offer, position, cost, details, title) "
					+ "VALUES(?,?,?,?,?)", Optional.of(ps -> {
						ps.setInt(1, entry.getId());
						ps.setInt(2, op.getPosition());
						ps.setDouble(3, op.getCost());
						ps.setString(4, op.getDetails());
						ps.setString(5, op.getPosTitle());
					}));
		});
	}

	@Override
	public Offer getInstance() {
		Offer o = new Offer()
				.withOfferPositionsReference(
						new Reference<Integer, List<OfferPosition>>((id) -> getOfferPositions(id)))
				.withCustomerReference(new Reference<Integer, Customer>(
						(id) -> MainController.getInstance().getCustomerController().getMasterMap().get(id)));
		return o;
	}

	private List<OfferPosition> getOfferPositions(int offerId) {
		return mapResultSet("SELECT * FROM offerPosition WHERE id_offer = ?",
				rs -> {
					OfferPosition p = new OfferPosition();
					p.setCost(rs.getDouble("cost"));
					p.setDetails(rs.getString("details"));
					p.setPosition(rs.getInt("position"));
					p.setPosTitle(rs.getString("title"));
					return p;
				}, Optional.of(ps -> ps.setInt(1, offerId)));
	}
}
