package it.eng.spagobi.commons.utilities.indexing;

import it.eng.spago.base.SourceBean;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spago.error.EMFUserError;
import it.eng.spagobi.analiticalmodel.document.bo.BIObject;
import it.eng.spagobi.analiticalmodel.document.bo.ObjMetaDataAndContent;
import it.eng.spagobi.commons.bo.Domain;
import it.eng.spagobi.commons.dao.DAOFactory;
import it.eng.spagobi.commons.dao.IBinContentDAO;
import it.eng.spagobi.commons.utilities.JTidyHTMLHandler;
import it.eng.spagobi.commons.utilities.SpagoBIUtilities;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetacontent;
import it.eng.spagobi.tools.objmetadata.bo.ObjMetadata;
import it.eng.spagobi.tools.objmetadata.dao.IObjMetacontentDAO;
import it.eng.spagobi.tools.objmetadata.dao.IObjMetadataDAO;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import net.sf.cglib.transform.impl.AddDelegateTransformer;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.TermEnum;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

/**Indexing class.
 * @author franceschini
 *
 */
public class LuceneIndexer {

	private static boolean deleting = false; // true during deletion pass
	private static IndexReader reader; // existing index
	private static IndexWriter writer; // new index being built
	private static TermEnum uidIter; // document id iterator

	private static final String LONG_TEXT = "LONG_TEXT";// html
	private static final String SHORT_TEXT = "SHORT_TEXT";// simple text
	
	

	static private Logger logger = Logger.getLogger(LuceneIndexer.class);
	
	
	/**Method to add biObj input to lucene index (no metadata included) 
	 * @param biObj
	 */
	public static void addBiobjToIndex(BIObject biObj){
		logger.debug("IN");
		try {
			String indexBasePath = "";
			SourceBean jndiBean =(SourceBean)ConfigSingleton.getInstance().getAttribute("SPAGOBI.RESOURCE_PATH_JNDI_NAME");
			if (jndiBean != null) {
				String jndi = jndiBean.getCharacters();
				indexBasePath = SpagoBIUtilities.readJndiResource(jndi);
			}
			String index = indexBasePath+"\\idx";
			Date start = new Date();
			
			writer = new IndexWriter(FSDirectory.open(new File(index)),
					new StandardAnalyzer(Version.LUCENE_CURRENT), false,
					new IndexWriter.MaxFieldLength(1000000));
			Document doc = new Document();
			String uid = createUidDocument(null, String.valueOf(biObj.getId().intValue()));
			doc.add(new Field(IndexingConstants.UID, uid , Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			doc.add(new Field(IndexingConstants.BIOBJ_ID, String.valueOf(biObj.getId().intValue()), Field.Store.YES,
					Field.Index.NOT_ANALYZED));
			doc.add(new Field(IndexingConstants.BIOBJ_NAME, biObj.getName(),
					Field.Store.NO, Field.Index.ANALYZED));
			doc.add(new Field(IndexingConstants.BIOBJ_DESCR, biObj.getDescription(),
					Field.Store.NO, Field.Index.ANALYZED));
			doc.add(new Field(IndexingConstants.BIOBJ_LABEL, biObj.getLabel(),
					Field.Store.NO, Field.Index.ANALYZED));
			
			writer.addDocument(doc);
			writer.optimize();
			writer.close();

			Date end = new Date();

			logger.info("Indexing time:: " + (end.getTime() - start.getTime())
					+ " milliseconds");
			logger.debug("OUT");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
	}
	/**Method to update a lucene document based on biObj input parameter
	 * @param biObj
	 */
	public static void updateBiobjInIndex(BIObject biObj){
		logger.debug("IN");
		try {
			String indexBasePath = "";
			SourceBean jndiBean =(SourceBean)ConfigSingleton.getInstance().getAttribute("SPAGOBI.RESOURCE_PATH_JNDI_NAME");
			if (jndiBean != null) {
				String jndi = jndiBean.getCharacters();
				indexBasePath = SpagoBIUtilities.readJndiResource(jndi);
			}
			String index = indexBasePath+"\\idx";
			Date start = new Date();
			
			writer = new IndexWriter(FSDirectory.open(new File(index)),
					new StandardAnalyzer(Version.LUCENE_CURRENT), false,
					new IndexWriter.MaxFieldLength(1000000));
			
			ArrayList<String> uids = new ArrayList<String>();
			//checks whether biobj has metadata content
			List metadata = DAOFactory.getObjMetadataDAO().loadAllObjMetadata();
			if (metadata != null && !metadata.isEmpty()) {
				ByteArrayInputStream bais = null;
				Iterator it = metadata.iterator();
				while (it.hasNext()) {
					ObjMetadata objMetadata = (ObjMetadata) it.next();
					ObjMetacontent objMetacontent = (ObjMetacontent) DAOFactory.getObjMetacontentDAO().loadObjMetacontent(objMetadata.getObjMetaId(), biObj.getId(), null);
					if(objMetacontent != null){
						Integer binId = objMetacontent.getBinaryContentId();
						String uid = createUidDocument(String.valueOf(binId.intValue()), String.valueOf(biObj.getId().intValue()));
						Integer idDomain = objMetadata.getDataType();
						Domain domain = DAOFactory.getDomainDAO().loadDomainById(idDomain);
						String binIdString = String.valueOf(binId.intValue());
						
						byte[] content = objMetacontent.getContent();
						String htmlContent = null;
						if (domain.getValueCd().equalsIgnoreCase(LONG_TEXT)) {
							bais = new ByteArrayInputStream(content);
							JTidyHTMLHandler htmlHandler = new JTidyHTMLHandler();
							htmlContent = htmlHandler.getContent(bais);
							bais.close();
						}
						uids.add(uid);
						
						//delete document 
						writer.deleteDocuments(new Term(IndexingConstants.UID, uid));
						//re-add document to index
						Document doc = new Document();
						addBiobjFieldsToDocument(doc, biObj.getId());						
						addFieldsToDocument(doc, String.valueOf(binId.intValue()), biObj.getId(),objMetadata.getName(),domain,htmlContent, content);
						writer.addDocument(doc);
						
					}
				}
			}
			if(uids.isEmpty()){
				//document with no metadata
				String uid = String.valueOf(biObj.getId().intValue());
				writer.deleteDocuments(new Term(IndexingConstants.UID, uid));
				Document doc = new Document();
				doc.add(new Field(IndexingConstants.UID, uid , Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field(IndexingConstants.BIOBJ_ID, String.valueOf(biObj.getId().intValue()), Field.Store.YES,
						Field.Index.NOT_ANALYZED));
				doc.add(new Field(IndexingConstants.BIOBJ_NAME, biObj.getName(),
						Field.Store.NO, Field.Index.ANALYZED));
				doc.add(new Field(IndexingConstants.BIOBJ_DESCR, biObj.getDescription(),
						Field.Store.NO, Field.Index.ANALYZED));
				doc.add(new Field(IndexingConstants.BIOBJ_LABEL, biObj.getLabel(),
						Field.Store.NO, Field.Index.ANALYZED));
				writer.addDocument(doc);
			}

			writer.optimize();
			writer.close();

			Date end = new Date();

			logger.info("Indexing time:: " + (end.getTime() - start.getTime())
					+ " milliseconds");
			logger.debug("OUT");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
	}
	/**Method called to create or increment Lucene index created over metadata binary contents.
	 * @param index  index file
	 * @param create indicating whether index is to be created or updated
	 */
	public void createIndex(File index, boolean create) {
		logger.debug("IN");
		try {
			Date start = new Date();
		    if (!create) { // delete stale docs
				deleting = true;
				indexDocs(index, create);
			}
			writer = new IndexWriter(FSDirectory.open(index),
					new StandardAnalyzer(Version.LUCENE_CURRENT), create,
					new IndexWriter.MaxFieldLength(1000000));
			indexDocs(index, create); // add new docs

			writer.optimize();
			writer.close();

			Date end = new Date();

			logger.info("Indexing time:: " + (end.getTime() - start.getTime())
					+ " milliseconds");
			logger.debug("OUT");

		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	/**Method walking directory hierarchy in uid order, while keeping uid iterator from
	 * existing index in sync. Mismatches indicate one of: (a) old documents to
	 * be deleted; (b) unchanged documents, to be left alone; or (c) new
	 * documents, to be indexed.
	 * @param index index file
	 * @param create create or update existing index
	 * @throws Exception
	 */
	private static void indexDocs(File index, boolean create) throws Exception {
		logger.debug("IN");
		if (!create) { // incrementally update

			reader = IndexReader.open(FSDirectory.open(index), false); // open
			// existing
			// index
			uidIter = reader.terms(new Term(IndexingConstants.UID, "")); // init uid iterator

			indexDocs();

			if (deleting) { // delete rest of stale docs

				while (uidIter.term() != null
						&& uidIter.term().field().equals(IndexingConstants.UID)) {
					logger.info("deleting "
							+ uidIter.term().text());
					reader.deleteDocuments(uidIter.term());
					uidIter.next();
				}
				deleting = false;
			 

			}

			uidIter.close(); // close uid iterator
			reader.close(); // close existing index

		} else {
			// don't have exisiting
			indexDocs();
		}
		logger.debug("OUT");
	}

	/**Method which indexes metadata binary contents. The logic is: if uid exists do not add to Document 
	 * otherwise add it. Binary contents can be either html or simple text.
	 * @throws Exception
	 */
	private static void indexDocs() throws Exception {
		logger.debug("IN");
		ByteArrayInputStream bais = null;
		try{			
			// call dao to get binary contents to index
			IObjMetadataDAO metadataDAO = DAOFactory.getObjMetadataDAO();
			List<ObjMetadata> results = metadataDAO.loadAllObjMetadata();
			// loop over list of metadata to index its content
			if (results != null) {
	
				for (int i = 0; i < results.size(); i++) {
					// look for binary content mimetype
					ObjMetadata metadata = results.get(i);
					Integer idDomain = metadata.getDataType();
					Integer metaId = metadata.getObjMetaId();
					//String metaLabel = metadata.getLabel();
					String metaName = metadata.getName();
					Domain domain = DAOFactory.getDomainDAO().loadDomainById(idDomain);
	
					IObjMetacontentDAO metacontentDAO = DAOFactory.getObjMetacontentDAO();
					List<ObjMetacontent> metacontents = metacontentDAO.loadObjMetacontentByObjMetaId(metaId);
					if (metacontents != null) {
						for (int j = 0; j < metacontents.size(); j++) {
							ObjMetacontent metacontent = metacontents.get(j);
							Integer binId = metacontent.getBinaryContentId();
							Integer biobjId = metacontent.getBiobjId();
							String binIdString = String.valueOf(binId.intValue());
	
							byte[] content = metacontent.getContent();
							String htmlContent = null;
							if (domain.getValueCd().equalsIgnoreCase(LONG_TEXT)) {
								bais = new ByteArrayInputStream(content);
								JTidyHTMLHandler htmlHandler = new JTidyHTMLHandler();
								htmlContent = htmlHandler.getContent(bais);
								bais.close();
							}
							if (uidIter != null) {
								while (uidIter.term() != null
										&& uidIter.term().field().equals(IndexingConstants.UID)
										&& uidIter.term().text().compareTo(binIdString) < 0) {
									if (deleting) { // delete stale docs
										logger.info("deleting stale document "+ uidIter.term().text());
										reader.deleteDocuments(uidIter.term());
									}
									uidIter.next();
								}
								if (uidIter.term() != null
										&& uidIter.term().field().equals(IndexingConstants.UID)
										&& uidIter.term().text().compareTo(binIdString) == 0) {
									uidIter.next(); // keep matching docs
								} else if (!deleting) { // add new docs
									Document doc = new Document();
									addFieldsToDocument(doc, binIdString, biobjId, metaName, domain, htmlContent, content);									
									writer.addDocument(doc);
								}
							} else { // creating a new index
								Document doc = new Document();
								addFieldsToDocument(doc, binIdString, biobjId, metaName, domain, htmlContent, content);								
								writer.addDocument(doc);
							}
							
						}	
					}
				}

			}
		}catch(Exception e){
			logger.error(e.getMessage());
		}finally{
			if(bais != null){
				bais.close();
			}
			logger.debug("OUT");
		}
			
	}
	private static String createUidDocument(String binId, String biobjId){
		String uid=biobjId;
		if(binId != null){
			uid+= "_"+binId;
		}
		
		return uid;
	}
	
	private static void addFieldsToDocument(Document doc, String binId, Integer biobjId, String metaName,Domain domain, String htmlContent, byte[] content) throws UnsupportedEncodingException{
		String uid = createUidDocument(binId, String.valueOf(biobjId.intValue()));
		doc.add(new Field(IndexingConstants.UID, uid , Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexingConstants.BIOBJ_ID, String.valueOf(biobjId.intValue()), Field.Store.YES,
				Field.Index.NOT_ANALYZED));
		doc.add(new Field(IndexingConstants.METADATA, metaName,
				Field.Store.YES,
				Field.Index.ANALYZED));
		if (domain.getValueCd().equalsIgnoreCase(LONG_TEXT)) { // index
																// html
																// binary
																// content
			doc.add(new Field(IndexingConstants.CONTENTS, htmlContent,
					Field.Store.NO, Field.Index.ANALYZED));
			logger.info("adding html binary content " + doc.get(IndexingConstants.UID));
		} else if (domain.getValueCd().equalsIgnoreCase(
				SHORT_TEXT)) {// index simple text binary
								// content
			doc.add(new Field(IndexingConstants.CONTENTS, new String(content, "UTF-8"),
					Field.Store.NO, Field.Index.ANALYZED));
			logger.info("adding simple text binary content " + doc.get(IndexingConstants.UID));
		}
		addBiobjFieldsToDocument(doc, biobjId);
	}
	
	private static void addBiobjFieldsToDocument(Document doc, Integer biObjectID){
		try {
			BIObject biObj = DAOFactory.getBIObjectDAO().loadBIObjectById(biObjectID);
			doc.add(new Field(IndexingConstants.BIOBJ_NAME, biObj.getName(),
					Field.Store.NO, Field.Index.ANALYZED));
			doc.add(new Field(IndexingConstants.BIOBJ_DESCR, biObj.getDescription(),
					Field.Store.NO, Field.Index.ANALYZED));
			doc.add(new Field(IndexingConstants.BIOBJ_LABEL, biObj.getLabel(),
					Field.Store.NO, Field.Index.ANALYZED));
			
		} catch (EMFUserError e) {
			logger.error(e.getMessage());
		}
	}

}
