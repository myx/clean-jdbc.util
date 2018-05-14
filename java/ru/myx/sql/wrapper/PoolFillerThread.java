/*
 * Created on 12.04.2006
 */
package ru.myx.sql.wrapper;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;

final class PoolFillerThread extends Thread {
	
	private final int connections;
	
	private final Driver driver;
	
	private final Properties info;
	
	private final String url;
	
	PoolFillerThread(final Driver driver, final Properties info, final String url, final int connections) {
		
		super("filler thread, url=" + url);
		this.driver = driver;
		this.info = info;
		this.url = url;
		this.connections = connections;
		this.setPriority(Thread.NORM_PRIORITY - 1);
		this.setDaemon(true);
	}
	
	@Override
	public final void run() {
		
		try {
			for (int i = this.connections; i > 0; --i) {
				try (final Connection conn = this.driver.connect(this.url, this.info)) {
					Thread.sleep(10L);
				}
				Thread.sleep(50L);
			}
		} catch (final InterruptedException e) {
			return;
		} catch (final SQLException e) {
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public final String toString() {
		
		return "filler thread (url=" + this.url + ")";
	}
}
