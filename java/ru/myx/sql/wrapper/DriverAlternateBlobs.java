package ru.myx.sql.wrapper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

final class DriverAlternateBlobs implements Driver {
	
	private static final String URL_PREFFIX = "jdbc:alternateBlobs:";
	
	static {
		try {
			DriverManager.registerDriver(new DriverAlternateBlobs());
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	private DriverAlternateBlobs() {
		
		// empty
	}
	
	@Override
	public final boolean acceptsURL(final String url) {
		
		return url.startsWith(DriverAlternateBlobs.URL_PREFFIX);
	}
	
	@Override
	public final Connection connect(final String url, final Properties info) throws SQLException {
		
		if (!url.startsWith(DriverAlternateBlobs.URL_PREFFIX)) {
			return null;
		}
		final String realUrl = url.substring(DriverAlternateBlobs.URL_PREFFIX.length());
		final Driver real = DriverManager.getDriver(realUrl);
		if (real == null) {
			return null;
		}
		final Connection result = real.connect(realUrl, info);
		return result == null
			? null
			: new ConnectionAlternateBlobs(result);
	}
	
	@Override
	public final int getMajorVersion() {
		
		return 4;
	}
	
	@Override
	public final int getMinorVersion() {
		
		return 0;
	}
	
	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		
		throw new SQLFeatureNotSupportedException();
	}
	
	@Override
	public final DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {
		
		final String realUrl = url.substring(DriverAlternateBlobs.URL_PREFFIX.length());
		final Driver real = DriverManager.getDriver(realUrl);
		return real.getPropertyInfo(realUrl, info);
	}
	
	@Override
	public final boolean jdbcCompliant() {
		
		return true;
	}
	
	@Override
	public String toString() {
		
		return DriverAlternateBlobs.URL_PREFFIX;
	}
}
