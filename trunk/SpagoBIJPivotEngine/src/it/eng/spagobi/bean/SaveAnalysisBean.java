/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package it.eng.spagobi.bean;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import it.eng.spagobi.bean.adapter.AnalysisAdapterUtil;
import it.eng.spagobi.utilities.GenericSavingException;
import it.eng.spagobi.utilities.SpagoBIAccessUtils;

import javax.servlet.http.HttpSession;

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
	
    public SaveAnalysisBean(String id,  RequestContext context) {
		super(id, null);
		analysisName = "";
		analysisDescription = "";
		analysisVisibility = "Public";
		recoveryAnalysisName = "";
        propertySupport = new PropertyChangeSupport(this);
        getDispatcher().addRequestListener(null, null, dispatcher);
    }
	
	public void setAnalysisName(String analysisName) {
	    if (analysisName == null || analysisName.trim().equals("")) {
			throw new FormatException("Please provide an analysis name");
		}
		if (analysisName.indexOf("/") != -1 || analysisName.indexOf("\\") != -1) {
			throw new FormatException("Analysis name contains file path separators");
		}
		if (analysisName.indexOf("<") != -1 || analysisName.indexOf(">") != -1) {
			throw new FormatException("Analysis name contains invalid characters");
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
				chart, table, olapModel);
		    XStream dataBinder = new XStream();
		    String xmlString = dataBinder.toXML(analysis);
            boolean visibilityBoolean = false;
		    if ("public".equalsIgnoreCase(analysisVisibility)) 
				visibilityBoolean = true;
		    SpagoBIAccessUtils sbiutils = new SpagoBIAccessUtils();
		    try {
		        byte[] response = sbiutils.saveSubObject(spagoBIBaseUrl, jcrPath, analysisName,
		        		analysisDescription, user, visibilityBoolean, xmlString);
		        String message = new String(response);
		        session.setAttribute("message", message);
		        // if the saving operation has no success, the previous 
		        if (message.toUpperCase().startsWith("KO")) this.analysisName = recoveryAnalysisName;
		    } catch (GenericSavingException gse) {		
		    	gse.printStackTrace();
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
