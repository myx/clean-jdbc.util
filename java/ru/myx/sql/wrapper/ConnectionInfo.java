package ru.myx.sql.wrapper;

import java.sql.Connection;

interface ConnectionInfo {
	
	/**
	 * @return long
	 */
	long connectionTimeToLive();
	
	/**
	 * @return connection
	 */
	Connection createConnection();
	
	/**
	 * @return int
	 */
	int connectionMaxLoops();
	
	/**
	 * @param poolConnectionHolder
	 */
	void reuse(final ConnectionHolder poolConnectionHolder);
	
}
