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
BirtDialogBase = Class.create( );

BirtDialogBase.prototype =
{
	__instance : null,
	__mask : null,
	__z_index : null,
	
	__neh_click_closure : null,
	__neh_resize_closure : null,
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	initialize : function( id )
	{
		this.__instance = $( id );
		this.__z_index = 200;
		
		if ( this.__instance )
		{
			this.__create_mask( );
		}
		
		// Closures
		this.__neh_click_closure = this.__neh_click.bindAsEventListener( this );
		this.__neh_resize_closure = this.__neh_resize.bindAsEventListener( this );
		
		this.__initialize( id );
	},
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	__initialize : function( )
	{
	},

	/**
	 *	Binding data to the dialog UI. Data includes zoom scaling factor.
	 *
	 *	@data, data DOM tree (schema TBD)
	 *	@return, void
	 */
	__cb_bind : function( data )
	{
		this.__bind( data );
		this.__l_show( );
	},

	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, toolbar id (optional since there is only one toolbar)
	 *	@return, void
	 */
	__cb_installEventHandlers : function( id )
	{
		// Initialise close button
		var oImg = this.__instance.getElementsByTagName( 'img' );
		Event.observe( oImg[0], 'click', this.__neh_click_closure, false );
		
		// Initialise ok and cancel buttons
		var oInputs = this.__instance.getElementsByTagName( 'input' );
		Event.observe( oInputs[oInputs.length - 2], 'click', this.__neh_click_closure, false );
		Event.observe( oInputs[oInputs.length - 1], 'click', this.__neh_click_closure, false );

		this.__installEventHandlers( id );
	},
	
	/**
	 *	Handle native event 'resize'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_resize : function( event )
	{
		BirtPosition.center( this.__instance );
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_click : function( event )
	{
		switch ( Event.element( event ).name )
		{
			case 'ok':
			{
				this.__okPress( );
				break;
			}
			case 'cancel':
			case 'close':
			{
				this.__l_hide( );
				break;
			}
		}
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__create_mask : function( )
	{
		// TODO: need to check browser. Iframe seems slow in firefox and div works there.
		this.__mask = this.__instance.ownerDocument.createElement( 'iframe' );
		document.body.appendChild( this.__mask );
		
		this.__mask.style.position = 'absolute';
		this.__mask.style.top = '0px';
		this.__mask.style.left = '0px';
		this.__mask.style.width = '100%';
		var height = BirtPosition.viewportHeight( );
		this.__mask.style.height = height + 'px';
		this.__mask.style.zIndex = '200';
		this.__mask.style.backgroundColor = '#dbe4ee';
		this.__mask.style.filter = 'alpha( opacity = 0.0 )';
		this.__mask.style.opacity = '.0';
		this.__mask.scrolling = 'no';
		this.__mask.marginHeight = '0px';
		this.__mask.marginWidth = '0px';
		this.__mask.style.display = 'none';
	},

	/**
	 *	Handle clicking on ok.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__okPress: function( )
	{
		this.__l_hide( );
	},

	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__l_show : function( )
	{
		this.__mask.style.zIndex = this.__z_index;
		this.__instance.style.zIndex = this.__z_index + 20;
		
		Element.show( this.__mask, this.__instance );
		BirtPosition.center( this.__instance );
		Event.observe( window, 'resize', this.__neh_resize_closure, false );
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__l_hide : function( )
	{
		Event.stopObserving( window, 'resize', this.__neh_resize_closure, false );
		Element.hide( this.__instance, this.__mask );
	}
}