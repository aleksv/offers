package at.neseri.offers.main.item;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.Database;

public class ItemDao extends ADao<Item> {

	public ItemDao(Database database) {
		super(database, "item");
	}

	@Override
	protected DaoFunction<ResultSet, Item> getSelectDbMapper() {
		return rs -> {
			return new Item().withId(rs.getInt("id")).withName(rs.getString("name")).withPrice(rs.getDouble("price"))
					.withUnit(rs.getString("unit"));
		};
	}

	@Override
	protected Map<String, DaoFunction<Item, Object>> getUpsertDbMapper() {
		Map<String, DaoFunction<Item, Object>> map = new HashMap<String, ADao.DaoFunction<Item, Object>>();
		map.put("name", c -> c.getName());
		map.put("price", c -> c.getPrice());
		map.put("unit", c -> c.getUnit() == null ? c.getUnit() : c.getUnit().toString());
		return map;
	}

//	@Override
//	public List<Item> getEntries() {
//		List<Item> customers = new ArrayList<>();
//		iterateResultSet("SELECT id, name, price, unit FROM item", rs -> {
//			customers.add(new Item().withId(rs.getInt("id")).withName(rs.getString("name"))
//					.withPrice(rs.getDouble("price")).withUnit(rs.getString("unit")));
//		});
//		return customers;
//	}
//
//	@Override
//	public void saveEntry(Item customer) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void deleteEntry(Item customer) {
//		// TODO Auto-generated method stub
//
//	}
}
