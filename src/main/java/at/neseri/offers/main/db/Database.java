package at.neseri.offers.main.db;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.property.PropertyKey;

public class Database implements AutoCloseable {

	protected final static org.apache.logging.log4j.Logger LOG = LogManager.getLogger(Database.class);

	private boolean isSetup = false;
	private Connection connection;

	private Connection createConnection() {
		Connection conn = null;
		try {
			String url = "jdbc:sqlite:" + Main.getDbPath();
			conn = DriverManager.getConnection(url);
			return conn;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void setup() {
		if (isSetup) {
			return;
		}
		connection = createConnection();
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS customer ("
					+ "  id integer PRIMARY KEY, "
					+ "  nachname text NOT NULL, "
					+ "  vorname text NOT NULL, "
					+ "  strasse text NOT NULL, "
					+ "  plz text NOT NULL, "
					+ "  ort text NOT NULL, "
					+ "  firma text NULL "
					+ ");");

			statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS item ("
					+ "  id integer PRIMARY KEY, "
					+ "  name text NOT NULL "
					+ ");");

			statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS offer ("
					+ "  id integer PRIMARY KEY, "
					+ "  created integer NOT NULL, "
					+ "  id_customer integer NULL, "
					+ "  note text NULL, "
					+ "  subject text NOT NULL, "
					+ "  condition text NULL "
					+ ");");

			statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS offerPosition ("
					+ "  id integer PRIMARY KEY, "
					+ "  id_offer integer NOT NULL, "
					+ "  position integer NOT NULL, "
					+ "  cost real NULL, "
					+ "  single_cost real NULL, "
					+ "  details text NULL, "
					+ "  title text NOT NULL "
					+ ");");

			statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS property ("
					+ "  id integer PRIMARY KEY, "
					+ "  key text NOT NULL, "
					+ "  value text NOT NULL "
					+ ");");

			statement = connection.createStatement();
			statement.execute("CREATE UNIQUE INDEX IF NOT EXISTS ux_property_key ON property(key);");

			statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS updateInfo ("
					+ "  value text NOT NULL "
					+ ");");

			insertProperties();
			execUpdates();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		isSetup = true;
	}

	private void execUpdates() throws SQLException {
		execUpdateScript("1.0.1.update");
	}

	private void execUpdateScript(String update101) throws SQLException {
		if (hasUpdate(update101)) {
			return;
		}
		try {
			String contents = new String(
					Files.readAllBytes(Paths.get(getClass().getResource(update101).toURI())));
			connection.createStatement().executeUpdate(contents);
			setUpdated(update101);
		} catch (IOException | URISyntaxException e) {
			LOG.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private boolean hasUpdate(String key) throws SQLException {
		PreparedStatement preparedStatement = connection
				.prepareStatement("SELECT value FROM updateInfo WHERE value = ?");
		preparedStatement.setString(1, key);
		ResultSet rs = preparedStatement.executeQuery();
		boolean has = false;
		while (rs.next()) {
			has = true;
		}
		rs.close();
		return has;
	}

	private void setUpdated(String key) throws SQLException {
		PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO updateInfo (value) VALUES(?)");
		preparedStatement.setString(1, key);
		preparedStatement.execute();
	}

	private void insertProperties() throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement(
				"SELECT key FROM property");) {
			ResultSet rs = ps.executeQuery();
			Set<PropertyKey> unsetKeys = new HashSet<>(Arrays.asList(PropertyKey.values()));
			while (rs.next()) {
				unsetKeys.remove(PropertyKey.valueOf(rs.getString("key")));
			}
			if (!unsetKeys.isEmpty()) {
				try (PreparedStatement psInsert = connection
						.prepareStatement("INSERT INTO property (key, value) VALUES (?, ?)");) {
					for (PropertyKey property : unsetKeys) {
						psInsert.setString(1, property.name());
						psInsert.setString(2, "-");
						psInsert.addBatch();
					}
					psInsert.executeBatch();
				}
			}
		}
	}

	public Connection getConnection() {
		return connection;
	}

	@Override
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
