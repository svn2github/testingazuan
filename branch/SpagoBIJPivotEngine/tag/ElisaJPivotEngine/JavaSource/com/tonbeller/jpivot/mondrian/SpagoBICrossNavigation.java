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

import org.apache.log4j.Logger;

import it.eng.spagobi.jpivotaddins.crossnavigation.SpagoBICrossNavigationConfig;

import com.tonbeller.jpivot.core.ExtensionSupport;
import com.tonbeller.jpivot.core.Model;
import com.tonbeller.jpivot.olap.model.Cell;
import com.tonbeller.wcf.table.TableModel;

/**
 * This class is an extension to JPivot MondrianModel: it must be declared as
 * <code>&lt;extension id="crossNavigation" class="com.tonbeller.jpivot.mondrian.SpagoBICrossNavigation"/&gt;</code>
 * in file com.tonbeller.jpivot.mondrian.config.xml to take effect.
 * When user clicks on a cell, the cross navigation table is built according to the TableModel returned by 
 * <code>crossNavigation(Cell cell, SpagoBICrossNavigationConfig config)</code> method.
 * 
 * @author Zerbetto Davide (davide.zerbetto@eng.it)
 *
 */
public class SpagoBICrossNavigation extends ExtensionSupport {

  private static transient Logger logger = Logger.getLogger(SpagoBICrossNavigationConfig.class);
	
  public static final String ID = "crossNavigation";
	
  /**
   * Constructor sets ID
   */
  public SpagoBICrossNavigation() {
	  super.setId(ID);
  }

  public TableModel crossNavigation(Cell cell,
		SpagoBICrossNavigationConfig config) {
	  logger.debug("IN");
	  Model model = getModel();
	  if (!(cell instanceof MondrianCell) || !(model instanceof MondrianModel)) {
		  logger.error("Cross navigation functionality is available only for MondrianModel, while current model is " + model.getClass().getName());
		  throw new RuntimeException("Cross navigation functionality is available only for MondrianModel, while current model is " + model.getClass().getName());
	  }
	  mondrian.olap.Cell mondrianCell = ((MondrianCell) cell).getMonCell();
	  MondrianModel mondrianModel = (MondrianModel) model;
	  SpagoBICrossNavigationTableModel dtm = new SpagoBICrossNavigationTableModel(config, mondrianCell, mondrianModel);
	  logger.debug("OUT");
	  return dtm;
  }
  
}