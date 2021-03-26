package at.neseri.offers.main.item;

import java.util.LinkedHashMap;
import java.util.Map;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.utils.AListController;

public class ItemController extends AListController<Item, ItemDao> {

	@Override
	public boolean hasFilterMatched(Item entry, String lowerCaseFilter) {
		return entry.getName().toLowerCase().contains(lowerCaseFilter)
				|| (entry.getUnit() != null && entry.getUnit().toString().toLowerCase().contains(lowerCaseFilter));
	}

	@Override
	protected ItemDao getDaoInstance() {
		return new ItemDao(Main.getDatabase());
	}

	@Override
	protected Map<String, String> getColumnFields() {
		Map<String, String> m = new LinkedHashMap<>();
		m.put("ID", "id");
		m.put("Name", "name");
		m.put("Einheit", "unit");
		m.put("Preis", "price");
		return m;
	}

	@Override
	protected String getTitle() {
		return "Positionen";
	}

}
