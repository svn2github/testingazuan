package com.tensegrity.palowebviewer.modules.ui.client.cubetable;

import com.tensegrity.palowebviewer.modules.paloclient.client.IElementType;
import com.tensegrity.palowebviewer.modules.paloclient.client.misc.IResultElement;

/**
 * This class formats data for client view
 * @author dmol
 *
 */
public interface IFormatter {
	public String format(IResultElement value);
	
	public IResultElement unformat(String value, IElementType type) throws NumberFormatException;
}
