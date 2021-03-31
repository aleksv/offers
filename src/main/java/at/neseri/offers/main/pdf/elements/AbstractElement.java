package at.neseri.offers.main.pdf.elements;

import java.awt.Color;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import at.neseri.offers.main.pdf.DocumentContentProvider;

abstract class AbstractElement {

	private DocumentContentProvider contentProvider;
	protected PDFont font = PDType1Font.HELVETICA;
	protected Color color = Color.BLACK;
	protected float fontSize = 12;

	public AbstractElement(DocumentContentProvider contentProvider) {
		this.contentProvider = contentProvider;
	}

	protected PDPageContentStream getContentStream() {
		return contentProvider.getStream();
	}

	protected PDPage getPage() {
		return contentProvider.getPage();
	}

	protected PDDocument getDocument() {
		return contentProvider.getDocument();
	}

	protected static double getHexColorPercentage(int color) {
		return ((double) color) / 255;
	}

	public void setFont(PDFont font) {
		this.font = font;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}
}
