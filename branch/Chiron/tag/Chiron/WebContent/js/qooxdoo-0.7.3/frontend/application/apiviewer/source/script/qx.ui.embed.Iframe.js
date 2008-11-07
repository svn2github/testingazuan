{type:"class",attributes:{"name":"Iframe","hasWarning":"true","packageName":"qx.ui.embed","superClass":"qx.ui.basic.Terminator","fullName":"qx.ui.embed.Iframe","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Container widget for internal frames (iframes).</p>\n\n<p>An iframe can display any <span class=\"caps\">HTML</span> page inside the widget.</p>"}},
  {type:"appearances",children:[
    {type:"appearance",attributes:{"type":"qx.ui.embed.Iframe","name":"iframe"}}
    ]},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"defaultValue":"null","name":"vSource"},children:[
          {type:"desc",attributes:{"text":"<p><span class=\"caps\">URL</span> of the <span class=\"caps\">HTML</span> page displayed in the iframe.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]}
      ]}
    ]},
  {type:"events",children:[
    {type:"event",attributes:{"name":"changeSource"},children:[
      {type:"desc",attributes:{"text":"Fired on change of the property {@link #source}."}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.ChangeEvent"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"load"},children:[
      {type:"desc",attributes:{"text":"<p>The &#8220;load&#8221; event is fired after the iframe content has successfully been loaded.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]}
    ]},
  {type:"methods-static",children:[
    {type:"method",attributes:{"hasError":"true","isStatic":"true","name":"load"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"obj"}}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"12","line":"92"}}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"name":"appearance","docFrom":"qx.ui.core.Widget","defaultValue":"\"iframe\"","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.core.Widget"}},
    {type:"property",attributes:{"apply":"_applyFrameName","defaultValue":"\"\"","propertyType":"new","name":"frameName","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>Name of the iframe.</p>"}}
      ]},
    {type:"property",attributes:{"apply":"_applyScrolling","defaultValue":"\"auto\"","propertyType":"new","name":"scrolling","possibleValues":"\"yes\",\"no\",\"auto\""},children:[
      {type:"desc",attributes:{"text":"<p>Whether the iframe&#8217;s content pane should have scroll bars</p>"}}
      ]},
    {type:"property",attributes:{"name":"selectable","docFrom":"qx.ui.core.Widget","defaultValue":"false","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.core.Widget"}},
    {type:"property",attributes:{"name":"source","check":"String","allowNull":"true","propertyType":"new","apply":"_applySource","event":"changeSource"},children:[
      {type:"desc",attributes:{"text":"<p>Source <span class=\"caps\">URL</span> of the iframe.</p>"}}
      ]},
    {type:"property",attributes:{"name":"tabIndex","docFrom":"qx.ui.core.Widget","defaultValue":"0","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.core.Widget"}}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.core.Widget#element","overriddenFrom":"qx.ui.core.Widget","name":"_applyElement"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>Previous value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.embed.Iframe#frameName","name":"_applyFrameName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>Previous value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"propName"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"uniqModIds"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.embed.Iframe#scrolling","name":"_applyScrolling"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>new value of the property</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>previous value of the property (null if it was not yet set).</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Applies changes of the property value of the property <code>scrolling</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #_applyScrolling}.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.embed.Iframe#source","name":"_applySource"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>Previous value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","overriddenFrom":"qx.ui.core.Widget","name":"_beforeAppear"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"21","line":"508"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","overriddenFrom":"qx.ui.core.Widget","name":"_beforeDisappear"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"24","line":"523"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","name":"_generateBlockerElement"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"31","line":"444"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_generateIframeElement"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vFrameName"},children:[
          {type:"desc",attributes:{"text":"<p>Name of the iframe.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Creates an template iframe element and sets all required html and style properties.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","name":"_onload"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"15","line":"647"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_syncScrolling"},children:[
      {type:"desc",attributes:{"text":"<p>Sync scrolling property to the iframe <span class=\"caps\">DOM</span> node.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","name":"_syncSource"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"19","line":"553"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"block"},children:[
      {type:"desc",attributes:{"text":"<p>Cover the iframe with a transparent blocker div element. This prevents\nmouse or key events to be handled by the iframe. To release the blocker\nuse {@link #release}.</p>"}}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"getBlockerNode"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"22","line":"211"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"getContentDocument"},children:[
      {type:"desc",attributes:{"text":"<p>Get the <span class=\"caps\">DOM</span> document object of the iframe.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The <span class=\"caps\">DOM</span> document object of the iframe.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"DOMDocument"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getContentWindow"},children:[
      {type:"desc",attributes:{"text":"<p>Get the <span class=\"caps\">DOM</span> window object of the iframe.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The <span class=\"caps\">DOM</span> window object of the iframe.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"DOMWindow"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getFrameName","fromProperty":"frameName"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>frameName</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #frameName}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>frameName</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getIframeNode"},children:[
      {type:"desc",attributes:{"text":"<p>Get the <span class=\"caps\">DOM</span> element of the iframe.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The <span class=\"caps\">DOM</span> element of the iframe.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Element"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getScrolling","fromProperty":"scrolling"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>scrolling</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #scrolling}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>scrolling</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getSource","fromProperty":"source"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>source</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #source}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>source</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initFrameName","fromProperty":"frameName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>frameName</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>frameName</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #frameName}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initScrolling","fromProperty":"scrolling"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>scrolling</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>scrolling</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #scrolling}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initSource","fromProperty":"source"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>source</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>source</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #source}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"isLoaded"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"1","line":"1"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"queryCurrentUrl"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the current (served) <span class=\"caps\">URL</span> inside the iframe</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Returns the location href or null (if a query is not possible/allowed)</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"release"},children:[
      {type:"desc",attributes:{"text":"<p>Release the blocker set by {@link #block}.</p>"}}
      ]},
    {type:"method",attributes:{"name":"reload"},children:[
      {type:"desc",attributes:{"text":"<p>Reload the contents of the iframe.</p>"}}
      ]},
    {type:"method",attributes:{"name":"resetFrameName","fromProperty":"frameName"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>frameName</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #frameName}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetScrolling","fromProperty":"scrolling"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>scrolling</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #scrolling}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetSource","fromProperty":"source"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>source</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #source}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"hasError":"true","name":"setBlockerNode"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vBlockerNode"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>vBlockerNode</code> is not documented.","column":"22","line":"223"}},
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"22","line":"223"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"setFrameName","fromProperty":"frameName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>frameName</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>frameName</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #frameName}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setIframeNode"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vIframeNode"},children:[
          {type:"desc",attributes:{"text":"<p>The new <span class=\"caps\">DOM</span> element of the iframe.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Change the <span class=\"caps\">DOM</span> element of the iframe.</p>"}}
      ]},
    {type:"method",attributes:{"name":"setScrolling","fromProperty":"scrolling"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>scrolling</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>scrolling</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #scrolling}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setSource","fromProperty":"source"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>source</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>source</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #source}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]}
    ]}
  ]}