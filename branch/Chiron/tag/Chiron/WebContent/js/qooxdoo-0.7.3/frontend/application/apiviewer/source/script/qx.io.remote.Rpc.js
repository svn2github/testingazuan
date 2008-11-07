{type:"class",attributes:{"name":"Rpc","hasWarning":"true","packageName":"qx.io.remote","superClass":"qx.core.Target","fullName":"qx.io.remote.Rpc","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Provides a Remote Procedure Call (RPC) implementation.</p>\n\n<p>Each instance of this class represents a &#8220;Service&#8221;. These services can\ncorrespond to various concepts on the server side (depending on the\nprogramming language/environment being used), but usually, a service means\na class on the server.</p>\n\n<p>In case multiple instances of the same service are needed, they can be\ndistinguished by ids. If such an id is specified, the server routes all\ncalls to a service that have the same id to the same server-side instance.</p>\n\n<p>When calling a server-side method, the parameters and return values are\nconverted automatically. Supported types are int (and Integer), double\n(and Double), String, Date, Map, and JavaBeans. Beans must have a default\nconstructor on the server side and are represented by simple JavaScript\nobjects on the client side (used as associative arrays with keys matching\nthe server-side properties). Beans can also be nested, but be careful not to\ncreate circular references! There are no checks to detect these (which would\nbe expensive), so you as the user are responsible for avoiding them.</p>\n\n<p>A simple example:</p>\n\n<pre class=\"javascript\">\n  function callRpcServer ()\n  {\n    var rpc = new qx.io.remote.Rpc();\n    rpc.setTimeout(10000);\n    rpc.setUrl(\"http://127.0.0.1:8007\");\n    rpc.setServiceName(\"qooxdoo.admin\");\n\n    // call a remote procedure -- takes no arguments, returns a string\n    var that = this;\n    this.RpcRunning = rpc.callAsync(\n      function(result, ex, id)\n      {\n        that.RpcRunning = null;\n        if (ex == null) {\n            alert(result);\n        } else {\n            alert(\"Async(\" + id + \") exception: \" + ex);\n        }\n      },\n      \"fss.getBaseDir\");\n  }\n</pre>\n\n<p><i>fss.getBaseDir</i> is the remote procedure in this case, potential arguments\nwould be listed after the procedure name.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"url"},children:[
          {type:"desc",attributes:{"text":"<p>identifies the url where the service\n                               is found.  Note that if the url is to\n                               a domain (server) other than where the\n                               qooxdoo script came from, i.e. it is\n                               cross-domain, then you must also call\n                               the setCrossDomain(true) method to\n                               enable the ScriptTransport instead of\n                               the XmlHttpTransport, since the latter\n                               can not handle cross-domain requests.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"serviceName"},children:[
          {type:"desc",attributes:{"text":"<p>identifies the service. For the Java\n                               implementation, this is the fully\n                               qualified name of the class that offers\n                               the service methods\n                               (e.g. &#8220;my.pkg.MyService&#8221;).</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]}
      ]}
    ]},
  {type:"events",children:[
    {type:"event",attributes:{"hasError":"true","name":"aborted"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"139"}}
        ]},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]},
    {type:"event",attributes:{"hasError":"true","name":"completed"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"139"}}
        ]},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]},
    {type:"event",attributes:{"hasError":"true","name":"failed"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"139"}}
        ]},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]},
    {type:"event",attributes:{"hasError":"true","name":"timeout"},children:[
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"3","line":"139"}}
        ]},
      {type:"types",children:[
        {type:"entry",attributes:{"type":"qx.event.type.Event"}}
        ]}
      ]}
    ]},
  {type:"methods-static",children:[
    {type:"method",attributes:{"isStatic":"true","name":"makeServerURL"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"defaultValue":"null","name":"instanceId"},children:[
          {type:"desc",attributes:{"text":"<p>an optional identifier for the\n                                  server side instance that should be\n                                  used. All calls to the same service\n                                  with the same instance id are\n                                  routed to the same object instance\n                                  on the server. The instance id can\n                                  also be used to provide additional\n                                  data for the service instantiation\n                                  on the server.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Creates an <span class=\"caps\">URL</span> for talking to a local service. A local service is one that\nlives in the same application as the page calling the service. For backends\nthat don&#8217;t support this auto-generation, this method returns null.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the url.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"protected","name":"_callInternal"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"args"},children:[
          {type:"desc",attributes:{"text":"<p>array of arguments</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Array"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"callType"},children:[
          {type:"desc",attributes:{"text":"<p>0 = sync,\n  1 = async with handler,\n  2 = async event listeners</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Integer"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"refreshSession"},children:[
          {type:"desc",attributes:{"text":"<p>whether a new session should be requested</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Internal <span class=\"caps\">RPC</span> call method</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"abort"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"opaqueCallRef"},children:[
          {type:"desc",attributes:{"text":"<p>the call reference as returned by\n                           <code>callAsync</code> or\n                           <code>callAsyncListeners</code></p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Aborts an asynchronous server call. Consequently, the callback function\nprovided to <code>callAsync</code> or <code>callAsyncListeners</code>\nwill be called with an exception.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"callAsync"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"handler"},children:[
          {type:"desc",attributes:{"text":"<p>the callback function.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Function"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"methodName"},children:[
          {type:"desc",attributes:{"text":"<p>the name of the method to call.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Makes an asynchronous server call. The method arguments (if any) follow\nafter the method name (as normal JavaScript arguments, separated by\ncommas, not as an array).</p>\n\n<p>When an answer from the server arrives, the <code>handler</code>\nfunction is called with the result of the call as the first, an\nexception as the second parameter, and the id (aka sequence number) of\nthe invoking request as the third parameter. If the call was\nsuccessful, the second parameter is <code>null</code>. If there was a\nproblem, the second parameter contains an exception, and the first one\nis <code>null</code>.</p>\n\n<p>The return value of this method is a call reference that you can store\nif you want to abort the request later on. This value should be treated\nas opaque and can change completely in the future! The only thing you\ncan rely on is that the <code>abort</code> method will accept this\nreference and that you can retrieve the sequence number of the request\nby invoking the getSequenceNumber() method (see below).</p>\n\n<p>If a specific method is being called, asynchronously, a number of times\nin succession, the getSequenceNumber() method may be used to\ndisambiguate which request a response corresponds to.  The sequence\nnumber value is a value which increments with each request.)</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the method call reference.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"callAsyncListeners"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"coalesce"},children:[
          {type:"desc",attributes:{"text":"<p>coalesce all failure types (&#8220;failed&#8221;,\n                          &#8220;timeout&#8221;, and &#8220;aborted&#8221;) to &#8220;failed&#8221;.\n                          This is reasonable in many cases, as\n                          the provided exception contains adequate\n                          disambiguating information.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"methodName"},children:[
          {type:"desc",attributes:{"text":"<p>the name of the method to call.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Makes an asynchronous server call and dispatch an event upon completion\nor failure. The method arguments (if any) follow after the method name\n(as normal JavaScript arguments, separated by commas, not as an array).</p>\n\n<p>When an answer from the server arrives (or fails to arrive on time), if\nan exception occurred, a &#8220;failed&#8221;, &#8220;timeout&#8221; or &#8220;aborted&#8221; event, as\nappropriate, is dispatched to any waiting event listeners.  If no\nexception occurred, a &#8220;completed&#8221; event is dispatched.</p>\n\n<p>When a &#8220;failed&#8221;, &#8220;timeout&#8221; or &#8220;aborted&#8221; event is dispatched, the event\ndata contains an object with the properties &#8216;origin&#8217;, &#8216;code&#8217;, &#8216;message&#8217;\nand &#8216;id&#8217;.  The object has a toString() function which may be called to\nconvert the exception to a string.</p>\n\n<p>When a &#8220;completed&#8221; event is dispatched, the event data contains the\n<span class=\"caps\">JSON</span>-RPC result.</p>\n\n<p>The return value of this method is a call reference that you can store\nif you want to abort the request later on. This value should be treated\nas opaque and can change completely in the future! The only thing you\ncan rely on is that the <code>abort</code> method will accept this\nreference and that you can retrieve the sequence number of the request\nby invoking the getSequenceNumber() method (see below).</p>\n\n<p>If a specific method is being called, asynchronously, a number of times\nin succession, the getSequenceNumber() method may be used to\ndisambiguate which request a response corresponds to.  The sequence\nnumber value is a value which increments with each request.)</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the method call reference.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"callSync"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"methodName"},children:[
          {type:"desc",attributes:{"text":"<p>the name of the method to call.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Makes a synchronous server call. The method arguments (if any) follow\nafter the method name (as normal JavaScript arguments, separated by\ncommas, not as an array).</p>\n\n<p>If a problem occurs when making the call, an exception is thrown.</p>\n\n<p><span class=\"caps\">WARNING</span>.  With some browsers, the synchronous interface\ncauses the browser to hang while awaiting a response!  If the server\ndecides to pause for a minute or two, your browser may do nothing\n(including refreshing following window changes) until the response is\nreceived.  Instead, use the asynchronous interface.</p>\n\n<p><span class=\"caps\">YOU</span> <span class=\"caps\">HAVE</span> <span class=\"caps\">BEEN</span> <span class=\"caps\">WARNED</span>.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the result returned by the server.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"fixUrl"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"url"},children:[
          {type:"desc",attributes:{"text":"<p>the <span class=\"caps\">URL</span> to examine.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Helper method to rewrite a <span class=\"caps\">URL</span> with a stale session id (so that it includes\nthe correct session id afterwards).</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the (possibly re-written) <span class=\"caps\">URL</span>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
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
    {type:"method",attributes:{"name":"getPassword","fromProperty":"password"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>password</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #password}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>password</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getServerData","fromProperty":"serverData"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>serverData</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #serverData}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>serverData</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getServiceName","fromProperty":"serviceName"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>serviceName</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #serviceName}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>serviceName</code>.</p>"}},
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
    {type:"method",attributes:{"access":"protected","name":"initServerData","fromProperty":"serverData"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>serverData</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>serverData</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #serverData}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initServiceName","fromProperty":"serviceName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>serviceName</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>serviceName</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #serviceName}.</p>"}},
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
    {type:"method",attributes:{"name":"isCrossDomain","fromProperty":"crossDomain"},children:[
      {type:"desc",attributes:{"text":"<p>Check whether the (computed) value of the boolean property <code>crossDomain</code> equals <code>true</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #crossDomain}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the property equals <code>true</code>.</p>"}},
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
    {type:"method",attributes:{"name":"refreshSession"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"handler"},children:[
          {type:"desc",attributes:{"text":"<p>a callback function that is called when the\n                          refresh is complete (or failed).</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Function"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Refreshes a server session by retrieving the session id again from the\nserver.</p>\n\n<p>The specified handler function is called when the refresh is\ncomplete. The first parameter can be <code>true</code> (indicating that\na refresh either wasn&#8217;t necessary at this time or it was successful) or\n<code>false</code> (indicating that a refresh would have been necessary\nbut can&#8217;t be performed because the server backend doesn&#8217;t support\nit). If there is a non-null second parameter, it&#8217;s an exception\nindicating that there was an error when refreshing the session.</p>"}},
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
    {type:"method",attributes:{"name":"resetPassword","fromProperty":"password"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>password</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #password}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetServerData","fromProperty":"serverData"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>serverData</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #serverData}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetServiceName","fromProperty":"serviceName"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>serviceName</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #serviceName}.</p>"}},
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
    {type:"method",attributes:{"name":"setServerData","fromProperty":"serverData"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>serverData</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>serverData</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #serverData}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setServiceName","fromProperty":"serviceName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>serviceName</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>serviceName</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #serviceName}.</p>"}},
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
    {type:"method",attributes:{"name":"toggleCrossDomain","fromProperty":"crossDomain"},children:[
      {type:"desc",attributes:{"text":"<p>Toggles the (computed) value of the boolean property <code>crossDomain</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #crossDomain}.</p>"}},
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
    {type:"property",attributes:{"defaultValue":"false","propertyType":"new","name":"crossDomain","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Indicate that the request is cross domain.</p>\n\n<p>A request is cross domain if the request&#8217;s <span class=\"caps\">URL</span> points to a host other\nthan the local host. This switches the concrete implementation that is\nused for sending the request from qx.io.remote.XmlHttpTransport to\nqx.io.remote.ScriptTransport because only the latter can handle cross\ndomain requests.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"password","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>Password to use for <span class=\"caps\">HTTP</span> authentication. Null if <span class=\"caps\">HTTP</span> authentication\nis not used.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"serverData","check":"Object"},children:[
      {type:"desc",attributes:{"text":"<p>Data sent as &#8220;out of band&#8221; data in the request to the server.  The\nformat of the data is opaque to <span class=\"caps\">RPC</span> and may be recognized only by\nparticular servers It is up to the server to decide what to do with\nit: whether to ignore it, handle it locally before calling the\nspecified method, or pass it on to the method.  This server data is\nnot sent to the server if it has been set to &#8216;undefined&#8217;.</p>\n\n<p><span class=\"caps\">TODO</span>: undefined is not supported by the new properties, alternative\nways to implement this? Maybe use null instead?</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"serviceName","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>The service name.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"timeout","check":"Integer"},children:[
      {type:"desc",attributes:{"text":"<p>The timeout for asynchronous calls in milliseconds.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"url","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>The <span class=\"caps\">URL</span> at which the service is located.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"useBasicHttpAuth","check":"Boolean"},children:[
      {type:"desc",attributes:{"text":"<p>Use Basic <span class=\"caps\">HTTP</span> Authentication</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"username","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>Username to use for <span class=\"caps\">HTTP</span> authentication. Null if <span class=\"caps\">HTTP</span> authentication\nis not used.</p>"}}
      ]}
    ]}
  ]}