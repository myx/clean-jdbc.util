package ru.myx.sql.wrapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;

/** Title: ae1 Base definitions Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author Alexander I. Kharitchev
 * @version 1.0 */
class StatementFilter implements Statement {
	
	protected Connection connection;
	
	protected Statement parent;
	
	StatementFilter(final Connection connection, final Statement parent) {
		
		this.connection = connection;
		this.parent = parent;
	}
	
	@Override
	public void addBatch(final String sql) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.addBatch(sql);
	}
	
	@Override
	public void cancel() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.cancel();
	}
	
	@Override
	public void clearBatch() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.clearBatch();
	}
	
	@Override
	public void clearWarnings() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.clearWarnings();
	}
	
	@Override
	public void close() throws SQLException {
		
		if (this.parent != null) {
			this.parent.close();
			this.parent = null;
		}
	}
	
	@Override
	public void closeOnCompletion() throws SQLException {
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean execute(final String sql) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.execute(sql);
	}
	
	@Override
	public boolean execute(final String sql, final int agKeys) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.execute(sql, agKeys);
	}
	
	@Override
	public boolean execute(final String sql, final int[] columnIndexes) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.execute(sql, columnIndexes);
	}
	
	@Override
	public boolean execute(final String sql, final String[] columnNames) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.execute(sql, columnNames);
	}
	
	@Override
	public int[] executeBatch() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.executeBatch();
	}
	
	@Override
	public ResultSet executeQuery(final String sql) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.executeQuery(sql);
	}
	
	@Override
	public int executeUpdate(final String sql) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.executeUpdate(sql);
	}
	
	@Override
	public int executeUpdate(final String sql, final int agKeys) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.executeUpdate(sql, agKeys);
	}
	
	@Override
	public int executeUpdate(final String sql, final int[] columnIndexes) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.executeUpdate(sql, columnIndexes);
	}
	
	@Override
	public int executeUpdate(final String sql, final String[] columnNames) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.executeUpdate(sql, columnNames);
	}
	
	@Override
	protected void finalize() throws java.lang.Throwable {
		
		try {
			this.close();
		} catch (final Throwable t) {
			// не интересует
		}
		super.finalize();
	}
	
	@Override
	public Connection getConnection() {
		
		return this.connection;
	}
	
	@Override
	public int getFetchDirection() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getFetchDirection();
	}
	
	@Override
	public int getFetchSize() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getFetchSize();
	}
	
	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getGeneratedKeys();
	}
	
	@Override
	public int getMaxFieldSize() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getMaxFieldSize();
	}
	
	@Override
	public int getMaxRows() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getMaxRows();
	}
	
	@Override
	public boolean getMoreResults() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getMoreResults();
	}
	
	@Override
	public boolean getMoreResults(final int current) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getMoreResults(current);
	}
	
	@Override
	public int getQueryTimeout() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getQueryTimeout();
	}
	
	@Override
	public ResultSet getResultSet() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getResultSet();
	}
	
	@Override
	public int getResultSetConcurrency() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getResultSetConcurrency();
	}
	
	@Override
	public int getResultSetHoldability() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getResultSetHoldability();
	}
	
	@Override
	public int getResultSetType() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getResultSetType();
	}
	
	@Override
	public int getUpdateCount() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getUpdateCount();
	}
	
	@Override
	public SQLWarning getWarnings() throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		return this.parent.getWarnings();
	}
	
	@Override
	public boolean isClosed() {
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isPoolable() {
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isWrapperFor(final Class<?> iface) {
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setCursorName(final String name) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.setCursorName(name);
	}
	
	@Override
	public void setEscapeProcessing(final boolean enable) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.setEscapeProcessing(enable);
	}
	
	@Override
	public void setFetchDirection(final int direction) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.setFetchDirection(direction);
	}
	
	@Override
	public void setFetchSize(final int rows) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.setFetchSize(rows);
	}
	
	@Override
	public void setMaxFieldSize(final int max) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.setMaxFieldSize(max);
	}
	
	@Override
	public void setMaxRows(final int max) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.setMaxRows(max);
	}
	
	@Override
	public void setPoolable(final boolean poolable) {
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setQueryTimeout(final int seconds) throws SQLException {
		
		if (this.parent == null) {
			throw new SQLException("Statement is closed!");
		}
		this.parent.setQueryTimeout(seconds);
	}
	
	@Override
	public <T> T unwrap(final Class<T> iface) {
		
		throw new UnsupportedOperationException();
	}
}
