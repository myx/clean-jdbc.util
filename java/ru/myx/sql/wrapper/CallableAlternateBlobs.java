package ru.myx.sql.wrapper;

import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

/**
 * Title: ae1 Base definitions Description: Copyright: Copyright (c) 2001
 * 
 * @author Alexander I. Kharitchev
 * @version 1.0
 */
final class CallableAlternateBlobs extends CallableFilter {
	CallableAlternateBlobs(final Connection connection, final CallableStatement parent) {
		super( connection, parent );
	}
	
	@Override
	public final void setBlob(final int i, final Blob blob) throws SQLException {
		super.setBinaryStream( i, blob.getBinaryStream(), (int) blob.length() );
	}
	
	@Override
	public final void setBytes(final int parm1, final byte[] parm2) throws SQLException {
		super.setBinaryStream( parm1, new java.io.ByteArrayInputStream( parm2 ), parm2.length );
	}
	
	@Override
	public final void setNull(final int parameterIndex, final int sqlType) throws SQLException {
		super.setNull( parameterIndex, (sqlType == Types.BLOB)
				? Types.LONGVARBINARY
				: sqlType );
	}
	
	@Override
	public final void setNull(final int paramIndex, final int sqlType, final String typeName) throws SQLException {
		super.setNull( paramIndex, (sqlType == Types.BLOB)
				? Types.LONGVARBINARY
				: sqlType, typeName );
	}
	
	@Override
	public final void setObject(final int parameterIndex, final Object object) throws SQLException {
		if (object instanceof Blob) {
			this.setBlob( parameterIndex, (Blob) object );
		} else {
			super.setObject( parameterIndex, object );
		}
	}
}
