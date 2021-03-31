package at.neseri.offers.main.pdf.elements;

import java.awt.Color;
import java.io.IOException;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPageContentStream;

import at.neseri.offers.main.pdf.DocumentContentProvider;

public class RectangleElement extends AbstractContentStreamElement {

	private boolean fill = false;
	private Color fillColor = Color.WHITE;
	private Color strokeColor = Color.BLACK;
	private float lineWidth = 1f;

	public RectangleElement(DocumentContentProvider contentProvider) {
		super(contentProvider);
	}

	public void write(float x, float y, float width, float height) throws IOException {
		PDPageContentStream contentStream = getContentStream();
		if (getCurrentLayer() != null) {
			contentStream.beginMarkedContent(COSName.OC, getCurrentLayer());
		}
		contentStream.addRect(x, y + lineWidth / 2, width - lineWidth, height - lineWidth);
		contentStream.setLineWidth(lineWidth);
		contentStream.setNonStrokingColor(fillColor);
		contentStream.setStrokingColor(strokeColor);
		if (fill) {
			contentStream.fillAndStroke();
		} else {
			contentStream.stroke();
		}
		if (getCurrentLayer() != null) {
			contentStream.endMarkedContent();
		}
	}

	public void setLineWidth(float lineWidth) {
		this.lineWidth = lineWidth;
	}

	public void setFill(boolean fill) {
		this.fill = fill;
	}

	public void setFillColor(Color fillColor) {
		this.fillColor = fillColor;
	}

	public void setStrokeColor(Color strokeColor) {
		this.strokeColor = strokeColor;
	}
}
