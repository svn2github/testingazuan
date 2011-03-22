package it.eng.spagobi.meta.querybuilder.ui.result;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;

public class ResultTableComparator extends ViewerComparator {
	private int propertyIndex;
	private static final int DESCENDING = 1;
	private int direction = DESCENDING;

	public ResultTableComparator() {
		this.propertyIndex = 0;
		direction = DESCENDING;
	}

	public void setColumn(int column) {
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
		String[] record1 = (String[]) e1;
		String[] record2 = (String[]) e2;
		int rc;
		if(record1[propertyIndex]==null){
			rc = 100;
		}else if (record2[propertyIndex]==null){
			rc = -100;
		}else{
			rc = record1[propertyIndex].compareTo(record2[propertyIndex]);
			// If descending order, flip the direction
			if (direction == DESCENDING) {
				rc = -rc;
			}
		}
		
		return rc;
	}

}
