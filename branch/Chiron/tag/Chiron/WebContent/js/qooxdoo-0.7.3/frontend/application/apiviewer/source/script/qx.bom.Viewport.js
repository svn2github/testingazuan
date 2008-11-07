{type:"class",attributes:{"isStatic":"true","name":"Viewport","packageName":"qx.bom","fullName":"qx.bom.Viewport","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Includes library functions to work with the client&#8217;s viewport (window).</p>"}},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"getHeight"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"defaultValue":"window","name":"win"},children:[
          {type:"desc",attributes:{"text":"<p>The window to query</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Window"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Returns the current height of the viewport (excluding a eventually visible scrollbar).</p>\n\n<code>clientHeight</code> is the inner height of an element in pixels. It includes padding\nbut not the vertical scrollbar (if present, if rendered), border or margin.\n\n<p>The property <code>innerHeight</code> is not useable as defined by the standard as it includes the scrollbars\nwhich is not the indented behavior of this method. We can decrement the size by the scrollbar\nsize but there are easier possibilities to work around this.</p>\n\n<p>Safari 2 and 3 beta (3.0.2) do not correctly implement <code>clientHeight</code> on documentElement/body,\nbut <code>innerHeight</code> works there. Interesting is that webkit do not correctly implement\n<code>innerHeight</code>, too. It calculates the size excluding the scroll bars and this\ndiffers from the behavior of all other browsers &#8211; but this is exactly what we want to have\nin this case.</p>\n\n<p>Opera as of 9.21 only works well using <code>body.clientHeight</code>.</p>\n\n<p>Verified to correctly work with:</p>\n\n<ul>\n<li>Mozilla Firefox 2.0.0.4</li>\n<li>Opera 9.2.1</li>\n<li>Safari 3.0 beta (3.0.2)</li>\n<li>Internet Explorer 7.0</li>\n</ul>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The Height of the viewable area of the page (excludes scrollbars).</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getScrollLeft"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"defaultValue":"window","name":"win"},children:[
          {type:"desc",attributes:{"text":"<p>The window to query</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Window"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Returns the scroll position of the viewport</p>\n\n<p>All clients except <span class=\"caps\">MSHTML</span> supports the non-standard property <code>pageXOffset</code>.\nAs this is easier to evaluate we prefer this property over <code>scrollLeft</code>.</p>\n\n<p>For <span class=\"caps\">MSHTML</span> the access method differs between standard and quirks mode;\nas this can differ from document to document this test must be made on\neach query.</p>\n\n<p>Verified to correctly work with:</p>\n\n<ul>\n<li>Mozilla Firefox 2.0.0.4</li>\n<li>Opera 9.2.1</li>\n<li>Safari 3.0 beta (3.0.2)</li>\n<li>Internet Explorer 7.0</li>\n</ul>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Scroll position from left edge, always a positive integer</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getScrollTop"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"defaultValue":"window","name":"win"},children:[
          {type:"desc",attributes:{"text":"<p>The window to query</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Window"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Returns the scroll position of the viewport</p>\n\n<p>All clients except <span class=\"caps\">MSHTML</span> supports the non-standard property <code>pageYOffset</code>.\nAs this is easier to evaluate we prefer this property over <code>scrollTop</code>.</p>\n\n<p>For <span class=\"caps\">MSHTML</span> the access method differs between standard and quirks mode;\nas this can differ from document to document this test must be made on\neach query.</p>\n\n<p>Verified to correctly work with:</p>\n\n<ul>\n<li>Mozilla Firefox 2.0.0.4</li>\n<li>Opera 9.2.1</li>\n<li>Safari 3.0 beta (3.0.2)</li>\n<li>Internet Explorer 7.0</li>\n</ul>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Scroll position from left edge, always a positive integer</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getWidth"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"defaultValue":"window","name":"win"},children:[
          {type:"desc",attributes:{"text":"<p>The window to query</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Window"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Returns the current width of the viewport (excluding a eventually visible scrollbar).</p>\n\n<code>clientWidth</code> is the inner width of an element in pixels. It includes padding\nbut not the vertical scrollbar (if present, if rendered), border or margin.\n\n<p>The property <code>innerWidth</code> is not useable as defined by the standard as it includes the scrollbars\nwhich is not the indented behavior of this method. We can decrement the size by the scrollbar\nsize but there are easier possibilities to work around this.</p>\n\n<p>Safari 2 and 3 beta (3.0.2) do not correctly implement <code>clientWidth</code> on documentElement/body,\nbut <code>innerWidth</code> works there. Interesting is that webkit do not correctly implement\n<code>innerWidth</code>, too. It calculates the size excluding the scroll bars and this\ndiffers from the behavior of all other browsers &#8211; but this is exactly what we want to have\nin this case.</p>\n\n<p>Opera as of 9.21 only works well using <code>body.clientWidth</code>.</p>\n\n<p>Verified to correctly work with:</p>\n\n<ul>\n<li>Mozilla Firefox 2.0.0.4</li>\n<li>Opera 9.2.1</li>\n<li>Safari 3.0 beta (3.0.2)</li>\n<li>Internet Explorer 7.0</li>\n</ul>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The width of the viewable area of the page (excludes scrollbars).</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]}
    ]}
  ]}