{type:"class",attributes:{"isAbstract":"true","name":"AbstractPage","packageName":"qx.ui.pageview","superClass":"qx.ui.layout.CanvasLayout","childClasses":"qx.ui.pageview.buttonview.Page,qx.ui.pageview.radioview.Page,qx.ui.pageview.tabview.Page","fullName":"qx.ui.pageview.AbstractPage","type":"class"},children:[
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vButton"}}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"name":"bottom","docFrom":"qx.ui.core.Widget","defaultValue":"0","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.core.Widget"}},
    {type:"property",attributes:{"apply":"_applyButton","propertyType":"new","name":"button","check":"qx.ui.pageview.AbstractButton"},children:[
      {type:"desc",attributes:{"text":"<p>The attached tab of this page.</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"false","propertyType":"new","overriddenFrom":"qx.ui.core.Widget","name":"display","refine":"true"},children:[
      {type:"desc",attributes:{"text":"<p>Make element displayed (if switched to true the widget will be created, if needed, too).\n Instead of qx.ui.core.Widget, the default is false here.</p>"}}
      ]},
    {type:"property",attributes:{"name":"left","docFrom":"qx.ui.core.Widget","defaultValue":"0","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.core.Widget"}},
    {type:"property",attributes:{"name":"right","docFrom":"qx.ui.core.Widget","defaultValue":"0","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.core.Widget"}},
    {type:"property",attributes:{"name":"top","docFrom":"qx.ui.core.Widget","defaultValue":"0","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.core.Widget"}}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.pageview.AbstractPage#button","name":"_applyButton"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>new value of the property</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.ui.pageview.AbstractButton"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>previous value of the property (null if it was not yet set).</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.ui.pageview.AbstractButton"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Applies changes of the property value of the property <code>button</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #_applyButton}.</p>"}}
      ]},
    {type:"method",attributes:{"name":"getButton","fromProperty":"button"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>button</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #button}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>button</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initButton","fromProperty":"button"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>button</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>button</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #button}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetButton","fromProperty":"button"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>button</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #button}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setButton","fromProperty":"button"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>button</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>button</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #button}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]}
    ]}
  ]}