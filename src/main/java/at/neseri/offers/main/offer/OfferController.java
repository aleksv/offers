package at.neseri.offers.main.offer;

import java.awt.Desktop;
import java.io.File;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.pdf.OfferPdfCreator;
import at.neseri.offers.main.utils.AListController;
import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

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

	@Override
	protected ContextMenu initContextMenu() {
		ContextMenu menu = super.initContextMenu();
		menu.getItems().add(new MenuItem("Druckvorschau"));
		menu.getItems().get(menu.getItems().size() - 1).setOnAction((ActionEvent event) -> {
			Offer entry = tableView.getSelectionModel().getSelectedItem();

			try (OfferPdfCreator pdfCreator = new OfferPdfCreator(entry);) {
				File file = Files.createTempFile("vorschau", ".pdf").toFile();
				file.deleteOnExit();
				pdfCreator.save(file.getAbsolutePath());
				Desktop.getDesktop().open(file);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
		return menu;
	}

	public List<String> getPosTitleSuggestions() {
		return dao.getPosTitleSuggestions();
	}
}
