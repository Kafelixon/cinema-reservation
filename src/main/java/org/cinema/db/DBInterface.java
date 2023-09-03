package org.cinema.db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DBInterface {

    private final DataSource dataSource;

    public enum IsolationLevel {
        NONE(Connection.TRANSACTION_NONE),
        READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
        READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
        REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
        SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

        public final int value;

        IsolationLevel(int value) {
            this.value = value;
        }
    }

    public DBInterface(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> T transactionalOperation(IsolationLevel level, TransactionalTask<T> task) throws DBException {
        try (Connection connection = dataSource.getConnection()) {
            connection.setTransactionIsolation(level.value);
            connection.setAutoCommit(false);

            T result = task.execute(connection);

            connection.commit();
            return result;
        } catch (SQLException e) {
            throw new DBException("Transaction failed", e);
        }
    }

    @FunctionalInterface
    public interface TransactionalTask<T> {
        T execute(Connection connection) throws SQLException;
    }

    public static class DBException extends RuntimeException {
        public DBException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
