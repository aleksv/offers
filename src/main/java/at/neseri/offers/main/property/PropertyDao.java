package at.neseri.offers.main.property;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import at.neseri.offers.main.db.ADao;
import at.neseri.offers.main.db.Database;

public class PropertyDao extends ADao<Property> {

	public PropertyDao(Database database) {
		super(database, "property");
	}

	@Override
	protected DaoBiConsumer<ResultSet, Property> getSelectDbMapper() {
		return (rs, prop) -> {
			prop.setId(rs.getInt("id"));
			prop.setKey(rs.getString("key"));
			prop.setValue(rs.getString("value"));
		};
	}

	@Override
	protected Map<String, DaoFunction<Property, Object>> getUpsertDbMapper() {
		Map<String, DaoFunction<Property, Object>> map = new HashMap<String, ADao.DaoFunction<Property, Object>>();
		map.put("key", c -> c.getKey());
		map.put("value", c -> c.getValue());
		return map;
	}

	@Override
	public Property getInstance() {
		return new Property();
	}

}
