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
 *	Birt error dialog.
 */
BirtExceptionDialog = Class.create( );

BirtExceptionDialog.prototype = Object.extend( new BirtDialogBase( ),
{
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	__initialize: function( id )
	{
		this.__cb_installEventHandlers( id );
		this.__z_index = 300;
	},
	
	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__bind: function( data )
	{
	 	if( !data )
	 	{
	 		return;
	 	}
	 	
	 	var faultCodes = data.getElementsByTagName( 'faultcode' );
	 	var faultStrings = data.getElementsByTagName( 'faultstring' );
	 	var oSpans = this.__instance.getElementsByTagName( 'SPAN' );
	 	if ( faultCodes[0] && faultCodes[0].firstChild )
	 	{
			oSpans[0].innerHTML = faultCodes[0].firstChild.data;
		}
		else
		{
			oSpans[0].innerHTML = "";
		}
	 	if ( faultStrings[0] && faultStrings[0].firstChild )
	 	{
			oSpans[1].innerHTML = faultStrings[0].firstChild.data;
		}
		else
		{
			oSpans[1].innerHTML = "";
		}
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__installEventHandlers : function( id )
	{
	}
} );