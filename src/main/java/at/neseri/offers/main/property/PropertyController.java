package at.neseri.offers.main.property;

import java.util.LinkedHashMap;
import java.util.Map;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.utils.AListController;

public class PropertyController extends AListController<Property, PropertyDao> {

	@Override
	public boolean hasFilterMatched(Property entry, String lowerCaseFilter) {
		return entry.getKey().toLowerCase().contains(lowerCaseFilter)
				|| entry.getValue().toLowerCase().contains(lowerCaseFilter);
	}

	@Override
	protected PropertyDao getDaoInstance() {
		return new PropertyDao(Main.getDatabase());
	}

	@Override
	protected Map<String, String> getColumnFields() {
		Map<String, String> m = new LinkedHashMap<>();
		m.put("Name", "key");
		m.put("Wert", "value");
		return m;
	}

	@Override
	protected String getTitle() {
		return "Stammdaten";
	}

	@Override
	protected boolean canDelete() {
		return false;
	}

	@Override
	protected int getHeight() {
		return 100;
	}
}
