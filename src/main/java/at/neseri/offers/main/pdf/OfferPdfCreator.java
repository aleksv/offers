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
import java.util.Map;
import java.util.Optional;

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
import at.neseri.offers.main.property.PropertyKey;

public class OfferPdfCreator extends AbstractPdfCreator {

	protected final static PDFont FONT = PDType1Font.HELVETICA;
	protected final static PDFont FONT_BOLD = PDType1Font.HELVETICA_BOLD;
	protected final static float FONT_SIZE_SMALL = 8;
	protected final static float FONT_SIZE_NORMAL = 10;
	protected final static float FONT_SIZE_BIG = 12;
	protected final static float MARGIN = 60;
	protected final static float MARGIN_TOP = 60;
	protected final static Color COLOR_LIGHT_GRAY = new Color(229, 233, 229);
	protected final static Color COLOR_DARK_GRAY = new Color(204, 204, 204);

	private TextElement textElement;
	private LineElement lineElement;
	private RectangleElement rectangleElement;
	private PDImageXObject logo;
	private Offer offer;
	private Map<PropertyKey, String> propertyMap;

	public OfferPdfCreator(Offer offer, Map<PropertyKey, String> propertyMap) {
		super();
		this.offer = offer;
		this.propertyMap = propertyMap;
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
		setLogo(Main.class.getResourceAsStream("/maier-maler.jpg").readAllBytes());

		addNewPageToDocument();
		Customer customer = offer.getCustomer();

		textElement.setColor(Color.BLACK);
		textElement.setFontSize(FONT_SIZE_NORMAL);

		textElement.setAlignment(Align.L);
		float currY = getPageHeight() - 150;
		boolean hasFirma = !Optional.ofNullable(customer.getFirma()).orElse("").isBlank();
		if (hasFirma) {
			currY += FONT_SIZE_NORMAL * 1.4;
			textElement.write(MARGIN, currY, customer.getFirma());
			currY -= FONT_SIZE_NORMAL * 1.4;
		}
		if (!Optional.ofNullable(customer.getVorname()).orElse("").isBlank()
				|| !Optional.ofNullable(customer.getNachname()).orElse("").isBlank()) {
			textElement.write(MARGIN, currY,
					(hasFirma ? "z. Hd. " : "") + customer.getVorname() + " " + customer.getNachname());
			currY -= FONT_SIZE_NORMAL * 1.4;
		}
		textElement.write(MARGIN, currY, customer.getStrasse());
		currY -= FONT_SIZE_NORMAL * 1.4;
		textElement.write(MARGIN, currY, customer.getPlz() + " " + customer.getOrt());

		currY -= 25;
		textElement.setAlignment(Align.R);
		textElement.write(getPageWidth() - MARGIN, currY,
				propertyMap.get(PropertyKey.ORT) + ", am "
						+ offer.getCreated().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
		textElement.setAlignment(Align.L);

		currY -= 25;
		textElement.setFontSize(FONT_SIZE_BIG);
		textElement.write(MARGIN, currY, "Angebot");
		textElement.write(MARGIN + 0.3f, currY + 0.3f, "Angebot");
		currY -= FONT_SIZE_BIG * 1.4;

		textElement.setFontSize(FONT_SIZE_NORMAL);
		textElement.write(MARGIN, currY, "Betreff: " + offer.getSubject());
		currY -= FONT_SIZE_NORMAL * 0.5f;

		lineElement.write(MARGIN, currY - 4f, getPageWidth() - MARGIN, currY - 4f);
		currY -= FONT_SIZE_NORMAL * 1.6;
		textElement.write(MARGIN, currY, "Material / Leistungsumfang");
		textElement.write(MARGIN + 0.25f, currY + 0.25f, "Material / Leistungsumfang");
		textElement.setAlignment(Align.R);
		textElement.write(getPageWidth() - MARGIN * 2.1f, currY, "Einzelpreis");
		textElement.write(getPageWidth() - MARGIN * 2.1f + 0.25f, currY + 0.25f, "Einzelpreis");
		textElement.write(getPageWidth() - MARGIN, currY, "Gesamtpreis");
		textElement.write(getPageWidth() - MARGIN + 0.25f, currY + 0.25f, "Gesamtpreis");
		textElement.setAlignment(Align.L);
		currY -= FONT_SIZE_NORMAL;
		lineElement.write(MARGIN, currY + 4f, getPageWidth() - MARGIN, currY + 4f);

		currY -= FONT_SIZE_NORMAL * 3;

		int pos = 1;
		double sum = 0;
		NumberFormat numberInstance = NumberFormat.getNumberInstance(Locale.GERMAN);
		numberInstance.setMinimumFractionDigits(2);
		numberInstance.setMaximumFractionDigits(2);
		for (OfferPosition op : offer.getOfferPositions()) {

			float detailsHeight = textElement.calcMultilineHeight(op.getDetails(), getPageWidth() - 5 * MARGIN);

			if (currY - MARGIN * 1.5 - ((FONT_SIZE_NORMAL * 1.4) * 2 + detailsHeight) < 0) {
				addNewPageToDocument();
				currY = getPageHeight() - 150;
			}

			if (!Optional.ofNullable(op.getPosTitle()).orElse("").isBlank()) {
				textElement.write(MARGIN, currY, "Pos. " + (pos) + ": " + op.getPosTitle());
				textElement.write(MARGIN + 0.25f, currY + 0.25f, "Pos. " + (pos++) + ": " + op.getPosTitle());
				currY -= FONT_SIZE_NORMAL * 1.4;
			}

			textElement.writeMultiline(MARGIN + 10, currY, getPageWidth() - 5 * MARGIN, op.getDetails());
			currY -= detailsHeight;

			textElement.setAlignment(Align.R);

			if (op.getSingleCost() > 0) {
				textElement.write(getPageWidth() - MARGIN * 2.1f, currY + FONT_SIZE_NORMAL * 1.4f,
						numberInstance.format(op.getSingleCost()));
			}
			if (op.getCost() > 0) {
				textElement.write(getPageWidth() - MARGIN, currY + FONT_SIZE_NORMAL * 1.4f,
						numberInstance.format(op.getCost()));
			}

			textElement.setAlignment(Align.L);

			currY -= FONT_SIZE_NORMAL * 1.4;
			sum += op.getCost();
		}
		currY -= FONT_SIZE_NORMAL * 1.4;
		if (offer.getNote() != null) {
			float height = textElement.calcMultilineHeight(offer.getNote(), getPageWidth() - 2 * MARGIN);
			if (currY - MARGIN * 1.5 - ((FONT_SIZE_NORMAL * 1.4) * 2 + height) < 0) {
				addNewPageToDocument();
				currY = getPageHeight() - 150;
			}
			textElement.writeMultiline(MARGIN, currY, getPageWidth() - 2 * MARGIN, offer.getNote());
			currY -= height;
		}

		if (currY - MARGIN - 230 < 0) {
			addNewPageToDocument();
		}
		textElement.write(MARGIN, 230, "Sämtliche Abdeckungsleistungen sind im Preis inbegriffen.");
		lineElement.write(MARGIN, 220, getPageWidth() - MARGIN, 220);
		textElement.setAlignment(Align.R);
		textElement.write(getPageWidth() - MARGIN - 130, 210, "EUR");
		textElement.write(getPageWidth() - MARGIN, 210, numberInstance.format(sum));
		textElement.write(getPageWidth() - MARGIN - 130, 196, "EUR");
		textElement.write(getPageWidth() - MARGIN, 196, numberInstance.format(sum * 0.2));
		lineElement.write(getPageWidth() - MARGIN - 150, 194, getPageWidth() - MARGIN, 194);
		textElement.write(getPageWidth() - MARGIN - 130, 182, "EUR");
		textElement.write(getPageWidth() - MARGIN - 130 + 0.25f, 182 + 0.25f, "EUR");
		textElement.write(getPageWidth() - MARGIN, 182,
				numberInstance.format(sum * 1.2));
		textElement.write(getPageWidth() - MARGIN - 0.25f, 182.2f,
				numberInstance.format(sum * 1.2));
		lineElement.write(getPageWidth() - MARGIN - 150, 180, getPageWidth() - MARGIN, 180);
		lineElement.write(getPageWidth() - MARGIN - 150, 178, getPageWidth() - MARGIN, 178);

		textElement.setAlignment(Align.L);
		textElement.write(getPageWidth() - MARGIN - 250, 210, "Nettosumme");
		textElement.write(getPageWidth() - MARGIN - 250, 196, "20 % MwSt.");
		textElement.write(getPageWidth() - MARGIN - 250, 182, "Bruttosumme");
		textElement.write(getPageWidth() - MARGIN - 250 + 0.25f, 182 + 0.25f, "Bruttosumme");

		String cond = Optional.ofNullable(offer.getCondition()).orElse("").trim();
		textElement.writeMultiline(MARGIN, 155, 600, ""
				+ (cond.isEmpty() ? "" : "Zahlungskonditionen:") + "\n"
				+ "\n"
				+ "Wir hoffen, Ihnen ein entsprechendes Angebot gemacht zu haben\n"
				+ "und erwarten mit großem Interesse Ihren weiteren Bescheid.\n"
				+ "\n"
				+ "Mit freundlichen Grüßen\n"
				+ "Patrick Maier");
		textElement.setAlignment(Align.R);
		textElement.write(getPageWidth() - MARGIN, 155, cond);

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

			drawHeader(simpleContentProvider);

			drawFooter(simpleContentProvider, i, totalPages);
			contentStream.close();
			i++;
		}
	}

	private void drawHeader(SimpleContentProvider simpleContentProvider) throws IOException {
		drawLogo(simpleContentProvider, new Dimension((int) (getPageWidth() - 2 * MARGIN), 100));
		TextElement textElement = new TextElement(simpleContentProvider);
		textElement.setAlignment(Align.R);
		textElement.setColor(new Color(0, 68, 146));
		textElement.setFontSize(FONT_SIZE_SMALL);
		String text = propertyMap.get(PropertyKey.PLZ) + " " + propertyMap.get(PropertyKey.ORT) + ", "
				+ propertyMap.get(PropertyKey.STRASSE) + "         Mobil " + propertyMap.get(PropertyKey.TEL)
				+ "         "
				+ propertyMap.get(PropertyKey.MAIL);
		textElement.write(getPageWidth() - MARGIN, getPageHeight() - 115, text);
		textElement.write(getPageWidth() - MARGIN + 0.1f, getPageHeight() - 115 + 0.1f, text);
	}

	private void drawFooter(DocumentContentProvider contentProvider, int currPage, int totalPages)
			throws IOException {
		TextElement textElement = new TextElement(contentProvider);
		LineElement lineElement = new LineElement(contentProvider);
		textElement.setFontSize(FONT_SIZE_SMALL - 1);
		textElement.setAlignment(Align.R);
		textElement.write(getPageWidth() - MARGIN, 33,
				String.format("Seite %s / %s", currPage, totalPages));

		textElement.setAlignment(Align.L);
		textElement.write(MARGIN, 42, "Dienstgebernummer: " + propertyMap.get(PropertyKey.DIENSTGEBERNUMMER));
		textElement.write(MARGIN, 33, "ATU: " + propertyMap.get(PropertyKey.ATU));
		textElement.write(MARGIN, 24, propertyMap.get(PropertyKey.BANK));

		textElement.setAlignment(Align.C);
		textElement.write(getPageWidth() / 2, 24, "IBAN: " + propertyMap.get(PropertyKey.IBAN));
		textElement.setAlignment(Align.R);
		textElement.write(getPageWidth() - MARGIN, 24, "BIC: " + propertyMap.get(PropertyKey.BIC));

		lineElement.setLineWidth(0.1f);
		lineElement.write(MARGIN - 1, 31.5f, getPageWidth() - MARGIN, 31.5f);
	}

	public void setLogo(byte[] logo) throws IOException {
		BufferedImage image = null;
		InputStream in = new ByteArrayInputStream(logo);
		image = ImageIO.read(in);
		this.logo = LosslessFactory.createFromImage(getDocument(), image);
	}

	private void drawLogo(DocumentContentProvider contentProvider, Dimension dim) throws IOException {
		if (logo == null) {
			return;
		}
		Dimension scaledDim = getScaledDimension(new Dimension(logo.getWidth(), logo.getHeight()), dim);
		contentProvider.getStream().drawImage(logo,
				getPageWidth() - scaledDim.width - MARGIN,
				getPageHeight() - scaledDim.height - MARGIN / 2,
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
