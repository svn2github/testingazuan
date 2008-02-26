
<%@ include file="/jsp/commons/portlet_base.jsp"%>

<%
    String save=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=SAVE_OBJECT&NEW_SESSION=TRUE&op=a");
    String read=urlBuilder.getResourceLink(request, "/servlet/AdapterHTTP?ACTION_NAME=SAVE_OBJECT&NEW_SESSION=TRUE&op=b");
    String saveJS=urlBuilder.getResourceLink(request, "js/analiticalmodel/save_viewpoint.js");
%>

<script type="text/javascript">
	var readUrl='<%=read%>';
	var saveUrl='<%=save%>';
	var saveJSUrl='<%=saveJS%>';
</script>


<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/portal_admin.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/form.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/analiticalmodel/table.css")%>' type='text/css' />
<LINK rel='StyleSheet' href='<%=urlBuilder.getResourceLink(request, "css/extjs/ext-all.css")%>' type='text/css' />

<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/extjs/ext-base.js")%>"></script>
<script type="text/javascript" src="<%=urlBuilder.getResourceLink(request, "js/extjs/ext-all-debug.js")%>"></script>

<div class="x-window-header">Prova</div>

<div class="x-content-tab">
    	<form action="/SpagoBI/servlet/AdapterHTTP?ACTION_NAME=SAVE_OBJECT&NEW_SESSION=TRUE&op=b" name="form" id="reports">
        	<fieldset>   
              	<div class="block">
                	<label for="A">Name</label>
                           <input type="text" name="v_name" id="v_name" />
                </div>
            	<div class="block">
	                <label for="C">&nbsp;</label>
            			<input type="button" class="button" id="b_save" value="Save" />
					<label for="C">&nbsp;</label>
            			<input type="button" class="button" id="cancel" value="Cancel" />
                 </div>                
        </fieldset>
    </form>
</div>
</div>
<script>
alert('ee');
Ext.onReady(function() {
alert('dd');
    Ext.get('b_save').on('click', function(){
		alert('ddd');
		//Ext.get('finestra').hide();
     
    });
});    
        

</script>