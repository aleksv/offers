package at.neseri.offers.main.utils;

import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UiUtils {
	private UiUtils() {
	}

	/**
	 * 
	 * @param loader
	 * @param isBlocking
	 * @return controller
	 */
	public static <T extends AStageController<?, ?>> Stage getStage(URL resource, Consumer<T> controllerConsumer) {
		try {
			FXMLLoader loader = new FXMLLoader(resource);
			Parent root;
			root = loader.load();
			root.getStylesheets().add("/style.css");
			Scene scene = new Scene(root);
			T controller = loader.getController();
			Stage stage = new Stage();
			stage.setScene(scene);
			controller.setStage(stage);
			controllerConsumer.accept(controller);
			return stage;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
