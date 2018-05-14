package ru.myx.sql.wrapper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

final class DriverSticky implements Driver {

	private static final Map<String, StickyConnectionInfo> CONNECTIONS_BY_KEY = new ConcurrentHashMap<>(64, 0.1f);

	private static final String URL_PREFFIX = "jdbc:sticky(16,5m,30s):";

	private static final int URL_PREFFIX_LENGTH = DriverSticky.URL_PREFFIX.length();

	static {
		try {
			DriverManager.registerDriver(new DriverSticky());
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private DriverSticky() {

		// empty
	}

	@Override
	public final boolean acceptsURL(final String url) {

		return url.startsWith(DriverSticky.URL_PREFFIX);
	}

	@Override
	public final Connection connect(final String url, final Properties info) throws SQLException {

		if (!url.startsWith(DriverSticky.URL_PREFFIX)) {
			return null;
		}
		final String key1 = PoolConnectionInfo.getKey1(url, info);
		StickyConnectionInfo ci = DriverSticky.CONNECTIONS_BY_KEY.get(key1);
		if (ci == null) {
			final String key2 = PoolConnectionInfo.getKey2(url, info);
			ci = DriverSticky.CONNECTIONS_BY_KEY.get(key2);
			if (ci == null) {
				final boolean created;
				synchronized (this) {
					ci = DriverSticky.CONNECTIONS_BY_KEY.get(key2);
					if (ci == null) {
						ci = new StickyConnectionInfo(16, DriverSticky.URL_PREFFIX_LENGTH, url, info);
						DriverSticky.CONNECTIONS_BY_KEY.put(key1, ci);
						DriverSticky.CONNECTIONS_BY_KEY.put(key2, ci);
						PoolChecker.TO_CHECK.add(ci);
						created = true;
					} else {
						created = false;
					}
				}
				if (created) {
					new PoolFillerThread(this, info, url, 1).start();
				}
			}
		}
		final ConnectionHolder ch = ci.nextConnectionHolder();
		return new ConnectionPool(ch);
	}

	@Override
	public int getMajorVersion() {

		return 4;
	}

	@Override
	public int getMinorVersion() {

		return 0;
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {

		throw new SQLFeatureNotSupportedException();
	}

	@Override
	public final DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) throws SQLException {

		if (!url.startsWith(DriverSticky.URL_PREFFIX)) {
			return null;
		}
		final String realUrl = url.substring(DriverSticky.URL_PREFFIX_LENGTH);
		final Driver real = DriverManager.getDriver(realUrl);
		return real.getPropertyInfo(realUrl, info);
	}

	@Override
	public boolean jdbcCompliant() {

		return true;
	}

	@Override
	public String toString() {

		return DriverSticky.URL_PREFFIX;
	}
}
