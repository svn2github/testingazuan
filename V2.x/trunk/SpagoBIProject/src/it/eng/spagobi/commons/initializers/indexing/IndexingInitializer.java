package it.eng.spagobi.commons.initializers.indexing;

import it.eng.spago.base.SourceBean;
import it.eng.spago.init.InitializerIFace;
import it.eng.spagobi.commons.utilities.SpagoBIUtilities;
import it.eng.spagobi.commons.utilities.indexing.LuceneIndexer;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.index.CorruptIndexException;

/**Initializer class for Metadata Indexing
 * @author franceschini
 *
 */
public class IndexingInitializer implements InitializerIFace {
	
	static private Logger logger = Logger.getLogger(IndexingInitializer.class);
	private SourceBean _config;
	
	public SourceBean getConfig() {
		return _config;
	}

	/* (non-Javadoc)
	 * @see it.eng.spago.init.InitializerIFace#init(it.eng.spago.base.SourceBean)
	 */
	public void init(SourceBean config) {
		logger.debug("IN");
		_config = config;
		//at server startup calls create index
		
		List nodes = _config.getAttributeAsList("INDEX");
		Iterator it = nodes.iterator();
		while (it.hasNext()) {
		    SourceBean node = (SourceBean) it.next();
		    String jndiResourcePath = (String) node.getAttribute("jndiResourcePath");
		    String location = SpagoBIUtilities.readJndiResource(jndiResourcePath);
		    String name = (String) node.getAttribute("name");
		    //first checks if iindex exists
		    File idxFile = new File(location+name);
		    if(!idxFile.exists()){
		    	logger.debug("Creating index");
			    LuceneIndexer indexer = new LuceneIndexer();
			    try {
					indexer.createIndex(idxFile);
				} catch (CorruptIndexException e) {
					logger.error("Index corrupted "+e.getMessage());
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
		    }else{
		    	logger.debug("Index already exists");
		    }

		    
		}
		logger.debug("OUT");
	}

}
