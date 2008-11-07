{type:"class",attributes:{"name":"Icon","packageName":"qx.theme.manager","superClass":"qx.core.Target","isSingleton":"true","fullName":"qx.theme.manager.Icon","type":"class"},children:[{type:"desc",attributes:{"text":"<p>This singleton selects the icon theme to use.</p>"}},{type:"events",children:[{type:"event",attributes:{"name":"changeIconTheme"},children:[{type:"desc",attributes:{"text":"Fired on change of the property {@link #iconTheme}."}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.event.type.ChangeEvent"}}]}]}]},{type:"methods",children:[{type:"method",attributes:{"access":"protected","apply":"qx.theme.manager.Icon#iconTheme","name":"_applyIconTheme"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>new value of the property</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Theme"}}]}]},{type:"param",attributes:{"name":"old"},children:[{type:"desc",attributes:{"text":"<p>previous value of the property (null if it was not yet set).</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Theme"}}]}]}]},{type:"desc",attributes:{"text":"<p>Applies changes of the property value of the property <code>iconTheme</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #_applyIconTheme}.</p>"}}]},{type:"method",attributes:{"name":"getIconTheme","fromProperty":"iconTheme"},children:[{type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>iconTheme</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #iconTheme}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>(Computed) value of <code>iconTheme</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"access":"protected","name":"initIconTheme","fromProperty":"iconTheme"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>Initial value for property <code>iconTheme</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>iconTheme</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #iconTheme}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>the default value</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"resetIconTheme","fromProperty":"iconTheme"},children:[{type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>iconTheme</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #iconTheme}.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"setIconTheme","fromProperty":"iconTheme"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>New value for property <code>iconTheme</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>iconTheme</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #iconTheme}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"syncIconTheme"},children:[{type:"desc",attributes:{"text":"<p>Sync dependend objects with internal database</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]}]},{type:"properties",children:[{type:"property",attributes:{"name":"iconTheme","check":"Theme","allowNull":"true","propertyType":"new","apply":"_applyIconTheme","event":"changeIconTheme"},children:[{type:"desc",attributes:{"text":"<p>currently used icon theme</p>"}}]}]},{type:"methods-static",children:[{type:"method",attributes:{"isStatic":"true","name":"getInstance"},children:[{type:"desc",attributes:{"text":"<p>Returns a singleton instance of this class. On the first call the class\nis instantiated by calling the constructor with no arguments. All following\ncalls will return this instance.</p>\n\n<p>This method has been added by setting the &#8220;type&#8221; key in the class definition\n({@link qx.Class#define}) to &#8220;singleton&#8221;.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>The singleton instance of this class.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.theme.manager.Icon"}}]}]}]}]}]}