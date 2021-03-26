package at.neseri.offers.main.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.prefs.Preferences;

public class Database implements AutoCloseable {

	private Connection connection;

	public Database() {
		connection = createConnection();
	}

	private Connection createConnection() {
		Connection conn = null;
		try {
			getDbPath();

			// db parameters
			String url = "jdbc:sqlite:C:\\Users\\av\\Desktop\\chinook.db";
			// create a connection to the database
			conn = DriverManager.getConnection(url);
			System.out.println("Connection to SQLite has been established.");
			return conn;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public void setup() {
		try {
			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS customer ("
					+ "  id integer PRIMARY KEY, "
					+ "  nachname text NOT NULL, "
					+ "  vorname text NOT NULL "
					+ ");");

			statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS item ("
					+ "  id integer PRIMARY KEY, "
					+ "  name text NOT NULL, "
					+ "  unit text NULL, "
					+ "  price real NOT NULL "
					+ ");");

			statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS offer ("
					+ "  id integer PRIMARY KEY, "
					+ "  created integer NOT NULL, "
					+ "  id_customer integer NULL, "
					+ "  note text NULL "
					+ ");");

			statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS offerToItem ("
					+ "  id_offer integer NULL, "
					+ "  id_item integer NULL, "
					+ "  quantity real NULL "
					+ ");");
		} catch (SQLException e) {
			throw new RuntimeException(e);
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

	public void getDbPath() {
		// Retrieve the user preference node for the package com.mycompany
		Preferences prefs = Preferences.userNodeForPackage(getClass());

		// Set the value of the preference
		String newValue = "a string";
		prefs.put("dbPath", newValue);

		// Get the value of the preference;
		// default value is returned if the preference does not exist
		String defaultValue = "default string";
		String propertyValue = prefs.get("dbPath", defaultValue); // "a string"
	}
}
