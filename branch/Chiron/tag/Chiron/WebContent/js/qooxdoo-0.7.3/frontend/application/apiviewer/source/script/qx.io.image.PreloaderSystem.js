{type:"class",attributes:{"name":"PreloaderSystem","packageName":"qx.io.image","superClass":"qx.core.Target","fullName":"qx.io.image.PreloaderSystem","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>The image preloader can be used to fill the browser cache with images,\nwhich are needed later. Once all images are pre loaded a &#8220;complete&#8221;\nevent is fired.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vPreloadList"},children:[
          {type:"desc",attributes:{"text":"<p>list of image URLs to preload</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String","dimensions":"1"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vCallBack"},children:[
          {type:"desc",attributes:{"text":"<p>callback function. This function gets called after the\n   preloading is completed</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Function"}}
            ]}
          ]},
        {type:"param",attributes:{"defaultValue":"window","name":"vCallBackScope"},children:[
          {type:"desc",attributes:{"text":"<p>scope for the callback function</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>If the callback is provided the preloading starts automatically and the callback\nis called on completion of the pre loading. Otherwhise the pre loading has to be\nstarted manually using {@link #start}.</p>"}}
      ]}
    ]},
  {type:"events",children:[
    {type:"event",attributes:{"name":"completed"},children:[
      {type:"desc",attributes:{"text":"<p>Fired after the pre loading of the images is complete</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"private","name":"__onerror"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Error handler</p>"}}
      ]},
    {type:"method",attributes:{"access":"private","name":"__oninterval"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Timer interval handler</p>"}}
      ]},
    {type:"method",attributes:{"access":"private","name":"__onload"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event object</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Load event handler</p>"}}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_check"},children:[
      {type:"desc",attributes:{"text":"<p>Checks whether the pre loading is complete and dispatches the &#8220;complete&#8221; event.</p>"}}
      ]},
    {type:"method",attributes:{"name":"start"},children:[
      {type:"desc",attributes:{"text":"<p>Start the preloading</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]}
    ]}
  ]}