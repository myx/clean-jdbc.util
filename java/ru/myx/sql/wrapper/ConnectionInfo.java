package ru.myx.sql.wrapper;

import java.sql.Connection;

interface ConnectionInfo {
	
	/** @return int */
	int connectionMaxLoops();
	
	/** @return long */
	long connectionTimeToLive();
	
	/** @return connection */
	Connection createConnection();
	
	/** @param poolConnectionHolder
	 */
	void reuse(final ConnectionHolder poolConnectionHolder);
	
}
