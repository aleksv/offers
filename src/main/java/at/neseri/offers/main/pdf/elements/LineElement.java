package at.neseri.offers.main.pdf.elements;

import java.io.IOException;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import at.neseri.offers.main.pdf.DocumentContentProvider;

public class LineElement extends AbstractContentStreamElement {

	private float lineWidth = 1f;

	public LineElement(DocumentContentProvider contentProvider) {
		super(contentProvider);
	}

	public void write(float fromX, float fromY, float toX, float toY) throws IOException {
		PDPageContentStream contentStream = getContentStream();
		if (getCurrentLayer() != null) {
			contentStream.beginMarkedContent(COSName.OC, getCurrentLayer());
		}
		contentStream.moveTo(fromX, fromY);
		contentStream.lineTo(toX, toY);
		contentStream.setLineWidth(lineWidth);
		contentStream.setStrokingColor(color);
		contentStream.stroke();
		if (getCurrentLayer() != null) {
			contentStream.endMarkedContent();
		}
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}
}
