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
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;

import java.util.ArrayList;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */
public class TwitterSearchDataProcessor {

	private final ITwitterCache twitterCache = new TwitterCacheFactory()
			.getCache("mysql");

	public List<TwitterSearchPojo> getTwitterSearchList(String searchType) {

		List<TwitterSearchPojo> searchList = new ArrayList<TwitterSearchPojo>();

		String sqlQuery = "SELECT * from twitter_search where type = '"
				+ searchType + "'";

		try {

			CachedRowSet rs = twitterCache.runQuery(sqlQuery);

			if (rs != null) {

				while (rs.next()) {

					long searchID = rs.getLong("search_id");
					String label = rs.getString("label");
					String keywords = rs.getString("keywords");
					java.sql.Date creationDate = rs.getDate("creation_date");
					java.sql.Timestamp lastActivationTime = rs
							.getTimestamp("last_activation_time");
					String frequency = rs.getString("frequency");
					String type = rs.getString("type");
					boolean loading = rs.getBoolean("loading");

					List<String> linksList = new ArrayList<String>();
					String links = "";

					CachedRowSet linksRs = twitterCache
							.runQuery("SELECT link from twitter_links_to_monitor where search_id = "
									+ searchID);
					while (linksRs.next()) {
						linksList.add(linksRs.getString("link"));
					}

					for (int i = 0; i < linksList.size(); i++) {
						if (i == linksList.size() - 1) {
							links = links + linksList.get(i);
						} else {
							links = links + linksList.get(i) + ", ";
						}
					}

					List<String> accountsList = new ArrayList<String>();
					String accounts = "";

					CachedRowSet accountsRs = twitterCache
							.runQuery("SELECT account_name from twitter_accounts_to_monitor where search_id = "
									+ searchID);
					while (accountsRs.next()) {
						accountsList.add(accountsRs.getString("account_name"));
					}

					for (int i = 0; i < accountsList.size(); i++) {
						if (i == accountsList.size() - 1) {
							accounts = accounts + accountsList.get(i);
						} else {
							accounts = accounts + accountsList.get(i) + ", ";
						}
					}

					TwitterSearchPojo searchPojo = new TwitterSearchPojo(
							searchID, label, keywords, creationDate,
							lastActivationTime, frequency, type, loading,
							links, accounts);

					searchList.add(searchPojo);
				}
			}

		} catch (Exception e) {
			System.out.println("**** connection failed: " + e);
		}

		return searchList;
	}

}
