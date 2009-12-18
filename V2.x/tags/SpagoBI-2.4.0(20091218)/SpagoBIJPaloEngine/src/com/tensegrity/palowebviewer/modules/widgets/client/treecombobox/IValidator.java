package com.tensegrity.palowebviewer.modules.widgets.client.treecombobox;

/**
 * Validates the value and if it's OK then sets it using {@link ISetter}.
 *
 */
public interface IValidator {
	
	void validateAndSet(Object value, ISetter setter);

}
