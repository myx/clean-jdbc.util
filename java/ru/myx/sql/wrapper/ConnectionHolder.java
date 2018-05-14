/*
 * Created on 25.04.2003
 */
package ru.myx.sql.wrapper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/** @author myx */
final class ConnectionHolder {
	
	private static final byte DT_NORMAL = 1;
	
	private static final byte DT_ORACLE = 2;
	
	private static final byte DT_UNKNOWN = 0;
	
	private final ConnectionInfo ci;
	
	private Connection conn;
	
	private byte databaseType;
	
	protected long date;
	
	protected int loops;
	
	ConnectionHolder(final ConnectionInfo ci) {
		
		this.ci = ci;
		this.conn = ci.createConnection();
		this.databaseType = ConnectionHolder.DT_UNKNOWN;
		this.loops = ci.connectionMaxLoops();
		this.date = System.currentTimeMillis() + ci.connectionTimeToLive();
	}
	
	/** goal: to check is connection alive */
	final boolean checkAlive() {
		
		if (this.conn == null) {
			return false;
		}
		try {
			if (this.conn.isClosed()) {
				this.conn = null;
				return false;
			}
			if (--this.loops < 0 || this.date < System.currentTimeMillis()) {
				try {
					this.conn.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.conn = null;
				return false;
			}
			if (this.databaseType == ConnectionHolder.DT_UNKNOWN) {
				this.databaseType = this.conn.getMetaData().getDatabaseProductName().toUpperCase().indexOf("ORACLE") == -1
					? ConnectionHolder.DT_NORMAL
					: ConnectionHolder.DT_ORACLE;
			}
			try (final Statement st = this.conn.createStatement()) {
				/** Do not use it here, may be unsupported? */
				try {
					st.setQueryTimeout(10);
				} catch (final Throwable t) {
					// ignore
				}
				st.execute(
						this.databaseType == ConnectionHolder.DT_ORACLE
							? "SELECT 5 FROM DUAL"
							: "SELECT 5");
			}
			return true;
		} catch (final SQLException e) {
			if (this.conn != null) {
				try {
					this.conn.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.conn = null;
			}
			return false;
		}
	}
	
	/** goal: to get alive connection */
	final void checkEnsure() {
		
		try {
			if (this.conn == null || this.conn.isClosed()) {
				this.conn = this.ci.createConnection();
				this.databaseType = ConnectionHolder.DT_UNKNOWN;
				this.loops = this.ci.connectionMaxLoops();
				this.date = System.currentTimeMillis() + this.ci.connectionTimeToLive();
				return;
			}
			if (--this.loops < 0 || this.date < System.currentTimeMillis()) {
				try {
					this.conn.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.conn = this.ci.createConnection();
				this.databaseType = ConnectionHolder.DT_UNKNOWN;
				this.loops = this.ci.connectionMaxLoops();
				this.date = System.currentTimeMillis() + this.ci.connectionTimeToLive();
				return;
			}
			if (this.databaseType == ConnectionHolder.DT_UNKNOWN) {
				this.databaseType = this.conn.getMetaData().getDatabaseProductName().toUpperCase().indexOf("ORACLE") == -1
					? ConnectionHolder.DT_NORMAL
					: ConnectionHolder.DT_ORACLE;
			}
			try (final Statement st = this.conn.createStatement()) {
				/** Do not use it here, may be unsupported? */
				try {
					st.setQueryTimeout(10);
				} catch (final Throwable t) {
					// ignore
				}
				st.execute(
						this.databaseType == ConnectionHolder.DT_ORACLE
							? "SELECT 5 FROM DUAL"
							: "SELECT 5");
			}
		} catch (final SQLException e) {
			if (this.conn != null) {
				try {
					this.conn.close();
				} catch (final Throwable t) {
					// ignore
				}
			}
			this.conn = this.ci.createConnection();
			this.databaseType = ConnectionHolder.DT_UNKNOWN;
			this.loops = this.ci.connectionMaxLoops();
			this.date = System.currentTimeMillis() + this.ci.connectionTimeToLive();
		}
	}
	
	final void destroy() {
		
		final Connection conn = this.conn;
		if (conn != null) {
			this.conn = null;
			try {
				conn.close();
			} catch (final Throwable t) {
				// ignore
			}
		}
	}
	
	final Connection getConnection() throws SQLException {
		
		if (this.conn == null || this.conn.isClosed()) {
			synchronized (this) {
				if (this.conn == null || this.conn.isClosed()) {
					this.conn = this.ci.createConnection();
					this.databaseType = ConnectionHolder.DT_UNKNOWN;
					this.loops = this.ci.connectionMaxLoops();
					this.date = System.currentTimeMillis() + this.ci.connectionTimeToLive();
				}
			}
		}
		return this.conn;
	}
	
	final void release() throws SQLException {
		
		if (this.conn != null && !this.conn.isClosed()) {
			this.ci.reuse(this);
		}
	}
}
