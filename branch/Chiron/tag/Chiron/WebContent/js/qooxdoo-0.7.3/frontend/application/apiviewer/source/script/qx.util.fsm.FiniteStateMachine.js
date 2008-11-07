{type:"class",attributes:{"name":"FiniteStateMachine","packageName":"qx.util.fsm","superClass":"qx.core.Target","fullName":"qx.util.fsm.FiniteStateMachine","type":"class"},children:[
  {type:"desc",attributes:{"text":"<p>A finite state machine.</p>\n\n<p>See {@link qx.util.fsm.State} for details on creating States,\nand {@link qx.util.fsm.Transitions} for details on creating\ntransitions between states.</p>"}},
  {type:"constructor",children:[
    {type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"machineName"},children:[
          {type:"desc",attributes:{"text":"<p>The name of this finite state machine</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]}
      ]}
    ]},
  {type:"methods",children:[
    {type:"method",attributes:{"access":"private","name":"__processEvents"},children:[
      {type:"desc",attributes:{"text":"<p>Process all of the events on the event queue.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"private","name":"__run"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"event"},children:[
          {type:"desc",attributes:{"text":"<p>An event that has been dispatched.  The event may be handled (if the\n  current state handles this event type), queued (if the current state\n  blocks this event type), or discarded (if the current state neither\n  handles nor blocks this event type).</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Run the finite state machine to process a single event.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>Whether the event should be disposed.  If it was blocked, we&#8217;ve\n  pushed it back onto the event queue, and it should not be disposed.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Boolean"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"addObject"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"friendlyName"},children:[
          {type:"desc",attributes:{"text":"<p>The friendly name to used for access to the object being added.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"obj"},children:[
          {type:"desc",attributes:{"text":"<p>The object to associate with the specified friendly name</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"groupNames"},children:[
          {type:"desc",attributes:{"text":"<p>An optional list of group names of which this object is a member.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Array"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Add an object (typically a widget) that is to be accessed during state\ntransitions, to the finite state machine.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"addState"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"state"},children:[
          {type:"desc",attributes:{"text":"<p>An object of class qx.util.fsm.State representing a state which is to\n  be a part of this finite state machine.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.util.fsm.State"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Add a state to the finite state machine.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"copyEvent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"event"},children:[
          {type:"desc",attributes:{"text":"<p>The event to be copied</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Copy an event</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The new copy of the provided event</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"qx.event.type.Event"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"displayAllObjects"},children:[
      {type:"desc",attributes:{"text":"<p>Display all of the saved objects and their reverse mappings.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"enqueueEvent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"event"},children:[
          {type:"desc",attributes:{"text":"<p>The event to be enqueued</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"bAddAtHead"},children:[
          {type:"desc",attributes:{"text":"<p>If <i>true</i>, put the event at the head of the queue for immediate\n  processing.  If <i>false</i>, place the event at the tail of the\n  queue so that it receives in-order processing.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Enqueue an event for processing</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"eventListener"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"event"},children:[
          {type:"desc",attributes:{"text":"<p>The event that was dispatched.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Event listener for all event types in the finite state machine</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getDebugFlags","fromProperty":"debugFlags"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>debugFlags</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #debugFlags}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>debugFlags</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getFriendlyName"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"obj"},children:[
          {type:"desc",attributes:{"text":"<p>The object for which the friendly name is desired</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Object"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Get the friendly name of an object.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>If the object has been previously registered via {@link #addObject},\n  then the friendly name of the object is returned; otherwise, null.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"String"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getGroupObjects"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"groupName"},children:[
          {type:"desc",attributes:{"text":"<p>The name of the group for which the member list is desired.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Retrieve the list of objects which have registered, via {@link\naddObject} as being members of the specified group.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>An array containing the friendly names of any objects which are\n  members of the specified group.  The resultant array may be empty.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Array"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getMaxSavedStates","fromProperty":"maxSavedStates"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>maxSavedStates</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #maxSavedStates}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>maxSavedStates</code>.</p>"}},
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
    {type:"method",attributes:{"name":"getNextState","fromProperty":"nextState"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>nextState</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #nextState}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>nextState</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getObject"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"friendlyName"},children:[
          {type:"desc",attributes:{"text":"<p>The friendly name of the object to be retrieved.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Retrieve an object previously saved via {@link #addObject}, using its\nFriendly Name.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The object which has the specified friendly name, or undefined if no\n  object has been associated with that name.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"getPreviousState","fromProperty":"previousState"},children:[
      {type:"desc",attributes:{"text":"<p>Returns the (computed) value of the property <code>previousState</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #previousState}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>(Computed) value of <code>previousState</code>.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
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
    {type:"method",attributes:{"access":"protected","name":"initDebugFlags","fromProperty":"debugFlags"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>debugFlags</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>debugFlags</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #debugFlags}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initMaxSavedStates","fromProperty":"maxSavedStates"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>maxSavedStates</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>maxSavedStates</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #maxSavedStates}.</p>"}},
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
    {type:"method",attributes:{"access":"protected","name":"initNextState","fromProperty":"nextState"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>nextState</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>nextState</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #nextState}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>the default value</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"access":"protected","name":"initPreviousState","fromProperty":"previousState"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>Initial value for property <code>previousState</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Calls the apply method and dispatches the change event of the property <code>previousState</code>\nwith the default value defined by the class developer. This function can\nonly be called from the constructor of a class.</p>\n\n<p>For further details take a look at the property definition: {@link #previousState}.</p>"}},
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
    {type:"method",attributes:{"name":"postponeEvent"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"event"},children:[
          {type:"desc",attributes:{"text":"<p>The event to add to the event queue for processing after state change.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.event.type.Event"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Add the specified event to a list of events to be passed to the next\nstate following state transition.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"pushState"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"bCurrent"},children:[
          {type:"desc",attributes:{"text":"<p>When <i>true</i>, then push the current state onto the stack.  This\n  might be used in a transition, before the state has changed.  When\n  <i>false</i>, then push the previous state onto the stack.  This\n  might be used in an on entry function to save the previous state to\n  return to.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Save the current or previous state on the saved-state stack.  A future\ntransition can then provide, as its nextState value, the class\nconstant:</p>\n\n<code>\n  qx.util.fsm.FiniteStateMachine.StateChange.POP_STATE_STACK\n  </code>\n\n<p>which will cause the next state to be whatever is at the top of the\nsaved-state stack, and remove that top element from the saved-state\nstack.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"removeObject"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"friendlyName"},children:[
          {type:"desc",attributes:{"text":"<p>The friendly name associated with an object, specifying which object\n  is to be removed.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"String"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Remove an object which had previously been added by {@link #addObject}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"replaceState"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"state"},children:[
          {type:"desc",attributes:{"text":"<p>An object of class qx.util.fsm.State representing a state which is to\n  be a part of this finite state machine.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"qx.util.fsm.State"}}
            ]}
          ]},
        {type:"param",attributes:{"name":"bDispose"},children:[
          {type:"desc",attributes:{"text":"<p>If <i>true</i>, then dispose the old state object.  If <i>false</i>,\n  the old state object is returned for disposing by the caller.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"Boolean"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Replace a state in the finite state machine.  This is useful if\ninitially &#8220;dummy&#8221; states are created which load the real state table\nfor a series of operations (and possibly also load the gui associated\nwith the new states at the same time).  Having portions of the finite\nstate machine and their associated gui pages loaded at run time can\nhelp prevent long delays at application start-up time.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The old state object if it was not disposed; otherwise null.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"Object"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetDebugFlags","fromProperty":"debugFlags"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>debugFlags</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #debugFlags}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetMaxSavedStates","fromProperty":"maxSavedStates"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>maxSavedStates</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #maxSavedStates}.</p>"}},
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
    {type:"method",attributes:{"name":"resetNextState","fromProperty":"nextState"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>nextState</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #nextState}.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"resetPreviousState","fromProperty":"previousState"},children:[
      {type:"desc",attributes:{"text":"<p>Resets the user value of the property <code>previousState</code>.</p>\n\n<p>The computed value falls back to the next available value e.g. appearance, init or\ninheritance value depeneding on the property configuration and value availability.</p>\n\n<p>For further details take a look at the property definition: {@link #previousState}.</p>"}},
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
    {type:"method",attributes:{"name":"setDebugFlags","fromProperty":"debugFlags"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>debugFlags</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>debugFlags</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #debugFlags}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setMaxSavedStates","fromProperty":"maxSavedStates"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>maxSavedStates</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>maxSavedStates</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #maxSavedStates}.</p>"}},
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
    {type:"method",attributes:{"name":"setNextState","fromProperty":"nextState"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>nextState</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>nextState</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #nextState}.</p>"}},
      {type:"return",children:[
        {type:"desc",attributes:{"text":"<p>The unmodified incoming value.</p>"}},
        {type:"types",children:[
          {type:"entry",attributes:{"type":"var"}}
          ]}
        ]}
      ]},
    {type:"method",attributes:{"name":"setPreviousState","fromProperty":"previousState"},children:[
      {type:"params",children:[
        {type:"param",attributes:{"name":"value"},children:[
          {type:"desc",attributes:{"text":"<p>New value for property <code>previousState</code>.</p>"}},
          {type:"types",children:[
            {type:"entry",attributes:{"type":"var"}}
            ]}
          ]}
        ]},
      {type:"desc",attributes:{"text":"<p>Sets the user value of the property <code>previousState</code>.</p>\n\n<p>For further details take a look at the property definition: {@link #previousState}.</p>"}},
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
    {type:"method",attributes:{"name":"start"},children:[
      {type:"desc",attributes:{"text":"<p>Start (or restart, after it has terminated) the finite state machine\nfrom the starting state.  The starting state is defined as the first\nstate added to the finite state machine.</p>"}},
      {type:"return",children:[
        {type:"types",children:[
          {type:"entry",attributes:{"type":"void"}}
          ]}
        ]}
      ]}
    ]},
  {type:"properties",children:[
    {type:"property",attributes:{"defaultValue":"7","propertyType":"new","name":"debugFlags","check":"Number"},children:[
      {type:"desc",attributes:{"text":"<p>Debug flags, composed of the bitmask values in {@link #DebugFlags}.</p>\n\n<p>Set the debug flags from the application by or-ing together bits, akin\nto this:</p>\n\n<pre class=\"javascript\">\nvar FSM = qx.util.fsm.FiniteStateMachine;\nfsm.setDebugFlags(FSM.DebugFlags.EVENTS |\n                  FSM.DebugFlags.TRANSITIONS |\n                  FSM.DebugFlags.FUNCTION_DETAIL |\n                  FSM.DebugFlags.OBJECT_NOT_FOUND);\n</pre>"}}
      ]},
    {type:"property",attributes:{"defaultValue":"2","propertyType":"new","name":"maxSavedStates","check":"Number"},children:[
      {type:"desc",attributes:{"text":"<p>The maximum number of states which may pushed onto the state-stack.  It\nis generally a poor idea to have very many states saved on a stack.\nFollowing program logic becomes very difficult, and the code can be\nhighly unmaintainable.  The default should be more than adequate.\nYou&#8217;ve been warned.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"name","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>The name of this finite state machine (for debug messages)</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"nextState","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>The state to which we will be transitioning.  This property is valid\nonly during a Transition&#8217;s ontransition function and a State&#8217;s onexit\nfunction.  At all other times, it is null.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"previousState","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>The previous state of the finite state machine, i.e. the state from\nwhich we most recently transitioned.  Note that this could be the same\nas the current state if a successful transition brought us back to the\nsame state.</p>"}}
      ]},
    {type:"property",attributes:{"allowNull":"true","propertyType":"new","name":"state","check":"String"},children:[
      {type:"desc",attributes:{"text":"<p>The current state of the finite state machine.</p>"}}
      ]}
    ]}
  ]}