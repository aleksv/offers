package at.neseri.offers.main.pdf;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

public interface DocumentContentProvider {
	public PDPageContentStream getStream();

	public PDPage getPage();

	public PDDocument getDocument();
}
