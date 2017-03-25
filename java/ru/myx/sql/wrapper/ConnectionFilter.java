package ru.myx.sql.wrapper;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

// import ae1.Log;
// import ae2.Report;
/**
 * Title: ae1 Base definitions Description: Copyright: Copyright (c) 2001
 * Company:
 *
 * @author Alexander I. Kharitchev
 * @version 1.0
 */
class ConnectionFilter implements Connection {
	
	
	protected Connection parent;
	
	ConnectionFilter(final Connection parent) {
		if (parent == null) {
			throw new RuntimeException("Cannot wrap null connection!");
		}
		this.parent = parent;
	}
	
	@Override
	public void abort(final Executor executor) throws SQLException {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void clearWarnings() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.clearWarnings();
	}
	
	@Override
	public void close() throws SQLException {
		
		
		final Connection parent = this.parent;
		if (parent != null) {
			this.parent = null;
			parent.close();
		}
	}
	
	@Override
	public void commit() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.commit();
	}
	
	@SuppressWarnings("static-method")
	protected CallableStatement convert(final CallableStatement st) {
		
		
		return st;
	}
	
	@SuppressWarnings("static-method")
	protected PreparedStatement convert(final PreparedStatement st) {
		
		
		return st;
	}
	
	@Override
	public Array createArrayOf(final String typeName, final Object[] elements) {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Blob createBlob() {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Clob createClob() {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public NClob createNClob() throws SQLException {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public SQLXML createSQLXML() throws SQLException {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public Statement createStatement() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is already closed (" + this + ")!");
		}
		return this.parent.createStatement();
	}
	
	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.createStatement(resultSetType, resultSetConcurrency);
	}
	
	@Override
	public Statement createStatement(final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
	}
	
	@Override
	public Struct createStruct(final String typeName, final Object[] attributes) {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	protected void finalize() throws java.lang.Throwable {
		
		
		if (this.parent != null) {
			try {
				this.close();
			} catch (final Throwable t) {
				t.printStackTrace();
			} finally {
				this.parent = null;
			}
		}
		super.finalize();
	}
	
	@Override
	public boolean getAutoCommit() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.getAutoCommit();
	}
	
	@Override
	public String getCatalog() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.getCatalog();
	}
	
	@Override
	public Properties getClientInfo() {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getClientInfo(final String name) {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getHoldability() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.getHoldability();
	}
	
	@Override
	public DatabaseMetaData getMetaData() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.getMetaData();
	}
	
	@Override
	public int getNetworkTimeout() throws SQLException {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String getSchema() throws SQLException {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public int getTransactionIsolation() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.getTransactionIsolation();
	}
	
	@Override
	public Map<String, Class<?>> getTypeMap() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.getTypeMap();
	}
	
	@Override
	public SQLWarning getWarnings() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.getWarnings();
	}
	
	@Override
	public boolean isClosed() throws SQLException {
		
		
		return this.parent == null || this.parent.isClosed();
	}
	
	@Override
	public boolean isReadOnly() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.isReadOnly();
	}
	
	@Override
	public boolean isValid(final int timeout) {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean isWrapperFor(final Class<?> iface) {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public String nativeSQL(final String sql) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Native sql:
		// sql="+sql);
		return this.parent.nativeSQL(sql);
	}
	
	@Override
	public CallableStatement prepareCall(final String sql) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Prepare call:
		// sql="+sql);
		return this.convert(this.parent.prepareCall(sql));
	}
	
	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Prepare call:
		// sql="+sql);
		return this.convert(this.parent.prepareCall(sql, resultSetType, resultSetConcurrency));
	}
	
	@Override
	public CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Prepare call:
		// sql="+sql);
		return this.convert(this.parent.prepareCall(sql, resultSetType, resultSetConcurrency, resultSetHoldability));
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Prepare statement:
		// sql="+sql);
		return this.convert(this.parent.prepareStatement(sql));
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql, final int agKeys) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Prepare statement:
		// sql="+sql);
		return this.convert(this.parent.prepareStatement(sql, agKeys));
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Prepare statement:
		// sql="+sql);
		return this.convert(this.parent.prepareStatement(sql, resultSetType, resultSetConcurrency));
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency, final int resultHoldability) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Prepare statement:
		// sql="+sql);
		return this.convert(this.parent.prepareStatement(sql, resultSetType, resultSetConcurrency, resultHoldability));
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql, final int[] columnIndexes) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Prepare statement:
		// sql="+sql);
		return this.convert(this.parent.prepareStatement(sql, columnIndexes));
	}
	
	@Override
	public PreparedStatement prepareStatement(final String sql, final String[] columnNames) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		// if(Report.MODE_DEBUG) Log.devel("WSM4/SQL", "Prepare statement:
		// sql="+sql);
		return this.convert(this.parent.prepareStatement(sql, columnNames));
	}
	
	@Override
	public void releaseSavepoint(final java.sql.Savepoint savepoint) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.releaseSavepoint(savepoint);
	}
	
	@Override
	public void rollback() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.rollback();
	}
	
	@Override
	public void rollback(final java.sql.Savepoint savepoint) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.rollback(savepoint);
	}
	
	@Override
	public void setAutoCommit(final boolean autoCommit) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.setAutoCommit(autoCommit);
	}
	
	@Override
	public void setCatalog(final String catalog) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.setCatalog(catalog);
	}
	
	@Override
	public void setClientInfo(final Properties properties) throws SQLClientInfoException {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setClientInfo(final String name, final String value) throws SQLClientInfoException {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setHoldability(final int x) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.setHoldability(x);
	}
	
	@Override
	public void setNetworkTimeout(final Executor executor, final int milliseconds) throws SQLException {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setReadOnly(final boolean readOnly) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.setReadOnly(readOnly);
	}
	
	@Override
	public java.sql.Savepoint setSavepoint() throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.setSavepoint();
	}
	
	@Override
	public java.sql.Savepoint setSavepoint(final String x) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		return this.parent.setSavepoint(x);
	}
	
	@Override
	public void setSchema(final String schema) throws SQLException {
		
		
		throw new UnsupportedOperationException();
	}
	
	@Override
	public void setTransactionIsolation(final int level) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.setTransactionIsolation(level);
	}
	
	@Override
	public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		
		
		if (this.parent == null) {
			throw new SQLException("Connection is closed (" + this + ")!");
		}
		this.parent.setTypeMap(map);
	}
	
	@Override
	public <T> T unwrap(final Class<T> iface) {
		
		
		throw new UnsupportedOperationException();
	}
}
