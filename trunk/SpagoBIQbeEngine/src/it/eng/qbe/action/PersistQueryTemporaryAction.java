
package it.eng.qbe.action;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletResponse;

import it.eng.qbe.model.DataMartModel;
import it.eng.qbe.utility.Utils;
import it.eng.qbe.wizard.ISingleDataMartWizardObject;
import it.eng.spago.base.Constants;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import org.safehaus.uuid.UUID;
import org.safehaus.uuid.UUIDGenerator;


/**
 * @author Andrea Zoppello
 * 
 * This action is responsible to Persist the current working query represented by
 * the object ISingleDataMartWizardObject in session
 */
public class PersistQueryTemporaryAction extends AbstractHttpAction {
	
	private static boolean isAbsolutePath(String path) {
		if(path == null) return false;
		return (path.startsWith("/") || path.startsWith("\\") || path.charAt(1) == ':');
	}
	
	private static String getQbeDataMartDir(File baseDir) {
		String qbeDataMartDir = null;
		qbeDataMartDir = (String)it.eng.spago.configuration.ConfigSingleton.getInstance().getAttribute("QBE.QBE-MART_DIR.dir");
		if( !isAbsolutePath(qbeDataMartDir) )  {
			String baseDirStr = (baseDir != null)? baseDir.toString(): System.getProperty("user.home");
			qbeDataMartDir = baseDir + System.getProperty("file.separator") + qbeDataMartDir;
		}
		return qbeDataMartDir;
	}
	
	public void service(SourceBean request, SourceBean response) {
		
		try{
			DataMartModel dmModel = (DataMartModel)getRequestContainer().getSessionContainer().getAttribute("dataMartModel");
	
			String qbeDataMartDir = getQbeDataMartDir(new File(it.eng.spago.configuration.ConfigSingleton.getInstance().getRootPath()));
		
			String publicDmDir = qbeDataMartDir +System.getProperty("file.separator") +  dmModel.getPath();
		
			UUIDGenerator uuidGenerator = UUIDGenerator.getInstance();
			UUID uuidObj = uuidGenerator.generateTimeBasedUUID();
			String uuid = uuidObj.toString();
		
			String fileName = publicDmDir + System.getProperty("file.separator") + uuid+ ".qbe";
		
			ISingleDataMartWizardObject wizardObject = Utils.getWizardObject(getRequestContainer().getSessionContainer());
			File f = new File(fileName);
			if (!f.exists()){
					f.createNewFile();
			}
			XMLEncoder encoder = new XMLEncoder(new BufferedOutputStream(
			        new FileOutputStream(fileName)));
			encoder.writeObject(wizardObject);
			encoder.close();
			getResponseContainer().setAttribute(Constants.HTTP_RESPONSE_FREEZED, Boolean.TRUE);
			/*
			SourceBean sBean = new SourceBean("uuid");
			sBean.setCharacters(uuid);
			response.setAttribute(sBean);
			*/
			getHttpResponse().getWriter().write(uuid);
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
}
