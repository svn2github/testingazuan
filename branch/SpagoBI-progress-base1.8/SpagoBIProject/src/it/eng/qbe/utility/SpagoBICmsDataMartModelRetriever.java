package it.eng.qbe.utility;


import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.security.IEngUserProfile;
import it.eng.spagobi.bo.dao.DAOFactory;
import it.eng.spagobi.bo.dao.IBIObjectCMSDAO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;


public class SpagoBICmsDataMartModelRetriever implements IDataMartModelRetriever {

	public static final String REFRESH_DATAMART = "REFRESH_DATAMART";
	
	public File getJarFile(String dataMartPath) {
		File modelFile = null;
		try {
			// get profile from session					
			RequestContainer reqCont = RequestContainer.getRequestContainer();
			SessionContainer session = reqCont.getSessionContainer();
			SessionContainer permSession = session.getPermanentContainer();
			IEngUserProfile profile = (IEngUserProfile)permSession.getAttribute(IEngUserProfile.ENG_USER_PROFILE);
			// calvulate the path in the tmp folder for the mart
			String curUser = (String)profile.getUserUniqueIdentifier();
			String pathTmpDir = System.getProperty("java.io.tmpdir");
			String pathModelFile =  pathTmpDir + dataMartPath + "/" + curUser + "/Datamart.jar";
			String pathDirModelFile =  pathTmpDir + dataMartPath + "/" + curUser;
			// if is a new request for datamart delete file in tmp and refresh it
			String refreshDatamart = (String)session.getAttribute(REFRESH_DATAMART);
			if(refreshDatamart!=null) {
				session.delAttribute("REFRESH_DATAMART");
				File dirModel = new File(pathDirModelFile);
				dirModel.mkdirs();
				modelFile = new File(pathModelFile);
			    if(modelFile.exists()) {
					modelFile.delete();
			    }
			    IBIObjectCMSDAO cmsDao = DAOFactory.getBIObjectCMSDAO();
				InputStream is = cmsDao.getTemplate(dataMartPath);
                modelFile = new File(pathModelFile);
				FileOutputStream fos = new FileOutputStream(modelFile);
				byte[] buffer = new byte[1024];
				int len;
				while ((len = is.read(buffer)) >= 0)
					fos.write(buffer, 0, len);
				fos.flush();
				is.close();
				fos.close();
				modelFile = new File(pathModelFile);
			} else {
				modelFile = new File(pathModelFile);
			}
			return modelFile;	
		} catch (Exception e) {
			return null;
		}
	}

	public File getJarFile(String dataMartPath, String dialect) {
	    return getJarFile(dataMartPath);
	}

}
