package ru.myx.sql.wrapper;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Title: ae1 Base definitions Description: Copyright: Copyright (c) 2001
 * Company:
 * 
 * @author Alexander I. Kharitchev
 * @version 1.0
 */
final class ConnectionAlternateBlobs extends ConnectionFilter {
	ConnectionAlternateBlobs(final Connection parent) {
		super( parent );
	}
	
	@Override
	public final CallableStatement prepareCall(final String sql) throws SQLException {
		return new CallableAlternateBlobs( this, super.prepareCall( sql ) );
	}
	
	@Override
	public final CallableStatement prepareCall(final String sql, final int resultSetType, final int resultSetConcurrency)
			throws SQLException {
		return new CallableAlternateBlobs( this, super.prepareCall( sql, resultSetType, resultSetConcurrency ) );
	}
	
	@Override
	public final PreparedStatement prepareStatement(final String sql) throws SQLException {
		return new PreparedAlternateBlobs( this, super.prepareCall( sql ) );
	}
	
	@Override
	public final PreparedStatement prepareStatement(
			final String sql,
			final int resultSetType,
			final int resultSetConcurrency) throws SQLException {
		return new PreparedAlternateBlobs( this, super.prepareCall( sql, resultSetType, resultSetConcurrency ) );
	}
}
