/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.dataset.provider;

import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

import it.eng.spago.base.SourceBean;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.engines.geo.IGeoEngineComponent;
import it.eng.spagobi.engines.geo.dataset.DataSet;
import it.eng.spagobi.services.datasource.bo.SpagoBiDataSource;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public interface IDatasetProvider extends IGeoEngineComponent {
    
    DataSet getDataSet();    
    SourceBean getDataDetails(String filterValue);
    
    void setSelectedHierarchyName(String hierarchyName);
    String getSelectedHierarchyName();
    void setSelectedLevelName(String levelName);
    String getSelectedLevelName();
    
    Set getHierarchyNames();
    Hierarchy getHierarchy(String name);    
    Hierarchy getSelectedHierarchy();
    Hierarchy.Level getSelectedLevel();
}
