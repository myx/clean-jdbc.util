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

/** Title: ae1 Base definitions Description: Copyright: Copyright (c) 2001 Company:
 *
 * @author Alexander I. Kharitchev
 * @version 1.0 */
final class DriverPool8x4 implements Driver {

	private static final Map<String, PoolConnectionInfo> CONNECTIONS_BY_KEY = new ConcurrentHashMap<>(64, 0.1f);

	private static final String URL_PREFFIX = "jdbc:pool(8,4):";

	private static final int URL_PREFFIX_LENGTH = DriverPool8x4.URL_PREFFIX.length();

	static {
		try {
			DriverManager.registerDriver(new DriverPool8x4());
		} catch (final SQLException e) {
			throw new RuntimeException(e);
		}
	}

	private DriverPool8x4() {

		// empty
	}

	@Override
	public final boolean acceptsURL(final String url) {

		return url.startsWith(DriverPool8x4.URL_PREFFIX);
	}

	@Override
	public final Connection connect(final String url, final Properties info) throws SQLException {

		if (!url.startsWith(DriverPool8x4.URL_PREFFIX)) {
			return null;
		}
		final String key1 = PoolConnectionInfo.getKey1(url, info);
		PoolConnectionInfo ci = DriverPool8x4.CONNECTIONS_BY_KEY.get(key1);
		if (ci == null) {
			final String key2 = PoolConnectionInfo.getKey2(url, info);
			ci = DriverPool8x4.CONNECTIONS_BY_KEY.get(key2);
			if (ci == null) {
				final boolean created;
				synchronized (this) {
					ci = DriverPool8x4.CONNECTIONS_BY_KEY.get(key2);
					if (ci == null) {
						ci = new PoolConnectionInfo(8, 4, DriverPool8x4.URL_PREFFIX_LENGTH, url, info);
						DriverPool8x4.CONNECTIONS_BY_KEY.put(key1, ci);
						DriverPool8x4.CONNECTIONS_BY_KEY.put(key2, ci);
						PoolChecker.TO_CHECK.add(ci);
						created = true;
					} else {
						created = false;
					}
				}
				if (created) {
					new PoolFillerThread(this, info, url, 8 * 4).start();
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

		if (!url.startsWith(DriverPool8x4.URL_PREFFIX)) {
			return null;
		}
		final String realUrl = url.substring(DriverPool8x4.URL_PREFFIX_LENGTH);
		final Driver real = DriverManager.getDriver(realUrl);
		return real.getPropertyInfo(realUrl, info);
	}

	@Override
	public boolean jdbcCompliant() {

		return true;
	}

	@Override
	public String toString() {

		return DriverPool8x4.URL_PREFFIX;
	}
}
