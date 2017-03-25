/*
 * Created on 19.04.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.sql.wrapper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
final class ConnectionPool extends ConnectionFilter {
	
	
	private final ConnectionHolder holder;

	private boolean rdo = false;

	private String oldCatalog = null;

	private Map<String, Class<?>> oldTypeMap = null;

	private int oldTILevel = -1;

	private boolean autocommit = true;

	ConnectionPool(final ConnectionHolder holder) throws SQLException {
		super(holder.getConnection());
		this.holder = holder;
	}

	@Override
	public final void close() throws SQLException {
		
		
		final Connection parent = this.parent;
		if (parent != null) {
			this.parent = null;
			if (!parent.isClosed()) {
				try {
					if (this.oldTILevel != -1) {
						parent.setTransactionIsolation(this.oldTILevel);
						this.oldTILevel = -1;
					}
					if (!this.autocommit) {
						parent.rollback();
						parent.setAutoCommit(true);
						this.autocommit = true;
					}
					if (this.rdo) {
						parent.setReadOnly(false);
						this.rdo = false;
					}
					if (this.oldCatalog != null) {
						parent.setCatalog(this.oldCatalog);
						this.oldCatalog = null;
					}
					if (this.oldTypeMap != null) {
						parent.setTypeMap(this.oldTypeMap);
						this.oldTypeMap = null;
					}
					this.holder.release();
				} catch (final SQLException e) {
					this.holder.destroy();
					throw e;
				}
			}
		}
	}

	@Override
	protected final PreparedStatement convert(final PreparedStatement st) {
		
		
		return st;
	}

	@Override
	public final void setAutoCommit(final boolean autoCommit) throws SQLException {
		
		
		super.setAutoCommit(this.autocommit = autoCommit);
	}

	@Override
	public final void setCatalog(final String catalog) throws SQLException {
		
		
		this.oldCatalog = super.getCatalog();
		super.setCatalog(catalog);
	}

	@Override
	public final void setReadOnly(final boolean readOnly) throws SQLException {
		
		
		super.setReadOnly(this.rdo = readOnly);
	}

	@Override
	public final void setTransactionIsolation(final int level) throws SQLException {
		
		
		if (this.oldTILevel == -1) {
			this.oldTILevel = super.getTransactionIsolation();
		}
		if (this.oldTILevel != level) {
			super.setTransactionIsolation(level);
		}
	}

	@Override
	public final void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
		
		
		this.oldTypeMap = super.getTypeMap();
		super.setTypeMap(map);
	}
}
