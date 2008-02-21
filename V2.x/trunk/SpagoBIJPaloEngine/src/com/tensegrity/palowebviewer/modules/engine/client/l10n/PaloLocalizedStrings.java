package com.tensegrity.palowebviewer.modules.engine.client.l10n;

import com.google.gwt.i18n.client.Constants;

public interface PaloLocalizedStrings extends Constants{
	
	/**
	 * @gwt.key action.logon.hint
	 */
	public String actionLoginHint();
	
	/**
	 * @gwt.key action.logout.hint
	 */
	public String actionLogoutHint();
	
	/**
	 * @gwt.key action.reload_tree.hint
	 */
	public String actionReloadTreeHint();
	
	/**
	 * @gwt.key action.save.hint
	 */
	public String actionSaveHint();

	/**
	 * @gwt.key action.save_as_view.hint
	 */
	public String actionSaveAsHint();
	
	/**
	 * @gwt.key label.dimensions
	 */
	public String labelDimensions();

	/**
	 * @gwt.key label.drag_dimensions
	 */
	public String labelDragDimensions();

	/**
	 * @gwt.key error.internal_server_error
	 */
	public String internalServerError();
	
	/**
	 * @gwt.key error.invalid_login_or_password
	 */
	public String errorInvalidLoginOrPassword();

	
}
