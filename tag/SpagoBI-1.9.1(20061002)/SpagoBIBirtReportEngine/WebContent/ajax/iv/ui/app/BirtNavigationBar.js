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
 *	BirtNavigationBar
 *	...
 */
BirtNavigationBar = Class.create( );

BirtNavigationBar.prototype = Object.extend( new BirtAppUIComponentBase( ), 
{
	/**
	 *	Current page number.
	 */
	__currentPage : null,
	
	/**
	 *	Total number of pages.
	 */
	__oTotalPage : null,
	
	/**
	 *	Current page number.
	 */
	__oPageNumber : null,
	
	/**
	 *	Initialization routine required by "ProtoType" lib.
	 *	@return, void
	 */
	__initialize : function( id )
	{
		this.__oPageNumber = $( 'pageNumber' );
		this.__oTotalPage = $( 'totalPage' );
		this.__cb_installEventHandlers( );
	},

	/**
	 *	Binding data to the navigation bar UI. Data includes page number, total
	 *	page number (optional).
	 *
	 *	@data, data DOM tree (Schema TBD)
	 *	@return, void
	 */
	__cb_bind : function( data )
	{
		if ( !data )
		{
			return;
		}
		
		var oPageNumbers = data.getElementsByTagName( 'PageNumber' );
		if ( !oPageNumbers && !oPageNumbers[0] )
		{
			return;
		}
		
		this.__oPageNumber.innerHTML = oPageNumbers[0].firstChild.data;
		
		var oTotalPages = data.getElementsByTagName( 'TotalPage' );
		this.__oTotalPage.innerHTML = ( oTotalPages && oTotalPages[0] )? oTotalPages[0].firstChild.data : '+';
		
		var pageNumber = parseInt( this.__oPageNumber.firstChild.data );
		var totalPage = ( this.__oTotalPage.firstChild.data == '+' )? '+' : parseInt( this.__oTotalPage.firstChild.data );

		var oImgs = this.__instance.getElementsByTagName( "img" );
		oImgs[0].src = ( pageNumber > 1 ) ? "images/iv/FirstPage.gif" : "images/iv/FirstPage_disabled.gif";
		oImgs[0].style.cursor = ( pageNumber > 1 ) ? "pointer" : "default";
		oImgs[1].src = ( pageNumber > 1 ) ? "images/iv/PreviousPage.gif" : "images/iv/PreviousPage_disabled.gif";
		oImgs[1].style.cursor = ( pageNumber > 1 ) ? "pointer" : "default";
		oImgs[2].src = ( totalPage == '+' || pageNumber < totalPage ) ? "images/iv/NextPage.gif" : "images/iv/NextPage_disabled.gif";
		oImgs[2].style.cursor = ( totalPage == '+' || pageNumber < totalPage ) ? "pointer" : "default";
		oImgs[3].src = ( totalPage == '+' || pageNumber < totalPage ) ? "images/iv/LastPage.gif" : "images/iv/LastPage_disabled.gif";
		oImgs[3].style.cursor = ( totalPage == '+' || pageNumber < totalPage ) ? "pointer" : "default";
		
	},
	
	/**
	 *	Install native/birt event handlers.
	 *
	 *	@id, navigation bar id (optional since there is only one nav bar)
	 *	@return, void
	 */
	__cb_installEventHandlers : function( )
	{
		var oImgs = this.__instance.getElementsByTagName( "img" );
		
		if ( oImgs )
		{
			for ( var i = 0; i < oImgs.length; i++ )
			{
				Event.observe( oImgs[i], 'click', this.__neh_click.bindAsEventListener( this ), false );
			}
		}
	},
	
	/**
	 *	Handle native event 'click'.
	 *
	 *	@event, incoming browser native event
	 *	@return, void
	 */
	__neh_click : function( event )
	{
		var pageNumber = parseInt( this.__oPageNumber.firstChild.data );
		var totalPage = ( this.__oTotalPage.firstChild.data == '+' )? '+' : parseInt( this.__oTotalPage.firstChild.data );
		
		var oBtn = Event.element( event );
		if ( oBtn )
		{
			switch ( oBtn.name )
			{
   				case 'first':
 				{
 					if ( pageNumber > 1 )
 					{
						birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : 1 } );
					}
 					break;
 				}
   				case 'previous':
 				{
 					if ( pageNumber > 1 )
 					{
						birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : pageNumber - 1 } );
					}
 					break;
 				}
   				case 'next':
 				{
 					if ( totalPage == '+' || pageNumber < totalPage )
 					{
	 					birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : pageNumber + 1 } );
 					}
 					break;
 				}
   				case 'last':
 				{
 					if ( totalPage == '+' || pageNumber < totalPage )
 					{
 						birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : totalPage } );
 					}
 					break;
 				}
   				case 'goto':
   				{
   					var oGotoPage = $( 'gotoPage' );
 					birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : oGotoPage.value } );
   					break;
   				}
				default:
				{
					break;
				}	
			}
		}
	},
	
	/**
	 * Load current page. Triggered by init.
	 */
	__init_page : function( )
	{
		if ( this.__oPageNumber.firstChild )
		{
			var pageNumber = parseInt( this.__oPageNumber.firstChild.data );
			birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE, { name : "page", value : pageNumber } );
		}
		else
		{
			birtEventDispatcher.broadcastEvent( birtEvent.__E_GETPAGE );
		}
	}
} );