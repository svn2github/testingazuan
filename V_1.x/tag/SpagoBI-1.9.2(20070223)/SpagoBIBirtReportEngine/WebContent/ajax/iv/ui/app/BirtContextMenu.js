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
 *	BirtContextMenu
 *	...
 */
BirtContextMenu = Class.create( );

BirtContextMenu.prototype = Object.extend( new BirtAppUIComponentBase( ),
{
	__contentRoot : null,
	
	/**
	 *	Mask used for context menu.
	 */
	__mask : null,
	
	/**
	 *	Event handler closures.
	 */
	__neh_click_closure : null,
	__neh_mousemove_closure : null,
	__neh_click_mask_closure : null,
		
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *
	 *	@return, void
	 */
	__initialize : function( id )
	{
		var oTDs = this.__instance.getElementsByTagName( 'td' );
		this.__contentRoot = oTDs[1];
		
		this.__mask = this.__create_mask( );
		
//		document.oncontextmenu = function( ) { return false };

		this.__neh_click_closure = this.__neh_click.bindAsEventListener( this );
		this.__neh_mousemove_closure = this.__neh_mousemove.bindAsEventListener( this );
		this.__neh_click_mask_closure = this.__neh_click_mask.bindAsEventListener( this );
		
		this.__cb_installEventHandlers( );
	},

	////////////////////////////////////////////////////////////////////////////
	//	Callback functions

	/**
	 *	Binding data to the context menu UI. Data includes context menu items.
	 *
	 *	@data, data DOM tree (Schema TBD)
	 *	@return, void
	 */
	__cb_bind : function( event, data )
	{
		var outer = this.__contentRoot.ownerDocument.createElement( "div" );
		this.__contentRoot.appendChild( outer );

		var div, text;
		var name, action, target;
		
		for( var i in data )
		{
			if( data[i].isLine )
			{
				div = this.__contentRoot.ownerDocument.createElement( 'hr' );
				outer.appendChild( div );
			}
			else
			{
				name = data[i].displayName;
				action = data[i].action;
				target = data[i].target;
				
				div = this.__contentRoot.ownerDocument.createElement( "div" );
				div.style.fontFamily = "Arial";
				div.style.fontSize = "8pt";
				div.style.height = "15px";
				div.style.padding = "2px";
				
				text = this.__contentRoot.ownerDocument.createTextNode( name );
				div.appendChild( text );
				outer.appendChild( div );
				
				if( data[i].inactive )
				{
					div.style.color = "#888888";
				}
				else
				{
					Event.observe( div , 'mouseover', this.__neh_mousemove_closure, false );
					Event.observe( div , 'mouseout', this.__neh_mousemove_closure, false );
					Event.observe( div , 'click', this.__neh_click_closure, false );
				}
			}
		}
		
		var xLoc = Event.pointerX( event );
		var yLoc = Event.pointerY( event );
		
		// Need a better way to calculate position.
		this.__instance.style.left = xLoc;
		this.__instance.style.top = yLoc;
		
		Element.show( this.__instance, this.__mask );
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, context menu id (optional since there is only one nav bar)
	 *	@return, void
	 */
	__cb_installEventHandlers : function( )
	{
		Event.observe( this.__mask, 'click', this.__neh_click_mask_closure, false );
	},
	
	/**
	 *	Unregister any birt event handlers.
	 *
	 *	@id, object id
	 *	@return, void
	 */
	__cb_disposeEventHandlers : function( )
	{
		Event.stopObserving( this.__mask, 'click', this.__neh_click_mask_closure, false );
	},

	////////////////////////////////////////////////////////////////////////////
	//	Native event handlers

	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_click : function( event )
	{
		var oDiv = Event.element( event );
		
		switch ( oDiv.firstChild.data )
		{
			case 'Hide Element':
			{
				birtEventDispatcher.fireEvent( '__E_HIDE_COMPONENT' );
				break;
			}
			case 'Show Element':
			{
				birtEventDispatcher.fireEvent( '__E_SHOW_COMPONENT' );
				break;
			}
		}
		
		this.__hide( );
	},

	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_mousemove : function( event )
	{
		var oDiv = Event.element( event );
		oDiv.style.color = ( event.type == 'mouseover' )? 'white' : 'black';
		oDiv.style.backgroundColor = ( event.type == 'mouseover' )? '#333399' : 'transparent';
	},

	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_click_mask : function( event )
	{
		debug( "birtContextMenu.__neh_click" );
		this.__hide( );
	},

	////////////////////////////////////////////////////////////////////////////
	//	Birt event handlers
	
	////////////////////////////////////////////////////////////////////////////
	// Local methods
	
	__create_mask : function( )
	{
		var oMask = document.createElement( 'div' );
		oMask.style.position = 'absolute';
		oMask.style.top = '0px';
		oMask.style.left = '0px';
		oMask.style.width = '100%';
		oMask.style.height = '99%';
		oMask.style.zIndex = '200';
		oMask.style.backgroundColor = '#dbe4ee';
		oMask.style.filter = 'alpha( opacity = 0.0 )';
		oMask.style.opacity = '.0';
		oMask.style.display = 'none';
		document.body.appendChild( oMask );
		return oMask;		
	},
	
	__hide : function( )
	{
		if ( this.__contentRoot.firstChild )
		{
			this.__contentRoot.removeChild( this.__contentRoot.firstChild );
		}
		
		Element.hide( this.__instance, this.__mask );
	}
} );