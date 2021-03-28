package at.neseri.offers.main.offer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.utils.AListController;

public class OfferController extends AListController<Offer, OfferDao> {

	@Override
	public boolean hasFilterMatched(Offer entry, String lowerCaseFilter) {
		return Optional.ofNullable(entry.getSubject()).orElse("").toLowerCase().contains(lowerCaseFilter)
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
		return m;
	}

	@Override
	protected String getTitle() {
		return "Angebote";
	}

	@Override
	protected int getWidth() {
		return 850;
	}

	@Override
	protected int getHeight() {
		return 750;
	}
}
