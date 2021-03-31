package at.neseri.offers.main.pdf.elements;

import java.io.IOException;

import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.interactive.action.PDAnnotationAdditionalActions;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotationWidget;
import org.apache.pdfbox.pdmodel.interactive.form.PDAcroForm;
import org.apache.pdfbox.pdmodel.interactive.form.PDTextField;

import at.neseri.offers.main.pdf.AbstractPdfCreator;
import at.neseri.offers.main.pdf.DocumentContentProvider;
import at.neseri.offers.main.pdf.elements.TextElement.Align;

public class TextBoxElement extends AbstractElement {

	private boolean isMultiline = false;
	private Align alignment = Align.L;

	public TextBoxElement(DocumentContentProvider contentProvider) {
		super(contentProvider);
	}

	public void write(float x, float y, float width, float height, String name) throws IOException {
		write(x, y, width, height, name, null);
	}

	public void write(float x, float y, float width, float height, String name, String value) throws IOException {

		PDAcroForm acroForm = getDocument().getDocumentCatalog().getAcroForm();

		acroForm.getDefaultResources().put(COSName.getPDFName(AbstractPdfCreator.getFontName(font)), font);
		PDTextField textBox = new PDTextField(acroForm);
		PDAnnotationAdditionalActions actions = new PDAnnotationAdditionalActions();
		textBox.getWidgets().get(0).setActions(actions);
		textBox.setMultiline(isMultiline);
		switch (alignment) {
		case C:
			textBox.setQ(1);
			break;
		case L:
			textBox.setQ(0);
			break;
		case R:
			textBox.setQ(2);
			break;
		}

		textBox.setPartialName(name);
		String defaultAppearanceString = "/" + AbstractPdfCreator.getFontName(font) + " " + fontSize + " Tf "
				+ getHexColorPercentage(color.getRed()) + " " + getHexColorPercentage(color.getGreen()) + " "
				+ getHexColorPercentage(color.getBlue()) + " rg";
		textBox.setDefaultAppearance(defaultAppearanceString);

		acroForm.getFields().add(textBox);

		PDAnnotationWidget widget = textBox.getWidgets().get(0);

		widget.setRectangle(new PDRectangle(x, y, width, height));
		widget.setPage(getPage());

		widget.setPrinted(true);

		getPage().getAnnotations().add(widget);
		if (value != null) {
			textBox.setValue(value.toString());
		}

	}

	public boolean isMultiline() {
		return isMultiline;
	}

	public void setMultiline(boolean isMultiline) {
		this.isMultiline = isMultiline;
	}

	public Align getAlignment() {
		return alignment;
	}

	public void setAlignment(Align alignment) {
		this.alignment = alignment;
	}

}
