package at.neseri.offers.main.pdf.elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.encoding.WinAnsiEncoding;

import at.neseri.offers.main.pdf.DocumentContentProvider;

public class TextElement extends AbstractContentStreamElement {

	private Align alignment = Align.L;

	public TextElement(DocumentContentProvider contentProvider) {
		super(contentProvider);
	}

	public void write(float x, float y, String text) throws IOException {
		text = replaceNonVisibleCharactersWithSpace(text);
		PDPageContentStream contentStream = getContentStream();
		float textOffset;
		switch (alignment) {
		case C:
			textOffset = font.getStringWidth(text) / 2 / 1000f * fontSize;
			break;
		case R:
			textOffset = font.getStringWidth(text) / 1000f * fontSize;
			break;
		default:
			textOffset = 0;
			break;

		}
		if (getCurrentLayer() != null) {
			contentStream.beginMarkedContent(COSName.OC, getCurrentLayer());
		}
		contentStream.beginText();
		contentStream.setNonStrokingColor(color);
		contentStream.setFont(font, fontSize);
		contentStream.newLineAtOffset(x - textOffset, y);
		try {
			contentStream.showText(text);
		} catch (IllegalArgumentException e) {
			contentStream.showText(stripIllegalChars(text));
		}
		contentStream.endText();
		if (getCurrentLayer() != null) {
			contentStream.endMarkedContent();
		}
	}

	private static String stripIllegalChars(String text) {
		StringBuilder stripped = new StringBuilder();
		for (int i = 0; i < text.length(); i++) {
			if (WinAnsiEncoding.INSTANCE.contains(text.charAt(i))) {
				stripped.append(text.charAt(i));
			}
		}
		return stripped.toString();
	}

	public void writeEllipsis(float x, float y, float width, String text) throws IOException {
		text = replaceNonVisibleCharactersWithSpace(text);
		while (getStringWidth(text) > width) {
			text = text.substring(0, text.length() - 4) + "...";
		}

		write(x, y, text);
	}

	public void writeMultiline(float x, float y, float width, String text) throws IOException {
		if (text == null) {
			text = "";
		}
		text = text.trim().replace("\r", "");
		List<String> lines = splitStringIntoLines(text, width);

		PDPageContentStream contentStream = getContentStream();
		contentStream.beginText();
		contentStream.setNonStrokingColor(color);
		contentStream.setFont(font, fontSize);
		contentStream.newLineAtOffset(x, y);
		for (String line : lines) {
			contentStream.showText(stripIllegalChars(line));
			contentStream.newLineAtOffset(0, -getLeading());
		}
		contentStream.endText();
	}

	private List<String> splitStringIntoLines(String text, float width) throws IOException {
		List<String> lines = new ArrayList<>();
		int lastSpace = -1;

		for (String textnl : text.split("\n")) {
			if (textnl.length() == 0) {
				lines.add("");
			} else {
				while (textnl.length() > 0) {
					int spaceIndex = textnl.indexOf(' ', lastSpace + 1);
					if (spaceIndex < 0) {
						spaceIndex = textnl.length();
					}
					String subString = textnl.substring(0, spaceIndex);
					float size = getStringWidth(subString);
					if (size > width) {
						if (lastSpace < 0) {
							lastSpace = spaceIndex;
						}
						subString = textnl.substring(0, lastSpace);
						lines.add(subString);
						textnl = textnl.substring(lastSpace).trim();
						lastSpace = -1;
					} else if (spaceIndex == textnl.length()) {
						lines.add(textnl);
						textnl = "";
					} else {
						lastSpace = spaceIndex;
					}
				}
			}
		}
		return lines;
	}

	public float getStringWidth(String str) throws IOException {
		return fontSize * font.getStringWidth(stripIllegalChars(str)) / 1000;
	}

	private static String replaceNonVisibleCharactersWithSpace(String text) {
		return text.trim().replaceAll("[^\\S ]+", " ");
	}

	public float calcMultilineHeight(String text, float width) throws IOException {
		if (text == null) {
			text = "";
		}
		text = text.trim().replace("\r", "");
		List<String> lines = splitStringIntoLines(text, width);
		return getLeading() * lines.size();
	}

	private float getLeading() {
		return 1.4f * fontSize;
	}

	public void setAlignment(Align alignment) {
		this.alignment = alignment;
	}

	public enum Align {
		L,
		R,
		C;
	}

}
