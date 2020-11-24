package at.neseri.offers.main.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class ADao {

	private Database database;

	public ADao(Database database) {
		this.database = database;
	}

	protected Database getDatabase() {
		return database;
	}

	protected void iterateResultSet(String sql, DaoConsumer<ResultSet> cons) {
		System.out.println(sql);
		try {
			ResultSet resultSet = database.getConnection().prepareStatement(sql).executeQuery();
			while (resultSet.next()) {
				cons.accept(resultSet);
			}
			resultSet.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected void executeUpdate(String sql, Optional<DaoConsumer<PreparedStatement>> consumer) {
		try {
			PreparedStatement prepareStatement = database.getConnection().prepareStatement(sql);
			consumer.ifPresent(c -> {
				try {
					c.accept(prepareStatement);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			});
			prepareStatement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public interface DaoConsumer<T> {
		void accept(T t) throws SQLException;
	}
}
