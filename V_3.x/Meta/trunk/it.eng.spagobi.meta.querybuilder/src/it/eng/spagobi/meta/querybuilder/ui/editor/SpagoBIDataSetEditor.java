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
package it.eng.spagobi.meta.querybuilder.ui.editor;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.datasource.IDataSource;
import it.eng.qbe.query.Query;
import it.eng.qbe.query.serializer.IQueryDeserializer;
import it.eng.qbe.query.serializer.SerializerFactory;
import it.eng.qbe.serializer.SerializationException;
import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.commons.utils.SpagoBIMetaConstants;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelPackage;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.serializer.EmfXmiSerializer;
import it.eng.spagobi.meta.model.validator.ModelValidator;
import it.eng.spagobi.meta.querybuilder.oda.OdaStructureBuilder;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



/**
 *	This class create an Editor for the QueryBuilder
 *  - First Page is for editing the query
 *  - Second Page is for showing query results
 */
public class SpagoBIDataSetEditor extends MultiPageEditorPart implements IResourceChangeListener{

	private QueryBuilder queryBuilder;
	private SpagoBIDataSetEditPage queryEditPage;
	private SpagoBIDataSetResultPage queryResultPage;
	
	protected boolean dirty;
	
	private JSONObject o;
	
	
	private static Logger logger = LoggerFactory.getLogger(SpagoBIDataSetEditor.class);
	

	public SpagoBIDataSetEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
		dirty = false;
	}

	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
	
		String persistenceUnitName;
		BusinessModel businessModel;
		IDataSource dataSource;
		
		logger.trace("IN");
		
		try {
			super.init(site, editorInput);
			
			String inputContents = getInputContents(editorInput);
			try {
				o = new JSONObject(inputContents);
			} catch (Throwable t) {
				throw new SpagoBIPluginException("Impossible to parse editor input [" + inputContents + "]", t);
			}
			
			JSONObject queryMetaJSON = o.optJSONObject("queryMeta");
			logger.debug("query meta is equal to [{}]", queryMetaJSON);
			
			JSONObject queryStatementJSON = o.optJSONObject("query");
			logger.debug("query statement is equal to [{}]", queryStatementJSON);
			
			String modelPath = queryMetaJSON.optString("modelPath");
			logger.debug("Model path is equal to [{}]", modelPath);
			 
			
			businessModel = loadBusinessModel(modelPath);
			logger.debug("Model [" + businessModel.getName() + "] sucesfully loaded from file [" + modelPath + "]");
			
			validateModel(businessModel);
			logger.debug("Model [" + businessModel.getName() + "] sucesfully valideated");
			
			persistenceUnitName = getPersistenceUnitName(modelPath, businessModel);	
			logger.debug("Persistence unit name for the mapping for model [" + businessModel.getName() + "] will be equal to [" + persistenceUnitName + "]");
			
			dataSource = createDataSource(modelPath, businessModel, persistenceUnitName);
			logger.debug("Data Source [" + dataSource.getName() + "] succesfully created");
			
			queryBuilder = new QueryBuilder(dataSource);
			logger.debug("Query buider class succesfully created");
			
			logger.debug("Loading query statement...");
		    if(queryStatementJSON == null) {
				logger.debug("No previously saved query statement to load");
			} else {
				IQueryDeserializer queryDeserializer = SerializerFactory.getDeserializer("application/json");
				if(queryDeserializer == null) {
					throw new SpagoBIPluginException("Impossible to load a query deserializeer for query encoded in [application/json] format");
				}
				Query query = queryDeserializer.deserializeQuery(queryStatementJSON, queryBuilder.getDataSource());
				queryBuilder.setQuery( query );
				
				logger.debug("Query statement succesfully loaded");
			}
		    
		} catch(Throwable t) {
			showInformation("Impossible to open Query editor","Query editor cannot be opened: " + t.getMessage());
			throw new PartInitException("Impossible to initialize editor [" + this.getClass().getName()+ "]: " + t.getCause().getMessage());
		} finally {
			logger.trace("OUT");
		}
	}
	
	private IFile getModelFile(String path) {
		IFile file;
		
		file = null;
		
		try {
			IWorkspace workspace= ResourcesPlugin.getWorkspace();    
			IPath location = Path.fromOSString(path); 
			file = workspace.getRoot().getFileForLocation(location);
						
		} catch (Throwable t) {
			throw new SpagoBIPluginException("Impossible to locate model file [" + path + "]");
		}
		return file;
	}
	
	private String getPersistenceUnitName(String modelPath, BusinessModel businessModel) {
		String persistenceUnitName;
		
		persistenceUnitName = null;
		try {
			IFile modelFile = getModelFile(modelPath);
			String modelDirectory = new File(modelPath).getParent();
			if ( isMappingDirty( modelFile ) ){
				persistenceUnitName = businessModel.getName() + "_" + System.currentTimeMillis();
				generateMapping(businessModel, modelDirectory, persistenceUnitName);
				//set the dirty property to false cause the mapping has just been created
				modelFile.setPersistentProperty(SpagoBIMetaConstants.DIRTY_MODEL, "false");
			}
			else {
				persistenceUnitName = discoverPersistenceUnitName(modelDirectory, businessModel.getName());
			}
		} catch (Throwable t) {
			throw new SpagoBIPluginException("Impossible to locate model file [" + "" + "]");
		}
		
		return persistenceUnitName;
	}
	
	private boolean isMappingDirty(IFile modelFile) {
		boolean isDirty;
		String isDirtyPropertyValue;
		
		isDirty = true;
		
		try {
			isDirtyPropertyValue = modelFile.getPersistentProperty(SpagoBIMetaConstants.DIRTY_MODEL);
			isDirty = "true".equalsIgnoreCase(isDirtyPropertyValue);
		} catch (Throwable t) {
			throw new SpagoBIPluginException("Impossible read the value of property [" + SpagoBIMetaConstants.DIRTY_MODEL + "]");
		}
		
		return isDirty;
	}
	
	public String discoverPersistenceUnitName(String modelDirectory, String modelName) {
		File persistenceFile = new File(modelDirectory+File.separatorChar+modelName+File.separatorChar+"src"+File.separatorChar+"META-INF","persistence.xml");
		String persistenceName = null;
		if (persistenceFile.exists()) {
			DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder;
			try {
				builder = domFactory.newDocumentBuilder();
		        Document document = builder.parse(persistenceFile);
				
				XPath xpath = XPathFactory.newInstance().newXPath();
		        XPathExpression expr = xpath.compile("/persistence/persistence-unit/@name");
		 
		        Object result = expr.evaluate(document, XPathConstants.STRING);
		        persistenceName = (String) result;
			} catch (ParserConfigurationException e) {
				logger.error("Impossible to retrieve Persistence Unit Name - ParserConfigurationException : [{}]",e);
				e.printStackTrace();
			} catch (SAXException e) {
				logger.error("Impossible to retrieve Persistence Unit Name - SAXException : [{}]",e);
				e.printStackTrace();
			} catch (IOException e) {
				logger.error("Impossible to retrieve Persistence Unit Name - IOException : [{}]",e);
				e.printStackTrace();
			} catch (XPathExpressionException e) {
				logger.error("Impossible to retrieve Persistence Unit Name - XPathExpressionException : [{}]",e);
				e.printStackTrace();
			}
		}
		return persistenceName;
	}
	
	public String getInputContents(IEditorInput editorInput) {
		String contents;
		
		contents = null;
		
		try {
			InputStream inputStream = openInputStream(editorInput);
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);
			
			String line = null;
			StringBuffer stringBuffer  = new StringBuffer();
			while((line = reader.readLine())!= null) {
				stringBuffer.append(line);
			}
			
			inputStreamReader.close();
			reader.close();
			inputStream.close();
			
			contents = stringBuffer.toString();
		} catch(Throwable t) {
			throw new SpagoBIPluginException("Impossible to read editor input [" + editorInput.getName() + "]", t);
		} finally {
			logger.trace("OUT");
		}
		
		return contents;
	}
	
	public InputStream openInputStream(IEditorInput editorInput) {
		InputStream inputStream;
		
		inputStream = null;
		
		try {
			if (editorInput instanceof FileEditorInput) { // input file is within eclipse workspace
				FileEditorInput fileEditorInput = (FileEditorInput)editorInput;
				inputStream = fileEditorInput.getFile().getContents();
			} else if(editorInput instanceof FileStoreEditorInput) { // input file is outside eclipse workspace
				FileStoreEditorInput fileStoreEditorInput = (FileStoreEditorInput)editorInput;
				URI fileStoreEditorInputURI = fileStoreEditorInput.getURI();
				FileInputStream fileInputStream = new FileInputStream(new File(fileStoreEditorInputURI));
				inputStream = new BufferedInputStream(fileInputStream);
			} else {
				throw new SpagoBIPluginException("Impossible to manage editor input of type [" + editorInput.getClass().getName() + "]");
			}
		} catch(Throwable t) {
			throw new SpagoBIPluginException("Impossible to open editor input [" + editorInput.getName() + "]", t);
		} finally {
			logger.trace("OUT");
		}
		
		return inputStream;
	}
	
	

	protected void createPages() {
		createEditPage();
		createResultsPage();

	}
	
	void createEditPage() {
		queryEditPage = new SpagoBIDataSetEditPage(getContainer(), queryBuilder);
		int index = addPage(queryEditPage);
		setPageText(index, "Edit");
	}
	
	void createResultsPage() {

		queryResultPage = new SpagoBIDataSetResultPage(getContainer(), queryBuilder);
		int index = addPage(queryResultPage);
		setPageText(index, "Results");
	}

	
	/**
	 * The <code>MultiPageEditorPart</code> implementation of this 
	 * <code>IWorkbenchPart</code> method disposes all nested editors.
	 * Subclasses may extend.
	 */
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		super.dispose();
	}

	
	public boolean isDirty() {
		return dirty;
	}
	
	public void setDirty(boolean value){
		dirty = value;
		logger.debug("Dirty state is [{}]",value);
	}
	
	public void fireDirty(){
		firePropertyChange(PROP_DIRTY);
		logger.debug("fireDirty executed");
	}
	
	
	/**
	 * Saves the multi-page editor's document.
	 */
	public void doSave(IProgressMonitor monitor) {
		Query query;
		JSONObject queryJSON;
		FileEditorInput fileEditorInput;
		String modelPath;
		
		logger.trace("IN");
		
		query = queryBuilder.getQuery();
		
		fileEditorInput = null;
		if (getEditorInput() instanceof FileEditorInput) {
			try {
				//Read the file to get the Model Path value
				fileEditorInput = (FileEditorInput)getEditorInput();
				InputStream inputStream = null;

				inputStream = fileEditorInput.getFile().getContents();
				InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
				BufferedReader reader = new BufferedReader(inputStreamReader);
				
				String line = null;
				StringBuffer stringBuffer  = new StringBuffer();
				while((line = reader.readLine())!= null) {
					stringBuffer.append(line);
				}
				inputStreamReader.close();
				reader.close();
				inputStream.close();
				String queryString = stringBuffer.toString();
				o = new JSONObject(queryString);

				JSONObject queryMeta = o.optJSONObject("queryMeta");
				modelPath = queryMeta.optString("modelPath");
				//******

				fileEditorInput = (FileEditorInput)getEditorInput();
				
				
				long UUID =System.currentTimeMillis();
				query.setId("q"+Long.valueOf(UUID).toString());
				//query.setId(fileEditorInput.getURI().toString());
				
				
				query.setDescription(fileEditorInput.getURI().toString());
				query.setDistinctClauseEnabled(false);
			} catch(CoreException e){
				throw new RuntimeException("Impossible to save query, CoreException", e);
			} catch (IOException e) {		
				throw new RuntimeException("Impossible to save query, IOException", e);
			} catch (JSONException e) {
				throw new RuntimeException("Impossible to save query, JSONException", e);
			}

		} else {
			throw new RuntimeException("Editor class [" + this.getClass().getName() + "] is unable to manage input of type [" + getEditorInput().getClass().getName() + "]");
		}
		
		queryJSON = null;
		try {
			queryJSON = (JSONObject)SerializerFactory.getSerializer("application/json").serialize(query, queryBuilder.getDataSource(), Locale.ENGLISH);
			logger.debug(queryJSON.toString());	
		} catch (SerializationException e) {
			throw new RuntimeException("Impossible to save query", e);
		}
		
	
		File file = new File(fileEditorInput.getURI());
		BufferedWriter out = null;
		ByteArrayInputStream in = null;
		try {
//			FileWriter fstream = new FileWriter(file);
//			out = new BufferedWriter(fstream);
//			out.write(queryJSON.toString(3));
			o.put("query", queryJSON);
		
			//Write model path inside query file
			JSONObject queryMeta = new JSONObject(); 
			o.put("queryMeta", queryMeta);
			queryMeta.put("modelPath",modelPath);
			
			in = new ByteArrayInputStream(o.toString(3).getBytes(fileEditorInput.getFile().getCharset()));
			fileEditorInput.getFile().setContents(in, true, true, monitor);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.flush();
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		//reset Dirty state
		setDirty(false);
		fireDirty();
		
		logger.trace("OUT");
	}
	/**
	 * Saves the multi-page editor's document as another file.
	 * Also updates the text for page 0's tab, and updates this multi-page editor's input
	 * to correspond to the nested editor's.
	 */
	public void doSaveAs() {
		logger.trace("IN");
		IEditorPart editor = getEditor(0);
		editor.doSaveAs();
		setPageText(0, editor.getTitle());
		setInput(editor.getEditorInput());
		logger.trace("OUT");
	}
	/* (non-Javadoc)
	 * Method declared on IEditorPart
	 */
	public void gotoMarker(IMarker marker) {
		setActivePage(0);
		IDE.gotoMarker(getEditor(0), marker);
	}
	
	/* (non-Javadoc)
	 * Method declared on IEditorPart.
	 */
	public boolean isSaveAsAllowed() {
		return true;
	}
	/**
	 * Calculates the contents of Result page when the it is activated.
	 */
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);
		if (newPageIndex == 1) {
			queryResultPage.refresh();
		}
	}
	
	protected BusinessModel loadBusinessModel(String modelPath) {
		BusinessModel businessModel;
		
		logger.trace("IN");
		
		businessModel = null;
		try {
			File modelFile = new File(modelPath);
			EmfXmiSerializer emfXmiSerializer = new EmfXmiSerializer();
			Model root = emfXmiSerializer.deserialize(modelFile);
			logger.debug("Model root is [{}] ",root );

			businessModel = root.getBusinessModels().get(0);
			logger.debug("Business Model name is [{}] ", businessModel.getName() );
		} catch(Throwable t) {
			throw new SpagoBIPluginException("Impossible to load model from file [" + modelPath + "]", t);
		} finally {
			logger.trace("OUT");
		}
		
		return businessModel;
	}
	
	
	protected void validateModel(BusinessModel businessModel) {
		ModelValidator modelValidator = new ModelValidator();
		if(modelValidator.validate(businessModel.getParentModel()) == false) {
			String message = "Model [" + businessModel.getName() + "] contains the following structural errors: ";
			for(String diagnosticMessage : modelValidator.getDiagnosticMessages()) {
				message += "\n - " + diagnosticMessage;
			}
			throw new SpagoBIPluginException(message);
			
		}
	}	
	
	public void generateMapping(BusinessModel businessModel, String outputFolder, String persistenceUnitName) {
	
		logger.trace("IN");
	
		try {
			JpaMappingJarGenerator generator = new JpaMappingJarGenerator();
			generator.setLibDir(new File("plugins"));
			generator.setPersistenceUnitName(persistenceUnitName);
			generator.generate(businessModel, outputFolder);
		} catch(Throwable t) {
			throw new SpagoBIPluginException("Impossible to generate mapping for business model [" + businessModel.getName() + "] into folder [" + outputFolder + "]", t);
		} finally {
			logger.trace("OUT");
		}
	}	
	
	public IDataSource createDataSource(String modelPath, BusinessModel businessModel, String persistenceUnitName){
		logger.debug("Creating datasource of [{}]",modelPath);
		
		PhysicalModel physicalModel = businessModel.getPhysicalModel();
		String connectionUrl = physicalModel.getProperties().get("connection.url").getValue();
		String connectionUsername = physicalModel.getProperties().get("connection.username").getValue();
		String connectionPassword = physicalModel.getProperties().get("connection.password").getValue();
		logger.debug("Datasource connection url is [{}]",connectionUrl);
		logger.debug("Datasource connection username is [{}]",connectionUsername);
		logger.debug("Datasource connection password is [{}]",connectionPassword);
		
		String dbname = physicalModel.getDatabaseName();
		logger.debug("Datasource database name is [{}]",dbname);
		
		// TODO read driver class from configuration file
		String dialect = null;
		String driver = null;
		if(dbname.toLowerCase().contains("oracle")) {
			dialect = "org.hibernate.dialect.OracleDialect";
			driver = "oracle.jdbc.OracleDriver";
		} else if(dbname.toLowerCase().contains("postgres")) {
			dialect = "org.hibernate.dialect.PostgreSQLDialect";
			driver = "org.postgresql.Driver";
		} else if(dbname.toLowerCase().contains("ingres")) {
			dialect = "org.hibernate.dialect.IngresDialect";
			driver = "com.ingres.jdbc.IngresDriver";
		} else if(dbname.toLowerCase().contains("microsoft")) {
			dialect = "org.hibernate.dialect.SQLServerDialect";
			driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		} else if(dbname.toLowerCase().contains("hsql")) {
			dialect = "org.hibernate.dialect.HSQLDialect";
			//driver = "org.hsqldb.jdbc.JDBCDriver";
			driver = "org.hsqldb.jdbcDriver";
			
		} else if(dbname.toLowerCase().contains("teradata")) {
			dialect = "org.hibernate.dialect.TeradataDialect";
			driver = "com.teradata.jdbc.TeraDriver";
		} else if(dbname.toLowerCase().contains("mysql")) {
			dialect = "org.hibernate.dialect.MySQLDialect";
			driver = "com.mysql.jdbc.Driver";
		} else {
			showInformation("Connection error", "impossible to find a suitable driver for [" + dbname + "] database");
		}
		
		
		Map<String,Object> dataSourceProperties = new HashMap<String,Object>();
		String modelName =  businessModel.getName();
		logger.debug("Datasource model name is [{}]",modelName);
		
		//Create Connection
		ConnectionDescriptor connectionDescriptor = new ConnectionDescriptor();			
		connectionDescriptor.setName( businessModel.getName());
		connectionDescriptor.setDialect( dialect );			
		connectionDescriptor.setDriverClass( driver );			
		connectionDescriptor.setPassword( connectionPassword );
		connectionDescriptor.setUrl( connectionUrl);
		connectionDescriptor.setUsername( connectionUsername );
		
		dataSourceProperties.put("connection", connectionDescriptor);
		
		String modelDirectory = new File(modelPath).getParent();
		List modelNames = new ArrayList();
		
		modelNames.add( persistenceUnitName );
		return OdaStructureBuilder.getDataSourceSingleModel(modelNames, dataSourceProperties,modelDirectory+File.separatorChar+"dist"+File.separatorChar);
	}
	
	/**
	 * Show an information dialog box.
	 */
	public void showInformation(final String title, final String message) {
	  Display.getDefault().asyncExec(new Runnable() {
	    @Override
	    public void run() {
	      MessageDialog.openInformation(null, title, message);
	    }
	  });
	}	
	
	
	/**
	 * Closes all project files on project close.
	 */
	public void resourceChanged(final IResourceChangeEvent event){
//		if(event.getType() == IResourceChangeEvent.PRE_CLOSE){
//			Display.getDefault().asyncExec(new Runnable(){
//				public void run(){
//					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
//					for (int i = 0; i<pages.length; i++){
//						if(((FileEditorInput)editor.getEditorInput()).getFile().getProject().equals(event.getResource())){
//							IEditorPart editorPart = pages[i].findEditor(editor.getEditorInput());
//							pages[i].closeEditor(editorPart,true);
//						}
//					}
//				}            
//			});
//		}
	}





	
}
