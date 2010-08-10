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
 *	Birt export report dialog.
 */
BirtExportReportDialog = Class.create( );

BirtExportReportDialog.prototype = Object.extend( new BirtDialogBase( ),
{
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	__initialize: function( id )
	{
		this.__cb_installEventHandlers( id );
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__installEventHandlers : function( id )
	{
	},
	
	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__bind: function( data )
	{
	},
	
	/**
	 *	Handle clicking on ok.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__okPress: function( )
	{
		alert( 'Export report' );
	}
} );