/**
 * 
 * LICENSE: see LICENSE.html file
 * 
 */
package it.eng.spagobi.analysis;

import it.eng.spagobi.bean.SaveAnalysisBean;

import com.tonbeller.wcf.component.Component;
import com.tonbeller.wcf.component.ComponentTag;
import com.tonbeller.wcf.controller.RequestContext;

public class SaveAnalysisTag extends ComponentTag {

  /**
   * creates a Print Component
   */
  public Component createComponent(RequestContext context) throws Exception {
	return new SaveAnalysisBean(id, context);
  }
  
}
