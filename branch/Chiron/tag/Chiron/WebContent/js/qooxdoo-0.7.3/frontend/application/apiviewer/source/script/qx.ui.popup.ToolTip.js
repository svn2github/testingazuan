{type:"class",attributes:{"name":"ToolTip","packageName":"qx.ui.popup","superClass":"qx.ui.popup.PopupAtom","fullName":"qx.ui.popup.ToolTip","type":"class"},children:[
  {type:"appearances",children:[
    {type:"appearance",attributes:{"type":"qx.ui.popup.ToolTip","name":"tool-tip"}}
    ]},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vLabel"}},
        {type:"param",attributes:{"name":"vIcon"}}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","overriddenFrom":"qx.ui.popup.Popup","name":"_afterAppear"},children:[
      {type:"desc",attributes:{"text":"<p>Callback method for the &#8220;afterAppear&#8221; event.</p>\n\n<p>If the property {@link #restrictToPageOnOpen} is set to <code>true</code>\nthe tooltip gets repositioned to ensure it is displayed within the\nboundaries of the {@link qx.ui.core.ClientDocument}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.popup.ToolTip#boundToWidget","name":"_applyBoundToWidget"},children:[
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
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.popup.ToolTip#hideInterval","name":"_applyHideInterval"},children:[
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
    {type:"method",attributes:{"access":"protected","apply":"qx.ui.popup.ToolTip#showInterval","name":"_applyShowInterval"},children:[
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
    {type:"method",attributes:{"access":"protected","overriddenFrom":"qx.ui.popup.Popup","name":"_beforeAppear"},children:[
      {type:"desc",attributes:{"text":"<p>Callback method for the &#8220;beforeAppear&#8221; event.</p>\n\n<p>Does two things: stops the timer for the show interval and\nstarts the timer for the hide interval.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","overriddenFrom":"qx.ui.popup.Popup","name":"_beforeDisappear"},children:[
      {type:"desc",attributes:{"text":"<p>Callback method for the &#8220;beforeDisappear&#8221; event.</p>\n\n<p>Stops the timer for the hide interval.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_onhidetimer"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>interval event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Callback method for the &#8220;interval&#8221; event of the hide timer.</p>\n\n<p>Hides the tooltip by calling the corresponding {@link #hide} method.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_onmouseover"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>mouseOver event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.MouseEvent"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Callback method for the &#8220;mouseOver&#8221; event.</p>\n\n<p>If property {@link #hideOnOver} is enabled the tooltip gets hidden</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_onshowtimer"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>interval event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Callback method for the &#8220;interval&#8221; event of the show timer.</p>\n\n<p>Positions the tooltip (sets left and top) and calls the\n{@link #show} method.</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_startHideTimer"},children:[
      {type:"desc",attributes:{"text":"<p>Utility method to start the timer for the hide interval\n(if the timer is disabled)</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_startShowTimer"},children:[
      {type:"desc",attributes:{"text":"<p>Utility method to start the timer for the show interval\n(if the timer is disabled)</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_stopHideTimer"},children:[
      {type:"desc",attributes:{"text":"<p>Utility method to stop the timer for the hide interval\n(if the timer is enabled)</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_stopShowTimer"},children:[
      {type:"desc",attributes:{"text":"<p>Utility method to stop the timer for the show interval\n(if the timer is enabled)</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getBoundToWidget","fromProperty":"boundToWidget"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>boundToWidget</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #boundToWidget}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>boundToWidget</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getHideInterval","fromProperty":"hideInterval"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>hideInterval</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #hideInterval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>hideInterval</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getHideOnHover","fromProperty":"hideOnHover"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>hideOnHover</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #hideOnHover}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>hideOnHover</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getMousePointerOffsetX","fromProperty":"mousePointerOffsetX"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>mousePointerOffsetX</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #mousePointerOffsetX}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>mousePointerOffsetX</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getMousePointerOffsetY","fromProperty":"mousePointerOffsetY"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>mousePointerOffsetY</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #mousePointerOffsetY}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>mousePointerOffsetY</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getShowInterval","fromProperty":"showInterval"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>showInterval</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #showInterval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>showInterval</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initBoundToWidget","fromProperty":"boundToWidget"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>boundToWidget</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>boundToWidget</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #boundToWidget}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initHideInterval","fromProperty":"hideInterval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>hideInterval</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>hideInterval</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #hideInterval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initHideOnHover","fromProperty":"hideOnHover"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>hideOnHover</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>hideOnHover</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #hideOnHover}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initMousePointerOffsetX","fromProperty":"mousePointerOffsetX"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>mousePointerOffsetX</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>mousePointerOffsetX</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #mousePointerOffsetX}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initMousePointerOffsetY","fromProperty":"mousePointerOffsetY"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>mousePointerOffsetY</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>mousePointerOffsetY</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #mousePointerOffsetY}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initShowInterval","fromProperty":"showInterval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>showInterval</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>showInterval</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #showInterval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isHideOnHover","fromProperty":"hideOnHover"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>hideOnHover</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #hideOnHover}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetBoundToWidget","fromProperty":"boundToWidget"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>boundToWidget</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #boundToWidget}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetHideInterval","fromProperty":"hideInterval"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>hideInterval</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #hideInterval}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetHideOnHover","fromProperty":"hideOnHover"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>hideOnHover</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #hideOnHover}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetMousePointerOffsetX","fromProperty":"mousePointerOffsetX"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>mousePointerOffsetX</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #mousePointerOffsetX}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetMousePointerOffsetY","fromProperty":"mousePointerOffsetY"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>mousePointerOffsetY</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #mousePointerOffsetY}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetShowInterval","fromProperty":"showInterval"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>showInterval</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #showInterval}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setBoundToWidget","fromProperty":"boundToWidget"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>boundToWidget</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>boundToWidget</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #boundToWidget}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setHideInterval","fromProperty":"hideInterval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>hideInterval</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>hideInterval</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #hideInterval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setHideOnHover","fromProperty":"hideOnHover"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>hideOnHover</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>hideOnHover</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #hideOnHover}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setMousePointerOffsetX","fromProperty":"mousePointerOffsetX"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>mousePointerOffsetX</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>mousePointerOffsetX</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #mousePointerOffsetX}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setMousePointerOffsetY","fromProperty":"mousePointerOffsetY"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>mousePointerOffsetY</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>mousePointerOffsetY</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #mousePointerOffsetY}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setShowInterval","fromProperty":"showInterval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>showInterval</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>showInterval</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #showInterval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleHideOnHover","fromProperty":"hideOnHover"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>hideOnHover</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #hideOnHover}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"name":"appearance","docFrom":"qx.ui.core.Widget","defaultValue":"\"tool-tip\"","refine":"true","propertyType":"new","overriddenFrom":"qx.ui.popup.Popup"}},
    {type:"property",attributes:{"apply":"_applyBoundToWidget","propertyType":"new","name":"boundToWidget","check":"qx.ui.core.Widget"},children:[
      {type:"desc",attributes:{"text":"<p>Widget to which the tooltip is bound to</p>"}}
      ]},
    {type:"property",attributes:{"apply":"_applyHideInterval","defaultValue":"4000","propertyType":"new","name":"hideInterval","check":"Integer"},children:[
      {type:"desc",attributes:{"text":"<p>Interval after the tooltip is hidden (in milliseconds)</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"true","propertyType":"new","name":"hideOnHover","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Controls whether the tooltip is hidden when hovered across</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"1","propertyType":"new","name":"mousePointerOffsetX","check":"Integer"},children:[
      {type:"desc",attributes:{"text":"<p>Horizontal offset of the mouse pointer (in pixel)</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"20","propertyType":"new","name":"mousePointerOffsetY","check":"Integer"},children:[
      {type:"desc",attributes:{"text":"<p>Vertical offset of the mouse pointer (in pixel)</p>"}}
      ]},
    {type:"property",attributes:{"apply":"_applyShowInterval","defaultValue":"1000","propertyType":"new","name":"showInterval","check":"Integer"},children:[
      {type:"desc",attributes:{"text":"<p>Interval after the tooltip is shown (in milliseconds)</p>"}}
      ]}
    ]}
  ]}