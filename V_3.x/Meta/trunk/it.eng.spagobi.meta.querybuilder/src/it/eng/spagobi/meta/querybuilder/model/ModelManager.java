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
package it.eng.spagobi.meta.querybuilder.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import it.eng.qbe.datasource.ConnectionDescriptor;
import it.eng.qbe.datasource.IDataSource;
import it.eng.spagobi.commons.exception.SpagoBIPluginException;
import it.eng.spagobi.meta.generator.GeneratorDescriptor;
import it.eng.spagobi.meta.generator.GeneratorFactory;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.validator.ModelValidator;
import it.eng.spagobi.meta.querybuilder.model.dao.IModelDAO;
import it.eng.spagobi.meta.querybuilder.model.dao.ModelDAOFileImpl;
import it.eng.spagobi.meta.querybuilder.oda.OdaStructureBuilder;
import it.eng.spagobi.meta.querybuilder.ui.editor.DatabaseConnectionManager;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class ModelManager {
	
	Object modelRefernce;	
	Model model;
	
	File mappingsFolder;
	

	private static Logger logger = LoggerFactory.getLogger(ModelManager.class);
	
	public ModelManager(Model model) {
		this.modelRefernce = model;
		this.model = model;
	}
	
	public ModelManager(File modelFile) {
		this.modelRefernce = modelFile;
	}
	
	public Model getModel() {
		if(model == null) {
			loadModel();
		}
		return model;
	}
	
	public PhysicalModel getPhysicalModel() {
		return getModel().getPhysicalModels().get(0);
	}
	
	public BusinessModel getBusinessModel() {
		return getModel().getBusinessModels().get(0);
	}
	
	public void loadModel() {
		if(modelRefernce instanceof File) {
			File modelFile = (File)modelRefernce;
			IModelDAO modelDAO = new ModelDAOFileImpl(modelFile.getParentFile());
			model = modelDAO.loadModel(modelFile.getName());
		}
	}
	
	public void validateModel() {
		ModelValidator modelValidator = new ModelValidator();
		if(modelValidator.validate(getModel()) == false) {
			String message = "Model [" + getModel().getName() + "] contains the following structural errors: ";
			for(String diagnosticMessage : modelValidator.getDiagnosticMessages()) {
				message += "\n - " + diagnosticMessage;
			}
			throw new SpagoBIPluginException(message);
		}
	}	
	
	public void generateMapping(File outputFolder) {
		setMappingsFolder(outputFolder);
		generateMapping();
	}
	
	public String generateMapping() {
		String persistenceUnitName = getBusinessModel().getName() + "_" + System.currentTimeMillis();
		return generateMapping(persistenceUnitName);
	}
	
	public String generateMapping(String persistenceUnitName) {
		
		logger.trace("IN");
		JpaMappingJarGenerator generator = null;
		try {
			Assert.assertNotNull("Impossible to generate mapping. Mapping folder is not set", getModelMappingFolder() );
			
			persistenceUnitName = getBusinessModel().getName() + "_" + System.currentTimeMillis();
			
			GeneratorDescriptor descriptor = GeneratorFactory.getGeneratorDescriptorById("it.eng.spagobi.meta.generator.jpamapping");
			generator = (JpaMappingJarGenerator)descriptor.getGenerator();
			generator.setLibDir(new File("plugins"));
			generator.setPersistenceUnitName(persistenceUnitName);
			generator.generate(getBusinessModel(), getMappingsFolder().toString());
		} catch(Throwable t) {
			throw new SpagoBIPluginException("Impossible to generate mapping for business model [" + getBusinessModel().getName() + "] into folder [" + getMappingsFolder() + "]", t);
		} finally {
			// finally block to hide technical folders created during generation
			if(generator != null){
				logger.debug("hide techical folders");
				generator.hideTechnicalResources();
			}
			logger.trace("OUT");
		}
		
		return persistenceUnitName;
	}
	
	public File getMappingsFolder() {
		if(mappingsFolder == null && modelRefernce instanceof File) {
			return ((File)modelRefernce).getParentFile();
		}
		return mappingsFolder;
	}
	
	public File getModelMappingFolder() {
		File mappingsFolder = getMappingsFolder();
		
		return mappingsFolder != null? new File(mappingsFolder, getBusinessModel().getName() ): null;
	}

	public void setMappingsFolder(File mappingsFolder) {
		this.mappingsFolder = mappingsFolder;
	}
	
	public IDataSource createDataSource() {
		return createDataSource( discoverPersistenceUnitName() );
	}
	
	public IDataSource createDataSource(String persistenceUnitName) {
		ConnectionDescriptor connectionDescriptor = getConnectionDescriptor();
		connectionDescriptor.setName( getBusinessModel().getName() );
		
		Map<String,Object> dataSourceProperties = new HashMap<String,Object>();
		dataSourceProperties.put("connection", connectionDescriptor);
		
		
		File mappingDistFolder = new File(getModelMappingFolder(), "dist");
		
		List<String> persistenceUnitNames = new ArrayList<String>();
		persistenceUnitNames.add( persistenceUnitName );
		
		return OdaStructureBuilder.getDataSourceSingleModel(persistenceUnitNames, dataSourceProperties, new File(mappingDistFolder, "datamart.jar"));
	}
	
	private String discoverPersistenceUnitName() {
		
		File mappingSrcFolder = new File(getModelMappingFolder(), "src");
		File mappingMetaInfFolder = new File(mappingSrcFolder, "META-INF");
		File persistenceFile = new File(mappingMetaInfFolder,"persistence.xml");
		
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
	
	private ConnectionDescriptor getConnectionDescriptor() {
		
		PhysicalModel physicalModel = getPhysicalModel();
		
		String databaseName = physicalModel.getDatabaseName();
		logger.debug("Datasource database name is [{}]", databaseName);
		
		List<String> missingProperties = new ArrayList<String>();
		
		String connectionUrl = getProperty(physicalModel, "connection.url");
		logger.debug("Datasource connection url is [{}]", connectionUrl);
		if(connectionUrl == null) missingProperties.add("connection.url");
		
		String connectionUsername = getProperty(physicalModel, "connection.username");
		logger.debug("Datasource connection username is [{}]",connectionUsername);
		if(connectionUsername == null) missingProperties.add("connection.username");
		
		String connectionPassword = getProperty(physicalModel, "connection.password");
		logger.debug("Datasource connection password is [{}]",connectionPassword);
		if(connectionPassword == null) missingProperties.add("connection.password");
		
		String connectionDriver = getProperty(physicalModel, "connection.driver");
		logger.debug("Datasource connection driver is [{}]",connectionDriver);
		if(connectionDriver == null) missingProperties.add("connection.driver");
		
		if(missingProperties.size() > 0) {
			throw new RuntimeException("Impossible to create connection descriptor because the following properties are not available in model [" + getBusinessModel().getName()+ "]" + missingProperties.toString());
		}
		
		DatabaseConnectionManager dcm = new DatabaseConnectionManager();
		String connectionDialect = dcm.getHibernateDialectClassName(databaseName);
		logger.debug("Datasource connection dialect is [{}]", connectionDialect);
		
		if(connectionDialect == null) {
			throw new RuntimeException("Impossible to create connection descriptor because it is not possible to find a suitable dialect for database [" + databaseName + "]");
		}
		
		
		//Create Connection
		ConnectionDescriptor connectionDescriptor = new ConnectionDescriptor();			
		connectionDescriptor.setName( getBusinessModel().getName());
		connectionDescriptor.setDialect( connectionDialect );			
		connectionDescriptor.setDriverClass( connectionDriver );			
		connectionDescriptor.setPassword( connectionPassword );
		connectionDescriptor.setUrl( connectionUrl);
		connectionDescriptor.setUsername( connectionUsername );
		
		return connectionDescriptor;
	}
	
	private String getProperty(PhysicalModel physicalModel, String propertyName) {
		ModelProperty property = physicalModel.getProperties().get(propertyName);
		return property!= null? property.getValue(): null;
	}
}
