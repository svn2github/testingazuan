{type:"class",attributes:{"isStatic":"true","name":"ScrollIntoView","packageName":"qx.html","fullName":"qx.html.ScrollIntoView","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Functions to scroll <span class=\"caps\">DOM</span> elements into the visible area of the parent element</p>"}},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"scrollX"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vElement"},children:[
          {type:"desc",attributes:{"text":"<p><span class=\"caps\">DOM</span> node to be scrolled into view</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"false","name":"vAlignLeft"},children:[
          {type:"desc",attributes:{"text":"<p>whether the element should be left aligned</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Scroll the parent <span class=\"caps\">DOM</span> element so that the element&#8217;s so that the x coordinate is inside\nthe visible area of the parent.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the element could be scrolled into the view</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"isStatic":"true","name":"scrollY"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vElement"},children:[
          {type:"desc",attributes:{"text":"<p><span class=\"caps\">DOM</span> node to be scrolled into view</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Element"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"false","name":"vAlignTop"},children:[
          {type:"desc",attributes:{"text":"<p>whether the element should be top aligned</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Scroll the parent <span class=\"caps\">DOM</span> element so that the element&#8217;s so that the y coordinate is inside\nthe visible area of the parent.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the element could be scrolled into the view</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]}
    ]}
  ]}