{type:"class",attributes:{"name":"Command","hasWarning":"true","packageName":"qx.client","superClass":"qx.core.Target","fullName":"qx.client.Command","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Commands can be used to globally define keyboard shortcuts. They could\nalso be used to assign a execution of a command sequence to multiple\nwidgets. It is possible to use the same Command in a MenuButton and\nToolBarButton for example.</p>"}},
  {type:"events",children:[
    {type:"event",attributes:{"name":"changeEnabled"},children:[
      {type:"desc",attributes:{"text":"Fired on change of the property {@link #enabled}."}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.ChangeEvent"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"execute"},children:[
      {type:"desc",attributes:{"text":"<p>Fired when the command is executed. Sets the &#8220;data&#8221; property of the event to\nthe object that issued the command.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.DataEvent"}}
        ]}
      ]}
    ]},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"shortcut"},children:[
          {type:"desc",attributes:{"text":"<p>shortcuts can be composed of optional modifier\n   keys Control, Alt, Shift, Meta and a non modifier key.\n   If no non modifier key is specified, the second paramater is evaluated.\n   The key must be seperated by a <code>+</code> or <code>-</code> character.\n   Examples: Alt+F1, Control+C, Control+Alt+Enf</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"keyCode"},children:[
          {type:"desc",attributes:{"text":"<p>Additional key of the command interpreted as a keyCode.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Create a new instance of Command</p>"}}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"private","name":"__oldKeyNameToKeyIdentifier"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"keyName"},children:[
          {type:"desc",attributes:{"text":"<p>old name of the key.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>converts an old key name as found in {@link qx.event.type.KeyEvent.keys} to\nthe new keyIdentifier.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>corresponding keyIdentifier or &#8220;Unidentified&#8221; if a conversion was not possible</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","apply":"qx.client.Command#shortcut","name":"_applyShortcut"},children:[
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
    {type:"method",attributes:{"name":"execute"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vTarget"},children:[
          {type:"desc",attributes:{"text":"<p>Object which issued the execute event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Fire the &#8220;execute&#8221; event on this command.</p>"}}
      ]},
    {type:"method",attributes:{"name":"getEnabled","fromProperty":"enabled"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>enabled</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>enabled</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getKeyCode","fromProperty":"keyCode"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>keyCode</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #keyCode}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>keyCode</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getKeyCode"},children:[
      {type:"deprecated"},
      {type:"desc",attributes:{"text":"<p>Supports old keyCode layer\nStill there for compatibility with the old key handler/commands</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>keyCode</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getKeyIdentifier","fromProperty":"keyIdentifier"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>keyIdentifier</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #keyIdentifier}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>keyIdentifier</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getShortcut","fromProperty":"shortcut"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>shortcut</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #shortcut}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>shortcut</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initEnabled","fromProperty":"enabled"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>enabled</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>enabled</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initKeyCode","fromProperty":"keyCode"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>keyCode</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>keyCode</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #keyCode}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initKeyIdentifier","fromProperty":"keyIdentifier"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>keyIdentifier</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>keyIdentifier</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #keyIdentifier}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initShortcut","fromProperty":"shortcut"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>shortcut</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>shortcut</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #shortcut}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isEnabled","fromProperty":"enabled"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>enabled</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"matchesKeyEvent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>the key event object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.KeyEvent"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Checks whether the given key event matches the command&#8217;s shortcut</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>whether the commands shortcut matches the key event</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetEnabled","fromProperty":"enabled"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>enabled</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetKeyCode","fromProperty":"keyCode"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>keyCode</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #keyCode}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetKeyIdentifier","fromProperty":"keyIdentifier"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>keyIdentifier</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #keyIdentifier}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetShortcut","fromProperty":"shortcut"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>shortcut</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #shortcut}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setEnabled","fromProperty":"enabled"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>enabled</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>enabled</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setKeyCode","fromProperty":"keyCode"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>keyCode</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>keyCode</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #keyCode}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setKeyCode"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"code"},children:[
          {type:"desc",attributes:{"text":"<p>keyCode</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]}
        ]},
      {type:"deprecated"},
      {type:"desc",attributes:{"text":"<p>Supports old keyCode layer\nStill there for compatibility with the old key handler/commands</p>"}}
      ]},
    {type:"method",attributes:{"name":"setKeyIdentifier","fromProperty":"keyIdentifier"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>keyIdentifier</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>keyIdentifier</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #keyIdentifier}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setShortcut","fromProperty":"shortcut"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>shortcut</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>shortcut</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #shortcut}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleEnabled","fromProperty":"enabled"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>enabled</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #enabled}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"overriddenFrom":"qx.core.Object","name":"toString"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the shortcut as string</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>shortcut</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"check":"Boolean","defaultValue":"true","propertyType":"new","name":"enabled","event":"changeEnabled"},children:[
      {type:"desc",attributes:{"text":"<p>whether the command should be respected/enabled</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","hasError":"true","propertyType":"new","name":"keyCode","check":"Number"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"108"}}
        ]}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"keyIdentifier","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>The key identifier</p>"}}
      ]},
    {type:"property",attributes:{"check":"String","allowNull":"true","propertyType":"new","name":"shortcut","apply":"_applyShortcut"},children:[
      {type:"desc",attributes:{"text":"<p>The command shortcut</p>"}}
      ]}
    ]}
  ]}