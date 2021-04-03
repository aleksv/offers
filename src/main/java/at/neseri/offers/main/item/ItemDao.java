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
	protected DaoBiConsumer<ResultSet, Item> getSelectDbMapper() {
		return (rs, item) -> {
			item.withId(rs.getInt("id")).withName(rs.getString("name"));
		};
	}

	@Override
	protected Map<String, DaoFunction<Item, Object>> getUpsertDbMapper() {
		Map<String, DaoFunction<Item, Object>> map = new HashMap<String, ADao.DaoFunction<Item, Object>>();
		map.put("name", c -> c.getName());
		return map;
	}

	@Override
	public Item getInstance() {
		return new Item();
	}

}
