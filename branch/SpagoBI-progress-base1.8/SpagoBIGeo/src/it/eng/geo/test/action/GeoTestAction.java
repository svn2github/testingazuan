package it.eng.geo.test.action;

import it.eng.geo.render.MapRenderer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractAction;
import sun.misc.BASE64Decoder;
/**
 * Classe <code>SessionExpiredAction</code> Gestione dell'avvenuta scadenza
 * della sessione
 * 
 * @author
 * @version 1.0
 */
public class GeoTestAction extends AbstractAction {

    /**
     * <code>service</code> Metodo contenente la logica per l'esecuzione del
     * comando
     * 
     * @param arg0
     *            di tipo <code>SourceBean</code> non usato
     * @param arg1
     *            di tipo <code>SourceBean</code> non usato
     * @exception Exception
     *                Se si sono verificati problemi, in realta' usato per
     *                forzare l'uso del blocco try-catch
     */
    public void service(SourceBean serviceRequest, SourceBean serviceResponse) throws Exception {

        MapRenderer mapRenderer = new MapRenderer();
        String templateBase64Coded = (String) serviceRequest.getAttribute("template");
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] template = bASE64Decoder.decodeBuffer(templateBase64Coded);
		byte[] output = null;
        try {
        	output = mapRenderer.renderMap(template);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        serviceResponse.setAttribute("risposta_servizio", output);
    }
}