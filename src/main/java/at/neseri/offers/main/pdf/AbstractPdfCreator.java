package at.neseri.offers.main.pdf;

import java.io.IOException;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDResources;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;

public abstract class AbstractPdfCreator implements AutoCloseable, DocumentContentProvider {

	private final static String FONT_PREFIX = "_";
	private PDDocument document;
	private PDPageContentStream currentContentStream;
	private PDPage currentPage;
	private float pageWidth;
	private float pageHeight;

	public AbstractPdfCreator() {
		document = new PDDocument();

		document.getDocumentCatalog().setAcroForm(new PDAcroForm(document));
		PDAcroForm acroForm = document.getDocumentCatalog().getAcroForm();
		PDResources resources = new PDResources();
		resources.put(COSName.getPDFName(AbstractPdfCreator.getFontName(getFont())), getFont());
		acroForm.setDefaultResources(resources);
		acroForm.setDefaultAppearance("/" + AbstractPdfCreator.getFontName(getFont()) + " 12 Tf 0 g");
		acroForm.setXFA(null);

	}

	public void addNewPageToDocument() throws IOException {
		currentPage = new PDPage(PDRectangle.A4);
		pageWidth = currentPage.getMediaBox().getWidth();
		pageHeight = currentPage.getMediaBox().getHeight();
		document.addPage(currentPage);
		if (currentContentStream != null) {
			currentContentStream.close();
		}
		currentContentStream = new PDPageContentStream(document, currentPage, AppendMode.APPEND, false);
	}

	public void save(String path) throws IOException {
		currentContentStream.close();
		document.save(path);
		currentContentStream = new PDPageContentStream(document, currentPage, AppendMode.APPEND, false);
	}

	@Override
	public void close() throws IOException {
		if (currentContentStream != null) {
			currentContentStream.close();
		}
		document.close();
	}

	@Override
	public PDPageContentStream getStream() {
		return currentContentStream;
	}

	@Override
	public PDDocument getDocument() {
		return document;
	}

	protected float getPageWidth() {
		return pageWidth;
	}

	protected float getPageHeight() {
		return pageHeight;
	}

	@Override
	public PDPage getPage() {
		return currentPage;
	}

	public abstract PDFont getFont();

	public static String getFontName(PDFont font) {
		return FONT_PREFIX + font.getName();
	}

}
