{type:"class",attributes:{"name":"Separator","packageName":"qx.ui.menu","superClass":"qx.ui.layout.CanvasLayout","fullName":"qx.ui.menu.Separator","type":"class"},children:[
  {type:"appearances",children:[
    {type:"appearance",attributes:{"type":"qx.ui.basic.Terminator","name":"menu-separator-line"}},
    {type:"appearance",attributes:{"type":"qx.ui.menu.Separator","name":"menu-separator"}}
    ]},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"}}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"name":"appearance","docFrom":"qx.ui.core.Widget","defaultValue":"\"menu-separator\"","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.core.Widget"}},
    {type:"property",attributes:{"name":"height","docFrom":"qx.ui.core.Widget","defaultValue":"\"auto\"","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.core.Widget"}}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","name":"_onmousedown"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>mouseDown event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.MouseEvent"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Callback method for the &#8220;mouseDown&#8221; event</p>\n\n<p>Simply stops the propagation of the event</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"hasIcon"},children:[
      {type:"desc",attributes:{"text":"<p>Returns <code>false</code> to clarify that the Separator widget has no icon</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>false</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"hasLabel"},children:[
      {type:"desc",attributes:{"text":"<p>Returns <code>false</code> to clarify that the Separator widget has no label</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>false</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"hasMenu"},children:[
      {type:"desc",attributes:{"text":"<p>Returns <code>false</code> to clarify that the Separator widget has no sub menu</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>false</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"hasShortcut"},children:[
      {type:"desc",attributes:{"text":"<p>Returns <code>false</code> to clarify that the Separator widget has no shortcut</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>false</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]}
    ]}
  ]}