{type:"class",attributes:{"name":"Icon","packageName":"qx.ui.table.headerrenderer","superClass":"qx.ui.table.headerrenderer.Default","fullName":"qx.ui.table.headerrenderer.Icon","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>A header cell renderer which renders an icon (only). The icon cannot be combined\nwith text.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"iconUrl"},children:[
          {type:"desc",attributes:{"text":"<p><span class=\"caps\">URL</span> to the icon to show</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"\"\"","name":"tooltip"},children:[
          {type:"desc",attributes:{"text":"<p>Text of the tooltip to show if the mouse hovers over the\n                            icon</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"name":"getIconUrl","fromProperty":"iconUrl"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>iconUrl</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #iconUrl}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>iconUrl</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initIconUrl","fromProperty":"iconUrl"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>iconUrl</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>iconUrl</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #iconUrl}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetIconUrl","fromProperty":"iconUrl"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>iconUrl</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #iconUrl}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setIconUrl","fromProperty":"iconUrl"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>iconUrl</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>iconUrl</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #iconUrl}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"docFrom":"qx.ui.table.IHeaderRenderer","overriddenFrom":"qx.ui.table.headerrenderer.Default","name":"updateHeaderCell"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"cellInfo"}},
        {type:"param",attributes:{"name":"cellWidget"}}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"defaultValue":"\"\"","propertyType":"new","name":"iconUrl","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p><span class=\"caps\">URL</span> of the icon to show</p>"}}
      ]}
    ]}
  ]}