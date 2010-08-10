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
 *	BirtDialogBase
 *	...
 */
BirtFontDialog = Class.create( );

BirtFontDialog.prototype = Object.extend( new BirtDialogBase( ),
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
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__bind: function( data )
	{
		if ( !data ) return;
		
		var oInputs = this.__instance.getElementsByTagName( 'input' );
		var oSelects = this.__instance.getElementsByTagName( 'select' );
		
		var oProperties = data.getElementsByTagName( 'Family' );
		if ( oProperties )
		{
			oInputs['family'].value = oProperties[0].firstChild.data;
			oSelects['familys'].value = oProperties[0].firstChild.data;
		}
		
		oProperties = data.getElementsByTagName( 'Style' );
		if ( oProperties )
		{
			oInputs['style'].value = oProperties[0].firstChild.data;
			oSelects['styles'].value = oProperties[0].firstChild.data;
		}
		
		oProperties = data.getElementsByTagName( 'Size' );
		if ( oProperties )
		{
			oInputs['size'].value = oProperties[0].firstChild.data;
			oSelects['sizes'].value = oProperties[0].firstChild.data;
		}
		
		oProperties = data.getElementsByTagName( 'Effect' );
		if ( oProperties )
		{
			oInputs['effect'].value = oProperties[0].firstChild.data;
		}
	},

	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__installEventHandlers: function( id )
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
		alert('ok');
	}
} );