{type:"class",attributes:{"name":"Button","hasWarning":"true","packageName":"qx.ui.pageview.tabview","superClass":"qx.ui.pageview.AbstractButton","fullName":"qx.ui.pageview.tabview.Button","type":"class"},children:[
  {type:"appearances",children:[
    {type:"appearance",attributes:{"type":"qx.ui.pageview.tabview.Button","name":"tab-view-button"},children:[
      {type:"states",children:[
        {type:"state",attributes:{"name":"checked"},children:[
          {type:"desc",attributes:{"text":"<p>Set by {@link #checked}</p>"}}
          ]},
        {type:"state",attributes:{"name":"over"}}
        ]}
      ]}
    ]},
  {type:"events",children:[
    {type:"event",attributes:{"name":"changeShowCloseButton"},children:[
      {type:"desc",attributes:{"text":"Fired on change of the property {@link #showCloseButton}."}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.ChangeEvent"}}
        ]}
      ]},
    {type:"event",attributes:{"hasError":"true","name":"closetab"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"11","line":"48"}}
        ]},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.pageview.AbstractButton#checked","overriddenFrom":"qx.ui.pageview.AbstractButton","name":"_applyChecked"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>new value of the property</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"old"},children:[
          {type:"desc",attributes:{"text":"<p>previous value of the property (null if it was not yet set).</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Applies changes of the property value of the property <code>checked</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #_applyChecked}.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.pageview.tabview.Button#closeButtonImage","name":"_applyCloseButtonImage"},children:[
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
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.pageview.tabview.Button#showCloseButton","name":"_applyShowCloseButton"},children:[
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
    {type:"method",attributes:{"access":"protected","hasError":"true","overriddenFrom":"qx.ui.pageview.AbstractButton","name":"_onkeydown"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Event"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>e</code> is not documented.","column":"18","line":"113"}},
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"18","line":"113"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","overriddenFrom":"qx.ui.pageview.AbstractButton","name":"_onkeypress"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Event"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>e</code> is not documented.","column":"19","line":"132"}},
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"19","line":"132"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","name":"_ontabclose"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Event"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Parameter <code>e</code> is not documented.","column":"19","line":"182"}},
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"19","line":"182"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","hasError":"true","overriddenFrom":"qx.ui.core.Widget","name":"_renderAppearance"},children:[
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"25","line":"259"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"getCloseButtonImage","fromProperty":"closeButtonImage"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>closeButtonImage</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #closeButtonImage}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>closeButtonImage</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getShowCloseButton","fromProperty":"showCloseButton"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>showCloseButton</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #showCloseButton}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>showCloseButton</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initCloseButtonImage","fromProperty":"closeButtonImage"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>closeButtonImage</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>closeButtonImage</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #closeButtonImage}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initShowCloseButton","fromProperty":"showCloseButton"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>showCloseButton</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>showCloseButton</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #showCloseButton}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isShowCloseButton","fromProperty":"showCloseButton"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>showCloseButton</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #showCloseButton}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetCloseButtonImage","fromProperty":"closeButtonImage"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>closeButtonImage</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #closeButtonImage}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetShowCloseButton","fromProperty":"showCloseButton"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>showCloseButton</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #showCloseButton}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setCloseButtonImage","fromProperty":"closeButtonImage"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>closeButtonImage</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>closeButtonImage</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #closeButtonImage}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setShowCloseButton","fromProperty":"showCloseButton"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>showCloseButton</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>showCloseButton</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #showCloseButton}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleShowCloseButton","fromProperty":"showCloseButton"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>showCloseButton</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #showCloseButton}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"name":"appearance","docFrom":"qx.ui.core.Widget","defaultValue":"\"tab-view-button\"","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.basic.Atom"}},
    {type:"property",attributes:{"apply":"_applyCloseButtonImage","defaultValue":"\"icon/16/actions/dialog-cancel.png\"","propertyType":"new","name":"closeButtonImage","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>Close Tab Icon</p>"}}
      ]},
    {type:"property",attributes:{"name":"showCloseButton","defaultValue":"false","event":"changeShowCloseButton","propertyType":"new","apply":"_applyShowCloseButton","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>default Close Tab Button</p>"}}
      ]}
    ]}
  ]}