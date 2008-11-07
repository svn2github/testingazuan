{type:"class",attributes:{"name":"Request","packageName":"qx.io.remote","superClass":"qx.core.Target","fullName":"qx.io.remote.Request","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>This class is used to send <span class=\"caps\">HTTP</span> requests to the server.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vUrl"},children:[
          {type:"desc",attributes:{"text":"<p>Target url to issue the request to.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vMethod"},children:[
          {type:"desc",attributes:{"text":"<p>Determines that type of request to issue (GET or <span class=\"caps\">POST</span>). Default is <span class=\"caps\">GET</span>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vResponseType"},children:[
          {type:"desc",attributes:{"text":"<p>The mime type of the response. Default is text/plain {@link qx.util.Mime}.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]}
      ]}
    ]},
  {type:"events",children:[
    {type:"event",attributes:{"name":"aborted"},children:[
      {type:"desc",attributes:{"text":"<p>Fired when the pending request has been aborted.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.io.remote.Response"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"changeState"},children:[
      {type:"desc",attributes:{"text":"Fired on change of the property {@link #state}."}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.ChangeEvent"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"completed"},children:[
      {type:"desc",attributes:{"text":"<p>Fired once the request has finished successfully. The event object\ncan be used to read the transferred data.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.io.remote.Response"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"configured"},children:[
      {type:"desc",attributes:{"text":"<p>Fired when the Request object changes its state to &#8216;configured&#8217;</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"created"},children:[
      {type:"desc",attributes:{"text":"<p>Fired when the Request object changes its state to &#8216;created&#8217;</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"failed"},children:[
      {type:"desc",attributes:{"text":"<p>Fired when the pending request failes.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.io.remote.Response"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"receiving"},children:[
      {type:"desc",attributes:{"text":"<p>Fired when the Request object changes its state to &#8216;receiving&#8217;</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"sending"},children:[
      {type:"desc",attributes:{"text":"<p>Fired when the Request object changes its state to &#8216;sending&#8217;</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]},
    {type:"event",attributes:{"name":"timeout"},children:[
      {type:"desc",attributes:{"text":"<p>Fired when the pending request times out.</p>"}},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.io.remote.Response"}}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","apply":"qx.io.remote.Request#method","name":"_applyMethod"},children:[
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
    {type:"method",attributes:{"access":"protected","apply":"qx.io.remote.Request#prohibitCaching","name":"_applyProhibitCaching"},children:[
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
    {type:"method",attributes:{"access":"protected","apply":"qx.io.remote.Request#responseType","name":"_applyResponseType"},children:[
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
    {type:"method",attributes:{"access":"protected","apply":"qx.io.remote.Request#state","name":"_applyState"},children:[
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
    {type:"method",attributes:{"access":"protected","name":"_onaborted"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event indicating state change</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event handler called when the request enters the aborted state.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_oncompleted"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event indicating state change</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event handler called when the request enters the completed state.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_onfailed"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event indicating state change</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event handler called when the request enters the failed state.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_onqueued"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event indicating state change</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event handler called when the request enters the queued state.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_onreceiving"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event indicating state change</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event handler called when the request enters the receiving state.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_onsending"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event indicating state change</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event handler called when the request enters the sending state.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"_ontimeout"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"e"},children:[
          {type:"desc",attributes:{"text":"<p>Event indicating state change</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event handler called when the request enters the timeout state.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"abort"},children:[
      {type:"desc",attributes:{"text":"<p>Abort sending this request.</p>\n\n<p>The request is removed from the singleton class qx.io.remote.RequestQueue&#8217;s\nlist of pending events. If the request haven&#8217;t been scheduled this\nmethod is a noop.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getAsynchronous","fromProperty":"asynchronous"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>asynchronous</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #asynchronous}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>asynchronous</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getCrossDomain","fromProperty":"crossDomain"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>crossDomain</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #crossDomain}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>crossDomain</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getData","fromProperty":"data"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>data</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #data}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>data</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getFileUpload","fromProperty":"fileUpload"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>fileUpload</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #fileUpload}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>fileUpload</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getFormField"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vId"},children:[
          {type:"desc",attributes:{"text":"<p>Identifier of the form field to get.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Get a form field in the <span class=\"caps\">POST</span> request.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getFormFields"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the object containg all form fields for the <span class=\"caps\">POST</span> request.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The returned object has as its property names each of the ids of\n    form fields which have been added, and as each property value, the value\n    of the property corresponding to that id.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getMethod","fromProperty":"method"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>method</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #method}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>method</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getParameter"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vId"},children:[
          {type:"desc",attributes:{"text":"<p>Identifier of the parameter to get.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Get a parameter in the request.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getParameters"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the object containg all parameters for the request.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The returned object has as its property names each of the ids of\n    parameters which have been added, and as each property value, the value\n    of the property corresponding to that id.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getPassword","fromProperty":"password"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>password</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #password}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>password</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getProhibitCaching","fromProperty":"prohibitCaching"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>prohibitCaching</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #prohibitCaching}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>prohibitCaching</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getRequestHeader"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vId"},children:[
          {type:"desc",attributes:{"text":"<p>The id of the header value being requested</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Retrieve the value of a header which was previously set</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The value of the header wiith the specified id</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getRequestHeaders"},children:[
      {type:"desc",attributes:{"text":"<p>Return the object containing all of the headers which have been added.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The returned object has as its property names each of the ids of headers\n    which have been added, and as each property value, the value of the\n    property corresponding to that id.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getResponseType","fromProperty":"responseType"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>responseType</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #responseType}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>responseType</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getSequenceNumber"},children:[
      {type:"desc",attributes:{"text":"<p>Obtain the sequence (id) number used for this request</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The sequence number of this request</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Integer"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getState","fromProperty":"state"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>state</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #state}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>state</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getTimeout","fromProperty":"timeout"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>timeout</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #timeout}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>timeout</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getTransport","fromProperty":"transport"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>transport</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #transport}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>transport</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getUrl","fromProperty":"url"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>url</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #url}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>url</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getUseBasicHttpAuth","fromProperty":"useBasicHttpAuth"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>useBasicHttpAuth</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #useBasicHttpAuth}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>useBasicHttpAuth</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getUsername","fromProperty":"username"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>username</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #username}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>username</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initAsynchronous","fromProperty":"asynchronous"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>asynchronous</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>asynchronous</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #asynchronous}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initCrossDomain","fromProperty":"crossDomain"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>crossDomain</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>crossDomain</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #crossDomain}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initData","fromProperty":"data"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>data</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>data</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #data}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initFileUpload","fromProperty":"fileUpload"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>fileUpload</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>fileUpload</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #fileUpload}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initMethod","fromProperty":"method"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>method</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>method</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #method}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initPassword","fromProperty":"password"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>password</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>password</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #password}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initProhibitCaching","fromProperty":"prohibitCaching"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>prohibitCaching</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>prohibitCaching</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #prohibitCaching}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initResponseType","fromProperty":"responseType"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>responseType</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>responseType</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #responseType}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initState","fromProperty":"state"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>state</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>state</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #state}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initTimeout","fromProperty":"timeout"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>timeout</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>timeout</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #timeout}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initTransport","fromProperty":"transport"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>transport</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>transport</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #transport}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initUrl","fromProperty":"url"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>url</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>url</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #url}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initUseBasicHttpAuth","fromProperty":"useBasicHttpAuth"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>useBasicHttpAuth</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>useBasicHttpAuth</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #useBasicHttpAuth}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initUsername","fromProperty":"username"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>username</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>username</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #username}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isAborted"},children:[
      {type:"desc",attributes:{"text":"<p>Determine if this request is in the aborted state.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":" if the request is in the aborted state;  otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isAsynchronous","fromProperty":"asynchronous"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>asynchronous</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #asynchronous}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isCompleted"},children:[
      {type:"desc",attributes:{"text":"<p>Determine if this request is in the completed state.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":" if the request is in the completed state;  otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isConfigured"},children:[
      {type:"desc",attributes:{"text":"<p>Determine if this request is in the configured state.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":" if the request is in the configured state;  otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isCrossDomain","fromProperty":"crossDomain"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>crossDomain</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #crossDomain}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isFailed"},children:[
      {type:"desc",attributes:{"text":"<p>Determine if this request is in the failed state.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":" if the request is in the failed state;  otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isFileUpload","fromProperty":"fileUpload"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>fileUpload</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #fileUpload}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isProhibitCaching","fromProperty":"prohibitCaching"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>prohibitCaching</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #prohibitCaching}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isQueued"},children:[
      {type:"desc",attributes:{"text":"<p>Determine if this request is in the queued state.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":" if the request is in the queued state;  otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isReceiving"},children:[
      {type:"desc",attributes:{"text":"<p>Determine if this request is in the receiving state.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":" if the request is in the receiving state;  otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isSending"},children:[
      {type:"desc",attributes:{"text":"<p>Determine if this request is in the sending state.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":" if the request is in the sending state;  otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isTimeout"},children:[
      {type:"desc",attributes:{"text":"<p>Determine if this request is in the timeout state.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":" if the request is in the timeout state;  otherwise."}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"isUseBasicHttpAuth","fromProperty":"useBasicHttpAuth"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>useBasicHttpAuth</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #useBasicHttpAuth}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"removeFormField"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vId"},children:[
          {type:"desc",attributes:{"text":"<p>Identifier of the form field to remove.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Remove a form field from the <span class=\"caps\">POST</span> request.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"removeParameter"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vId"},children:[
          {type:"desc",attributes:{"text":"<p>Identifier of the parameter to remove.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Remove a parameter from the request.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"removeRequestHeader"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vId"},children:[
          {type:"desc",attributes:{"text":"<p>The id of the header to be removed</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Remove a previously-added request header</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"overriddenFrom":"qx.core.Object","name":"reset"},children:[
      {type:"desc",attributes:{"text":"<p>Abort sending this request if it has not already been aborted.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetAsynchronous","fromProperty":"asynchronous"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>asynchronous</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #asynchronous}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetCrossDomain","fromProperty":"crossDomain"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>crossDomain</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #crossDomain}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetData","fromProperty":"data"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>data</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #data}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetFileUpload","fromProperty":"fileUpload"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>fileUpload</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #fileUpload}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetMethod","fromProperty":"method"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>method</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #method}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetPassword","fromProperty":"password"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>password</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #password}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetProhibitCaching","fromProperty":"prohibitCaching"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>prohibitCaching</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #prohibitCaching}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetResponseType","fromProperty":"responseType"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>responseType</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #responseType}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetState","fromProperty":"state"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>state</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #state}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetTimeout","fromProperty":"timeout"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>timeout</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #timeout}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetTransport","fromProperty":"transport"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>transport</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #transport}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetUrl","fromProperty":"url"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>url</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #url}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetUseBasicHttpAuth","fromProperty":"useBasicHttpAuth"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>useBasicHttpAuth</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #useBasicHttpAuth}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetUsername","fromProperty":"username"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>username</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #username}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"send"},children:[
      {type:"desc",attributes:{"text":"<p>Schedule this request for transport to server.</p>\n\n<p>The request is added to the singleton class qx.io.remote.RequestQueue&#8217;s\nlist of pending requests.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setAsynchronous","fromProperty":"asynchronous"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>asynchronous</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>asynchronous</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #asynchronous}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setCrossDomain","fromProperty":"crossDomain"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>crossDomain</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>crossDomain</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #crossDomain}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setData","fromProperty":"data"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>data</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>data</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #data}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setFileUpload","fromProperty":"fileUpload"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>fileUpload</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>fileUpload</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #fileUpload}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setFormField"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vId"},children:[
          {type:"desc",attributes:{"text":"<p>String identifier of the form field to add.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vValue"},children:[
          {type:"desc",attributes:{"text":"<p>Value of form field</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Add a form field to the <span class=\"caps\">POST</span> request.</p>\n\n<p><span class=\"caps\">NOTE</span>: Adding any programatic form fields using this method will switch the\n      Transport implementation to IframeTransport.</p>\n\n<p><span class=\"caps\">NOTE</span>: Use of these programatic form fields disallow use of synchronous\n      requests and cross-domain requests.  Be sure that you do not need\n      those features when setting these programatic form fields.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setMethod","fromProperty":"method"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>method</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>method</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #method}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setParameter"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vId"},children:[
          {type:"desc",attributes:{"text":"<p>String identifier of the parameter to add.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vValue"},children:[
          {type:"desc",attributes:{"text":"<p>Value of parameter. May be a string (for one parameter) or an array of\n    strings (for setting multiple parameter values with the same parameter\n    name).</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Add a parameter to the request.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setPassword","fromProperty":"password"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>password</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>password</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #password}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setProhibitCaching","fromProperty":"prohibitCaching"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>prohibitCaching</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>prohibitCaching</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #prohibitCaching}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setRequestHeader"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"vId"},children:[
          {type:"desc",attributes:{"text":"<p>The identifier to use for this added header</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"vValue"},children:[
          {type:"desc",attributes:{"text":"<p>The value to use for this added header</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Add a request header to the request.</p>\n\n<p>Example: request.setRequestHeader(&#8220;Content-Type&#8221;, qx.util.Mime.HTML)</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setResponseType","fromProperty":"responseType"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>responseType</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>responseType</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #responseType}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setState","fromProperty":"state"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>state</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>state</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #state}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setTimeout","fromProperty":"timeout"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>timeout</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>timeout</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #timeout}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setTransport","fromProperty":"transport"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>transport</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>transport</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #transport}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setUrl","fromProperty":"url"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>url</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>url</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #url}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setUseBasicHttpAuth","fromProperty":"useBasicHttpAuth"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>useBasicHttpAuth</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>useBasicHttpAuth</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #useBasicHttpAuth}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setUsername","fromProperty":"username"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>username</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>username</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #username}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleAsynchronous","fromProperty":"asynchronous"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>asynchronous</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #asynchronous}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleCrossDomain","fromProperty":"crossDomain"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>crossDomain</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #crossDomain}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleFileUpload","fromProperty":"fileUpload"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>fileUpload</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #fileUpload}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleProhibitCaching","fromProperty":"prohibitCaching"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>prohibitCaching</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #prohibitCaching}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"toggleUseBasicHttpAuth","fromProperty":"useBasicHttpAuth"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>useBasicHttpAuth</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #useBasicHttpAuth}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the new value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"defaultValue":"true","propertyType":"new","name":"asynchronous","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Set the request to asynchronous.</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"false","propertyType":"new","name":"crossDomain","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Indicate that the request is cross domain.</p>\n\n<p>A request is cross domain if the request&#8217;s <span class=\"caps\">URL</span> points to a host other than\nthe local host. This switches the concrete implementation that is used for\nsending the request from qx.io.remote.XmlHttpTransport to\nqx.io.remote.ScriptTransport, because only the latter can handle cross\ndomain requests.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"data","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>Set the data to be sent via this request</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"false","propertyType":"new","name":"fileUpload","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Indicate that the request will be used for a file upload.</p>\n\n<p>The request will be used for a file upload.  This switches the concrete\nimplementation that is used for sending the request from\nqx.io.remote.XmlHttpTransport to qx.io.remote.IFrameTransport, because only\nthe latter can handle file uploads.</p>"}}
      ]},
    {type:"property",attributes:{"apply":"_applyMethod","defaultValue":"qx.net.Http.METHOD_GET","propertyType":"new","name":"method","possibleValues":"qx.net.Http.METHOD_GET,qx.net.Http.METHOD_POST,qx.net.Http.METHOD_PUT,qx.net.Http.METHOD_HEAD,qx.net.Http.METHOD_DELETE"},children:[
      {type:"desc",attributes:{"text":"<p>Determines what type of request to issue (GET or <span class=\"caps\">POST</span>).</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"password","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>Password to use for <span class=\"caps\">HTTP</span> authentication.\nSet to <span class=\"caps\">NULL</span> if <span class=\"caps\">HTTP</span> authentication is not used.</p>"}}
      ]},
    {type:"property",attributes:{"apply":"_applyProhibitCaching","defaultValue":"true","propertyType":"new","name":"prohibitCaching","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Prohibit request from being cached.</p>\n\n<p>Setting the value to <i>true</i> adds a parameter &#8220;nocache&#8221; to the request\nwith a value of the current time. Setting the value to <i>false</i> removes\nthe parameter.</p>"}}
      ]},
    {type:"property",attributes:{"apply":"_applyResponseType","defaultValue":"qx.util.Mime.TEXT","propertyType":"new","name":"responseType","possibleValues":"qx.util.Mime.TEXT,qx.util.Mime.JAVASCRIPT,qx.util.Mime.JSON,qx.util.Mime.XML,qx.util.Mime.HTML"},children:[
      {type:"desc",attributes:{"text":"<p>Response type of request.</p>\n\n<p>The response type is a <span class=\"caps\">MIME</span> type, default is text/plain. Other supported\n<span class=\"caps\">MIME</span> types are text/javascript, text/html, application/json,\napplication/xml.</p>"}}
      ]},
    {type:"property",attributes:{"possibleValues":"\"configured\",\"queued\",\"sending\",\"receiving\",\"completed\",\"aborted\",\"timeout\",\"failed\"","name":"state","defaultValue":"\"configured\"","propertyType":"new","apply":"_applyState","event":"changeState"},children:[
      {type:"desc",attributes:{"text":"<p>The state that the request is in, while being processed.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"timeout","check":"Integer"},children:[
      {type:"desc",attributes:{"text":"<p>Number of millieseconds before the request is being timed out.</p>\n\n<p>If this property is null, the timeout for the request comes is the\nqx.io.remote.RequestQueue&#8217;s property defaultTimeout.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"transport","check":"qx.io.remote.Exchange"},children:[
      {type:"desc",attributes:{"text":"<p>The transport instance used for the request.</p>\n\n<p>This is necessary to be able to abort an asynchronous request.</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"\"\"","propertyType":"new","name":"url","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>Target url to issue the request to.</p>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"false","propertyType":"new","name":"useBasicHttpAuth","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Use Basic <span class=\"caps\">HTTP</span> Authentication.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"username","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>Username to use for <span class=\"caps\">HTTP</span> authentication.\nSet to <span class=\"caps\">NULL</span> if <span class=\"caps\">HTTP</span> authentication is not used.</p>"}}
      ]}
    ]}
  ]}