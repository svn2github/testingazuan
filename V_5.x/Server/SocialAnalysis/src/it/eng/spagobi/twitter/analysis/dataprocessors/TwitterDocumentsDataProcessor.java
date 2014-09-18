/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005-2010 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

 **/

package it.eng.spagobi.twitter.analysis.dataprocessors;

import it.eng.spagobi.twitter.analysis.cache.ITwitterCache;
import it.eng.spagobi.twitter.analysis.cache.TwitterCacheFactory;
import it.eng.spagobi.twitter.analysis.pojos.TwitterDocumentPojo;
import it.eng.spagobi.utilities.assertion.Assert;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */
public class TwitterDocumentsDataProcessor {

	private final ITwitterCache twitterCache = new TwitterCacheFactory().getCache("mysql");

	private static final Logger logger = Logger.getLogger(TwitterDocumentsDataProcessor.class);

	private final String HOST = "http://localhost:";
	private final String PORT = "8080";
	private final String CALL = "/SpagoBI/servlet/AdapterHTTP?ACTION_NAME=EXECUTE_DOCUMENT_ACTION&NEW_SESSION=TRUE&OBJECT_LABEL=";

	// fields to avoid logic into JSP
	private List<String> labels = new ArrayList<String>();
	private List<TwitterDocumentPojo> documents = new ArrayList<TwitterDocumentPojo>();

	public TwitterDocumentsDataProcessor() {

	}

	/**
	 * This method retrieves the SpagoBI docs for a search
	 *
	 * @param searchIDStr
	 * @return
	 */
	public void initializeTwitterDocumentsDataProcessor(String searchID) {

		logger.debug("Method initializeTwitterDocumentsDataProcessor(): Start");

		Assert.assertNotNull(searchID, "Impossibile initialize TwitterDocumentsDataProcessor without a correct search ID");

		String sqlQuery = "SELECT documents, creation_time, last_activation_time from twitter_monitor_scheduler where search_id = '" + searchID + "'";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			Assert.assertNotNull(rs, "The query [ " + sqlQuery + " ] doesn't have a valid result");

			if (rs.next()) {

				String documentsStr = rs.getString("documents");
				java.sql.Timestamp creationTimestamp = rs.getTimestamp("creation_time");
				java.sql.Timestamp lastActivationTimestamp = rs.getTimestamp("last_activation_time");

				Assert.assertNotNull(documentsStr, "SQL NULL for documents column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(creationTimestamp, "SQL NULL for creation_time column in results of [ " + sqlQuery + " ] ");
				Assert.assertNotNull(lastActivationTimestamp, "SQL NULL for last_activation_time column in results of [ " + sqlQuery + " ] ");

				// convert sql timestamp into Calendar obj
				Date startDate = roundSQLTimestamp(creationTimestamp);
				Date endDate = roundSQLTimestamp(lastActivationTimestamp);

				String[] documentsArr = documentsStr.split(",");

				for (int i = 0; i < documentsArr.length; i++) {

					String label = documentsArr[i].trim();

					String encodedLabel = URLEncoder.encode(label, "UTF-8");

					String url = this.composeUrlConstants(encodedLabel);

					TwitterDocumentPojo documentPojo = new TwitterDocumentPojo(label, url);

					this.documents.add(documentPojo);
					this.labels.add(label);

				}

			}

			logger.debug("Method initializeTwitterDocumentsDataProcessor(): End");

		} catch (SQLException e) {
			throw new SpagoBIRuntimeException("Method initializeTwitterDocumentsDataProcessor(): Impossible to exectute sql query [ " + sqlQuery + " ]", e);
		} catch (UnsupportedEncodingException e) {
			throw new SpagoBIRuntimeException("Method initializeTwitterDocumentsDataProcessor(): Impossibile to encode documents URL", e);
		}
	}

	/**
	 * This method composes url constants and adds the doc label
	 *
	 * @param docLabel
	 * @return
	 */
	private String composeUrlConstants(String docLabel) {

		return HOST + PORT + CALL + docLabel;

	}

	/**
	 * This method rounds timestamp mills, secs and mins
	 *
	 * @param dbTimestamp
	 * @return
	 */
	private Date roundSQLTimestamp(java.sql.Timestamp dbTimestamp) {

		Calendar calendarTime = GregorianCalendar.getInstance();
		calendarTime.setTime(dbTimestamp);

		calendarTime.set(Calendar.MILLISECOND, 0);
		calendarTime.set(Calendar.SECOND, 0);
		calendarTime.set(Calendar.MINUTE, 0);

		Date roundDate = new Date(calendarTime.getTimeInMillis());

		return roundDate;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

	public List<TwitterDocumentPojo> getDocuments() {
		return documents;
	}

	public void setDocuments(List<TwitterDocumentPojo> documents) {
		this.documents = documents;
	}

}
