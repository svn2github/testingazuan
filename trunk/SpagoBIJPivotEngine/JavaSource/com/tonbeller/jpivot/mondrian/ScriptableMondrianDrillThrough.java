/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package com.tonbeller.jpivot.mondrian;

import mondrian.olap.Util.PropertyList;
import mondrian.rolap.RolapConnectionProperties;

import com.tonbeller.jpivot.core.ExtensionSupport;
import com.tonbeller.jpivot.olap.model.Cell;
import com.tonbeller.jpivot.olap.navi.DrillThrough;
import com.tonbeller.wcf.table.TableModel;
/**
 * @author Robin Bagot
 *
 * Implementation of the DrillExpand Extension for Mondrian Data Source.
*/
public class ScriptableMondrianDrillThrough extends ExtensionSupport implements DrillThrough {

  private boolean extendedContext = true;

  /**
   * Constructor sets ID
   */
  public ScriptableMondrianDrillThrough() {
    super.setId(DrillThrough.ID);
  }

  /**
   * drill through is possible if <code>member</code> is not calculated
   */
  public boolean canDrillThrough(Cell cell) {
    return ((MondrianCell) cell).getMonCell().canDrillThrough();
    //String sql = ((MondrianCell) cell).getMonCell().getDrillThroughSQL(extendedContext);
    //return sql != null;
  }

  /**
   * does a drill through, retrieves data that makes up the selected Cell
   */
  public TableModel drillThrough(Cell cell) {
    String sql = ((MondrianCell) cell).getMonCell().getDrillThroughSQL(extendedContext);
    if (sql == null) {
      throw new NullPointerException("DrillThroughSQL returned null");
    }
    ScriptableMondrianDrillThroughTableModel dtm = new ScriptableMondrianDrillThroughTableModel();
    dtm.setSql(sql);
    String connectString = getConnection().getConnectString();
    PropertyList connectInfo = mondrian.olap.Util.parseConnectString(connectString);
    String jdbcUrl = connectInfo.get(RolapConnectionProperties.Jdbc);
    dtm.setJdbcUrl(jdbcUrl);
    String jdbcUser = connectInfo.get(RolapConnectionProperties.JdbcUser);
    dtm.setJdbcUser(jdbcUser);
    String jdbcPassword = connectInfo.get(RolapConnectionProperties.JdbcPassword);
    dtm.setJdbcPassword(jdbcPassword);
    String dataSourceName = connectInfo.get(RolapConnectionProperties.DataSource);
    dtm.setDataSourceName(dataSourceName);
    String catalog = connectInfo.get(RolapConnectionProperties.Catalog);
    String catalogExtension = catalog.replaceFirst(".*/", "").replaceFirst("\\.xml$", ".ext.xml");
    dtm.setCatalogExtension(catalogExtension);
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

  public boolean isExtendedContext() {
    return extendedContext;
  }

  public void setExtendedContext(boolean extendedContext) {
    this.extendedContext = extendedContext;
  }

}
