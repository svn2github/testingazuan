/*

SpagoBI - The Business Intelligence Free Platform

Copyright (C) 2005 Engineering Ingegneria Informatica S.p.A.

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either
version 2.1 of the License, or (at your option) any later version.

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the Free Software
Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA  02110-1301  USA

*/


/* *
 * @author Andrea Gioia (andrea.gioia@eng.it)
 * @author Amit Rana (amit.rana@eng.it)
 * @author Gaurav Jauhri (gaurav.jauhri@eng.it)
 * 
 */

/**
 * Class to show the title text "SpagoBI - Chiron" on the top of the page
 * @see spagobi.app.Chiron#main
 */
 
qx.Class.define("spagobi.app.ui.Header",
{
  //extend : qx.legacy.ui.embed.HtmlEmbed,//change
  extend : qx.ui.container.Composite,
  //extend : qx.ui.embed.Html,

  /**
   * Constructor to show the title text using HTML tags
   */	
  construct : function()
  {
    /*this.base(arguments, "<h1><span>SpagoBI</span> - Chiron</h1>");

    //this.setHtmlProperty("className", "header");//change
    this.setCssClass("header");
    this.setHeight(50);*/
    this.base(arguments);

    this.setLayout(new qx.ui.layout.HBox);
    this.setAppearance("app-header");

    var title = new qx.ui.basic.Label("SpagoBI - Chiron");
    var version = new qx.ui.basic.Label("V 0.8");

    this.add(title);
    this.add(new qx.ui.core.Spacer, {flex : 1});
    this.add(version);
    
  }
});

