/*
 * ====================================================================
 * This software is subject to the terms of the Common Public License
 * Agreement, available at the following URL:
 *   http://www.opensource.org/licenses/cpl.html .
 * Copyright (C) 2003-2004 TONBELLER AG.
 * All Rights Reserved.
 * You must accept the terms of that agreement to use this software.
 * ====================================================================
 *
 *
 */
package com.tonbeller.jpivot.mondrian;

import it.eng.spagobi.jpivotaddins.crossnavigation.SpagoBICrossNavigationConfig;
import mondrian.olap.Cell;

import com.tonbeller.wcf.table.AbstractTableModel;
import com.tonbeller.wcf.table.DefaultTableRow;
import com.tonbeller.wcf.table.TableRow;

/**
 * This class is the cross navigation table model, i.e. the model of the table displaying available choices for cross navigation
 * from current OLAP document to another SpagoBI document.
 * 
 * @author Zerbetto Davide (davide.zerbetto@eng.it)
 *
 */
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
		return 3;
	}

	public String getColumnTitle(int columnIndex) {
		String columnTitle = null;
		switch (columnIndex) {
			case 0: 
				columnTitle = "Document";
				break;
			case 1:
				columnTitle = "Customized view";
				break;
			case 2:
				columnTitle = "Description";
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
