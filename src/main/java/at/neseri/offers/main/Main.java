package at.neseri.offers.main;

import java.io.IOException;

import at.neseri.offers.main.db.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * 
 * @author av
 *
 */
public class Main extends Application {

	private final static Database DATABASE;
	static {
		DATABASE = new Database();
		DATABASE.setup();
	}

	private MainController mainController;

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main.fxml"));
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root);
		setUserAgentStylesheet(STYLESHEET_MODENA);
		stage.setScene(scene);
		stage.setTitle("FileOps");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
		stage.setWidth(1000);
		stage.setMinWidth(600);
		stage.setHeight(600);
		stage.setMinHeight(600);
		stage.show();

		mainController = fxmlLoader.getController();

	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void stop() throws Exception {
		DATABASE.close();
	}

	public static Database getDatabase() {
		return DATABASE;
	}
}
