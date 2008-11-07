{type:"class",attributes:{"name":"State","hasWarning":"true","packageName":"qx.util.fsm","superClass":"qx.core.Object","fullName":"qx.util.fsm.State","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>Create a new state which may be added to a finite state machine.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"stateName"},children:[
          {type:"desc",attributes:{"text":"<p>The name of this state.  This is the name which may be referenced in\n  objects of class qx.util.fsm.Transition, when passing of\n  the the transition&#8217;s predicate means transition to this state.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"stateInfo"},children:[
          {type:"desc",attributes:{"text":"<pre>\n  An object containing any of the following properties:\n\n    onentry -\n      A function which is called upon entry to the state.  Its signature\n      is function(fsm, event) and it is saved in the onentry property of\n      the state object.  (This function is called after the Transition's\n      action function and after the previous state's onexit function.)\n\n      In the onentry function:\n\n        fsm -\n          The finite state machine object to which this state is attached.\n\n        event -\n          The event that caused the finite state machine to run\n\n    onexit -\n      A function which is called upon exit from the state.  Its signature\n      is function(fsm, event) and it is saved in the onexit property of\n      the state object.  (This function is called after the Transition's\n      action function and before the next state's onentry function.)\n\n      In the onexit function:\n\n        fsm -\n          The finite state machine object to which this state is attached.\n\n        event -\n          The event that caused the finite state machine to run\n\n    autoActionsBeforeOnentry -\n    autoActionsAfterOnentry -\n    autoActionsBeforeOnexit -\n    autoActionsAfterOnexit -\n      Automatic actions which take place at the time specified by the\n      property name.  In all cases, the action takes place immediately\n      before or after the specified function.\n\n      The property value for each of these properties is an object which\n      describes some number of functions to invoke on a set of specified\n      objects (typically widgets).\n\n      An example, using autoActionsBeforeOnentry, might look like this:\n\n      \"autoActionsBeforeOnentry\" :\n      {\n        // The name of a function.\n        \"enabled\" :\n        [\n          {\n            // The parameter value, thus \"setEnabled(true);\"\n            \"parameters\" : [ true ],\n\n            // The function would be called on each object:\n            //  this.getObject(\"obj1\").setEnabled(true);\n            //  this.getObject(\"obj2\").setEnabled(true);\n            \"objects\" : [ \"obj1\", \"obj2\" ],\n\n            // And similarly for each object in each specified group.\n            \"groups\"  : [ \"group1\", \"group2\" ]\n          }\n        ],\n\n        // The name of another function.\n        \"visible\" :\n        [\n          {\n            // The parameter value, thus \"setEnabled(true);\"\n            \"parameters\" : [ false ],\n\n            // The function would be called on each object and group, as\n            // described above.\n            \"objects\" : [ \"obj3\", \"obj4\" ],\n            \"groups\"  : [ \"group3\", \"group4\" ]\n          }\n        ]\n      };\n\n    events (required) -\n      A description to the finite state machine of how to handle a\n      particular event, optionally associated with a specific target\n      object on which the event was dispatched.  This should be an object\n      containing one property for each event which is either handled or\n      blocked.  The property name should be the event name.  The property\n      value should be one of:\n\n        (a) qx.util.fsm.FiniteStateMachine.EventHandling.PREDICATE\n\n        (b) qx.util.fsm.FiniteStateMachine.EventHandling.BLOCKED\n\n        (c) a string containing the name of an explicit Transition to use\n\n        (d) an object where each property name is the Friendly Name of an\n            object (meaning that this rule applies if both the event and\n            the event's target object's Friendly Name match), and its\n            property value is one of (a), (b) or (c), above.\n\n      This object is saved in the events property of the state object.\n\n    Additional properties may be provided in stateInfo.  They will not be\n    used by the finite state machine, but will be available via\n    this.getUserData(\"\") during the state's onentry and\n    onexit functions.\n  </pre>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Map"}}
            ]}
          ]}
        ]}
      ]}
    ]},
  {type:"methods-static",children:[
    {type:"method",attributes:{"access":"protected","isStatic":"true","name":"_commonTransformAutoActions"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"actionType"},children:[
          {type:"desc",attributes:{"text":"<p>The name of the action being validated (for debug messages)</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>The property value which is being validated</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Common function for checking the value provided for\nauto actions.</p>\n\n<p>Auto-action property values passed to us look akin to:</p>\n\n<pre class=\"javascript\">\n    {\n      // The name of a function.\n      \"setEnabled\" :\n      [\n        {\n          // The parameter value(s), thus \"setEnabled(true);\"\n          \"parameters\"   : [ true ],\n\n          // The function would be called on each object:\n          //  this.getObject(\"obj1\").setEnabled(true);\n          //  this.getObject(\"obj2\").setEnabled(true);\n          \"objects\" : [ \"obj1\", \"obj2\" ]\n\n          // And similarly for each object in each specified group.\n          \"groups\"  : [ \"group1\", \"group2\" ],\n        }\n      ];\n\n      \"setTextColor\" :\n      [\n        {\n          \"parameters\" : [ \"blue\" ]\n          \"groups\"     : [ \"group3\", \"group4\" ],\n          \"objects\"    : [ \"obj3\", \"obj4\" ]\n        }\n      ];\n    };\n    </pre>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"private","hasError":"true","name":"__transformAutoActionsAfterOnentry"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"42","line":"857"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"private","hasError":"true","name":"__transformAutoActionsAfterOnexit"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"41","line":"887"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"private","hasError":"true","name":"__transformAutoActionsBeforeOnentry"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"43","line":"842"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"private","hasError":"true","name":"__transformAutoActionsBeforeOnexit"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"42","line":"872"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"private","hasError":"true","name":"__transformEvents"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"25","line":"768"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"private","hasError":"true","name":"__transformName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Value passed to setter</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"23","line":"692"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"private","hasError":"true","name":"__transformOnentry"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Function"}},
          {type:"entry",attributes:{"type":"var"}},
          {type:"entry",attributes:{"type":"null"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"26","line":"712"}}
        ]}
      ]},
    {type:"method",attributes:{"access":"private","hasError":"true","name":"__transformOnexit"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Current value</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Function"}},
          {type:"entry",attributes:{"type":"var"}},
          {type:"entry",attributes:{"type":"null"}}
          ]}
        ]},
      {type:"errors",children:[
        {type:"error",attributes:{"msg":"Documentation is missing.","column":"25","line":"740"}}
        ]}
      ]},
    {type:"method",attributes:{"name":"addTransition"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"trans"},children:[
          {type:"desc",attributes:{"text":"<p>An object of class qx.util.fsm.Transition representing a transition\n  which is to be a part of this state.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.util.fsm.Transition"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Add a transition to a state</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getAutoActionsAfterOnentry","fromProperty":"autoActionsAfterOnentry"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>autoActionsAfterOnentry</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsAfterOnentry}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>autoActionsAfterOnentry</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getAutoActionsAfterOnexit","fromProperty":"autoActionsAfterOnexit"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>autoActionsAfterOnexit</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsAfterOnexit}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>autoActionsAfterOnexit</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getAutoActionsBeforeOnentry","fromProperty":"autoActionsBeforeOnentry"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>autoActionsBeforeOnentry</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsBeforeOnentry}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>autoActionsBeforeOnentry</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getAutoActionsBeforeOnexit","fromProperty":"autoActionsBeforeOnexit"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>autoActionsBeforeOnexit</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsBeforeOnexit}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>autoActionsBeforeOnexit</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getEvents","fromProperty":"events"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>events</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #events}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>events</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getName","fromProperty":"name"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>name</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #name}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>name</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getOnentry","fromProperty":"onentry"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>onentry</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #onentry}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>onentry</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getOnexit","fromProperty":"onexit"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>onexit</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #onexit}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>onexit</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initAutoActionsAfterOnentry","fromProperty":"autoActionsAfterOnentry"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>autoActionsAfterOnentry</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>autoActionsAfterOnentry</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsAfterOnentry}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initAutoActionsAfterOnexit","fromProperty":"autoActionsAfterOnexit"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>autoActionsAfterOnexit</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>autoActionsAfterOnexit</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsAfterOnexit}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initAutoActionsBeforeOnentry","fromProperty":"autoActionsBeforeOnentry"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>autoActionsBeforeOnentry</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>autoActionsBeforeOnentry</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsBeforeOnentry}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initAutoActionsBeforeOnexit","fromProperty":"autoActionsBeforeOnexit"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>autoActionsBeforeOnexit</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>autoActionsBeforeOnexit</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsBeforeOnexit}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initEvents","fromProperty":"events"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>events</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>events</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #events}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initName","fromProperty":"name"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>name</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>name</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #name}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initOnentry","fromProperty":"onentry"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>onentry</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>onentry</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #onentry}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initOnexit","fromProperty":"onexit"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>onexit</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>onexit</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #onexit}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetAutoActionsAfterOnentry","fromProperty":"autoActionsAfterOnentry"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>autoActionsAfterOnentry</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsAfterOnentry}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetAutoActionsAfterOnexit","fromProperty":"autoActionsAfterOnexit"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>autoActionsAfterOnexit</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsAfterOnexit}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetAutoActionsBeforeOnentry","fromProperty":"autoActionsBeforeOnentry"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>autoActionsBeforeOnentry</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsBeforeOnentry}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetAutoActionsBeforeOnexit","fromProperty":"autoActionsBeforeOnexit"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>autoActionsBeforeOnexit</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsBeforeOnexit}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetEvents","fromProperty":"events"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>events</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #events}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetName","fromProperty":"name"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>name</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #name}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetOnentry","fromProperty":"onentry"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>onentry</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #onentry}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetOnexit","fromProperty":"onexit"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>onexit</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #onexit}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setAutoActionsAfterOnentry","fromProperty":"autoActionsAfterOnentry"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>autoActionsAfterOnentry</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>autoActionsAfterOnentry</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsAfterOnentry}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setAutoActionsAfterOnexit","fromProperty":"autoActionsAfterOnexit"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>autoActionsAfterOnexit</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>autoActionsAfterOnexit</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsAfterOnexit}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setAutoActionsBeforeOnentry","fromProperty":"autoActionsBeforeOnentry"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>autoActionsBeforeOnentry</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>autoActionsBeforeOnentry</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsBeforeOnentry}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setAutoActionsBeforeOnexit","fromProperty":"autoActionsBeforeOnexit"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>autoActionsBeforeOnexit</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>autoActionsBeforeOnexit</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #autoActionsBeforeOnexit}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setEvents","fromProperty":"events"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>events</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>events</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #events}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setName","fromProperty":"name"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>name</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>name</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #name}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setOnentry","fromProperty":"onentry"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>onentry</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>onentry</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #onentry}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setOnexit","fromProperty":"onexit"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>onexit</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>onexit</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #onexit}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"allowNull":"true","defaultValue":"[Unsupported item type: function]","name":"autoActionsAfterOnentry","propertyType":"new"},children:[
      {type:"desc",attributes:{"text":"<p>Automatic actions to take after return from the state&#8217;s onentry\nfunction.</p>\n\n<p>The value passed to setAutoActionsAfterOnentry() should like something\nakin to:</p>\n\n<pre class=\"javascript\">\n    \"autoActionsAfterOnentry\" :\n    {\n      // The name of a function.  This would become \"setEnabled(\"\n      \"enabled\" :\n      [\n        {\n          // The parameter value, thus \"setEnabled(true);\"\n          \"parameters\" : [ true ],\n\n          // The function would be called on each object:\n          //  this.getObject(\"obj1\").setEnabled(true);\n          //  this.getObject(\"obj2\").setEnabled(true);\n          \"objects\" : [ \"obj1\", \"obj2\" ]\n\n          // And similarly for each object in each specified group.\n          \"groups\"  : [ \"group1\", \"group2\" ],\n        }\n      ];\n    };\n    </pre>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","defaultValue":"[Unsupported item type: function]","name":"autoActionsAfterOnexit","propertyType":"new"},children:[
      {type:"desc",attributes:{"text":"<p>Automatic actions to take after returning from the state&#8217;s onexit\nfunction.</p>\n\n<p>The value passed to setAutoActionsAfterOnexit() should like something\nakin to:</p>\n\n<pre class=\"javascript\">\n    \"autoActionsBeforeOnexit\" :\n    {\n      // The name of a function.  This would become \"setEnabled(\"\n      \"enabled\" :\n      [\n        {\n          // The parameter value, thus \"setEnabled(true);\"\n          \"parameters\" : [ true ],\n\n          // The function would be called on each object:\n          //  this.getObject(\"obj1\").setEnabled(true);\n          //  this.getObject(\"obj2\").setEnabled(true);\n          \"objects\" : [ \"obj1\", \"obj2\" ]\n\n          // And similarly for each object in each specified group.\n          \"groups\"  : [ \"group1\", \"group2\" ],\n        }\n      ];\n    };\n    </pre>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","defaultValue":"[Unsupported item type: function]","name":"autoActionsBeforeOnentry","propertyType":"new"},children:[
      {type:"desc",attributes:{"text":"<p>Automatic actions to take prior to calling the state&#8217;s onentry function.</p>\n\n<p>The value passed to setAutoActionsBeforeOnentry() should like something\nakin to:</p>\n\n<pre class=\"javascript\">\n    \"autoActionsBeforeOnentry\" :\n    {\n      // The name of a function.  This would become \"setEnabled(\"\n      \"enabled\" :\n      [\n        {\n          // The parameter value, thus \"setEnabled(true);\"\n          \"parameters\" : [ true ],\n\n          // The function would be called on each object:\n          //  this.getObject(\"obj1\").setEnabled(true);\n          //  this.getObject(\"obj2\").setEnabled(true);\n          \"objects\" : [ \"obj1\", \"obj2\" ]\n\n          // And similarly for each object in each specified group.\n          \"groups\"  : [ \"group1\", \"group2\" ],\n        }\n      ];\n    };\n    </pre>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","defaultValue":"[Unsupported item type: function]","name":"autoActionsBeforeOnexit","propertyType":"new"},children:[
      {type:"desc",attributes:{"text":"<p>Automatic actions to take prior to calling the state&#8217;s onexit function.</p>\n\n<p>The value passed to setAutoActionsBeforeOnexit() should like something\nakin to:</p>\n\n<pre class=\"javascript\">\n    \"autoActionsBeforeOnexit\" :\n    {\n      // The name of a function.  This would become \"setEnabled(\"\n      \"enabled\" :\n      [\n        {\n          // The parameter value, thus \"setEnabled(true);\"\n          \"parameters\" : [ true ],\n\n          // The function would be called on each object:\n          //  this.getObject(\"obj1\").setEnabled(true);\n          //  this.getObject(\"obj2\").setEnabled(true);\n          \"objects\" : [ \"obj1\", \"obj2\" ]\n\n          // And similarly for each object in each specified group.\n          \"groups\"  : [ \"group1\", \"group2\" ],\n        }\n      ];\n    };\n    </pre>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","name":"events","propertyType":"new"},children:[
      {type:"desc",attributes:{"text":"<p>The object representing handled and blocked events for this state.\nThis is documented in the constructor, and is typically provided\nthrough the constructor&#8217;s stateInfo object, but it is also possible\n(but highly <span class=\"caps\">NOT</span> recommended) to change this dynamically.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","name":"name","propertyType":"new"},children:[
      {type:"desc",attributes:{"text":"<p>The name of this state.  This name may be used as a Transition&#8217;s\nnextState value, or an explicit next state in the &#8216;events&#8217; handling\nlist in a State.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","defaultValue":"[Unsupported item type: function]","name":"onentry","propertyType":"new"},children:[
      {type:"desc",attributes:{"text":"<p>The onentry function for this state.  This is documented in the\nconstructor, and is typically provided through the constructor&#8217;s\nstateInfo object, but it is also possible (but highly <span class=\"caps\">NOT</span> recommended)\nto change this dynamically.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","defaultValue":"[Unsupported item type: function]","name":"onexit","propertyType":"new"},children:[
      {type:"desc",attributes:{"text":"<p>The onexit function for this state.  This is documented in the\nconstructor, and is typically provided through the constructor&#8217;s\nstateInfo object, but it is also possible (but highly <span class=\"caps\">NOT</span> recommended)\nto change this dynamically.</p>"}}
      ]}
    ]}
  ]}