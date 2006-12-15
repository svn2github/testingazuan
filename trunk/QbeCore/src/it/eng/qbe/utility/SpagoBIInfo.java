/**
 * 
 */
package it.eng.qbe.utility;

import java.io.Serializable;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBIInfo implements Serializable {
	private String templatePath;
	private String spagobiurl;
	private String user;
	
	public SpagoBIInfo(String templatePath, String spagobiurl, String user) {
		setTemplatePath(templatePath);
		setSpagobiurl(spagobiurl);
		setUser(user);
	}

	public String getSpagobiurl() {
		return spagobiurl;
	}

	private void setSpagobiurl(String spagobiurl) {
		this.spagobiurl = spagobiurl;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	private void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public String getUser() {
		return user;
	}

	private void setUser(String user) {
		this.user = user;
	}
}
