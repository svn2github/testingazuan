/** SpagoBI, the Open Source Business Intelligence suite
 * Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0, without the "Incompatible With Secondary Licenses" notice.
 * If a copy of the MPL was not distributed with this file, You can obtain one at http://mozilla.org/MPL/2.0/. **/
package it.eng.spagobi.twitter.analysis.cache;

import it.eng.spagobi.bitly.analysis.pojos.BitlyLinkCategoryPojo;
import it.eng.spagobi.bitly.analysis.pojos.BitlyLinkPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterAccountToMonitorPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterMessageObject;
import it.eng.spagobi.twitter.analysis.pojos.TwitterMonitorSchedulerPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchPojo;
import it.eng.spagobi.twitter.analysis.pojos.TwitterSearchSchedulerPojo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.GregorianCalendar;
import java.util.List;

import javax.sql.rowset.CachedRowSet;

import org.apache.log4j.Logger;

import com.sun.rowset.CachedRowSetImpl;

/**
 * @author Marco Cortella (marco.cortella@eng.it), Giorgio Federici
 *         (giorgio.federici@eng.it)
 *
 */
public class MySQLTwitterCache extends AbstractTwitterCache {

	private static final Logger logger = Logger.getLogger(MySQLTwitterCache.class);
	private Connection conn = null;

	public MySQLTwitterCache(String url, String driver, String userName, String password) {
		super(url, driver, userName, password);
	}

	@Override
	public Connection openConnection() {

		logger.debug("Method openConnection(): Start");

		try {
			Class.forName(getDriver()).newInstance();
			conn = DriverManager.getConnection(getUrl(), getUserName(), getPassword());

		} catch (InstantiationException e) {

			logger.debug("**** ERROR Connecting to the database: " + e);
		} catch (IllegalAccessException e) {

			logger.debug("**** ERROR Connecting to the database: " + e);
		} catch (ClassNotFoundException e) {
			logger.debug("**** ERROR Connecting to the database: " + e);
		} catch (SQLException e) {
			logger.debug("**** ERROR Connecting to the database: " + e);
		}

		logger.debug("Method openConnection(): End");

		return conn;

	}

	@Override
	public void closeConnection() {

		logger.debug("Method closeConnection(): Start");

		try {
			if ((conn != null) && (!conn.isClosed())) {
				conn.close();
			}
		} catch (SQLException e) {

			logger.debug("**** ERROR Disconnecting to the database: " + e);
		}

		logger.debug("Method closeConnection(): End");
	}

	@Override
	public long insertTwitterSearch(TwitterSearchPojo twitterSearch) {

		logger.debug("Method insertTwitterSearch(): Start");

		PreparedStatement statement = null;

		ResultSet generatedKeys = null;

		long searchID = -1;

		try {
			// TODO ragionare sulle possibili ottimizzazioni della
			// openConnection nella fase di inserimento ricerca, tweets, utenti,
			// etc.
			conn = openConnection();

			logger.debug("Method insertTwitterSearch(): Connection opened. Trying to insert the new search..");

			boolean loading = true;

			// a streaming search is loading when the stream is open. For now,
			// disabled at creation
			if (twitterSearch.getSearchType().equals("streamingAPI")) {
				loading = false;
			}

			String insertSearchSQL = "INSERT INTO `twitterdb`.`twitter_search`" + " (`label`,`keywords`, `creation_date`,`frequency`,`type`,`loading`,`isDeleted`)"
					+ " VALUES (?,?,?,?,?,?,?)";

			statement = conn.prepareStatement(insertSearchSQL, Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, twitterSearch.getLabel());
			statement.setString(2, twitterSearch.getKeywords());
			statement.setDate(3, new java.sql.Date(GregorianCalendar.getInstance().getTimeInMillis()));
			statement.setString(4, twitterSearch.getFrequency());
			statement.setString(5, twitterSearch.getSearchType());
			statement.setBoolean(6, loading);
			statement.setBoolean(7, false);

			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {

				logger.debug("Method insertTwitterSearch(): Creating search failed, no rows affected.");
				throw new SQLException("Creating search failed, no rows affected.");
			}

			generatedKeys = statement.getGeneratedKeys();

			if (generatedKeys.next()) {

				searchID = generatedKeys.getLong(1);
				logger.debug("Method insertTwitterSearch(): Creating search success. Search ID: " + searchID);
			}

			closeConnection();

		} catch (Exception e) {
			logger.debug("Method insertTwitterSearch(): Error creating new search - " + e);

		}

		logger.debug("Method insertTwitterSearch(): End");

		return searchID;
	}

	@Override
	public void insertTweet(TwitterMessageObject twitterMessage, long searchID) {

		logger.debug("Method insertTweet(): Start");

		java.sql.PreparedStatement st;
		Statement stmt;

		try {

			stmt = conn.createStatement();
			ResultSet res1 = stmt.executeQuery("SELECT tweet_id from `twitter_data` where tweet_id = '" + twitterMessage.getTweetID() + "' and search_id = '" + searchID + "'");

			if (!res1.next()) {

				logger.debug("Method insertTweet(): Inserting new tweet..");

				st = conn
						.prepareStatement("INSERT INTO `twitterdb`.`twitter_data` "
								+ "(`tweet_id`,`user_id`,`search_id`,`date_created_at`,`time_created_at`,`source_client`,`tweet_text`,`tweet_text_translated`,`geo_latitude`,`geo_longitude`,`hashtags`,`mentions`,`retweet_count`,`is_retweet`,`language_code`,`place_country`,`place_name`,`url_cited`,`is_favorited`,`favorited_count`,`reply_to_screen_name`,`reply_to_user_id`,`reply_to_tweet_id`,`original_RT_tweet_id`,`is_sensitive`,`media_count`)"
								+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				st.setLong(1, twitterMessage.getTweetID());
				st.setLong(2, twitterMessage.getUserID());
				st.setLong(3, searchID);
				st.setDate(4, twitterMessage.getDateCreatedAt());
				st.setTimestamp(5, twitterMessage.getTimeCreatedAt());
				st.setString(6, twitterMessage.getSourceClient());
				st.setString(7, twitterMessage.getTweetText());
				st.setString(8, twitterMessage.getTweetTextTranslated());
				st.setDouble(9, twitterMessage.getGeoLatitude());
				st.setDouble(10, twitterMessage.getGeoLongitude());
				st.setString(11, twitterMessage.getHashtags());
				st.setString(12, twitterMessage.getMentions());
				st.setInt(13, twitterMessage.getRetweetCount());
				st.setBoolean(14, twitterMessage.isRetweet());
				st.setString(15, twitterMessage.getLanguageCode());
				st.setString(16, twitterMessage.getPlaceCountry());
				st.setString(17, twitterMessage.getPlaceName());
				st.setString(18, twitterMessage.getUrlCited());
				st.setBoolean(19, twitterMessage.isFavorited());
				st.setLong(20, twitterMessage.getFavoritedCount());
				st.setString(21, twitterMessage.getReplyToScreenName());
				st.setString(22, twitterMessage.getReplyToUserId());
				st.setString(23, twitterMessage.getReplyToTweetId());
				st.setString(24, twitterMessage.getOriginalRTTweetId());
				st.setBoolean(25, twitterMessage.isSensitive());
				st.setInt(26, twitterMessage.getMediaCount());

				st.executeUpdate();

				logger.debug("Method insertTweet(): Tweet " + twitterMessage.getTweetID() + " inserted for search " + searchID);
			} else {

				// update tweet info
				logger.debug("Method insertTweet(): Tweet " + twitterMessage.getTweetID() + " already present for search " + searchID);
			}

			logger.debug("Method insertTweet(): End");

		} catch (Exception e) {
			logger.debug("Method insertTweet(): Error creating new tweet - " + e);

		}

	}

	@Override
	public void insertBitlyAnalysis(BitlyLinkPojo linkPojo, List<BitlyLinkCategoryPojo> linkCategoryPojos, long searchID) {

		logger.debug("Method insertBitlyAnalysis(): Start");

		PreparedStatement statement = null;
		PreparedStatement statementLinkCategory = null;

		ResultSet generatedKeys = null;

		conn = openConnection();

		try {

			logger.debug("Method insertBitlyAnalysis(): Inserting new link to monitor..");

			String link_insert = "INSERT INTO `twitterdb`.`twitter_links_to_monitor`" + "(`search_id`,`link`,`clicks_count`)" + " VALUES (?,?,?)";

			statement = conn.prepareStatement(link_insert, Statement.RETURN_GENERATED_KEYS);
			statement.setLong(1, searchID);
			statement.setString(2, linkPojo.getLink());
			statement.setInt(3, linkPojo.getCounter_clicks());

			int affectedRows = statement.executeUpdate();
			if (affectedRows == 0) {

				logger.debug("Method insertBitlyAnalysis(): Error inserting new link to monitor..");
				throw new SQLException("Creating link to monitor failed, no rows affected.");
			}

			generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {

				logger.debug("Method insertBitlyAnalysis(): Success inserting new link to monitor. Creating linked analysis..");

				long link_id = generatedKeys.getLong(1);

				for (BitlyLinkCategoryPojo linkCategoryPojo : linkCategoryPojos) {

					String linkCategory_insert = "INSERT INTO `twitterdb`.`twitter_link_to_monitor_category`" + "(`link_id`,`type`,`category`,`clicks_count`)"
							+ " VALUES (?,?,?,?)";

					statementLinkCategory = conn.prepareStatement(linkCategory_insert);
					statementLinkCategory.setLong(1, link_id);
					statementLinkCategory.setString(2, linkCategoryPojo.getType());
					statementLinkCategory.setString(3, linkCategoryPojo.getCategory());
					statementLinkCategory.setInt(4, linkCategoryPojo.getClicks_count());

					statementLinkCategory.executeUpdate();
				}

			} else {
				throw new SQLException("Creating link failed, no generated key obtained.");
			}
		} catch (SQLException e) {
			logger.debug("Method insertBitlyAnalysis(): Creating link failed, no generated key obtained." + e);
		} finally {
			if (generatedKeys != null)
				try {
					generatedKeys.close();
				} catch (SQLException e) {
					logger.debug("Method insertBitlyAnalysis(): Creating link failed, no generated key obtained." + e);
				}
			if (statement != null)
				try {
					statement.close();
				} catch (SQLException e) {
					logger.debug("Method insertBitlyAnalysis(): Creating link failed, no generated key obtained." + e);
				}
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					logger.debug("Method insertBitlyAnalysis(): Creating link failed, no generated key obtained." + e);
				}

			logger.debug("Method insertBitlyAnalysis(): End");
		}
	}

	@Override
	public void insertTwitterUser(TwitterMessageObject twitterMessage) {

		logger.debug("Method insertTwitterUser(): Start");

		java.sql.PreparedStatement st;
		Statement stmt;

		try {

			stmt = conn.createStatement();
			ResultSet res1 = stmt.executeQuery("SELECT user_id from `twitter_users` where user_id = '" + twitterMessage.getUserID() + "'");

			if (!res1.next()) {

				logger.debug("Method insertTwitterUser(): Inserting new user: " + twitterMessage.getUsername());

				st = conn
						.prepareStatement("INSERT INTO `twitterdb`.`twitter_users`"
								+ " (`user_id`,`username`, `description`,`followers_count`,`profile_image_source`,`location`,`location_code`,`language_code`,`name`,`time_zone`,`tweets_count`,`verified`,`following_count`,`UTC_offset`,`is_geo_enabled`,`listed_count`,`start_date`,`end_date`)"
								+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
				st.setLong(1, twitterMessage.getUserID());
				st.setString(2, twitterMessage.getUsername());
				st.setString(3, twitterMessage.getDescription());
				st.setInt(4, twitterMessage.getFollowersCount());
				st.setString(5, twitterMessage.getProfileImgSrc());
				st.setString(6, twitterMessage.getLocation());

				String locationCode = findCountryCodeFromUserLocation(twitterMessage.getLocation(), twitterMessage.getTimeZone());
				st.setString(7, locationCode);

				st.setString(8, twitterMessage.getUserLanguageCode());
				st.setString(9, twitterMessage.getName());
				st.setString(10, twitterMessage.getTimeZone());
				st.setInt(11, twitterMessage.getTweetsCount());
				st.setBoolean(12, twitterMessage.isVerified());
				st.setInt(13, twitterMessage.getFollowingCount());
				st.setInt(14, twitterMessage.getUtcOffset());
				st.setBoolean(15, twitterMessage.isGeoEnabled());
				st.setInt(16, twitterMessage.getListedCount());
				st.setDate(17, twitterMessage.getStartDate());
				st.setDate(18, twitterMessage.getEndDate());

				st.executeUpdate();

				logger.debug("Method insertTwitterUser(): New user: " + twitterMessage.getUsername() + " inserted");
			} else {
				logger.debug("Method insertTwitterUser(): User: " + twitterMessage.getUsername() + " already present in the DB");
			}

		} catch (Exception e) {
			logger.debug("Method insertTwitterUser(): Error inserting a user: " + e);

		}

	}

	@Override
	public void insertAccountToMonitor(TwitterAccountToMonitorPojo accountToMonitor) {

		logger.debug("Method insertAccountToMonitor(): Start");

		try {

			conn = openConnection();

			String accountName = "@" + accountToMonitor.getUsername();

			logger.debug("Method insertAccountToMonitor(): Inserting new account to monitor for search: " + accountToMonitor.getSearchID());

			java.sql.PreparedStatement st = conn.prepareStatement("INSERT INTO `twitterdb`.`twitter_accounts_to_monitor`"
					+ " (`search_id`,`account_name`,`followers_count`,`timestamp`)" + " VALUES (?,?,?,?)");
			st.setLong(1, accountToMonitor.getSearchID());
			st.setString(2, accountName);
			st.setInt(3, accountToMonitor.getFollowers());
			st.setTimestamp(4, accountToMonitor.getTimestamp());

			st.executeUpdate();

			logger.debug("Method insertAccountToMonitor(): New account to monitor for search: " + accountToMonitor.getSearchID() + " inserted");

			closeConnection();
		} catch (Exception e) {
			logger.debug("Method insertAccountToMonitor(): Error inserting new account to monitor for search: " + accountToMonitor.getSearchID());

		}

		logger.debug("Method insertAccountToMonitor(): End");
	}

	@Override
	public CachedRowSet runQuery(String sqlQuery) {

		logger.debug("Method runQuery(): Start");

		CachedRowSet rowset = null;

		try {

			conn = openConnection();

			rowset = new CachedRowSetImpl();
			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(sqlQuery);
			rowset.populate(rs);

			closeConnection();

		} catch (Exception e) {
			logger.debug("Method runQuery(): Error running the query: " + sqlQuery + " - " + e);
		}

		logger.debug("Method runQuery(): End");
		return rowset;

	}

	@Override
	public void updateTwitterSearchLoading(long searchID, boolean isLoading) {

		logger.debug("Method updateTwitterSearchLoading: Start");

		try {

			conn = openConnection();

			if (isLoading) {
				java.sql.PreparedStatement st = conn.prepareStatement("UPDATE `twitterdb`.`twitter_search` SET loading = ?, last_activation_time = ? where search_id = '"
						+ searchID + "'");
				st.setBoolean(1, isLoading);
				st.setTimestamp(2, new java.sql.Timestamp(GregorianCalendar.getInstance().getTimeInMillis()));
				st.executeUpdate();
			} else {
				java.sql.PreparedStatement st = conn.prepareStatement("UPDATE `twitterdb`.`twitter_search` SET loading = ? where search_id = '" + searchID + "'");
				st.setBoolean(1, isLoading);
				st.executeUpdate();
			}

			closeConnection();

		} catch (Exception e) {
			logger.debug("Method updateTwitterSearchLoading: Error updating loading field for search: " + searchID + " - " + e);
		}

		logger.debug("Method updateTwitterSearchLoading: End");
	}

	@Override
	public void stopStreamingSearch() {

		logger.debug("Method stopStreamingSearch(): Start");

		try {

			conn = openConnection();

			java.sql.PreparedStatement st = conn.prepareStatement("UPDATE `twitterdb`.`twitter_search` SET loading = 0 where type = 'streamingAPI'");
			st.executeUpdate();
			closeConnection();

		} catch (Exception e) {
			logger.debug("Method stopStreamingSearch(): Error stopping search streams " + e);
		}

		logger.debug("Method stopStreamingSearch(): End");

	}

	@Override
	public void insertTwitterSearchScheduler(TwitterSearchSchedulerPojo twitterScheduler) {

		logger.debug("Method insertTwitterSearchScheduler(): Start");

		try {

			conn = openConnection();

			java.sql.PreparedStatement st = conn.prepareStatement("INSERT INTO `twitterdb`.`twitter_search_scheduler`"
					+ " (`search_id`,`starting_date`,`repeat_frequency`,`repeat_type`,`active`)" + " VALUES (?,?,?,?,?)");
			st.setLong(1, twitterScheduler.getSearchID());
			st.setTimestamp(2, new java.sql.Timestamp(twitterScheduler.getStartingDate().getTimeInMillis()));
			st.setInt(3, twitterScheduler.getRepeatFrequency());
			st.setString(4, twitterScheduler.getRepeatType());
			st.setBoolean(5, true);

			st.executeUpdate();
			closeConnection();

		} catch (Exception e) {
			logger.debug("Method insertTwitterSearchScheduler(): Error inserting search scheduler for search: " + twitterScheduler.getSearchID() + " - " + e);
		}

		logger.debug("Method insertTwitterSearchScheduler(): End");
	}

	@Override
	public void insertTwitterMonitorScheduler(TwitterMonitorSchedulerPojo twitterScheduler) {

		logger.debug("Method insertTwitterMonitorScheduler(): Start");

		try {

			conn = openConnection();

			java.sql.PreparedStatement st = conn.prepareStatement("INSERT INTO `twitterdb`.`twitter_monitor_scheduler`"
					+ " (`search_id`,`ending_date`,`repeat_frequency`,`repeat_type`,`active`,`up_to_value`,`up_to_type` )" + " VALUES (?,?,?,?,?,?,?)");
			st.setLong(1, twitterScheduler.getSearchID());
			st.setTimestamp(2, new java.sql.Timestamp(twitterScheduler.getEndingDate().getTimeInMillis()));
			st.setInt(3, twitterScheduler.getRepeatFrequency());
			st.setString(4, twitterScheduler.getRepeatType());
			st.setBoolean(5, twitterScheduler.isActive());
			st.setInt(6, twitterScheduler.getUpToValue());
			st.setString(7, twitterScheduler.getUpToType());

			st.executeUpdate();
			closeConnection();

		} catch (Exception e) {
			logger.debug("Method insertTwitterMonitorScheduler(): Error inserting monitor scheduler for search " + twitterScheduler.getSearchID() + " - " + e);
		}

		logger.debug("Method insertTwitterMonitorScheduler(): End");
	}

	@Override
	public void deleteSearch(TwitterSearchPojo twitterSearch) {

		logger.debug("Method deleteSearch(): Start");

		try {

			conn = openConnection();

			boolean loading = false;

			logger.debug("Method deleteSearch(): Deleting monitor scheduler..");

			java.sql.PreparedStatement monitorSt = conn.prepareStatement("DELETE FROM `twitterdb`.`twitter_monitor_scheduler` WHERE search_id = " + twitterSearch.getSearchID());

			monitorSt.executeUpdate();

			if (twitterSearch.getSearchType().equals("searchAPI")) {

				logger.debug("Method deleteSearch(): Deleting search scheduler..");

				java.sql.PreparedStatement accountSt = conn.prepareStatement("DELETE FROM `twitterdb`.`twitter_search_scheduler` WHERE search_id = " + twitterSearch.getSearchID());

				accountSt.executeUpdate();
			}

			logger.debug("Method deleteSearch(): Deleting logically the search " + twitterSearch.getSearchID());

			java.sql.PreparedStatement st = conn.prepareStatement("UPDATE `twitterdb`.`twitter_search` SET loading = 0 and isDeleted = 1 where search_id = "
					+ twitterSearch.getSearchID());
			st.executeUpdate();

			closeConnection();

		} catch (Exception e) {
			logger.debug("Method deleteSearch(): Error deleting search " + twitterSearch.getSearchID() + " - " + e);
		}

		logger.debug("Method deleteSearch()deleteSearch(): End");
	}

	@Override
	public void updateStartingDateSearchScheduler(TwitterSearchSchedulerPojo twitterSearchSchedulerPojo) {

		logger.debug("Method updateStartingDateSearchScheduler: Start");

		try {

			conn = openConnection();

			java.sql.Timestamp startingDateTimestamp = new java.sql.Timestamp(twitterSearchSchedulerPojo.getStartingDate().getTimeInMillis());

			java.sql.PreparedStatement st = conn.prepareStatement("UPDATE `twitterdb`.`twitter_search_scheduler` SET starting_date = ? where id = '"
					+ twitterSearchSchedulerPojo.getId() + "'");
			st.setTimestamp(1, startingDateTimestamp);
			st.executeUpdate();

			closeConnection();

		} catch (Exception e) {
			logger.debug("Method updateStartingDateSearchScheduler: Error updating search scheduler for search: " + twitterSearchSchedulerPojo.getSearchID() + " - " + e);
		}

		logger.debug("Method updateStartingDateSearchScheduler: End");

	}

	@Override
	public TwitterMonitorSchedulerPojo stopSearchScheduler(TwitterSearchPojo twitterSearch) {

		logger.debug("Method stopSearchScheduler(): Start");

		TwitterMonitorSchedulerPojo twitterMonitor = null;

		try {

			conn = openConnection();

			java.sql.PreparedStatement st = conn.prepareStatement("UPDATE `twitterdb`.`twitter_search_scheduler` SET active = ? where search_id = '" + twitterSearch.getSearchID()
					+ "'");
			st.setBoolean(1, false);
			st.executeUpdate();

			CachedRowSet rs = runQuery("SELECT * FROM `twitterdb`.`twitter_monitor_scheduler` WHERE search_id = '" + twitterSearch.getSearchID() + "'");

			if (rs.next()) {
				String type = rs.getString("repeat_type");
				int frequency = rs.getInt("repeat_frequency");
				int upToValue = rs.getInt("up_to_value");
				String upToType = rs.getString("up_to_type");
				boolean active = rs.getBoolean("active");

				twitterMonitor = new TwitterMonitorSchedulerPojo();
				twitterMonitor.setSearchID(twitterSearch.getSearchID());
				twitterMonitor.setRepeatFrequency(frequency);
				twitterMonitor.setRepeatType(type);
				twitterMonitor.setUpToType(upToType);
				twitterMonitor.setUpToValue(upToValue);
				twitterMonitor.setActive(active);
			}

			closeConnection();

		} catch (Exception e) {
			logger.debug("Method stopSearchScheduler(): Error updating search scheduler for search: " + twitterSearch.getSearchID() + " - " + e);
		}

		logger.debug("Method stopSearchScheduler(): End");
		return twitterMonitor;

	}

	@Override
	public void updateMonitorScheduler(TwitterMonitorSchedulerPojo twitterMonitorSchedulerPojo) {

		logger.debug("Method updateMonitorScheduler(): Start");

		try {

			conn = openConnection();

			java.sql.Timestamp endingDateTimestamp = new java.sql.Timestamp(twitterMonitorSchedulerPojo.getEndingDate().getTimeInMillis());

			java.sql.PreparedStatement st = conn.prepareStatement("UPDATE `twitterdb`.`twitter_monitor_scheduler` SET ending_date = '" + endingDateTimestamp + "', active = "
					+ twitterMonitorSchedulerPojo.isActive() + " where search_id = " + twitterMonitorSchedulerPojo.getSearchID());
			st.executeUpdate();

			closeConnection();

		} catch (Exception e) {
			logger.debug("Method updateMonitorScheduler(): Error updating monitor scheduler for search: " + twitterMonitorSchedulerPojo.getSearchID() + " - " + e);
		}

		logger.debug("Method updateMonitorScheduler(): End");

	}

	@Override
	public TwitterMonitorSchedulerPojo getTwitterMonitorScheduler(long searchID) {
		logger.debug("Method stopSearchScheduler(): Start");

		TwitterMonitorSchedulerPojo twitterMonitor = null;

		try {

			conn = openConnection();

			CachedRowSet rs = runQuery("SELECT * FROM `twitterdb`.`twitter_monitor_scheduler` WHERE search_id = " + searchID);

			if (rs.next()) {
				String type = rs.getString("repeat_type");
				int frequency = rs.getInt("repeat_frequency");
				int upToValue = rs.getInt("up_to_value");
				String upToType = rs.getString("up_to_type");
				boolean active = rs.getBoolean("active");

				twitterMonitor = new TwitterMonitorSchedulerPojo();
				twitterMonitor.setSearchID(searchID);
				twitterMonitor.setRepeatFrequency(frequency);
				twitterMonitor.setRepeatType(type);
				twitterMonitor.setUpToType(upToType);
				twitterMonitor.setUpToValue(upToValue);
				twitterMonitor.setActive(active);
			}

			closeConnection();

		} catch (Exception e) {
			logger.debug("Method getTwitterMonitorScheduler(): Error updating search scheduler for search: " + searchID + " - " + e.getMessage());
		}

		logger.debug("Method getTwitterMonitorScheduler(): End");
		return twitterMonitor;
	}
}
