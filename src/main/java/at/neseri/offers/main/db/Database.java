package at.neseri.offers.main.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import at.neseri.offers.main.Main;
import at.neseri.offers.main.property.PropertyKey;

public class Database implements AutoCloseable {

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
					+ "  ort text NOT NULL "
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

			insertProperties();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		isSetup = true;
	}

	private void insertProperties() throws SQLException {
		try (PreparedStatement ps = connection.prepareStatement("SELECT COUNT(1) AS count FROM property");) {
			ResultSet rs = ps.executeQuery();
			rs.next();
			if (rs.getInt("count") == 0) {
				try (PreparedStatement psInsert = connection
						.prepareStatement("INSERT INTO property (key, value) VALUES (?, ?)");) {
					for (PropertyKey property : PropertyKey.values()) {
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
