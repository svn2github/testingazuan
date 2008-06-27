{type:"class",attributes:{"name":"DefaultFilter","hasWarning":"true","packageName":"qx.log","superClass":"qx.log.Filter","fullName":"qx.log.DefaultFilter","type":"class"},children:[{type:"desc",attributes:{"text":"<p>The default filter. Has a minimum level and can be enabled or disabled.</p>"}},{type:"constructor",children:[{type:"method",attributes:{"isCtor":"true","name":"ctor"}}]},{type:"methods",children:[{type:"method",attributes:{"hasError":"true","overriddenFrom":"qx.log.Filter","name":"decide"},children:[{type:"params",children:[{type:"param",attributes:{"name":"evt"}}]},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"Integer"}}]}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>evt</code> is not documented.","column":"14","line":"95"}},{type:"error",attributes:{"msg":"Documentation is missing.","column":"14","line":"95"}}]}]},{type:"method",attributes:{"name":"getEnabled","fromProperty":"enabled"},children:[{type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>enabled</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>(Computed) value of <code>enabled</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"getMinLevel","fromProperty":"minLevel"},children:[{type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>minLevel</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #minLevel}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>(Computed) value of <code>minLevel</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"access":"protected","name":"initEnabled","fromProperty":"enabled"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>Initial value for property <code>enabled</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>enabled</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>the default value</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"access":"protected","name":"initMinLevel","fromProperty":"minLevel"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>Initial value for property <code>minLevel</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>minLevel</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #minLevel}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>the default value</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"isEnabled","fromProperty":"enabled"},children:[{type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>enabled</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"method",attributes:{"name":"resetEnabled","fromProperty":"enabled"},children:[{type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>enabled</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"resetMinLevel","fromProperty":"minLevel"},children:[{type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>minLevel</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #minLevel}.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"setEnabled","fromProperty":"enabled"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>New value for property <code>enabled</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>enabled</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"setMinLevel","fromProperty":"minLevel"},children:[{type:"params",children:[{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>New value for property <code>minLevel</code>.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>minLevel</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #minLevel}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"toggleEnabled","fromProperty":"enabled"},children:[{type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>enabled</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>the new value</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]}]},{type:"properties",children:[{type:"property",attributes:{"defaultValue":"true","propertyType":"new","name":"enabled","check":"Boolean"},children:[{type:"desc",attributes:{"text":"<p>Whether the filter should be enabled. If set to false all log events\nwill be denied.</p>"}}]},{type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"minLevel","check":"Number"},children:[{type:"desc",attributes:{"text":"<p>The minimum log level. If set only log messages with a level greater or equal\nto the set level will be accepted.</p>"}}]}]}]}