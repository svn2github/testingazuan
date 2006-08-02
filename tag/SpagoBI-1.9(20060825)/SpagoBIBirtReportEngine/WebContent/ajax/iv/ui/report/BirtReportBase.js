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
 *	@fileoverview
 *	BirtReportBase.js is used as the abstract base class for report ui components
 */
BirtReportBase = Class.create( );
BirtReportBase.prototype =
{
	__neh_contextMenu_closure : null,
	
	__neh_select_closure : null,
	
	/**
	 *	Initialization invoked on instantiation
	 *
	 *	@return, void
	 */
	initialize : function( subclass )
	{
		debug( "Initializing BirtReportBase  " + subclass );
		
		this.defaultContextItems = { }; //initialized here so that instantiating components get a deep copy
		this.defaultContextItems.line1 = { isLine: "true" };
		this.componentContextItems = { }; //initialized here so that instantiating components get a deep copy
		this.defaultContextItems.hide = { displayName: "Hide Element", action: this.__neh_hide, target: this, inactive: false };
		this.defaultContextItems.show = { displayName: "Show Element", action: this.__neh_show, target: this, inactive: true };
	},
	
	/**
	 *	Re-render ui object with new content.
	 *
	 *	@id, ui object id
	 *	@content, new html UI content
	 *	@return, void
	 */
	__cb_render : function( id, content )
	{
		var oDiv = $( id );
		
		while( oDiv.childNodes.length > 0)
		{
			oDiv.removeChild(oDiv.firstChild);
		}
		
		var container = document.createElement( "div" );
		container.style.position = "relative";
		container.style.padding = "15px";
		container.innerHTML = content;
		oDiv.appendChild( container );
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, table object id
	 *	@return, void
	 */
	__cb_installEventHandlers : function( id, children )
	{
		var container = $( id );

		container[Constants.reportBase] = true;
		container[ Constants.activeIds ] = [ ]; // Need to remember active children
		container[ Constants.activeIdTypes ] = [ ]; // Need to remember active children types
		container[ Constants.birtVisible ] = true;

		birtEventDispatcher.registerEventHandler( birtEvent.__E_HIDE_COMPONENT, id, this.__beh_hide );
		birtEventDispatcher.registerEventHandler( birtEvent.__E_SHOW_COMPONENT, id, this.__beh_show );
		
		if ( !children )
		{
			return;
		}

		// Also need to take care the active children.
		for( var i = 0; i < children.length; i++ )
		{
			var oElementIds = children[i].getElementsByTagName( 'Id' );
			var oElementTypes = children[i].getElementsByTagName( 'Type' );

			var birtObj = null;
			var birtObj = BirtClass.forType( oElementTypes[0].firstChild.data );

			if ( !birtObj || !birtObj.__cb_installEventHandlers )
			{
				continue;
			}
			
			container[ Constants.activeIds ].push( oElementIds[0].firstChild.data );
			container[ Constants.activeIdTypes ].push( oElementTypes[0].firstChild.data );

			birtObj.__cb_installEventHandlers( oElementIds[0].firstChild.data );
		}
	},
	
	/**
	 *	Unregister any birt event handlers.
	 *	Remove local event listeners
	 *
	 *	@id, object id
	 *	@return, void
	 */
	__cb_disposeEventHandlers : function( id )
	{
		var container = $( id );
      	
		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_HIDE_COMPONENT, id );
		birtEventDispatcher.unregisterEventHandler( birtEvent.__E_SHOW_COMPONENT, id );
		
		var id = null;
		while( container[ Constants.activeIds ].length > 0 )
		{
			var id = container[ Constants.activeIds ].shift( )
			var type = container[ Constants.activeIdTypes ].shift( );
			var birtObj = BirtClass.forType( type );
			if ( !birtObj || !birtObj.__cb_disposeEventHandlers )
			{
				continue;
			}
			birtObj.__cb_disposeEventHandlers( id );
		}
	},
	
	/**
	 *	Handle native event 'contextmenu'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_contextMenu : function( event )
	{	
		Event.stop( event );
			
		var container = Event.element( event );
		while( !container[Constants.reportBase] ) //TODO error check
		{
			container = container.parentNode;
		}
		
		birtEventDispatcher.fireEvent( birtEvent.__E_BLUR );
		this.__neh_selectElement( event );
		birtEventDispatcher.setFocusId( container.id );
		
		var contextList = { };
		Object.extend( contextList, this.componentContextItems );
		Object.extend( contextList, this.defaultContextItems );
		
		// TODO: need emitter support.
		this.defaultContextItems.hide.inactive = !container[ Constants.birtVisible ];
		this.defaultContextItems.show.inactive = container[ Constants.birtVisible ];
		
		birtContextMenu.__cb_bind( event, contextList );
	},
	
	/**
	 *	Handle selection of entire  element
	 *	@param event incoming browser native event
	 */
	__neh_selectElement : function( event )
	{	
		debug( "BirtReportBase.__neh_selectElement" );
		var container = Event.element( event );

		// handle anchor.
		if ( container.tagName == 'A' )
		{
			return;
		}
		
		while( !container[Constants.reportBase] ) //TODO error check
		{
			container = container.parentNode;
		}
		
		birtEventDispatcher.fireEvent( birtEvent.__E_BLUR );
		birtEventDispatcher.setFocusElement( container );
		this.selectElement( container );

		Event.stop( event );
	},
	
	////////////////////////////////////////////////////////////////////////////
	//	Birt event handlers
	
	/**
	 *	Birt event hander for "hide" event.
	 *
	 *	@id, table id
	 *	@return, void
	 */
	__beh_hide : function( id )
	{
		var container = $( id );
		while( !container[Constants.reportBase] ) //TODO error check
		{
			container = container.parentNode;
		}

		try
		{
			birtSoapRequest.addOperation( birtEventDispatcher.getFocusId( ), Constants.operator.hide );
			container[ Constants.birtVisible ] = false;
		}
		catch( error )
		{
			debug( "BirtReportBase.__neh_hide ERROR : " + error );
		}
		
		birtSoapRequest.setURL( "test/" + id.substring( 0, 5 ).toLowerCase( ) + "Hide.xml" );
		
		return true;
	},
	
	/**
	 *	Birt event hander for "show" event.
	 *  TODO For Dev only: later Table Component will only appear as "show"
	 *	@id, table id
	 *	@return, void
	 */
	__beh_show : function( id )
	{
		var container = $( id );
		while( !container[Constants.reportBase] ) //TODO error check
		{
			container = container.parentNode;
		}

		container[ Constants.birtVisible ] = true;
		
		birtSoapRequest.setURL( "test/" + id.substring( 0, 5 ).toLowerCase( ) + "Basic.xml" );
		return true;
	},
	
	/**
	 *	Birt event hander for "blur" event.
	 *  ABSTRACT
	 *	@id, table id
	 *	@return, void
	 */
	__beh_blur : function( id )
	{	
		debug( "ERROR: Abstract Method BirtReportBase.__beh_blur Called" );
		throw "ERROR: Abstract Method BirtReportBase.__beh_blur Called"; 
	},
	
	/**
	 *	"Abstract Method" must be instantiated by extending class
	 */
	selectElement : function ( event )
	{
		debug( "ERROR: Abstract Method BirtReportBase.selectElement Called" );
		throw "ERROR: Abstract Method BirtReportBase.selectElement Called";  
	},
	
	/**
	 *	Creates selection visual indicator
	 */
	createOverlay : function( container )
	{
		var borderWidth = 2;
		var height = container.clientHeight;
		var width = container.clientWidth;
		var left = 0;
		var top = 0;
		var divs = [];
		var div;
				
		for( var i = 0 ; i < 4; i++ )
		{
			div = container.ownerDocument.createElement( "div" );
			//div.style.backgroundColor = "#4682bf";
			div.style.backgroundColor = "#3399FF";
			div.style.position = "absolute";
			div.style.width = borderWidth;
			div.style.height = borderWidth;
			div.style.display= "none";
			div.style.top = top;
			div.style.left = left;
			div.style.zIndex = 100;
			var opacity = typeof( opacity ) != "undefined" ? opacity : 0.5;
			div.style.opacity = opacity;
			div.style.filter = 'alpha( opacity=' + ( opacity * 100 ) + ')';
			container.appendChild( div );
			divs.push( div );
		}
		
		for( var i = 0; i < 2; i++ ) //style sides
		{
			divs[i].style.height = height;
		}
		
		for( var i = 2; i < 4; i++ ) //style top & bottom
		{
			divs[i].style.width = width - 2 * borderWidth;
			divs[i].style.left = borderWidth;
			divs[i].style.overflow = "hidden"; //work around IE min height problem
		}
		
		divs[1].style.left = width - borderWidth;
		divs[3].style.top = height - borderWidth;
		container.columnOverlay = divs;
	}
}