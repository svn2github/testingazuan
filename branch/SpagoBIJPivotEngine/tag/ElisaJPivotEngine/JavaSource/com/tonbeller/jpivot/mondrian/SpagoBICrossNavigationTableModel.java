/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package com.tonbeller.jpivot.mondrian;

import it.eng.spagobi.jpivotaddins.crossnavigation.SpagoBICrossNavigationConfig;
import mondrian.olap.Cell;

import com.tonbeller.wcf.table.AbstractTableModel;
import com.tonbeller.wcf.table.DefaultTableRow;
import com.tonbeller.wcf.table.TableRow;

public class SpagoBICrossNavigationTableModel extends AbstractTableModel {

	private SpagoBICrossNavigationConfig config = null;
	private Cell cell = null;
	private MondrianModel model = null;
	
	public SpagoBICrossNavigationTableModel(SpagoBICrossNavigationConfig config, Cell cell, MondrianModel model) {
		this.config = config;
		this.cell = cell;
		this.model = model;
	}
	
	public int getColumnCount() {
		return 2;
	}

	public String getColumnTitle(int columnIndex) {
		String columnTitle = null;
		switch (columnIndex) {
			case 0: 
				columnTitle = "Document label";
				break;
			case 1:
				columnTitle = "Customized view label";
				break;
		}
		return columnTitle;
	}

	public TableRow getRow(int rowIndex) {
		Object[] row = config.getChoice(rowIndex, cell, model);
		DefaultTableRow tableRow = new DefaultTableRow(row);
		return tableRow;
	}

	public int getRowCount() {
		return config.getChoicesNumber();
	}

	public String getTitle() {
		return "Cross navigation";
	}

}
