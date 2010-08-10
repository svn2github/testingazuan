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

package it.eng.spago.error;

import it.eng.spago.base.CloneableObject;
import it.eng.spago.base.Constants;
import it.eng.spago.base.RequestContainer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.base.SourceBeanException;
import it.eng.spago.tracing.TracerSingleton;
import it.eng.spagobi.utilities.PortletUtilities;

import java.io.Serializable;
import java.util.Vector;

import javax.portlet.PortletRequest;

/**
* Un'istanza di <code>EMFUserError</code> rappresenta un errore codificato. Questo significa che esiste un
* repository contenente il riferimento a questo errore e la sua descrizione.
* @version 1.0, 06/03/2002
* @author Luigi Bellio
* @see EMFErrorHandler
*/
public class EMFUserError extends EMFAbstractError implements Serializable {
public static final String USER_ERROR_ELEMENT = "USER_ERROR";
public static final String USER_ERROR_CODE = "CODE";
private int _code = 0;
private Vector _params = null;

/**
 * Costruisce un oggetto di tipo <code>EMFUserError</code> identificandolo  tramite  una severity e un
 * codice di errore .
 * @param severity severity dell'errore.
 * @param code codice di errore.
 */
public EMFUserError(String severity, int code) {
    super();
    init(severity, code, null, null);
} // public EMFUserError(String severity, int code)

/**
 * Costruisce un oggetto di tipo <code>EMFUserError</code> identificandolo  tramite  una severity ,un
 * codice di errore e una collezione di parametri che andranno a sostituire i caratteri <em>%</em> nella
 * stringa di descrizione.
 * @param severity severity dell'errore.
 * @param code codice di errore.
 * @param params vettore di parametri che  verranno inseriti nella stringa di descrizione.
 */
public EMFUserError(String severity, int code, Vector params) {
    super();
    init(severity, code, params, null);
} // public EMFUserError(String severity, int code, Vector params)

/**
 * Costruisce un oggetto di tipo <code>EMFUserError</code> identificandolo  tramite  una severity ,un
 * codice di errore , una collezione di parametri che andranno a sostituire i caratteri <em>%</em> nella
 * stringa di descrizione e un oggetto di qualsiasi natura.
 * @param severity severity dell'errore.
 * @param code codice di errore.
 * @param params vettore di parametri che  verranno inseriti nella stringa di descrizione.
 * @param additionalInfo oggetto di qualsiasi natura.
 */
public EMFUserError(String severity, int code, Vector params, Object additionalInfo) {
    super();
    init(severity, code, params, additionalInfo);
} // public EMFUserError(String severity, int code, Vector params, Object additionalInfo)

/**
 * Costruisce un oggetto di tipo <code>EMFUserError</code> utilizzando lo stato del parametro
 * in input .
 * @param EMFUserError oggetto della stessa classe.
 */
public EMFUserError(EMFUserError userError) {
    super(userError);
    _code = userError._code;
} // public EMFUserError(EMFUserError userError)

/**
 * Ritorna un clone dell'oggetto stesso.
 * @return CloneableObject  il clone dell'oggetto.
 * @see CloneableObject
 */
public CloneableObject cloneObject() {
    return new EMFUserError(this);
} // public CloneableObject cloneObject()

/**
 * Questo metodo ha il compito di inizializzare lo stato dell'oggetto,viene invocato da tutti i costruttori
 * di <code>EMFUserError</code>.
 */
private void init(String severity, int code, Vector params, Object additionalInfo) {
    TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "EMFUserError::init: invocato");
    setSeverity(severity);
    TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG,
        "EMFUserError::init: severity [" + getSeverity() + "]");
    _code = code;
    _params = params;
    TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG, "EMFUserError::init: code [" + code + "]");
    //String description = MessageBundle.getMessage(String.valueOf(code));
    
    PortletRequest portReq = null;
    String description = null;
   	//portReq = PortletUtilities.getPortletRequest();
   	description = PortletUtilities.getMessage(String.valueOf(code), "messages");

    setDescription(description);
    if ((params != null) && (description != null)) {
        for (int i = 0; i < params.size(); i++) {
            String toParse = description;
            String replacing = "%" + i;
            String replaced = (String)params.elementAt(i);
            StringBuffer parsed = new StringBuffer();
            int parameterIndex = toParse.indexOf(replacing);
            while (parameterIndex != -1) {
                parsed.append(toParse.substring(0, parameterIndex));
                parsed.append(replaced);
                toParse = toParse.substring(parameterIndex + replacing.length(), toParse.length());
                parameterIndex = toParse.indexOf(replacing);
            } // while (parameterIndex != -1)
            parsed.append(toParse);
            description = parsed.toString();
        } // for (int i = 0; i < params.size(); i++)
        setDescription(description);
    } // if ((params != null) && (description != null))
    TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.DEBUG,
        "EMFUserError::init: description [" + getDescription() + "]");
    setAdditionalInfo(additionalInfo);
} // private void init(String severity, int code, Vector params, Object additionalInfo)

/**
 * Ritorna il codice dell'errore.
 * <p>
 * @return <em>int</em> codice dell'errore.
 */
public int getCode() {
    return _code;
} // public Integer getCode()


/**
 * Ritorna il vettore dei parametri.
 * <p>
 * @return <em>Vector</em> vettore parametri.
 */
public Vector getParams() {
	return _params;
}

/**
 * Ritorna un <code>SourceBean</code> popolato con gli attributi dell'oggetto.
 * @return <code>SourceBean</code> contenente gli attributi dell'oggetto.
 * @see SourceBean
 */
public SourceBean getSourceBean() {
    SourceBean errorBean = null;
    try {
        errorBean = new SourceBean(USER_ERROR_ELEMENT);
        errorBean.setAttribute(EMFAbstractError.ERROR_SEVERITY, getSeverity());
        errorBean.setAttribute(USER_ERROR_CODE, String.valueOf(_code));
        errorBean.setAttribute(EMFAbstractError.ERROR_DESCRIPTION, getDescription());
        Object additionalInfo = getAdditionalInfo();
        if (additionalInfo != null)
            errorBean.setAttribute(EMFAbstractError.ERROR_ADDITIONAL_INFO, additionalInfo);
    } // try
    catch (SourceBeanException ex) {
        TracerSingleton.log(Constants.NOME_MODULO, TracerSingleton.CRITICAL, "EMFUserError::getSourceBean:", ex);
    } // catch (SourceBeanException ex) try
    return errorBean;
} // public SourceBean getSourceBean()
} // public class EMFUserError extends EMFAbstractError implements Serializable