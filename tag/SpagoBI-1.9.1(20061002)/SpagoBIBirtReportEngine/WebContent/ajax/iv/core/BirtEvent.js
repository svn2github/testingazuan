/******************************************************************************
 *	Copyright (c) 2004 Actuate Corporation and others.
 *	All rights reserved. This program and the accompanying materials 
 *	are made available under the terms of the Eclipse Public License v1.0
 *	which accompanies this distribution, and is available at
 *		http://www.eclipse.org/legal/epl-v10.html
 *	
 *	Contributors:
 *		Actuate Corporation - Initial implementation.
 *****************************************************************************/
 
/**
 *	BirtEvent
 *	...
 */
BirtEvent = Class.create( );

BirtEvent.prototype =
{
	__E_GETPAGE : '__E_GETPAGE',					// Getting designated page.

	__E_BLUR : '__E_BLUR',							// Blur current selection.

	__E_SORT : '__E_SORT',							// Collapse/expand UI component.
	__E_COLLAPSEEXPAND : '__E_COLLAPSEEXPAND',		// Collapse/expand UI component.
	
	__E_PRINT : '__E_PRINT',						// Print event.
	
	__E_QUERY_EXPORT : '__E_QUERY_EXPORT',
	__E_EXPORT : '__E_EXPORT',						// Export event

	__E_QUERY_FONT : '__E_QUERY_FONT',				// Query font event.
	
	__E_ACTIONADDCOL : '__E_ACTIONADDCOL',
	__E_ACTIONDELETECOL : '__E_ACTIONDELETECOL',

	__E_SHOW_TOC : '__E_SHOW_TOC',
	__E_TOC : '__E_TOC',
	__E_TOC_IMAGE_CLICK : '__E_TOC_IMAGE_CLICK',
	__E_PARAMETER : '__E_PARAMETER',
	
	__E_CHANGE_PARAMETER : '__E_CHANGE_PARAMETER',  //Change parameter event.
	__E_CASCADING_PARAMETER : '__E_CASCADING_PARAMETER',  //Cascading parameter event.
	
	__E_SHOW_COMPONENT : '__E_SHOW_COMPONENT',		//UI component event.
	__E_HIDE_COMPONENT : '__E_HIDE_COMPONENT',
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	Define available birt events.
	 *
	 *	@return, void
	 */
	initialize: function( )
	{
	}
}

var birtEvent = new BirtEvent( );