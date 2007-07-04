/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package it.eng.spagobi.jpivotaddins.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Locale;

import it.eng.spagobi.jpivotaddins.bean.adapter.AnalysisAdapterUtil;
import it.eng.spagobi.utilities.GenericSavingException;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;
import it.eng.spagobi.utilities.messages.EngineMessageBundle;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

import com.thoughtworks.xstream.XStream;
import com.tonbeller.jpivot.chart.ChartComponent;
import com.tonbeller.jpivot.olap.model.OlapModel;
import com.tonbeller.jpivot.olap.navi.MdxQuery;
import com.tonbeller.jpivot.table.TableComponent;
import com.tonbeller.wcf.component.ComponentSupport;
import com.tonbeller.wcf.controller.Dispatcher;
import com.tonbeller.wcf.controller.DispatcherSupport;
import com.tonbeller.wcf.controller.RequestContext;
import com.tonbeller.wcf.format.FormatException;

public class SaveAnalysisBean extends ComponentSupport {
	
	public static final String PUBLIC_VISIBLITY = "Public";
	public static final String PRIVATE_VISIBLITY = "Private";
	
	private transient Logger logger = Logger.getLogger(this.getClass());
	
	private PropertyChangeSupport propertySupport;
	
	Dispatcher dispatcher = new DispatcherSupport();
	
	private String analysisName;
	
	private String analysisDescription;

	private String analysisVisibility;

	private String recoveryAnalysisName;
	
	public String getAnalysisDescription() {
		return analysisDescription;
	}

	public void setAnalysisDescription(String analysisDescription) {
		this.analysisDescription = analysisDescription;
	}

	public String getAnalysisName() {
		return analysisName;
	}
	
    public SaveAnalysisBean(String id,  RequestContext aContext) {
		super(id, null);
		analysisName = "";
		analysisDescription = "";
		analysisVisibility = PUBLIC_VISIBLITY;
		recoveryAnalysisName = "";
        propertySupport = new PropertyChangeSupport(this);
        getDispatcher().addRequestListener(null, null, dispatcher);
    }
	
	public void setAnalysisName(String analysisName) {
		RequestContext context = RequestContext.instance();
		Locale locale = context.getLocale();
	    if (analysisName == null || analysisName.trim().equals("")) {
	    	logger.error("Analysis name missing.");
	    	String msg = EngineMessageBundle.getMessage("error.analysis.name.missing", locale);
			throw new FormatException(msg);
		}
		if (analysisName.indexOf("/") != -1 || analysisName.indexOf("\\") != -1) {
			logger.error("Analysis name contains file path separators.");
	    	String msg = EngineMessageBundle.getMessage("error.analysis.name.contains.separators", locale);
			throw new FormatException(msg);
		}
		if (analysisName.indexOf("<") != -1 || analysisName.indexOf(">") != -1) {
			logger.error("Analysis name contains invalid characters.");
	    	String msg = EngineMessageBundle.getMessage("error.analysis.name.invalid.characters", locale);
			throw new FormatException(msg);
		}
		// save the current analisys name if the recoveryAnalysisName variable
		this.recoveryAnalysisName = this.analysisName;
		this.analysisName = analysisName;
	}

	public String getAnalysisVisibility() {
		return analysisVisibility;
	}

	public void setAnalysisVisibility(String analysisVisibility) {
		this.analysisVisibility = analysisVisibility;
	}
	
	public void resetFields (){
		analysisName = null;
		analysisDescription = null;
		analysisVisibility = null;
	}
	
	public void saveSubObject(RequestContext reqContext){
		HttpSession session = reqContext.getSession();
		String spagoBIBaseUrl = (String) session.getAttribute("spagobiurl");
		String jcrPath = (String) session.getAttribute("templatePath");
		String user = (String) session.getAttribute("user");
		OlapModel olapModel = (OlapModel) session.getAttribute("query01");
		String query = null;
		MdxQuery mdxQuery = (MdxQuery) olapModel.getExtension("mdxQuery");
		if (mdxQuery != null) {
			query = mdxQuery.getMdxQuery();
		}
		if (query != null) {
			ChartComponent chart = (ChartComponent) session.getAttribute("chart01");
			TableComponent table = (TableComponent) session.getAttribute("table01");
			AnalysisBean analysis = (AnalysisBean) session.getAttribute("analysisBean");
			analysis = AnalysisAdapterUtil.createAnalysisBean(analysis.getConnectionName(), analysis.getCatalogUri(), 
					analysis.getCatalog(),
				chart, table, olapModel);
		    XStream dataBinder = new XStream();
		    String xmlString = dataBinder.toXML(analysis);
            boolean visibilityBoolean = false;
		    if (PUBLIC_VISIBLITY.equalsIgnoreCase(analysisVisibility)) 
				visibilityBoolean = true;
		    SpagoBIAccessUtils sbiutils = new SpagoBIAccessUtils();
		    try {
		        byte[] response = sbiutils.saveSubObject(spagoBIBaseUrl, jcrPath, analysisName,
		        		analysisDescription, user, visibilityBoolean, xmlString);
		        String message = new String(response);
		        session.setAttribute("saveSubObjectMessage", message);
		        // if the saving operation has no success, the previous 
		        if (message.toUpperCase().startsWith("KO")) this.analysisName = recoveryAnalysisName;
		    } catch (GenericSavingException gse) {		
		    	logger.error("Error while saving analysis.", gse);
		    	session.setAttribute("saveSubObjectMessage", "KO - " + gse.getMessage());
		    }   
		}
	}

	public Document render(RequestContext arg0) throws Exception {
		return null;
	}
	
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.addPropertyChangeListener(listener);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertySupport.removePropertyChangeListener(listener);
    }

    /**
     * called once by the creating tag
     */
    public void initialize(RequestContext context) throws Exception {
            super.initialize(context);
    }
	
}
