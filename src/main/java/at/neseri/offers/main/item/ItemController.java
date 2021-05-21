package at.neseri.offers.main.item;

import java.util.LinkedHashMap;
import java.util.Map;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.utils.AListController;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;

public class ItemController extends AListController<Item, ItemDao> {

	@Override
	public boolean hasFilterMatched(Item entry, String lowerCaseFilter) {
		return entry.getName().toLowerCase().contains(lowerCaseFilter);
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
		return m;
	}

	@Override
	public void initialize() {
		super.initialize();
		TableColumn<Item, ?> idCol = tableView.getColumns().get(0);
		idCol.setMinWidth(80);
		idCol.setMaxWidth(80);

		tableView.getSortOrder().clear();
		TableColumn<Item, ?> nameCol = tableView.getColumns().get(1);
		nameCol.setSortType(SortType.ASCENDING);
		tableView.getSortOrder().add(nameCol);
		tableView.sort();
	}

	@Override
	protected String getTitle() {
		return "Positionen";
	}

	@Override
	protected int getWidth() {
		return 600;
	}

	@Override
	protected int getHeight() {
		return 180;
	}
}
