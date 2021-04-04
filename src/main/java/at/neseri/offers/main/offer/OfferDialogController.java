package at.neseri.offers.main.offer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.UnaryOperator;

import org.controlsfx.control.textfield.TextFields;

import at.neseri.offers.main.MainController;
import at.neseri.offers.main.customer.Customer;
import at.neseri.offers.main.item.Item;
import at.neseri.offers.main.utils.AStageController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class OfferDialogController extends AStageController<Offer, OfferDao> {

	@FXML
	private TextField idTextfield;
	@FXML
	private ChoiceBox<Customer> customerChoiceBox;
	@FXML
	private TextField subjectTextfield;
	@FXML
	private TextArea noteTextarea;
	@FXML
	private VBox itemsVbox;
	@FXML
	private Button addItemButton;
	private List<Pane> itemPanes = new ArrayList<>();
	private List<String> posTitleAutocompleteSuggestions;
	private final List<OfferPosition> offerPositions = new ArrayList<>();

	private List<TextField> posTitleTextfields = new ArrayList<>();

	@Override
	public void onLoad() {

		ObservableList<Customer> masterList = MainController.getInstance().getCustomerController().getMasterList();
		customerChoiceBox.setItems(masterList);

		posTitleAutocompleteSuggestions = MainController.getInstance().getOfferController().getPosTitleSuggestions();

		idTextfield.setText(String.valueOf(entry.getId()));
		customerChoiceBox.getSelectionModel().select(entry.getCustomer());
		Optional.ofNullable(entry.getSubject()).ifPresent(subjectTextfield::setText);
		Optional.ofNullable(entry.getNote()).ifPresent(noteTextarea::setText);
		entry.getOfferPositions().stream().forEach(op -> addItemPanel(op));

		offerPositions.addAll(entry.getOfferPositions());

	}

	@Override
	protected boolean save() {
		if (subjectTextfield.getText().trim().isBlank()) {
			showMandatoryError("Betreff", subjectTextfield);
			return false;
		} else if (customerChoiceBox.getSelectionModel().getSelectedItem() == null) {
			showMandatoryError("Kunde", customerChoiceBox);
			return false;
		} else if (posTitleTextfields.stream().filter(o -> o.getText() == null || o.getText().isBlank()).findFirst()
				.isPresent()) {
			showMandatoryError("Pos.-Titel", posTitleTextfields.stream()
					.filter(o -> o.getText() == null || o.getText().isBlank()).findFirst().get());
			return false;
		}

		entry.setCustomerId(customerChoiceBox.getSelectionModel().getSelectedItem().getId());
		entry.setSubject(subjectTextfield.getText());
		entry.setNote(noteTextarea.getText());
		entry.getOfferPositions().clear();
		entry.getOfferPositions().addAll(offerPositions);
		return true;
	}

	public void onAddItemButton() {
		OfferPosition offerPosition = new OfferPosition();
		addItemPanel(offerPosition);
		offerPositions.add(offerPosition);
	}

	@SuppressWarnings("unchecked")
	private void addItemPanel(OfferPosition offerPosition) {
		try {
			itemsVbox.getChildren().add(new Separator());
			GridPane gp = FXMLLoader.load(getClass().getResource("Item.fxml"));
			itemPanes.add(gp);

			AtomicReference<ChoiceBox<Item>> addItemChoicebox = new AtomicReference<>();
			AtomicReference<TextArea> detailsTextarea = new AtomicReference<>();
			AtomicReference<TextField> costTextfield = new AtomicReference<>();
			AtomicReference<TextField> posTitleTextfield = new AtomicReference<>();
			AtomicReference<Button> deleteItemButton = new AtomicReference<>();

			gp.getChildren().stream().filter(Objects::nonNull).forEach(n -> {
				switch (Optional.of(n).map(Node::getId).orElse("")) {
				case "detailsTextarea":
					detailsTextarea.set((TextArea) n);
					break;
				case "addItemChoicebox":
					addItemChoicebox.set((ChoiceBox<Item>) n);
					break;
				case "costTextfield":
					costTextfield.set((TextField) n);
					break;
				case "posTitleTextfield":
					posTitleTextfield.set((TextField) n);
					break;
				case "deleteItemButton":
					deleteItemButton.set((Button) n);
					break;
				}
			});

			detailsTextarea.get().setText(offerPosition.getDetails());
			addItemChoicebox.get().setItems(MainController.getInstance().getItemController().getMasterList());
			updateCost(offerPosition, costTextfield);
			posTitleTextfield.get().setText(offerPosition.getPosTitle());

			detailsTextarea.get().textProperty().addListener(s -> {
				offerPosition.setDetails(((javafx.beans.property.StringProperty) s).get());
			});

			posTitleTextfield.get().textProperty().addListener(s -> {
				offerPosition.setPosTitle(((javafx.beans.property.StringProperty) s).get());
			});

			costTextfield.get().textProperty().addListener(s -> {
				String strValue = ((javafx.beans.property.StringProperty) s).get();
				if (!strValue.isBlank()) {
					offerPosition.setCost(Double.parseDouble(strValue.replace(",", ".")));
				}
			});

			addItemChoicebox.get().getSelectionModel().selectedIndexProperty()
					.addListener((observableValue, number, number2) -> {
						int index = number2.intValue();
						if (index < 0) {
							return;
						}
						Item value = addItemChoicebox.get().getItems().get(index);
						if (Optional.ofNullable(detailsTextarea.get().getText()).orElse("").trim().isBlank()) {
							detailsTextarea.get().setText(String.valueOf(value));
						} else {
							detailsTextarea.get()
									.appendText("\n" + String.valueOf(value));
						}
						addItemChoicebox.get().getSelectionModel().clearSelection();
					});

			UnaryOperator<Change> filter = change -> {
				String text = change.getText();
				if (text.matches("[0-9,]*")
						&& ((costTextfield.get().getText() + text).replaceAll("[^,]+", "")).length() <= 1) {
					return change;
				}
				return null;
			};

			TextFormatter<String> textFormatter = new TextFormatter<>(filter);
			costTextfield.get().setTextFormatter(textFormatter);

			TextFields.bindAutoCompletion(posTitleTextfield.get(), posTitleAutocompleteSuggestions);

			deleteItemButton.get().setOnAction(e -> {
				offerPositions.remove(offerPosition);
				itemsVbox.getChildren().remove(gp);
				posTitleTextfields.remove(posTitleTextfield.get());
			});

			itemsVbox.getChildren().add(gp);
			posTitleTextfields.add(posTitleTextfield.get());

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void updateCost(OfferPosition offerPosition, AtomicReference<TextField> costTextfield) {
		if (offerPosition.getCost() > 0) {
			costTextfield.get().textProperty()
					.set(String.valueOf(((double) Math.round((offerPosition.getCost()) * 100)) / 100)
							.replace(".",
									","));
		} else {
			costTextfield.get().clear();
		}
	}

}
