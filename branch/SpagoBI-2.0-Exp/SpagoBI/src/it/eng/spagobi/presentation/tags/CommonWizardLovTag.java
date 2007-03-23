package it.eng.spagobi.presentation.tags;

import it.eng.spagobi.utilities.PortletUtilities;

import javax.servlet.jsp.tagext.TagSupport;

public class CommonWizardLovTag extends TagSupport {

	protected String generateProfAttrTitleSection(String urlImg) {
		StringBuffer output = new StringBuffer();
		output.append("		<td class='titlebar_level_2_empty_section'>&nbsp;</td>\n");
		output.append("		<td class='titlebar_level_2_button_section'>\n");
		output.append("			<a style='text-decoration:none;' href='javascript:opencloseProfileAttributeWin()'> \n");
		output.append("				<img width='22px' height='22px'\n");
		output.append("				 	 src='" + urlImg +"'\n");
		output.append("					 name='info'\n");
		output.append("					 alt='"+PortletUtilities.getMessage("SBIDev.lov.avaiableProfAttr", "messages")+"'\n");
		output.append("					 title='"+PortletUtilities.getMessage("SBIDev.lov.avaiableProfAttr", "messages")+"'/>\n");
		output.append("			</a>\n");
		output.append("		</td>\n");
		String outputStr = output.toString();
		return outputStr;
	}
	
}
