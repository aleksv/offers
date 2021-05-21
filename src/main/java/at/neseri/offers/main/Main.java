package at.neseri.offers.main;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;

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

	private final static org.apache.logging.log4j.Logger LOG = LogManager.getLogger(Main.class);

	private final static Database DATABASE = new Database();

	private MainController mainController;
	private static String dbPath = System.getProperty("user.home") + "/" + "at.neseri.chinook.db";

	@Override
	public void start(Stage stage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/at/neseri/offers/main/Main.fxml"));
		Parent root = fxmlLoader.load();
		Scene scene = new Scene(root);
		setUserAgentStylesheet(STYLESHEET_MODENA);
		root.getStylesheets().add("/style.css");
		stage.setScene(scene);
		stage.setTitle("Angebots-Progrämmle");
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
		stage.setWidth(1000);
		stage.setMinWidth(600);
		stage.setHeight(600);
		stage.setMinHeight(600);
		stage.show();

		mainController = fxmlLoader.getController();

	}

	public static void main(String[] args) {
		try {
			if (args.length >= 1) {
				dbPath = args[0];
			}
			launch(args);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public void stop() throws Exception {
		DATABASE.close();
	}

	public static Database getDatabase() {
		DATABASE.setup();
		return DATABASE;
	}

	public static String getDbPath() {
		return dbPath;
	}

}
