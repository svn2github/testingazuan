{type:"class",attributes:{"isStatic":"true","name":"Style","packageName":"qx.bom.element","fullName":"qx.bom.element.Style","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Style querying and modification of <span class=\"caps\">HTML</span> elements.</p>\n\n<p>Automatically normalizes cross-browser differences. Optimized for\nperformance.</p>"}},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"get"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"element"},children:[
          {type:"desc",attributes:{"text":"<p>The <span class=\"caps\">DOM</span> element to modify</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"name"},children:[
          {type:"desc",attributes:{"text":"<p>Name of the style attribute (js variant e.g. marginTop, wordSpacing)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"mode"},children:[
          {type:"desc",attributes:{"text":"<p>Choose one of the modes {@link #COMPUTED_MODE}, {@link #CASCADED_MODE},\n  {@link #LOCAL_MODE}. The computed mode is the default one.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Number"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"true","name":"smart"},children:[
          {type:"desc",attributes:{"text":"<p>Whether the implementation should automatically use\n   special implementations for some properties</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Gets the value of a style property.</p>\n\n<p><strong>Computed</strong></p>\n\n<p>Returns the computed value of a style property. Compared to the cascaded style,\nthis one also interprets the values e.g. translates <code>em</code> units to\n<code>px</code>.</p>\n\n<p><strong>Cascaded</strong></p>\n\n<p>Returns the cascaded value of a style property.</p>\n\n<p><strong>Local</strong></p>\n\n<p>Ignores inheritance cascade. Does not interpret values.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The value of the property</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"getCss"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"element"},children:[
          {type:"desc",attributes:{"text":"<p>The <span class=\"caps\">DOM</span> element to query</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Returns the full content of the style attribute.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the full <span class=\"caps\">CSS</span> string</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"reset"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"element"},children:[
          {type:"desc",attributes:{"text":"<p>The <span class=\"caps\">DOM</span> element to modify</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"name"},children:[
          {type:"desc",attributes:{"text":"<p>Name of the style attribute (js variant e.g. marginTop, wordSpacing)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"true","name":"smart"},children:[
          {type:"desc",attributes:{"text":"<p>Whether the implementation should automatically use\n   special implementations for some properties</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Resets the value of a style property</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"set"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"element"},children:[
          {type:"desc",attributes:{"text":"<p>The <span class=\"caps\">DOM</span> element to modify</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"name"},children:[
          {type:"desc",attributes:{"text":"<p>Name of the style attribute (js variant e.g. marginTop, wordSpacing)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>The value for the given style</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"true","name":"smart"},children:[
          {type:"desc",attributes:{"text":"<p>Whether the implementation should automatically use\n   special implementations for some properties</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the value of a style property</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"setCss"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"element"},children:[
          {type:"desc",attributes:{"text":"<p>The <span class=\"caps\">DOM</span> element to modify</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>The full <span class=\"caps\">CSS</span> string</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Set the full <span class=\"caps\">CSS</span> content of the style attribute</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]}
    ]},
  {type:"constants",children:[
    {type:"constant",attributes:{"type":"Number","name":"COMPUTED_MODE","value":"1"},children:[
      {type:"desc",attributes:{"text":"<p>Computed value of a style property. Compared to the cascaded style,\nthis one also interprets the values e.g. translates <code>em</code> units to\n<code>px</code>.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"Integer"}}
        ]}
      ]},
    {type:"constant",attributes:{"type":"Number","name":"CASCADED_MODE","value":"2"},children:[
      {type:"desc",attributes:{"text":"<p>Cascaded value of a style property.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"Integer"}}
        ]}
      ]},
    {type:"constant",attributes:{"type":"Number","name":"LOCAL_MODE","value":"3"},children:[
      {type:"desc",attributes:{"text":"<p>Local value of a style property. Ignores inheritance cascade. Does not interpret values.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"Integer"}}
        ]}
      ]}
    ]}
  ]}