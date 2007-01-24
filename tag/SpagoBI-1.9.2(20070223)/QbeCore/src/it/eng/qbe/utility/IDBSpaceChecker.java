package it.eng.qbe.utility;

import java.sql.Connection;



	public interface IDBSpaceChecker {
	
		/**
		 * 
		 * @param aConnection - The sql connection provide by qbe, pay attention this class must keep the connection open
		 * @return a numeber representing the available space for the given connection in db
		 */
		public int getPercentageOfFreeSpace(Connection aConnection);
	}
