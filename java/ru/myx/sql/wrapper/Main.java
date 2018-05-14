package ru.myx.sql.wrapper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/** Title: ae1 Base definitions Description: Copyright: Copyright (c) 2001 Company:
 * 
 * @author Alexander I. Kharitchev
 * @version 1.0 */
public final class Main implements Driver {
	
	static {
		Main.init(DriverPool2x2.class);
		Main.init(DriverPool4x4.class);
		Main.init(DriverPool8x4.class);
		Main.init(DriverPool16x4.class);
		Main.init(DriverSticky.class);
		Main.init(DriverAlternateBlobs.class);
		Main.init(DriverNoImplicitTransactions.class);
	}
	
	private static final void init(final Class<?> cls) {
		
		try {
			Class.forName(cls.getName());
		} catch (final Throwable t) {
			// ignore
		}
	}
	
	private Main() {
		
		// empty
	}
	
	@Override
	public final boolean acceptsURL(final String url) {
		
		return false;
	}
	
	@Override
	public final Connection connect(final String url, final Properties info) {
		
		return null;
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
	public final DriverPropertyInfo[] getPropertyInfo(final String url, final Properties info) {
		
		return null;
	}
	
	@Override
	public final boolean jdbcCompliant() {
		
		return false;
	}
}
