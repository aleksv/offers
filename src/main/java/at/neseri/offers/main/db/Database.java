package at.neseri.offers.main.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database implements AutoCloseable {

	private Connection connection;

	public Database() {
		connection = createConnection();
	}

	private Connection createConnection() {
		Connection conn = null;
		try {
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
}
