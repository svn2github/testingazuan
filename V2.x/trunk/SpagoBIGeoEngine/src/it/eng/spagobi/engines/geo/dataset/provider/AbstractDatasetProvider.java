/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.spagobi.engines.geo.dataset.provider;

import it.eng.spago.base.SourceBean;
import it.eng.spagobi.engines.geo.AbstractGeoEngineComponent;
import it.eng.spagobi.engines.geo.commons.excpetion.GeoEngineException;
import it.eng.spagobi.engines.geo.dataset.DataSet;
import it.eng.spagobi.engines.geo.dataset.DataSetMetaData;
import it.eng.spagobi.engines.geo.dataset.provider.configurator.AbstractDatasetProviderConfigurator;

import java.util.Map;
import java.util.Set;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class AbstractDatasetProvider extends AbstractGeoEngineComponent implements IDatasetProvider {

	private DataSetMetaData metaData;	
	private Map hierarchies;	
	private String selectedHierarchyName;
	private String selectedLevelName;
	
    public AbstractDatasetProvider() {
        super();
    }   
    
    public void init(Object conf) throws GeoEngineException {
		super.init(conf);
		AbstractDatasetProviderConfigurator.configure( this, getConf() );
	}
  
    public DataSet getDataSet() {
        return null;
    }

	public SourceBean getDataDetails(String filterValue) {
		return null;
	}

	public Set getHierarchyNames() {
		if(hierarchies != null) {
			return hierarchies.keySet();
		}
		return null;
	}

	public Hierarchy getHierarchy(String name) {
		if(hierarchies != null) {
			return (Hierarchy)hierarchies.get( name );
		}
		return null;
	}

	public Hierarchy getSelectedHierarchy() {
		if(hierarchies != null) {
			return (Hierarchy)hierarchies.get( selectedHierarchyName );
		}
		return null;
	}

	public Hierarchy.Level getSelectedLevel() {
		Hierarchy selectedHierarchy = getSelectedHierarchy();
		if(selectedHierarchy != null) {
			return selectedHierarchy.getLevel( selectedLevelName );
		}
		return null;
	}

	protected DataSetMetaData getMetaData() {
		return metaData;
	}

	public void setMetaData(DataSetMetaData metaData) {
		this.metaData = metaData;
	}

	public void setHierarchies(Map hierarchies) {
		this.hierarchies = hierarchies;
		
	}

	public String getSelectedHierarchyName() {
		return selectedHierarchyName;
	}

	public void setSelectedHierarchyName(String selectedHierarchyName) {
		this.selectedHierarchyName = selectedHierarchyName;
	}

	public String getSelectedLevelName() {
		return selectedLevelName;
	}

	public void setSelectedLevelName(String selectedLevelName) {
		this.selectedLevelName = selectedLevelName;
	}

}
