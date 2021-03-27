package at.neseri.offers.main.offer;

import java.util.LinkedHashMap;
import java.util.Map;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.utils.AListController;

public class OfferController extends AListController<Offer, OfferDao> {

	@Override
	public boolean hasFilterMatched(Offer entry, String lowerCaseFilter) {
		return entry.getNote().toLowerCase().contains(lowerCaseFilter)
				|| entry.getCustomer().toString().toLowerCase().contains(lowerCaseFilter);
	}

	@Override
	protected OfferDao getDaoInstance() {
		return new OfferDao(Main.getDatabase());
	}

	@Override
	protected Map<String, String> getColumnFields() {
		Map<String, String> m = new LinkedHashMap<>();
		m.put("ID", "id");
		m.put("Erstellt", "created");
		m.put("Betreff", "subject");
		m.put("Kunde", "customer");
		m.put("Bemerkung", "note");
		return m;
	}

	@Override
	protected String getTitle() {
		return "Angebote";
	}

	@Override
	protected int getWidth() {
		return 800;
	}

	@Override
	protected int getHeight() {
		return 550;
	}
}
