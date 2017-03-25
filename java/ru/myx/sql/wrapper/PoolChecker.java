/*
 * Created on 27.09.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.sql.wrapper;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

/**
 * @author barachta
 * 
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
final class PoolChecker extends Thread {
	private static final long	CHECK_INTERVAL		= 1000L;
	
	private static final long	CHECK_LOOP_INTERVAL	= 10000L;
	
	static final List<Runnable>	TO_CHECK			= new ArrayList<>();
	static {
		new PoolChecker().start();
	}
	
	private PoolChecker() {
		super( null, null, "Connection checker thread (mwm.sql.driver)" );
		this.setDaemon( true );
	}
	
	@Override
	public final void run() {
		for (; !Thread.interrupted();) {
			try {
				for (final Runnable current : PoolChecker.TO_CHECK) {
					current.run();
					try {
						synchronized (this) {
							this.wait( PoolChecker.CHECK_INTERVAL );
						}
					} catch (final InterruptedException e) {
						return;
					}
				}
				try {
					synchronized (this) {
						this.wait( PoolChecker.CHECK_LOOP_INTERVAL );
					}
				} catch (final InterruptedException e) {
					return;
				}
			} catch (final ConcurrentModificationException t) {
				// ignore
			} catch (final Throwable t) {
				t.printStackTrace();
			}
		}
	}
	
	@Override
	public final String toString() {
		return "POOL-CHECKER";
	}
}
