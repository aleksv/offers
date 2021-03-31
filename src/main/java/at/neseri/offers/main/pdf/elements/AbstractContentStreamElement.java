package at.neseri.offers.main.pdf.elements;

import org.apache.pdfbox.pdmodel.PDDocumentCatalog;
import org.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentGroup;
import org.apache.pdfbox.pdmodel.graphics.optionalcontent.PDOptionalContentProperties;

import at.neseri.offers.main.pdf.DocumentContentProvider;

public abstract class AbstractContentStreamElement extends AbstractElement {

	private PDOptionalContentGroup currentLayer;

	public AbstractContentStreamElement(DocumentContentProvider contentProvider) {
		super(contentProvider);
	}

	public void setCurrentLayer(String layerName) {
		this.currentLayer = layerName == null ? null : getLayer(layerName);
	}

	public void resetCurrentLayer() {
		this.currentLayer = null;
	}

	protected PDOptionalContentGroup getCurrentLayer() {
		return currentLayer;
	}

	private PDOptionalContentGroup getLayer(String layerName) {
		PDDocumentCatalog catalog = getDocument().getDocumentCatalog();
		PDOptionalContentProperties ocprops = catalog.getOCProperties();
		if (ocprops == null) {
			ocprops = new PDOptionalContentProperties();
			catalog.setOCProperties(ocprops);
		}
		PDOptionalContentGroup layer = null;
		if (ocprops.hasGroup(layerName)) {
			layer = ocprops.getGroup(layerName);
		} else {
			layer = new PDOptionalContentGroup(layerName);
			ocprops.addGroup(layer);
		}
		return layer;
	}

}
