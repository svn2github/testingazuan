{type:"class",attributes:{"name":"MExecutable","packageName":"qx.ui.core","includer":"qx.ui.form.Button,qx.ui.menu.Button,qx.ui.form.SplitButton,qx.ui.control.DateChooser,qx.ui.tree.FolderOpenButton,spagobi.ui.TimeChooser","fullName":"qx.ui.core.MExecutable","type":"mixin"},children:[{type:"desc",attributes:{"text":"<p>This mixin is included by all widgets, which support an &#8216;execute&#8217; like\nbuttons or menu entries.</p>"}},{type:"events",children:[{type:"event",attributes:{"name":"changeCommand"},children:[{type:"desc",attributes:{"text":"Fired on change of the property {@link #command}."}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.event.type.Data"}}]}]},{type:"event",attributes:{"name":"execute"},children:[{type:"desc",attributes:{"text":"<p>Fired if the {@link #execute} method is invoked.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.event.type.Event"}}]}]}]},{type:"methods",children:[{type:"method",attributes:{"access":"protected","apply":"qx.ui.core.MExecutable#command","isMixin":"true","name":"_applyCommand"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>new value of the property</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.event.Command"}}]}]},{type:"param",attributes:{"name":"old"},children:[{type:"desc",attributes:{"text":"<p>previous value of the property (null if it was not yet set).</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.event.Command"}}]}]}]},{type:"desc",attributes:{"text":"<p>Applies changes of the property value of the property <code>command</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #_applyCommand}.</p>"}}]},{type:"method",attributes:{"access":"protected","isMixin":"true","name":"_onChangeEnabledCommand"},children:[{type:"params",children:[{type:"param",attributes:{"name":"e"},children:[{type:"desc",attributes:{"text":"<p>The change event</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.event.type.Data"}}]}]}]},{type:"desc",attributes:{"text":"<p>Event Listener. Listen for enabled changes in the associated command</p>"}}]},{type:"method",attributes:{"isMixin":"true","name":"execute"},children:[{type:"desc",attributes:{"text":"<p>Initiate the execute action.</p>"}}]},{type:"method",attributes:{"name":"getCommand","fromProperty":"command"},children:[{type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>command</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #command}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>(Computed) value of <code>command</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"access":"protected","name":"initCommand","fromProperty":"command"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>Initial value for property <code>command</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>command</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #command}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>the default value</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"resetCommand","fromProperty":"command"},children:[{type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>command</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #command}.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"setCommand","fromProperty":"command"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>New value for property <code>command</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>command</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #command}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]}]},{type:"properties",children:[{type:"property",attributes:{"name":"command","check":"qx.event.Command","allowNull":"true","propertyType":"new","apply":"_applyCommand","isMixin":"true","event":"changeCommand"},children:[{type:"desc",attributes:{"text":"<p>A command called if the {@link #execute} method is called, e.g. on a\nbutton click.</p>"}}]}]}]}