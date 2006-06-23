package it.eng.spagobi.validation;

import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.ResponseContainer;
import it.eng.spago.base.SessionContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.error.EMFErrorHandler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.TestCase;

public class SpagoBIValidationImplJUnitTest extends TestCase {

	private SourceBean request = null, response = null;
	
	private RequestContainer reqContainer = null;

	private ResponseContainer resContainer = null;
	
	private SpagoBIValidationImpl validationImpl = null;

	protected void setUp() throws Exception {
		try {
			request = new SourceBean("");
			response = new SourceBean("");
		} catch (SourceBeanException e1) {
			e1.printStackTrace();
			fail("Unaspected exception occurred!");
		}
		reqContainer = new RequestContainer();
		resContainer = new ResponseContainer();
		reqContainer.setServiceRequest(request);
		resContainer.setServiceResponse(response);
		SessionContainer session = new SessionContainer(true);
		reqContainer.setSessionContainer(session);
		resContainer.setErrorHandler(new EMFErrorHandler());

		validationImpl = new SpagoBIValidationImpl();
		super.setUp();
	}

	class Validation {
		public String field, condition;
		Validation(String condition, String field){
			this.condition = condition;
			this.field = field;
		}
		public String toString(){
			return "condizione = "+this.condition+", valore del campo = "+this.field;
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.validation.SpagoBIValidationImpl.validate(String, String,
	 * RequestContainer, ResponseContainer)'. Verifies that all single
	 * validation checks work correctly: TestPage1 service in validation.xml
	 * contains a validation block for each check type.
	 */
	public void testValidateTestPage1() {
		
		String serviceType = "PAGE";
		String serviceName = "TestPage1";
		boolean val = false;
		List correctList = new ArrayList();
		correctList.add(new Validation("MANDATORY","jrmygfmy"));
		correctList.add(new Validation("ALFANUMERIC","dk_fghS-DFS45hfgh"));
		correctList.add(new Validation("MINLENGTH","1234"));
		correctList.add(new Validation("URL","http://www.xhgfvhjyg.com/rhusj"));
		correctList.add(new Validation("NUMERIC","763"));
		correctList.add(new Validation("DECIMALS","5,123456789"));
		correctList.add(new Validation("MAXLENGTH","123456789"));
		correctList.add(new Validation("LETTERSTRING","evkDFSWEGRymjusevgyskvg"));
		correctList.add(new Validation("EMAIL","nome.cognome@dominio.abc"));
		correctList.add(new Validation("FISCALCODE","ZRBDVD83D20F657E"));
		correctList.add(new Validation("NUMERICRANGE","99.9999999999999999"));
		correctList.add(new Validation("DATERANGE","01/09/2004"));
		correctList.add(new Validation("STRINGRANGE","catania"));
		correctList.add(new Validation("REGEXP","righvgkgykSFSVSBVSDVASDVAS"));
		correctList.add(new Validation("DATE","23/02/1876"));
		correctList.add(new Validation("DATE","29/02/2004"));
		
		List incorrectList = new ArrayList();
		incorrectList.add(new Validation("MANDATORY","")); // non posso mettere un valore null nella request (che è un SourceBean)
		incorrectList.add(new Validation("ALFANUMERIC","dkf.ghSDFS45hfgh"));
		incorrectList.add(new Validation("ALFANUMERIC","22.3"));
		incorrectList.add(new Validation("ALFANUMERIC","22,3"));
		incorrectList.add(new Validation("MINLENGTH","12"));
		incorrectList.add(new Validation("URL","htttp://www.xhgfvhjyg.com/rhusj"));
		incorrectList.add(new Validation("URL","xhgfvhjyg.com/rhusj"));
		incorrectList.add(new Validation("URL","http:\\//www.xhgfvhjyg/rhusj"));
		incorrectList.add(new Validation("URL","http://www.xhgfvhjyg.com/rhusj\\"));
		incorrectList.add(new Validation("URL","http//wwww.xhgfvhjyg.com/rhusj"));
		incorrectList.add(new Validation("URL","/http://www.xhgfvhjyg.com/rhusj"));
		incorrectList.add(new Validation("NUMERIC","76f3"));
		incorrectList.add(new Validation("NUMERIC","5,4"));
		incorrectList.add(new Validation("DECIMALS","5,12345678901"));
		incorrectList.add(new Validation("MAXLENGTH","12345678901"));
		incorrectList.add(new Validation("LETTERSTRING","evkseg6ymjusevgyskvg"));
		incorrectList.add(new Validation("EMAIL","nome.cognome.dominio.com"));
		incorrectList.add(new Validation("EMAIL","http://nome.cognome@dominio.com"));
		incorrectList.add(new Validation("EMAIL","\\nome.cognome@dominio.com"));
		incorrectList.add(new Validation("EMAIL","nome.cognome@dominio"));
		incorrectList.add(new Validation("EMAIL","nome.cognome@dominio.dgerjygjygk"));
		incorrectList.add(new Validation("EMAIL","£%$/%!$/%@dominio.com"));
		incorrectList.add(new Validation("FISCALCODE","ZRBDVDD20F657E"));
		incorrectList.add(new Validation("FISCALCODE","zRBDVD83D20F657E"));
		incorrectList.add(new Validation("FISCALCODE","6ZRBDVD83D20F657E"));
		incorrectList.add(new Validation("FISCALCODE","ZRBDVD83ZZ20F657E"));
		incorrectList.add(new Validation("FISCALCODE","ZRB4DVD83D20F657E"));
		incorrectList.add(new Validation("FISCALCODE","ZRBDVD83D20F657EA"));
		incorrectList.add(new Validation("FISCALCODE","ZRBDVD863D20F657E"));
		incorrectList.add(new Validation("NUMERICRANGE","679"));
		incorrectList.add(new Validation("NUMERICRANGE","-1"));
		incorrectList.add(new Validation("NUMERICRANGE","50,5"));
		incorrectList.add(new Validation("DATERANGE","01/09/2006"));
		incorrectList.add(new Validation("STRINGRANGE","ciao"));
		incorrectList.add(new Validation("REGEXP","righvg6kgykSFSVSBVSDVASDVAS"));
		incorrectList.add(new Validation("DATE","1876/02/23"));
		incorrectList.add(new Validation("DATE","32/01/2000"));
		incorrectList.add(new Validation("DATE","23/13/2000"));
		incorrectList.add(new Validation("DATE","29/02/2005"));
		incorrectList.add(new Validation("DATE","23/02/2000000000"));
		incorrectList.add(new Validation("DATE","2a3/02/2000"));
		incorrectList.add(new Validation("DATE","23//02/2000"));
		incorrectList.add(new Validation("DATE","23/02/2000.5"));
		
		try {
			for (int i = 0; i < correctList.size(); i++){
				Validation validation = (Validation) correctList.get(i);
				request.updAttribute("MESSAGE", validation.condition);
				request.updAttribute("field", validation.field);
				val = validationImpl.validate(serviceType, serviceName,
						reqContainer, resContainer);
				assertTrue(val);
			}
		
			Collection errors = resContainer.getErrorHandler().getErrors();
			assertEquals(0,errors.size());
		
			for (int i = 0; i < incorrectList.size(); i++){
				Validation validation = (Validation) incorrectList.get(i);
				request.updAttribute("MESSAGE", validation.condition);
				request.updAttribute("field", validation.field);
				val = validationImpl.validate(serviceType, serviceName,
						reqContainer, resContainer);
				assertFalse(val);
				resContainer.setErrorHandler(new EMFErrorHandler());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
	}

	/**
	 * Test method for
	 * 'it.eng.spagobi.validation.SpagoBIValidationImpl.validate(String, String,
	 * RequestContainer, ResponseContainer)'. Verifies that more validation
	 * checks work correctly: TestPage2 service in validation.xml contains some
	 * complex validation blocks involving different check types.
	 */
	public void testValidateTestPage2() {

		String serviceType = "PAGE";
		String serviceName = "TestPage2";
		boolean val = false;
		List correctList = new ArrayList();
		correctList.add(new Validation("DATA_DI_NASCITA","25/11/1982"));
		correctList.add(new Validation("COMUNE_DI_NASCITA","Cinto Euganeo"));
		correctList.add(new Validation("IMPORTO_IN_EURO_SENZA_SIMBOLO","25"));
		correctList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO"," Euro 25,10"));
		correctList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","EURO 25,10"));
		correctList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","EURO 0,10"));
		correctList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","EURO 0"));
		correctList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","EURO 25"));
		correctList.add(new Validation("PAROLA_CHIAVE","abce"));

		List incorrectList = new ArrayList();
		incorrectList.add(new Validation("DATA_DI_NASCITA",""));
		incorrectList.add(new Validation("DATA_DI_NASCITA","25\11/2006"));
		incorrectList.add(new Validation("COMUNE_DI_NASCITA",""));
		incorrectList.add(new Validation("COMUNE_DI_NASCITA","/Cinto Euganeo"));
		incorrectList.add(new Validation("COMUNE_DI_NASCITA","Bo"));
		incorrectList.add(new Validation("COMUNE_DI_NASCITA","Cinto Euganeo in provincia di Padova"));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_SENZA_SIMBOLO",""));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_SENZA_SIMBOLO","€ 25"));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_SENZA_SIMBOLO","25,123"));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_SENZA_SIMBOLO","1000001"));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO",""));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","Euro 025,10"));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","euro 25,"));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","Euro 25,1"));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","Euro 025,10"));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","Euro 25,10,2"));
		incorrectList.add(new Validation("IMPORTO_IN_EURO_CON_SIMBOLO","Euro 25,10,02"));
		incorrectList.add(new Validation("PAROLA_CHIAVE","abcg"));
		
		try {
			for (int i = 0; i < correctList.size(); i++){
				Validation validation = (Validation) correctList.get(i);
				request.updAttribute("MESSAGE", validation.condition);
				request.updAttribute("field", validation.field);
				val = validationImpl.validate(serviceType, serviceName,
						reqContainer, resContainer);
				assertTrue(val);
				assertFalse(validationImpl.isBlocking());
			}
		
			Collection errors = resContainer.getErrorHandler().getErrors();
			assertEquals(0,errors.size());
		
			for (int i = 0; i < incorrectList.size(); i++){
				Validation validation = (Validation) incorrectList.get(i);
				request.updAttribute("MESSAGE", validation.condition);
				request.updAttribute("field", validation.field);
				val = validationImpl.validate(serviceType, serviceName,
						reqContainer, resContainer);
				assertFalse(val);
				assertFalse(validationImpl.isBlocking());
				resContainer.setErrorHandler(new EMFErrorHandler());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.validation.SpagoBIValidationImpl.validate(String, String,
	 * RequestContainer, ResponseContainer)'. Verifies that all single
	 * validation checks for different date formats work correctly: TestPage3
	 * service in validation.xml contains a validation block for each check
	 * type.
	 */
	public void testValidateTestPage3() {

		String serviceType = "PAGE";
		String serviceName = "TestPage3";
		boolean val = false;
		
		List correctList = new ArrayList();
//		date format MM/dd/yyyy
		correctList.add(new Validation("DATE_MM/dd/yyyy","12/13/2000"));
		correctList.add(new Validation("DATE_MM/dd/yyyy","12/31/2004"));
		correctList.add(new Validation("DATE_MM/dd/yyyy","02/29/2004"));
//		date format yyyy/MM/dd
		correctList.add(new Validation("DATE_yyyy/MM/dd","2004/02/29"));
		correctList.add(new Validation("DATE_yyyy/MM/dd","2004/12/31"));
		correctList.add(new Validation("DATE_yyyy/MM/dd","2004/12/02"));
//		date format yyyy/dd/MM
		correctList.add(new Validation("DATE_yyyy/dd/MM","2004/29/02"));
		correctList.add(new Validation("DATE_yyyy/dd/MM","2004/31/12"));
		correctList.add(new Validation("DATE_yyyy/dd/MM","2004/02/12"));
//		date format MM.dd.yyyy
		correctList.add(new Validation("DATE_MM.dd.yyyy","12.13.2000"));
		correctList.add(new Validation("DATE_MM.dd.yyyy","12.31.2004"));
		correctList.add(new Validation("DATE_MM.dd.yyyy","02.29.2004"));
//		date format yyyy/dd-MM
		correctList.add(new Validation("DATE_yyyy/dd-MM","2004/29-02"));
		correctList.add(new Validation("DATE_yyyy/dd-MM","2004/31-12"));
		correctList.add(new Validation("DATE_yyyy/dd-MM","2004/02-12"));
		
		List incorrectList = new ArrayList();
//		date format MM/dd/yyyy
		incorrectList.add(new Validation("DATE_MM/dd/yyyy","23/02/1876"));
		incorrectList.add(new Validation("DATE_MM/dd/yyyy","01/32/2000"));
		incorrectList.add(new Validation("DATE_MM/dd/yyyy","23/13/2000"));
		incorrectList.add(new Validation("DATE_MM/dd/yyyy","02/29/2005"));
		incorrectList.add(new Validation("DATE_MM/dd/yyyy","2/29/2005"));
		incorrectList.add(new Validation("DATE_MM/dd/yyyy","02/02/2000000000"));
		incorrectList.add(new Validation("DATE_MM/dd/yyyy","0a2/23/2000"));
		incorrectList.add(new Validation("DATE_MM/dd/yyyy","02//23/2000"));
		incorrectList.add(new Validation("DATE_MM/dd/yyyy","02/23/2000.5"));
//		date format yyyy/MM/dd
		incorrectList.add(new Validation("DATE_yyyy/MM/dd","02/29/2004"));
		incorrectList.add(new Validation("DATE_yyyy/MM/dd","2004/13/31"));
		incorrectList.add(new Validation("DATE_yyyy/MM/dd","2004/12/32"));
		incorrectList.add(new Validation("DATE_yyyy/MM/dd","2005/02/29"));
		incorrectList.add(new Validation("DATE_yyyy/MM/dd","2004/2/12"));
		incorrectList.add(new Validation("DATE_yyyy/MM/dd","2000000000004/12/02"));
		incorrectList.add(new Validation("DATE_yyyy/MM/dd","2004/0a2/29"));
		incorrectList.add(new Validation("DATE_yyyy/MM/dd","2004/12//31"));
		incorrectList.add(new Validation("DATE_yyyy/MM/dd","2004.5/12/02"));
//		date format yyyy/dd/MM
		incorrectList.add(new Validation("DATE_yyyy/dd/MM","02/29/2004"));
		incorrectList.add(new Validation("DATE_yyyy/dd/MM","2004/31/13"));
		incorrectList.add(new Validation("DATE_yyyy/dd/MM","2004/32/12"));
		incorrectList.add(new Validation("DATE_yyyy/dd/MM","2005/29/02"));
		incorrectList.add(new Validation("DATE_yyyy/dd/MM","2004/12/2"));
		incorrectList.add(new Validation("DATE_yyyy/dd/MM","2000000000004/02/12"));
		incorrectList.add(new Validation("DATE_yyyy/dd/MM","2004/0a2/10"));
		incorrectList.add(new Validation("DATE_yyyy/dd/MM","2004/12//11"));
		incorrectList.add(new Validation("DATE_yyyy/dd/MM","2004.5/12/02"));
//		date format MM.dd.yyyy
		incorrectList.add(new Validation("DATE_MM.dd.yyyy","23.02.1876"));
		incorrectList.add(new Validation("DATE_MM.dd.yyyy","01.32.2000"));
		incorrectList.add(new Validation("DATE_MM.dd.yyyy","23.13.2000"));
		incorrectList.add(new Validation("DATE_MM.dd.yyyy","02.29.2005"));
		incorrectList.add(new Validation("DATE_MM.dd.yyyy","2.29.2005"));
		incorrectList.add(new Validation("DATE_MM.dd.yyyy","02.02.2000000000"));
		incorrectList.add(new Validation("DATE_MM.dd.yyyy","0a2.23.2000"));
		incorrectList.add(new Validation("DATE_MM.dd.yyyy","02..23.2000"));
		incorrectList.add(new Validation("DATE_MM.dd.yyyy","02.23.2000.5"));
//		date format yyyy/dd-MM
		incorrectList.add(new Validation("DATE_yyyy/dd-MM","02-29/2004"));
		incorrectList.add(new Validation("DATE_yyyy/dd-MM","2004/31-13"));
		incorrectList.add(new Validation("DATE_yyyy/dd-MM","2004/32-12"));
		incorrectList.add(new Validation("DATE_yyyy/dd-MM","2005/29-02"));
		incorrectList.add(new Validation("DATE_yyyy/dd-MM","2004/12-2"));
		incorrectList.add(new Validation("DATE_yyyy/dd-MM","2000000000004/02-12"));
		incorrectList.add(new Validation("DATE_yyyy/dd-MM","2004/0a2-10"));
		incorrectList.add(new Validation("DATE_yyyy/dd-MM","2004/12--11"));
		incorrectList.add(new Validation("DATE_yyyy/dd-MM","2004.5/12-02"));
		
		try {
			for (int i = 0; i < correctList.size(); i++){
				Validation validation = (Validation) correctList.get(i);
				request.updAttribute("MESSAGE", validation.condition);
				request.updAttribute("field", validation.field);
				val = validationImpl.validate(serviceType, serviceName,
						reqContainer, resContainer);
				assertTrue(val);
			}
		
			Collection errors = resContainer.getErrorHandler().getErrors();
			assertEquals(0,errors.size());
		
			for (int i = 0; i < incorrectList.size(); i++){
				Validation validation = (Validation) incorrectList.get(i);
				request.updAttribute("MESSAGE", validation.condition);
				request.updAttribute("field", validation.field);
				val = validationImpl.validate(serviceType, serviceName,
						reqContainer, resContainer);
				assertFalse(val);
				resContainer.setErrorHandler(new EMFErrorHandler());
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
	}
	
	/**
	 * Test method for
	 * 'it.eng.spagobi.validation.SpagoBIValidationImpl.isBlocking()'. Verifies
	 * if a validation block is blocking as expected (it refers to TestPage1
	 * service in validation.xml).
	 */
	public void testIsBlocking() {
		String serviceType = "PAGE";
		String serviceName = "TestPage1";
		List correctList = new ArrayList();
		correctList.add(new Validation("MANDATORY","jrmygfmy"));
		correctList.add(new Validation("ALFANUMERIC","dk_fghS-DFS45hfgh"));
		correctList.add(new Validation("MINLENGTH","1234"));
		correctList.add(new Validation("URL","http://www.xhgfvhjyg.com/rhusj"));
		correctList.add(new Validation("NUMERIC","763"));
		correctList.add(new Validation("DECIMALS","5,123456789"));
		correctList.add(new Validation("MAXLENGTH","123456789"));
		correctList.add(new Validation("LETTERSTRING","evkDFSWEGRymjusevgyskvg"));
		correctList.add(new Validation("EMAIL","nome.cognome@dominio.abc"));
		correctList.add(new Validation("FISCALCODE","ZRBDVD83D20F657E"));
		correctList.add(new Validation("NUMERICRANGE","99.9999999999999999"));
		correctList.add(new Validation("DATERANGE","01/09/2004"));
		correctList.add(new Validation("STRINGRANGE","ciao"));
		correctList.add(new Validation("REGEXP","righvgkgykSFSVSBVSDVASDVAS"));
		correctList.add(new Validation("DATE","23/02/1876"));

		try {
			for (int i = 0; i < correctList.size(); i++){
				Validation validation = (Validation) correctList.get(i);
				request.updAttribute("MESSAGE", validation.condition);
				request.updAttribute("field", validation.field);
				validationImpl.validate(serviceType, serviceName,
						reqContainer, resContainer);
				assertTrue(validationImpl.isBlocking());
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Unaspected exception occurred!");
		}
	}

}
