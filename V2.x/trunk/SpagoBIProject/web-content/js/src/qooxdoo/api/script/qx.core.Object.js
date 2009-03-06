{type:"class",attributes:{"name":"Object","packageName":"qx.core","childClasses":"qx.application.AbstractGui,qx.application.Native,qx.bom.Font,qx.bom.History,qx.dev.Tokenizer,qx.dev.unit.TestCase,qx.dev.unit.TestFunction,qx.dev.unit.TestResult,qx.dev.unit.TestSuite,qx.event.Command,qx.event.Timer,qx.event.dispatch.AbstractBubbling,qx.event.dispatch.Direct,qx.event.dispatch.MouseCapture,qx.event.handler.Appear,qx.event.handler.Application,qx.event.handler.Capture,qx.event.handler.DomReady,qx.event.handler.DragDrop,qx.event.handler.Element,qx.event.handler.Focus,qx.event.handler.Iframe,qx.event.handler.Input,qx.event.handler.Keyboard,qx.event.handler.Mouse,qx.event.handler.Object,qx.event.handler.UserAction,qx.event.handler.Window,qx.event.message.Bus,qx.event.message.Message,qx.event.type.Event,qx.fx.Base,qx.fx.queue.Manager,qx.fx.queue.Queue,qx.html.Element,qx.io.remote.Exchange,qx.io.remote.Request,qx.io.remote.RequestQueue,qx.io.remote.Rpc,qx.io.remote.transport.Abstract,qx.io2.HttpRequest,qx.legacy.application.Gui,qx.legacy.dev.TimeTracker,qx.legacy.event.handler.EventHandler,qx.legacy.event.handler.FocusHandler,qx.legacy.event.handler.KeyEventHandler,qx.legacy.io.image.Manager,qx.legacy.io.image.Preloader,qx.legacy.io.image.PreloaderManager,qx.legacy.io.image.PreloaderSystem,qx.legacy.log.Filter,qx.legacy.log.LogEventProcessor,qx.legacy.theme.manager.Appearance,qx.legacy.theme.manager.Icon,qx.legacy.theme.manager.Meta,qx.legacy.theme.manager.Widget,qx.legacy.ui.core.Border,qx.legacy.ui.core.Font,qx.legacy.ui.core.Widget,qx.legacy.ui.layout.impl.LayoutImpl,qx.legacy.ui.selection.RadioManager,qx.legacy.ui.selection.Selection,qx.legacy.ui.selection.SelectionManager,qx.legacy.ui.table.celleditor.CheckBox,qx.legacy.ui.table.celleditor.ComboBox,qx.legacy.ui.table.celleditor.Dynamic,qx.legacy.ui.table.celleditor.PasswordField,qx.legacy.ui.table.celleditor.TextField,qx.legacy.ui.table.cellrenderer.Abstract,qx.legacy.ui.table.columnmodel.Basic,qx.legacy.ui.table.columnmodel.resizebehavior.Abstract,qx.legacy.ui.table.headerrenderer.Default,qx.legacy.ui.table.model.Abstract,qx.legacy.ui.table.pane.Model,qx.legacy.ui.table.rowrenderer.Default,qx.legacy.ui.table.selection.Manager,qx.legacy.ui.table.selection.Model,qx.legacy.ui.tree.TreeRowStructure,qx.legacy.ui.util.column.Widths,qx.legacy.util.GuiBuilder,qx.legacy.util.IntegerRange,qx.legacy.util.NativeWindow,qx.legacy.util.ObjectManager,qx.legacy.util.ValueManager,qx.locale.Manager,qx.log.appender.Element,qx.test.event.MockBubblingHandler,qx.theme.manager.Appearance,qx.theme.manager.Decoration,qx.theme.manager.Icon,qx.theme.manager.Meta,qx.ui.core.EventHandler,qx.ui.core.FocusHandler,qx.ui.core.LayoutItem,qx.ui.core.selection.Abstract,qx.ui.decoration.Background,qx.ui.decoration.Beveled,qx.ui.decoration.Grid,qx.ui.decoration.Single,qx.ui.decoration.Uniform,qx.ui.form.RadioGroup,qx.ui.layout.Abstract,qx.ui.menu.Manager,qx.ui.popup.Manager,qx.ui.progressive.State,qx.ui.progressive.model.Abstract,qx.ui.progressive.renderer.Abstract,qx.ui.progressive.renderer.table.Widths,qx.ui.progressive.renderer.table.cell.Abstract,qx.ui.progressive.structure.Abstract,qx.ui.table.celleditor.CheckBox,qx.ui.table.celleditor.ComboBox,qx.ui.table.celleditor.Dynamic,qx.ui.table.celleditor.PasswordField,qx.ui.table.celleditor.SelectBox,qx.ui.table.celleditor.TextField,qx.ui.table.cellrenderer.Abstract,qx.ui.table.columnmodel.Basic,qx.ui.table.columnmodel.resizebehavior.Abstract,qx.ui.table.headerrenderer.Default,qx.ui.table.model.Abstract,qx.ui.table.pane.Model,qx.ui.table.rowrenderer.Default,qx.ui.table.selection.Manager,qx.ui.table.selection.Model,qx.ui.tooltip.Manager,qx.ui.window.Manager,qx.util.DeferredCall,qx.util.DeferredCallManager,qx.util.ObjectPool,qx.util.Template,qx.util.ValueManager,qx.util.format.DateFormat,qx.util.format.NumberFormat,qx.util.fsm.FiniteStateMachine,qx.util.fsm.State,qx.util.fsm.Transition,qx.util.range.Range,spagobi.data.ArrayDataReader,spagobi.data.DataProxy,spagobi.data.DataReader,spagobi.data.DataStore,spagobi.data.MemoryDataProxy,spagobi.data.Record","fullName":"qx.core.Object","type":"class"},children:[{type:"desc",attributes:{"text":"<p>The qooxdoo root class. All other classes are direct or indirect subclasses of this one.</p>\n\n<p>This class contains methods for:</p>\n\n<ul>\n<li>object management (creation and destruction)</li>\n<li>interfaces for event system</li>\n<li>generic setter/getter support</li>\n<li>interfaces for logging console</li>\n<li>user friendly OO interfaces like {@link #self} or {@link #base}</li>\n</ul>"}},{type:"constructor",children:[{type:"method",attributes:{"isCtor":"true","name":"ctor"},children:[{type:"desc",attributes:{"text":"<p>Create a new instance</p>"}}]}]},{type:"methods",children:[{type:"method",attributes:{"access":"protected","name":"_disposeArray"},children:[{type:"params",children:[{type:"param",attributes:{"name":"field"},children:[{type:"desc",attributes:{"text":"<p>Name of the field which refers to the array</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]}]},{type:"desc",attributes:{"text":"<p>Disposes all members of the given array and deletes\nthe field which refers to the array afterwards.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"access":"protected","name":"_disposeFields"},children:[{type:"params",children:[{type:"param",attributes:{"name":"varargs"},children:[{type:"desc",attributes:{"text":"<p>List of fields to dispose</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"arguments"}}]}]}]},{type:"desc",attributes:{"text":"<p>Disconnects given fields from instance.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"access":"protected","name":"_disposeMap"},children:[{type:"params",children:[{type:"param",attributes:{"name":"field"},children:[{type:"desc",attributes:{"text":"<p>Name of the field which refers to the array</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]}]},{type:"desc",attributes:{"text":"<p>Disposes all members of the given map and deletes\nthe field which refers to the map afterwards.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"access":"protected","name":"_disposeObjects"},children:[{type:"params",children:[{type:"param",attributes:{"name":"varargs"},children:[{type:"desc",attributes:{"text":"<p>List of fields (which store objects) to dispose</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"arguments"}}]}]}]},{type:"desc",attributes:{"text":"<p>Disconnects and disposes given objects from instance.\nOnly works with qx.core.Object based objects e.g. Widgets.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"addListener"},children:[{type:"params",children:[{type:"param",attributes:{"name":"type"},children:[{type:"desc",attributes:{"text":"<p>name of the event type</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]},{type:"param",attributes:{"name":"listener"},children:[{type:"desc",attributes:{"text":"<p>event callback function</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Function"}}]}]},{type:"param",attributes:{"defaultValue":"null","name":"self"},children:[{type:"desc",attributes:{"text":"<p>reference to the &#8216;this&#8217; variable inside the callback</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Object"}}]}]},{type:"param",attributes:{"defaultValue":"false","name":"capture"},children:[{type:"desc",attributes:{"text":"<p>Whether to attach the event to the\n        capturing phase of the bubbling phase of the event. The default is\n        to attach the event handler to the bubbling phase.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"desc",attributes:{"text":"<p>Add event listener to this object.</p>"}}]},{type:"method",attributes:{"name":"addListenerOnce"},children:[{type:"params",children:[{type:"param",attributes:{"name":"type"},children:[{type:"desc",attributes:{"text":"<p>name of the event type</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]},{type:"param",attributes:{"name":"listener"},children:[{type:"desc",attributes:{"text":"<p>event callback function</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Function"}}]}]},{type:"param",attributes:{"defaultValue":"window","name":"self"},children:[{type:"desc",attributes:{"text":"<p>reference to the &#8216;this&#8217; variable inside the callback</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Object"}}]}]},{type:"param",attributes:{"defaultValue":"false","name":"capture"},children:[{type:"desc",attributes:{"text":"<p>Whether to attach the event to the\n        capturing phase of the bubbling phase of the event. The default is\n        to attach the event handler to the bubbling phase.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"desc",attributes:{"text":"<p>Add event listener to this object, which is only called once. After the\nlistener is called the event listener gets removed.</p>"}}]},{type:"method",attributes:{"name":"base"},children:[{type:"params",children:[{type:"param",attributes:{"name":"args"},children:[{type:"desc",attributes:{"text":"<p>the arguments variable of the calling method</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"arguments"}}]}]},{type:"param",attributes:{"name":"varags"},children:[{type:"desc",attributes:{"text":"<p>variable number of arguments passed to the overwritten function</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Call the same method of the super class.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>the return value of the method of the base class.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"clone"},children:[{type:"desc",attributes:{"text":"<p>Returns a clone of this object. Copies over all user configured\nproperty values. Do not configure a parent nor apply the appearance\nstyles directly.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>The clone</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.core.Object"}}]}]}]},{type:"method",attributes:{"name":"debug"},children:[{type:"params",children:[{type:"param",attributes:{"name":"msg"},children:[{type:"desc",attributes:{"text":"<p>the message to log. If this is not a string, the\n         object dump will be logged.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Logs a debug message.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"dispatchEvent"},children:[{type:"params",children:[{type:"param",attributes:{"name":"evt"},children:[{type:"desc",attributes:{"text":"<p>event to dispatch</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.event.type.Event"}}]}]}]},{type:"desc",attributes:{"text":"<p>Dispatch an event on this object</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>whether the event default was prevented or not.\n    Returns true, when the event was <span class=\"caps\">NOT</span> prevented.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"method",attributes:{"name":"dispose"},children:[{type:"desc",attributes:{"text":"<p>Dispose this object</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"error"},children:[{type:"params",children:[{type:"param",attributes:{"name":"msg"},children:[{type:"desc",attributes:{"text":"<p>the message to log. If this is not a string, the\n     object dump will be logged.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Logs an error message.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"fireDataEvent"},children:[{type:"params",children:[{type:"param",attributes:{"name":"type"},children:[{type:"desc",attributes:{"text":"<p>Event type to fire</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]},{type:"param",attributes:{"name":"data"},children:[{type:"desc",attributes:{"text":"<p>User defined data attached to the event object</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]},{type:"param",attributes:{"defaultValue":"null","name":"oldData"},children:[{type:"desc",attributes:{"text":"<p>The event&#8217;s old data (optional)</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]},{type:"param",attributes:{"defaultValue":"false","name":"cancelable"},children:[{type:"desc",attributes:{"text":"<p>Whether or not an event can have its default\n    action prevented. The default action can either be the browser&#8217;s\n    default action of a native event (e.g. open the context menu on a\n    right click) or the default action of a qooxdoo class (e.g. close\n    the window widget). The default action can be prevented by calling\n    {@link #preventDefault}</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"desc",attributes:{"text":"<p>Creates and dispatches an non-bubbling data event on this object.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>whether the event default was prevented or not.\n    Returns true, when the event was <span class=\"caps\">NOT</span> prevented.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"method",attributes:{"name":"fireEvent"},children:[{type:"params",children:[{type:"param",attributes:{"name":"type"},children:[{type:"desc",attributes:{"text":"<p>Event type to fire</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]},{type:"param",attributes:{"defaultValue":"qx.event.type.Event","name":"clazz"},children:[{type:"desc",attributes:{"text":"<p>The event class</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Class"}}]}]},{type:"param",attributes:{"defaultValue":"null","name":"args"},children:[{type:"desc",attributes:{"text":"<p>Arguments, which will be passed to\n      the event&#8217;s init method.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Array"}}]}]}]},{type:"desc",attributes:{"text":"<p>Creates and dispatches an event on this object.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>whether the event default was prevented or not.\n    Returns true, when the event was <span class=\"caps\">NOT</span> prevented.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"method",attributes:{"name":"fireNonBubblingEvent"},children:[{type:"params",children:[{type:"param",attributes:{"name":"type"},children:[{type:"desc",attributes:{"text":"<p>Event type to fire</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]},{type:"param",attributes:{"defaultValue":"qx.event.type.Event","name":"clazz"},children:[{type:"desc",attributes:{"text":"<p>The event class</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Class"}}]}]},{type:"param",attributes:{"defaultValue":"null","name":"args"},children:[{type:"desc",attributes:{"text":"<p>Arguments, which will be passed to\n      the event&#8217;s init method.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Array"}}]}]}]},{type:"desc",attributes:{"text":"<p>Create an event object and dispatch it on this object.\nThe event dispached with this method does never bubble! Use only if you\nare sure that bubbling is not required.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>whether the event default was prevented or not.\n    Returns true, when the event was <span class=\"caps\">NOT</span> prevented.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"method",attributes:{"name":"get"},children:[{type:"params",children:[{type:"param",attributes:{"name":"prop"},children:[{type:"desc",attributes:{"text":"<p>Name of the property.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]}]},{type:"desc",attributes:{"text":"<p>Returns the value of the given property.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>The value of the value</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"getUserData"},children:[{type:"params",children:[{type:"param",attributes:{"name":"key"},children:[{type:"desc",attributes:{"text":"<p>the key</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]}]},{type:"desc",attributes:{"text":"<p>Load user defined data from the object</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>the user data</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Object"}}]}]}]},{type:"method",attributes:{"name":"hasListener"},children:[{type:"params",children:[{type:"param",attributes:{"name":"type"},children:[{type:"desc",attributes:{"text":"<p>name of the event type</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]},{type:"param",attributes:{"defaultValue":"false","name":"capture"},children:[{type:"desc",attributes:{"text":"<p>Whether to check for listeners of\n        the bubbling or of the capturing phase.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"desc",attributes:{"text":"<p>Check if there are one or more listeners for an event type.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>Whether the object has a listener of the given type.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"method",attributes:{"name":"info"},children:[{type:"params",children:[{type:"param",attributes:{"name":"msg"},children:[{type:"desc",attributes:{"text":"<p>the message to log. If this is not a string, the\n     object dump will be logged.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Logs an info message.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"isDisposed"},children:[{type:"desc",attributes:{"text":"<p>Returns true if the object is disposed.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>whether the object has been disposed</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"method",attributes:{"name":"removeListener"},children:[{type:"params",children:[{type:"param",attributes:{"name":"type"},children:[{type:"desc",attributes:{"text":"<p>name of the event type</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]},{type:"param",attributes:{"name":"listener"},children:[{type:"desc",attributes:{"text":"<p>event callback function</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Function"}}]}]},{type:"param",attributes:{"defaultValue":"null","name":"self"},children:[{type:"desc",attributes:{"text":"<p>reference to the &#8216;this&#8217; variable inside the callback</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Object"}}]}]},{type:"param",attributes:{"name":"capture"},children:[{type:"desc",attributes:{"text":"<p>Whether to remove the event listener of\n  the bubbling or of the capturing phase.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Boolean"}}]}]}]},{type:"desc",attributes:{"text":"<p>Remove event listener from this object</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"reset"},children:[{type:"params",children:[{type:"param",attributes:{"name":"prop"},children:[{type:"desc",attributes:{"text":"<p>Name of the property.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]}]},{type:"desc",attributes:{"text":"<p>Resets the value of the given property.</p>"}}]},{type:"method",attributes:{"name":"self"},children:[{type:"params",children:[{type:"param",attributes:{"name":"args"},children:[{type:"desc",attributes:{"text":"<p>the arguments variable of the calling method</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"arguments"}}]}]}]},{type:"desc",attributes:{"text":"<p>Returns the static class (to access static members of this class)</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>the return value of the method of the base class.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"method",attributes:{"name":"serialize"},children:[{type:"desc",attributes:{"text":"<p>Returns a json map of the object configuration.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>The json result</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Map"}}]}]}]},{type:"method",attributes:{"name":"set"},children:[{type:"params",children:[{type:"param",attributes:{"name":"data"},children:[{type:"desc",attributes:{"text":"<p>a map of property values. The key is the name of the property.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Map"}},{type:"entry",attributes:{"type":"String"}}]}]},{type:"param",attributes:{"defaultValue":"","name":"value"},children:[{type:"desc",attributes:{"text":"<p>the value, only used when <code>data</code> is a string.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Sets multiple properties at once by using a property list or\nsets one property and its value by the first and second argument.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>this instance.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Object"}}]}]}]},{type:"method",attributes:{"name":"setUserData"},children:[{type:"params",children:[{type:"param",attributes:{"name":"key"},children:[{type:"desc",attributes:{"text":"<p>the key</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]},{type:"param",attributes:{"name":"value"},children:[{type:"desc",attributes:{"text":"<p>the value of the user data</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Object"}}]}]}]},{type:"desc",attributes:{"text":"<p>Store user defined data inside the object.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"toHashCode"},children:[{type:"desc",attributes:{"text":"<p>Return unique hash code of object</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>unique hash code of the object</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"Integer"}}]}]}]},{type:"method",attributes:{"name":"toString"},children:[{type:"desc",attributes:{"text":"<p>Returns a string represantation of the qooxdoo object.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>string representation of the object</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"String"}}]}]}]},{type:"method",attributes:{"name":"trace"},children:[{type:"desc",attributes:{"text":"<p>Prints the current stak trace</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]},{type:"method",attributes:{"name":"warn"},children:[{type:"params",children:[{type:"param",attributes:{"name":"msg"},children:[{type:"desc",attributes:{"text":"<p>the message to log. If this is not a string, the\n     object dump will be logged.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"var"}}]}]}]},{type:"desc",attributes:{"text":"<p>Logs a warning message.</p>"}},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]}]}]}]}