package at.neseri.offers.main.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public abstract class ADao<T extends IIdentity> {

	private Database database;
	private String tablename;

	public ADao(Database database, String tablename) {
		this.database = database;
		this.tablename = tablename;
	}

	protected abstract DaoFunction<ResultSet, T> getSelectDbMapper();

	public List<T> getEntries() {
		List<T> entries = new ArrayList<>();
		iterateResultSet("SELECT * FROM " + tablename, rs -> {
			entries.add(getSelectDbMapper().apply(rs));
		});
		return entries;
	}

	protected abstract Map<String, DaoFunction<T, Object>> getUpsertDbMapper();

	public void saveEntry(T entry) {

		Map<String, DaoFunction<T, Object>> mappers = new TreeMap<>(getUpsertDbMapper());
		if (entry.getId() > 0) {
			executeUpdate("UPDATE " + tablename + " SET "
					+ mappers.keySet().stream().map(k -> " " + k + "=? ").collect(Collectors.joining(","))
					+ " WHERE id = ?", Optional.of(ps -> {
						AtomicInteger i = new AtomicInteger(0);
						for (DaoFunction<T, Object> fct : mappers.values()) {
							ps.setObject(i.incrementAndGet(), fct.apply(entry));
						}
					}));
		} else {
			executeUpdate("INSERT INTO " + tablename + " ("
					+ mappers.keySet().stream().map(k -> " " + k + " ").collect(Collectors.joining(",")) + ") VALUES("
					+ mappers.keySet().stream().map(k -> " ? ").collect(Collectors.joining(",")) + ")",
					Optional.of(ps -> {
						AtomicInteger i = new AtomicInteger(0);
						for (DaoFunction<T, Object> fct : mappers.values()) {
							ps.setObject(i.incrementAndGet(), fct.apply(entry));
						}
					}));
			iterateResultSet("SELECT last_insert_rowid()", rs -> {
				entry.setId(rs.getInt(1));
			});
		}

	}

	public void deleteEntry(T entry) {
		executeUpdate("DELETE FROM " + tablename + " WHERE id = ?", Optional.of(ps -> {
			ps.setInt(1, entry.getId());
		}));
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

	public interface DaoFunction<T, S> {
		S apply(T t) throws SQLException;
	}
}
