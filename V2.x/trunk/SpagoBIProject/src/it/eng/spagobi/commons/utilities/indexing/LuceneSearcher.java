package it.eng.spagobi.commons.utilities.indexing;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneSearcher {
	
	static private Logger logger = Logger.getLogger(LuceneSearcher.class);

	public static ScoreDoc[] searchIndex(IndexSearcher searcher, String queryString, String index) throws IOException, ParseException {
		logger.debug("IN");
		String field = "contents";

		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		Query query = new QueryParser(Version.LUCENE_CURRENT, field, analyzer).parse(queryString);

		logger.debug("Searching for: " + query.toString(field));
		int hitsPerPage = 10;

	    // Collect enough docs to show 5 pages
	    TopScoreDocCollector collector = TopScoreDocCollector.create( 5 * hitsPerPage, false);
	    searcher.search(query, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
	    int numTotalHits = collector.getTotalHits();
	    logger.info(numTotalHits + " total matching documents");

	    //loop over results
/*	    for (int i=0; i<hits.length; i++){
	    	ScoreDoc hit = hits[i];
	    	logger.debug("Doc id:: "+hit.doc);
	    	logger.debug("Doc score:: "+hit.score);
	    	Document doc = searcher.doc(hit.doc);
	        String uid = doc.get("uid");
	        if (uid != null) {
	          logger.debug("Doc uid:: "+uid);
	        }
	    }*/
	    logger.debug("OUT");
	    return hits;
	    

	}
	public static void main(String[] argv) {
		
		String index = "C:\\Programmi\\resources\\idx";
		String queryString ="federa";//or install for test
		IndexReader reader;
		try {
			reader = IndexReader.open(FSDirectory.open(new File(index)), true);
			// read-only=true
			boolean isIndexCurrent = reader.isCurrent();
			//if !isC
			IndexSearcher searcher = new IndexSearcher(reader);
			searchIndex(searcher, queryString, index);
			
			// searcher can only be closed when there
			// is no need to access the documents any more.
			searcher.close();
		} catch (CorruptIndexException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // only searching, so
		catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

}
