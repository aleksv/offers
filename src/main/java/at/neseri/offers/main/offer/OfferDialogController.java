package at.neseri.offers.main.offer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
	private VBox itemsVbox;
	@FXML
	private Button addItemButton;
	private List<Pane> itemPanes = new ArrayList<>();

	public void initialize() {
		ObservableList<Customer> masterList = MainController.getInstance().getCustomerController().getMasterList();
		customerChoiceBox.setItems(masterList);
	}

	@Override
	public void onLoad() {
		idTextfield.setText(String.valueOf(entry.getId()));
		customerChoiceBox.getSelectionModel().select(entry.getCustomer());
		Optional.ofNullable(entry.getSubject()).ifPresent(s -> subjectTextfield.setText(s));
	}

	@Override
	protected void save() {
		entry.setCustomerId(customerChoiceBox.getSelectionModel().getSelectedItem().getId());
		entry.setSubject(subjectTextfield.getText());
	}

	public void onAddItemButton() {
		System.out.println("+");
		addItemPanel();
	}

	private void addItemPanel() {
		try {
			itemsVbox.getChildren().add(new Separator());
			GridPane gp = FXMLLoader.load(getClass().getResource("Item.fxml"));
			itemPanes.add(gp);

			Label posLabel = null;
			AtomicReference<ChoiceBox<Item>> addItemChoicebox = new AtomicReference<>();
			AtomicReference<TextArea> detailsTextarea = new AtomicReference<>();
			for (Node n : gp.getChildren()) {
				switch (Optional.of(n).map(Node::getId).orElse("")) {
				case "posLabel":
					posLabel = ((Label) n);
					posLabel.setText("Pos. " + itemPanes.size());
					break;
				case "detailsTextarea":
					detailsTextarea.set((TextArea) n);
					break;
				case "addItemChoicebox":
					addItemChoicebox.set((ChoiceBox<Item>) n);
					addItemChoicebox.get().setItems(MainController.getInstance().getItemController().getMasterList());
					break;
				}
			}

			addItemChoicebox.get().getSelectionModel().selectedIndexProperty()
					.addListener((observableValue, number, number2) -> {
						detailsTextarea.get()
								.appendText(String.valueOf(addItemChoicebox.get().getItems().get((Integer) number2))
										+ "\n");
					});

//			addItemChoicebox.get().addEventHandler(eventType, eventHandler);

			itemsVbox.getChildren().add(gp);

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
