/**

 SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

**/
package it.eng.spagobi.meta.querybuilder.ui.shared.result;

import it.eng.spagobi.utilities.comparator.ObjectComparator;

import java.text.ParseException;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Alberto Ghedin (alberto.ghedin@eng.it)
 *
 */

public class ResultTableComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;
	private Class type;
	private ObjectComparator comparator;
	
	private static Logger logger = LoggerFactory.getLogger(ResultTableViewer.class);
	
	public ResultTableComparator() {
		this.propertyIndex = -1;
		direction = DESCENDING;
		//build a comparator
		comparator = new ObjectComparator(DataStoreReader.getDateFormatter(), DataStoreReader.getTimestampFormatter());
	}

	/**
	 * Set the column used to order the table
	 * @param column number of the column
	 * @param type data type of the data in the column
	 */
	public void setColumn(int column, Class type) {
		this.type = type;
		if (column == this.propertyIndex) {
			// Same column as last sort; toggle the direction
			direction = 1 - direction;
		} else {
			// New column; do an ascending sort
			this.propertyIndex = column;
			direction = DESCENDING;
		}
	}


	@Override
	public int compare(Viewer viewer, Object e1, Object e2) {
		if(propertyIndex<0){
			return 0;
		}
		String[] record1 = (String[]) e1;
		String[] record2 = (String[]) e2;
		try {
			return comparator.compare(record1[propertyIndex], record2[propertyIndex], type, direction);
		} catch (ParseException e) {
			logger.error("Error parsing the strings ["+record1[propertyIndex]+" , "+record2[propertyIndex]+"] in to dates");
			return record1[propertyIndex].compareTo(record2[propertyIndex]);
		}
		
		
	}
}
