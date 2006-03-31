<%--			
			<TR height='20'>
							
							<TD width='15%' 
								onClick="vediSchermo('Field Selection','DIV_FIELD_SELECTION')" 
								onMouseOver="goIn(this)" 
								onMouseOut="goOut(this)" 
								id='DIV_FIELD_SELECTION' 
								class='LAYMENU'
								title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.FieldSelectionTooltip")%> ">
								<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.FieldSelection") %>
							</TD>
							
						    <TD width='15%' 
						    	onClick="vediSchermo('Conditions','DIV_FIELD_CONDITION')" 
								onMouseOver="goIn(this)" 
								onMouseOut="goOut(this)" 
								id='DIV_FIELD_CONDITION' 
								class='LAYMENU'
								title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.ConditionsTooltip") %>">
								<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Conditions") %>
							</TD>
							
							<TD width='15%' onClick="vediSchermo('Ordering','DIV_FIELD_ORDER_BY')" 
								onMouseOver="goIn(this)" 
								onMouseOut="goOut(this)" 
								id='DIV_FIELD_ORDER_BY' 
								class='LAYMENU'
								title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.OrderingTooltip") %>">
								<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Ordering") %>
							</TD>
							
							<TD width='15%' onClick="vediSchermo('Grouping','DIV_FIELD_GROUP_BY')" 
								onMouseOver="goIn(this)" 
								onMouseOut="goOut(this)"
								id='DIV_FIELD_GROUP_BY'
								class='LAYMENU'
								title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.GroupingTooltip") %>">
								<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Grouping") %>
							</TD>
							
							<TD width='15%' onClick="vediSchermo('ResumeQuery','DIV_RESUME_QUERY')" 
								onMouseOver="goIn(this)" 
								onMouseOut="goOut(this)"
								id='DIV_RESUME_QUERY' 
								class='LAYMENU'
								title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.ResumeTooltip") %>">
								<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Resume") %>
							</TD>
							
							<TD width='15%' onClick="vediSchermo('SaveQuery','DIV_SAVE_QUERY')" 
								onMouseOver="goIn(this)" 
								onMouseOut="goOut(this)"
								id='DIV_SAVE_QUERY' 
								class='LAYMENU'
								title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.SaveTooltip") %>">
								<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Save") %>
							</TD>

							<TD width='1215%.3%' onClick="vediSchermo('Grouping','DIV_EXEC')" 
								onMouseOver="goIn(this)" 
								onMouseOut="goOut(this)" 
								id='DIV_EXEC'
								class='LAYMENU'
								title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.PreviewTooltip") %>">
								<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Preview") %>
							</TD>

						</TR>
--%>
	
	
<div style='visibility:visible;width:100%' class='UITabs'>
	<div class="first-tab-level" style="background-color:#f8f8f8">
		<div style="overflow: hidden;width:100%">
			<div class='tab' id='DIV_FIELD_SELECTION'>
				<a href='javascript:void(0)' 
				   onclick="vediSchermo('Field Selection','DIV_FIELD_SELECTION')" 
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.FieldSelectionTooltip")%> " 
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.FieldSelection") %>
				</a>
			</div>
			<div class='tab' id='DIV_FIELD_CONDITION'>
				<a href='javascript:void(0)' 
				   onclick="vediSchermo('Conditions','DIV_FIELD_CONDITION')" 
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.ConditionsTooltip") %>" 
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Conditions") %>
				</a>
			</div>
			<div class='tab' id='DIV_FIELD_ORDER_BY'>
				<a href='javascript:void(0);' 
				   onclick="vediSchermo('Ordering','DIV_FIELD_ORDER_BY')" 
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.OrderingTooltip") %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Ordering") %>
				</a>
			</div>
			<div class='tab' id='DIV_FIELD_GROUP_BY'>
				<a href='javascript:void(0)' 
				   onclick="vediSchermo('Grouping','DIV_FIELD_GROUP_BY')" 
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.GroupingTooltip") %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Grouping") %>
				</a>
			</div>
			<div class='tab' id='DIV_RESUME_QUERY'>
				<a href='javascript:void(0)' 
				   onclick="vediSchermo('ResumeQuery','DIV_RESUME_QUERY')" 
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.ResumeTooltip") %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Resume") %>
				</a>
			</div>
			<div class='tab' id='DIV_SAVE_QUERY'>
				<a href='javascript:void(0)' 
				   onclick="vediSchermo('SaveQuery','DIV_SAVE_QUERY')" 
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.SaveTooltip") %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Save") %>
				</a>
			</div>
			<div class='tab' id='DIV_EXEC'>
				<a href='javascript:void(0)' 
				   onclick="vediSchermo('Grouping','DIV_EXEC')" 
				   title="<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.PreviewTooltip") %>"
				   style="color:black;"
				   >
					<%=qbeMsg.getMessage(requestContainer,"QBE.Tab.Preview") %>
				</a>
			</div>
		</div>
	</div>
</div>

