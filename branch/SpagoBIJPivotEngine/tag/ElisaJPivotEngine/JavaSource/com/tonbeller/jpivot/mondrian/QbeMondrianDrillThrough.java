/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package com.tonbeller.jpivot.mondrian;

import com.tonbeller.jpivot.core.ExtensionSupport;

public class QbeMondrianDrillThrough extends ExtensionSupport {

  public static final String ID = "qbeDrillThrough";
	
  private boolean extendedContext = true;

  /**
   * Constructor sets ID
   */
  public QbeMondrianDrillThrough() {
	  super.setId(ID);
  }

  /**
   * gets the mondrian connection
   * @return
   */
  public mondrian.olap.Connection getConnection() {
    MondrianModel model = (MondrianModel) getModel();
    return model.getConnection();
  }

  public boolean isExtendedContext() {
    return extendedContext;
  }

  public void setExtendedContext(boolean extendedContext) {
    this.extendedContext = extendedContext;
  }

}
