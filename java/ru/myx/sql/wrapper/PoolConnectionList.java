/*
 * Created on 25.04.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.sql.wrapper;

final class PoolConnectionList {
	private final PoolConnectionInfo	ci;
	
	private final ConnectionHolder[]	list;
	
	private int							head	= 0;
	
	private int							tail	= 0;
	
	private int							size	= 0;
	
	private final int					caps;
	
	private final int					mask;
	
	PoolConnectionList(final int capacity, final PoolConnectionInfo ci) {
		this.caps = capacity;
		this.mask = capacity - 1;
		this.list = new ConnectionHolder[capacity];
		this.ci = ci;
	}
	
	final void check() {
		synchronized (this) {
			for (int i = this.size - 1, head = this.head; i >= 0; --i) {
				this.list[(--head) & this.mask].checkEnsure();
			}
			if (this.size >= this.caps) {
				return;
			}
		}
		this.reuse( new ConnectionHolder( this.ci ) );
	}
	
	final ConnectionHolder nextConnectionHolder() {
		synchronized (this) {
			if (this.size > 0) {
				this.size--;
				final int index = (--this.head) & this.mask;
				try {
					return this.list[index];
				} finally {
					this.list[index] = null;
				}
			}
		}
		return new ConnectionHolder( this.ci );
	}
	
	final boolean reuse(final ConnectionHolder hldr) {
		synchronized (this) {
			if (this.size < this.caps) {
				this.size++;
				hldr.loops--;
				this.list[(--this.tail) & this.mask] = hldr;
				return true;
			}
		}
		return false;
	}
}
