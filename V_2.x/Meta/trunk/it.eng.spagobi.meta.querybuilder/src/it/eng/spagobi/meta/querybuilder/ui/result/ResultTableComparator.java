package it.eng.spagobi.meta.querybuilder.ui.result;

import it.eng.spagobi.utilities.comparator.ObjectComparator;

import java.text.ParseException;


import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		comparator = new ObjectComparator(DataStoreReader.getDateFormatter(), DataStoreReader.getTimestampFormatter());
	}

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
