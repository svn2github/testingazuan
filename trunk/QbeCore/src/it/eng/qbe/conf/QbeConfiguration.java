/**

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

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
package it.eng.qbe.conf;

import java.io.File;

import it.eng.qbe.model.io.IDataMartModelRetriever;
import it.eng.qbe.model.io.IQueryPersister;
import it.eng.qbe.model.io.LocalFileSystemDataMartModelRetriever;
import it.eng.qbe.utility.IDBSpaceChecker;
import it.eng.spago.configuration.ConfigSingleton;
import it.eng.spagobi.utilities.file.FileUtils;

/**
 * @author Andrea Gioia
 *
 */
public class QbeConfiguration {
	
	private IDataMartModelRetriever dataMartModelRetriever = null;
	private IQueryPersister queryPersister = null;
	
	private Boolean spaceCheckerEnabled = null;
	private IDBSpaceChecker dbSpaceChecker = null;
	
	private Long freeSpaceLbLimit = null;
	
	private File qbeDataMartDir = null;
	
	private File qbeTempDir = null;
	
	
	
	static private QbeConfiguration instance = null;
	
	static public QbeConfiguration getInstance() {
		if(instance == null) instance = new QbeConfiguration();
		return instance;
	}
	
	private QbeConfiguration() {
		
	}
	
	public IDataMartModelRetriever getDataMartModelRetriever() throws Exception {		
		if(dataMartModelRetriever == null) {
			String dataMartModelRetrieverClassName = (String)ConfigSingleton.getInstance().getAttribute("QBE.DATA-MART-MODEL-RETRIEVER.className");
			dataMartModelRetriever = (IDataMartModelRetriever)Class.forName(dataMartModelRetrieverClassName).newInstance();
			if(dataMartModelRetriever instanceof LocalFileSystemDataMartModelRetriever) {
				LocalFileSystemDataMartModelRetriever localFileSystemDataMartModelRetriever = (LocalFileSystemDataMartModelRetriever)dataMartModelRetriever;
				localFileSystemDataMartModelRetriever.setContextDir(getQbeDataMartDir());
			}
		}
		return dataMartModelRetriever;
	}
	
	public void setDataMartModelRetriever(IDataMartModelRetriever dataMartModelRetriever) {
		this.dataMartModelRetriever = dataMartModelRetriever;
	}
	
	
	public IQueryPersister getQueryPersister() throws Exception{
		if(queryPersister == null) {
			String queryPersisterClass = (String)ConfigSingleton.getInstance().getAttribute("QBE.QUERY-PERSISTER.className");
			queryPersister = (IQueryPersister)Class.forName(queryPersisterClass).newInstance();
		}
		return queryPersister;
	}
	
	public void setQueryPersister(IQueryPersister QueryPersister) {
		this.queryPersister = queryPersister;
	}
	
	public boolean isSpaceCheckerEnabled() {
		if(spaceCheckerEnabled == null) {
			String makeCheck =(String) ConfigSingleton.getInstance().getAttribute("QBE.QBE-CHECK-SPACE-BEFORE-CREATEVIEW.check");
			spaceCheckerEnabled = new Boolean(makeCheck.trim().equalsIgnoreCase("true"));
		}
		return spaceCheckerEnabled.booleanValue();
	}
	
	public void setSpaceCheckerEnabled(boolean spaceCheckerEnabled) {
		this.spaceCheckerEnabled = new Boolean(spaceCheckerEnabled);
	}

	public IDBSpaceChecker getDbSpaceChecker() {
		if(dbSpaceChecker == null) {
			String className = (String) ConfigSingleton.getInstance().getAttribute("QBE.QBE-CHECK-SPACE-BEFORE-CREATEVIEW.checkerClass");
			try {
				dbSpaceChecker = (IDBSpaceChecker)Class.forName(className).newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
		return dbSpaceChecker;
	}

	public void setDbSpaceChecker(IDBSpaceChecker dbSpaceChecker) {
		this.dbSpaceChecker = dbSpaceChecker;
	}

	public long getFreeSpaceLbLimit() {
		if(freeSpaceLbLimit == null) {
			String freeSpaceLbLimitStr = (String) ConfigSingleton.getInstance().getAttribute("QBE.QBE-CHECK-SPACE-BEFORE-CREATEVIEW.failIfSpaceLess");
			freeSpaceLbLimit = new Long(freeSpaceLbLimitStr);
		}
		return freeSpaceLbLimit.longValue();
	}

	public void setFreeSpaceLbLimit(long freeSpaceLbLimit) {
		this.freeSpaceLbLimit = new Long(freeSpaceLbLimit);
	}

	public File getQbeDataMartDir() {
		if(qbeDataMartDir == null) {
			File baseDir = new File(ConfigSingleton.getInstance().getRootPath());
			String qbeDataMartDirPath = (String)ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");																					
			if( !FileUtils.isAbsolutePath(qbeDataMartDirPath) )  {
				String baseDirStr = (baseDir != null)? baseDir.toString(): System.getProperty("user.home");
				qbeDataMartDirPath = baseDir + System.getProperty("file.separator") + qbeDataMartDirPath;
				qbeDataMartDir = new File(qbeDataMartDirPath);
			}
		}
		
		return qbeDataMartDir;
	}

	public void setQbeDataMartDir(File qbeDataMartDir) {
		this.qbeDataMartDir = qbeDataMartDir;
	}
	

	public File getQbeTempDir() {
		if(qbeTempDir == null) {
			qbeTempDir = new File( System.getProperty("java.io.tmpdir") );
			if(!qbeTempDir.exists()) {
				qbeTempDir.mkdirs();
			}
		}
		
		return qbeTempDir;
	}

	public void setQbeTempDir(File qbeTempDir) {
		this.qbeTempDir = qbeTempDir;
	}
	
}
