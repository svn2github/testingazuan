/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package com.tonbeller.jpivot.mondrian;

import it.eng.spagobi.jpivotaddins.crossnavigation.SpagoBICrossNavigationConfig;

import com.tonbeller.jpivot.core.ExtensionSupport;
import com.tonbeller.jpivot.olap.model.Cell;
import com.tonbeller.wcf.table.TableModel;

public class SpagoBICrossNavigation extends ExtensionSupport {

  public static final String ID = "crossNavigation";
	
  /**
   * Constructor sets ID
   */
  public SpagoBICrossNavigation() {
	  super.setId(ID);
  }

  public TableModel crossNavigation(Cell cell,
		SpagoBICrossNavigationConfig config) {
	  mondrian.olap.Cell mondrianCell = ((MondrianCell) cell).getMonCell();
	  MondrianModel model = (MondrianModel) getModel();
	  SpagoBICrossNavigationTableModel dtm = new SpagoBICrossNavigationTableModel(config, mondrianCell, model);
	  return dtm;
  }
  
  /**
   * gets the mondrian connection
   * @return
   */
  public mondrian.olap.Connection getConnection() {
    MondrianModel model = (MondrianModel) getModel();
    return model.getConnection();
  }
  
}