{type:"class",attributes:{"name":"RepeatButton","packageName":"qx.ui.form","superClass":"qx.ui.form.Button","fullName":"qx.ui.form.RepeatButton","type":"class"},children:[
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vText"}},
        {type:"param",attributes:{"name":"vIcon"}},
        {type:"param",attributes:{"name":"vIconWidth"}},
        {type:"param",attributes:{"name":"vIconHeight"}},
        {type:"param",attributes:{"name":"vFlash"}}
        ]}
      ]}
    ]},
  {type:"events",children:[
    {type:"event",attributes:{"name":"execute"},children:[
      {type:"desc",attributes:{"text":"<p>This event gets dispatched with every interval. The timer gets executed\nas long as the user holds down the mouse button.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","name":"_oninterval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>interval event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Callback for the interval event.</p>\n\n<p>Stops the timer and starts it with a new interval\n(value of the &#8220;interval&#8221; property). Dispatches the\n&#8220;execute&#8221; event.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","overriddenFrom":"qx.ui.form.Button","name":"_onmousedown"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>mouseDown event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.MouseEvent"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Callback method for the &#8220;mouseDown&#8221; method.</p>\n\n<p>Sets the interval of the timer (value of firstInterval property) and\nstarts the timer. Additionally removes the state &#8220;abandoned&#8221; and adds the\nstate &#8220;pressed&#8221;.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","overriddenFrom":"qx.ui.form.Button","name":"_onmouseup"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>mouseUp event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.MouseEvent"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Callback method for the &#8220;mouseUp&#8221; event.</p>\n\n<p>Handles the case that the user is releasing the mouse button\nbefore the timer interval method got executed. This way the\n&#8220;execute&#8221; method get executed at least one time.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getFirstInterval","fromProperty":"firstInterval"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>firstInterval</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #firstInterval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>firstInterval</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getInterval","fromProperty":"interval"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>interval</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #interval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>interval</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initFirstInterval","fromProperty":"firstInterval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>firstInterval</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>firstInterval</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #firstInterval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initInterval","fromProperty":"interval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>interval</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>interval</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #interval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetFirstInterval","fromProperty":"firstInterval"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>firstInterval</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #firstInterval}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetInterval","fromProperty":"interval"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>interval</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #interval}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setFirstInterval","fromProperty":"firstInterval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>firstInterval</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>firstInterval</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #firstInterval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setInterval","fromProperty":"interval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>interval</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>interval</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #interval}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"defaultValue":"500","propertyType":"new","name":"firstInterval","check":"Integer"},children:[
      {type:"desc",attributes:{"text":"<p>Interval used for the first run of the timer. Usually a greater value\nthan the &#8220;interval&#8221; property value to a little delayed reaction at the first\ntime.</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"100","propertyType":"new","name":"interval","check":"Integer"},children:[
      {type:"desc",attributes:{"text":"<p>Interval used after the first run of the timer. Usually a smaller value\nthan the &#8220;firstInterval&#8221; property value to get a faster reaction.</p>"}}
      ]}
    ]}
  ]}