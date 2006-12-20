/**
 * 
 */
package it.eng.qbe.utility;

import java.io.Serializable;
import java.util.Locale;

/**
 * @author Andrea Gioia
 *
 */
public class SpagoBIInfo implements Serializable {
	private String templatePath;
	private String spagobiurl;
	private String user;
	private Locale loacale;
	
	public SpagoBIInfo(String templatePath, String spagobiurl, String user, Locale locale) {
		setTemplatePath(templatePath);
		setSpagobiurl(spagobiurl);
		setUser(user);
		setLocale(locale);
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

	public Locale getLoacale() {
		return this.loacale;
	}

	
	private void setLocale(Locale locale) {
		this.loacale = locale;
	}
}
