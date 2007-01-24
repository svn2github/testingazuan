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
 
Event.prototype = Object.extend( Event,
{
	/**
	 *	Extension to prototype 'Event' since Event.stop(event) isn't
	 *	stopping in ie
	 */
	stop: function( event )
	{
		event.cancelBubble = true;
		
		if ( event.preventDefault )
		{ 
			event.preventDefault( );
			event.stopPropagation( );
	    }
	    else
	    {
			event.returnValue = false;
	    }
	},
	
	/**
	 *	Stops click from propigating without using .bindAsEventListener
	 */
	colClickStop: function( e )
	{
 		if (!e) var e = $("Document").contentWindow.event;
		debug( e.type);
		Event.stop( e );
	}
});

/**
 *	Birt Class
 */
var BirtClass =
{
	forType : function( type )
	{
		if ( type )
		{
			if ( type.toUpperCase( ) == 'Table'.toUpperCase( ) )
			{
				return eval( 'birtReportTable' );
			}
			else if ( type.toUpperCase( ) == 'Chart'.toUpperCase( ) )
			{
				return eval( 'birtReportChart' );
			}
			else if ( type.toUpperCase( ) == 'Document'.toUpperCase( ) )
			{
				return eval( 'birtReportDocument' );
			}
		}
		
		return null;
	}
}