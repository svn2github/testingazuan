/**
 *
 *	LICENSE: see COPYING file
 *
**/
package it.eng.geo.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import it.eng.geo.configuration.Constants;
import it.eng.geo.render.MapRenderer;
import it.eng.spago.base.SourceBean;
import it.eng.spago.dispatching.action.AbstractHttpAction;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import sun.misc.BASE64Decoder;

/**
 * Classe <code>SessionExpiredAction</code> Gestione dell'avvenuta scadenza
 * della sessione
 * 
 * @author
 * @version 1.0
 */
public class GeoAction extends AbstractHttpAction {

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
	public void service(SourceBean serviceRequest, SourceBean serviceResponse)
			throws Exception {
		HttpServletResponse response = this.getHttpResponse();
		ServletOutputStream outputStream = response.getOutputStream();
		this.freezeHttpResponse();
		MapRenderer mapRenderer = new MapRenderer();
		String templateBase64Coded = (String) serviceRequest
				.getAttribute("template");
		BASE64Decoder bASE64Decoder = new BASE64Decoder();
		byte[] template = bASE64Decoder.decodeBuffer(templateBase64Coded);
		byte[] map = mapRenderer.renderMap(template);
		String contentType = mapRenderer.getContentType();
		response.setContentType(contentType);
		if (contentType.equalsIgnoreCase(Constants.JPEG_MIME_TYPE)) {
			InputStream inputStream = new ByteArrayInputStream(map);
			mapRenderer.sVGToJPEGTransform(inputStream, outputStream);
		} else if (contentType.equalsIgnoreCase(Constants.PDF_MIME_TYPE)) {
			// InputStream inputStream = new
			// ByteArrayInputStream(mapString.getBytes());
			// sVGToPDFTransform(inputStream, outputStream);
		} else {
			outputStream.write(map);
		}
	}
}