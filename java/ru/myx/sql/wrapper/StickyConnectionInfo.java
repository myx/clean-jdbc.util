package ru.myx.sql.wrapper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class StickyConnectionInfo implements ConnectionInfo, Runnable {
	
	private final String				realUrl;
	
	private final Driver				driver;
	
	private final Properties			info;
	
	private final ConnectionHolder[]	list;
	
	private final long[]				till;
	
	private int							head	= 0;
	
	private int							tail	= 0;
	
	private int							size	= 0;
	
	private final int					caps;
	
	private final int					mask;
	
	StickyConnectionInfo(final int capacity, final int urlPrefixLength, final String url, final Properties info)
			throws SQLException {
		this.realUrl = url.substring( urlPrefixLength );
		this.driver = DriverManager.getDriver( this.realUrl );
		this.info = info;
		this.caps = capacity;
		this.mask = capacity - 1;
		this.list = new ConnectionHolder[capacity];
		this.till = new long[capacity];
	}
	
	@Override
	public final int connectionMaxLoops() {
		return Integer.MAX_VALUE;
	}
	
	@Override
	public long connectionTimeToLive() {
		return 300000L;
	}
	
	@Override
	public Connection createConnection() {
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
		final ConnectionHolder ready;
		synchronized (this) {
			if (this.size > 0) {
				this.size--;
				final int index = (--this.head) & this.mask;
				ready = this.list[index];
				this.list[index] = null;
			} else {
				ready = null;
			}
		}
		if (ready == null) {
			return new ConnectionHolder( this );
		}
		ready.checkAlive();
		return ready;
	}
	
	@Override
	public void reuse(final ConnectionHolder hldr) {
		final long time = System.currentTimeMillis();
		if (hldr.date <= time) {
			hldr.destroy();
			return;
		}
		final ConnectionHolder removed;
		synchronized (this) {
			if (this.size < this.caps) {
				this.size++;
				hldr.loops--;
				// adds to a head (stack like) - because the goal is not to keep
				// much connections alive
				final int index = (this.head++) & this.mask;
				this.list[index] = hldr;
				this.till[index] = time + 30000L;
				return;
			}
			{
				// replace eldest if current one is more fresh and make it
				// latest
				final int index = this.tail & this.mask;
				final ConnectionHolder candidate = this.list[index];
				if (hldr.date > candidate.date) {
					removed = candidate;
					this.list[index] = hldr;
					this.till[index] = time + 30000L;
					this.head++;
					this.tail++;
				} else {
					removed = hldr;
				}
			}
		}
		removed.destroy();
	}
	
	@Override
	public void run() {
		final long time = System.currentTimeMillis();
		for (;;) {
			final ConnectionHolder removed;
			synchronized (this) {
				if (this.size == 0) {
					break;
				}
				// check from tail - most idle connections are there
				final int index = this.tail & this.mask;
				if (this.till[index] > time) {
					break;
				}
				removed = this.list[index];
				this.list[index] = null;
				this.tail++;
				this.size--;
			}
			removed.destroy();
		}
	}
	
	@Override
	public final String toString() {
		return "sci{url=" + this.realUrl + '}';
	}
	
}
