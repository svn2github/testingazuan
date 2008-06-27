{type:"class",attributes:{"name":"IframeManager","hasWarning":"true","packageName":"qx.ui.embed","superClass":"qx.util.manager.Object","isSingleton":"true","fullName":"qx.ui.embed.IframeManager","type":"class"},children:[{type:"desc",attributes:{"text":"<p>This singleton manages multiple instances of qx.ui.embed.Iframe.</p>\n\n<p>The problem: When dragging over an iframe then all mouse events will be\npassed to the document of the iframe, not the main document.</p>\n\n<p>The solution: In order to be able to track mouse events over iframes, this\nmanager will block all iframes during a drag with a glasspane.</p>"}},{type:"constructor",children:[{type:"method",attributes:{"isCtor":"true","name":"ctor"}}]},{type:"methods",children:[{type:"method",attributes:{"hasError":"true","name":"handleMouseDown"},children:[{type:"params",children:[{type:"param",attributes:{"name":"evt"},children:[{type:"types",children:[{type:"entry",attributes:{"type":"Event"}}]}]}]},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>evt</code> is not documented.","column":"23","line":"73"}},{type:"error",attributes:{"msg":"Documentation is missing.","column":"23","line":"73"}}]}]},{type:"method",attributes:{"hasError":"true","name":"handleMouseUp"},children:[{type:"params",children:[{type:"param",attributes:{"name":"evt"},children:[{type:"types",children:[{type:"entry",attributes:{"type":"Event"}}]}]}]},{type:"return",children:[{type:"types",children:[{type:"entry",attributes:{"type":"void"}}]}]},{type:"errors",children:[{type:"error",attributes:{"msg":"Parameter <code>evt</code> is not documented.","column":"21","line":"91"}},{type:"error",attributes:{"msg":"Documentation is missing.","column":"21","line":"91"}}]}]}]},{type:"methods-static",children:[{type:"method",attributes:{"isStatic":"true","name":"getInstance"},children:[{type:"desc",attributes:{"text":"<p>Returns a singleton instance of this class. On the first call the class\nis instantiated by calling the constructor with no arguments. All following\ncalls will return this instance.</p>\n\n<p>This method has been added by setting the &#8220;type&#8221; key in the class definition\n({@link qx.Class#define}) to &#8220;singleton&#8221;.</p>"}},{type:"return",children:[{type:"desc",attributes:{"text":"<p>The singleton instance of this class.</p>"}},{type:"types",children:[{type:"entry",attributes:{"type":"qx.ui.embed.IframeManager"}}]}]}]}]}]}