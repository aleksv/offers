package at.neseri.offers.main.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import at.neseri.offers.main.offer.Offer;
import at.neseri.offers.main.offer.OfferPosition;

public class PdfCreator {

	public static void main(String[] args) throws IOException {
//		new PdfCreator().doIt();
	}

	public void doIt(Offer entry) throws IOException {
		// the document
		PDDocument doc = null;
		try {
			doc = new PDDocument();

			PDPage page = new PDPage(PDRectangle.A4);
			doc.addPage(page);
			PDFont font = PDType1Font.HELVETICA;

			PDPageContentStream cs = new PDPageContentStream(doc, page);
			cs.moveTo(0, 0);
			cs.setLeading(14);
			cs.beginText();
			cs.newLineAtOffset(50, page.getCropBox().getHeight() - 150);
			cs.setFont(font, 11);
			cs.showText(entry.getCustomer().getVorname() + " " + entry.getCustomer().getNachname());
			cs.newLine();
			cs.showText("Kapellenweg 28");
			cs.newLine();
			cs.showText("6972 Fuﬂach");
			cs.newLine();
			cs.newLine();
			cs.showText(entry.getSubject());
			cs.newLine();
			cs.newLine();

			String date = "Hohenems, am " + entry.getCreated();

			cs.newLineAtOffset(page.getCropBox().getWidth() - 220, 14);
			cs.showText(date);
			cs.newLine();
			cs.newLine();

			cs.newLineAtOffset(-300, 14);
			cs.newLine();
			cs.newLine();

			int pos = 1;
			for (OfferPosition op : entry.getOfferPositions()) {
				cs.showText("Pos. " + pos + ": " + op.getPosTitle());
				cs.newLine();
				cs.newLine();
				pos++;
			}

			cs.endText();
			cs.close();
			File file = Files.createTempFile("druck", ".pdf").toFile();
			doc.save(file);
			Desktop.getDesktop().open(file);
			file.deleteOnExit();
		} finally {
			if (doc != null) {
				doc.close();
			}
		}
	}

	private static float getTextWidth(PDFont font, int fontSize,
			String text) throws IOException {
		return (font.getStringWidth(text) / 1000.0f) * fontSize;
	}
}
