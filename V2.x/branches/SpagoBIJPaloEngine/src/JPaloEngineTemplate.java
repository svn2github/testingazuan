

import it.eng.spago.base.SourceBean;

/**
 * @author Monica Franceschini
 *
 */
public class JPaloEngineTemplate {
	
	
	/**
	 * expected template structure:
	 *
	 * <olap database="Palo" 
     *  	 schema="Demo" 
     *		 cube="Sales">
	 * </olap>
	 */
	private SourceBean templateSB;
	
	private static final String DATABASE_ATTRIBUTE_NAME = "database";
	private static final String SCHEMA_ATTRIBUTE_NAME = "schema";
	private static final String CUBE_ATTRIBUTE_NAME = "cube";
	private static final String VIEW_NAME = "view";
	
	
	
	public JPaloEngineTemplate(SourceBean template) {
		setTemplateSB(template);
	}

	protected SourceBean getTemplateSB() {
		return templateSB;
	}

	protected void setTemplateSB(SourceBean templateSB) {
		this.templateSB = templateSB;
	}

	
	public String getDatabaseName() {	
		return (String)getTemplateSB().getAttribute( DATABASE_ATTRIBUTE_NAME );
	}
	
	public String getSchemaName() {		
		return (String)getTemplateSB().getAttribute( SCHEMA_ATTRIBUTE_NAME );
	}
	
	public String getCubeName() {		
		return (String)getTemplateSB().getAttribute( CUBE_ATTRIBUTE_NAME );
	}
	
	public String getViewName() {		
		return (String)getTemplateSB().getAttribute( VIEW_NAME );
	}
	
}
