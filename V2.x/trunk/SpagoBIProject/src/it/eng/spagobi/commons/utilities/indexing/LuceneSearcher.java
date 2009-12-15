package it.eng.spagobi.commons.utilities.indexing;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.MultiFieldQueryParser;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneSearcher {

	static private Logger logger = Logger.getLogger(LuceneSearcher.class);

	public static ScoreDoc[] searchIndex(IndexSearcher searcher,
			String queryString, String index, String[] fields, String metaDataToSearch)
			throws IOException, ParseException {
		logger.debug("IN");
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		BooleanQuery andQuery = new BooleanQuery();
		if(metaDataToSearch != null){
			//search for query string on metadata name field and content
			//where metadata name = metaDataToSearch
			Query queryMetadata = new TermQuery(new Term(IndexingConstants.METADATA, metaDataToSearch));
			andQuery.add(queryMetadata, BooleanClause.Occur.MUST);
		}
		Query query = new MultiFieldQueryParser(Version.LUCENE_CURRENT, fields,
				analyzer).parse(queryString);
		andQuery.add(query, BooleanClause.Occur.MUST);
		logger.debug("Searching for: " + andQuery.toString());
		int hitsPerPage = 50;

		// Collect enough docs to show 5 pages
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				5 * hitsPerPage, false);
		searcher.search(andQuery, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		int numTotalHits = collector.getTotalHits();
		logger.info(numTotalHits + " total matching documents");

		logger.debug("OUT");
		return hits;

	}

	public static ScoreDoc[] searchIndexFuzzy(IndexSearcher searcher,
			String queryString, String index, String[] fields, String metaDataToSearch)
			throws IOException, ParseException {
		logger.debug("IN");
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
		BooleanQuery orQuery = new BooleanQuery();
		for(int i=0; i< fields.length;i++){
			Query query = new FuzzyQuery(new Term(fields[i], queryString));
			orQuery.add(query, BooleanClause.Occur.SHOULD);
		}
		
		logger.debug("Searching for: " + orQuery.toString());
		int hitsPerPage = 50;

		// Collect enough docs to show 5 pages
		TopScoreDocCollector collector = TopScoreDocCollector.create(
				5 * hitsPerPage, false);
		searcher.search(orQuery, collector);
		ScoreDoc[] hits = collector.topDocs().scoreDocs;

		int numTotalHits = collector.getTotalHits();
		logger.info(numTotalHits + " total matching documents");

		logger.debug("OUT");
		return hits;

	}


}
