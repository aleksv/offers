package at.neseri.offers.main.db;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;

public abstract class ADao<T extends IIdentity> {

	protected final static org.apache.logging.log4j.Logger LOG = LogManager.getLogger(ADao.class);
	private Database database;
	private String tablename;

	public ADao(Database database, String tablename) {
		this.database = database;
		this.tablename = tablename;
	}

	protected abstract DaoBiConsumer<ResultSet, T> getSelectDbMapper();

	public List<T> getEntries(Collection<T> existing) {
		List<T> entries = new ArrayList<>();
		Map<Integer, T> existingMap = existing.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
		iterateResultSet("SELECT * FROM " + tablename, rs -> {
			T entry = Optional.ofNullable(existingMap.get(rs.getInt("id"))).orElse(getInstance());
			getSelectDbMapper().accept(rs, entry);
			entries.add(entry);
		}, Optional.empty());
		return entries;
	}

	protected abstract Map<String, DaoFunction<T, Object>> getUpsertDbMapper();

	public void saveEntry(T entry) {

		Map<String, DaoFunction<T, Object>> mappers = new TreeMap<>(getUpsertDbMapper());
		if (entry.getId() > 0) {
			AtomicInteger i = new AtomicInteger(0);
			executeUpdate("UPDATE " + tablename + " SET "
					+ mappers.keySet().stream().map(k -> " " + k + "=? ").collect(Collectors.joining(","))
					+ " WHERE id = ?", Optional.of(ps -> {
						for (DaoFunction<T, Object> fct : mappers.values()) {
							ps.setObject(i.incrementAndGet(), fct.apply(entry));
							LOG.info("Param: " + fct.apply(entry));
						}
						ps.setObject(i.incrementAndGet(), entry.getId());
					}));
		} else {
			executeUpdate("INSERT INTO " + tablename + " ("
					+ mappers.keySet().stream().map(k -> " " + k + " ").collect(Collectors.joining(",")) + ") VALUES("
					+ mappers.keySet().stream().map(k -> " ? ").collect(Collectors.joining(",")) + ")",
					Optional.of(ps -> {
						AtomicInteger i = new AtomicInteger(0);
						for (DaoFunction<T, Object> fct : mappers.values()) {
							ps.setObject(i.incrementAndGet(), fct.apply(entry));
							LOG.info("Param: " + fct.apply(entry));
						}
					}));
			iterateResultSet("SELECT last_insert_rowid()", rs -> {
				entry.setId(rs.getInt(1));
			}, Optional.empty());
		}

	}

	public void deleteEntry(T entry) {
		executeUpdate("DELETE FROM " + tablename + " WHERE id = ?", Optional.of(ps -> {
			ps.setInt(1, entry.getId());
			LOG.info("Param: " + entry.getId());
		}));
	}

	protected Database getDatabase() {
		return database;
	}

	protected void iterateResultSet(String sql, DaoConsumer<ResultSet> cons,
			Optional<DaoConsumer<PreparedStatement>> ps) {
		LOG.info(sql);
		try {
			PreparedStatement prepareStatement = database.getConnection().prepareStatement(sql);
			if (ps.isPresent()) {
				ps.get().accept(prepareStatement);
			}
			ResultSet resultSet = prepareStatement.executeQuery();
			while (resultSet.next()) {
				cons.accept(resultSet);
			}
			resultSet.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	protected <T2> List<T2> mapResultSet(String sql, DaoFunction<ResultSet, T2> func,
			Optional<DaoConsumer<PreparedStatement>> ps) {
		List<T2> result = new ArrayList<>();
		iterateResultSet(sql, rs -> {
			result.add(func.apply(rs));
		}, ps);
		return result;
	}

	protected void executeUpdate(String sql, Optional<DaoConsumer<PreparedStatement>> consumer) {
		try {
			LOG.info(sql);
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

	public interface DaoBiConsumer<X, T> {
		void accept(X x, T t) throws SQLException;
	}

	public interface DaoFunction<T, S> {
		S apply(T t) throws SQLException;
	}

	public abstract T getInstance();
}
