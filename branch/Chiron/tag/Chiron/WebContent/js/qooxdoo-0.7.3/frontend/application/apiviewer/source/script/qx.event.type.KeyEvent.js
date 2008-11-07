{type:"class",attributes:{"name":"KeyEvent","packageName":"qx.event.type","superClass":"qx.event.type.DomEvent","fullName":"qx.event.type.KeyEvent","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>A key event instance contains all data for each occured key event</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vType"},children:[
          {type:"desc",attributes:{"text":"<p>event type (keydown, keypress, keyinput, keyup)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vDomEvent"},children:[
          {type:"desc",attributes:{"text":"<p><span class=\"caps\">DOM</span> event object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vDomTarget"},children:[
          {type:"desc",attributes:{"text":"<p>target element of the <span class=\"caps\">DOM</span> event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vTarget"}},
        {type:"param",attributes:{"name":"vOriginalTarget"}},
        {type:"param",attributes:{"name":"vKeyCode"},children:[
          {type:"desc",attributes:{"text":"<p>emulated key code for compatibility with older qoodoo applications</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vCharCode"},children:[
          {type:"desc",attributes:{"text":"<p>char code from the &#8220;keypress&#8221; event</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vKeyIdentifier"},children:[
          {type:"desc",attributes:{"text":"<p>the key identifier</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"oldProperty":"true","name":"charCode","propertyType":"fast"},children:[
      {type:"desc",attributes:{"text":"<p>Unicode number of the pressed character.\nOnly valid in &#8220;keyinput&#8221; events</p>"}}
      ]},
    {type:"property",attributes:{"oldProperty":"true","name":"keyIdentifier","propertyType":"fast"},children:[
      {type:"desc",attributes:{"text":"<p>Identifier of the pressed key. This property is modeled after the <em>KeyboardEvent.keyIdentifier</em> property\nof the W3C <span class=\"caps\">DOM</span> 3 event specification (<a href=\"http://www.w3.org/TR/2003/NOTE-DOM-Level-3-Events-20031107/events.html#Events-KeyboardEvent-keyIdentifier\">http://www.w3.org/TR/2003/NOTE-DOM-Level-3-Events-20031107/events.html#Events-KeyboardEvent-keyIdentifier</a>).</p>\n\n<p>It is not valid in &#8220;keyinput&#8221; events&#8221;</p>\n\n<p>Printable keys are represented by a unicode string, non-printable keys have one of the following\nvalues:</p>\n\n<table>\n<tr><th>Backspace</th><td>The Backspace (Back) key.</td></tr>\n<tr><th>Tab</th><td>The Horizontal Tabulation (Tab) key.</td></tr>\n<tr><th>Space</th><td>The Space (Spacebar) key.</td></tr>\n<tr><th>Enter</th><td>The Enter key. Note: This key identifier is also used for the Return (Macintosh numpad) key.</td></tr>\n<tr><th>Shift</th><td>The Shift key.</td></tr>\n<tr><th>Control</th><td>The Control (Ctrl) key.</td></tr>\n<tr><th>Alt</th><td>The Alt (Menu) key.</td></tr>\n<tr><th>CapsLock</th><td>The CapsLock key</td></tr>\n<tr><th>Meta</th><td>The Meta key. (Apple Meta and Windows key)</td></tr>\n<tr><th>Escape</th><td>The Escape (Esc) key.</td></tr>\n<tr><th>Left</th><td>The Left Arrow key.</td></tr>\n<tr><th>Up</th><td>The Up Arrow key.</td></tr>\n<tr><th>Right</th><td>The Right Arrow key.</td></tr>\n<tr><th>Down</th><td>The Down Arrow key.</td></tr>\n<tr><th>PageUp</th><td>The Page Up key.</td></tr>\n<tr><th>PageDown</th><td>The Page Down (Next) key.</td></tr>\n<tr><th>End</th><td>The End key.</td></tr>\n<tr><th>Home</th><td>The Home key.</td></tr>\n<tr><th>Insert</th><td>The Insert (Ins) key. (Does not fire in Opera/Win)</td></tr>\n<tr><th>Delete</th><td>The Delete (Del) Key.</td></tr>\n<tr><th>F1</th><td>The F1 key.</td></tr>\n<tr><th>F2</th><td>The F2 key.</td></tr>\n<tr><th>F3</th><td>The F3 key.</td></tr>\n<tr><th>F4</th><td>The F4 key.</td></tr>\n<tr><th>F5</th><td>The F5 key.</td></tr>\n<tr><th>F6</th><td>The F6 key.</td></tr>\n<tr><th>F7</th><td>The F7 key.</td></tr>\n<tr><th>F8</th><td>The F8 key.</td></tr>\n<tr><th>F9</th><td>The F9 key.</td></tr>\n<tr><th>F10</th><td>The F10 key.</td></tr>\n<tr><th>F11</th><td>The F11 key.</td></tr>\n<tr><th>F12</th><td>The F12 key.</td></tr>\n<tr><th>NumLock</th><td>The Num Lock key.</td></tr>\n<tr><th>PrintScreen</th><td>The Print Screen (PrintScrn, SnapShot) key.</td></tr>\n<tr><th>Scroll</th><td>The scroll lock key</td></tr>\n<tr><th>Pause</th><td>The pause/break key</td></tr>\n<tr><th>Win</th><td>The Windows Logo key</td></tr>\n<tr><th>Apps</th><td>The Application key (Windows Context Menu)</td></tr>\n</table>"}}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"name":"getCharCode","fromProperty":"charCode"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>charCode</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #charCode}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>charCode</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getKeyCode"},children:[
      {type:"deprecated",children:[
        {type:"desc",attributes:{"text":"<p>Will be removed with qooxdoo 0.7</p>"}}
        ]},
      {type:"desc",attributes:{"text":"<p>Legacy keycode</p>"}}
      ]},
    {type:"method",attributes:{"name":"getKeyIdentifier","fromProperty":"keyIdentifier"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>keyIdentifier</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #keyIdentifier}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>keyIdentifier</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]}
    ]}
  ]}