/*
 * Created on 25.04.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.sql.wrapper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ConcurrentModificationException;
import java.util.Properties;

// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
// //////////////////////////////////////////////////////////////////////////
final class PoolConnectionInfo implements ConnectionInfo, Runnable {
	static final String getKey1(final String url, final Properties info) {
		return url + '#' + System.identityHashCode( info );
	}
	
	static final String getKey2(final String url, final Properties info) {
		final StringBuilder key = new StringBuilder( url.length() << 1 ).append( url ).append( '?' );
		for (;;) {
			try {
				for (final Object object : info.keySet()) {
					final String name = object.toString();
					key.append( name ).append( '=' ).append( info.getProperty( name ) ).append( ';' );
				}
				return key.toString();
			} catch (final ConcurrentModificationException e) {
				continue;
			}
		}
	}
	
	private final PoolConnectionList[]	lists;
	
	private final int					mask;
	
	private int							counterGet		= 0;
	
	private int							counterReuse	= 0;
	
	private int							counterCheck	= 0;
	
	private int							counterLoops	= 0;
	
	private final String				realUrl;
	
	private final Driver				driver;
	
	private final Properties			info;
	
	PoolConnectionInfo(final int leafs,
			final int leafCapacity,
			final int urlPrefixLength,
			final String url,
			final Properties info) throws SQLException {
		this.realUrl = url.substring( urlPrefixLength );
		this.driver = DriverManager.getDriver( this.realUrl );
		this.info = info;
		this.lists = new PoolConnectionList[leafs + 1];
		for (int i = leafs; i >= 0; --i) {
			this.lists[i] = new PoolConnectionList( leafCapacity, this );
		}
		this.mask = leafs - 1;
	}
	
	@Override
	public final int connectionMaxLoops() {
		return 128 + ((--this.counterLoops) & 0xF);
	}
	
	@Override
	public final long connectionTimeToLive() {
		return 300000L;
	}
	
	@Override
	public final Connection createConnection() {
		try {
			final Connection result = this.driver.connect( this.realUrl, this.info );
			if (result == null) {
				throw new RuntimeException( "No connection for: " + this.realUrl );
			}
			return result;
		} catch (final SQLException e) {
			throw new RuntimeException( e );
		}
	}
	
	final ConnectionHolder nextConnectionHolder() {
		final ConnectionHolder holder = this.lists[(--this.counterGet) & this.mask].nextConnectionHolder();
		return holder;
	}
	
	@Override
	public final void reuse(final ConnectionHolder holder) {
		if (!this.lists[(--this.counterReuse) & this.mask].reuse( holder )) {
			holder.destroy();
		}
	}
	
	@Override
	public final void run() {
		try {
			final int index = (--this.counterCheck) & this.mask;
			final PoolConnectionList list = this.lists[this.lists.length - 1];
			list.check();
			this.lists[this.lists.length - 1] = this.lists[index];
			this.lists[index] = list;
		} catch (final Throwable t) {
			t.printStackTrace();
		}
	}
	
	@Override
	public final String toString() {
		return "pci{url=" + this.realUrl + '}';
	}
}
