package ru.myx.sql.wrapper;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;

/** Title: ae1 Base definitions Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author Alexander I. Kharitchev
 * @version 1.0 */
final class PreparedAlternateBlobs extends PreparedFilter {
	
	PreparedAlternateBlobs(final Connection connection, final PreparedStatement parent) {
		
		super(connection, parent);
	}
	
	@Override
	public void setBlob(final int i, final Blob blob) throws java.sql.SQLException {
		
		super.setBinaryStream(i, blob.getBinaryStream(), (int) blob.length());
	}
	
	@Override
	public final void setBytes(final int parm1, final byte[] parm2) throws java.sql.SQLException {
		
		super.setBinaryStream(parm1, new java.io.ByteArrayInputStream(parm2), parm2.length);
	}
	
	@Override
	public final void setNull(final int parameterIndex, final int sqlType) throws java.sql.SQLException {
		
		super.setNull(
				parameterIndex,
				(sqlType == java.sql.Types.BLOB)
					? java.sql.Types.LONGVARBINARY
					: sqlType);
	}
	
	@Override
	public void setNull(final int paramIndex, final int sqlType, final String typeName) throws java.sql.SQLException {
		
		super.setNull(
				paramIndex,
				(sqlType == java.sql.Types.BLOB)
					? java.sql.Types.LONGVARBINARY
					: sqlType,
				typeName);
	}
	
	@Override
	public void setObject(final int parameterIndex, final Object object) throws java.sql.SQLException {
		
		if (object instanceof java.sql.Blob) {
			this.setBlob(parameterIndex, (java.sql.Blob) object);
		} else {
			super.setObject(parameterIndex, object);
		}
	}
}
