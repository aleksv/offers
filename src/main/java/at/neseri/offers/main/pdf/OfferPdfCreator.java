package at.neseri.offers.main.pdf;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Locale;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.PDPageContentStream.AppendMode;
import org.apache.pdfbox.pdmodel.PDPageTree;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.offer.Offer;
import at.neseri.offers.main.offer.OfferPosition;
import at.neseri.offers.main.pdf.elements.LineElement;
import at.neseri.offers.main.pdf.elements.RectangleElement;
import at.neseri.offers.main.pdf.elements.TextElement;
import at.neseri.offers.main.pdf.elements.TextElement.Align;

public class OfferPdfCreator extends AbstractPdfCreator {

	protected final static PDFont FONT = PDType1Font.HELVETICA;
	protected final static PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
	protected final static float FONT_SIZE_SMALL = 8;
	protected final static float FONT_SIZE_NORMAL = 10;
	protected final static float FONT_SIZE_BIG = 12;
	protected final static float MARGIN = 30;
	protected final static Color COLOR_LIGHT_GRAY = new Color(229, 233, 229);
	protected final static Color COLOR_DARK_GRAY = new Color(204, 204, 204);

	private TextElement textElement;
	private LineElement lineElement;
	private String ueberschrift;
	private String subUeberschrift;
	private RectangleElement rectangleElement;
	private PDImageXObject logo;
	private Offer offer;

	public OfferPdfCreator(Offer offer) {
		super();
		this.offer = offer;
		this.ueberschrift = "teest";
		this.subUeberschrift = "teeexst";
		rectangleElement = new RectangleElement(this);
		textElement = new TextElement(this);
		textElement.setFont(FONT);
		textElement.setFontSize(FONT_SIZE_NORMAL);
		lineElement = new LineElement(this);
	}

	public void drawSubHeader(float y, String text) throws IOException {
		rectangleElement.setLineWidth(0f);
		rectangleElement.setFill(true);
		rectangleElement.setFillColor(COLOR_LIGHT_GRAY);
		rectangleElement.setStrokeColor(COLOR_LIGHT_GRAY);
		rectangleElement.write(MARGIN, y, 150, FONT_SIZE_BIG);

		lineElement.write(MARGIN, y + FONT_SIZE_BIG, getPageWidth() - MARGIN, y + FONT_SIZE_BIG);

		textElement.setFontSize(FONT_SIZE_NORMAL);
		textElement.setFont(FONT_BOLD);
		textElement.write(MARGIN + 2, y + 2.5f, text);
	}

	@Override
	public void save(String path) throws IOException {
		create();
		drawHeaderAndFooter();
		super.save(path);
	}

	private void create() throws IOException {
		setLogo(Main.class.getResourceAsStream("maier-maler.png").readAllBytes());

		addNewPageToDocument();
		Customer customer = offer.getCustomer();

		float currY = getPageHeight() - 100;
		textElement.write(MARGIN, currY, customer.getVorname() + " " + customer.getNachname());
		currY -= FONT_SIZE_NORMAL * 1.4;
		textElement.write(MARGIN, currY, customer.getStrasse());
		currY -= FONT_SIZE_NORMAL * 1.4;
		textElement.write(MARGIN, currY, customer.getPlz() + " " + customer.getOrt());

		currY -= 30;
		textElement.setAlignment(Align.R);
		textElement.write(getPageWidth() - MARGIN, currY,
				"Hohenems am " + offer.getCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		textElement.setAlignment(Align.L);
		currY -= 40;

		textElement.setFontSize(FONT_SIZE_BIG);
		textElement.write(MARGIN, currY, "Angebot");
		textElement.write(MARGIN + 0.1f, currY + 0.1f, "Angebot");
		currY -= FONT_SIZE_BIG * 1.4;

		textElement.setFontSize(FONT_SIZE_NORMAL);
		textElement.write(MARGIN, currY, "Betreff: " + offer.getSubject());
		currY -= FONT_SIZE_NORMAL * 3;

		lineElement.write(MARGIN, currY, getPageWidth() - MARGIN, currY);
		currY -= FONT_SIZE_NORMAL * 1.6;
		textElement.write(MARGIN, currY, "Material / Leistungsumfang");
		textElement.write(MARGIN + 0.1f, currY + 0.1f, "Material / Leistungsumfang");
		textElement.setAlignment(Align.R);
		textElement.write(getPageWidth() - MARGIN, currY, "Gesamtpreis");
		textElement.write(getPageWidth() - MARGIN + 0.1f, currY + 0.1f, "Gesamtpreis");
		textElement.setAlignment(Align.L);
		currY -= FONT_SIZE_NORMAL;
		lineElement.write(MARGIN, currY, getPageWidth() - MARGIN, currY);

		currY -= FONT_SIZE_NORMAL * 3;

		int pos = 1;
		for (OfferPosition op : offer.getOfferPositions()) {
			textElement.write(MARGIN, currY, "Pos. " + (pos) + ": " + op.getPosTitle());
			textElement.write(MARGIN + 0.15f, currY + 0.15f, "Pos. " + (pos++) + ": " + op.getPosTitle());

			textElement.setAlignment(Align.R);
			NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.GERMAN);
			numberInstance.setMinimumFractionDigits(2);
			numberInstance.setMaximumFractionDigits(2);
			textElement.write(getPageWidth() - MARGIN, currY,
					numberInstance.format(op.getCost()));
			textElement.setAlignment(Align.L);

			currY -= FONT_SIZE_NORMAL * 1.4;

			textElement.writeMultiline(MARGIN + 10, currY, getPageWidth() - 2 * MARGIN, op.getDetails());
			currY -= (FONT_SIZE_NORMAL * 1.4) * op.getDetails().split("\n").length;

			currY -= FONT_SIZE_NORMAL * 1.4;
			currY -= FONT_SIZE_NORMAL * 1.4;
		}

		textElement.writeMultiline(MARGIN, currY, getPageWidth() - 2 * MARGIN, offer.getNote());

	}

	private void drawHeaderAndFooter() throws IOException {
		PDPageTree pages = getDocument().getPages();
		Iterator<PDPage> iterator = pages.iterator();
		int totalPages = pages.getCount();
		int i = 1;
		while (iterator.hasNext()) {
			PDPage page = iterator.next();
			PDPageContentStream contentStream;

			if (page.equals(getPage())) {
				contentStream = getStream();
			} else {
				contentStream = new PDPageContentStream(getDocument(), page, AppendMode.APPEND, false);
			}

			SimpleContentProvider simpleContentProvider = new SimpleContentProvider(contentStream, page, getDocument());
			if (i == 1) {
				drawFirstPageHeader(simpleContentProvider);
			}
			drawFooter(simpleContentProvider, i, totalPages);
			contentStream.close();
			i++;
		}
	}

	private void drawFooter(DocumentContentProvider contentProvider, int currPage, int totalPages)
			throws IOException {
		TextElement textElement = new TextElement(contentProvider);
		LineElement lineElement = new LineElement(contentProvider);
		textElement.setFontSize(FONT_SIZE_SMALL);
		textElement.setAlignment(Align.R);
		textElement.write(getPageWidth() - MARGIN, 30,
				String.format("%s / %s", currPage, totalPages));

		textElement.setAlignment(Align.L);
		textElement.write(MARGIN, 30, "Hypo - Bank Hohenems");

		textElement.setAlignment(Align.C);
		textElement.write(getPageWidth() / 2, 30, "IBAN: AT 9258 0001 9144 7850 05");

		lineElement.setLineWidth(0.1f);
		lineElement.write(MARGIN - 1, 40, getPageWidth() - MARGIN, 40);
	}

	private void drawFirstPageHeader(DocumentContentProvider contentProvider) throws IOException {
		TextElement textElement = new TextElement(contentProvider);
		RectangleElement rectangleElement = new RectangleElement(contentProvider);
		LineElement lineElement = new LineElement(contentProvider);
		float height = getPageHeight() - MARGIN;
		float width = logo == null ? getPageWidth() - (MARGIN + MARGIN) : 380;
		textElement.setFontSize(FONT_SIZE_BIG);
		textElement.setFont(FONT_BOLD);
		final float heightHauptueberschrift = 25;
		final float heightSubUeberschrift = 12;
		final float heightSeparator = 2f;

		height -= heightHauptueberschrift;

		rectangleElement.setLineWidth(0f);
		rectangleElement.setFill(true);
		rectangleElement.setFillColor(COLOR_LIGHT_GRAY);
		rectangleElement.setStrokeColor(COLOR_LIGHT_GRAY);
		rectangleElement.write(MARGIN, height, width, heightHauptueberschrift);
		textElement.write(MARGIN + 2, height + heightHauptueberschrift - FONT_SIZE_BIG, ueberschrift);

		height += heightSeparator;
		lineElement.setLineWidth(heightSeparator);
		lineElement.write(MARGIN, height, MARGIN + width, height);

		height -= heightSubUeberschrift;

		rectangleElement.setFillColor(COLOR_DARK_GRAY);
		rectangleElement.setStrokeColor(COLOR_DARK_GRAY);
		rectangleElement.write(MARGIN, height, width, heightSubUeberschrift);
		textElement.setFontSize(FONT_SIZE_NORMAL);
		textElement.write(MARGIN + 2, height + heightSubUeberschrift - FONT_SIZE_NORMAL, subUeberschrift);

		drawLogo(contentProvider);
	}

	public void setLogo(byte[] logo) throws IOException {
		BufferedImage image = null;
		InputStream in = new ByteArrayInputStream(logo);
		image = ImageIO.read(in);
		this.logo = LosslessFactory.createFromImage(getDocument(), image);
	}

	private void drawLogo(DocumentContentProvider contentProvider) throws IOException {
		if (logo == null) {
			return;
		}
		Dimension scaledDim = getScaledDimension(new Dimension(logo.getWidth(), logo.getHeight()), new Dimension(300,
				150));
		contentProvider.getStream().drawImage(logo,
				getPageWidth() - 300 / 2 - scaledDim.width / 2 - MARGIN,
				getPageHeight() - scaledDim.height - MARGIN,
				scaledDim.width,
				scaledDim.height);
	}

	private static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
		int originalWidth = imgSize.width;
		int originalHeight = imgSize.height;
		int boundWidth = boundary.width;
		int boundHeight = boundary.height;
		int newWidth = originalWidth;
		int newHeight = originalHeight;

		if (originalWidth > boundWidth) {
			newWidth = boundWidth;
			newHeight = (newWidth * originalHeight) / originalWidth;
		}
		if (newHeight > boundHeight) {
			newHeight = boundHeight;
			newWidth = (newHeight * originalWidth) / originalHeight;
		}
		return new Dimension(newWidth, newHeight);
	}

	@Override
	public PDFont getFont() {
		return FONT;
	}

	private class SimpleContentProvider implements DocumentContentProvider {

		private PDPageContentStream contentStream;
		private PDPage page;
		private PDDocument document;

		public SimpleContentProvider(PDPageContentStream contentStream, PDPage page, PDDocument document) {
			this.contentStream = contentStream;
			this.page = page;
			this.document = document;
		}

		@Override
		public PDPageContentStream getStream() {
			return contentStream;
		}

		@Override
		public PDPage getPage() {
			return page;
		}

		@Override
		public PDDocument getDocument() {
			return document;
		}
	}
}
